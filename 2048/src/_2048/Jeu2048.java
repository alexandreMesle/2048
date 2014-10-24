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
	private Grille grille;
	private int nbLignes, nbColonnes, puissanceGagnante;
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

	protected void setGrille(Grille grille)
	{
		this.grille = grille;
	}

	protected void setPuissanceGagnante(int puissanceGagnante)
	{
		this.puissanceGagnante = puissanceGagnante;
	}

	protected void setScoreListener(Listener<Integer> scoreListener)
	{
		this.scoreListener = scoreListener;
	}

	public void reinitialiser()
	{
		grille = new Grille(nbLignes, nbColonnes, puissanceGagnante);
		grille.setCoordonneesListener(coordonneesListener);
		grille.setScoreListener(scoreListener);
		grille.setAnnulableListener(annulableListener);
		grille.setRetablissableListener(retablissableListener);		
	}
	
	public boolean gauche()
	{
		return grille.mouvement(grille.GAUCHE);
	}

	public boolean droite()
	{
		return grille.mouvement(grille.DROITE);
	}

	public boolean haut()
	{
		return grille.mouvement(grille.HAUT);
	}

	public boolean bas()
	{
		return grille.mouvement(grille.BAS);
	}

	public boolean perd()
	{
		return grille.perd();
	}
	
	public boolean gagne()
	{
		return grille.gagne();
	}
	
	public int getNbLignes()
	{
		return grille.getNbLignes();
	}
	
	public int getNbColonnes()
	{
		return grille.getNbColonnes();
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
		return grille.get(coordonnees);
	}
	
	public int getScore()
	{
		return grille.getScore();
	}
	
	public boolean detruireTuile(int ligne, int colonne)
	{
		return grille.detruireTuile(new Coordonnees(grille, ligne, colonne));
	}
	
	public boolean annuler()
	{
		return grille.annuler();
	}
	
	public boolean retablir()
	{
		return grille.retablir();
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
		coordonneesListener = listener;
		grille.setCoordonneesListener(listener);
	}
	
	public void setScoreCoordonneesListener(Listener<Integer> listener)
	{
		scoreListener = listener;
		grille.setScoreListener(listener);
	}
	
	public void setAnnulableListener(Listener<Boolean> listener)
	{
		annulableListener = listener;
		grille.setAnnulableListener(listener);
	}
	
	public void setRetablissableListener(Listener<Boolean> listener)
	{
		retablissableListener = listener;
		grille.setRetablissableListener(listener);
	}
	
	public String toString()
	{
		return grille.toString();
	}

	@Override
	public Iterator<Coordonnees> iterator()
	{
		return grille.iterator();
	}
}
