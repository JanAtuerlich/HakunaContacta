package de.hakunacontacta.shared;

import java.util.Collection;
import java.util.Date;  

  
public class ContactData2Record {  
  
    private static ContactRecord[] records;  
  
    public static ContactRecord[] getRecords() {  
        return records;  
    }  
  
    public static ContactRecord[] getNewRecords(Collection<Contact> contacts) {  

    	ContactRecord[] contactRecords = new ContactRecord[contacts.size()];
    			
    	int i = 0;
    		for (Contact contact : contacts) {
    			String[] groups = new String[contact.getGroups().size()];
    			for (String string : groups) {
        			for (ContactGroup contactGroup : contact.getGroups()) {
        				string = contactGroup.getName();
    				} 
				}
				contactRecords[i] = new  ContactRecord(contact.geteTag(), contact.getName(),groups); 
				i++;
			} 
		
    	
    	return contactRecords;
    	  
    }  
}  



