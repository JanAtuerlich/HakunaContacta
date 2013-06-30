package de.hakunacontacta.contactModule;

import java.io.Serializable;
import java.util.ArrayList;

import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;





public class Contact implements Serializable{

	private String eTag;
	private String name;
	private boolean selected = false;
	private ArrayList<ContactSourceType> sourceTypes = new ArrayList<ContactSourceType>();
	private ArrayList<ContactGroup> groups = new ArrayList<ContactGroup>();
	

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



	public ArrayList<ContactSourceType> getSourceTypes() {
		return sourceTypes;
	}



	public void setSourceTypes(ArrayList<ContactSourceType> sourceTypes) {
		this.sourceTypes = sourceTypes;
	}



	public ArrayList<ContactGroup> getGroups() {
		return groups;
	}



	public void setGroups(ArrayList<ContactGroup> groups) {
		this.groups = groups;
	}


	//andere Methoden

	public void addSourceType(ContactSourceType type){
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
		for (ContactSourceType sourceType : this.getSourceTypes()) {
			for(ContactSourceField sourceField : sourceType.getSourceFields()){
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

