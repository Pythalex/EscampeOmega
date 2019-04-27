package jeux.escampe.joueur.heuristiques;

import java.util.Random;

import jeux.escampe.EscampeBoard;
import jeux.escampe.Lisere;
import jeux.escampe.Point2D;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.Heuristique;

public class HGamma extends HBeta {

    Random r = new Random();
    
    public int distancesPaladinsAmis(EscampeBoard p, Boolean blanc){
        int res = 0;
        Point2D licorne = new Point2D(0, 0);
        Point2D paladin = new Point2D(0, 0);
        if (blanc){
            licorne.x = p.pions[EscampeBoard.licorne_noire*2];
            licorne.y = p.pions[EscampeBoard.licorne_noire*2+1];
            for (int i = 1; i < EscampeBoard.licorne_noire; i++){
                paladin.x = p.pions[i*2];
                paladin.y = p.pions[i*2+1];
                res += paladin.manhattan_distance(licorne);
            }
        } else {
            licorne.x = p.pions[EscampeBoard.licorne_blanche*2];
            licorne.y = p.pions[EscampeBoard.licorne_blanche*2+1];
            for (int i = EscampeBoard.licorne_noire + 1; i < EscampeBoard.nb_pions; i++){
                paladin.x = p.pions[i*2];
                paladin.y = p.pions[i*2+1];
                res += paladin.manhattan_distance(licorne);
            }
        }
        return res;
    }

    public int distancePaladinsEnnemis(EscampeBoard p, Boolean blanc){
        int res = 0;
        Point2D licorne = new Point2D(0, 0);
        Point2D paladin = new Point2D(0, 0);
        if (!blanc){
            licorne.x = p.pions[EscampeBoard.licorne_noire*2];
            licorne.y = p.pions[EscampeBoard.licorne_noire*2+1];
            for (int i = 1; i < EscampeBoard.licorne_noire; i++){
                paladin.x = p.pions[i*2];
                paladin.y = p.pions[i*2+1];
                res += paladin.manhattan_distance(licorne);
            }
        } else {
            licorne.x = p.pions[EscampeBoard.licorne_blanche*2];
            licorne.y = p.pions[EscampeBoard.licorne_blanche*2+1];
            for (int i = EscampeBoard.licorne_noire + 1; i < EscampeBoard.nb_pions; i++){
                paladin.x = p.pions[i*2];
                paladin.y = p.pions[i*2+1];
                res += paladin.manhattan_distance(licorne);
            }
        }
        return res;
    }

    @Override
    public int eval(PlateauClonable plateau, String joueur) {
        int beta = super.eval(plateau, joueur);
        EscampeBoard p = (EscampeBoard)plateau;
        boolean blanc = p.isJoueurBlanc(joueur);
        return beta + distancePaladinsEnnemis(p, blanc) - distancesPaladinsAmis(p, blanc);
    }

}