package jeux.modele.algorithmes;

import jeux.modele.IJoueur;
import jeux.modele.PlateauClonable;

public interface Heuristique {

	public int eval(PlateauClonable p, String j);

}
 