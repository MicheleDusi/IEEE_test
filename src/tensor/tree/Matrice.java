package tensor.tree;

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
public class Matrice {
	
	private static final int FIRST_ELEMENT = 0;
	public static final int SARRUS_DIMENSION = 3;
	private static final int MIN_RAND_VALUE = 0;
	private static final int MAX_RAND_VALUE = 100;
	
	private static final String STRING_FORMAT_ELEMENTO = "%15.3f ";

	private static final String EXCEPTION_MATRICE_NON_QUADRATA = "Errore: la matrice fornita non è quadrata.";
	private static final String EXCEPTION_MATRICE_VUOTA = "Errore: la matrice fornita contiene una o più righe o colonne non inizializzate.";
	private static final String EXCEPTION_DIMENSIONE_SARRUS_ERRATA = "Non è possibile utilizzare l'algoritmo di Sarrus su matrici di dimensione diversa da 3";

	
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
	public Matrice(List<List<Double>> matrice_listata) throws IllegalArgumentException {
		if (matrice_listata.size() == 0 || matrice_listata.get(FIRST_ELEMENT).size() == 0) {
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
				for (int j = 0; j < matrice[0].length; j++) {
//					System.out.println(matrice_listata.get(i).get(j));
					this.matrice[i][j] = matrice_listata.get(i).get(j);
				}
			}
		}
	}
	
	/**
	 * Costruttore che prende in ingresso un array bidimensionale di double e lo salva all'interno dell'oggetto
	 * Matrice, creando un'istanza dello stesso.
	 * 
	 * @param matrice Matrice con cui creare l'oggetto.
	 * @throws IllegalArgumentException
	 */
	public Matrice(double[][] matrice) throws IllegalArgumentException {
		if (matrice.length == 0 || matrice[FIRST_ELEMENT].length == 0) {
			// La matrice è degenere (non contiene righe o colonne ben definite).
			throw new IllegalArgumentException(EXCEPTION_MATRICE_VUOTA);
			
		} else if (matrice.length != matrice[FIRST_ELEMENT].length) {
			// La matrice fornita non è quadrata
			throw new IllegalArgumentException(EXCEPTION_MATRICE_NON_QUADRATA);
			
		} else {
			this.matrice = matrice;
		}
	}
	
	/**
	 * Restituisce una matrice generata casualmente.
	 * Questo metodo è necessario per fare i confronti sul calcolo del determinante.
	 */
	public static Matrice generaCasuale(int dim) {
		double [][] matrice = new double [dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				matrice[i][j] = Math.random() * (MAX_RAND_VALUE - MIN_RAND_VALUE) + MIN_RAND_VALUE;
			}
		}
		return new Matrice(matrice);
	}

	/**
	 * Metodo che calcola il determinante della matrice con l'algortimo richiesto.
	 * 
	 * @return Determinante della matrice.
	 */
	public double calcolaDeterminante(MathUtility.AlgoritmoPerDeterminante algo) {
		switch(algo) {
		case GAUSS:
			return this.calcolaDeterminanteGauss();
		case SARRUS:
			if (this.matrice.length == SARRUS_DIMENSION) {
				return this.calcolaDeterminanteSarrus();
			} else {
				throw new IllegalArgumentException(EXCEPTION_DIMENSIONE_SARRUS_ERRATA);
			}
		case AUTOMATICO:
		default:
			return this.calcolaDeterminante();
		}
	}

	/**
	 * Metodo che calcola il determinante della matrice scegliendo opportunamente di volta in volta
	 * il metodo più conveniente in funzione della dimensione della matrice.
	 * 
	 * @return Determinante della matrice.
	 */
	public double calcolaDeterminante() {
		/* Nota: Qui e nel metodo del calcolo del determinante con Sarrus ho preferito
		 * lasciare indicati i numeri degli indici delle celle della matrice. Sostituirli con costanti
		 * avrebbe appesantito il codice, e dal punto di vista matematico i numeri espliciti aiutano con
		 * la comprensione della formula.
		 */
		switch (matrice.length) {
		case 1:
			return matrice[0][0];
		case 2:
			return matrice[0][0] * matrice[1][1] - matrice[1][0] * matrice[0][1];
		case SARRUS_DIMENSION:
			return this.calcolaDeterminanteSarrus();
		default:
			return this.calcolaDeterminanteGauss();
		}
	}
	
	/**
	 * Metodo che implementa il calcolo del determinante attraverso l'algoritmo di eliminazione di Gauss.
	 * 
	 * @return Determinante.
	 */
	private double calcolaDeterminanteGauss() {
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
	 * Metodo che implementa il calcolo del determinante attraverso l'algoritmo di Sarrus.
	 * 
	 * @return Determinante.
	 */
	private double calcolaDeterminanteSarrus() {
		// So con certezza che la dimensione è 3
		double det = matrice[0][0] * matrice[1][1] * matrice[2][2];
		det += matrice[1][0] * matrice[2][1] * matrice[0][2];
		det += matrice[2][0] * matrice[0][1] * matrice[1][2];
		det -= matrice[0][0] * matrice[2][1] * matrice[1][2];
		det -= matrice[1][0] * matrice[0][1] * matrice[2][2];
		det -= matrice[2][0] * matrice[1][1] * matrice[0][2];
		return det;
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
	 * Nota: i metodi della classe (ad eccezione dei costruttori) hano come modifcatore di accesso quello di
	 * default, ma questo metodo è public.
	 * Questo succede perchè non è possibile ridurre la visibilità di un metodo ereditato dalla superclasse,
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
	
}
