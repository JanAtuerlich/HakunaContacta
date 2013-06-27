package de.hakunacontacta.dummyContactManager;

import java.util.ArrayList;

public class SourceTypeDummy implements Comparable<SourceTypeDummy> {
	
	String type;
	ArrayList<SourceFieldDummy> sourceFields = new ArrayList<SourceFieldDummy>();
	
	public SourceTypeDummy(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<SourceFieldDummy> getSourceFields() {
		return sourceFields;
	}

	public void addSourceField(String name, String value) {
		SourceFieldDummy sourceField = new SourceFieldDummy(name, value);
		sourceFields.add(sourceField);
	}

	@Override
	public int compareTo(SourceTypeDummy o) {
		return this.getType().compareTo(o.getType());
	}	
}
