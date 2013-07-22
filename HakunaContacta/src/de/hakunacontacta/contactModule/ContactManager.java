package de.hakunacontacta.contactModule;

import com.google.gdata.client.contacts.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.ServiceException;

import de.hakunacontacta.shared.Constant;
import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Diese Klasse Verwaltet alle Kontakte und Kontaktgruppen, sowie deren Laden.
 * Sie verwendet OAuth2, um von Google alle Kontaktinformationen zu erhalten
 * 
 * @author MB
 * @category contactModule
 */
@SuppressWarnings("serial")
public class ContactManager implements IContactManager, Serializable {
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ArrayList<ContactGroup> contactGroups = new ArrayList<ContactGroup>();
	private final static Logger LOGGER = Logger.getLogger(ContactManager.class.getName());

	/**
	 * Der Konstruktor muss public sein und darf keine Übergabeparameter haben,
	 * da die Klasse serialisierbar ist
	 */
	public ContactManager() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hakunacontacta.contactModule.IContactManager#load(java.lang.String)
	 */
	public void load(String token) {

		LOGGER.fine("Running ContactManager");

		ContactsService myService = new ContactsService("<var>Hakuna Contacta</var>");

		myService.setHeader("Authorization", "Bearer " + token);

		try {
			// Diese Feed-URL ist notwendig um alle Gruppen aus der Google API
			// zu beziehen
			URL groupFeedUrl = new URL("https://www.google.com/m8/feeds/groups/default/full");
			// Dieser Feed beinhaltet alle Gruppen, die der User angelegt hat
			// und alle System-Gruppen
			ContactGroupFeed groupResultFeed = myService.getFeed(groupFeedUrl, ContactGroupFeed.class);

			// Über alle Gruppen wird iteriert
			for (ContactGroupEntry groupEntry : groupResultFeed.getEntries()) {
				String groupID = groupEntry.getId(); // Die Gruppen-ID wird
														// bezogen
				String name = groupEntry.getTitle().getPlainText(); // Der Name
																	// der
																	// Gruppe
																	// wird
																	// bezogen

				ContactGroup contactGroup = new ContactGroup(); // Eine neues
																// Gruppen-Objekt
																// wird erstellt
				contactGroup.setName(name);
				contactGroup.setGroupID(groupID);
				contactGroups.add(contactGroup); // Die Gruppe wird der
													// ArrayList aller Gruppen
													// hinzugefügt

			}

			// Diese Feed-URL ist notwendig um alle Kontakte aus der Google API
			// zu beziehen (maximal 10000)
			URL contactFeedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full?max-results=10000");
			// Dieser Feed beinhaltet alle Kontakte des Users
			ContactFeed contactResultFeed = myService.getFeed(contactFeedUrl, ContactFeed.class);

			// Über alle Kontakte wird iteriert
			for (ContactEntry entry : contactResultFeed.getEntries()) {
				// Der Kontakt wird nur beachtet, wenn er einen Namen hat
				if (entry.hasName()) {
					Name name = entry.getName();
					if (name.hasFullName()) {
						String fullname = name.getFullName().getValue(); // Der
																			// vollständige
																			// Name
																			// wird
																			// bezogen
						String eTag = entry.getId(); // Die ID (eindeutig) wird
														// bezogen
						Contact contact = new Contact(); // Ein neues
															// Kontakt-Objekt
															// wird angelegt
						contact.seteTag(eTag);
						contact.setName(fullname);
						// Die Gruppen des Kontakts werden durchlaufen und für
						// alle Einträge werden Referenzen auf die
						// Gruppen-Objekte
						// in der ArrayList angelegt, sowie in diesen Gruppen
						// Objekten Referenzen auf den Kontakt
						for (GroupMembershipInfo groupInfo : entry.getGroupMembershipInfos()) {
							for (ContactGroup group : contactGroups) {
								if (group.getGroupID().equals(groupInfo.getHref())) {
									group.addContact(contact);
									contact.addContactGroup(group);
									break;
								}
							}
						}

						// Es wird ein SourceType für jegliche Namen des
						// Kontakts angelegt
						ContactSourceType sourceTypeName = new ContactSourceType();
						sourceTypeName.setType("Name");
						ContactSourceField sourceFieldName = new ContactSourceField();
						// Der Vollständige Name ist auf jeden Fall vorhanden
						// und wird direkt angelegt
						sourceFieldName.setName("Vollständiger Name");
						sourceFieldName.setValue(fullname);
						sourceTypeName.addSourceField(sourceFieldName);
						// Falls er einen zweiten Vornamen besitzt, wird er
						// hinzugefügt
						if (name.hasAdditionalName()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Zweiter Vorname");
							sourceField.setValue(name.getAdditionalName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Nachnamen besitzt, wird er hinzugefügt
						if (name.hasFamilyName()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Nachname");
							sourceField.setValue(name.getFamilyName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Vornamen besitzt, wird er hinzugefügt
						if (name.hasGivenName()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Vorname");
							sourceField.setValue(name.getGivenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Prefix besitzt, wird er hinzugefügt
						if (name.hasNamePrefix()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Prefix");
							sourceField.setValue(name.getNamePrefix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Suffix besitzt, wird er hinzugefügt
						if (name.hasNameSuffix()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Suffix");
							sourceField.setValue(name.getNameSuffix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls ein Geschlecht eingetragen ist, wird es
						// hinzugefügt
						if (entry.hasGender()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Geschlecht");
							sourceField.setValue(entry.getGender().getValue().toString());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er Initialien besitzt, wird er hinzugefügt
						if (entry.hasInitials()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Initialen");
							sourceField.setValue(entry.getInitials().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Mädchennamen besitzt, wird er
						// hinzugefügt
						if (entry.hasMaidenName()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Mädchenname");
							sourceField.setValue(entry.getMaidenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Nickname besitzt, wird er hinzugefügt
						if (entry.hasNickname()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Nickname");
							sourceField.setValue(entry.getNickname().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						// Falls er einen Kurznamen besitzt, wird er hinzugefügt
						if (entry.hasShortName()) {
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Kurzname");
							sourceField.setValue(entry.getShortName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						contact.addSourceType(sourceTypeName);

						// Falls der Kontakt eine Firma oder einen Titel
						// eingetragen hat, wird dies hinzugefügt
						if (entry.hasOrganizations()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Titel und Unternehmen");
							for (Organization orga : entry.getOrganizations()) {
								ContactSourceField sourceFieldEnterprice = new ContactSourceField();
								sourceFieldEnterprice.setName("Unternehmen");
								sourceFieldEnterprice.setValue(orga.getOrgName().getValue());
								ContactSourceField sourceFieldTitle = new ContactSourceField();
								sourceFieldTitle.setName("Titel");
								sourceFieldTitle.setValue(orga.getOrgTitle().getValue());
								sourceType.addSourceField(sourceFieldEnterprice);
								sourceType.addSourceField(sourceFieldTitle);
							}
							contact.addSourceType(sourceType);
						}

						// Hier werden Mailadressen inklusive Bezeichnung
						// hinzugefügt, falls der Kontakt diese besitzt
						if (entry.hasEmailAddresses()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Email");
							for (Email email : entry.getEmailAddresses()) {
								String emailLabel = null;
								// Manchmal kommt es vor, dass die Bezeichnung
								// der Adresse nicht vorhanden ist, diese muss
								// dann über eine ID herausgefunden werden
								if (email.getLabel() != null)
									emailLabel = email.getLabel();
								else
									emailLabel = transformFieldName(email.getRel());
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(emailLabel);
								sourceField.setValue(email.getAddress());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Hier werden Telefonnummern inklusive Bezeichnung
						// hinzugefügt, falls der Kontakt diese besitzt
						if (entry.hasPhoneNumbers()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Telefon");
							for (PhoneNumber phoneNumber : entry.getPhoneNumbers()) {
								String phoneLabel = null;
								// Manchmal kommt es vor, dass die Bezeichnung
								// der Telefonnummer nicht vorhanden ist, diese
								// muss dann über eine ID herausgefunden werden
								if (phoneNumber.getLabel() != null)
									phoneLabel = phoneNumber.getLabel();
								else
									phoneLabel = transformFieldName(phoneNumber.getRel());
								// Aus einem unbekannten Grund wird die
								// Bezeichnung Google Voice von Google als
								// grandcentral gespeichert, dieser Fall wird
								// hiermit abgefangen
								if (phoneLabel.equals("grandcentral"))
									phoneLabel = "Google Voice";
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(phoneLabel);
								sourceField.setValue(phoneNumber.getPhoneNumber());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Hier werden Postadressen inklusive Bezeichnung
						// hinzugefügt, falls der Kontakt diese besitzt
						if (entry.hasPostalAddresses() || entry.hasStructuredPostalAddresses()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Adresse");
							// Es gibt in den Kontakten strukturierte und nicht
							// strukturierte Adressen
							for (PostalAddress postalAddress : entry.getPostalAddresses()) {
								String addressLabel = null;
								// Manchmal kommt es vor, dass die Bezeichnung
								// der Adresse nicht vorhanden ist, diese muss
								// dann über eine ID herausgefunden werden
								if (postalAddress.getLabel() != null)
									addressLabel = postalAddress.getLabel();
								else
									addressLabel = transformFieldName(postalAddress.getRel());
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(addressLabel);
								sourceField.setValue(postalAddress.getValue());
								sourceType.addSourceField(sourceField);
							}
							// Es gibt in den Kontakten strukturierte und nicht
							// strukturierte Adressen
							for (StructuredPostalAddress postalAddress : entry.getStructuredPostalAddresses()) {
								String addressLabel = null;
								// Manchmal kommt es vor, dass die Bezeichnung
								// der Adresse nicht vorhanden ist, diese muss
								// dann über eine ID herausgefunden werden
								if (postalAddress.getLabel() != null)
									addressLabel = postalAddress.getLabel();
								else
									addressLabel = transformFieldName(postalAddress.getRel());
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(addressLabel);
								sourceField.setValue(postalAddress.getFormattedAddress().getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Falls im Kontakt ein Geburtstag hinterlegt ist, wird
						// dieser ausgelesen
						if (entry.hasBirthday()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Geburtstag");
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Geburtstag");
							sourceField.setValue(entry.getBirthday().getValue());
							sourceType.addSourceField(sourceField);
							contact.addSourceType(sourceType);
						}

						// Falls im Kotakt Webseiten hintelegt sind, werden
						// diese ausgelesen
						if (entry.hasWebsites()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("URL");
							for (Website website : entry.getWebsites()) {
								String websiteLabel = null;
								// Manchmal kommt es vor, dass die Bezeichnung
								// der Webseite nicht vorhanden ist, diese muss
								// dann über eine ID herausgefunden werden
								if (website.getLabel() != null)
									websiteLabel = website.getLabel();
								else
									websiteLabel = transformFieldName(website.getRel().toValue());
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(websiteLabel);
								sourceField.setValue(website.getHref());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Falls der Kontakt Instant Messaging Adressen
						// hinterlegt hat, werden diese bezogen
						if (entry.hasImAddresses()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Instant Messaging");
							for (Im im : entry.getImAddresses()) {
								String imLabel = null;
								// Aus unbekannter Ursache speichert Google die
								// Bezeichnung manchmal als Klartext und
								// manchmal als ID im Feld
								// Da alle ID's gleich beginnen, können diese
								// hier aussortiert und gesondert behandelt
								// werden
								if (!im.getProtocol().startsWith("http://schemas.google.com/g/2005#"))
									imLabel = im.getProtocol();
								else
									imLabel = transformFieldName(im.getProtocol());
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(imLabel);
								sourceField.setValue(im.getAddress());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Falls der Kontakt benutzerdefinierte Felder besitzt,
						// werden diese hier ausgelesen
						if (entry.hasUserDefinedFields()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Benuzerdefiniertes Feld");
							for (UserDefinedField field : entry.getUserDefinedFields()) {
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(field.getKey());
								sourceField.setValue(field.getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}

						// Ich konnte leider nicht herausfinden, wozu dieses
						// Feld dient, es wird aber ebenfalls ausgelesen um alle
						// Fälle abzudecken
						if (entry.hasExtendedProperties()) {
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Extended Property");
							for (ExtendedProperty property : entry.getExtendedProperties()) {
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(property.getName());
								sourceField.setValue(property.getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
						contacts.add(contact);
					}
				}
			}

		} catch (ServiceException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Einige Feldbezeichner werden von Google nicht als Klartext, sondern als
	 * ID abgespeichert. Diese ID's sind alle in der Constant Klasse hinterlegt
	 * und können hier überpfüft und ersetzt werden
	 * 
	 * @param googleCode
	 *            ist die Google-ID eines Feldbezeichners
	 * @return Klartext für einen Feldbezeichner, der bisher nur eine Google-ID
	 *         hatte
	 */
	private String transformFieldName(String googleCode) {
		switch (googleCode) {
		case Constant.GOOGLE_CONTACT_IS_HOME:
			return "Privat";
		case Constant.GOOGLE_CONTACT_IS_HOME_FAX:
			return "Fax (privat)";
		case Constant.GOOGLE_CONTACT_IS_MAIN:
			return "Festnetz";
		case Constant.GOOGLE_CONTACT_IS_MOBILE:
			return "Mobil";
		case Constant.GOOGLE_CONTACT_IS_PAGER:
			return "Pager";
		case Constant.GOOGLE_CONTACT_IS_WORK:
			return "Geschäftlich";
		case Constant.GOOGLE_CONTACT_IS_WORK_FAX:
			return "Fax (geschäftl.)";
		case Constant.GOOGLE_WEBSITE_IS_BLOG:
			return "Blog";
		case Constant.GOOGLE_WEBSITE_IS_HOME_PAGE:
			return "Homepage";
		case Constant.GOOGLE_WEBSITE_IS_PROFILE:
			return "Profil";
		case Constant.GOOGLE_WEBSITE_IS_WORK:
			return "Geschäftlich";
		case Constant.GOOGLE_IM_IS_AIM:
			return "AIM";
		case Constant.GOOGLE_IM_IS_GOOGLE_TALK:
			return "Google Talk";
		case Constant.GOOGLE_IM_IS_ICQ:
			return "ICQ";
		case Constant.GOOGLE_IM_IS_JABBER:
			return "Jabber";
		case Constant.GOOGLE_IM_IS_MSN:
			return "MSN";
		case Constant.GOOGLE_IM_IS_QQ:
			return "QQ";
		case Constant.GOOGLE_IM_IS_SKYPE:
			return "Skype";
		case Constant.GOOGLE_IM_IS_YAHOO:
			return "Yahoo!";
		default:
			return "Unbekannt";
		}
	}

	// Getter für die Kontakte
	public ArrayList<Contact> getContacts() {
		return contacts;

	}

	// Getter für die Gruppen
	public ArrayList<ContactGroup> getGroups() {
		return contactGroups;

	}

	@Override
	// Wird evtl nicht mehr benötigt
	public ArrayList<ContactGroup> getAllGroups() {

		return contactGroups;
	}

	@Override
	// Wird evtl nicht mehr benötigt
	public ArrayList<Contact> getContacts(String groupName) {
		ArrayList<Contact> contactsFromGroups = new ArrayList<Contact>();
		if (groupName != "all") {
			for (ContactGroup contactGroup : contactGroups) {
				if (contactGroup.getName() == groupName) {
					contactsFromGroups = contactGroup.getContacts();

				}
			}
			return contactsFromGroups;
		} else {
			return contacts;
		}

	}

	@Override
	// Wird evtl nicht mehr benötigt
	public void selectGroup(String groupName) {
		for (ContactGroup contactGroup : contactGroups) {
			if (contactGroup.getName() == groupName) {
				contactGroup.selectChkContacts();
			}
		}

	}

	@Override
	// Wird evtl nicht mehr benötigt
	public void unselectGroup(String groupName) {
		for (ContactGroup contactGroup : contactGroups) {
			if (contactGroup.getName() == groupName) {
				contactGroup.unselectChkContacts();
			}
		}
	}

	@Override
	// Wird evtl nicht mehr benötigt
	public void selectContact(String eTag) {
		for (Contact contact : contacts) {
			if (contact.geteTag() == eTag) {
				contact.selectChkGroups();
			}
		}
	}

	@Override
	// Wird evtl nicht mehr benötigt
	public void unselectContact(String eTag) {
		for (Contact contact : contacts) {
			if (contact.geteTag() == eTag) {
				contact.unselectChkGroups();
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.contactModule.IContactManager#getSourceTypesOfSelectedContacts()
	 */
	public ArrayList<ContactSourceType> getSourceTypesOfSelectedContacts() {
		ArrayList<ContactSourceType> exportSourceTypes = new ArrayList<ContactSourceType>();

		for (Contact contact : contacts) {
			if (contact.getSelected() == true) {
				for (ContactSourceType contactSourceType : contact.getSourceTypes()) {
					if (!exportSourceTypes.contains(contactSourceType)) {
						ContactSourceType exportSourceType = new ContactSourceType();
						exportSourceType.setType(contactSourceType.getType());
						exportSourceTypes.add(exportSourceType);
					}
				}
				for (ContactSourceType exportSourceType : exportSourceTypes) {
					for (ContactSourceType contactSourceType : contact.getSourceTypes()) {
						if (exportSourceType.getType() == contactSourceType.getType()) {
							for (ContactSourceField contactSourceField : contactSourceType.getSourceFields()) {
								if (!exportSourceType.getSourceFields().contains(contactSourceField)) {
									ContactSourceField exportSourceField = new ContactSourceField();
									exportSourceField.setName(contactSourceField.getName());
									exportSourceField.setAnzahl(1);
									exportSourceType.addSourceField(exportSourceField);
								} else {
									for (ContactSourceField exportSourceField : exportSourceType.getSourceFields()) {
										if (exportSourceField.getName() == contactSourceField.getName()) {
											exportSourceField.setAnzahl(exportSourceField.getAnzahl() + 1);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return exportSourceTypes;
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.contactModule.IContactManager#getSelectedContacts()
	 */
	public ArrayList<Contact> getSelectedContacts() {
		ArrayList<Contact> contacsSelected = new ArrayList<Contact>();
		for (Contact contact : contacts) {
			if (contact.getSelected() == true) {
				contacsSelected.add(contact);
			}
		}
		return contacsSelected;
	}


	/**
	 * 	Diese Methode ist ein Setter um die Selektierungen von Page1 zu übernehmen
	 * @param contacts schreibt die aktuellen Kontakte in den Kontaktmanager auf dem Server zurück 
	 * @param contactGroups schreibt die aktuellen Kontaktgruppen in den Kontaktmanager auf dem Server zurück
	 */
	public void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups) {
		this.contacts = contacts;
		this.contactGroups = contactGroups;
	}

}