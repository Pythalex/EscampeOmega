/**
 * 
 */

package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

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
     * Le joueur Min (l'adversaire)
     */
    private Joueur joueurMin;

    /**
     * Le joueur Max (celui dont l'algorithme de recherche adopte le point de vue)
     */
    private Joueur joueurMax;

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
    public AlphaBeta(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
        this(h, joueurMax, joueurMin, PROFMAXDEFAUT);
    }

    public AlphaBeta(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
        this.h = h;
        this.joueurMin = joueurMin;
        this.joueurMax = joueurMax;
        profMax = profMaxi;
        // System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    public CoupJeu meilleurCoup(PlateauJeu p) {
        profondeurCourante = 0;

        ArrayList<CoupJeu> coupsPossibles = p.coupsPossibles(joueurMax);

        PlateauJeu pbis = p.copy();

        CoupJeu coupMax = coupsPossibles.get(0);
        pbis.joue(joueurMax, coupMax);
        int max = Integer.MIN_VALUE;

        max = minMax(pbis, max, Integer.MAX_VALUE);

        for (int i = 1; i < coupsPossibles.size(); i++) {
            pbis = p.copy();
            pbis.joue(joueurMax, coupsPossibles.get(i));

            int newVal = minMax(pbis, max, Integer.MAX_VALUE);
            if (newVal > max) {
                coupMax = coupsPossibles.get(i);
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
    public int maxMin(PlateauJeu n, int alpha, int beta) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, joueurMax);
        } else {
            nbnoeuds++;

            PlateauJeu pbis;
            Joueur j = joueurMax;

            for (CoupJeu c : n.coupsPossibles(j)) {
                pbis = n.copy();
                pbis.joue(j, c);

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
    public int minMax(PlateauJeu n, int alpha, int beta) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, joueurMin);
        } else {
            nbnoeuds++;

            PlateauJeu pbis;
            Joueur j = joueurMin;

            for (CoupJeu c : n.coupsPossibles(j)) {
                pbis = n.copy();
                pbis.joue(j, c);

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
