package tensor;

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import tensor.tree.Matrice;
import tensor.tree.NodoTensore;
import tensor.utility.MathUtility.AlgoritmoPerDeterminante;
import tensor.utility.ParserXML;

/**
 * Classe Main che si occupa del'avvio e dell'esecuzione del programma nel suo complesso.
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class Main {

	private static final String [] INPUTS = {"input_0.xml", "input_1.xml", "input_2.xml"};

	private static final String UNITA_NODI = "\nUnità di tensore per Nodi e Tensori:";
	private static final String INDICI_TENSORI = "\nUnità di tensore:";
	private static final String STRING_TEMPI = "Tempo medio con l'algoritmo \"%s\":";
	private static final String STRING_TEMPI_DIM = "  Matrici %dx%d: %.9f secondi.";
	private static final String STRING_INPUT_TITLE = "Calcolo per il file: \"%s\"";

	private static final int MAX_DIM_MATRICE = 100; // E' la massima dimensione per cui viene effettuato il calcolo dei tempi (vedi modulo 2).
	private static final double NANOS_PER_SECONDS = 1E9;
	
	/**
	 * Metodo che gestisce l'esecuzione e il flusso del programma.
	 */
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
		// Eseguo entrambi i moduli per i file di input.
		for (String input : INPUTS) {
			System.out.println(String.format(STRING_INPUT_TITLE, input));
			modulo1(input);
			modulo2(input);
		}

	}
	
	/**
	 * Implementazione del modulo 1.
	 */
	private static void modulo1(String file) throws FileNotFoundException, XMLStreamException  {
		// Lettura da file
		NodoTensore albero = ParserXML.leggiFile(file);
		
		// Stampo elenco degli indici
		System.out.println(INDICI_TENSORI);
		for (String s : albero.getListaIndici()) {
			System.out.println(s);
		}
		// Stampo elenco delle Unità di Tensore
		System.out.println(UNITA_NODI);
		for (String s : albero.getListaUnita()) {
			System.out.println(s);
		}
	}
	
	/**
	 * Implementazione del modulo 2.
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	private static void modulo2(String file) throws FileNotFoundException, XMLStreamException {
		// Lettura da file
		NodoTensore albero = ParserXML.leggiFile(file);
		
		// Preparazione delle variabili ausiliarie per il calcolo dei tempi
		long [][] start_tempi = new long[AlgoritmoPerDeterminante.values().length][MAX_DIM_MATRICE];
		long [][] media_tempi = new long[AlgoritmoPerDeterminante.values().length][MAX_DIM_MATRICE];
		// Preparo il conteggio delle matrici per dimensione
		int [] conteggio_matrici = new int [MAX_DIM_MATRICE];
		for (int i = 0; i < MAX_DIM_MATRICE; i++) {
			conteggio_matrici[i] = 0;
		}
		// Estraggo la lista delle matrici
		List<Matrice> matrici = albero.getMatrici();
		Matrice matr = null;
		
		for (int mat_index = 0; mat_index < matrici.size(); mat_index++) {
			matr = matrici.get(mat_index);
			conteggio_matrici[matr.getDimensione() - 1]++;
			
			// Calcolo il determinante delle matrici in tutti i modi possibili
			for (int a = 0; a < AlgoritmoPerDeterminante.values().length; a++) {
				AlgoritmoPerDeterminante algo = AlgoritmoPerDeterminante.values()[a];
				
				start_tempi[a][matr.getDimensione() - 1] = System.nanoTime();
				if (matr.getDimensione() >= AlgoritmoPerDeterminante.getMinDimMatrice(AlgoritmoPerDeterminante.values()[a]) &&
						matr.getDimensione() <= AlgoritmoPerDeterminante.getMaxDimMatrice(AlgoritmoPerDeterminante.values()[a])) {
					matr.calcolaDeterminante(algo);
					media_tempi[a][matr.getDimensione() - 1] += System.nanoTime() - start_tempi[a][matr.getDimensione() - 1];
				}
			}
		}
		
		
		for (int a = 0; a < AlgoritmoPerDeterminante.values().length; a++) {
			System.out.println(String.format(STRING_TEMPI, AlgoritmoPerDeterminante.values()[a]));
			for (int d = AlgoritmoPerDeterminante.getMinDimMatrice(AlgoritmoPerDeterminante.values()[a]); d <= AlgoritmoPerDeterminante.getMaxDimMatrice(AlgoritmoPerDeterminante.values()[a]); d++) {
				if (conteggio_matrici[d - 1] != 0) {
					media_tempi[a][d - 1] /= conteggio_matrici[d - 1];
					System.out.println(String.format(STRING_TEMPI_DIM, d, d, media_tempi[a][d - 1] / NANOS_PER_SECONDS));
				}
			}
		}
		System.out.println("\n\n");
		
	}

}
