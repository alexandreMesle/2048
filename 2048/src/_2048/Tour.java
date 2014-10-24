package _2048;

import java.io.Serializable;
import java.util.Stack;

class Tour implements Serializable
{
	private static final long serialVersionUID = -6603904945201105393L;
	private int nbThreads = 0;
	private Stack<Operation> operations = new Stack<>(), operationsRetablir = new Stack<>();
	
	boolean getMouvementEffectue()
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

abstract class Operation implements Serializable
{
	private static final long serialVersionUID = 4452112939719850396L;
	protected int score;
	protected Grille grille;
	protected boolean operationGagnante = false, changeScore = false;
	
	Operation (Grille grille, int score)
	{
		this.score = score;
		this.grille = grille;
	}
	
	protected void pause()
	{
		try
		{
			Thread.sleep(Grille.TEMPS_ATTENTE);
		} catch (InterruptedException e){}
		
	}
	
	boolean executer()
	{
		if (changeScore)
		{
			grille.ajouteScore(score);
			if (operationGagnante)
				grille.setValeurGagnanteAtteinte(true);
		}
		pause();
		return true;
	}
	
	void annuler()
	{
		if (changeScore)
		{
			grille.enleveScore(score);
			if (operationGagnante)
				grille.setValeurGagnanteAtteinte(false);
		}
		pause();
}
	
	void setOperationGagnante()
	{
		operationGagnante = true;
	}
}

class Deplacement extends Operation
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
	private static final long serialVersionUID = -5040720192393632469L;
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
	
	boolean executer()
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
