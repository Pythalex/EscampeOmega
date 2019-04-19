package jeux.escampe.joueur;

import jeux.escampe.EscampeBoard;
import jeux.modele.IArbitre;

public class Arbitre implements IArbitre {

    EscampeBoard plateau;

    public Arbitre(){
        plateau = new EscampeBoard();
    }

    @Override
    public void play(int color, String coup) {
        plateau.play(coup, joueurFromInt(color));
    }

    @Override
    public Boolean GameOver() {
        return plateau.gameOver();
    }

    public String joueurFromInt(int color){
        return (color == -1 ? "blanc" : "noir");
    }

}