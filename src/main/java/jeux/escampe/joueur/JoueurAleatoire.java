package jeux.escampe.joueur;

import jeux.escampe.EscampeBoard;
import jeux.escampe.joueur.heuristiques.Aleatoire;
import jeux.modele.IJoueur;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.AlgoJeu;
import jeux.modele.algorithmes.AlphaBeta;
import jeux.modele.algorithmes.Heuristique;

public class JoueurAleatoire implements IJoueur {

    // IA
    private Heuristique h = new Aleatoire();
    private AlgoJeu algo;

    // infos sur sa couleur
    private String moi; // blanc/noir
    private String ennemi;
    private int colornum;

    // plateau courant
    private PlateauClonable plateau = new EscampeBoard();

    public JoueurAleatoire(){
        
    }

	@Override
	public void initJoueur(int mycolour) {
        colornum = mycolour;
        moi = (mycolour == BLANC) ? "blanc" : "noir";
        ennemi = (mycolour == NOIR) ? "blanc" : "noir";
        initAlgo();
    }
    
    public void initAlgo(){
        algo = new AlphaBeta(h, moi, ennemi);
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