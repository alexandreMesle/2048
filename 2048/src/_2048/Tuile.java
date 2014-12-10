package _2048;

import java.io.Serializable;

public class Tuile implements Serializable
{
	private static final long serialVersionUID = 8239151527953937239L;
	final static int LARGEUR_TUILE= 5; 
	private Partie2048 partie2048;
	private Coordonnees coordonnees;
	private int valeur;
	private Tour tour;
	
	Tuile(Partie2048 partie2048, Coordonnees coordonnees, int valeur)
	{
		this.partie2048 = partie2048;
		setCoordonnees(coordonnees);
		this.valeur= valeur;
		this.tour = partie2048.getTourActuel();
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
			partie2048.getGrille().supprime(this);
			this.coordonnees = coordonnees;
			partie2048.getGrille().set(this.coordonnees, this);
		}
	}
	
	void mouvement(Direction direction)
	{
		while(partie2048.executer(new Deplacement(partie2048, this, direction)));
		partie2048.executer(new Fusion(partie2048, this, direction));
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

	TuileFusionnee(Partie2048 partie2048, Tuile source, Tuile destination)
	{
		super(partie2048, null, source.getValeur() * 2);
	}
}
