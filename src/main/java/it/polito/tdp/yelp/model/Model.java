package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<User, DefaultWeightedEdge>grafo;
	private List<User>allUsers;
	private YelpDao dao;
	private Map<String, User>idMapUsers;
	private Integer maxSimilarita;
	
	public Model() {
		this.allUsers = new ArrayList<>();
		this.dao = new YelpDao();
		this.idMapUsers = new HashMap<>();
	}

	public String creaGrafo(Integer anno, Integer nMinRecensioni) {
		this.grafo = new SimpleWeightedGraph<User, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allUsers = dao.getAllUsers(nMinRecensioni);
		Graphs.addAllVertices(grafo, this.allUsers);
		
		for(User x : this.allUsers) {
			this.idMapUsers.put(x.getUserId(), x);
		}
		
		for(User x : allUsers) {
			for(User y : allUsers) {
				Integer similarita = dao.getSimilarita(anno, x, y);
				if(!x.equals(y) && similarita > 0) {
					Graphs.addEdge(grafo, x, y, similarita);
				}
			}
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi";
	}
	
	public List<User>getAllUtentiConMaggiorSimilarita(User u){
		List<User>adiacenti = new ArrayList<>(Graphs.neighborListOf(grafo, u));
		this.maxSimilarita = 0;
		List<User> res = new ArrayList<>();
		for(User x : adiacenti) {
			DefaultWeightedEdge e = grafo.getEdge(u, x);
			int n = (int) grafo.getEdgeWeight(e);
			if(n == maxSimilarita) {
				res.add(x);
			}else if(n > maxSimilarita) {
				this.maxSimilarita = n;
				res.clear();
				res.add(x);
			}
		}
		return res;
	}


	public Graph<User, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public Map<String, User> getIdMapUsers() {
		return idMapUsers;
	}

	public Integer getMaxSimilarita() {
		return maxSimilarita;
	}
//ritornare sim result
	public SimResult simula(Integer x1, Integer x2, int anno, Integer nMinRecensioni) {
		Simulatore sim = new Simulatore(x1, x2, anno, nMinRecensioni);
		sim.initialize();
		sim.run();
		SimResult res = sim.getRisultato();
		return res;
	}
	
	
	
}
