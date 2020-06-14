package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	private SerieADAO dao;
	private Map<Integer,Season> map;
	private Graph<Season, DefaultWeightedEdge> graph;
	private double best ;
	private Season bestSeason;
	private List<SeasondAndPoints> bestSoluion;
	private int bestL;
	
	public Model() {
this.dao= new SerieADAO();
this.map= new HashMap<Integer, Season>();
this.dao.listAllSeasons(map);


}
	
	
	
	public List<Team> listTeams() {
		return this.dao.listTeams();
	}

	public List<SeasondAndPoints> listSeasondAndPoints(String team)
	{
		return this.dao.listSeasondAndPoints(team, map);
	}

	public void creaGrafo(String team)
	{
		this.graph=  new DefaultDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Season> list =  new LinkedList<>();
		for(SeasondAndPoints s : this.listSeasondAndPoints(team))
		{
			list.add(s.getSeason());
			
		}
		
		for(SeasondAndPoints s1 : this.listSeasondAndPoints(team))
		{
			for(SeasondAndPoints s2 : this.listSeasondAndPoints(team))
			{
				if(!s1.equals(s2))
				{
					if(s1.getPunteggio()>s2.getPunteggio())
					Graphs.addEdge(this.graph, s1.getSeason(), s2.getSeason(), s1.getPunteggio()-s2.getPunteggio());
				}
			}
		}
		
		Graphs.addAllVertices(this.graph, list);
	}
	
	public int edgeSize()
	{
		return this.graph.edgeSet().size();
	}
	
	
	public int vertexSize()
	{
		return this.graph.vertexSet().size();
	}
	
	public double  in(Season date)
	{
		double peso=0;
		for(DefaultWeightedEdge s : this.graph.incomingEdgesOf(date))
		{
			peso+=this.graph.getEdgeWeight(s);
		}
		return peso;
	}

	
	public double  out(Season date)
	{
		double peso=0;
		for(DefaultWeightedEdge s : this.graph.outgoingEdgesOf(date))
		{
			peso+=this.graph.getEdgeWeight(s);
		}
		return peso;
	}
	
	public void findMagg()
	{
		for(Season s : this.graph.vertexSet())
		{
			if((in(s)-out(s))>best)
			{
				best=in(s)-out(s);
				this.bestSeason=s;
			}
		}
	}



	public double getBest() {
		return best;
	}



	public Season getBestSeason() {
		return bestSeason;
	}
	
	
	public List<SeasondAndPoints> camminoVirtuoso(String team)
	{
		this.bestL=0;
		this.bestSoluion= new LinkedList<>();
		List<Season> parziale = new ArrayList<>();
		/*perche quando uso ultimo deve sempre inserire un elemento prima di partire con la risorsione latrimenti parziale.get(parziale.size -1) mi 
		 * da un null poin exception
		 * poiche non mi specifica da quale iniziare faccio un for e provo con tutti */
		for(Season s : this.graph.vertexSet())
		{
			parziale.add(s);
			ricorsivo(1,parziale);
			parziale.remove(0);
		}
		
		return this.bestSoluion;
		
	}
private void ricorsivo(int livello,List<Season> parziale) {
	
		boolean trovato = false;
		
		Season ultimo = parziale.get(parziale.size()-1);
		for(Season prossimo : Graphs.successorListOf(this.graph, ultimo ))
		{
			if(!parziale.contains(prossimo))
			{
				//if(stazioniConsecutive().indexOf(ultimo)+1 == stazioniConsecutive().indexOf(prossimo)) //vuol dire che sono uno il successore dell altro
				{ trovato = true;
					parziale.add(prossimo);
				ricorsivo(livello+1,parziale);
				parziale.remove(parziale.size()-1);
				}
			}
		}
		
		if(!trovato)
		{
			if(parziale.size() > bestL)
			{
				this.bestL=parziale.size();
				//this.bestSoluion= new  ArrayList<>(parziale);
			}
		}
	}
	
	
}
