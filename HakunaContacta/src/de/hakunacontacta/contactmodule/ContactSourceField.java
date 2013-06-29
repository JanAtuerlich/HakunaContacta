package de.hakunacontacta.contactModule;

import java.io.Serializable;

public class ContactSourceField implements Serializable{

	private String name;
	private String value;
	private int anzahl;

	public ContactSourceField() {
		
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
