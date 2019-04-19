package jeux.escampe.joueur;

import jeux.escampe.EscampeBoard;
import jeux.modele.IJoueur;

public class Metroid implements IJoueur {

    public EscampeBoard plateau;

    public Metroid(){
        
    }

	@Override
	public void initJoueur(int mycolour) {
		plateau = new EscampeBoard();
	}

	@Override
	public int getNumJoueur() {
		return 0;
	}

	@Override
	public String choixMouvement() {
		return null;
	}

	@Override
	public void declareLeVainqueur(int colour) {
		
	}

	@Override
	public void mouvementEnnemi(String coup) {
		
	}

	@Override
	public String binoName() {
		return null;
	}

}