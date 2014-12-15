package _2048;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

public class Jeu2048 implements Serializable, Iterable<Coordonnees>
{
	private static final long serialVersionUID = 8905215052383616386L;
	private Partie2048 partie2048;
	private int nbLignes, nbColonnes, puissanceGagnante;
	transient private Listener<Boolean> transactionListener = null;
	transient private Listener<Coordonnees> coordonneesListener = null;
	transient private Listener<Integer> scoreListener = null;
	transient private Listener<Boolean> annulableListener = null, 
			retablissableListener;	
	
	public Jeu2048(int nbLignes, int nbColonnes, int puissanceGagnante)
	{
		this.nbColonnes = nbColonnes;
		this.nbLignes = nbLignes;
		this.puissanceGagnante = puissanceGagnante;
		reinitialiser();
	}
	
	public Jeu2048(int nbLignes, int nbColonnes)
	{
		this(nbLignes, nbColonnes, 11);
	}

	private  void setPuissanceGagnante(int puissanceGagnante)
	{
		this.puissanceGagnante = puissanceGagnante;
	}

	public void reinitialiser()
	{
		partie2048 = new Partie2048(this, nbLignes, nbColonnes, puissanceGagnante);
	}

	public synchronized boolean gauche()
	{
		return partie2048.mouvement(partie2048.GAUCHE);
	}

	public synchronized  boolean droite()
	{
		return partie2048.mouvement(partie2048.DROITE);
	}

	public synchronized boolean haut()
	{
		return partie2048.mouvement(partie2048.HAUT);
	}

	public synchronized boolean bas()
	{
		return partie2048.mouvement(partie2048.BAS);
	}

	public boolean perd()
	{
		return partie2048.perd();
	}
	
	public boolean gagne()
	{
		return partie2048.gagne();
	}
	
	public int getNbLignes()
	{
		return partie2048.getNbLignes();
	}
	
	public int getNbColonnes()
	{
		return partie2048.getNbColonnes();
	}
	
	public void setNbLignes(int nbLignes)
	{
		this.nbLignes = nbLignes;
	}

	public void setNbColonnes(int nbColonnes)
	{
		this.nbColonnes = nbColonnes;
	}
	
	public Tuile get(Coordonnees coordonnees)
	{
		return partie2048.get(coordonnees);
	}
	
	public int getScore()
	{
		return partie2048.getScore();
	}
	
	public boolean detruireTuile(int ligne, int colonne)
	{
		return partie2048.detruireTuile(ligne, colonne);
	}
	
	public synchronized boolean annuler()
	{
		return partie2048.annuler();
	}
	
	public synchronized boolean retablir()
	{
		return partie2048.retablir();
	}
	
	public boolean sauvegarder(String fileName) 
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			return true;
		} 
		catch (IOException e)
		{
			System.out.println(e);
			return false;
		}
		finally
		{
			if (oos != null)
				try
				{
					oos.close();
				} 
			catch (IOException e){}
		}
	}
	
	public static Jeu2048 restaurer(String fileName) 
	{
		ObjectInputStream ois = null;
		Jeu2048 jeu2048 = null;
		try
		{
			FileInputStream fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
			jeu2048 = (Jeu2048)(ois.readObject());
			
		} 
		catch (IOException | ClassNotFoundException e){}
		finally
		{
			try
			{
				if(ois != null)
					ois.close();
			} 
			catch (IOException e){}
		}
		return jeu2048;
	}

	public void setCoordonneesListener(Listener<Coordonnees> listener)
	{
		this.coordonneesListener = listener;
		if (listener != null)
			for (Coordonnees coordonnees : partie2048)
				declencheListenerCoordonnees(coordonnees);
	}
	
	public void setTransactionListener(Listener<Boolean> listener)
	{
		this.transactionListener = listener;
	}

	public void setScoreListener(Listener<Integer> listener)
	{
		this.scoreListener = listener;
		if (listener != null)
			declencheListenerScore(getScore());
	}
	
	public void setAnnulableListener(Listener<Boolean> listener)
	{
		this.annulableListener = listener;
		declencheListenerHistorique();
	}
	
	public void setRetablissableListener(Listener<Boolean> listener)
	{
		this.retablissableListener = listener;
		declencheListenerHistorique();
	}
	
	void declencheListenerCoordonnees(Coordonnees coordonnees)
	{
		if (coordonneesListener != null && coordonnees != null)
			coordonneesListener.actionPerformed(coordonnees);	
	}

	void declencheListenerTransaction(boolean lock)
	{
		if (transactionListener != null)
			transactionListener.actionPerformed(lock);
	}

	void declencheListenerScore(Integer score)
	{
		if (scoreListener != null)
			scoreListener.actionPerformed(score);	
	}

	void declencheListenerHistorique()
	{
		if (annulableListener != null)
			annulableListener.actionPerformed(partie2048.annulable());
		if (retablissableListener != null)
			retablissableListener.actionPerformed(partie2048.retablissable());
	}

	public String toString()
	{
		return partie2048.toString();
	}

	@Override
	public Iterator<Coordonnees> iterator()
	{
		return partie2048.iterator();
	}
}
