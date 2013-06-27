package de.hakunacontacta.dummyContactManager;

import java.util.ArrayList;

public class ContactDummy {
	
	String eTag;
	String name;
	ArrayList<SourceTypeDummy> sourceTypes = new ArrayList<SourceTypeDummy>();
	boolean selected;
	
	public ContactDummy(String eTag, String name){
		this.eTag = eTag;
		this.name = name;
		selected = false;
	}
	
	public String geteTag() {
		return eTag;
	}
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<SourceTypeDummy> getSourceTypes() {
		return sourceTypes;
	}
	public void addSourceType(String type) {
		SourceTypeDummy sourcetype = new SourceTypeDummy(type);
		sourceTypes.add(sourcetype);
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setSourceTypes(ArrayList<SourceTypeDummy> sourceTypes) {
		this.sourceTypes = sourceTypes;
	}

	public String toString(){
		String string = this.eTag + " " + this.name;
		for (SourceTypeDummy sourceType : this.getSourceTypes()) {
			for(SourceFieldDummy sourceField : sourceType.getSourceFields()){
				string += "\n \t" + sourceType.getType() + " " + sourceField.getName() + "= \t" + sourceField.getValue();
			}
		}
		string += "\n   ";
		return string;
	}
	
}
