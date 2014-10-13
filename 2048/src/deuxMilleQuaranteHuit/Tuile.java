package deuxMilleQuaranteHuit;

public class Tuile
{
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
		while(grille.getTourActuel().executer(new Deplacement(grille, this, direction)));
		grille.getTourActuel().executer(new Fusion(grille, this, direction));
	}
	
	int distance(Tuile autre)
	{
		return getCoordonnees().distance(autre.getCoordonnees());
	}
	
	@Override
	public String toString()
	{
		return "" + String.format("%" + LARGEUR_TUILE + "d", valeur);
	}
}

class TuileFusionnee extends Tuile
{
	TuileFusionnee(Grille grille, Tuile source, Tuile destination)
	{
		super(grille, null, source.getValeur() * 2);
		if (source.getTour() == grille.getTourActuel() || destination.getTour() == grille.getTourActuel())
			throw new RuntimeException("Une des tuiles a déjà été crée lors de ce tour.");
		if (source.getValeur() != destination.getValeur())
			throw new RuntimeException("Impossible de fusionner des tuiles de valeurs différentes.");
		if (source.distance(destination) != 1)
			throw new RuntimeException("Impossible de fusionner des tuiles non adjacentes.");
	}
}
