package _2048;


import java.io.Serializable;
import java.util.Stack;

interface Evenement
{
	public void annuler();
	public boolean executer();
}

public class Historique<T extends Evenement> implements Serializable
{
	private static final long serialVersionUID = 5944113120854771369L;
	
	protected Stack<T> evenements = new Stack<>(),
			evenementsRetablir = new Stack<>();

	public T getEvenementEnCours()
	{
		return evenements.peek();
	}
	
	protected int size()
	{
		return evenements.size();
	}
	
	public void ajouter(T evenement)
	{
		evenements.add(evenement);
		evenementsRetablir.clear();
	}
			
	public boolean annulable()
	{
		return evenements.size() > 1;		
	}
	
	public boolean retablissable()
	{
		return !evenementsRetablir.isEmpty();		
	}
	
	public void annuler()
	{
		getEvenementEnCours().annuler();
		evenementsRetablir.add(getEvenementEnCours());
		evenements.pop();
	}
	
	public void retablir()
	{
		evenements.add(evenementsRetablir.peek());
		evenementsRetablir.pop();
		getEvenementEnCours().executer();
	}
}
