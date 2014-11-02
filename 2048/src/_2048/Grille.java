package _2048;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

// TODO utiliser un tri topologique pour les déplacements
// TODO fusionner les deux historiques

class Grille implements Serializable, Iterable<Coordonnees>
{
	private static final long serialVersionUID = -1393369598429605704L;
	final Direction GAUCHE = new Direction(this, 0, -1),
			DROITE = new Direction(this, 0, 1),
			HAUT = new Direction(this, -1, 0),
			BAS = new Direction(this, 1, 0);
	final static int TEMPS_ATTENTE = 5;
	private int nbLignes, nbColonnes;
	private Map<Coordonnees, Tuile> tuiles = new HashMap<>();
	private List<Coordonnees> coordonnees = new LinkedList<>();
	private int valeurGagnante = 2048;
	private boolean valeurGagnanteAtteinte = false;
	private int score = 0;
	private Stack<Tour> tours = new Stack<>(), toursRetablir = new Stack<>();
	private transient Listener<Coordonnees> coordonneesListener = null;
	private transient Listener<Integer> scoreListener = null;
	private transient Listener<Boolean> annulableListener = null, 
			retablissableListener = null;
	
	Grille(int nbLignes, int nbColonnes, int puissanceGagnante)
	{
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		valeurGagnante = Utils.puissance(2, puissanceGagnante);
		for (int i = 0 ;  i < nbLignes; i++)
			for (int j = 0 ; j < nbColonnes ; j++)
				coordonnees.add(new Coordonnees(this, i, j));
		ajouteTour();
		executer(new Creation(this, coordonneesAleatoires(), valeurAleatoire()));
	}
	
	private List<Coordonnees> casesVides()
	{
		List<Coordonnees> liste = new ArrayList<Coordonnees>();
		for (Coordonnees coordonnees : this.coordonnees)
			if (estVide(coordonnees))
				liste.add(coordonnees);
		return liste;
	}
	
	private Coordonnees coordonneesAleatoires()
	{
		List<Coordonnees> casesVides = casesVides();
		return casesVides.get(Utils.nextInt(casesVides.size()));
	}

	private int valeurAleatoire()
	{
		return (Utils.nextBoolean()) ? 2 : 4;
	}

	Tuile get(Coordonnees coordonnees)
	{
		return tuiles.get(coordonnees);
	}
	
	void set(Coordonnees coordonnees, Tuile tuile)
	{
		if (coordonnees != null)
		{
			tuiles.put(coordonnees, tuile);
			actionPerformed(coordonnees);
		}
		else
			supprime(tuile);		
	}

	void supprime(Tuile tuile)
	{
		Coordonnees coordonnees = tuile.getCoordonnees();
		tuiles.remove(coordonnees);
		actionPerformed(coordonnees);
	}
	
	boolean detruireTuile(Coordonnees coordonnees)
	{
		ajouteTour();
		return executer(new Destruction(this, coordonnees));
	}

	boolean estVide(Coordonnees coordonnees)
	{
		return !tuiles.containsKey(coordonnees);
	}
	
	private Tuile get(int ligne, int colonne)
	{
		return get(new Coordonnees(this, ligne, colonne));
	}
	
	int getNbLignes()
	{
		return nbLignes;
	}
	
	int getNbColonnes()
	{
		return nbColonnes;
	}
	
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
	
	boolean uneSeuleTuile()
	{
		return tuiles.size() == 1;
	}
	
	private void ajouteTour()
	{
		tours.add(new Tour());
		toursRetablir.clear();		
	}
	
	private boolean annulable()
	{
		return tours.size() != 1;
	}

	private boolean retablissable()
	{
		return !toursRetablir.isEmpty();
	}

	
	boolean annuler()
	{
		if (annulable())
		{
			getTourActuel().annuler();
			toursRetablir.add(getTourActuel());
			tours.pop();
			actionPerformedHistorique();
			return true;
		}
		else
			return false;
	}

	boolean retablir()
	{
		if (retablissable())
		{
			tours.add(toursRetablir.peek());
			toursRetablir.pop();
			tours.peek().retablir();
			actionPerformedHistorique();
			return true;
		}
		else
			return false;
	}

	Tour getTourActuel()
	{
		return tours.peek();
	}
	
	boolean executer(Operation operation)
	{
		return getTourActuel().executer(operation);
	}
	
	void mouvement(final Direction direction, final Coordonnees source)
	{
		final Direction oppose = direction.oppose();
		Runnable r = new Runnable()
		{
			public void run()
			{
				getTourActuel().ajouteThread();
				Coordonnees maCase = source;
				while(maCase.verifie())
				{
					if (!estVide(maCase))
						get(maCase).mouvement(direction);
					maCase = maCase.plus(oppose);
					try{Thread.sleep(TEMPS_ATTENTE);}
					catch (InterruptedException e){e.printStackTrace();}
				}
				getTourActuel().enleveThread();
				synchronized(Grille.this)
				{
					Grille.this.notifyAll();
				}
			}
		};
		(new Thread(r)).start();
	}	
	
	boolean mouvement(Direction direction)
	{		
		ajouteTour();
		for (Coordonnees c : direction.bord())
			mouvement(direction, c);
		do
		{
			try
			{
				synchronized (this)
				{
					wait();
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		} 
		while (getTourActuel().threadsEnCours());
		if (getTourActuel().getMouvementEffectue())
			executer(new Creation(this, coordonneesAleatoires(), valeurAleatoire()));
		actionPerformedHistorique();
		return getTourActuel().getMouvementEffectue();
	}
	
	boolean gagne()
	{
		return valeurGagnanteAtteinte;
	}
	
	private boolean perd(Coordonnees source, Coordonnees destination)
	{
		return (destination.verifie() && 
				(estVide(destination) || get(destination).getValeur() == get(source).getValeur()));

	}
	
	boolean perd()
	{
		for (Coordonnees coordonnees : this.coordonnees)
		{
			if (estVide(coordonnees))
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
		int largeurLigne = Tuile.LARGEUR_TUILE * getNbColonnes() + 3 * (getNbColonnes() - 1) + 4;
		char symboleLigne = '-';
		String res = "Score = " + getScore() + "\n";		
		res += Utils.ligne(largeurLigne, symboleLigne) + "\n";
		for (int i = 0 ; i < nbLignes ; i++)
		{
			res += "| ";
			for (int j = 0 ; j < nbColonnes ; j++)
			{
				Tuile tuile = get(i, j);  
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
			for (Coordonnees coordonnees : tuiles.keySet())
				actionPerformed(coordonnees);
	}
	
	void actionPerformed(Coordonnees coordonnees)
	{
		if (coordonneesListener != null && coordonnees != null)
			coordonneesListener.actionPerformed(coordonnees);	
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
			annulableListener.actionPerformed(annulable());
		if (retablissableListener != null)
			retablissableListener.actionPerformed(retablissable());
	}

	@Override
	public Iterator<Coordonnees> iterator()
	{
		return coordonnees.iterator();
	}
}
