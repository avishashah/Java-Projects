package com.aurionpro.model;

public enum Builder {
	FENDER, MARTIN, GIBSON, COLLINGS, OLSON, RYAN, PRS, ANY;
	
	public String toString() {
		switch(this) {
		case FENDER: return "Fender Guitar";
		case MARTIN: return "Martin Guitars";
        case GIBSON: return "Gibson Guitars";
        case COLLINGS: return "Collings Guitars";
        case OLSON: return "Olson Custom";
        case RYAN: return "Ryan Handmade";
        case PRS: return "PRS (Paul Reed Smith)";
        case ANY: return "Any Builder";
        default: return "Unknown Builder";
		}
	}
}
