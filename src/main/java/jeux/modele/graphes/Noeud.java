package jeux.modele.graphes;

/**
 * un algorithme de recherche dans un espace d'�tats, sous une forme arborescente.
 * Elle est "minimale" au sens où chaque noeud contient les information minimales,
 * � savoir :
 *   - l'�tat associ� � un noeud
 *   - un noeud parent (id�alement, vers le "meilleur" parent connu, si cela a
 *     du sens pour l'algorithme de recherche consid�r�.
 *      (null si l'�tat correspond � un l'�tat initial du probl�me).
 *
 * Cette classe pourra �tre �tendue si n�cessaire, pour ajouter les informations
 * utiles en fonctin des besoins de chaque algorithme de recherche.
 *
 * @author Philippe Chatalic
 * 
 */
public class Noeud {

    // ------------------- Attributs -------------------
    /**
     * L'�tat associ� au noeud
     */
    private Etat etat;
    /**
     * le noeud correspondant � l'�tat � partir duquel l'�tat de ce noeud
     * a �t� produit lors de l'exploration du graphe de recherche
     */
    private Noeud pere;

    // ------------------- Constructeurs -------------------
    public Noeud() {
        etat = null;
        pere = null;
    }

    /**
     * Construit un noeud a partir
     */
    public Noeud(Etat e, Noeud n) {
        etat = e;
        pere = n;
    }

    // ------------------- Accesseurs -------------------
    /**
     * @return l'�tat associ� au noeud
     */
    public Etat getEtat() {
        return this.etat;
    }

    /**
     * @return le parent de ce noeud dans le graphe de recherche
     */
    public Noeud getPere() {
        return this.pere;
    }


    // ------------------- Modifieurs -------------------

    public void setPere(Noeud n) {
        pere = n;
    }

    public void setEtat(Etat e) {
        etat = e;
    }

    // ------------------- MoAutres m�thodesdifieurs -------------------


 
    /**
     * @return vrai si le noeud n correspond au m�me �tat que le noeud courant
     */
    public boolean memeEtat(Noeud n) {
        return etat.equals(n.getEtat());
    }
    
    public boolean memeEtat(Etat e) {
        return e.equals(this.getEtat());
    }

    public String toString() {
        String ret = "Noeud : etat = " + etat;
        return ret;
    }

}
