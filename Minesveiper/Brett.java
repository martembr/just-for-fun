import java.util.Random;
import java.util.Scanner;


class Brett {

    private Rute[][] rutenett;
    private int rader, kol;
    private int antallMiner;
    private int antallApnet;
    private double vanskelighetsgrad;
    private boolean gameOver;
    private boolean vunnet;

    public Brett(int rader, int kol, double vanskelighetsgrad) {
        this.rader = rader;
        this.kol = kol;
        this.vanskelighetsgrad = vanskelighetsgrad;
        rutenett = new Rute[rader][kol];
        opprettRuter();
        finnNaboer();
    }

    private void opprettRuter() {
        Random rand = new Random();
        int grense = (int) (1/vanskelighetsgrad);
        for (int i = 0; i < rader; i++) {
            for (int j = 0; j < kol; j++) {
                if (rand.nextInt(grense) == 0) {
                    rutenett[i][j] = new MineRute(this, i, j);
                    antallMiner++;
                } else {
                    rutenett[i][j] = new Rute(this, i, j);
                }
            }
        }
    }

    public int hentAntallMiner() {
        return antallMiner;
    }


	public Rute[][] hentRutenett() {
		return rutenett;
	}

    public boolean harVunnet() {
        return vunnet;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public void settGameOver() {
        gameOver = true;
    }

    public void spill() {
        System.out.println("Antall miner: " + antallMiner);
        skrivUt();
        Scanner sc = new Scanner(System.in);
        while (!gameOver && !vunnet) {
            System.out.println("Skriv inn rad, kol:");
            int radInp = -1;
            int kolInp = -1;
            while (radInp < 0 || kolInp < 0) {
                String brukerinp = sc.nextLine();
                String[] indekser = brukerinp.split(", ");
                try {
                    radInp = Integer.parseInt(indekser[0]);
                    kolInp = Integer.parseInt(indekser[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Ikke tall");
                    System.out.println("Bruk format <rad, kol>");
                }
            }
            aapne(radInp, kolInp);
            skrivUt();
        }
        if (vunnet) {
            System.out.println("Du har vunnet");
            System.out.println("Antall miner " + antallMiner);
        } else {
            System.out.println("Du har tapt");
            System.out.println("Antall miner " + antallMiner);
        }
    }


    public void aapne(int rad, int kolonne) {
        if (rad >= 0 && rad <= rader-1 && kolonne >= 0 && kolonne <= kol-1) {
            rutenett[rad][kolonne].aapne();
            antallApnet++;
            if (rader*kol - antallApnet == antallMiner) {
                vunnet = true;
            }
        } else {
            System.out.println("Utenfor rutenettet");
        }
    }

    private void finnNaboer() {
        for (int i = 0; i < rutenett.length; i++) {
            for (int j = 0; j < rutenett[i].length; j++) {
                if (i > 0) {
                    rutenett[i][j].settNaboer(rutenett[i-1][j]); // nord
                    if (j > 0) {
                        rutenett[i][j].settNaboer(rutenett[i-1][j-1]); // nord-v
                    }
                    if (j < rutenett[i].length-1) {
                        rutenett[i][j].settNaboer(rutenett[i-1][j+1]); // nord-oe
                    }
                }
                if (j > 0) {
                    rutenett[i][j].settNaboer(rutenett[i][j-1]); // vest
                }
                if (j < rutenett[i].length-1) {
                    rutenett[i][j].settNaboer(rutenett[i][j+1]); // oest
                }
                if (i < rutenett.length-1) {
                    rutenett[i][j].settNaboer(rutenett[i+1][j]); // soer
                    if (j > 0) {
                        rutenett[i][j].settNaboer(rutenett[i+1][j-1]); // soer-v
                    }
                    if (j < rutenett[i].length-1) {
                        rutenett[i][j].settNaboer(rutenett[i+1][j+1]); // soer-oe
                    }
                }
            }
        }
    }

    public void skrivUt() {
        for (int i = 0; i < rutenett.length; i++) {
            for (int j = 0; j < rutenett[i].length; j++) {
                System.out.print(rutenett[i][j].tilTegn());
            }
            System.out.println("");
        }
    }



    public static void main(String[] args) {
        Brett b = new Brett(3, 4, 0.2);
        b.spill();
    }

}
