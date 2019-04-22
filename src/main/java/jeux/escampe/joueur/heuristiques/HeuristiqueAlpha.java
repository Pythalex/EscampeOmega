package jeux.escampe.joueur.heuristiques;

import java.util.Random;

import jeux.escampe.EscampeBoard;
import jeux.escampe.Point2D;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.Heuristique;

public class HeuristiqueAlpha implements Heuristique {

    Random r = new Random();

    public Point2D getPaladinProcheDe(int x, int y, EscampeBoard p, boolean blanc) {
        Point2D objectif = new Point2D(x, y);
        Point2D minpos = new Point2D(-1, -1);
        Point2D paladinpos = new Point2D(-1, -1);
    
        if (blanc){

            int mindistance = Integer.MAX_VALUE;
            for (int i = 1; i < EscampeBoard.licorne_noire; i++){
                paladinpos.x = p.pions[i*2];
                paladinpos.y = p.pions[i*2+1];
                int dist = paladinpos.manhattan_distance(objectif);
                
                if (dist < mindistance){
                    mindistance = dist;
                    minpos.x = paladinpos.x;
                    minpos.y = paladinpos.y;
                }
            }

        } else { // ennemi
             
            int mindistance = Integer.MAX_VALUE;
            for (int i = EscampeBoard.licorne_noire + 1; i < EscampeBoard.nb_pions; i++){
                paladinpos.x = p.pions[i*2];
                paladinpos.y = p.pions[i*2+1];
                int dist = paladinpos.manhattan_distance(objectif);
                
                if (dist < mindistance){
                    mindistance = dist;
                    minpos.x = paladinpos.x;
                    minpos.y = paladinpos.y;
                }
            }

        }

        return minpos;
    }

    @Override
    public int eval(PlateauClonable plateau, String joueur) {
        EscampeBoard p = (EscampeBoard) plateau;

        // get position licorne alliée
        Point2D licorneAllie = new Point2D(p.pions[EscampeBoard.licorne_blanche*2], 
            p.pions[EscampeBoard.licorne_blanche*2+1]);
        // get position licorne ennemie
        Point2D licorneEnnemie = new Point2D(p.pions[EscampeBoard.licorne_noire*2], 
            p.pions[EscampeBoard.licorne_noire*2+1]);
        // get position du pion ennemi le plus proche de la licorne alliée
        Point2D paladinEnnemi = getPaladinProcheDe(licorneAllie.x, licorneAllie.y, p, "noir".equals(joueur));
        // get position du pion allié le plus proche de la licorne ennemie
        Point2D paladinAllie = getPaladinProcheDe(licorneEnnemie.x, licorneEnnemie.y, p, "blanc".equals(joueur));
        
        // difference
        return paladinEnnemi.manhattan_distance(licorneAllie) - paladinAllie.manhattan_distance(licorneEnnemie);
    }

}