package jeux.modele.graphes;

import java.util.Collection;

/**
 * L'interface d'un �tat est succincte, tout ce dont on a besoin pour un �tat
 * est : 
 * - que l'on soit capable de calculer ses successeurs
 * - que l'on puisse l'afficher d'une mani�re standard pour visualiser un chemin 
 * - et que la fonction equals soit red�finie de mani�re ad�quate.
 */
public interface Etat {


    /**
     * @param e l'�tat consid�r�
     * @return L'ensemble des �tats atteignables � partir de l'�tat par un
     * op�rateur de changement d'�tat L'utilisation d'une Collection permet de
     * choisir librement La classe la mieux adapt�e en fonction du probl�me
     */
    public Collection<Etat> successeurs();

    @Override
    public boolean equals(Object obj);

    /**
     * @return Une repr�sentation de l'�tat sous forme de cha�ne
     */
    @Override
    public String toString();
}
