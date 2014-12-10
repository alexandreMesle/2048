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
	final Direction GAUCHE, DROITE, HAUT, BAS;
	final static int TEMPS_ATTENTE = 5;
	private int valeurGagnante = 2048;
	private boolean valeurGagnanteAtteinte = false;
	private int score = 0;
	private Grille grille;
	private Historique<Tour> historique = new Historique<>();
	private transient Listener<Coordonnees> coordonneesListener = null;
	private transient Listener<Integer> scoreListener = null;
	transient private Listener<Boolean> transactionListener = null;
	private transient Listener<Boolean> annulableListener = null, 
			retablissableListener = null;
	
	Partie2048(int nbLignes, int nbColonnes, int puissanceGagnante)
	{
		grille = new Grille(this, nbLignes, nbColonnes);
		GAUCHE = new Direction(grille, 0, -1);
		DROITE = new Direction(grille, 0, 1);
		HAUT = new Direction(grille, -1, 0);
		BAS = new Direction(grille, 1, 0);
		valeurGagnante = Utils.puissance(2, puissanceGagnante);
		commenceTour();
		executer(new Creation(this, grille.getCaseVideAleatoire(), Utils.valeurAleatoire()));
	}
	
	boolean detruireTuile(int ligne, int colonne)
	{
		commenceTour();
		return executer(new Destruction(this, 
				new Coordonnees(grille, ligne, colonne)));
	}

//	boolean detruireTuile(Coordonnees coordonnees)
//	{
//		commenceTour();
//		return executer(new Destruction(this, coordonnees));
//	}
//
	void ajouteScore(int score)
	{
		this.score += score;
		actionPerformed(this.score);
	}

	void enleveScore(int score)
	{
		this.score -= score;
		actionPerformed(this.score);
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
			actionPerformedTransaction(false);
			historique.annuler();
			actionPerformedHistorique();
			actionPerformedTransaction(true);
			return true;
		}
		else
			return false;
	}

	boolean retablir()
	{
		if (historique.retablissable())
		{
			actionPerformedTransaction(false);
			historique.retablir();
			actionPerformedHistorique();
			actionPerformedTransaction(true);
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
		actionPerformedTransaction(false);
		commenceTour();
		List<Tuile> listeDeTuiles = new ArrayList<Tuile>(grille.getTuiles());
		Comparator<Tuile> comparateur = getComparateur(direction);
		Collections.sort(listeDeTuiles, comparateur);
		for (Tuile t : listeDeTuiles)
			t.mouvement(direction);
		if (getTourActuel().getMouvementEffectue())
			executer(new Creation(this, grille.getCaseVideAleatoire(), Utils.valeurAleatoire()));
		actionPerformedHistorique();
		actionPerformedTransaction(true);
		return getTourActuel().getMouvementEffectue();
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
	
	void setCoordonneesListener(Listener<Coordonnees> listener)
	{
		this.coordonneesListener = listener;
		if (listener != null)
			for (Coordonnees coordonnees : grille)
				actionPerformed(coordonnees);
	}
	
	void setTransactionListener(Listener<Boolean> listener)
	{
		this.transactionListener = listener;
	}
	
	void actionPerformed(Coordonnees coordonnees)
	{
		if (coordonneesListener != null && coordonnees != null)
			coordonneesListener.actionPerformed(coordonnees);	
	}

	void actionPerformedTransaction(boolean lock)
	{
		if (transactionListener != null)
			transactionListener.actionPerformed(lock);
	}

	void setScoreListener(Listener<Integer> listener)
	{
		this.scoreListener = listener;
		if (listener != null)
			actionPerformed(getScore());
	}
	
	void actionPerformed(Integer score)
	{
		if (scoreListener != null)
			scoreListener.actionPerformed(score);	
	}

	void setAnnulableListener(Listener<Boolean> listener)
	{
		this.annulableListener = listener;
		actionPerformedHistorique();
	}
	
	void setRetablissableListener(Listener<Boolean> listener)
	{
		this.retablissableListener = listener;
		actionPerformedHistorique();
	}
	
	void actionPerformedHistorique()
	{
		if (annulableListener != null)
			annulableListener.actionPerformed(historique.annulable());
		if (retablissableListener != null)
			retablissableListener.actionPerformed(historique.retablissable());
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
}
