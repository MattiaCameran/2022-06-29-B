package it.polito.tdp.itunes.model;

public class Adiacenza {

	Album a1;
	Album a2;
	int peso;
	public Adiacenza(Album a1, Album a2, int peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Album getA1() {
		return a1;
	}
	public Album getA2() {
		return a2;
	}
	public int getPeso() {
		return peso;
	}
	
}
