package de.hakunacontacta.shared;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Die Klasse ContactSourceType hängt im Contacts und beschreibt die Art der Informationen wie z.B. Adresse, oder Telefonnummern
 * der in Form einer ArrayList im ContactSourceType enthaltenen Informationen
 * 
 */
@SuppressWarnings("serial")
public class ContactSourceType implements Serializable, Comparable<ContactSourceType> {

	private String type;

	private ArrayList<ContactSourceField> sourceFields = new ArrayList<ContactSourceField>();

	public ContactSourceType() {
	}

	
	@Override
	public int compareTo(ContactSourceType o) {
		return this.getType().compareTo(o.getType());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactSourceType other = (ContactSourceType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

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

	public void addSourceField(ContactSourceField sourceField) {
		sourceFields.add(sourceField);
	}
}
