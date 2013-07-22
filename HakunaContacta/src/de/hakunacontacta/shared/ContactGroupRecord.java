package de.hakunacontacta.shared;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Diese Klasse dient zum Abspeichern von Kontaktgruppen und kann in Drag&Drop
 * von GWT eingesetzt werden
 * 
 * @author MB
 * @category transportObject
 */
public class ContactGroupRecord extends ListGridRecord {

	public ContactGroupRecord() {
	}

	/**
	 * Der Kontstrukter ruft für jedes übergebene Attribut den jeweiligen Setter
	 * auf
	 * 
	 * @param groupname
	 *            speichert den Gruppen-Namen ab
	 * @param selected
	 *            speichert ab ob die Gruppe selektiert ist
	 * @param contacts
	 *            speichert alle Kontakte ab, die die Gruppe kennt
	 * @param displayedName
	 *            ist ein neues Attribut, das für die Anzeige auf Page1
	 *            verwendet werden soll
	 */
	public ContactGroupRecord(String groupname, boolean selected, String[] contacts, String displayedName) {
		setGroupname(groupname);
		setContacts(contacts);
		setSelected(selected);
		setDisplayedName(displayedName);
	}

	public void setSelected(boolean selected) {
		setAttribute("selected", selected);
	}

	public boolean getSelected() {
		return getAttributeAsBoolean("selected");
	}

	public void setGroupname(String groupname) {
		setAttribute("groupname", groupname);
	}

	public String getGroupname() {
		return getAttributeAsString("groupname");
	}

	public void setContacts(String[] contacts) {
		setAttribute("contacts", contacts);
	}

	public String[] getContacts() {
		return getAttributeAsStringArray("contacts");
	}

	public String getDisplayedName() {
		return getAttributeAsString("displayedName");
	}

	public void setDisplayedName(String displayedName) {
		setAttribute("displayedName", displayedName);
	}
}
