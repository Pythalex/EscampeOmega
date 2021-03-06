/**
 * 
 */

package jeux.modele.algorithmes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import jeux.modele.PlateauClonable;

public class AlphaBeta implements AlgoJeu {

    /**
     * La profondeur de recherche par défaut
     */
    private static int PROFMAXDEFAUT = 8;

    // -------------------------------------------
    // Attributs
    // -------------------------------------------

    /**
     * La profondeur de recherche utilisée pour l'algorithme
     */
    public int profMax = PROFMAXDEFAUT;

    /**
     * L'heuristique utilisée par l'algorithme
     */
    private Heuristique h;

    /**
     * Le playeur Min (l'adversaire)
     */
    private String PlayerMin;

    /**
     * Le playeur Max (celui dont l'algorithme de recherche adopte le point de vue)
     */
    private String PlayerMax;

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
    public AlphaBeta(Heuristique h, String PlayerMax, String PlayerMin) {
        this(h, PlayerMax, PlayerMin, PROFMAXDEFAUT);
    }

    public AlphaBeta(Heuristique h, String PlayerMax, String PlayerMin, int profMaxi) {
        this.h = h;
        this.PlayerMin = PlayerMin;
        this.PlayerMax = PlayerMax;
        profMax = profMaxi;
        // System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    public String meilleurCoup(PlateauClonable p) {
        profondeurCourante = 0;

        List<String> possiblesMoves = Arrays.stream(p.possiblesMoves(PlayerMax)).collect(Collectors.toList());
        //System.out.println("Nombre de coups : " + possiblesMoves.size());

        PlateauClonable pbis = p.copy();

        String coupMax = possiblesMoves.get(0);
        pbis.play(coupMax, PlayerMax);
        int max = Integer.MIN_VALUE;

        max = minMax(pbis, max, Integer.MAX_VALUE);

        for (int i = 1; i < possiblesMoves.size(); i++) {
            pbis = p.copy();
            pbis.play(possiblesMoves.get(i), PlayerMax);

            int newVal = minMax(pbis, max, Integer.MAX_VALUE);
            if (newVal > max) {
                coupMax = possiblesMoves.get(i);
                max = newVal;
            }
        }

        // System.out.println("Nombre de feuilles visitées : " + nbfeuilles);
        // System.out.println("Nombre de noeuds visitées : " + nbnoeuds);
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
        // System.err.println("Maxmin " + profondeurCourante);
        // System.err.println(n);
        // Scanner sc = new Scanner(System.in);
        // sc.nextLine();
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            //System.out.println("Feuille à profondeur " + (profondeurCourante));
            
            return h.eval(n, PlayerMax);
        } else {
            nbnoeuds++;

            PlateauClonable pbis;
            String j = PlayerMax;
            String[] coups = n.possiblesMoves(j);
            // System.out.println("Coups : ");
            // System.out.println(Arrays.toString(coups));
            // System.out.println("\n\n=================\n\n");

            for (String c : coups) {
                //System.err.println("Coup : " + c);
                pbis = n.copy();
                //System.err.println("Coup : " + c + " | maxMin | Joueur : " + PlayerMax);
                pbis.play(c, j);

                alpha = Integer.max(alpha, minMax(pbis, alpha, beta));

                if (alpha >= beta) {
                    profondeurCourante--;
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
        // System.err.println("Minmax " + profondeurCourante);
        // System.err.println(n);
        // Scanner sc = new Scanner(System.in);
        // sc.nextLine();
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            //System.out.println("Feuille à profondeur " + (profondeurCourante));

            profondeurCourante--;
            return h.eval(n, PlayerMin);
        } else {
            nbnoeuds++;

            PlateauClonable pbis;
            String j = PlayerMin;

            String[] coups = n.possiblesMoves(j);
            // System.out.println("Coups : ");
            // System.out.println(Arrays.toString(coups));

            for (String c : coups) {
                //System.err.println("Coup : " + c);
                pbis = n.copy();
                //System.err.println("Coup : " + c + " | minMax | Joueur : " + PlayerMax);
                pbis.play(c, j);

                beta = Integer.min(beta, maxMin(pbis, alpha, beta));

                if (alpha >= beta) {
                    profondeurCourante--;
                    return alpha;
                }
            }

            profondeurCourante--;
            return beta;
        }
    }

}
