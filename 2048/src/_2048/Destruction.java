package _2048;

class Destruction extends Operation
{
	private static final long serialVersionUID = -6836602562059509073L;
	private Coordonnees coordonnees;
	private Tuile tuile;
	
	Destruction(Grille grille, Coordonnees coordonnees)
	{
		super(grille, 0);
		this.coordonnees = coordonnees;
		this.tuile = grille.get(coordonnees);
		if (tuile != null)
			score = -tuile.getValeur();
	}
	
	public boolean executer()
	{
		if (tuile != null && !grille.uneSeuleTuile())
			tuile.setCoordonnees(null);
		else
			return false;
		return super.executer();
	}
	
	@Override
	public void annuler()
	{
		tuile.setCoordonnees(coordonnees);
		super.annuler();
	}
}
