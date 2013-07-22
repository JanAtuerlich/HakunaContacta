package de.hakunacontacta.shared;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * Diese Klasse dient zum Abspeichern von Kontakten und kann in Drag&Drop von
 * GWT eingesetzt werden
 * 
 * @author MB
 * @category transportObject
 * 
 */
public class ContactRecord extends ListGridRecord {

	public ContactRecord() {
	}

	/**
	 * Der Kontstrukter ruft für jedes übergebene Attribut den jeweiligen Setter
	 * auf
	 * 
	 * @param etag
	 *            speichert den eindeutigen Identifikator des Kontaktes ab
	 * @param name
	 *            speichert den Namen des Kontaktes ab
	 * @param selected
	 *            speichert ob der Kontakt selektiert ist
	 * @param groups
	 *            speichert alle Gruppen ab, die der Kontakt kennt
	 */
	public ContactRecord(String etag, String name, boolean selected, String[] groups) {
		setEtag(etag);
		setName(name);
		setSelected(selected);
		setGroups(groups);
	}

	public void setSelected(boolean selected) {
		setAttribute("selected", selected);
	}

	public boolean getSelected() {
		return getAttributeAsBoolean("selected");
	}

	public void setEtag(String etag) {
		setAttribute("etag", etag);
	}

	public String getEtag() {
		return getAttributeAsString("etag");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getName() {
		return getAttributeAsString("name");
	}

	public void setGroups(String[] groups) {
		setAttribute("groups", groups);
	}

	public String[] getGroups() {
		return getAttributeAsStringArray("groups");
	}

}
