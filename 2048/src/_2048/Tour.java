package _2048;


import java.io.Serializable;

class Tour extends Historique<Operation> implements Evenement, Serializable
{
	private static final long serialVersionUID = -6603904945201105393L;

	boolean getMouvementEffectue()
	{
		return size() != 0;
	}
	
	boolean executer(Operation operation)
	{
		if (operation.executer())
		{
			ajouter(operation);
			return true;
		}
		return false;
	}	
	
	public void retablir()
	{
		while (retablissable())
			super.retablir();
	}	
	
	@Override
	public boolean annulable()
	{
		return super.size() != 0;
	}
	
	public void annuler()
	{
		while(annulable())
				super.annuler();
	}

	@Override
	public boolean executer()
	{
		retablir();
		return true;
	}
}
