package _2048;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Stack;

class Partie2048 implements Serializable, Iterable<Coordonnees>
{
	private static final long serialVersionUID = -1393369598429605704L;
	private Jeu2048 jeu2048;
	final Direction GAUCHE, DROITE, HAUT, BAS;
	final static int TEMPS_ATTENTE = 5;
	private int valeurGagnante = 2048;
	private boolean valeurGagnanteAtteinte = false;
	private int score = 0;
	private Grille grille;
	private Historique<Tour> historique = new Historique<>();
	
	Partie2048(Jeu2048 jeu2048, int nbLignes, int nbColonnes, int puissanceGagnante)
	{
		this.jeu2048 = jeu2048;
		grille = new Grille(this, nbLignes, nbColonnes);
		GAUCHE = new Direction(grille, 0, -1);
		DROITE = new Direction(grille, 0, 1);
		HAUT = new Direction(grille, -1, 0);
		BAS = new Direction(grille, 1, 0);
		valeurGagnante = Utils.puissance(2, puissanceGagnante);
		commenceTour();
		executer(new Creation(this, grille.getCaseVideAleatoire(), Utils.valeurAleatoire()));
	}

	void ajouteScore(int score)
	{
		this.score += score;
		jeu2048.declencheListenerScore(this.score);
	}

	void enleveScore(int score)
	{
		this.score -= score;
		jeu2048.declencheListenerScore(this.score);
	}
	
	int getScore()
	{
		return score;
	}
	
	int getValeurGagnante()
	{
		return valeurGagnante; 
	}
	
	void setValeurGagnanteAtteinte(boolean valeur)
	{
		valeurGagnanteAtteinte = valeur; 
	}
	
	private void commenceTour()
	{
		historique.ajouter(new Tour());
	}
	
	boolean annuler()
	{
		if (historique.annulable())
		{
			jeu2048.declencheListenerTransaction(false);
			historique.annuler();
			jeu2048.declencheListenerHistorique();
			jeu2048.declencheListenerTransaction(true);
			return true;
		}
		else
			return false;
	}

	boolean retablir()
	{
		if (historique.retablissable())
		{
			jeu2048.declencheListenerTransaction(false);
			historique.retablir();
			jeu2048.declencheListenerHistorique();
			jeu2048.declencheListenerTransaction(true);
			return true;
		}
		else
			return false;
	}

	Tour getTourActuel()
	{
		return historique.getEvenementEnCours();
	}
	
	boolean executer(Operation operation)
	{
		return getTourActuel().executer(operation);
	}
	
	Comparator<Tuile> getComparateur(final Direction direction)
	{
		return new Comparator<Tuile>() 
				{
				@Override
				public int compare(Tuile tuile1, Tuile tuile2) 
				{
					return tuile2.getCoordonnees().produitScalaire(direction) -
							tuile1.getCoordonnees().produitScalaire(direction);
				}
			};
	}
	
	boolean mouvement(Direction direction)
	{		
		jeu2048.declencheListenerTransaction(false);
		commenceTour();
		List<Tuile> listeDeTuiles = new ArrayList<Tuile>(grille.getTuiles());
		Comparator<Tuile> comparateur = getComparateur(direction);
		Collections.sort(listeDeTuiles, comparateur);
		for (Tuile t : listeDeTuiles)
			t.mouvement(direction);
		if (getTourActuel().getMouvementEffectue())
			executer(new Creation(this, grille.getCaseVideAleatoire(), Utils.valeurAleatoire()));
		jeu2048.declencheListenerHistorique();
		jeu2048.declencheListenerTransaction(true);
		return getTourActuel().getMouvementEffectue();
	}
	
	boolean detruireTuile(int ligne, int colonne)
	{
		commenceTour();
		return executer(new Destruction(this, 
				new Coordonnees(grille, ligne, colonne)));
	}

	boolean gagne()
	{
		return valeurGagnanteAtteinte;
	}
	
	private boolean perd(Coordonnees source, Coordonnees destination)
	{
		return (destination.verifie() && 
					(grille.estVide(destination) || 
					grille.get(destination).getValeur() == grille.get(source).getValeur())
				);

	}
	
	boolean perd()
	{
		for (Coordonnees coordonnees : grille)
		{
			if (grille.estVide(coordonnees))
				return false;
			if (perd (coordonnees, coordonnees.plus(BAS)))
				return false;
			if (perd (coordonnees, coordonnees.plus(DROITE)))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString()
	{
		int largeurLigne = Tuile.LARGEUR_TUILE * grille.getNbColonnes() 
				+ 3 * (grille.getNbColonnes() - 1) + 4;
		char symboleLigne = '-';
		String res = "Score = " + getScore() + "\n";		
		res += Utils.ligne(largeurLigne, symboleLigne) + "\n";
		for (int i = 0 ; i < grille.getNbLignes(); i++)
		{
			res += "| ";
			for (int j = 0 ; j < grille.getNbColonnes() ; j++)
			{
				Tuile tuile = grille.get(i, j);  
				res += (tuile != null) ? tuile.toString() : String.format("%" + Tuile.LARGEUR_TUILE + "s", "");
				res += " | ";
			}
			res += "\n" + Utils.ligne(largeurLigne, symboleLigne) + "\n";
		}
		return res;
	}
	
	
	int getNbLignes()
	{
		return grille.getNbLignes();
	}
	
	int getNbColonnes()
	{
		return grille.getNbColonnes();
	}
	
	Grille getGrille()
	{
		return grille;
	}

	@Override
	public Iterator<Coordonnees> iterator()
	{
		return grille.iterator();
	}
	
	Tuile get(Coordonnees coordonnees)
	{
		return grille.get(coordonnees);
	}
	
	boolean annulable()
	{
		return historique.annulable();
	}
	
	boolean retablissable()
	{
		return historique.retablissable();
	}
	
	void declencheListenerCoordonnees(Coordonnees coordonnees)
	{
		jeu2048.declencheListenerCoordonnees(coordonnees);
	}
}
