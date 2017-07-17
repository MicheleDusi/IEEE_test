package tensor.tree;

import java.util.TreeSet;

/**
 * Classe che rappresenta un Tensore. All'interno dell'albero con cui lavora il programma ogni Tensore
 * è un nodo, per questo motivo la classe è figlia di NodoTensore.
 *  
 * @author Michele Dusi <michele.dusi.it@ieee.org>
 *
 */
public class Tensore extends NodoTensore {

	private static final String EXCEPTION_DIMENSIONE_MATRICE_DIFFERENTE = "Non è possibile aggiungere una matrice di questa dimensione al tensore.";
	private static final String EXCEPTION_TENSORE_PIENO = "Il tensore contiene già il numero corretto di matrici.";
	
	private TreeSet<Matrice> matrici;
	private int dimensione;

	/**
	 * Costruttore che inizializza il Tensore come vuoto, e definisce la label.
	 * 
	 * @param _label Etichetta identificativa.
	 */
	public Tensore(String _label) {
		super(_label);
		this.matrici = new TreeSet<Matrice>(Matrice.getComparator());
		this.dimensione = 0;
	}
	
	/**
	 * Metodo che aggiunge una matrice al Tensore. La Matrice deve rispettare le regole di costruzione
	 * dell'oggetto (essere quadrata e non degenere), e la sua dimensione deve concordare con le dimensioni
	 * delle altre matrici già presenti.
	 * Non è possibile aggiungere un numero di matrici superiore alla dimensione.
	 * La dimensione del tensore è automaticamente definita con l'aggiunta della prima matrice.
	 * 
	 * @param nuova_matrice L'oggetto Matrice da aggiungere.
	 * @throws IllegalArgumentException Se la Matrice non rispetta le dimensioni del Tensore.
	 * @throws IllegalStateException Se il Tensore contiene già il numero massimo di matrici. 
	 */
	public void aggiungiMatrice(Matrice nuova_matrice) throws IllegalArgumentException, IllegalStateException {
		if (this.matrici.isEmpty()) {
			this.matrici.add(nuova_matrice);
			this.dimensione = nuova_matrice.getDimensione();
		} else {
			if (nuova_matrice.getDimensione() != this.dimensione) {
				// La matrice che voglio aggiungere non corrisponde
				throw new IllegalArgumentException(EXCEPTION_DIMENSIONE_MATRICE_DIFFERENTE);
			} else if (this.matrici.size() == this.dimensione) {
				// Ho già aggiunto tutte le matrici che potevo
				throw new IllegalStateException(EXCEPTION_TENSORE_PIENO);
			} else {
				this.matrici.add(nuova_matrice);
			}
		}
	}
	
	/**
	 * Calcola l'indice del tensore sommando tutti i determinanti delle matrici che lo compongono.
	 * 
	 * @return Indice del tensore come double.
	 */
	public double calcolaIndice() {
		double somma_indice = 0;
		for (Matrice m : this.matrici) {
			somma_indice += m.calcolaDeterminanteGauss();
		}
		return somma_indice;
	}
	
	/**
	 * Restituisce il valore dell'Unità del Tensore.
	 * 
	 * @return Unità del Tensore.
	 */
	@Override
	public double getUnitaTensore() {
		if (this.isRoot()) {
			return this.matrici.last().calcolaDeterminanteGauss();
		} else {
			return this.matrici.first().calcolaDeterminanteGauss();
		}
	}
	
	/**
	 * Calcola la quantità TensorUnit come massimo fra i determinanti delle matrici che lo compongono.
	 * Poichè le matrici vengono ordinate durante la costruzione, il metodo restituisce il determinante
	 * della matrice nella prima posizione.
	 * 
	 * @return Unità del Tensore come double.
	 */
	@Override
	@Deprecated
	public double calcolaUnitaTensore() {
		return this.matrici.first().calcolaDeterminanteGauss();
	}
	
	
}
