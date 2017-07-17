package tensor.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import tensor.tree.NodoTensore;
import tensor.tree.Matrice;
import tensor.tree.Tensore;

/**
 * Classe Parser che legge in input un file .xml, crea l'albero di NodiTensore e lo restituisce al Main.
 * La lettura viene effettuata in blocchi separati, dove ogni blocco è responsabile della creazione di un singolo oggetto.
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public final class ParserXML {
	
	private static final int IN_BUFF_SIZE = 4096;
	
	// Tag
	private static final String TAG_NODE = "tensornode";
	private static final String TAG_TENSOR = "tensor";
	private static final String TAG_MATRIX = "matrix";
	private static final String TAG_TREE = "ttree";
	private static final String TAG_LABEL = "label";
	private static final String TAG_ROW = "row";
	private static final String TAG_COLUMN = "column";
	
	// Logs
	private static final String LOG_START_READING = "Inizio a leggere il documento...";
	private static final String LOG_END_READING = "Lettura terminata.";
			
	private static XMLStreamReader reader;
	
	private ParserXML() {};

	/**
	 * Metodo effettivo che si occupa del parsing.
	 * Vedere la descrizione della classe per ulteriori informazioni.
	 * 
	 * @param nome_file Il nome del File da leggere.
	 * @return
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 */
	public static NodoTensore leggiFile(String nome_file) throws FileNotFoundException, XMLStreamException {
		// Preparazione degli oggetti necessari
		XMLInputFactory factory = XMLInputFactory.newFactory();
		reader = factory.createXMLStreamReader(new BufferedReader(new FileReader(new File(nome_file)), IN_BUFF_SIZE));
		NodoTensore root = null;
		boolean is_building = false;
		
		// Ciclo di lettura
		while (reader.hasNext()) {
			switch(reader.next()) {
			case XMLStreamConstants.START_DOCUMENT:
				System.out.println(LOG_START_READING);
				break;
			case XMLStreamConstants.START_ELEMENT:
				switch(reader.getLocalName().toLowerCase()) {
				case TAG_TREE:
					is_building = true;
					break;
				case TAG_NODE:
					if (is_building) { // Controllo che si sia dichiarato il tag iniziale
						root = costruisciNodoTensore(true);
					}
					break;
				default:
					// Error
					throw new IllegalStateException(reader.getLocalName());
				}
				break;
			case XMLStreamConstants.END_DOCUMENT:
				System.out.println(LOG_END_READING);
				break;
			}
		}
		return root;
	}
	
	private static NodoTensore costruisciNodoTensore(boolean is_root) throws XMLStreamException {
		boolean is_building = true;
		NodoTensore nodo = new NodoTensore(is_root);
		do {
			switch(reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName().toLowerCase()) {
				case TAG_LABEL:
					if (reader.next() == XMLStreamConstants.CHARACTERS) {
						nodo.setLabel(reader.getText());
					}
					break;
				case TAG_NODE:
					nodo.aggiungiFiglio(costruisciNodoTensore(false));
					break;
				case TAG_TENSOR:
					nodo.aggiungiFiglio(costruisciTensore());
					break;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (reader.getLocalName().equals(TAG_NODE)) {
					is_building = false;
				}
			}
		} while (is_building && reader.hasNext());
		return nodo;
	}
	
	private static Tensore costruisciTensore() throws IllegalArgumentException, XMLStreamException {
		boolean is_building = true;
		Tensore tensore = new Tensore();
		do {
			switch(reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName().toLowerCase()) {
				/*
				 * NOTA: All'interno della consegna non è specificato da nessuna parte se i Tensori 
				 * possano avere o non avere altri Tensori o NodiTensori come figli. E' solamente richiesto
				 * che i Nodi "foglia" (quelli senza figli) siano tensori. Per questo motivo, poiché non intendo 
				 * escludere una possibilità di cui non viene fatta menzione, prevedo anche il caso in cui un
				 * Tensore abbia dei nodi figli.
				 */
				case TAG_NODE:
					tensore.aggiungiFiglio(costruisciNodoTensore(false));
					break;
				case TAG_TENSOR:
					tensore.aggiungiFiglio(costruisciTensore());
					break;
				case TAG_MATRIX:
					tensore.aggiungiMatrice(costruisciMatrice());;
					break;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (reader.getLocalName().equals(TAG_TENSOR)) {
					is_building = false;
				}
			}
		} while (is_building && reader.hasNext());
		return tensore;
	}
	
	private static Matrice costruisciMatrice() throws NumberFormatException, XMLStreamException {
		boolean is_building = true;
		List<List<Double>> temp_matrice = new ArrayList<>();
		List<Double> current_row = null;
		do {
			switch(reader.next()) {
			case XMLStreamConstants.START_ELEMENT:
				switch (reader.getLocalName().toLowerCase()) {
				case TAG_ROW:
					current_row = new ArrayList<Double>();
					temp_matrice.add(current_row);
					break;
				case TAG_COLUMN:
					if (reader.next() == XMLStreamConstants.CHARACTERS) {
						int read_value = Integer.parseInt(reader.getText().trim());
//						System.out.println(read_value);
						current_row.add(Double.parseDouble(read_value + ""));
					}
					break;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (reader.getLocalName().equals(TAG_MATRIX)) {
					is_building = false;
				}
			}
		} while (is_building && reader.hasNext());
		return new Matrice(temp_matrice);
	}
}
