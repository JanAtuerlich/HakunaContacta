package de.hakunacontacta.shared;

import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;

  
public class ContactData2Record {  
  
    private static ContactRecord[] records;  
  
    public static ContactRecord[] getRecords() {  
        return records;  
    }  
  
    public static ContactRecord[] getNewRecords(ArrayList<Contact> contacts) {  

    	ContactRecord[] contactRecords = new ContactRecord[contacts.size()];
    			
    	int i = 0;
    		for (Contact contact : contacts) {
    			String[] groups = new String[contact.getGroups().size()];
    			int j = 0;
        		for (ContactGroup contactGroup : contact.getGroups()) {      			
        			groups[j] = contactGroup.getName();
        			j++;
    			} 
				
				contactRecords[i] = new  ContactRecord(contact.geteTag(), contact.getName(),contact.getSelected(), groups); 
				i++;
			} 
		
    	
    	return contactRecords;
    	  
    }  
}  



