package frame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import _2048.Coordonnees;
import _2048.Jeu2048;
import _2048.Listener;
import _2048.Tuile;

// TODO enlever les alt+
// TODO meta-interface graphique

public class JFrame2048
{
	private static final int DEFAULT_NB_LIGNES = 4, DEFAULT_NB_COLONNES = 4, 
			PUISSANCE_GAGNANTE = 11, HAUT = 1, BAS = 2, DROITE = 3, GAUCHE=4;
	private static final String FILE_NAME = "sauvegarde.2048";
	private JFrame frame;
	private Jeu2048 jeu2048;
	private Map<Coordonnees, TuileLabel> labels = new HashMap<>();
	private JPanel grillePanel = null;
	
	public JFrame2048()
	{
		jeu2048 = Jeu2048.restaurer(FILE_NAME);
		if (jeu2048 == null)
			jeu2048 = new Jeu2048(DEFAULT_NB_LIGNES, DEFAULT_NB_COLONNES, 
					PUISSANCE_GAGNANTE);
		frame = new JFrame("2048");
		frame.setContentPane(getMainPanel());
		jeu2048.setCoordonneesListener(getCoordonneesListener());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.addWindowListener(getWindowListener());
		frame.setMenuBar(getMenuBar());
		frame.pack();
	}
	
	private MenuBar getMenuBar()
	{
		MenuBar menuBar= new MenuBar();
		menuBar.add(getMenuJeu());
		menuBar.add(getMenuParametres());
		return menuBar;
	}

	private Menu getMenuParametres()
	{
		Menu menu = new Menu("Paramètres");
		menu.add(getItemNbLignes());
		menu.add(getItemNbColonnes());
		return menu;
	}

	private Menu getMenuJeu()
	{
		Menu menu = new Menu("Jeu");
		menu.add(getItemQuitter());
		menu.add(getItemReinitialiser());
		menu.add(getItemAnnuler());
		menu.add(getItemRetablir());
		return menu;
	}

	private void enableFenetre(boolean b)
	{
		frame.setEnabled(b);
	}
	
	private boolean demandeConfirmation(String message)
	{
		return JOptionPane.showConfirmDialog(frame, message, "", JOptionPane.YES_NO_OPTION) 
				== JOptionPane.YES_OPTION;
	}
	
	private void reinitialiser()
	{
		if (demandeConfirmation("Cette action vous fera abandonner la partie en cours. Etes-vous sûr de vouloir continuer ?"))
		{
			jeu2048.setCoordonneesListener(null);
			jeu2048.reinitialiser();
			getGrillePanel();
			jeu2048.setCoordonneesListener(getCoordonneesListener());
			frame.pack();
		}
	}
	
	private void sauvegarder()
	{
		if (demandeConfirmation("Souhaitez vous sauvegarder cette partie ?")) 
			jeu2048.sauvegarder(FILE_NAME);
	}
	
	private JPanel getMainPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(getScorePanel());
		panel.add(getGrillePanel());
		panel.add(getDirectionPanel());
//		panel.addKeyListener(getKeyListener());
		return panel;
	}
	
	private JPanel getGrillePanel()
	{
		if (grillePanel == null)
			grillePanel = new JPanel();
		grillePanel.removeAll();
		labels.clear();
		grillePanel.setLayout(new GridLayout(getNbLignes(), getNbColonnes()));
		for (Coordonnees coordonnees : jeu2048)
		{
			Tuile tuile = jeu2048.get(coordonnees);
			int valeur = (tuile != null) ? tuile.getValeur() : 0;
			TuileLabel label = new TuileLabel(coordonnees, valeur);
			grillePanel.add(label);
			labels.put(coordonnees, label);
		}
//		grillePanel.addKeyListener(getKeyListener());
		return grillePanel;
	}
	
	private JPanel getScorePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel("Score : " + jeu2048.getScore());
		jeu2048.setScoreCoordonneesListener(getScoreListener(label));
		panel.add(label);		
//		panel.addKeyListener(getKeyListener());
		return panel;
	}

	private JPanel getDirectionPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(getBouton(HAUT));
		panel.add(getBouton(BAS));
		panel.add(getBouton(GAUCHE));
		panel.add(getBouton(DROITE));
		return panel;
	}

	private ActionListener getBoutonListener(final int direction)
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Runnable r = new Runnable()
				{
					@Override
					public void run()
					{
						enableFenetre(false);
						switch (direction)
						{
						case HAUT: jeu2048.haut() ; break;
						case BAS: jeu2048.bas() ; break;
						case GAUCHE: jeu2048.gauche() ; break;
						case DROITE: jeu2048.droite() ; break;
						default: System.out.println("erreur !");
						}
						enableFenetre(true);
					}
				};
				(new Thread(r)).start();
			}
		};
	}

	private JButton getBouton(String label, int key, String tipToolText)
	{
		JButton bouton = new JButton();
		bouton.setText(label); 
		bouton.setMnemonic(key);
		bouton.setToolTipText(tipToolText);
		return bouton;
	}
	
	private JButton getBouton(int direction)
	{
		JButton bouton = null;
		switch (direction)
		{
		case HAUT: bouton = getBouton("haut", KeyEvent.VK_UP, "Alt + flèche haut");	break;
		case BAS: bouton = getBouton("bas", KeyEvent.VK_DOWN, "Alt + flèche bas"); break;
		case GAUCHE: bouton = getBouton("gauche", KeyEvent.VK_LEFT, "Alt + flèche gauche");	break;
		case DROITE: bouton = getBouton("droite", KeyEvent.VK_RIGHT, "Alt + flèche droite"); break;
			default: throw new RuntimeException("direction inconnue " + direction);
		}
		bouton.addActionListener(getBoutonListener(direction));
		return bouton;
	}

	private MenuItem getItemQuitter()
	{
		MenuItem item = new MenuItem("Fermer");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				frame.dispose();
			}
		});
		return item;
	}

	private MenuItem getItemReinitialiser()
	{
		MenuItem item = new MenuItem("Réinitialiser");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				reinitialiser();
			}
		});
		return item;
	}

	private ActionListener getNbLignesListener(final String message, final boolean lignes)
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String nb = (String)JOptionPane.showInputDialog(message);
				if (nb != null)
				{
					if (lignes)
						jeu2048.setNbLignes(new Integer(nb));
					else
						jeu2048.setNbColonnes(new Integer(nb));
					reinitialiser();
				}
			}
		};
	}
	
	private MenuItem getItemNbLignes()
	{
		MenuItem item = new MenuItem("Changer le nombre de lignes");
		item.addActionListener(getNbLignesListener("Nombre de lignes", true));
		return item;
	}

	private MenuItem getItemNbColonnes()
	{
		MenuItem item = new MenuItem("Changer le nombre de colonnes");
		item.addActionListener(getNbLignesListener("Nombre de colonnes", false));
		return item;
	}

	private Listener<Boolean> getAnnulableListener(final MenuItem item)
	{
		return new Listener<Boolean>()
		{
			@Override
			public void actionPerformed(Boolean action)
			{
				item.setEnabled(action);
			}
		};
	}

	private ActionListener getAnnulerListener(final boolean annuler)
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Runnable r = new Runnable()
				{
					@Override
					public void run()
					{
						enableFenetre(false);
						if (annuler)
							jeu2048.annuler();
						else
							jeu2048.retablir();
						enableFenetre(true);
					}
				};
				(new Thread(r)).start();
			}
		};
	}
	
	private MenuItem getItemAnnuler()
	{
		final MenuItem item = new MenuItem("Annuler");
		item.addActionListener(getAnnulerListener(true));
		item.setShortcut(new MenuShortcut(KeyEvent.VK_Z));
		jeu2048.setAnnulableListener(getAnnulableListener(item));
		return item;
	}

	private MenuItem getItemRetablir()
	{
		final MenuItem item = new MenuItem("Rétablir");
		item.addActionListener(getAnnulerListener(false));
		item.setShortcut(new MenuShortcut(KeyEvent.VK_R));
		jeu2048.setRetablissableListener(getAnnulableListener(item));
		return item;
	}

	private Listener<Coordonnees> getCoordonneesListener()
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
	
	private Listener<Integer> getScoreListener(final JLabel label )
	{
		return new Listener<Integer>()
		{
			@Override
			public void actionPerformed(Integer score)
			{
				label.setText("Score = " + score);
			}
		};		
	}

	private WindowListener getWindowListener()
	{
		return new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				sauvegarder();
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0)
			{
				sauvegarder();
				System.exit(0);
			}			
		};
	}
	
	private KeyListener getKeyListener()
	{
		return new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				System.out.println("key pressed" + e.getKeyCode());
			}
		};
	}
	
	public int getNbLignes()
	{
		return jeu2048.getNbLignes();
	}
	
	public int getNbColonnes()
	{
		return jeu2048.getNbColonnes();
	}	
	
	public static void main(String[] args)
	{
		new JFrame2048();
	}
}
