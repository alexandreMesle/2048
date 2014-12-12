package _2048;


import java.util.Random;

class Utils
{
	private static Random random = new Random();
	
	static int puissance(int b, int n)
	{
		if (n == 0)
			return 1;
		if (n%2 == 0)
			return puissance(b*b, n/2);
		return b * puissance(b, n - 1);
	}
	
	static int valeurAbsolue(int x)
	{
		return (x >= 0) ? x : -x;
	}
	
	static int nextInt(int maxValue)
	{
		return valeurAbsolue(random.nextInt())%maxValue;
	}
	
	static boolean nextBoolean()
	{
		return random.nextBoolean();
	}
	
	static String ligne(int number, char symbol)
	{
		return String.format("%0" + number + "d", 0).replace("0", "" + symbol);
	}
	
	static int valeurAleatoire()
	{
		return (Utils.nextBoolean()) ? 2 : 4;
	}
}
