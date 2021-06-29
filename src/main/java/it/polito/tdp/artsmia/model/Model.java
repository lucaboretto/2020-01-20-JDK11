package it.polito.tdp.artsmia.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idArtistMap;
	private List<Artist> result;
	private int numExpMax;
	public Model() {
		this.dao = new ArtsmiaDAO()	;
	}
	
	public void creaGrafo(String ruolo) {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idArtistMap = new HashMap<>();
		dao.listArtists(idArtistMap, ruolo);
		
		Graphs.addAllVertices(this.grafo, this.idArtistMap.values());
		for(Adiacenza a: this.dao.getArchi(ruolo, this.idArtistMap)) {
			Artist a1 = a.getA1();
			Artist a2 = a.getA2();
			Graphs.addEdgeWithVertices(this.grafo, a1, a2, a.getPeso());
		}
		
		System.out.println("GRAFO CREATO\n");
		System.out.println("NUMERO VERTICI: " + this.grafo.vertexSet().size()+"\n");
		System.out.println("NUMERO ARCHI: " + this.grafo.edgeSet().size()+ "\n");

	
	} 
	
	public List<Artist> cercaPercorso(int idStart){
		List<Artist> parziale = new ArrayList<>();
		parziale.add(this.idArtistMap.get(idStart));
		this.result = new ArrayList<>();
		cerca(parziale);
		System.out.println("\nNumero esposizioni condivise per massimizzare il risultato: " + this.numExpMax +"\n");
		for(Artist a: result) {
			System.out.println(a + "\n");
		}
		return result;
	}
	
	private void cerca(List<Artist> parziale) {
		
		Artist ultimo = parziale.get(parziale.size() - 1);
		List<Artist> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		if(vicini.size()!= 0 && parziale.containsAll(vicini)) {
			if(parziale.size() > this.result.size()) {
				result = new ArrayList<>(parziale);
				numExpMax = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(result.get(0), result.get(1)));  
				return;
			}
		}
		else {
			for(Artist a: vicini) {
				if(!parziale.contains(a)) {
					if(valido(parziale, a)) {
						parziale.add(a);
						cerca(parziale);
						parziale.remove(a);
					}
				}
			}
		}
		
		
	}
	
	private boolean valido(List<Artist> parziale, Artist a) {
		Artist ultimo = parziale.get(parziale.size()-1);
		if(!ultimo.equals(parziale.get(0))) {
			//non è il primo arco, che determina il peso di tutti quelli dopo
			DefaultWeightedEdge e = this.grafo.getEdge(ultimo, a);
			DefaultWeightedEdge primoArco = this.grafo.getEdge(parziale.get(0), parziale.get(1));
			double pesoPrimo = this.grafo.getEdgeWeight(primoArco);
			double peso = this.grafo.getEdgeWeight(e);
			if(peso==pesoPrimo)
				return true;
			else
				return false;
		}
		return true;
	}
	
	public int getNumEsposizioniPercorsoMax() {
		return this.numExpMax;
	}
	
	public List<Adiacenza> getAdiacenze(String ruolo){
		List<Adiacenza> adiacenze = new ArrayList<>(this.dao.getArchi(ruolo, this.idArtistMap));
		Collections.sort(adiacenze);
		return adiacenze;
	}
	
	
	public List<String> getRuoli(){
		return this.dao.getRoles();
	}
	
	public List<Integer> getIdArtisti()	{
		List<Integer> res = new ArrayList<>(this.idArtistMap.keySet());
		Collections.sort(res);
		return res;
	}
	
}
