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

public class ContactManager implements IContactManager, Serializable{
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ArrayList<ContactGroup> contactGroups = new ArrayList<ContactGroup>();
	private final static Logger LOGGER = Logger.getLogger(ContactManager.class.getName());

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
	
	public ContactManager(){
		
	}
	
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
						String eTag = entry.getId();
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
//						ArrayList<SourceType> sourceTypes = new ArrayList<SourceType>();
						
						
						ContactSourceType sourceTypeName = new ContactSourceType();
						sourceTypeName.setType("Name");
						ContactSourceField sourceFieldName = new ContactSourceField();
						sourceFieldName.setName("Vollständiger Name");
						sourceFieldName.setValue(fullname);
						sourceTypeName.addSourceField(sourceFieldName);
						if (name.hasAdditionalName()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Zweiter Vorname");
							sourceField.setValue(name.getAdditionalName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasFamilyName()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Nachname");
							sourceField.setValue(name.getFamilyName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasGivenName()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Vorname");
							sourceField.setValue(name.getGivenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasNamePrefix()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Prefix");
							sourceField.setValue(name.getNamePrefix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (name.hasNameSuffix()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Suffix");
							sourceField.setValue(name.getNameSuffix().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasGender()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Geschlecht");
							sourceField.setValue(entry.getGender().getValue().toString());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasInitials()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Initialen");
							sourceField.setValue(entry.getInitials().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasMaidenName()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Mädchenname");
							sourceField.setValue(entry.getMaidenName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasNickname()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Nickname");
							sourceField.setValue(entry.getNickname().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						if (entry.hasShortName()){
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Kurzname");
							sourceField.setValue(entry.getShortName().getValue());
							sourceTypeName.addSourceField(sourceField);
						}
						contact.addSourceType(sourceTypeName);

						if (entry.hasOrganizations()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Titel und Unternehmen");
							for (Organization orga : entry.getOrganizations()){
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
												
						if (entry.hasEmailAddresses()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Email");
							for (Email email : entry.getEmailAddresses()){
								String emailLabel = null;
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
						
						if (entry.hasPhoneNumbers()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Telefon");
							for (PhoneNumber phoneNumber : entry.getPhoneNumbers()){
								String phoneLabel = null;
								if (phoneNumber.getLabel() != null)
									phoneLabel = phoneNumber.getLabel();
								else
									phoneLabel = transformFieldName(phoneNumber.getRel());
								if (phoneLabel.equals("grandcentral"))
									phoneLabel = "Google Voice";
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(phoneLabel);
								sourceField.setValue(phoneNumber.getPhoneNumber());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);							
						}
						
						if (entry.hasPostalAddresses() || entry.hasStructuredPostalAddresses()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Adresse");
							for (PostalAddress postalAddress : entry.getPostalAddresses()){
								String addressLabel = null;
								if (postalAddress.getLabel() != null)
									addressLabel = postalAddress.getLabel();
								else
									addressLabel = transformFieldName(postalAddress.getRel());
								ContactSourceField sourceField = new ContactSourceField();
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
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(addressLabel);
								sourceField.setValue(postalAddress.getFormattedAddress().getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasBirthday()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Geburtstag");
							ContactSourceField sourceField = new ContactSourceField();
							sourceField.setName("Geburtstag");
							sourceField.setValue(entry.getBirthday().getValue());
							sourceType.addSourceField(sourceField);
							contact.addSourceType(sourceType);
						}
						
						if (entry.hasWebsites()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("URL");
							for (Website website : entry.getWebsites()){
								String websiteLabel = null;
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
						
						if (entry.hasImAddresses()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Instant Messaging");
							for (Im im : entry.getImAddresses()){
								String imLabel = null;
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
						
						if (entry.hasUserDefinedFields()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Benuzerdefiniertes Feld");
							for (UserDefinedField field : entry.getUserDefinedFields()){
								ContactSourceField sourceField = new ContactSourceField();
								sourceField.setName(field.getKey());
								sourceField.setValue(field.getValue());
								sourceType.addSourceField(sourceField);
							}
							contact.addSourceType(sourceType);
						}
											
						if (entry.hasExtendedProperties()){
							ContactSourceType sourceType = new ContactSourceType();
							sourceType.setType("Extended Property");
							for (ExtendedProperty property : entry.getExtendedProperties()){
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
//				for (ContactSourceType type : contact.getSourceTypes()){
//					System.out.println("\t" + type.getType() + ":");
//					for (ContactSourceField field : type.getSourceFields()){
//						System.out.println("\t\t" + field.getName() + ": " + field.getValue());
//					}
//				}
//			}
//			System.out.println("\n\nAnzahl der Kontakte: " + anzahl);
			
			
			
			
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

	public ArrayList<Contact> getContacts() {
		return contacts;
		
	}
	
	public ArrayList<ContactGroup> getGroups() {
		return contactGroups;
		
	}


	@Override
	public ArrayList<ContactGroup> getAllGroups() {
		
		return contactGroups;
	}

	@Override
	public ArrayList<Contact> getContacts(String groupName) {
		ArrayList<Contact> contactsFromGroups = new ArrayList<Contact>(); //braucht man für getContacts Methode
		if (groupName != "all"){
			for(ContactGroup contactGroup : contactGroups) {
				if (contactGroup.getName() == groupName){
					contactsFromGroups = contactGroup.getContacts();
					
				}
			}
			return contactsFromGroups;
		}
		else{
			return contacts;
		}
	
	}

	@Override
	public void selectGroup(String groupName) {
		for(ContactGroup contactGroup : contactGroups) {
			if (contactGroup.getName() == groupName){
				contactGroup.selectChkContacts();
			}
		}
		
	}

	@Override
	public void unselectGroup(String groupName) {
		for(ContactGroup contactGroup : contactGroups) {
			if (contactGroup.getName() == groupName){
				contactGroup.unselectChkContacts();
			}
		}
	}

	@Override
	public void selectContact(String eTag) {
		for(Contact contact : contacts) {
			if (contact.geteTag() == eTag){
				contact.selectChkGroups();
			}
		}
	}

	@Override
	public void unselectContact(String eTag) {
		for(Contact contact : contacts) {
			if (contact.geteTag() == eTag){
				contact.unselectChkGroups();
			}
		}
	}

	
	@Override
	public ArrayList<ContactSourceType> getSourceTypesOfSelectedContacts() {
		ArrayList<ContactSourceType> exportSourceTypes = new ArrayList<ContactSourceType>();
		
		for (Contact contact : contacts) {
			if (contact.getSelected() == true) {
				for (ContactSourceType contactSourceType : contact.getSourceTypes()) {
					if(!exportSourceTypes.contains(contactSourceType)){
						ContactSourceType exportSourceType = new ContactSourceType();
						exportSourceType.setType(contactSourceType.getType());
						exportSourceTypes.add(exportSourceType);
					}
				}
				for (ContactSourceType exportSourceType : exportSourceTypes) {
					for (ContactSourceType contactSourceType : contact.getSourceTypes()) {
						if(exportSourceType.getType() == contactSourceType.getType()){
							for (ContactSourceField contactSourceField : contactSourceType.getSourceFields()) {
								if(!exportSourceType.getSourceFields().contains(contactSourceField)){
									ContactSourceField exportSourceField = new ContactSourceField();
									exportSourceField.setName(contactSourceField.getName());
									exportSourceField.setAnzahl(1);
									exportSourceType.addSourceField(exportSourceField);
								}else{
									for (ContactSourceField exportSourceField : exportSourceType.getSourceFields()) {
										if(exportSourceField.getName() == contactSourceField.getName()){
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
		
//		String x = null;
//		x = "Contacts + SourceTypes:\n";
//		for (Contact contact : this.getSelectedContacts()) {
//			x += "\n " + contact.getName() + ": ";
//			
//			for (ContactSourceType contactSourceType : contact.getSourceTypes()) {
//				
//				x += contactSourceType.getType()+ "( ";
//				
//				for (ContactSourceField contactSourceField : contactSourceType.getSourceFields()) {
//					
//					x+= contactSourceField.getName() + " [" +contactSourceField.getValue() + "], ";
//					
//				}
//				
//				x += ") \n";
//				
//			}
//		}
//		
//		System.out.println(x);
		
		return exportSourceTypes;
	}

	@Override
	public ArrayList<Contact> getSelectedContacts() {
		ArrayList<Contact> contacsSelected = new ArrayList<Contact>();
		for (Contact contact : contacts) {
			if (contact.getSelected() == true) {
				contacsSelected.add(contact);
			}
		}
		return contacsSelected;
	}

	public void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups) {
		this.contacts = contacts;
		this.contactGroups = contactGroups;
//		for (ContactGroup contactGroup : contactGroups) {
//			if(contactGroup.getSelected()) System.out.println("Selektiert: " + contactGroup.getName());
//		}
//		for (Contact contact : contacts) {
//			if(contact.getSelected()) System.out.println("Selektiert: " + contact.getName());
//		}
	}
	

}
