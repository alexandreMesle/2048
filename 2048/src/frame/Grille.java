package frame;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import _2048.Coordonnees;
import _2048.Jeu2048;
import _2048.Listener;
import _2048.Tuile;

class Grille
{
	private Jeu2048 jeu2048;
	private Map<Coordonnees, TuileLabel> labels = new HashMap<>();
	private JPanel grillePanel = new JPanel();

	Grille(Jeu2048 jeu2048)
	{
		this.jeu2048 = jeu2048;
	}
	
	void reset()
	{
		grillePanel.removeAll();
		labels.clear();
		initJPanel();
	}
	
	private void initJPanel()
	{
		grillePanel.setLayout(new GridLayout(jeu2048.getNbLignes(), 
											jeu2048.getNbColonnes()));
		for (Coordonnees coordonnees : jeu2048)
		{
			Tuile tuile = jeu2048.get(coordonnees);
			int valeur = (tuile != null) ? tuile.getValeur() : 0;
			TuileLabel label = new TuileLabel(coordonnees, valeur);
			grillePanel.add(label);
			labels.put(coordonnees, label);
		}
		jeu2048.setCoordonneesListener(getCoordonneesListener());
//		grillePanel.addKeyListener(getKeyListener());
	}
	
	JPanel getPanel()
	{
		initJPanel();
		return grillePanel;
	}
	
	Listener<Coordonnees> getCoordonneesListener()
	{
		return new Listener<Coordonnees>()
		{
			@Override
			public void actionPerformed(Coordonnees coordonnees)
			{
				TuileLabel label = labels.get(coordonnees); 
				Tuile tuile = jeu2048.get(coordonnees); 
				label.setValeur((tuile != null) ? tuile.getValeur() : 0);
			}
		};		
	}
	

}
