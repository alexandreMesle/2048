package _2048;


import java.io.Serializable;

public class Coordonnees implements Serializable, Comparable<Coordonnees>
{
	private static final long serialVersionUID = 8976684575490475011L;
	private int ligne, colonne;
	protected Grille grille;

	Coordonnees(Grille grille, int ligne, int colonne)
	{
		this.grille = grille;
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
		return new Coordonnees(grille, ligne + autre.getLigne(), colonne
				+ autre.getColonne());
	}

	int produitScalaire(Coordonnees autre)
	{
		return ligne * autre.ligne + colonne * autre.colonne; 
	}
	
	boolean verifie()
	{
		return 0 <= ligne && ligne < grille.getNbLignes() && 
			0 <= colonne && colonne < grille.getNbColonnes();
	}

	boolean estVide()
	{
		return verifie() && grille.estVide(this);
	}

	@Override
	public int hashCode()
	{
		return getLigne() * grille.getNbColonnes() + getColonne();
	}

	@Override
	public boolean equals(Object o)
	{
		Coordonnees other = (Coordonnees) o;
		return getLigne() == other.getLigne()
				&& getColonne() == other.getColonne();
	}

	public int distance(Coordonnees autre)
	{
		return Utils.valeurAbsolue(getLigne() - autre.getLigne())
				+ Utils.valeurAbsolue(getColonne() - autre.getColonne());
	}

	@Override
	public String toString()
	{
		return "(" + ligne + ", " + colonne + ")";
	}

	@Override
	public int compareTo(Coordonnees autre)
	{
		if (getLigne() == autre.getLigne())
			return getColonne() - autre.getColonne();
		return getLigne() - autre.getLigne();
	}
}
