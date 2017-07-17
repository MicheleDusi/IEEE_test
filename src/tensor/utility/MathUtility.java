package tensor.utility;

/**
 * Classe di utilità,
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public final class MathUtility {
	
	private static final double DELTA = 1E-9;
	
	/**
	 * Enum di utilità per esplicitare l'algoritmo per il calcolo del determinante voluto.
	 */
	public static enum AlgoritmoPerDeterminante {
		GAUSS, SARRUS, AUTOMATICO;
	}

	private MathUtility() {};
	
	/**
	 * Metodo che verifica se un numero (double) è pari a zero, con una precisione arbitraria definita dalla
	 * costante di classe DELTA (pari a 1E-9).
	 * 
	 * @param num Numero da verificare.
	 * @return TRUE se il numero è nullo.
	 */
	public static boolean isZero(double num) {
		return(Math.abs(num) < DELTA);
	}
	
	
}
