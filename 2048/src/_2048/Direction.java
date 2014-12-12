package _2048;


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
}
