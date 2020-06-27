package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	ExtFlightDelaysDAO dao;
	Graph<String, DefaultWeightedEdge> grafo;
	List<Collegamento> collegamenti;

	
	public Model() {
		dao= new ExtFlightDelaysDAO();
	}
	
	public List<String> getVertici(){
		return dao.getVertici();
	}
	
	public void creaGrafo() {
		grafo= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici());
		collegamenti= dao.getArchi();
		
		for(Collegamento c:collegamenti) {
				if(this.grafo.containsVertex(c.getV1()) && this.grafo.containsVertex(c.getV2()) && !this.grafo.containsEdge(c.getV1(), c.getV2())) {
					Graphs.addEdgeWithVertices(grafo, c.getV1(), c.getV2(), c.getPeso());
				}
			}
		
		
		System.out.println("Grafo creato con "+ this.grafo.vertexSet().size() +" vertici e " + this.grafo.edgeSet().size() +" archi");
	
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Collegamento> getVicini(String stato){
		List<Collegamento> coll= new ArrayList<>();
		
		for(DefaultWeightedEdge d: this.grafo.outgoingEdgesOf(stato)) {
			coll.add(new Collegamento(this.grafo.getEdgeSource(d), this.grafo.getEdgeTarget(d), this.grafo.getEdgeWeight(d)));
		}
		Collections.sort(coll);
		return coll;
	}
}
