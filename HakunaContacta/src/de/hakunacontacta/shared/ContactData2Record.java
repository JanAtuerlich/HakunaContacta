package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

/**
 * Diese Klasse wird benötigt um Kontakte so abzuspeichern, dass sie für
 * Drag&Drop auf Page1 zur Verfügung stehen
 * 
 * @author MB
 * @category transportObject
 */
public class ContactData2Record {

	private ContactRecord[] records;

	/**
	 * Diese Methode erstellt aus den übergebenen Kontakten ein String Array vom
	 * Typ ContactRecord, das alle Parameter der Kontakte, sowie deren Gruppen
	 * abspeichert
	 * 
	 * @param contacts ist eine ArrayList von Kontakten, die zu ContactRecords umgewandelt werden soll
	 * @return gibt die Kontakte in Form von ContactRecords zurück
	 */
	public ContactRecord[] getNewRecords(ArrayList<Contact> contacts) {

		// ein Array mit Records wird angelegt, es hat die Länge der Anzahl der
		// vorhandenen Kontakte
		ContactRecord[] contactRecords = new ContactRecord[contacts.size()];

		// Die Kontakte werden durchlaufen und für jeden Kontakt wird ein
		// Kontakt-Record angelegt
		int i = 0;
		for (Contact contact : contacts) {

			String[] groups = new String[contact.getGroups().size()];
			int j = 0;

			// Die Gruppen des Kontakts werden ebenfalls als String-Array
			// abgespeichert
			for (ContactGroup contactGroup : contact.getGroups()) {
				groups[j] = contactGroup.getName();
				j++;
			}

			// Der Kontakt-Record wird angelegt
			contactRecords[i] = new ContactRecord(contact.geteTag(), contact.getName(), contact.getSelected(), groups);
			i++;
		}

		return contactRecords;

	}

	public ContactRecord[] getRecords() {
		return records;
	}
}
