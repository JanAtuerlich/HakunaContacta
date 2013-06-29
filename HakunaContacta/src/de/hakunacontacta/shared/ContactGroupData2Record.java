package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;


public class ContactGroupData2Record {
	
    private static ContactGroupRecord[] records;  
    
    public static ContactGroupRecord[] getRecords() {  
        return records;  
    }  
  
    public static ContactGroupRecord[] getNewRecords(ArrayList<ContactGroup> contactGroups) {  
    	
    	ContactGroupRecord[] contactGroupRecords = new ContactGroupRecord[contactGroups.size()];
    	
    	
    	int i = 0;	
    		for (ContactGroup contactGroup : contactGroups) {
    	    	
    			contactGroup.getContacts();
    			String[] contacts = new String[contactGroup.getContacts().size()];
    			int j = 0;
        		for (Contact contact : contactGroup.getContacts()) {
        				contacts[j] = contact.geteTag();
        				j++;
				}
    			contactGroupRecords[i] = new  ContactGroupRecord(contactGroup.getName(), contactGroup.getSelected(), contacts); 
    			i++;
			}
		

		return contactGroupRecords;
    	 
    }  
}
