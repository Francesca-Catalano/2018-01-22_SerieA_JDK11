package it.polito.tdp.seriea.model;

public class SeasondAndPoints {
	private Season season;
	private int punteggio;
	public SeasondAndPoints(Season season, int punteggio) {
		super();
		this.season = season;
		this.punteggio = punteggio;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	@Override
	public String toString() {
		return  season.getSeason() + " " + punteggio ;
	}
	public Season getSeason() {
		return season;
	}

}
