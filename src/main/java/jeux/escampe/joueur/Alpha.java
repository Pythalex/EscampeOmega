package jeux.escampe.joueur;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Arrays;

import jeux.escampe.EscampeBoard;
import jeux.modele.IJoueur;
import jeux.modele.algorithmes.AlgoJeu;
import jeux.modele.algorithmes.AlphaBeta;
import jeux.modele.algorithmes.Heuristique;

import jeux.escampe.joueur.heuristiques.HAlpha;

public class Alpha implements IJoueur {

    protected EscampeBoard plateau;

    // IA
    protected Heuristique h = new HAlpha();
    protected AlgoJeu algo;
    protected final int profmax = 5;

    // infos sur sa couleur
    protected String moi; // blanc/noir
    protected String ennemi;
    protected int colornum;

    public Alpha(){
        
    }

	@Override
	public void initJoueur(int mycolour) {
        plateau = new EscampeBoard();
        colornum = mycolour;
        moi = (mycolour == BLANC) ? "blanc" : "noir";
        ennemi = (mycolour == NOIR) ? "blanc" : "noir";
        plateau = new EscampeBoard();
        initAlgo();
    }
    
    public void initAlgo(){
        algo = new AlphaBeta(h, moi, ennemi, profmax);
    }

	@Override
	public int getNumJoueur() {
		return colornum;
	}

	@Override
	public String choixMouvement() {
        String coup = algo.meilleurCoup(plateau);
        plateau.play(coup, moi);
        return coup;
	}

	@Override
	public void declareLeVainqueur(int colour) {
		if (colour == colornum){
            System.out.println("Alpha wins bloop bloop.");
        } else {
            System.out.println("...");
        }
	}

	@Override
	public void mouvementEnnemi(String coup) {
		plateau.play(coup, ennemi);
	}

	@Override
	public String binoName() {
		return "BONIN et LIMACHE";
	}

}