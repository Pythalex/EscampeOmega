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

import jeux.escampe.joueur.heuristiques.HBeta;

public class Beta extends Alpha {

    protected Heuristique h = new HBeta();

	@Override
	public void declareLeVainqueur(int colour) {
		if (colour == colornum){
            System.out.println("Beta wins bloop bloop.");
        } else {
            System.out.println("...");
        }
	}

}