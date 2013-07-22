package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

/**
 * Diese Klasse wird ben�tigt um Kontaktgruppen so abzuspeichern, dass sie f�r
 * Drag&Drop auf Page1 zur Verf�gung stehen
 * 
 * @author MB
 * @category transportObject
 */
public class ContactGroupData2Record {

	private ContactGroupRecord[] records;

	/**
	 * Diese Methode erstellt aus den �bergebenen Kontaktgruppen ein String
	 * Array vom Typ ContactGroupRecord, das alle Parameter der Gruppen und
	 * deren enthaltenen Kontakten abspeichert. Au�erdem wird ein zus�tzliches
	 * Feld f�r die Anzeige der Gruppen hinzugef�gt, das die Anzahl der
	 * enthaltenen Kontakte in Klammern enth�lt
	 * 
	 * @param contactGroups
	 *            ist eine ArrayList von Gruppen, die zu ContactGroupRecords
	 *            umgewandelt werden soll
	 * @return gibt die Gruppen in Form von ContactGroupRecords zur�ck, die nun
	 *         auch ein zus�tzliches Feld "displayName" enthalten
	 */
	public ContactGroupRecord[] getNewRecords(ArrayList<ContactGroup> contactGroups) {

		// Nur Gruppen, in denen Kontakte vorhanden sind, sollen als Record
		// angelegt werden
		int arraySize = 0;
		for (ContactGroup contactGroup : contactGroups) {
			// Nur wenn sich Kontakte in der Gruppe befinden, wird der Z�hler
			// hochgez�hlt
			if (contactGroup.getContacts().size() > 0) {
				arraySize++;
			}
		}

		// Ein Array mit Gruppen Records wird angelegt, die L�nge entspricht der
		// Anzahl Gruppen, in denen Kontakte enthalten sind
		ContactGroupRecord[] contactGroupRecords = new ContactGroupRecord[arraySize];

		int arrayPosition = 0;
		for (ContactGroup contactGroup : contactGroups) {
			// Es wird f�r jede Gruppe ein Array mit den ID's aller enthaltenen
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
			// Record f�r diese Gruppe erstellt
			if (counter > 0) {
				// Es wird ein extra Feld f�r den Anzeigename angelegt, der
				// zus�tzlich die Anzahl enthaltener Kontakte beinhaltet
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