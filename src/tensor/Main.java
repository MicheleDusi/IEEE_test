package tensor;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import tensor.tree.Matrice;
import tensor.tree.NodoTensore;
import tensor.utility.MathUtility;
import tensor.utility.ParserXML;

/**
 * Classe Main che si occupa del'avvio e dell'esecuzione del programma nel suo complesso.
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class Main {

	private static final String INPUT_0 = "input_0.xml";

	private static final String UNITA_NODI = "\nUnità di tensore per Nodi e Tensori:";
	private static final String INDICI_TENSORI = "\nUnità di tensore:";
	private static final String STRING_TEMPI = "Tempo medio con l'algoritmo \"%s\": %f secondi.";

	private static final int TEST = 10;
	private static final double NANOS_PER_SECONDS = 1E9;
	
	/**
	 * Metodo che gestisce l'esecuzione e il flusso del programma.
	 */
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
		// Modulo 1
		modulo1();
		
		// Modulo 2
		modulo2();

	}
	
	/**
	 * Implementazione del modulo 1.
	 */
	private static void modulo1() throws FileNotFoundException, XMLStreamException  {
		// Lettura da file
		NodoTensore albero = ParserXML.leggiFile(INPUT_0);
		
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
	 */
	private static void modulo2() {
		long [] start_tempi = new long[MathUtility.AlgoritmoPerDeterminante.values().length];
		long [] media_tempi = new long[MathUtility.AlgoritmoPerDeterminante.values().length];
		
		for (int t = 0; t < TEST; t++) {
			// Genero una matrice casuale
			Matrice matr = Matrice.generaCasuale(Matrice.SARRUS_DIMENSION);
			
			for (int a = 0; a < MathUtility.AlgoritmoPerDeterminante.values().length; a++) {
				start_tempi[a] = System.nanoTime();
				matr.calcolaDeterminante(MathUtility.AlgoritmoPerDeterminante.values()[a]);
				media_tempi[a] += System.nanoTime() - start_tempi[a];
			}
		}
		
		
		System.out.println("\n\n");
		for (int a = 0; a < MathUtility.AlgoritmoPerDeterminante.values().length; a++) {
			media_tempi[a] /= TEST;
			System.out.println(String.format(STRING_TEMPI, MathUtility.AlgoritmoPerDeterminante.values()[a], media_tempi[a] / NANOS_PER_SECONDS));
		}
		
	}

}
