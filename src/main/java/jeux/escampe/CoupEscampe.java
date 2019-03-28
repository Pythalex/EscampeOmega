package jeux.escampe;

import iia.jeux.modele.CoupJeu;

public class CoupEscampe implements CoupJeu{

    private int pion;
    private int dx;
    private int dy;

	public CoupEscampe(int pion, int dx, int dy) {
        this.pion = pion;
        this.dx = dx;
        this.dy = dy;
    }
    
    // accessors

    public int getPion() { return pion; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }
	     
}
