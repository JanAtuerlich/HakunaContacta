package de.hakunacontacta.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;



public class SourceType implements Serializable{

	private String type;
//	private int anzahl; brauch ma nicht
	private Collection<SourceField> sourceFields = new HashSet<SourceField>();
	
	
	public SourceType(){
	}


	//setter und getter
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
//	public int getAnzahl() { brauch ma net mehr...
//		return anzahl;
//	}
//	public void setAnzahl(int anzahl) {
//		this.anzahl = anzahl;
//	}
	public Collection<SourceField> getSourceFields() {
		return sourceFields;
	}
	public void setSourceFields(Collection<SourceField> sourceFields) {
		this.sourceFields = sourceFields;
	}

	//andere Methoden
	public void addSourceField(SourceField sourceField) {
		sourceFields.add(sourceField);
	}
	
}
