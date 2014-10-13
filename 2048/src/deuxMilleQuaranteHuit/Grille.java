package deuxMilleQuaranteHuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class Grille
{
	final Direction GAUCHE, DROITE, HAUT, BAS;
	private int nbLignes, nbColonnes;
	private Map<Coordonnees, Tuile> tuiles;
	private Random random = new Random();
	
	Grille(int nbLignes, int nbColonnes)
	{
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		tuiles = new HashMap<>();
		GAUCHE = new Direction(0, -1);
		DROITE = new Direction(0, 1);
		HAUT = new Direction(-1, 0);
		BAS = new Direction(1, 0);
		new Tuile(nextCoordonnees(), 2);
	}
	
	private int absolut(int x)
	{
		return (x >= 0) ? x : -x;
	}
	
	private int nextInt(int maxValue)
	{
		return absolut(random.nextInt())%nbLignes;
	}
	
	private List<Coordonnees> casesVides()
	{
		List<Coordonnees> liste = new ArrayList<Coordonnees>();
		return liste;
	}
	
	private Coordonnees nextCoordonnees()
	{
		return new Coordonnees(nextInt(nbLignes), nextInt(nbColonnes));
	}

	Tuile get(Coordonnees coordonnees)
	{
		return tuiles.get(coordonnees);
	}
	
	private void set(Coordonnees coordonnees, Tuile tuile)
	{
		if (coordonnees != null)
			tuiles.put(coordonnees, tuile);
	}
	
	private void set(int ligne, int colonne, Tuile tuile)
	{
		set(new Coordonnees(ligne, colonne), tuile);
	}
	
	public boolean estVide(Coordonnees coordonnees)
	{
		return get(coordonnees) == null;
	}
	
	private Tuile get(int ligne, int colonne)
	{
		return get(new Coordonnees(ligne, colonne));
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
		tuiles.clear();
	}
	
	void mouvement(Direction direction, Coordonnees source)
	{
		Direction oppose = direction.oppose();
		Coordonnees maCase = source;
		while(maCase.verifie())
		{
			if (!estVide(maCase))
				get(maCase).mouvement(direction);
			maCase = maCase.plus(oppose);
		}
	}
	
	void mouvement(Direction direction)
	{		
		for (Coordonnees c : direction.bord())
		{
			mouvement(direction, c);
		}
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
			res += "| ";
			for (int j = 0 ; j < nbColonnes ; j++)
			{
				Tuile tuile = get(i, j);  
				res += (tuile != null) ? tuile.toString() : " ";
				res += " | ";
			}
			res += "\n+\n";
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
		
		boolean estVide()
		{
			return verifie() && Grille.this.estVide(this);
		}

		@Override
		public boolean equals(Object o)
		{
			Coordonnees other = (Coordonnees)o;
			return getLigne() == other.getLigne() 
					&& getColonne() == other.getColonne();
		}
		
		@Override
		public String toString()
		{
			return "(" + ligne + ", " + colonne + ")";
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
		
		public Direction oppose()
		{
			return new Direction(-getLigne(), -getColonne());
		}
		
		private int depart(int coordonnee, int limite)
		{
			if (coordonnee <= 0)
				return 0;
			return (limite - 1);
		}
		
		private Coordonnees depart()
		{
			return new Coordonnees(depart(getLigne(), getNbLignes()), 
							depart(getColonne(), getNbColonnes()));
		}
		
		private Direction delta()
		{
			return new Direction((getColonne() != 0) ? 1 : 0, 
					(getLigne() != 0) ? 1 : 0);
		}
		
		public List<Coordonnees> bord()
		{
			List<Coordonnees> liste = new ArrayList<>();
			Coordonnees point = depart();
			Direction delta = delta();
			while(point.verifie())
			{
				liste.add(point);
				point = point.plus(delta);
			}
			return liste;
		}
	}
	
	public class Tuile
	{
		private Coordonnees coordonnees;
		private int valeur;
		
		Tuile(Coordonnees coordonnees, int valeur)
		{
			setCoordonnees(coordonnees);
			this.valeur= valeur;
			System.out.println(this.coordonnees);
		}
		
		public int getValeur()
		{
			return valeur;
		}
		
		Coordonnees getCoordonnees()
		{
			return coordonnees;
		}
		
		void setCoordonnees(Coordonnees coordonnees)
		{
			if (this.coordonnees != coordonnees)
			{
				set(this.coordonnees, null);
				this.coordonnees = coordonnees;
				set(this.coordonnees, this);
			}
		}
		
		void mouvement(Direction direction)
		{
			Coordonnees cible = coordonnees.plus(direction);
			while(cible.estVide())
			{
				setCoordonnees(cible);
				cible = coordonnees.plus(direction);
			}
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
