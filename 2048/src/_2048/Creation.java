package _2048;


class Creation extends Operation
{
	private static final long serialVersionUID = -5040720192393632469L;
	private Coordonnees coordonnees; 
	private Tuile tuile;
	
	Creation(Partie2048 partie2048, Coordonnees coordonnees, int valeur)
	{
		super(partie2048, 0);
		this.coordonnees = coordonnees;
		this.tuile = new Tuile(partie2048, null, valeur);
	}

	@Override
	public boolean executer()
	{
		tuile.setCoordonnees(coordonnees);
		return super.executer();
	}
	
	@Override
	public void annuler()
	{
		tuile.setCoordonnees(null);
		super.annuler();
	}
}
