package frame;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import _2048.Jeu2048;
import _2048.Listener;

// TODO enlever les alt+
// TODO meta-interface graphique
// TODO permettre la destruction 
// TODO message victoire
// TODO séparer la gestion des événements

public class JFrame2048
{
	private static final int DEFAULT_NB_LIGNES = 4, DEFAULT_NB_COLONNES = 4, 
			PUISSANCE_GAGNANTE = 11, HAUT = 1, BAS = 2, DROITE = 3, GAUCHE=4;
	private static final String FILE_NAME = "sauvegarde.2048";
	private JFrame jFrame;
	private Jeu2048 jeu2048;
	private Grille grille;
	private BarreMenu menu;
	
	public JFrame2048()
	{
		jeu2048 = Jeu2048.restaurer(FILE_NAME);
		if (jeu2048 == null)
			jeu2048 = new Jeu2048(DEFAULT_NB_LIGNES, DEFAULT_NB_COLONNES, 
					PUISSANCE_GAGNANTE);
		grille = new Grille(jeu2048);
		menu = new BarreMenu(this);
		jFrame = new JFrame("2048");
		jFrame.setContentPane(getMainPanel());
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.addWindowListener(getWindowListener());
		jFrame.setMenuBar(menu.getMenuBar());
		jFrame.pack();
	}
	
	Jeu2048 getJeu2048()
	{
		return jeu2048;
	}
	
	JFrame getJFrame()
	{
		return jFrame;
	}
	
	private void enableFenetre(boolean b)
	{
		jFrame.setEnabled(b);
	}
	
	boolean demandeConfirmation(String message)
	{
		return JOptionPane.showConfirmDialog(jFrame, message, "", JOptionPane.YES_NO_OPTION) 
				== JOptionPane.YES_OPTION;
	}
	
	void reinitialiser()
	{
		jeu2048.reinitialiser();
		grille.reset();//getGrillePanel();
		jeu2048.setCoordonneesListener(grille.getCoordonneesListener());
		jFrame.pack();
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
		panel.add(grille.getPanel()/*getGrillePanel()*/);
		panel.add(getDirectionPanel());
//		panel.addKeyListener(getKeyListener());
		jeu2048.setTransactionListener(getTransactionListener());
		return panel;
	}
	
	private JPanel getScorePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel("Score : " + jeu2048.getScore());
		jeu2048.setScoreListener(getScoreListener(label));
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

	private Runnable getRunnableAction(final int direction)
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				//enableFenetre(false);
				switch (direction)
				{
				case HAUT: jeu2048.haut() ; break;
				case BAS: jeu2048.bas() ; break;
				case GAUCHE: jeu2048.gauche() ; break;
				case DROITE: jeu2048.droite() ; break;
				default: System.out.println("erreur !");
				}
				//enableFenetre(true);
			}
		};

	}
	
	private ActionListener getBoutonListener(final int direction)
	{
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Runnable r = getRunnableAction(direction);
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

	private Listener<Boolean> getTransactionListener()
	{
		return new Listener<Boolean>()
		{
			@Override
			public void actionPerformed(Boolean lock)
			{
				enableFenetre(lock);
			}
		};
	}
	
	private Listener<Integer> getScoreListener(final JLabel label)
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
	
//	private KeyListener getKeyListener()
//	{
//		return new KeyAdapter()
//		{
//			@Override
//			public void keyPressed(KeyEvent e)
//			{
//				System.out.println("key pressed" + e.getKeyCode());
//			}
//		};
//	}
//	
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
