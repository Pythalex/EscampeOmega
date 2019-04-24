package jeux.escampe.joueur.heuristiques;

import java.util.Random;

import jeux.escampe.EscampeBoard;
import jeux.escampe.Lisere;
import jeux.escampe.Point2D;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.Heuristique;

public class HBeta implements Heuristique {

    Random r = new Random();

    /**
     * Renvoie le nombre de paladins qui peuvent prendre la licorne de couleur indiquée par blanc.
     * @param blanc - si vrai, la licorne considérée est blanche, sinon noire.
     * @return le nombre de paladins
     */
    public int nbPaladinsPouvantPrendre(boolean blanc, EscampeBoard p){
        int total = 0;
        Point2D licorne = new Point2D(0, 0);

        if (blanc){
            licorne.x = p.pions[EscampeBoard.licorne_blanche*2];
            licorne.y = p.pions[EscampeBoard.licorne_blanche*2 + 1];
            // pour tous les paladins noirs
            for (int i = EscampeBoard.licorne_noire + 1; i < EscampeBoard.nb_pions; i++){
                if (p.canMoveTo(i, licorne))
                    total++;
            }
        } else {
            licorne.x = p.pions[EscampeBoard.licorne_noire*2];
            licorne.y = p.pions[EscampeBoard.licorne_noire*2 + 1];
            // pour tous les paladins blancs
            for (int i = 1; i < EscampeBoard.licorne_noire; i++){
                if (p.canMoveTo(i, licorne))
                    total++;
            }
        }

        return total;
    }
    
    @Override
    public int eval(PlateauClonable plateau, String joueur) {
        EscampeBoard p = (EscampeBoard) plateau;

        boolean licorne_blanche = true;
        boolean licorne_noire = false;

        int diff = nbPaladinsPouvantPrendre(licorne_blanche, p) -
            nbPaladinsPouvantPrendre(licorne_noire, p);

        // On inverse l'heuristique si on joue le joueur noir
        return diff * (p.isJoueurBlanc(joueur) ? 1 : -1);
    }

}