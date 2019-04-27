package jeux.escampe.joueur.heuristiques;

import java.util.Random;

import jeux.escampe.EscampeBoard;
import jeux.escampe.Point2D;
import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.Heuristique;

public class HAlpha implements Heuristique {

    Random r = new Random();

    public int getDistancePaladinProcheDeLicorne(EscampeBoard p, boolean licorneBlanche) {
        Point2D objectif = new Point2D(-1, -1);
        Point2D minpos = new Point2D(-1, -1);
        Point2D paladinpos = new Point2D(-1, -1);
        int mindistance = 0;
    
        if (licorneBlanche){ // on veut les paladins pres de  la licorne blanche

            objectif.x = p.pions[EscampeBoard.licorne_blanche*2];
            objectif.y = p.pions[EscampeBoard.licorne_blanche*2 + 1];

            mindistance = Integer.MAX_VALUE;
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

        } else { // ennemi

            objectif.x = p.pions[EscampeBoard.licorne_noire*2];
            objectif.y = p.pions[EscampeBoard.licorne_noire*2 + 1];
             
            mindistance = Integer.MAX_VALUE;
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

        }

        return mindistance;
    }

    @Override
    public int eval(PlateauClonable plateau, String joueur) {
        EscampeBoard p = (EscampeBoard) plateau;
        boolean licorneBlanche = true;
        boolean licorneNoire = false;

        // distance de manhattan entre le pion ennemi le plus proche de la licorne alliée et la licorne
        int distancePaladinBlancSurLicorneNoire = getDistancePaladinProcheDeLicorne(p, licorneNoire);
        // distance de manhattan entre le pion allié le plus proche de la licorne ennemie et la licorne
        int distancePaladinNoireSurLicorneBlanche = getDistancePaladinProcheDeLicorne(p, licorneBlanche);

        
        // System.err.println("Pour joueur " + joueur + " -> " + distancePaladinBlancSurLicorneNoire + " " + distancePaladinNoireSurLicorneBlanche);
        // try {
        //     System.err.println(plateau);
        //     Thread.sleep(1500);
        // } catch (Exception e){
        //     e.printStackTrace();
        // }

        // difference
        if (p.isJoueurBlanc(joueur)){
            //System.out.println(joueur + "blanc");
            return distancePaladinNoireSurLicorneBlanche - 
                distancePaladinBlancSurLicorneNoire;
        } else {
            //System.out.println(joueur + "noir");
            return distancePaladinBlancSurLicorneNoire - 
                distancePaladinNoireSurLicorneBlanche;
        }
        
    }

}