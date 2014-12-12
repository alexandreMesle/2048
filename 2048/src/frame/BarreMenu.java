package frame;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import _2048.Jeu2048;
import _2048.Listener;

class BarreMenu
{
	private final JFrame2048 jFrame2048;
	
	BarreMenu(JFrame2048 jFrame2048)
	{
		this.jFrame2048 = jFrame2048;
	}
	
	MenuBar getMenuBar()
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
	
	private MenuItem getItemQuitter()
	{
		MenuItem item = new MenuItem("Fermer");
		item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				jFrame2048.getJFrame().dispose();
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
				jFrame2048.reinitialiser();
			}
		});
		return item;
	}

	private MenuItem getItemNbLignes()
	{
		MenuItem item = new MenuItem("Changer le nombre de lignes");
		item.addActionListener(getNbListener("Nombre de lignes", true));
		return item;
	}

	private MenuItem getItemNbColonnes()
	{
		MenuItem item = new MenuItem("Changer le nombre de colonnes");
		item.addActionListener(getNbListener("Nombre de colonnes", false));
		return item;
	}

	private MenuItem getItemAnnuler()
	{
		final MenuItem item = new MenuItem("Annuler");
		item.addActionListener(getAnnulerListener(true));
		item.setShortcut(new MenuShortcut(KeyEvent.VK_Z));
		jFrame2048.getJeu2048().setAnnulableListener(getAnnulableListener(item));
		return item;
	}

	private MenuItem getItemRetablir()
	{
		final MenuItem item = new MenuItem("Rétablir");
		item.addActionListener(getAnnulerListener(false));
		item.setShortcut(new MenuShortcut(KeyEvent.VK_R));
		jFrame2048.getJeu2048().setRetablissableListener(getAnnulableListener(item));
		return item;
	}

	private ActionListener getNbListener(final String message, final boolean lignes)
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
						jFrame2048.getJeu2048().setNbLignes(new Integer(nb));
					else
						jFrame2048.getJeu2048().setNbColonnes(new Integer(nb));
					jFrame2048.reinitialiser();
				}
			}
		};
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
						//enableFenetre(false);
						if (annuler)
							jFrame2048.getJeu2048().annuler();
						else
							jFrame2048.getJeu2048().retablir();
						//enableFenetre(true);
					}
				};
				(new Thread(r)).start();
			}
		};
	}
}
