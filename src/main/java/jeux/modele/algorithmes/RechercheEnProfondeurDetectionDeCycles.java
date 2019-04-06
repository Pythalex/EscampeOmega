package jeux.modele.algorithmes;

import jeux.modele.graphes.Probleme;
import jeux.modele.graphes.Solution;
import jeux.modele.graphes.Noeud;
import jeux.modele.graphes.Etat;

import java.util.ArrayList;

import java.util.Scanner;

/**
 * RechercheEnProfondeurDetectionDeCycles une version de l'algorithme de
 * recherche en profondeur d'abord capable de d�tecter les cycles pour ne pas
 * red�velopper des �tats d�j� vus pr�c�demments sur le chemin en cours
 * d'exploration
 *
 * @author BONIN Alexandre
 */
public class RechercheEnProfondeurDetectionDeCycles extends AlgorithmeRechercheEE {

    static Scanner sc = new Scanner(System.in);

    //----------------------------------------------------
    // Constructeurs
    //----------------------------------------------------
    public RechercheEnProfondeurDetectionDeCycles() {
        super();
    }

    public void pause(){
        sc.nextLine();
    }

    //----------------------------------------------------
    // Methode(s) requise par la classe abstraite  AlgorithmeRechercheEE
    //----------------------------------------------------
    @Override
    public Solution chercheSolution(Probleme p) {

        Noeud init = new Noeud(p.getEtatInitial(), null);
        ArrayList<Etat> dejaVu = new ArrayList<Etat>();

        return rprof(init, p, dejaVu);
    }

    public Solution rprof(Noeud n, Probleme p, ArrayList<Etat> dejaVu){

        Solution sol;
        nbNoeudsDeveloppes++;

        Etat courant = n.getEtat();
        
        // No cycle
        if (dejaVu.contains(courant)){
            return null;
        }
        else {
            dejaVu.add(courant);
        }
        // ========

        if (p.isTerminal(courant)){
            return construire_solution(n);
        } else {
            for(Etat s: courant.successeurs()){
                sol = rprof(new Noeud(s, n), p, dejaVu);
                if (sol != null){
                    return sol;
                }
            }
            return null;
        }
    }
}
