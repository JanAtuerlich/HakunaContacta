package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

public class ContactGroupData2Record {

	private ContactGroupRecord[] records;

	public ContactGroupRecord[] getRecords() {
		return records;
	}

	public ContactGroupRecord[] getNewRecords(ArrayList<ContactGroup> contactGroups) {

		int arraySize = 0;
		for (ContactGroup contactGroup : contactGroups) {
			if (contactGroup.getContacts().size() > 0) {
				arraySize++;
			}
		}
		ContactGroupRecord[] contactGroupRecords = new ContactGroupRecord[arraySize];

		int arrayPosition = 0;
		for (ContactGroup contactGroup : contactGroups) {

			contactGroup.getContacts();
			int counter = contactGroup.getContacts().size();
			String[] contacts = new String[counter];
			int j = 0;
			for (Contact contact : contactGroup.getContacts()) {
				contacts[j] = contact.geteTag();
				j++;
			}
			if (counter > 0) {
				String displayedName = contactGroup.getName() + " (" + counter + ")";
				contactGroupRecords[arrayPosition] = new ContactGroupRecord(contactGroup.getName(), contactGroup.getSelected(), contacts, displayedName);
				arrayPosition++;
			}
		}

		return contactGroupRecords;

	}
}
