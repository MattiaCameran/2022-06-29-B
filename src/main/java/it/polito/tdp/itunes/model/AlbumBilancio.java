package it.polito.tdp.itunes.model;

import java.util.Objects;

public class AlbumBilancio implements Comparable<AlbumBilancio>{
	
	Album a;
	double bilancio;
	public AlbumBilancio(Album a, double bilancio) {
		super();
		this.a = a;
		this.bilancio = bilancio;
	}
	public Album getA() {
		return a;
	}
	public void setA(Album a) {
		this.a = a;
	}
	public double getBilancio() {
		return bilancio;
	}
	public void setBilancio(double bilancio) {
		this.bilancio = bilancio;
	}
	@Override
	public int hashCode() {
		return Objects.hash(a);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlbumBilancio other = (AlbumBilancio) obj;
		return Objects.equals(a, other.a);
	}
	@Override
	public String toString() {
		return a.getTitle() + " bilancio= " + this.getBilancio();
	}
	@Override
	public int compareTo(AlbumBilancio o) {
		
		return (int)-(this.bilancio - o.getBilancio());
	}

}
