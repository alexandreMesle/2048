package _2048;

import java.io.Serializable;

abstract class Operation implements Evenement, Serializable
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
		} 
		catch (InterruptedException e){}
		
	}
	
	public boolean executer()
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
	
	public void annuler()
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
