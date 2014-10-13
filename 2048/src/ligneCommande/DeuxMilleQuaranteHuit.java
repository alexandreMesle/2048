package ligneCommande;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DeuxMilleQuaranteHuit
{
	private static final char HAUT = '8', BAS = '2', DROITE = '6', GAUCHE='4', MODE_TRICHE = 't', 
			AIDE = 'h', ANNULER = 'u', RETABLIR = 'r'; 
	private static final int NB_LIGNES = 4, NB_COLONNES = 4, PUISSANCE_GAGNANTE = 5;
	deuxMilleQuaranteHuit.DeuxMilleQuaranteHuit deuxMilleQuaranteHuit = 
			new deuxMilleQuaranteHuit.DeuxMilleQuaranteHuit(NB_LIGNES, NB_COLONNES, PUISSANCE_GAGNANTE);  
	
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
		if (!deuxMilleQuaranteHuit.detruireTuile(new Integer(ligne), new Integer(colonne)))
			System.out.println("Imposible de supprimer la tuile.");
	}
	
	public void annuler()
	{
		if (!deuxMilleQuaranteHuit.annuler())
			System.out.println("Aucune opération à annuler.");
	}
	
	public void retablir()
	{
		if (!deuxMilleQuaranteHuit.retablir())
			System.out.println("Aucune opération à retablir.");
	}
	
	public void jouer()
	{
		boolean gagner = false;
		System.out.println(deuxMilleQuaranteHuit);
		while (!deuxMilleQuaranteHuit.perd())
		{
			switch(saisieChar("Option ? (h pour l'aide)"))
			{
				case HAUT : deuxMilleQuaranteHuit.haut();break;
				case BAS : deuxMilleQuaranteHuit.bas();break;
				case GAUCHE : deuxMilleQuaranteHuit.gauche();break;
				case DROITE : deuxMilleQuaranteHuit.droite();break;
				case MODE_TRICHE : modeTriche() ; break;
				case ANNULER : annuler() ; break;
				case RETABLIR : retablir() ; break;
				default : System.out.println("Erreur de saisie");
			}
			if (!gagner && deuxMilleQuaranteHuit.gagne())
			{
				System.out.println("Vous avez gagné !");
				gagner = true;
			}
			System.out.println(deuxMilleQuaranteHuit);			
		}
		System.out.println("La partie est terminée...");
	}
	
	public static void main(String[] args)
	{
		DeuxMilleQuaranteHuit deuxMilleQuaranteHuit = new DeuxMilleQuaranteHuit();
		deuxMilleQuaranteHuit.jouer();
	}
}
