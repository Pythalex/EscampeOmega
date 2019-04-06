package jeux.escampe.joueur.heuristiques;

import java.util.Random;

import jeux.modele.PlateauClonable;
import jeux.modele.algorithmes.Heuristique;

public class Aleatoire implements Heuristique {

    Random r = new Random();

    @Override
    public int eval(PlateauClonable p, String j) {
        int base = r.nextInt(10);
        return r.nextBoolean() ? base : -base;
    }

}