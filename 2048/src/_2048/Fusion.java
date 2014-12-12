package _2048;


class Fusion extends Operation
{
	private static final long serialVersionUID = 6846331205920753895L;
	private Tuile source, destination;
	private Coordonnees coordonneesSource, coordonneesDestination;
	private TuileFusionnee tuile;
	
	Fusion(Partie2048 partie2048, Tuile source, Direction direction)
	{
		super(partie2048, source.getValeur()*2);
		this.source = source;
		coordonneesSource = source.getCoordonnees();
		coordonneesDestination = source.getCoordonnees().plus(direction);
		destination = partie2048.getGrille().get(coordonneesDestination);
		tuile = new TuileFusionnee(partie2048, source, destination);
		changeScore = true;
	}
	
	public boolean executer()
	{
		if (destination == null)
			return false;
		if (source.getTour() == partie2048.getTourActuel() || destination.getTour() == partie2048.getTourActuel())
			return false;
		if (source.getValeur() != destination.getValeur())
			return false;
		if (source.distance(destination) != 1)
			return false;
		tuile.setCoordonnees(destination.getCoordonnees());
		source.setCoordonnees(null);
		if (!partie2048.gagne() && tuile.getValeur() == partie2048.getValeurGagnante())
			setOperationGagnante();
		return super.executer();
	}
	
	@Override
	public void annuler()
	{
		tuile.setCoordonnees(null);
		source.setCoordonnees(coordonneesSource);
		destination.setCoordonnees(coordonneesDestination);
		super.annuler();
	}
}
