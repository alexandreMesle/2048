package ligneCommande;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import _2048.Coordonnees;
import _2048.Jeu2048;
import _2048.Listener;

// TODO texte d'aide

public class LigneCommande2048
{
	private static final char HAUT = '8', BAS = '2', DROITE = '6', GAUCHE='4', MODE_TRICHE = 't', 
			AIDE = 'h', ANNULER = 'u', RETABLIR = 'r', QUITTER = 'q', REINITIALISER = 'x'; 
	private static final int NB_LIGNES = 4, NB_COLONNES = 4, PUISSANCE_GAGNANTE = 11;
	private static final String FILE_NAME = "sauvegarde.2048",
			AIDE_TEXT = "texte";
	private Jeu2048 jeu2048;
	
	public LigneCommande2048()
	{
		jeu2048 = Jeu2048.restaurer(FILE_NAME);
		if (jeu2048 == null)
			jeu2048 = new Jeu2048(NB_LIGNES, NB_COLONNES, PUISSANCE_GAGNANTE);
		jeu2048.setCoordonneesListener(getListener());
	}
	
	private Listener<Coordonnees> getListener()
	{
		return new Listener<Coordonnees>()
		{
			@Override
			public void actionPerformed(Coordonnees action)
			{
				//System.out.println(action);
			}
		};
	}
	
	private String saisieString(String message)
	{
		String buffer = "";
		System.out.println(message);
		do
		{
			try
			{
				buffer = (new BufferedReader(new InputStreamReader(System.in))).readLine();
			} 
			catch (IOException e)
			{
				System.out.println("Erreur de saisie, veuillez recommencer :");;
			}
		}
		while(buffer.length() != 1);
		return buffer;
	}

	private char saisieChar(String message)
	{
		return saisieString(message).charAt(0);
	}
	
	public void modeTriche()
	{
		String ligne = saisieString("ligne ? ");
		String colonne = saisieString("colonne ? ");
		if (!jeu2048.detruireTuile(new Integer(ligne), new Integer(colonne)))
			System.out.println("Imposible de supprimer la tuile.");
	}
	
	public void annuler()
	{
		if (!jeu2048.annuler())
			System.out.println("Aucune opération à annuler.");
	}
	
	public void reinitialiser()
	{
		jeu2048.reinitialiser();
	}
	
	public void retablir()
	{
		if (!jeu2048.retablir())
			System.out.println("Aucune opération à retablir.");
	}

	public void quitter()
	{
		if (!jeu2048.sauvegarder(FILE_NAME))
			System.out.println("Impossible de sauvegarder la partie.");
		System.out.println("Au revoir !");
		System.exit(0);
	}

	public void jouer()
	{
		System.out.println(jeu2048);
		while (!jeu2048.perd())
		{
			switch(saisieChar("Option ? (h pour l'aide)"))
			{
				case HAUT : jeu2048.haut();break;
				case BAS : jeu2048.bas();break;
				case GAUCHE : jeu2048.gauche();break;
				case DROITE : jeu2048.droite();break;
				case MODE_TRICHE : modeTriche() ; break;
				case ANNULER : annuler() ; break;
				case RETABLIR : retablir() ; break;
				case QUITTER : quitter() ; break;
				case REINITIALISER : reinitialiser(); break;
				case AIDE : System.out.println(AIDE_TEXT); break;
				default : System.out.println("Erreur de saisie");
			}
			if (jeu2048.gagne())
				System.out.println("Vous avez gagné !");
			System.out.println(jeu2048);			
		}
		System.out.println("La partie est terminée...");
	}
	
	public static void main(String[] args)
	{
		LigneCommande2048 deuxMilleQuaranteHuit = new LigneCommande2048();
		deuxMilleQuaranteHuit.jouer();
	}
}
