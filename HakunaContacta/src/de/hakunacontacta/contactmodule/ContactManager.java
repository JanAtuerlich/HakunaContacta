package de.hakunacontacta.contactmodule;

import com.google.gdata.client.contacts.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.ServiceException;

import de.hakunacontacta.shared.Constant;
import de.hakunacontacta.shared.Contact;
import de.hakunacontacta.shared.ContactGroup;
import de.hakunacontacta.shared.SourceField;
import de.hakunacontacta.shared.SourceType;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;

public class ContactManager {
	private Collection<Contact> contacts = new HashSet<Contact>();
	private Collection<ContactGroup> contactGroups = new HashSet<ContactGroup>();
	private final static Logger LOGGER = Logger.getLogger(ContactManager.class
			.getName());
	
	private boolean fertig = false;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void load(String token){

		LOGGER.fine("Running ContactManager");
		
		ContactsService myService = new ContactsService(
				"<var>Hakuna Contacta</var>");

		myService.setHeader("Authorization",
				"Bearer " + token);
		
		
		try {
			// Gruppen anfordern
			// Request the feed
			URL groupFeedUrl = new URL(
					"https://www.google.com/m8/feeds/groups/default/full");
			ContactGroupFeed groupResultFeed = myService.getFeed(groupFeedUrl,
					ContactGroupFeed.class);

			for (ContactGroupEntry groupEntry : groupResultFeed.getEntries()) {
				
				String groupID = groupEntry.getId();
				String name = groupEntry.getTitle().getPlainText();
				
				ContactGroup contactGroup = new ContactGroup();
				contactGroup.setName(name);
				contactGroup.setGroupID(groupID);
				contactGroups.add(contactGroup);
				
			}
			
			// Kontakte anfordern
			URL contactFeedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full?max-results=10000");
			ContactFeed contactResultFeed = myService.getFeed(contactFeedUrl, ContactFeed.class);
			
			for (ContactEntry entry : contactResultFeed.getEntries()) {
				if (entry.hasName()){
					Name name = entry.getName();
					if (name.hasFullName()){
						String fullname = name.getFullName().getValue();
						String eTag = entry.getEtag();
						Contact contact = new Contact();
						contact.seteTag(eTag);
						contact.setName(fullname);
						for (GroupMembershipInfo groupInfo : entry.getGroupMembershipInfos()){
							for (ContactGroup group: contactGroups){
								if (group.getGroupID().equals(groupInfo.getHref())){
									group.addContact(contact);
									contact.addContactGroup(group);
									break;
								}
							}
						}
//						Collection<SourceType> sourceTypes = new HashSet<SourceType>();
						
						
						SourceType sourceTypeName = new SourceType();
						sourceTypeName.setType("Name");
						SourceField sourceFieldName = new SourceField();
						sourceFieldName.setName("Vollst�ndiger Name");
						sourceFieldName.setValue(fullname);
						sourceTypeName.addSourceField(sourceFieldName);
						if (name.hasAdditionalName()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Zweiter Vorname");
							sourceFieldName.setValue(name.getAdditionalName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasFamilyName()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Nachname");
							sourceFieldName.setValue(name.getFamilyName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasGivenName()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Vorname");
							sourceFieldName.setValue(name.getGivenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasNamePrefix()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Prefix");
							sourceFieldName.setValue(name.getNamePrefix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasNameSuffix()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Suffix");
							sourceFieldName.setValue(name.getNameSuffix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasGender()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Geschlecht");
							sourceFieldName.setValue(entry.getGender().getValue().toString());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasInitials()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Initialen");
							sourceFieldName.setValue(entry.getInitials().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasMaidenName()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("M�dchenname");
							sourceFieldName.setValue(entry.getMaidenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasNickname()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Nickname");
							sourceFieldName.setValue(entry.getNickname().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasShortName()){
							SourceField sourceField = new SourceField();
							sourceFieldName.setName("Kurzname");
							sourceFieldName.setValue(entry.getShortName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						contact.addSourceType(sourceTypeName);

						if (entry.hasOrganizations()){
							SourceType sourceType = new SourceType();
							sourceType.setType("Titel und Unternehmen");
							for (Organization orga : entry.getOrganizations()){
								SourceField sourceFieldEnterprice = new SourceField();
								sourceFieldEnterprice.setName("Unternehmen");
								sourceFieldEnterprice.setValue(orga.getOrgName().getValue());
								SourceField sourceFieldTitle = new SourceField();
								sourceFieldTitle.setName("Titel");
								sourceFieldTitle.setValue(orga.getOrgTitle().getValue());
								sourceType.addSourceField(sourceFieldEnterprice);
								sourceType.addSourceField(sourceFieldTitle);
							}
							contact.addSourceType(sourceType);
						}
												
						if (entry.hasEmailAddresses()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Email");
							for (Email email : entry.getEmailAddresses()){
								String emailLabel = null;
								if (email.getLabel() != null)
									emailLabel = email.getLabel();
								else
									emailLabel = transformFieldName(email.getRel());
								SourceField sourceField = new SourceField();
								sourceField.setName(emailLabel);
								sourceField.setValue(email.getAddress());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasPhoneNumbers()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Telefon");
							for (PhoneNumber phoneNumber : entry.getPhoneNumbers()){
								String phoneLabel = null;
								if (phoneNumber.getLabel() != null)
									phoneLabel = phoneNumber.getLabel();
								else
									phoneLabel = transformFieldName(phoneNumber.getRel());
								if (phoneLabel.equals("grandcentral"))
									phoneLabel = "Google Voice";
								SourceField sourceField = new SourceField();
								sourceField.setName(phoneLabel);
								sourceField.setValue(phoneNumber.getPhoneNumber());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);							
						}
						
						if (entry.hasPostalAddresses() || entry.hasStructuredPostalAddresses()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Adresse");
							for (PostalAddress postalAddress : entry.getPostalAddresses()){
								String addressLabel = null;
								if (postalAddress.getLabel() != null)
									addressLabel = postalAddress.getLabel();
								else
									addressLabel = transformFieldName(postalAddress.getRel());
								SourceField sourceField = new SourceField();
								sourceField.setName(addressLabel);
								sourceField.setValue(postalAddress.getValue());
								sourceType.addSourceField(sourceField);
							}
							for (StructuredPostalAddress postalAddress : entry.getStructuredPostalAddresses()){
								String addressLabel = null;
								if (postalAddress.getLabel() != null)
									addressLabel = postalAddress.getLabel();
								else
									addressLabel = transformFieldName(postalAddress.getRel());
								SourceField sourceField = new SourceField();
								sourceField.setName(addressLabel);
								sourceField.setValue(postalAddress.getFormattedAddress().getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasBirthday()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Geburtstag");
							SourceField sourceField = new SourceField();
							sourceField.setName("Geburtstag");
							sourceField.setValue(entry.getBirthday().getValue());
							sourceType.addSourceField(sourceField);
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasWebsites()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("URL");
							for (Website website : entry.getWebsites()){
								String websiteLabel = null;
								if (website.getLabel() != null)
									websiteLabel = website.getLabel();
								else
									websiteLabel = transformFieldName(website.getRel().toValue());
								SourceField sourceField = new SourceField();
								sourceField.setName(websiteLabel);
								sourceField.setValue(website.getHref());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);							
						}
						
						if (entry.hasImAddresses()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Instant Messaging");
							for (Im im : entry.getImAddresses()){
								String imLabel = null;
								if (!im.getProtocol().startsWith("http://schemas.google.com/g/2005#"))
									imLabel = im.getProtocol();
								else
									imLabel = transformFieldName(im.getProtocol());
								SourceField sourceField = new SourceField();
								sourceField.setName(imLabel);
								sourceField.setValue(im.getAddress());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasUserDefinedFields()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Benuzerdefiniertes Feld");
							for (UserDefinedField field : entry.getUserDefinedFields()){
								SourceField sourceField = new SourceField();
								sourceField.setName(field.getKey());
								sourceField.setValue(field.getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
											
						if (entry.hasExtendedProperties()){
							SourceType sourceType = new SourceType();
							sourceTypeName.setType("Extended Property");
							for (ExtendedProperty property : entry.getExtendedProperties()){
								SourceField sourceField = new SourceField();
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

//			int anzahl = 0;
//			for (Contact contact: contacts){
//				anzahl++;
//				System.out.println(contact.getName() + " (" + contact.geteTag() + ")\n\tGruppen:");
//				int index = 0;
//				for (ContactGroup group: contact.getGroups()){
//					index++;
//					System.out.println("\t\tGruppe " + index + ": " + group.getName());
//				}
//				
//				for (SourceType type : contact.getSourceTypes()){
//					System.out.println("\t" + type.getType() + ":");
//					for (SourceField field : type.getSourceFields()){
//						System.out.println("\t\t" + field.getName() + ": " + field.getValue());
//					}
//				}
//			}
//			System.out.println("\n\nAnzahl der Kontakte: " + anzahl);
			
			
			fertig = true;
			
			
//			for (ContactGroup group: contactGroups){
//				System.out.println(group.getName() + ":");
//				for (Contact contact: group.getContacts()){
//					System.out.println("\t" + contact.getName() + ":");
//					for (SourceType type: contact.getSourceTypes()){
//						System.out.println("\t\t" + type.getType() + ":");
//						for (SourceField field: type.getSourceFields()){
//							System.out.println("\t\t\t" + field.getName() + ": "+ field.getValue());
//						}
//					}
//				}
//			}

		} catch (ServiceException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String transformFieldName (String googleCode){
		switch (googleCode){
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
			return "Gesch�ftlich";
		case Constant.GOOGLE_CONTACT_IS_WORK_FAX:
			return "Fax (gesch�ftl.)";
		case Constant.GOOGLE_WEBSITE_IS_BLOG:
			return "Blog";
		case Constant.GOOGLE_WEBSITE_IS_HOME_PAGE:
			return "Homepage";
		case Constant.GOOGLE_WEBSITE_IS_PROFILE:
			return "Profil";
		case Constant.GOOGLE_WEBSITE_IS_WORK:
			return "Gesch�ftlich";
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

	public Collection<Contact> getContacts() {
		return contacts;
		
	}
	
	public Collection<ContactGroup> getGroups() {
		return contactGroups;
		
	}
	

}
