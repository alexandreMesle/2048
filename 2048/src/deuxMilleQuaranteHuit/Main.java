package deuxMilleQuaranteHuit;

public class Main
{

	public static void main(String[] args)
	{
		DeuxMilleQuaranteHuit deuxMilleQuaranteHuit = new DeuxMilleQuaranteHuit(4,  4);
		System.out.println(deuxMilleQuaranteHuit);
		deuxMilleQuaranteHuit.bas();
		System.out.println("bas");
		System.out.println(deuxMilleQuaranteHuit);
		deuxMilleQuaranteHuit.droite();
		System.out.println("droite");
		System.out.println(deuxMilleQuaranteHuit);
		deuxMilleQuaranteHuit.haut();
		System.out.println("haut");
		System.out.println(deuxMilleQuaranteHuit);
		deuxMilleQuaranteHuit.gauche();
		System.out.println("gauche");
		System.out.println(deuxMilleQuaranteHuit);
	}
}
