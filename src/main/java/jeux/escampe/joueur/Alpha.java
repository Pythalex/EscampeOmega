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
    protected AlphaBeta algo;
    protected int profmaxPlacement = 2;
    protected int profmax = 7;

    // infos sur sa couleur
    protected String moi; // blanc/noir
    protected String ennemi;
    protected int colornum;
    public boolean first = true;

    public Alpha(){
        
    }

	@Override
	public void initJoueur(int mycolour) {
        plateau = new EscampeBoard();
        colornum = mycolour;
        moi = (mycolour == BLANC) ? "blanc" : "noir";
        ennemi = (mycolour == NOIR) ? "blanc" : "noir";
        plateau = new EscampeBoard();
        first = true;
        initAlgo();
    }
    
    public void initAlgo(){
        algo = new AlphaBeta(h, moi, ennemi, profmaxPlacement);
    }

	@Override
	public int getNumJoueur() {
		return colornum;
	}

	@Override
	public String choixMouvement() {

        String coup;

        if (first){
            String coups[] = plateau.possiblesMoves(moi);
            coup = coups[new Random().nextInt(coups.length)];
            first = false;
            algo.profMax = profmax;
        } else {
            coup = algo.meilleurCoup(plateau);
        }
            
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