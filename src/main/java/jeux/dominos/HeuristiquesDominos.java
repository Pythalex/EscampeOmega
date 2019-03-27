package jeux.dominos;

import iia.jeux.alg.Heuristique;
import iia.jeux.modele.PlateauJeu;
import iia.jeux.modele.joueur.Joueur;
import iia.jeux.modele.CoupJeu;


public class HeuristiquesDominos{

	public static  Heuristique hblanc = new Heuristique(){
				
		public int eval(PlateauJeu p, Joueur j){
			PlateauDominos pbis = (PlateauDominos)p;

			if (pbis.finDePartie()){
				if (pbis.isJoueurBlanc(j))
					return Integer.MIN_VALUE;
				else
					return Integer.MAX_VALUE;
			}

			return pbis.nbCoupsBlanc() - pbis.nbCoupsNoir();
		}
	};

	public static  Heuristique hnoir = new Heuristique(){
	
		public int eval(PlateauJeu p, Joueur j){
			PlateauDominos pbis = (PlateauDominos)p;

			if (pbis.finDePartie()){
				if (pbis.isJoueurNoir(j))
					return Integer.MIN_VALUE;
				else
					return Integer.MAX_VALUE;
			}

			return pbis.nbCoupsNoir() - pbis.nbCoupsBlanc();
		}
	};

}
