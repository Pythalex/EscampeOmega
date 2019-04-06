/**
 * 
 */

package jeux.modele.algorithmes;

import java.util.ArrayList;
import java.util.Arrays;

import jeux.modele.PlateauClonable;

public class AlphaBeta implements AlgoJeu {

    /**
     * La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 8;

    // -------------------------------------------
    // Attributs
    // -------------------------------------------

    /**
     * La profondeur de recherche utilisée pour l'algorithme
     */
    private int profMax = PROFMAXDEFAUT;

    /**
     * L'heuristique utilisée par l'algorithme
     */
    private Heuristique h;

    /**
     * Le playur Min (l'adversaire)
     */
    private String playurMin;

    /**
     * Le playur Max (celui dont l'algorithme de recherche adopte le point de vue)
     */
    private String playurMax;

    /**
     * Le nombre de noeuds développé par l'algorithme (intéressant pour se faire une
     * idée du nombre de noeuds développés)
     */
    private int nbnoeuds = 0;

    /**
     * Le nombre de feuilles évaluées par l'algorithme
     */
    private int nbfeuilles = 0;

    private int profondeurCourante = 0;

    // -------------------------------------------
    // Constructeurs
    // -------------------------------------------
    public AlphaBeta(Heuristique h, String playurMax, String playurMin) {
        this(h, playurMax, playurMin, PROFMAXDEFAUT);
    }

    public AlphaBeta(Heuristique h, String playurMax, String playurMin, int profMaxi) {
        this.h = h;
        this.playurMin = playurMin;
        this.playurMax = playurMax;
        profMax = profMaxi;
        // System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    public String meilleurCoup(PlateauClonable p) {
        profondeurCourante = 0;

        ArrayList<String> possiblesMoves = (ArrayList<String>)Arrays.asList(p.possiblesMoves(playurMax));

        PlateauClonable pbis = p.copy();

        String coupMax = possiblesMoves.get(0);
        pbis.play(playurMax, coupMax);
        int max = Integer.MIN_VALUE;

        max = minMax(pbis, max, Integer.MAX_VALUE);

        for (int i = 1; i < possiblesMoves.size(); i++) {
            pbis = p.copy();
            pbis.play(playurMax, possiblesMoves.get(i));

            int newVal = minMax(pbis, max, Integer.MAX_VALUE);
            if (newVal > max) {
                coupMax = possiblesMoves.get(i);
                max = newVal;
            }
        }

        System.out.println("Nombre de feuilles visitées : " + nbfeuilles);
        System.out.println("Nombre de noeuds visitées : " + nbnoeuds);
        return coupMax;
    }

    // -------------------------------------------
    // Méthodes publiques
    // -------------------------------------------
    public String toString() {
        return "Alphabeta(ProfMax=" + profMax + ")";
    }

    // -------------------------------------------
    // Méthodes internes
    // -------------------------------------------

    /* Evaluation pour ami */
    public int maxMin(PlateauClonable n, int alpha, int beta) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, playurMax);
        } else {
            nbnoeuds++;

            PlateauClonable pbis;
            String j = playurMax;

            for (String c : n.possiblesMoves(j)) {
                pbis = n.copy();
                pbis.play(j, c);

                alpha = Integer.max(alpha, minMax(pbis, alpha, beta));

                if (alpha >= beta) {
                    return beta;
                }
            }

            profondeurCourante--;
            return alpha;
        }
    }

    /* Evaluation pour ennemi */
    public int minMax(PlateauClonable n, int alpha, int beta) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, playurMin);
        } else {
            nbnoeuds++;

            PlateauClonable pbis;
            String j = playurMin;

            for (String c : n.possiblesMoves(j)) {
                pbis = n.copy();
                pbis.play(j, c);

                beta = Integer.min(beta, maxMin(pbis, alpha, beta));

                if (alpha >= beta) {
                    return alpha;
                }
            }

            profondeurCourante--;
            return beta;
        }
    }

}