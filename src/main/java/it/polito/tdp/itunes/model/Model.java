package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	
	private Graph<Album, DefaultWeightedEdge> grafo;
	
	private ItunesDAO dao;
	
	private Map<Integer, Album> idMap;
	
	private List<Album> camminoMassimo;
	int contatore = 0;
	int max = 0;
	
	public Model() {
		
		this.dao = new ItunesDAO();
		
		this.idMap = new HashMap<Integer, Album>();
	}
	
	public String creaGrafo(int n) {
		
		this.dao.getAllAlbums(n, idMap);
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici
		
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//Creo gli archi
		int durataValida = 4*n*1000;
		
		for(Album a1: this.grafo.vertexSet()) {
			for(Album a2: this.grafo.vertexSet()) {
				
				if(!a1.equals(a2)) {
					
					int sommaDurate = a1.getDurata()+ a2.getDurata();
					if(a1.getDurata() > a2.getDurata() && sommaDurate > durataValida) {
									
							Graphs.addEdgeWithVertices(this.grafo, a2, a1, sommaDurate);
					}
						else if(a2.getDurata() > a1.getDurata() && sommaDurate > durataValida){				
							Graphs.addEdgeWithVertices(this.grafo, a1, a2, sommaDurate);
					}
				}
			}
		}

		return "Grafo creato!\n"+"Numero vertici: "+this.grafo.vertexSet().size()+"\n"+
		"Numero archi: "+this.grafo.edgeSet().size()+"\n";
		
	}
	
	public List<Album> getAlbum(){
		List<Album> temp = new LinkedList<Album>(idMap.values());
		Collections.sort(temp);
		return temp;
	}
	
	public List<AlbumBilancio> getCammino(Album a){
		
		List<Album> successori = Graphs.successorListOf(this.grafo, a);
		List<AlbumBilancio> lista = new LinkedList<AlbumBilancio>();
		
		for(Album a1: successori) {
			AlbumBilancio ab = this.getAlbumBilancio(a1);
			if(ab!=null) {
			lista.add(ab);
			}
		}
		Collections.sort(lista);
		return lista;
	}
	
	public AlbumBilancio getAlbumBilancio(Album a) {
		
		List<Album> predecessori =  Graphs.predecessorListOf(this.grafo, a);
		List<Album> successori = Graphs.successorListOf(this.grafo, a);
		AlbumBilancio ab = null;
		
		double sommaP = 0.0;
		double sommaS = 0.0;
		double bilancio = 0.0;
		
				for(Album a1: predecessori) {
					
				sommaP += this.grafo.getEdgeWeight(this.grafo.getEdge(a1, a));
			}
				for(Album a2: successori) {
					
					sommaS += this.grafo.getEdgeWeight(this.grafo.getEdge(a, a2));
				}
				
			bilancio = sommaP-sommaS;
			
			ab = new AlbumBilancio(a, bilancio);
			
			return ab;
	}
	
	public List<Album> calcolaPercorso(Album partenza, Album destinazione, int x)
	{
	
		
		camminoMassimo = new LinkedList<Album>();	//Creo la lista cammino.
		
		List<Album> parziale = new ArrayList<Album>();	//Creo la lista parziale.
		
		parziale.add(partenza);							//Io so già che parto dall'album inserito dall'utente.
		
		cerca(parziale, destinazione, x);
		
		return camminoMassimo;
	}

	private void cerca(List<Album> parziale, Album destinazione, int x) {
		
		AlbumBilancio ab0 = this.getAlbumBilancio(parziale.get(0));
		
		//Stato terminale
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			
			if(contatore > max) {
				max = contatore;
				camminoMassimo = new LinkedList<Album>(parziale);
				return;
			}
		}
			//Stato normale
			for(Album a: Graphs.successorListOf(this.grafo, parziale.get(parziale.size()-1))) {
				
				contatore = 0;	//Contatore numero vertici con bilancio superiore a album di partenza
				
				if(!parziale.contains(a)) {	//Se la lista non contiene (prevengo cicli)
					
				if(this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(parziale.size()-1), a)) > x) {	//Se il peso dell'arco formato dall'ultimo elemento della lista e l'album da inserire supera x
					
					AlbumBilancio ab = this.getAlbumBilancio(a);
					if(ab.getBilancio() > ab0.getBilancio()) {	//Se il bilancio del vertice a supera quello del vertice di partenza, aggiungi uno.
						contatore++;
					}
					
					parziale.add(a);	//Aggiungi l'album
					cerca(parziale, destinazione, x);
					parziale.remove(parziale.size()-1);
					
				}
			}
			
		}
		
	}
}
