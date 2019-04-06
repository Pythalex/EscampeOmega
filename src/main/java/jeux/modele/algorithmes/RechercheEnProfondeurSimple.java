package jeux.modele.algorithmes;

import jeux.modele.graphes.Probleme;
import jeux.modele.graphes.Solution;
import jeux.modele.graphes.Noeud;
import jeux.modele.graphes.Etat;

/**
 * RechercheEnProfondeurSimple
 * une impl�mentation simple de l'algorithme de recherche
 * en profondeur d'abord, sans d�tection de cycles, donc uniquement adapt�e
 * aux espaces d'�tats sans cycles ni branches infinies.
 * 
 * @author BONIN Alexandre
 */
public class RechercheEnProfondeurSimple extends AlgorithmeRechercheEE {

    //----------------------------------------------------
    // Constructeurs
    //----------------------------------------------------

    public RechercheEnProfondeurSimple() {
        super();
    }

    //----------------------------------------------------
    // Methode(s) requise par la classe abstraite  AlgorithmeRechercheEE
    //----------------------------------------------------

    @Override
    public Solution chercheSolution(Probleme p) {

        Noeud init = new Noeud(p.getEtatInitial(), null);

        return rprof(init, p);
    }

    public Solution rprof(Noeud n, Probleme p){

        Solution sol;
        nbNoeudsDeveloppes++;
        System.out.println(nbNoeudsDeveloppes);

        if (p.isTerminal(n.getEtat())){
            return construire_solution(n);
        } else {
            for(Etat s: n.getEtat().successeurs()){
                sol = rprof(new Noeud(s, n), p);
                if (sol != null){
                    return sol;
                }
            }
            return null;
        }
    }
    
}
