package jeux.escampe;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.ArrayList;

import java.util.List;

import java.lang.StringBuilder;

public class PlateauEscampe implements PlateauJeu {
    
    private static Joueur jBlanc = new Joueur("Blanc");
    private static Joueur jNoir = new Joueur("Noir");

    private int pions[];
    private int width = 6;
    private int height = 6;
    private int nb_pions = 12;
    private int cases_pions[][] = {
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    private static final int licorne_blanche = 1;
    private static final int licorne_noire = 6;
    
    public static Lisere SIMPLE = Lisere.SIMPLE;
    public static Lisere DOUBLE = Lisere.DOUBLE;
    public static Lisere TRIPLE = Lisere.TRIPLE;
    private static Lisere cases_liseres[][] = {
        {SIMPLE, DOUBLE, DOUBLE, TRIPLE, SIMPLE, DOUBLE},
        {TRIPLE, SIMPLE, TRIPLE, SIMPLE, TRIPLE, DOUBLE},
        {DOUBLE, TRIPLE, SIMPLE, DOUBLE, SIMPLE, TRIPLE},
        {DOUBLE, SIMPLE, TRIPLE, DOUBLE, TRIPLE, SIMPLE},
        {SIMPLE, TRIPLE, SIMPLE, TRIPLE, SIMPLE, DOUBLE},
        {TRIPLE, DOUBLE, DOUBLE, SIMPLE, TRIPLE, DOUBLE}
    };

    public PlateauEscampe() {
        pions = new int[24];
        Point2D pos;
        for (int i = 0; i < nb_pions; i++){
            if (i < 7) {
                pions[i * 2] = 1;
                pions[i * 2 + 1] = i + 1;
            } else {
                pions[i * 2] = 6;
                pions[i * 2 + 1] = i - 6;
            }
            pos = getPionPos(i);
            cases_pions[pos.y][pos.x] = i;
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
            cases_pions[pos.y][pos.x] = pion;
            pion++; 
        }
    }

    public PlateauEscampe(PlateauEscampe other){
        pions = other.pions.clone();
        cases_pions = other.cases_pions.clone();
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
        CoupEscampe ce = (CoupEscampe)c;
        int pion = ce.getPion();
        int dx = ce.getDx();
        int dy = ce.getDy();

        Point2D from = getPionPos(pion);
        Point2D to = from.translate(dx, dy);
        // sort du terrain ?
        if (to.x < 0 || to.x >= width || to.y < 0 || to.y >= height)
            return false;

        Lisere lfrom = cases_liseres[from.y][from.x];
        boolean isjb = isJoueurBlanc(j);
        boolean isjn = isJoueurNoir(j);
        if (isjb){
            if (pion > 6){
                return false;
            }
        } else if (isjn){
            if (pion < 7){
                return false;
            }
        } else {
            throw new IllegalArgumentException("Le joueur n'est pas une référence vers un joueur valide.");
        }

        boolean result;
        switch (lfrom){
            case SIMPLE:
                if ((dx == 0 && dy == -1) || (dx == 0 && dy == -1) || (dx == 0 && dy == -1) || (dx == 0 && dy == -1)) {
                    // Verification de collision
                    int pion_en_xy = cases_pions[to.y][to.x];
                    if (isjb && pion_en_xy != licorne_noire){
                        result = false;
                    } else if (isjn && pion_en_xy != licorne_blanche){
                        result = false;
                    } else {
                        result = true;
                    }
                } else {
                    // Mouvement impossible
                    result = false;
                }
                break;
            case DOUBLE:
                break;
            case TRIPLE:
            default:
                break;
        }

        return result;
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

    public boolean isJoueurBlanc(Joueur other) {
        return jBlanc.equals(other);
    }

    public boolean isJoueurNoir(Joueur other) {
        return jNoir.equals(other);
    }

}