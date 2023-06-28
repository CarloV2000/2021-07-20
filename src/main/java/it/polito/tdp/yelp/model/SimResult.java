package it.polito.tdp.yelp.model;

import java.util.Map;

public class SimResult {

	private Integer numeroGiorniSimulati;
	private Map<Integer, Integer>nTotUtentiIntervistatiPerIntervistatore;
	
	public SimResult(Integer numeroGiorniSimulati, Map<Integer, Integer> nTotUtentiIntervistatiPerIntervistatore) {
		this.numeroGiorniSimulati = numeroGiorniSimulati;
		this.nTotUtentiIntervistatiPerIntervistatore = nTotUtentiIntervistatiPerIntervistatore;
	}
	public Integer getNumeroGiorniSimulati() {
		return numeroGiorniSimulati;
	}
	public void setNumeroGiorniSimulati(Integer numeroGiorniSimulati) {
		this.numeroGiorniSimulati = numeroGiorniSimulati;
	}
	public Map<Integer, Integer> getnTotUtentiIntervistatiPerIntervistatore() {
		return nTotUtentiIntervistatiPerIntervistatore;
	}
	public void setnTotUtentiIntervistatiPerIntervistatore(Map<Integer, Integer> nTotUtentiIntervistatiPerIntervistatore) {
		this.nTotUtentiIntervistatiPerIntervistatore = nTotUtentiIntervistatiPerIntervistatore;
	}
	
	
}
