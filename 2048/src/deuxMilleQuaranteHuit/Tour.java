package deuxMilleQuaranteHuit;

import java.util.Stack;

class Tour
{
	private int nbThreads = 0;
	private Stack<Operation> operations = new Stack<>(), operationsRetablir = new Stack<>();
	
	boolean getBouge()
	{
		return !operations.isEmpty();
	}
	
	synchronized boolean threadsEnCours()
	{
		return nbThreads != 0;
	}
	
	synchronized void ajouteThread()
	{		
		nbThreads++;
	}

	synchronized void enleveThread()
	{
		nbThreads--;
	}
	
	synchronized boolean executer(Operation operation)
	{
		if (operation.executer())
		{
			operations.add(operation);
			return true;
		}
		return false;
	}
	
	void retablir()
	{
		while(!operationsRetablir.isEmpty())
		{
			operationsRetablir.peek().executer();
			operations.add(operationsRetablir.peek());
			operationsRetablir.pop();
		}
	}	
	
	void annuler()
	{
		while(!operations.isEmpty())
		{
			operations.peek().annuler();
			operationsRetablir.add(operations.peek());
			operations.pop();
		}
	}	
}

abstract class Operation
{
	protected int score;
	protected Grille grille;
	
	Operation (Grille grille, int score)
	{
		this.score = score;
		this.grille = grille;
	}
	
	boolean executer()
	{
		grille.ajouteScore(score);
		return true;
	}
	
	void annuler()
	{
		grille.enleveScore(score);		
	}
}

class Deplacement extends Operation
{
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
	boolean executer()
	{
		if (destination.estVide())
			tuile.setCoordonnees(destination);
		else
			return false;
		return super.executer();
	}
	
	@Override
	void annuler()
	{
		tuile.setCoordonnees(source);
		super.annuler();
	}
}

class Creation extends Operation
{
	private Coordonnees coordonnees; 
	private Tuile tuile;
	
	Creation(Grille grille, Coordonnees coordonnees, int valeur)
	{
		super(grille, 0);
		this.coordonnees = coordonnees;
		this.tuile = new Tuile(grille, null, valeur);
	}

	@Override
	boolean executer()
	{
		tuile.setCoordonnees(coordonnees);
		return super.executer();
	}
	
	@Override
	void annuler()
	{
		tuile.setCoordonnees(null);
		super.annuler();
	}
}

class Fusion extends Operation
{
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
	}
	
	boolean executer()
	{
		if (destination != null && source.getValeur() == destination.getValeur()
				&& destination.getTour() != grille.getTourActuel())
			tuile = new TuileFusionnee(grille, source, destination);
		else
			return false;
		tuile.setCoordonnees(destination.getCoordonnees());
		source.setCoordonnees(null);
		if (tuile.getValeur() == grille.getValeurGagnante())
			grille.setValeurGagnanteAtteinte(true);
		return super.executer();
	}
	
	@Override
	void annuler()
	{
		tuile.setCoordonnees(null);
		source.setCoordonnees(coordonneesSource);
		destination.setCoordonnees(coordonneesDestination);
		super.annuler();
	}
}

class Destruction extends Operation
{
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
	
	boolean executer()
	{
		if (tuile != null && !grille.uneSeuleTuile())
			tuile.setCoordonnees(null);
		else
			return false;
		return super.executer();
	}
	
	@Override
	void annuler()
	{
		tuile.setCoordonnees(coordonnees);
		super.annuler();
	}
}