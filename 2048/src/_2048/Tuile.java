package _2048;

import java.io.Serializable;

public class Tuile implements Serializable
{
	private static final long serialVersionUID = 8239151527953937239L;
	final static int LARGEUR_TUILE= 5; 
	private Grille grille;
	private Coordonnees coordonnees;
	private int valeur;
	private Tour tour;
	
	Tuile(Grille grille, Coordonnees coordonnees, int valeur)
	{
		this.grille = grille;
		setCoordonnees(coordonnees);
		this.valeur= valeur;
		this.tour = grille.getTourActuel();
	}
	
	public int getValeur()
	{
		return valeur;
	}
	
	public Tour getTour()
	{
		return tour;
	}
	
	Coordonnees getCoordonnees()
	{
		return coordonnees;
	}
	
	void setCoordonnees(Coordonnees coordonnees)
	{
		if (this.coordonnees != coordonnees)
		{
			grille.supprime(this);
			this.coordonnees = coordonnees;
			grille.set(this.coordonnees, this);
		}
	}
	
	void mouvement(Direction direction)
	{
		while(grille.executer(new Deplacement(grille, this, direction)));
		grille.executer(new Fusion(grille, this, direction));
	}
	
	int distance(Tuile autre)
	{
		return getCoordonnees().distance(
				autre.getCoordonnees());
	}
	
	@Override
	public String toString()
	{
		return "" + String.format("%" + LARGEUR_TUILE + "d", valeur);
	}
}

class TuileFusionnee extends Tuile
{
	private static final long serialVersionUID = -184129757574441051L;

	TuileFusionnee(Grille grille, Tuile source, Tuile destination)
	{
		super(grille, null, source.getValeur() * 2);
	}
}
