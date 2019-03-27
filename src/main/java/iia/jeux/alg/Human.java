/**
 * 
 */

package iia.jeux.alg;

import java.util.ArrayList;

import iia.jeux.modele.CoupJeu;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import java.util.Scanner;

public class Human implements AlgoJeu {

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
    public Human(Heuristique h, Joueur joueurMax, Joueur joueurMin) {
        this(h, joueurMax, joueurMin, PROFMAXDEFAUT);
    }

    public Human(Heuristique h, Joueur joueurMax, Joueur joueurMin, int profMaxi) {
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
        System.out.print("Entrer le coup à jouer: ");
        ArrayList<CoupJeu> coups = p.coupsPossibles(joueurMax);
        Scanner sc = new Scanner(System.in);

        while (true) {
            String reponse = sc.nextLine();
            for (CoupJeu c : coups) {
                if (c.toString().equals(reponse))
                    return c;
            }
            System.out.print("Votre coup n'est pas possible, réessayez : ");
        }
    }

    // -------------------------------------------
    // Méthodes publiques
    // -------------------------------------------
    public String toString() {
        return "Human";
    }

}
