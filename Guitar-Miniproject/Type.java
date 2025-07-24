package com.aurionpro.model;

public enum Type {
	ACOUSTIC, ELECTRIC;
	
	public String toString() {
		switch(this) {
		case ACOUSTIC: return "Acoustic guitar";
		case ELECTRIC: 	return "Electric guitar";
		default: return "Unknown type";
		}
	}
	
}
