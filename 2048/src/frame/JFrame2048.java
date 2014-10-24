package frame;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
	
	private void reinitialiser()
	{
		if (JOptionPane.showConfirmDialog(frame, "Cette action vous fera abandonner la partie en cours. Etes-vous sûr "
				+ "de vouloir continuer ?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
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
		if (JOptionPane.showConfirmDialog(frame, "Souhaitez vous sauvegarder cette partie ?", "", JOptionPane.YES_NO_OPTION) 
				== JOptionPane.YES_OPTION)
		{
			jeu2048.sauvegarder(FILE_NAME);
		}
	}
	
	private JPanel getMainPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(getScorePanel());
		panel.add(getGrillePanel());
		panel.add(getDirectionPanel());
		return panel;
	}
	
	private JPanel getGrillePanel()
	{
		if (grillePanel == null)
			grillePanel = new JPanel();
		grillePanel.removeAll();
		grillePanel.setLayout(new GridLayout(getNbLignes(), getNbColonnes()));
		labels.clear();
		for (Coordonnees coordonnees : jeu2048)
		{
			Tuile tuile = jeu2048.get(coordonnees);
			int valeur = (tuile != null) ? tuile.getValeur() : 0;
			TuileLabel label = new TuileLabel(coordonnees, valeur);
			grillePanel.add(label);
			labels.put(coordonnees, label);
		}
		return grillePanel;
	}
	
	private JPanel getScorePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel("Score : " + jeu2048.getScore());
		jeu2048.setScoreCoordonneesListener(getScoreListener(label));
		panel.add(label);		
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

	private JButton getBouton(final int direction)
	{
		JButton bouton = new JButton();
		switch (direction)
		{
		case HAUT: bouton.setText("haut"); 
				bouton.setMnemonic(KeyEvent.VK_UP);
				bouton.setToolTipText("Alt + flèche haut");;
				break;
		case BAS: bouton.setText("bas") ;
				bouton.setMnemonic(KeyEvent.VK_DOWN);
				bouton.setToolTipText("Alt + flèche bas");;
				break;
		case GAUCHE: bouton.setText("gauche");
				bouton.setMnemonic(KeyEvent.VK_LEFT);
				bouton.setToolTipText("Alt + flèche gauche");;
				break;
		case DROITE: bouton.setText("droite"); 
				bouton.setMnemonic(KeyEvent.VK_RIGHT);
				bouton.setToolTipText("Alt + flèche droite");;
				break;
			default: System.out.println("erreur !");
		}
		bouton.addActionListener(new ActionListener()
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
		});
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

	private MenuItem getItemNbLignes()
	{
		MenuItem item = new MenuItem("Changer le nombre de lignes");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String nbLignes = (String)JOptionPane.showInputDialog("Nombre de lignes");
				if (nbLignes != null)
				{
					jeu2048.setNbLignes(new Integer(nbLignes));
					reinitialiser();
				}
			}
		});
		return item;
	}

	private MenuItem getItemNbColonnes()
	{
		MenuItem item = new MenuItem("Changer le nombre de colonnes");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String nbColonnes= (String)JOptionPane.showInputDialog("Nombre de colonnes");
				if (nbColonnes != null)
				{
					jeu2048.setNbColonnes(new Integer(nbColonnes));
					reinitialiser();
					
				}
			}
		});
		return item;
	}

	private MenuItem getItemAnnuler()
	{
		final MenuItem item = new MenuItem("Annuler");
		item.addActionListener(new ActionListener()
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
						jeu2048.annuler();
						enableFenetre(true);
					}
				};
				(new Thread(r)).start();
			}
		});
		item.setShortcut(new MenuShortcut(KeyEvent.VK_Z));
		jeu2048.setAnnulableListener(new Listener<Boolean>()
		{
			@Override
			public void actionPerformed(Boolean action)
			{
				item.setEnabled(action);
			}
		});
		return item;
	}

	private MenuItem getItemRetablir()
	{
		final MenuItem item = new MenuItem("Rétablir");
		item.addActionListener(new ActionListener()
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
						jeu2048.retablir();
						enableFenetre(true);
					}
				};
				(new Thread(r)).start();
			}
		});
		jeu2048.setRetablissableListener(new Listener<Boolean>()
		{
			@Override
			public void actionPerformed(Boolean action)
			{
				item.setEnabled(action);
			}
		});
		item.setShortcut(new MenuShortcut(KeyEvent.VK_R));
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
