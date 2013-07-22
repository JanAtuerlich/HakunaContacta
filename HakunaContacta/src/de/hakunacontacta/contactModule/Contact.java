package de.hakunacontacta.contactModule;

import java.io.Serializable;
import java.util.ArrayList;

import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;

/**
 * Contact wird vom ContactManager verwaltet und kann einen Kontakt abspeichern
 * 
 * @author MB
 * @category contactModule
 * @version 0.1beta
 */

public class Contact implements Serializable {

	private String eTag;
	private String name;
	private boolean selected = false;
	private ArrayList<ContactSourceType> sourceTypes = new ArrayList<ContactSourceType>();
	private ArrayList<ContactGroup> groups = new ArrayList<ContactGroup>();

	/**
	 * Der Konstruktor sollte public sein und keine Parameter erwarten, da
	 * Contact serialisierbar ist
	 */
	public Contact() {
	}

	/**
	 * Diese Methode f�gt der ArrayList von ContactSourceTypes einen Eintrag
	 * hinzu
	 * 
	 * @param type
	 *            ist ein Feld Ihres Kontaktes, z.B. Nachname, Vorname oder
	 *            E-Mail-Adresse
	 */
	public void addSourceType(ContactSourceType type) {
		sourceTypes.add(type);
	}

	/**
	 * Diese Methode f�gt allen Gruppen, die im Contact-Objekt �ber die Methode
	 * setGroups(ArrayList<ContactGroup> groups) gespeichert wurden, dieses
	 * Contact-Objekt hinzu
	 */
	public void addYourselfToContactGroups() {
		for (ContactGroup contactGroup : groups) {
			contactGroup.addContact(this);
		}
	}

	/**
	 * Diese Methode pr�ft f�r alle Gruppen dieses Kontaktes ob alle Kontakte
	 * der Gruppen dieses Kontaktes selektiert sind, dabei werden alle Gruppen,
	 * deren Kontakte alle selektiert sind auf "selektiert" gesetzt
	 */
	public void selectChkGroups() {
		for (ContactGroup contactGroup : groups) {
			contactGroup.checkGroupForSelection();
		}

	}

	/**
	 * Diese Methode setzt alle Gruppen dieses Kontaktes auf "nicht selektiert"
	 */
	public void unselectChkGroups() {
		for (ContactGroup contactGroup : groups) {
			contactGroup.setSelected(false);
		}
	}

	/**
	 * Diese Methode f�gt dem Kontakt eine Gruppe hinzu, die Gruppe kennt
	 * dadurch noch nicht den Kontakt
	 * 
	 * @param group
	 *            ist eine einzelne ContactGroup, die einem Contact-Objekt
	 *            hinzugef�gt wird, die Gruppe kennt dadurch noch nicht den
	 *            Kontakt
	 */
	public void addContactGroup(ContactGroup group) {
		this.groups.add(group);

	}

	/**
	 * Gibt den eindeutigen Identifikator des Kontaktes zur�ck (eTag)
	 * 
	 * @return Gibt den eindeutigen Identifikator des Kontaktes zur�ck (eTag)
	 */
	public String geteTag() {
		return eTag;
	}

	/**
	 * Setzt den eindeutigen Identifikator des Kontaktes zur�ck (eTag)
	 * 
	 * @param eTag
	 */
	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	/**
	 * Gibt den Namen des Kontaktes zur�ck
	 * 
	 * @return Gibt den Namen des Kontaktes zur�ck
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen des Kontaktes
	 * 
	 * @param name
	 *            ist ein String, der der vollst�ndige Name eines Kontaktes ist
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gibt einen boolean zur�ck, der aussagt ob der Kontakt selektiert ist oder
	 * nicht
	 * 
	 * @return Gibt einen boolean zur�ck, der aussagt ob der Kontakt selektiert
	 *         ist oder nicht
	 */
	public boolean getSelected() {
		return selected;
	}

	/**
	 * Setzt einen boolean, der aussagt ob der Kontakt selektiert ist oder nicht
	 * 
	 * @param selected
	 *            ist ein boolean, true hei�t der Kontakt ist selektiert, false
	 *            bedeutet der Kontakt ist nicht selektiert
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Gibt alle ContactSourceTypes des Kontaktes in Form einer ArrayList
	 * zur�ck, diese k�nnten z.B. den type "Nachname", "Vorname" oder
	 * "E-Mail-Adresse" haben
	 * 
	 * @return Gibt alle ContactSourceTypes des Kontaktes in Form einer
	 *         ArrayList zur�ck, diese k�nnten z.B. den type "Nachname",
	 *         "Vorname" oder "E-Mail-Adresse" haben
	 */
	public ArrayList<ContactSourceType> getSourceTypes() {
		return sourceTypes;
	}

	/**
	 * �berschreibt sofort alle SourceTypes des Kontaktes mit einer neuen
	 * ArrayList von ContactSourceTypes, diese k�nnten z.B. den type "Nachname",
	 * "Vorname" oder "E-Mail-Adresse" haben
	 * 
	 * @param sourceTypes
	 */
	public void setSourceTypes(ArrayList<ContactSourceType> sourceTypes) {
		this.sourceTypes = sourceTypes;
	}

	/**
	 * Gibt alle dem Kontakt bekannten Gruppen in Form einer ArrayList zur�ck
	 * 
	 * @return Gibt alle dem Kontakt bekannten Gruppen in Form einer ArrayList
	 *         zur�ck
	 */
	public ArrayList<ContactGroup> getGroups() {
		return groups;
	}

	/**
	 * �berschreibt sofort alle Gruppen des Kontaktes mit einer neuen ArrayList
	 * von ContactGroups
	 * 
	 * @param groups
	 *            ist eine ArrayList von ContactGroups, der Kontakt kennt nur
	 *            noch diese Gruppen und vergisst alle die er vorher kannte, das
	 *            hat noch keinen Effekt bei allen betroffenen Gruppen
	 */
	public void setGroups(ArrayList<ContactGroup> groups) {
		this.groups = groups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = this.eTag + " " + this.name;
		for (ContactSourceType sourceType : this.getSourceTypes()) {
			for (ContactSourceField sourceField : sourceType.getSourceFields()) {
				string += "\n \t" + sourceType.getType() + " " + sourceField.getName() + "= \t" + sourceField.getValue();
			}
		}
		string += "\n   ";
		return string;
	}

}
