package de.hakunacontacta.shared;

import java.io.Serializable;
import java.util.ArrayList;



public class ContactSourceType implements Serializable, Comparable<ContactSourceType>{

	private String type;

	private ArrayList<ContactSourceField> sourceFields = new ArrayList<ContactSourceField>();
	public ContactSourceType(){
	}


	//setter und getter
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<ContactSourceField> getSourceFields() {
		return sourceFields;
	}
	public void setSourceFields(ArrayList<ContactSourceField> sourceFields) {
		this.sourceFields = sourceFields;
	}

	//andere Methoden
	public void addSourceField(ContactSourceField sourceField) {
		sourceFields.add(sourceField);
	}
	@Override
	public int compareTo(ContactSourceType o) {
		return this.getType().compareTo(o.getType());
	}	
	
}
