package tensor;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import tensor.tree.NodoTensore;
import tensor.utility.ParserXML;

public class Main {

	private static final String INPUT_0 = "input_0.xml";

	private static final String UNITA_NODI = "\nUnità di tensore per Nodi e Tensori:";
	private static final String INDICI_TENSORI = "\nUnità di tensore:";
	
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
		// Modulo 1
		modulo1();

	}
	
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

}
