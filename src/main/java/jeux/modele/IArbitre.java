package jeux.modele;

public interface IArbitre {
    
        // Mais pas lors de la conversation avec l'arbitre (méthodes initJoueur et getNumJoueur)
    // Vous pouvez changer cela en interne si vous le souhaitez
    static final int BLANC = -1;
    static final int NOIR = 1;

    public void play(int color, String coup);

    public Boolean GameOver();

}