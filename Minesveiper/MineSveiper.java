import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.geometry.Pos;



public class MineSveiper extends Application {

    private Stage teater;
    private Brett brettet;
    private GridPane rutenett;
    private GUIRute[][] guiRuter;
    //private HBox knappeboks;
    private VBox kulisser;
    private Text minerTekst;
    private Text markerteTekst;
    private Text vunnetTekst;
    private double[] vanskelighetsgrader = {0.06, 0.10, 0.14};
    private double vanskelighetsgrad = 0.06; // endre
    private int maksGrad;
    private int antallSpill = 0;
    private int antallVinn = 0;
    private int antallMarkerteMiner;
    private int antallMiner;

    @Override
    public void start(Stage stage) {
        teater = stage;
        //Pane kulisser = new Pane();
        kulisser = new VBox(10);
        HBox knappeboks = lagKnappeBoks();
        HBox radioKnapper = lagRadioKnappeBoks();
        HBox displayBoks = lagDisplayBoks();
        kulisser.getChildren().addAll(knappeboks, radioKnapper, displayBoks);
        Scene scene = new Scene(kulisser);
        teater.setScene(scene);
        teater.sizeToScene();
        teater.show();
    }


    public HBox lagKnappeBoks() {
        HBox knappeboks = new HBox(10);
        knappeboks.setAlignment(Pos.CENTER);
        Button liten = new Button("Small");
        liten.setOnMouseClicked(e -> lagRuter(10, 10));
        Button medium = new Button("Medium");
        medium.setOnMouseClicked(e -> lagRuter(15, 15));
        Button stor = new Button("Large");
        stor.setOnMouseClicked(e -> lagRuter(15, 25));
        knappeboks.getChildren().addAll(liten, medium, stor);
        return knappeboks;
    }

    public HBox lagRadioKnappeBoks() {
        HBox radioKnapper=  new HBox(10);
        radioKnapper.setAlignment(Pos.CENTER);
        ToggleGroup group = new ToggleGroup();
        RadioButton easy = new RadioButton("Easy");
        easy.setToggleGroup(group);
        easy.setSelected(true);
        easy.setOnAction(e -> settVanskelighetsgrad(vanskelighetsgrader[0]));
        RadioButton medium = new RadioButton("Medium");
        medium.setToggleGroup(group);
        medium.setOnAction(e -> settVanskelighetsgrad(vanskelighetsgrader[1]));
        RadioButton hard = new RadioButton("Hard");
        hard.setToggleGroup(group);
        hard.setOnAction(e -> settVanskelighetsgrad(vanskelighetsgrader[2]));
        radioKnapper.getChildren().addAll(easy, medium, hard);
        return radioKnapper;
    }


    public HBox lagDisplayBoks() {
        Text t1 = new Text("marked: ");
        StackPane markerte = new StackPane();
        Rectangle rekt1 = new Rectangle(20, 20);
        rekt1.setFill(Color.BLACK);
        markerteTekst = new Text("0");
        markerteTekst.setStroke(Color.WHITE);
        markerte.getChildren().addAll(rekt1, markerteTekst);
        Text t2 = new Text(" mines: ");
        StackPane miner = new StackPane();
        Rectangle rekt2 = new Rectangle(20, 20);
        rekt2.setFill(Color.BLACK);
        minerTekst = new Text("" + antallMiner);
        minerTekst.setStroke(Color.WHITE);
        miner.getChildren().addAll(rekt2, minerTekst);
        Text t3 = new Text(" wins: ");
        StackPane wins = new StackPane();
        Rectangle rekt3 = new Rectangle(50, 20);
        rekt3.setFill(Color.BLACK);
        vunnetTekst = new Text("0/0");
        vunnetTekst.setStroke(Color.WHITE);
        wins.getChildren().addAll(rekt3, vunnetTekst);
        HBox displ = new HBox(10);
        displ.setAlignment(Pos.CENTER);
        displ.getChildren().addAll(t1, markerte, t2, miner, t3, wins);
        return displ;
    }

    public void settVanskelighetsgrad(double grad) {
        vanskelighetsgrad = grad;
    }

    public void lagRuter(int rader, int kolonner) {
        if (rutenett != null) {
            kulisser.getChildren().remove(rutenett);
            antallMarkerteMiner = 0;
        }
        vunnetTekst.setText(antallVinn + "/" + antallSpill);
        antallSpill++;
        brettet = new Brett(rader, kolonner, vanskelighetsgrad);
        antallMiner = brettet.hentAntallMiner();
        rutenett = new GridPane();
        guiRuter = new GUIRute[rader][kolonner];
        int str = 400/rader;
        maksGrad = rader/4;
        Rute[][] rutene = brettet.hentRutenett();
        for (int i = 0; i < rutene.length; i++) {
            for (int j = 0; j < rutene[i].length; j++) {
                GUIRute r = new GUIRute(rutene[i][j].erMine(), rutene[i][j], str);
                guiRuter[i][j] = r;
                rutenett.add(r, j, i);
            }
        }
        settNaboer();
        rutenett.setGridLinesVisible(true);
        kulisser.getChildren().add(rutenett);
        minerTekst.setText("" + antallMiner);
        markerteTekst.setText("" + antallMarkerteMiner);
        teater.sizeToScene();
    }

    private void settNaboer() {
        for (int i = 0; i < guiRuter.length; i++) {
            for (int j = 0; j < guiRuter[i].length; j++) {
                if (i > 0) {
                    guiRuter[i][j].settNaboer(guiRuter[i-1][j]); // nord
                    if (j > 0) {
                        guiRuter[i][j].settNaboer(guiRuter[i-1][j-1]); // nord-v
                    }
                    if (j < guiRuter[i].length-1) {
                        guiRuter[i][j].settNaboer(guiRuter[i-1][j+1]); // nord-oe
                    }
                }
                if (j > 0) {
                    guiRuter[i][j].settNaboer(guiRuter[i][j-1]); // vest
                }
                if (j < guiRuter[i].length-1) {
                    guiRuter[i][j].settNaboer(guiRuter[i][j+1]); // oest
                }
                if (i < guiRuter.length-1) {
                    guiRuter[i][j].settNaboer(guiRuter[i+1][j]); // soer
                    if (j > 0) {
                        guiRuter[i][j].settNaboer(guiRuter[i+1][j-1]); // soer-v
                    }
                    if (j < guiRuter[i].length-1) {
                        guiRuter[i][j].settNaboer(guiRuter[i+1][j+1]); // soer-oe
                    }
                }
            }
        }
    }

    public boolean sjekkOver() {
        boolean over = false;
        if (brettet.gameOver()) {
            //System.out.println("Du har tapt!");
            visBrett(false);
            over = true;
            vunnetTekst.setText(antallVinn + "/" + antallSpill);
        } else if (brettet.harVunnet()) {
            visBrett(true);
            over = true;
            antallVinn++;
            //System.out.println("Du har vunnet " + antallVinn + "/" + antallSpill + " spill");
            vunnetTekst.setText(antallVinn + "/" + antallSpill);
        }
        return over;
    }

    public void visBrett(boolean vunnet) {
        for (int i = 0; i < guiRuter.length; i++) {
            for (int j = 0; j < guiRuter[i].length; j++) {
                guiRuter[i][j].ferdig(vunnet);
            }
        }
    }


    class GUIRute extends StackPane {

        private Button knapp;
        private Rute ruten;
        private Rectangle rekt;
        private int miner = -1;
        private GUIRute[] naboer = new GUIRute[8];
        private boolean aapnet;
        private boolean hoyreklikket = false;



        public GUIRute(boolean mine, Rute ruten, int storrelse) {
            super();
            this.ruten = ruten;
            knapp = new Button();
            knapp.setOnMouseClicked(e -> trykket(e, 0));
            knapp.setMinSize(storrelse, storrelse);
            if (mine) {
                rekt = new Rectangle((storrelse-2), (storrelse-2), Color.RED);
                getChildren().addAll(rekt, knapp);
            } else {
                miner = ruten.hentMineNaboer();
                Text tekst = null;
                if (miner != 0) {
                    tekst = new Text(" " + miner);
                }else {
                    tekst = new Text("  ");
                }
                tekst.setFont(new Font(20));
                rekt =new Rectangle(storrelse, storrelse, Color.WHITE);
                getChildren().addAll(rekt, tekst, knapp);
            }
        }

        public void settNaboer(GUIRute nabo) {
            for (int i = 0; i < naboer.length; i++) {
                if (naboer[i] == null) {
                    naboer[i] = nabo;
                    return;
                }
            }
        }


        public void ferdig(boolean vunnet) {
            knapp.setDisable(true);
            if (vunnet && rekt.getFill() == Color.RED) {
                rekt.setFill(Color.GREEN);
            }
        }

        public void aapne(int grad) {
            if (grad > maksGrad || ruten.erMine() || brettet.harVunnet() || brettet.gameOver()) {
                return;
            }
            if (grad != 0 && !aapnet) {
                if (ruten.hentMineNaboer() > 0) {
                    trykket(null, maksGrad);
                } else {
                    trykket(null, grad+1);
                }
            }
            if (ruten.hentMineNaboer() == 0) {
                for (GUIRute r: naboer) {
                    if (r != null) {
                        r.aapne(grad + 1);
                    }
                }
            }
        }

        public void trykket(MouseEvent e, int grad) {
            if (e != null && e.getButton() == MouseButton.SECONDARY) {
                if (hoyreklikket) {
                    knapp.setText("");
                    hoyreklikket = false;
                    antallMarkerteMiner--;
                } else {
                    knapp.setText("x");
                    hoyreklikket = true;
                    antallMarkerteMiner++;
                }
                markerteTekst.setText("" + antallMarkerteMiner);
                return;
            }
            getChildren().remove(knapp);
            brettet.aapne(ruten.hentRad(), ruten.hentKol());
            aapnet = true;
            if (!sjekkOver() && grad == 0) {
                aapne(grad);
            }
        }
    }

}
