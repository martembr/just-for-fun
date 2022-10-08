class Rute {

    protected String tegn;
    protected String uaapnetTegn = " . ";
    private int mineNaboer = 0;
    protected boolean aapnet = false;
    private Rute[] naboer = new Rute[8];
    protected Brett brettet;
    private int rad;
    private int kol;

    public Rute(Brett brettet, int r, int k) {
        tegn = " ";
        this.brettet = brettet;
        rad = r;
        kol = k;
    }

    public boolean erMine() {
        return false;
    }

    public String tilTegn() {
        if (!aapnet) {
            return uaapnetTegn;
        }
        return tegn;
    }

    public void oekMineNaboer() {
        mineNaboer++;
    }

    public int hentRad() {
        return rad;
    }

    public int hentKol() {
        return kol;
    }

    public int hentMineNaboer() {
        return mineNaboer;
    }

    public void settNaboer(Rute nabo) {
        for (int i = 0; i < naboer.length; i++) {
            if (naboer[i] == null) {
                naboer[i] = nabo;
                return;
            }
        }
    }

    public void aapne() {
        tegn = " " + mineNaboer + " ";
        aapnet = true;
    }




} // slutt Rute-klasse

class MineRute extends Rute {

    public MineRute(Brett brettet, int r, int k) {
        super(brettet, r, k);
    }

    @Override
    public void settNaboer(Rute nabo) {
        super.settNaboer(nabo);
        nabo.oekMineNaboer();
    }

    @Override
    public boolean erMine() {
        return true;
    }

    @Override
    public void aapne() {
        tegn = " * ";
        aapnet = true;
        brettet.settGameOver();
    }


}
