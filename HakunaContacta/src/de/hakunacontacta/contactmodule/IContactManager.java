package de.hakunacontacta.contactModule;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

public interface IContactManager {

	public void	load(String token);
	public ArrayList<ContactGroup> getAllGroups();
	public ArrayList<Contact>getContacts(String groupName);
	public void selectGroup(String groupName);
	public void unselectGroup(String groupName);
	public void selectContact(String eTag);
	public void unselectContact(String eTag);
	public ArrayList<ContactSourceType> getSourceTypesOfSelectedContacts();
	public ArrayList<Contact> getSelectedContacts();
}
