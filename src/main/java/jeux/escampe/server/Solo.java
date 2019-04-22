package jeux.escampe.server;

import java.util.Date;

import javax.swing.JFrame;

import jeux.modele.IArbitre;
import jeux.modele.IJoueur;
import jeux.escampe.gui.Applet;
import jeux.escampe.joueur.Arbitre;
import jeux.escampe.joueur.JoueurAleatoire;

/**
 * Petite Classe toute simple qui vous montre comment on peut lancer une partie sur deux IJoueurs...
 * Cela vous servira a debugger facilement votre projet en conditions presque reelles de tournoi
 * 
 * Attention, l'arbitre n'est pas lancé dessus, mais comme il s'agit de deux IJoueur à vous il n'est
 * pas nécessaire de vérifier la validité des coups (bien entendu)
 * 
 * Par contre, comme rien ne vérifie la fin de partie (pas d'arbitre), vos IJoueur devront renvoyer
 * la chaine "xxxxx" pour dire que la partie est finie.
 * 
 * Cette classe n'affiche rien : elle se contente de donner la main alternativement aux deux
 * joueurs.
 * 
 * 2008-2012
 */
public class Solo {
    private static IJoueur joueurBlanc;
    private static IJoueur joueurNoir;
    private static IArbitre arbitre;
    
    // Ne pas modifier ces constantes, elles seront utilisees par l'arbitre
    private final static int BLANC = -1;
    private final static int NOIR = 1;
    
    private static int nbCoups = 0;
    
    // Par défaut, on a une applet graphique
    static boolean APPLETGRAPHIQUE = true;

    // applet game viewer
    static private Applet vueDuJeu;
	static private JFrame f = null;
	
	static int[][] plateau = new int[6][6];
    
    
    /**
     * Pour éviter de toujours envoyer des lignes de commandes, vous pouvez renvoyer automatiquement
     * dans cette méthode votre joueur par défaut. Attention, il faut bien remplir le return new
     * VOTREJOUEUR() pour que cela fonctionne la classe implantee renvoyee doit implanter
     * l'interface IJoueur...
     * 
     * @param s
     * @return Ijoueur un joueur demande
     */
    private static IJoueur getDefaultPlayer(String s) {
    	System.out.println(s + " : defaultPlayer");
    	// vous devez faire qq chose comme return new MonMeilleurJoueur();
    	return new JoueurAleatoire();
    }
    
    /**
     * Juste pour rendre le tout plus generique, et vous donner une idee de comment le tournoi sera
     * lance automatiquement, voici une methode permettant de charger une certaine classe implantant
     * un IJoueur
     * 
     * @param classeJoueur
     * @param s
     * @return la classe chargee dynamiquement
     */
    private static IJoueur loadNamedPlayer(String classeJoueur, String s) {
    	IJoueur joueur;
    	System.out.print(s + " : Chargement de la classe joueur " + classeJoueur + "... ");
    	try {
    		Class<?> cjoueur = Class.forName(classeJoueur);
    		joueur = (IJoueur) cjoueur.newInstance();
    	}
    	catch (Exception e) {
    		System.out.println("Erreur de chargement");
    		System.out.println(e);
    		return null;
    	}
    	System.out.println("Ok");
    	return joueur;
	}
	
	public static int[] _1DTo2D(int _1d) {
		return new int[] { _1d % 6, _1d / 6 };
	}
	public static int posStringToPosInTab(String posStr) {
		String[] strPosToTabPos = new String[] { "A", "B", "C", "D", "E", "F" };
		String[] posStrTab = posStr.toUpperCase().split("");

		try {
			if (posStrTab.length != 2 || Integer.parseInt(posStrTab[1]) > 6)
				return -1;
		} catch (NumberFormatException e) {
			return -1;
		}

		for (int i = 0; i < 6; i++) {
			if (strPosToTabPos[i].equals(posStrTab[0]))
				return (Integer.parseInt(posStrTab[1]) - 1) * 6 + i;
		}
		return -1;
	}
    
    /**
     * Boucle principale du jeu, en utilisant une version de l'arbitre identique a celle du tournoi
     * L'arbitre sera le garant de la validite des coups, et de leur affichage standard pour la
     * publication via le site web.
     * 
     * @param joueurBlanc
     * @param joueurNoir
     */
    public static void gameLoop(IJoueur joueurBlanc, IJoueur joueurNoir, IArbitre arbitre) {
    	String coup;
    	IJoueur joueurCourant = joueurNoir; // Dans Escampe le joueur Noir commence
    	
    	while (true) {

            if (arbitre.GameOver()){
                break;
            }

    		nbCoups++;
    		
    		System.out.println("\n*********\nOn demande à " + joueurCourant.binoName() + " (" + joueurCourant.getNumJoueur() + ") de jouer...");
    		long waitingTime1 = new Date().getTime();
    		
			coup = joueurCourant.choixMouvement();
			String[] coupSplit;

			if(coup.length() == 17){
				int[] i;
				boolean prems = true;
				for(String s : coup.split("/")){
					i = _1DTo2D(posStringToPosInTab(s));
					if(prems){
						plateau[i[1]][i[0]] = (joueurCourant.getNumJoueur() == -1 ? -2 : 2);
						prems = false;
					}
					else plateau[i[1]][i[0]] = joueurCourant.getNumJoueur();
				}
			}
			else if(coup.length() == 5){
				coupSplit = coup.split("-");
				int[] i0, i1;
				i0 = _1DTo2D(posStringToPosInTab(coupSplit[0]));
				i1 = _1DTo2D(posStringToPosInTab(coupSplit[1]));
				plateau[i1[1]][i1[0]] = plateau[i0[1]][i0[0]];
				plateau[i0[1]][i0[0]] = 0;
			}
			vueDuJeu.addBoard(coup, plateau);
    		
    		long waitingTime2 = new Date().getTime();
    		// On rajoute 1 pour eliminer les temps infinis
    		long waitingTime = waitingTime2 - waitingTime1 + 1;
            System.out.println("Le joueur " + joueurCourant.binoName() + " a joué le coup " + coup + " en " + waitingTime + "s.");
            
            arbitre.play(joueurCourant.getNumJoueur(), coup);

            if (nbCoups == 2) { // Dans Escampe le joueur Blanc rejoue après avoir posé ses pièces
    			// On avertit le joueur Noir du placement des pièces
    			joueurNoir.mouvementEnnemi(coup);
    		}
    		else {
    			if (joueurCourant.getNumJoueur() == BLANC)
    				joueurCourant = joueurNoir;
    			else
    				joueurCourant = joueurBlanc;
    			
    			// On avertit le second joueur du coup calcule par le precedent
    			joueurCourant.mouvementEnnemi(coup);
    			// Ce sera ensuite à lui de jouer de nouveau en haut de la boucle
            }
            
            // try {
            //     Thread.sleep(500);
            // } catch (Exception e){
            //     e.printStackTrace();
            // }   
    	}
    	
    	System.out.println("Partie finie en " + nbCoups + " coups.\n");	
    }
    
    /**
     * On charge eventuellement les classes demandee pour les joueurs, et on lance la boucle
     * principale
     * 
     * @param args
     */
    public static void main(String args[]) {
    	// S'il le faut, on initialise l'applet graphique
    	if (APPLETGRAPHIQUE) {
    		f = new JFrame("Vue du jeu");
    		vueDuJeu = new Applet();
    		vueDuJeu.buildUI(f.getContentPane());
    		f.setSize(vueDuJeu.getDimension());
    		vueDuJeu.setMyFrame(f);
			f.setVisible(true);
    		vueDuJeu.addBoard("Départ ", plateau);
			vueDuJeu.update(f.getGraphics(), f.getInsets());
    	}
    	
    	System.out.println("Partie solo ...");
    	
    	if (args.length == 0) { // On a deux classes à charger
    		joueurBlanc = getDefaultPlayer("Blanc");
    		joueurNoir = getDefaultPlayer("Noir");
    	}
    	else if (args.length == 1) {
    		joueurBlanc = loadNamedPlayer(args[0], "Blanc");
    		joueurNoir = loadNamedPlayer(args[0], "Noir");
    	}
    	else {
    		joueurBlanc = loadNamedPlayer(args[0], "Blanc");
    		joueurNoir = loadNamedPlayer(args[1], "Noir");
        }
        
        System.out.println(args.length);
        for(String a: args){
            System.out.println(a);
        }
    	
    	joueurBlanc.initJoueur(BLANC);
    	System.out.println("Joueur Blanc : " + joueurBlanc.binoName());
    	
    	joueurNoir.initJoueur(NOIR);
        System.out.println("Joueur Noir : " + joueurNoir.binoName());
        
        arbitre = new Arbitre();
    	
    	System.out.println("Initialisation des deux joueurs et de l'arbitre ok.");
    	
    	gameLoop(joueurBlanc, joueurNoir, arbitre);
    }
}

