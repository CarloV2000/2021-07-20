package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;
import it.polito.tdp.yelp.model.Evento.EventType;

public class Simulatore {
	       //parametri di ingresso
			private int x1; //n intervistatori disponibili (sempre < di x2)
			private int x2; //n utenti da intervistare (deve essere < del n vertici del grafo!)
			
			//parametri
			private Model model;
			private YelpDao dao;
			private List<User>vertici;
			private Graph<User, DefaultWeightedEdge>grafo;
			private Integer numeroUtentiIntervistati;
			private int anno;
			private Integer nMinRecensioni;
			private List<User>rimanenti;
			
			//variabili di uscita
			private List<Integer>numeroUtentiIntervistatiPerOgniIntervistatore;
			private Integer numeroGiorniSimulati;//la simulazione termina quando vengono intervistati x2 utenti
			
			//stato del mondo :
			//il primo numero è un intero che rappresenta l'intervistatore
			//il secondo è la lista di utenti intervistati
			private Map<Integer, Integer>mappaIntervistatoriUtentiIntervistati;
			
			
			//coda degli eventi
			PriorityQueue<Evento> queue;
			

			public Simulatore(int x1, int x2, int anno, Integer nMinRecensioni) {
				super();
				this.x1 = x1;
				this.x2 = x2;
				this.anno = anno;
				this.nMinRecensioni = nMinRecensioni;
				this.model = new Model();
				this.dao = new YelpDao();
				this.numeroUtentiIntervistatiPerOgniIntervistatore = new ArrayList<>();
				this.mappaIntervistatoriUtentiIntervistati = new HashMap<>();
				this.grafo = new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
				model.creaGrafo(anno, nMinRecensioni);
				this.grafo = model.getGrafo();
				this.rimanenti = new ArrayList<>(grafo.vertexSet());
				
			}
			
			
			
			/**
			 * metodo che popola la coda degli eventi
			 */
			public void initialize() {
				this.numeroUtentiIntervistati = 0;
				this.queue = new PriorityQueue<Evento>();
				
				//eventi INTERVISTA
				rimanenti = new ArrayList<>(model.getGrafo().vertexSet());
			
				for (int i = 0; i < x1; i++) {
					if(numeroUtentiIntervistati > this.x2) {
						break;
					}
						User userScelto = this.getSceltaUser(rimanenti);
						rimanenti.remove(userScelto);
						this.queue.add(new Evento(1, EventType.INTERVISTA, i, userScelto));
						this.mappaIntervistatoriUtentiIntervistati.put(i, 1);
						System.out.print("Intervista da parte dell'intervistatore "+ i + " all'User "+userScelto+" al giorno "+1+"\n");
				}
				
			}
			
			private User getSceltaUser(List<User> utenti) {
				Integer n = (int) (Math.random()*(utenti.size()-1));
				User u = null;
				u = utenti.get(n);
				return u;
			}

			public void run() {
				
				this.numeroGiorniSimulati = 1;
				this.vertici = new ArrayList<>(grafo.vertexSet());
				
				while(!queue.isEmpty()) {
					Evento e = queue.poll();
					
					Integer time = e.getGiorno();
					EventType type = e.getType();
					Integer intervistatore = e.getIntervistatore();
					User intervistato = e.getIntervistato();
					this.numeroUtentiIntervistati = this.getNumeroUtentiIntervistati();
					if(this.numeroUtentiIntervistati > this.x2){
						break;
					}
					
					switch(type) {
					case INTERVISTA:
						double prob = Math.random();
						if(prob <= 0.6) {
							//intervistatore porta a termine l'intervista e ne intervista un altro il giorno dopo
							// l'utente assegnato è scelto come il piu simile tra quelli non ancora assegnati 
							// se ce ne sono tanti simili se ne sceglie uno a caso
							this.model.getMaxSimilarita();
							List<User> userScelti = this.getAllUtentiConMaggiorSimilarita(intervistato);
							User userScelto = this.getSceltaUser(userScelti);
							this.queue.add(new Evento(time+1, EventType.INTERVISTA, intervistatore, userScelto));
							rimanenti.remove(userScelto);
							this.mappaIntervistatoriUtentiIntervistati.put(intervistatore, mappaIntervistatoriUtentiIntervistati.get(intervistatore)+1);
							System.out.println("Intervistatore "+intervistatore+" porta a termine l'intervista e ne intervista un altro il giorno dopo (al giorno "+(time+1)+")\n");
						}
						else if(prob > 0.6 && prob <= 0.8) {
							//intervistatore porta a termine l'intervista e fa pausa per un giorno 
							this.model.getMaxSimilarita();
							List <User> userScelti = model.getAllUtentiConMaggiorSimilarita(intervistato);
							User userScelto = this.getSceltaUser(userScelti);
							this.queue.add(new Evento(time+2, EventType.INTERVISTA, intervistatore, userScelto));
							rimanenti.remove(userScelto);
							this.mappaIntervistatoriUtentiIntervistati.put(intervistatore,  mappaIntervistatoriUtentiIntervistati.get(intervistatore)+1);

							System.out.println("Intervistatore "+intervistatore+" porta a termine l'intervista e ne intervista un altro due giorni dopo (al giorno "+(time+2)+")\n");
						}
						else if(prob > 0.8) {
							this.queue.add(new Evento(time+1, EventType.INTERVISTA, intervistatore, intervistato));
							rimanenti.remove(intervistato);
							this.mappaIntervistatoriUtentiIntervistati.put(intervistatore,  mappaIntervistatoriUtentiIntervistati.get(intervistatore)+1);
							System.out.println("Intervistatore "+intervistatore+" NON porta a termine l'intervista e lo reintervista il giorno dopo (al giorno "+(time+1)+")\n");
						}
					}
					break;
				}
				
			}//
			
			private List<User> getAllUtentiConMaggiorSimilarita(User intervistato) {
				List<User>utentiSimili = new ArrayList<>(model.getAllUtentiConMaggiorSimilarita(intervistato));
				List<User> utentiValidi = new ArrayList<>();
				for(User x : utentiSimili) {
					if(rimanenti.contains(x)) {
						utentiValidi.add(x);
					}
				}
				return utentiValidi;
			}



			public Integer getNumeroUtentiIntervistati() {
				Integer n = 0;
				for(Integer x : this.mappaIntervistatoriUtentiIntervistati.values()) {
					n += x;
				}
				return n;
			}
			
			public SimResult getRisultato() {
				Integer n = this.numeroUtentiIntervistati;
				Map<Integer, Integer>mappa = new HashMap<>(this.mappaIntervistatoriUtentiIntervistati);
				SimResult res = new SimResult(n,mappa);
				return res;
			}
}
