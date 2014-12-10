package _2048;

import java.io.Serializable;

abstract class Operation implements Evenement, Serializable
{
	private static final long serialVersionUID = 4452112939719850396L;
	protected int score;
	protected Partie2048 partie2048;
	protected boolean operationGagnante = false, changeScore = false;
	
	Operation (Partie2048 partie2048, int score)
	{
		this.score = score;
		this.partie2048 = partie2048;
	}
	
	protected void pause()
	{
		try
		{
			Thread.sleep(Partie2048.TEMPS_ATTENTE);
		} 
		catch (InterruptedException e){}
		
	}
	
	public boolean executer()
	{
		if (changeScore)
		{
			partie2048.ajouteScore(score);
			if (operationGagnante)
				partie2048.setValeurGagnanteAtteinte(true);
		}
		pause();
		return true;
	}
	
	public void annuler()
	{
		if (changeScore)
		{
			partie2048.enleveScore(score);
			if (operationGagnante)
				partie2048.setValeurGagnanteAtteinte(false);
		}
		pause();
	}
	
	void setOperationGagnante()
	{
		operationGagnante = true;
	}
}
