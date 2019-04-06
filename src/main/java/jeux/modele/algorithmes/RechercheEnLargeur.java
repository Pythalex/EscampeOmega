package jeux.modele.algorithmes;

import jeux.modele.graphes.Probleme;
import jeux.modele.graphes.Solution;
import jeux.modele.graphes.Noeud;
import jeux.modele.graphes.Etat;

import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * RechercheEnLargeur codage de l'algorithme de recherche en largeur d'abord
 * 
 * @author BONIN Alexandre
 */
public class RechercheEnLargeur extends AlgorithmeRechercheEE {

    public ArrayList<Etat> dejaDev = new ArrayList<Etat>();
    public Queue<Noeud> frontiere = new LinkedList<Noeud>();

    // ----------------------------------------------------
    // Constructeurs
    // ----------------------------------------------------
    public RechercheEnLargeur() {
        super();
    }

    // ----------------------------------------------------
    // Methode(s) requise par la classe abstraite AlgorithmeRechercheEE
    // ----------------------------------------------------
    @Override
    public Solution chercheSolution(Probleme p) {
        // vars
        Noeud init = new Noeud(p.getEtatInitial(), null);
        frontiere.add(init);

        Noeud n;

        while (!frontiere.isEmpty()){
            n = frontiere.peek();
            // on développe un nouveau noeud
            nbNoeudsDeveloppes++;

            if (p.isTerminal(n.getEtat())){
                return construire_solution(n);
            } else {
                frontiere.poll();
                dejaDev.add(n.getEtat());

                for(Etat s: n.getEtat().successeurs()){
                    if (!dejaDev.contains(s)){
                        boolean continu = true;
                        for (Noeud m: frontiere){
                            if (m.getEtat().equals(s)){
                                continu = false;
                                break;
                            }
                        }
                        if (continu){
                            Noeud ns = new Noeud(s, n); // Pere(s) <- n
                            frontiere.add(ns);
                        }
                    }
                }
            }
        }

        return null; // echec
    }

}
