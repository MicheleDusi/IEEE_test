package tensor.utility;

public final class MathUtility {
	
	private static final double DELTA = 1E-9;

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
