package jeux.modele;

public interface Partiel {

    /**
     * Initialise un plateau à partir d'un fichier texte.
     * @param fileName      le nom du fichier à lire
     */
    public void setFromFile(String fileName);

    /**
     * Sauvegarde la configuration de l'état courant (plateau et pièces restantes) dans un fichier.
     * 
     * @param fileName      le nom du fichier à créer
     * Le format doit être compatible avec celui utilisé pour la lecture
     */
    public void saveToFile(String fileName);

    public boolean isValidMove(String move, String player);

    public String[] possiblesMoves(String player);

    public void play(String move, String player);

    public boolean gameOver();

}