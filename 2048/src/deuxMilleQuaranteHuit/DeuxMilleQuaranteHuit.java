package deuxMilleQuaranteHuit;

public class DeuxMilleQuaranteHuit 
{
	private Grille grille;
	
	public DeuxMilleQuaranteHuit(int nbLignes, int nbColonnes)
	{
		grille = new Grille(nbLignes, nbColonnes);
	}
	
	public void gauche()
	{
		grille.mouvement(grille.GAUCHE);
	}

	public void droite()
	{
		grille.mouvement(grille.DROITE);
	}

	public void haut()
	{
		grille.mouvement(grille.HAUT);
	}

	public void bas()
	{
		grille.mouvement(grille.BAS);

	}
	
	public void reset()
	{
		grille.reset();
	}
	
	public int getNbLignes()
	{
		return grille.getNbLignes();
	}
	
	public int getNbColones()
	{
		return grille.getNbColonnes();
	}
	
	public Grille.Tuile get(Grille.Coordonnees coordonnees)
	{
		return grille.get(coordonnees);
	}
	
	public String toString()
	{
		return grille.toString();
	}
}