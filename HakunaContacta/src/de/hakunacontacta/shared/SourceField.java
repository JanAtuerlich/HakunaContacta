package de.hakunacontacta.shared;

import java.io.Serializable;

public class SourceField implements Serializable{

	private String name;
	private String value;
	private int anzahl;

	public SourceField() {
		
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

}
