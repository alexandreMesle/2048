package deuxMilleQuaranteHuit;

public class DeuxMilleQuaranteHuit 
{
	private Grille grille;
	private int nbLignes, nbColonnes, puissanceGagnante;
	
	public DeuxMilleQuaranteHuit(int nbLignes, int nbColonnes, int puissanceGagnante)
	{
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.puissanceGagnante = puissanceGagnante;
		reset();
	}
	
	public DeuxMilleQuaranteHuit(int nbLignes, int nbColonnes)
	{
		this(nbLignes, nbColonnes, 11);
	}

	public void reset()
	{
		grille = new Grille(nbLignes, nbColonnes, puissanceGagnante);
	}
	
	public boolean gauche()
	{
		return grille.mouvement(grille.GAUCHE);
	}

	public boolean droite()
	{
		return grille.mouvement(grille.DROITE);
	}

	public boolean haut()
	{
		return grille.mouvement(grille.HAUT);
	}

	public boolean bas()
	{
		return grille.mouvement(grille.BAS);
	}

	public boolean perd()
	{
		return grille.perd();
	}
	
	public boolean gagne()
	{
		return grille.gagne();
	}
	
	public int getNbLignes()
	{
		return grille.getNbLignes();
	}
	
	public int getNbColones()
	{
		return grille.getNbColonnes();
	}
	
	public Tuile get(Coordonnees coordonnees)
	{
		return grille.get(coordonnees);
	}
	
	public int getScore()
	{
		return grille.getScore();
	}
	
	public boolean detruireTuile(int ligne, int colonne)
	{
		return grille.detruireTuile(new Coordonnees(grille, ligne, colonne));
	}
	
	public boolean annuler()
	{
		return grille.annuler();
	}
	
	public boolean retablir()
	{
		return grille.retablir();
	}
	
	public String toString()
	{
		return grille.toString();
	}
}
