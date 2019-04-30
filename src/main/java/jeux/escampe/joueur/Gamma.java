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

import jeux.escampe.joueur.heuristiques.HGamma;

public class Gamma extends Alpha {

    protected int profmaxPlacement = 3;
    protected int profmax = 10;

    protected Heuristique h = new HGamma();

	@Override
	public void declareLeVainqueur(int colour) {
		if (colour == colornum){
            System.out.println("Gamma wins bloop bloop.");
        } else {
            System.out.println("...");
        }
	}

}