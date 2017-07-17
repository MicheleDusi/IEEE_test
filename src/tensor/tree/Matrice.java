package tensor.tree;

import java.util.Comparator;
import java.util.List;

import tensor.utility.MathUtility;

/**
 * Classe che rappresenta una matrice bidimensionale quadrata.
 * E' costruita in modo da occupare poco spazio in memoria (contiene al suo interno un solo
 * array bidimensionale) e calcola di volta in volta i valori richiesti dall'utente, adottando
 * l'algoritmo più efficiente sulla base delle informazioni a sua disposizione.
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
class Matrice {
	
	private static final int FIRST_ELEMENT = 0;
	
	private static final String STRING_FORMAT_ELEMENTO = "%7.2f";

	private static final String EXCEPTION_MATRICE_NON_QUADRATA = "Errore: la matrice fornita non è quadrata.";
	private static final String EXCEPTION_MATRICE_VUOTA = "Errore: la matrice fornita contiene una o più righe o colonne non inizializzate.";
	
	private double [][] matrice;
	
	/**
	 * Costruttore che istanzia un oggetto Matrice dato in ingresso una List bidimensionale
	 * contenente oggetti wrapper Double. Questo particolare costruttore è comodo per costruire
	 * la matrice quando non si è a conoscenza delle dimensioni a priori.
	 * 
	 * Il costruttore opera in automatico la conversione da List ad Array. Ciò rallenta la costruzione 
	 * dell'oggetto, ma permette di memorizzare tutte le informazioni in meno spazio, e inoltre rende 
	 * più efficienti le operazioni matematiche su di esso.
	 * 
	 * @param matrice_listata La Matrice come List bidimensionale di Double.
	 * @throws IllegalArgumentException Se la matrice fornita come parametro è degenere o non quadrata.
	 */
	Matrice(List<List<Double>> matrice_listata) throws IllegalArgumentException {
		if (matrice_listata.isEmpty() || matrice_listata.get(FIRST_ELEMENT).isEmpty()) {
			// La matrice è degenere (non contiene righe o colonne ben definite).
			throw new IllegalArgumentException(EXCEPTION_MATRICE_VUOTA);
			
		} else if (matrice_listata.size() != matrice_listata.get(FIRST_ELEMENT).size()) {
			// La matrice fornita non è quadrata
			throw new IllegalArgumentException(EXCEPTION_MATRICE_NON_QUADRATA);
			
		} else {
			/*
			 * Opero la conversione da List ad Array.
			 */
			this.matrice = new double [matrice_listata.size()][matrice_listata.size()];
			for (int i = 0; i < matrice.length; i++) {
				for (int j = 0; j < matrice.length; j++) {
					this.matrice[i][j] = matrice_listata.get(i).get(j);
				}
			}
		}
	}
	
	/**
	 * Costruttore che istanzia un oggetto Mriferimentoatrice dato in ingresso un array bidimensionale.
	 * 
	 * @param mat La Matrice da istanziare sotto forma di array bidimensionale.
	 * @throws IllegalArgumentException Se la matrice fornita come parametro è degenere o non quadrata.
	 */
	Matrice(double [][] mat) throws IllegalArgumentException {
		if (mat.length == 0 || mat[0].length == 0) {
			// La matrice è degenere (non contiene righe o colonne ben definite).
			throw new IllegalArgumentException(EXCEPTION_MATRICE_VUOTA);
			
		} else if (mat.length != mat[0].length) {
			// La matrice fornita non è quadrata
			throw new IllegalArgumentException(EXCEPTION_MATRICE_NON_QUADRATA);
			
		} else {
			// Semplice assegnazione
			this.matrice = mat;
		}
	}
	
	/**
	 * Metodo che calcola il determinante della matrice attraverso il metodo di Gauss.
	 * 
	 * @return Determinante della matrice.
	 */
	double calcolaDeterminanteGauss() {
		double determinante = 1;
		// TODO implementazione con partenza dalla seconda riga -> un ciclo risparmiato
		for (int k = 0; k < matrice.length; k++) {
			// Riga di riferimento
			
			if (MathUtility.isZero(matrice[k][k]))
				return 0; // Velocizza il calcolo in caso in cui un pivot sia nullo -> determinante nullo.
			
			determinante *= matrice[k][k]; // Moltiplico il determinante per l'elemento sulla diagonale
			for (int i = k + 1; i < matrice.length; i++) {
				// Riga da modificare
				
				if (!MathUtility.isZero(matrice[i][k])) { // Evito il denominatore nullo.
					double coeff = - matrice[k][k] / matrice[i][k]; // Coefficiente per cui moltiplicare la riga
					for (int j = i; j < matrice.length; j++) {
						// Scorro su tutta la riga
						matrice[i][j] = matrice[i][j] * coeff + matrice[k][j];
					}
				}
			}
		}
		return determinante;
	}
	
	/**
	 * Restituisce la dimensione di riga o colonna della Matrice.
	 * I due valori sono coincidenti poiché la matrice è quadrata.
	 * 
	 * @return Dimensione di una riga/colonna.
	 */
	public int getDimensione() {
		return this.matrice.length;
	}
	
	/**
	 * Restituisce una descrizione completa degli elementi della matrice.
	 * Le cifre, durante la visualizzazione, subiscono un arrotondamento alla seconda cifra decimale.
	 * 
	 * Nota: la classe ha come modificatore di accesso quello di default (<i>"friendly"</i>), ma questo metodo è
	 * <i>public</i>. Questo succede perchè non è possibile ridurre la visibilità di un metodo ereditato dalla superclasse,
	 * in questo caso da Object.
	 * 
	 * @return Matrice sotto forma di Stringa.
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice.length; j++) {
				s.append(String.format(STRING_FORMAT_ELEMENTO, matrice[i][j]));
			}
			s.append(System.lineSeparator());
		}
		return s.toString();
	}

	/**
	 * Classe comparatore per le Matrici.
	 * Il confronto viene effettuato sul determinante.
	 */
	private static class MatriceComparator implements Comparator<Matrice> {
	
		@Override
		public int compare(Matrice mat1, Matrice mat2) {
			double diff = mat1.calcolaDeterminanteGauss() - mat2.calcolaDeterminanteGauss();
			if (MathUtility.isZero(diff)) {
				return 0;
			} else if (diff > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Metodo che instanzia un oggetto "MatriceComparator" e lo restituisce.
	 * In questo modo, essendo la classe interna privata, mi assicuro che nessuno possa istanziare
	 * un oggetto in maniera differente da quanto previsto.
	 * 
	 * @return Nuova istanza dell'oggetto MatriceComparator.
	 */
	public static MatriceComparator getComparator() {
		return new MatriceComparator();
	}
	
	// PROVA MAIN
	public static void main (String [] args) {
		double [][] dd = {{2,5,4},{0,0,4},{0,0,-22}};
		Matrice matr = new Matrice(dd);
		System.out.println(matr.toString());
		System.out.println(String.format(STRING_FORMAT_ELEMENTO, matr.calcolaDeterminanteGauss()));
	}
	
}
