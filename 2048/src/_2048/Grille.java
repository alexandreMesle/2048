package _2048;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.Set;

class Grille implements Serializable, Iterable<Coordonnees>
{
	private static final long serialVersionUID = 6444629649066952174L;
	private Partie2048 partie;
	private int nbLignes, nbColonnes;
	private Map<Coordonnees, Tuile> tuiles = new HashMap<>();
	private List<Coordonnees> coordonnees = new LinkedList<>();

	Grille(Partie2048 partie, int nbLignes, int nbColonnes)
	{
		this.partie = partie;
		this.nbLignes = nbLignes;
		this.nbColonnes = nbColonnes;
		for (int i = 0 ;  i < nbLignes; i++)
			for (int j = 0 ; j < nbColonnes ; j++)
				coordonnees.add(new Coordonnees(this, i, j));
	}

	List<Coordonnees> getCasesVides()
	{
		List<Coordonnees> liste = new ArrayList<Coordonnees>();
		for (Coordonnees coordonnees : this.coordonnees)
			if (estVide(coordonnees))
				liste.add(coordonnees);
		return liste;
	}

	Collection<Tuile> getTuiles()
	{
		return tuiles.values(); 
	}
	
	Set<Coordonnees> getCasesPleines()
	{
		return tuiles.keySet();
	}
	
	Coordonnees getCaseVideAleatoire()
	{
		List<Coordonnees> casesVides = getCasesVides();
		return casesVides.get(Utils.nextInt(casesVides.size()));
	}

	Tuile get(Coordonnees coordonnees)
	{
		return tuiles.get(coordonnees);
	}
	
	void set(Coordonnees coordonnees, Tuile tuile)
	{
		if (coordonnees != null)
		{
			tuiles.put(coordonnees, tuile);
			partie.declencheListenerCoordonnees(coordonnees);
		}
		else
			supprime(tuile);		
	}

	void supprime(Tuile tuile)
	{
		Coordonnees coordonnees = tuile.getCoordonnees();
		tuiles.remove(coordonnees);
		partie.declencheListenerCoordonnees(coordonnees);
	}
	
	boolean estVide(Coordonnees coordonnees)
	{
		return !tuiles.containsKey(coordonnees);
	}
	
	Tuile get(int ligne, int colonne)
	{
		return get(new Coordonnees(this, ligne, colonne));
	}
	
	int getNbLignes()
	{
		return nbLignes;
	}
	
	int getNbColonnes()
	{
		return nbColonnes;
	}

	boolean uneSeuleTuile()
	{
		return tuiles.size() == 1;
	}

	@Override
	public Iterator<Coordonnees> iterator()
	{
		return coordonnees.iterator();
	}
}
