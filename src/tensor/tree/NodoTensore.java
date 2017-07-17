package tensor.tree;

import java.util.ArrayList;
import java.util.List;

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
	
	private static final String INTRO_NODI_FIGLI = "Nodi Figli:\n";
	private static final String INTRO_LABEL = "LABEL: %s\n";
	private static final String INTRO_UNITA = "NODO %s: Unità di tensore = %10.3f";
	
	private String label;
	
	private List<NodoTensore> nodi_figli;
	private boolean is_root;
	
	double unita_tensore;

	/**
	 * Costruttore che definisce l'etichetta del Nodo e modifica il flag che identifica la radice.
	 * 
	 * @param _label Etichetta.
	 * @param _is_root TRUE se il nodo da costruire è il Nodo radice.
	 */
	public NodoTensore(String _label, boolean _is_root) {
		this.label = _label;
		this.is_root = _is_root;
		this.nodi_figli = new ArrayList<NodoTensore>();
	}
	
	public NodoTensore(boolean _is_root) {
		this(null, _is_root);
	}
	
	public NodoTensore() {
		this(null, false);
	}
	
	public void setRoot(boolean _is_root)  {
		this.is_root = _is_root;
		if (this.is_root) {
			this.unita_tensore = Double.MAX_VALUE;
		} else {
			this.unita_tensore = Double.MIN_VALUE;
		}
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String _label) {
		this.label = _label;
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
				this.unita_tensore = Math.max(this.unita_tensore, nuovo_figlio.unita_tensore);
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
		if (label_da_cercare == Tensore.DEFAULT_LABEL)
			return false; // La Label "Tensore" funge da jolly.
		
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
	 * Restituisce una descrizione del contenuto dell'oggetto.
	 */
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append(String.format(INTRO_LABEL, this.label));
		if (this.nodi_figli.size() > 0)
			s.append(INTRO_NODI_FIGLI);
		for (NodoTensore nt : this.nodi_figli) {
			s.append(nt.toString());
		}
		return s.toString();
	}
	
	/**
	 * Restituisce la lista degli indici di ciascun Tensore dell'albero.
	 * 
	 * @return
	 */
	public List<String> getListaIndici() {
		List<String> lista = new ArrayList<String>();
		for (NodoTensore nt : this.nodi_figli) {
			lista.addAll(nt.getListaIndici());
		}
		return lista;
	}
	
	public List<String> getListaUnita() {
		List<String> lista = new ArrayList<String>();
		lista.add(String.format(INTRO_UNITA, this.label, this.unita_tensore));
		for (NodoTensore nt : this.nodi_figli) {
			lista.addAll(nt.getListaUnita());
		}
		return lista;
	}

}
