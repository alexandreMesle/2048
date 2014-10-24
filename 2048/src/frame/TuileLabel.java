package frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import _2048.Coordonnees;

public class TuileLabel extends JLabel
{
	private static final long serialVersionUID = 7712249228589591390L;
	private Coordonnees coordonnees;
	private final static Map<Integer, Color> couleursFond = new HashMap<>(),
			couleursTexte = new HashMap<>();
	
	static 
	{
		couleursFond.put(0, Color.LIGHT_GRAY);couleursFond.put(0, Color.LIGHT_GRAY); 		
		couleursFond.put(2, Color.WHITE); couleursTexte.put(2, Color.BLACK);
		couleursFond.put(4, Color.GRAY); couleursTexte.put(4, Color.WHITE);
		couleursFond.put(8, Color.ORANGE);couleursTexte.put(8, Color.BLACK);
		couleursFond.put(16, Color.GREEN);couleursTexte.put(16, Color.BLACK);
		couleursFond.put(32, Color.MAGENTA);couleursTexte.put(32, Color.WHITE);
		couleursFond.put(64, Color.RED);couleursTexte.put(64, Color.WHITE);
		couleursFond.put(128, Color.YELLOW);couleursTexte.put(128, Color.BLACK);
		couleursFond.put(256, Color.PINK);couleursTexte.put(256, Color.BLACK);
		couleursFond.put(512, Color.DARK_GRAY);couleursTexte.put(512, Color.WHITE);
		couleursFond.put(1024, Color.CYAN);couleursTexte.put(1024, Color.BLACK);
		couleursFond.put(2048, Color.PINK);couleursTexte.put(2048, Color.BLACK);
	}
	
	TuileLabel(Coordonnees coordonnees, int valeur)
	{
		super();
		this.coordonnees = coordonnees;
		setPreferredSize(new Dimension(50, 50));
		setOpaque(true);
		setValeur(valeur);
		setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setFont(new Font("Serif", Font.PLAIN, 16));
	}
	
	void setValeur(int valeur)
	{
		setText((valeur != 0) ? "" + valeur : "");
		setBackground(couleursFond.get(valeur));
		setForeground(couleursTexte.get(valeur));
	}
	
	void reinitialise()
	{
		setValeur(0);		
	}
}
