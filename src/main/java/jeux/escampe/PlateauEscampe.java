package jeux.escampe;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.ArrayList;

import java.util.List;

import java.lang.StringBuilder;

public class PlateauEscampe implements PlateauJeu {

    /*
     * Pour coder un nouveau jeu... il faut au minimum coder - Une classe PlateauX
     * pour représenter l'état du "plateau" de ce jeu. Cette classe doit fournir le
     * code des méthodes de l'interface PlateauJeu qui permettent de caractériser
     * les règles du jeu Une classe CoupX qui
     */

    
    private int pions[];
    private int width = 6;
    private int height = 6;
    private int nb_pions = 12;

    public PlateauEscampe() {
        pions = new int[24];
        for (int i = 0; i < nb_pions; i++){
            if (i < 7) {
                pions[i * 2] = 1;
                pions[i * 2 + 1] = i + 1;
            } else {
                pions[i * 2] = 6;
                pions[i * 2 + 1] = i - 6;
            }
        }
    }

    public PlateauEscampe(List<Point2D> positions){
        if (positions == null){
            throw new NullPointerException("La liste des positions ne doit pas être nulle");
        }
        if (positions.size() != nb_pions){
            throw new IllegalArgumentException("La liste des positions doit contenir 12 positions.");
        }
        for (int i = 0; i < positions.size() - 1; i++){
            Point2D current = positions.get(i);
            for (int j = i + 1; j < positions.size(); j++){
                if (current.equals(positions.get(j))){
                    throw new IllegalArgumentException("La liste des positions ne doit pas comporter de doublons. Deux pièces ne peuvent pas se chevaucher.");
                }
            }
        }

        pions = new int[nb_pions * 2];
        int pion = 0;
        for (Point2D pos: positions){
            pions[pion * 2] = pos.x;
            pions[pion * 2 + 1] = pos.y;
            pion++; 
        }
    }

    public PlateauEscampe(PlateauEscampe other){
        pions = other.pions.clone();
    }

    public ArrayList<CoupJeu> coupsPossibles(Joueur j) {
        throw new UnsupportedOperationException("Il vous faut coder cette méthode");
    }

    public void joue(Joueur j, CoupJeu c) {
        throw new UnsupportedOperationException("Il vous faut coder cette méthode");
    }

    public boolean finDePartie() {
        return pions[0] == -1 && pions[1] == -1 || pions[12] == -1 && pions[13] == -1;
    }

    public PlateauJeu copy() {
        return new PlateauEscampe(this);
    }

    public boolean coupValide(Joueur j, CoupJeu c) {
        throw new UnsupportedOperationException("Il vous faut coder cette méthode");
    }

    public Point2D getPionPos(int index){
        return new Point2D(pions[index * 2 + 1] - 1, pions[index * 2] - 1);
    }

    public String toString(){
        String[] p = new String[36];
        
        for (int i = 0; i < nb_pions; i++){
            Point2D pos = getPionPos(i);
            
            if (i == 1) {
                p[pos.y * width + pos.x] = "LB";
            } else if (i == 6){
                p[pos.y * width + pos.x] = "LN";
            } else if (i < 6) {
                p[pos.y * width + pos.x] = "PB";
            } else {
                p[pos.y * width + pos.x] = "PN";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("+-----------------+\n");
        for (int j = 0; j < height; j++){
            sb.append("|");
            for (int i = 0; i < width; i++){
                String c = p[j * width + i];
                sb.append((c == null ? "  " : c) + "|");
            }
            sb.append("+-----------------+\n");
        }
        return sb.toString();
    }

}