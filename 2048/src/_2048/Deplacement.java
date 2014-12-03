package _2048;

public class Deplacement extends Operation
{
	private static final long serialVersionUID = 4345504943178469271L;
	private Coordonnees source, destination;
	private Tuile tuile;

	Deplacement(Grille grille, Tuile tuile, Direction direction)
	{
		super(grille, 0);
		this.tuile = tuile;
		source = tuile.getCoordonnees();
		destination = source.plus(direction);
	}
	
	@Override
	public boolean executer()
	{
		if (destination.estVide())
			tuile.setCoordonnees(destination);
		else
			return false;
		return super.executer();
	}
	
	@Override
	public void annuler()
	{
		tuile.setCoordonnees(source);
		super.annuler();
	}
}

