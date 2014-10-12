package deuxMilleQuaranteHuit;

import java.util.Random;

class Grille
{
	final Direction GAUCHE, DROITE, HAUT, BAS;
	private int nbLignes, nbColonnes;
	private Tuile[][] tuiles;
	private Random random = new Random();
	
	Grille(int nbLignes, int nbColonnes)
	{
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		tuiles = new Tuile[nbLignes][nbColonnes];
		GAUCHE = new Direction(0, -1);
		DROITE = new Direction(0, 1);
		HAUT = new Direction(-1, 0);
		BAS = new Direction(1, 0);
		new Tuile(nextCoordonnees(), 2);
	}
	
	private Coordonnees nextCoordonnees()
	{
		return new Coordonnees(random.nextInt()%nbLignes, random.nextInt()%nbColonnes);
	}

	Tuile get(Coordonnees coordonnees)
	{
		return get(coordonnees.getLigne(), coordonnees.getColonne());
	}
	
	private void set(Coordonnees coordonnes, Tuile tuile)
	{
		if (coordonnes != null)
			set(coordonnes.getLigne(), coordonnes.getColonne(), tuile);
	}
	
	private void set(int ligne, int colonne, Tuile tuile)
	{
		if (tuiles[ligne][colonne] != null)
			tuiles[ligne][colonne].setCoordonnees(null);
		tuiles[ligne][colonne] = tuile;
		if (tuile != null)
			tuile.setCoordonnees(new Coordonnees(ligne, colonne));
	}
	
	private Tuile get(int ligne, int colonne)
	{
		return tuiles[ligne][colonne];
	}
	
	int getNbLignes()
	{
		return nbLignes;
	}
	
	int getNbColonnes()
	{
		return nbColonnes;
	}
	
	void reset()
	{
		tuiles = new Tuile[nbLignes][nbColonnes];
	}
	
	void mouvement(Direction direction)
	{
		
	}
	
	boolean gagne()
	{
		return false;
	}
	
	boolean perd()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		String res = "";
		for (int i = 0 ; i < nbLignes ; i++)
		{
			for (int j = 0 ; j < nbColonnes ; j++)
			{
				Tuile tuile = get(i, j);  
				res += (tuile != null) ? tuile.toString() : " ";
				res += " ";
			}
			res += "\n";
		}
		return res;
	}
	
	public class Coordonnees
	{
		private int ligne, colonne;
		
		Coordonnees(int ligne, int colonne)
		{
			this.ligne = ligne;
			this.colonne = colonne;
		}
		
		int getLigne()
		{
			return ligne;
		}
		
		int getColonne()
		{
			return colonne;
		}
		
		Coordonnees plus(Coordonnees autre)
		{
			return new Coordonnees(ligne + autre.getLigne(), colonne + autre.getColonne());
		}
		
		boolean verifie()
		{
			return 0 <= ligne && ligne < getNbLignes() 
					&& 0 <= colonne && colonne < getNbColonnes(); 
		}
	}
	
	class Direction extends Coordonnees
	{
		private Direction(Coordonnees direction)
		{
			this(direction.getLigne(), direction.getColonne());
		}
		
		private Direction(int ligne, int colonne)
		{
			super(ligne, colonne);
		}
	}
	
	public class Tuile
	{
		private Coordonnees coordonnees;
		private int valeur;
		
		Tuile(Coordonnees coordonnees, int valeur)
		{
			this.coordonnees = coordonnees;
			setCoordonnees(coordonnees);
			this.valeur= valeur;
		}
		
		public int getValeur()
		{
			return valeur;
		}
		
		Coordonnees getCoordonnees()
		{
			return coordonnees;
		}
		
		void setCoordonnees(Coordonnees coordonnes)
		{
			set(this.coordonnees, null);
			this.coordonnees = coordonnes;
			set(this.coordonnees, this);			
		}
		
		@Override
		public String toString()
		{
			return "" + valeur;
		}
	}
	
	class TuileFusionnee extends Tuile
	{
		private Tuile tuile1, tuile2;
		
		TuileFusionnee(Tuile source, Tuile destination)
		{
			super(destination.getCoordonnees(), source.getValeur() * 2);
			if (source.getValeur() != destination.getValeur())
				throw new RuntimeException("Impossible de fusionner des tuiles de valeurs différentes.");
			this.tuile1 = source;
			this.tuile2 = destination;
		}
	}
}
