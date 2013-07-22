package de.hakunacontacta.contactModule;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Contact wird vom ContactManager verwaltet und kann sich viele Kontakte
 * abspeichern
 * 
 * @author MB
 * @category contactModule
 * 
 */
public class ContactGroup implements Serializable {

	private String name;
	private String groupID;
	private int anzahl;
	private boolean selected;
	private ArrayList<Contact> contacts = new ArrayList<Contact>();

	/**
	 * Der Konstruktor sollte public sein und keine Parameter erwarten, da
	 * ContactGroup serialisierbar ist
	 */
	public ContactGroup() {
	}

	/**
	 * Diese Methode fügt dieser Gruppe einen Kontakt hinzu, der Kontakt ist
	 * davon nicht direkt betroffen
	 * 
	 * @param contact
	 *            sollte erst hinzugefügt werden, wenn er vollständig
	 *            parametriert ist
	 */
	public void addContact(Contact contact) {
		contacts.add(contact);
	}

	/**
	 * Diese Methode setzt jeden Kontakt, der diese Gruppe kennt auf selektiert
	 */
	public void selectChkContacts() {

		for (Contact contact : contacts) {
			boolean contactSelecting = false;
			for (ContactGroup group : contact.getGroups()) {
				if (group.getSelected() == true) {
					contactSelecting = true;
				}
			}
			if (contactSelecting == false) {
				contact.setSelected(false);
			}
		}

	}

	/**
	 * Diese Methode setzt alle Kontakte dieser Gruppe auf "nicht selektiert"
	 */
	public void unselectChkContacts() {
		for (Contact contact : contacts) {
			contact.setSelected(false);
		}

	}

	/**
	 * Diese Methode prüft alle Kontakte der Gruppe, nur wenn alle selektiert
	 * sind wird die Gruppe selektiert
	 */
	public void checkGroupForSelection() {
		boolean groupSelected = true;
		for (Contact contact : contacts) {
			if (contact.getSelected() == false) {
				groupSelected = false;
			}
		}
		if (groupSelected == true) {
			this.setSelected(true);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ArrayList<Contact> contacts) {
		this.contacts = contacts;
	}

}
