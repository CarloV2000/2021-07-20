package it.polito.tdp.yelp.model;

public class Evento implements Comparable<Evento> {

	public enum EventType{
		INTERVISTA
	}
	
	private int giorno;
	private EventType type;
	private Integer intervistatore;
	private User intervistato;
	
	public Evento(int giorno, EventType type, Integer intervistatore, User intervistato) {
		super();
		this.giorno = giorno;
		this.type = type;
		this.intervistatore = intervistatore;
		this.intervistato = intervistato;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Integer getIntervistatore() {
		return intervistatore;
	}

	public void setIntervistatore(Integer intervistatore) {
		this.intervistatore = intervistatore;
	}

	public User getIntervistato() {
		return intervistato;
	}

	public void setIntervistato(User intervistato) {
		this.intervistato = intervistato;
	}

	@Override
	public int compareTo(Evento o) {
		return this.giorno-o.giorno;
	}
	
	
}
