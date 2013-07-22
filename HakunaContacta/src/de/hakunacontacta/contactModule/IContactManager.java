package de.hakunacontacta.contactModule;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.shared.ContactSourceType;

public interface IContactManager {

	/**
	 * Diese Methode verbindet sich mit OAuth2 mit dem Google-Konto und
	 * speichert initial alle Kontakte mit deren Elementen und Belegungen und
	 * Gruppen im Kontaktmanager ab
	 * 
	 * @param token wird benötigt, um den Nutzer zu identifizieren
	 */
	public void load(String token);

	public ArrayList<ContactGroup> getAllGroups();

	public ArrayList<Contact> getContacts(String groupName);

	public void selectGroup(String groupName);

	public void unselectGroup(String groupName);

	public void selectContact(String eTag);

	public void unselectContact(String eTag);

	/**
	 * Diese Methode liefert alle ContactSourceTypes der selektierten Kontakte
	 * 
	 * @return ContactSourceTypes der selektierten Kontakte
	 */
	public ArrayList<ContactSourceType> getSourceTypesOfSelectedContacts();

	/**
	 * Diese Methode liefert eine ArrayList aller Kontakte, die selektiert
	 * wurden
	 * 
	 * @return
	 */
	public ArrayList<Contact> getSelectedContacts();
}
