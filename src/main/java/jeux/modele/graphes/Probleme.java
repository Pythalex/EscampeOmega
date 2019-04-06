package jeux.modele.graphes;

/**
 * Un probl�me se caract�rise par 
 * - un nom 
 * - un �tat initial, 
 * - une m�thode qui selon le probl�me pos� d�termine si un �tat 
 *   est un �tat terminal (ou but)
 *
 * @author Philippe Chatalic
 * @author Alexandre Allauzen
 */
public interface Probleme {

    /**
     * @return l'�tat initial du probl�me
     */
    public Etat getEtatInitial();

     /**
     * @return vrai si l'�tat est Terminal pour ce probl�me
     */
    public abstract boolean isTerminal(Etat e);

   /**
     * @return Une repr�sentation du probl�me sous forme de cha�ne
     */
    @Override
    public String toString();

}