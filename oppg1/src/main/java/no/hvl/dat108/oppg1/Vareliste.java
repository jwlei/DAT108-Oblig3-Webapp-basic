package no.hvl.dat108.oppg1;

import java.util.ArrayList;

public class Vareliste {
    private static ArrayList<Vare> varer = new ArrayList<Vare>();

    public ArrayList<Vare> getVarer() {
        return varer;
    }

    public int index(Vare vare) {
        int varen = varer.indexOf(vare);
        return varen;
    }

    public Vareliste() {
    }

    public void LeggTilVare(String navn) {
        varer.add(new Vare(navn));
    }

    public void fjernVare(int index) {
        varer.remove(index);
    }


}
