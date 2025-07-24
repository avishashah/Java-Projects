package com.aurionpro.model;

public class Guitar {
	private String serailNumber;
	private double price;
	private GuitarSpec spec;
	
	public Guitar(String serailNumber, double price, GuitarSpec spec) {
		this.serailNumber = serailNumber;
		this.price = price;
		this.spec = spec;
	}
	
	public String getSerialNumber() {
		return serailNumber;
	}
	
	public double getPrice() {
		return price;
	}
	
	public double setPrice(double price) {
		return this.price=price;
	}
	
	public GuitarSpec getSpec() {
		return spec;
	}

	@Override
	public String toString() {
		return "\n------------------\nSerailNumber: " + serailNumber + "\nPrice: " + price + spec;
	}
	
	
}
