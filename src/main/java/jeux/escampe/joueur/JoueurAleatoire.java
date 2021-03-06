package jeux.escampe.joueur;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import jeux.escampe.EscampeBoard;
import jeux.modele.IJoueur;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.AlgoJeu;
import jeux.modele.algorithmes.AlphaBeta;
import jeux.modele.algorithmes.Heuristique;

public class JoueurAleatoire implements IJoueur {


    // infos sur sa couleur
    private String moi; // blanc/noir
    private String ennemi;
    private int colornum;

    // plateau courant
    private PlateauClonable plateau;

    public JoueurAleatoire(){
        
    }

	@Override
	public void initJoueur(int mycolour) {
        colornum = mycolour;
        moi = (mycolour == BLANC) ? "blanc" : "noir";
        ennemi = (mycolour == NOIR) ? "blanc" : "noir";
        plateau = new EscampeBoard();
    }

	@Override
	public int getNumJoueur() {
		return colornum;
	}

	@Override
	public String choixMouvement() {
        List<String> coups = Arrays.stream(plateau.possiblesMoves(moi)).collect(Collectors.toList());

        String coup = coups.get(new Random().nextInt(coups.size()));
        plateau.play(coup, moi);
		return coup;
	}

	@Override
	public void declareLeVainqueur(int colour) {
		if (colour == colornum){
            System.out.println("L'aléatoire gagne toujours.");
        } else {
            System.out.println("Je suis aléatoire donc ça compte pas.");
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