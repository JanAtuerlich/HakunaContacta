package de.hakunacontacta.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;



public class Contact implements Serializable{

	private String eTag;
	private String name;
	private boolean selected;
	private Collection<SourceType> sourceTypes = new HashSet<SourceType>();
	private Collection<ContactGroup> groups = new HashSet<ContactGroup>();
	

	public Contact(){
	}

	
	//getter und setter

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



	public boolean getSelected() {
		return selected;
	}



	public void setSelected(boolean selected) {
		this.selected = selected;
	}



	public Collection<SourceType> getSourceTypes() {
		return sourceTypes;
	}



	public void setSourceTypes(Collection<SourceType> sourceTypes) {
		this.sourceTypes = sourceTypes;
	}



	public Collection<ContactGroup> getGroups() {
		return groups;
	}



	public void setGroups(Collection<ContactGroup> groups) {
		this.groups = groups;
	}


	//andere Methoden

	public void addSourceType(SourceType type){
			sourceTypes.add(type);
	}

	public void addYourselfToContactGroups(){
		for(ContactGroup contactGroup : groups) {
			contactGroup.addContact(this);
		}
	}

	public void selectChkGroups(){
		for(ContactGroup contactGroup : groups) {
			contactGroup.checkGroupForSelection();
		}
		
	}
	public void unselectChkGroups(){
		for(ContactGroup contactGroup : groups) {
			contactGroup.setSelected(false);
		}
	}


	@Override
	public String toString() {
		String string = this.eTag + " " + this.name;
		for (SourceType sourceType : this.getSourceTypes()) {
			for(SourceField sourceField : sourceType.getSourceFields()){
				string += "\n \t" + sourceType.getType() + " " + sourceField.getName() + "= \t" + sourceField.getValue();
			}
		}
		string += "\n   ";
		return string;
	}


	public void addContactGroup(ContactGroup group) {
		this.groups.add(group);
		
	}
	
	
	//public boolean checkGroups(String markesGroupName){ }

}
