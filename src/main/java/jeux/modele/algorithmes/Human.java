/**
 * 
 */

package jeux.modele.algorithmes;

import java.util.ArrayList;
import java.util.Arrays;

import jeux.modele.PlateauClonable;

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
     * Le playur Max (celui dont l'algorithme de recherche adopte le point de vue)
     */
    private String playurMax;

    // -------------------------------------------
    // Constructeurs
    // -------------------------------------------
    public Human(Heuristique h, String playurMax, String playurMin) {
        this(h, playurMax, playurMin, PROFMAXDEFAUT);
    }

    public Human(Heuristique h, String playurMax, String playurMin, int profMaxi) {
        this.playurMax = playurMax;
        // System.out.println("Initialisation d'un MiniMax de profondeur " + profMax);
    }

    // -------------------------------------------
    // Méthodes de l'interface AlgoJeu
    // -------------------------------------------
    public String meilleurCoup(PlateauClonable p) {
        System.out.print("Entrer le coup à playr: ");
        ArrayList<String> coups = (ArrayList<String>)Arrays.asList(p.possiblesMoves(playurMax));
        Scanner sc = new Scanner(System.in);

        while (true) {
            String reponse = sc.nextLine();
            for (String c : coups) {
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
