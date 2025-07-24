package com.aurionpro.model;

public enum Wood {
	INDAIN_ROSEWOOD, BRAZILIAN_ROSEWOOD, MAHOGANY, MAPLE, COCOBOLO, CEDAR, ALDER, SITKA, ADIRONDECK;
	
	public String toString() {
		return name().charAt(0)+name().substring(1).toLowerCase();
	}
}
