package tensor.tree;

import java.util.Comparator;
import java.util.TreeSet;

import tensor.utility.MathUtility;

/**
 * Classe che rappresenta un nodo nell'albero di Tensori del programma. Ogni nodo contiene i riferimenti
 * ai propri nodi figli, e un flag booleano che identifica il nodo radice.
 * Inoltre ogni nodo figlio è identificato univocamente dalla sua etichetta.
 * 
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class NodoTensore {
	
	private static final String EXCEPTION_LABEL_GIA_PRESENTE = "Un nodo con label \"%s\" è già presente come figlio.";
	
	private String label;
	
	private TreeSet<NodoTensore> nodi_figli;
	private boolean is_root;
	
	private double unita_tensore;
	
	/** Classe interna che implementa un comparatore per i Tensori. */
	private class NodoTensoreComparator implements Comparator<NodoTensore> {
		
		@Override
		public int compare(NodoTensore node1, NodoTensore node2) {
			double diff = node1.calcolaUnitaTensore() - node2.calcolaUnitaTensore();
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
	 * Costruttore che definisce l'etichetta del Nodo e modifica il flag che identifica la radice.
	 * 
	 * @param _label Etichetta.
	 * @param _is_root TRUE se il nodo da costruire è il Nodo radice.
	 */
	public NodoTensore(String _label, boolean _is_root) {
		this.label = _label;
		this.is_root = _is_root;
		this.nodi_figli = new TreeSet<NodoTensore>(new NodoTensoreComparator());
		this.unita_tensore = Double.MIN_VALUE;
	}
	
	/**
	 * Costruttore simile al precedente, permette la costruzioni di Nodi non-radice attraverso
	 * una sintassi più leggera.
	 * 
	 * @param _label Etichetta.
	 */
	public NodoTensore(String _label) {
		this(_label, false);
	}
	
	/**
	 * Metodo che collega un Nodo-figlio a questo Nodo.
	 * Il Nodo figlio viene aggiunto solo se non ne è presente già uno con la stessa etichetta, altrimenti
	 * viene lanciata una IllegalStateException.
	 * 
	 * @param nuovo_figlio
	 */
	public void aggiungiFiglio(NodoTensore nuovo_figlio) throws IllegalArgumentException {
		if (this.contieneLabel(nuovo_figlio.label)) {
			throw new IllegalArgumentException(String.format(EXCEPTION_LABEL_GIA_PRESENTE, nuovo_figlio.label));
		} else {
			this.nodi_figli.add(nuovo_figlio);
			if (this.is_root) {
				this.unita_tensore = Math.min(this.unita_tensore, nuovo_figlio.unita_tensore);
			} else {
				this.unita_tensore = Math.min(this.unita_tensore, nuovo_figlio.unita_tensore);
			}
		}
	}
	
	/**
	 * Metodo che ricerca un nodo attraverso la sua etichetta.
	 * 
	 * @param label_da_cercare Label con cui effettuare i confronti.
	 * @return TRUE se il nodo cercato è uno dei figli diretti del Nodo considerato. // TODO
	 */
	public boolean contieneLabel(String label_da_cercare) {
		// TODO Implementare ricerca ricorsiva
		for (NodoTensore nt : this.nodi_figli) {
			if (nt.label.equals(label_da_cercare)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo che indica se il NodoTensore considerato è un nodo-radice.
	 * 
	 * @return TRUE se il NodoTensore ha il flag "is_root" true.
	 */
	public boolean isRoot() {
		return this.is_root;
	}
	
	/**
	 * Metodo che restituisce l'Unità Tensore calcolata finora.
	 * Non effettua calcoli poiché il valore viene aggiornato di volta in volta.
	 */
	public double getUnitaTensore() {
		return this.unita_tensore;
	}
	
	/**
	 * Metodo che calcola <emph>esplicitamente</emph> l'Unità del Tensore in questione, ossia il massimo determinante
	 * fra tutti i determinanti delle matrici det Tensori figli.
	 * Nel caso del nodo-radice viene considerata come Unità di Tensore il minimo valore di
	 * Unità di Tensore fra i Nodi figli diretti.
	 * 
	 * @return Unità del Tensore.
	 */
	@Deprecated
	public double calcolaUnitaTensore() {
		if (this.is_root) {
			return this.nodi_figli.last().calcolaUnitaTensore(); // TODO Calcolo ricorsivo del minimo?
		} else {
			return this.nodi_figli.first().calcolaUnitaTensore();
		}
	}
	

}
