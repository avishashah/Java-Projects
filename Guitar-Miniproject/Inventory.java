package com.aurionpro.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
	private List<Guitar> guitars = new ArrayList<>();
	
	public void addGuitar(String serialNumber, double price, GuitarSpec spec) {
		guitars.add(new Guitar(serialNumber, price, spec));
	}
	
	public Guitar getGuitar(String serialNumber) {
		for(Guitar g: guitars) {
			if(g.getSerialNumber().equalsIgnoreCase(serialNumber))
				return g;
		}
		return null;
	}
	
	public List<Guitar> search(GuitarSpec searchSpec){
		List<Guitar> results = new ArrayList<Guitar>();
		for(Guitar g: guitars) {
			if(g.getSpec().matches(searchSpec))
				results.add(g);
		}
		return results;
	}
}
