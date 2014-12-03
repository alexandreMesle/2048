package _2048;

class Fusion extends Operation
{
	private static final long serialVersionUID = 6846331205920753895L;
	private Tuile source, destination;
	private Coordonnees coordonneesSource, coordonneesDestination;
	private TuileFusionnee tuile;
	
	Fusion(Grille grille, Tuile source, Direction direction)
	{
		super(grille, source.getValeur()*2);
		this.source = source;
		coordonneesSource = source.getCoordonnees();
		coordonneesDestination = source.getCoordonnees().plus(direction);
		destination = grille.get(coordonneesDestination);
		tuile = new TuileFusionnee(grille, source, destination);
		changeScore = true;
	}
	
	public boolean executer()
	{
		if (destination == null)
			return false;
		if (source.getTour() == grille.getTourActuel() || destination.getTour() == grille.getTourActuel())
			return false;
		if (source.getValeur() != destination.getValeur())
			return false;
		if (source.distance(destination) != 1)
			return false;
		tuile.setCoordonnees(destination.getCoordonnees());
		source.setCoordonnees(null);
		if (!grille.gagne() && tuile.getValeur() == grille.getValeurGagnante())
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
