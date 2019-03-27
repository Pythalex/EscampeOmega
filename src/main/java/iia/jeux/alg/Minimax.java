/**
 * 
 */

package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;

public class Minimax implements AlgoJeu {

    /**
     * La profondeur de recherche par défaut
     */
    private final static int PROFMAXDEFAUT = 3;

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
    public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
        this(h, joueurMax, joueurMin, PROFMAXDEFAUT);
    }

    public Minimax(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
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
        int max = minMax(pbis);

        for (int i = 1; i < coupsPossibles.size(); i++) {
            pbis = p.copy();
            pbis.joue(joueurMax, coupsPossibles.get(i));

            int newVal = minMax(pbis);
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
        return "MiniMax(ProfMax=" + profMax + ")";
    }

    // -------------------------------------------
    // Méthodes internes
    // -------------------------------------------

    /* Evaluation pour ami */
    public int maxMin(PlateauJeu n) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, joueurMax);
        } else {
            nbnoeuds++;

            int max = Integer.MIN_VALUE;
            PlateauJeu pbis;
            Joueur j = joueurMax;

            for (CoupJeu c : n.coupsPossibles(j)) {
                pbis = n.copy();
                pbis.joue(j, c);

                max = Integer.max(max, minMax(pbis));
            }

            profondeurCourante--;
            return max;
        }
    }

    /* Evaluation pour ennemi */
    public int minMax(PlateauJeu n) {
        profondeurCourante++;
        if (profondeurCourante >= profMax) {
            nbfeuilles++;

            profondeurCourante--;
            return h.eval(n, joueurMin);
        } else {
            nbnoeuds++;

            int min = Integer.MAX_VALUE;
            PlateauJeu pbis;
            Joueur j = joueurMin;

            for (CoupJeu c : n.coupsPossibles(j)) {
                pbis = n.copy();
                pbis.joue(j, c);

                min = Integer.min(min, maxMin(pbis));
            }

            profondeurCourante--;
            return min;
        }
    }

}
