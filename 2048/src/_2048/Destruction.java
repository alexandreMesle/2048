package _2048;


class Destruction extends Operation
{
	private static final long serialVersionUID = -6836602562059509073L;
	private Coordonnees coordonnees;
	private Tuile tuile;
	
	Destruction(Partie2048 partie2048, Coordonnees coordonnees)
	{
		super(partie2048, 0);
		this.coordonnees = coordonnees;
		this.tuile = partie2048.getGrille().get(coordonnees);
		if (tuile != null)
			score = -tuile.getValeur();
	}
	
	public boolean executer()
	{
		if (tuile != null && !partie2048.getGrille().uneSeuleTuile())
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
