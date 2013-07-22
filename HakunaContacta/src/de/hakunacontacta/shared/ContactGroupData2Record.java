package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

/**
 * Diese Klasse wird benötigt um Kontaktgruppen so abzuspeichern, dass sie für
 * Drag&Drop auf Page1 zur Verfügung stehen
 * 
 * @author MB
 * @category transportObject
 */
public class ContactGroupData2Record {

	private ContactGroupRecord[] records;

	/**
	 * Diese Methode erstellt aus den übergebenen Kontaktgruppen ein String
	 * Array vom Typ ContactGroupRecord, das alle Parameter der Gruppen und
	 * deren enthaltenen Kontakten abspeichert. Außerdem wird ein zusätzliches
	 * Feld für die Anzeige der Gruppen hinzugefügt, das die Anzahl der
	 * enthaltenen Kontakte in Klammern enthält
	 * 
	 * @param contactGroups
	 *            ist eine ArrayList von Gruppen, die zu ContactGroupRecords
	 *            umgewandelt werden soll
	 * @return gibt die Gruppen in Form von ContactGroupRecords zurück, die nun
	 *         auch ein zusätzliches Feld "displayName" enthalten
	 */
	public ContactGroupRecord[] getNewRecords(ArrayList<ContactGroup> contactGroups) {

		// Nur Gruppen, in denen Kontakte vorhanden sind, sollen als Record
		// angelegt werden
		int arraySize = 0;
		for (ContactGroup contactGroup : contactGroups) {
			// Nur wenn sich Kontakte in der Gruppe befinden, wird der Zähler
			// hochgezählt
			if (contactGroup.getContacts().size() > 0) {
				arraySize++;
			}
		}

		// Ein Array mit Gruppen Records wird angelegt, die Länge entspricht der
		// Anzahl Gruppen, in denen Kontakte enthalten sind
		ContactGroupRecord[] contactGroupRecords = new ContactGroupRecord[arraySize];

		int arrayPosition = 0;
		for (ContactGroup contactGroup : contactGroups) {
			// Es wird für jede Gruppe ein Array mit den ID's aller enthaltenen
			// Kontakte angelegt
			contactGroup.getContacts();
			int counter = contactGroup.getContacts().size();
			String[] contacts = new String[counter];
			int j = 0;
			for (Contact contact : contactGroup.getContacts()) {
				contacts[j] = contact.geteTag();
				j++;
			}
			// Falls Kontakte in dieser Gruppe enhalten sind, wird ein Group
			// Record für diese Gruppe erstellt
			if (counter > 0) {
				// Es wird ein extra Feld für den Anzeigename angelegt, der
				// zusätzlich die Anzahl enthaltener Kontakte beinhaltet
				String displayedName = contactGroup.getName() + " (" + counter + ")";
				contactGroupRecords[arrayPosition] = new ContactGroupRecord(contactGroup.getName(), contactGroup.getSelected(), contacts, displayedName);
				arrayPosition++;
			}
		}
		return contactGroupRecords;
	}

	public ContactGroupRecord[] getRecords() {
		return records;
	}
}