package _2048;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Coordonnees implements Serializable
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

	boolean verifie()
	{
		return 0 <= ligne && ligne < grille.getNbLignes() && 0 <= colonne
				&& colonne < grille.getNbColonnes();
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
}

class Direction extends Coordonnees
{
	private static final long serialVersionUID = -2309155139265325090L;

	Direction(Grille grille, Coordonnees direction)
	{
		this(grille, direction.getLigne(), direction.getColonne());
	}

	Direction(Grille grille, int ligne, int colonne)
	{
		super(grille, ligne, colonne);
	}

	public Direction oppose()
	{
		return new Direction(grille, -getLigne(), -getColonne());
	}

	private int depart(int coordonnee, int limite)
	{
		if (coordonnee <= 0)
			return 0;
		return (limite - 1);
	}

	private Coordonnees depart()
	{
		return new Coordonnees(grille,
				depart(getLigne(), grille.getNbLignes()), depart(getColonne(),
						grille.getNbColonnes()));
	}

	private Direction delta()
	{
		return new Direction(grille, (getColonne() != 0) ? 1 : 0,
				(getLigne() != 0) ? 1 : 0);
	}

	public List<Coordonnees> bord()
	{
		List<Coordonnees> liste = new ArrayList<>();
		Coordonnees point = depart();
		Direction delta = delta();
		while (point.verifie())
		{
			liste.add(point);
			point = point.plus(delta);
		}
		return liste;
	}
}
