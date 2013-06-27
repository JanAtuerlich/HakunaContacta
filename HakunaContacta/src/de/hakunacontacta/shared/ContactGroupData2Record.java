package de.hakunacontacta.shared;

import java.util.Collection;


public class ContactGroupData2Record {
	
    private static ContactGroupRecord[] records;  
    
    public static ContactGroupRecord[] getRecords() {  
        return records;  
    }  
  
    public static ContactGroupRecord[] getNewRecords(Collection<ContactGroup> contactGroups) {  
    	
    	ContactGroupRecord[] contactGroupRecords = new ContactGroupRecord[contactGroups.size()];
    	
    	
    	int i = 0;	
    		for (ContactGroup contactGroup : contactGroups) {
    	    	
    			contactGroup.getContacts();
    			String[] contacts = new String[contactGroup.getContacts().size()];
    			for (String string : contacts) {
        			for (Contact contact : contactGroup.getContacts()) {
        				string = contact.geteTag();
    				} 
				}
    			contactGroupRecords[i] = new  ContactGroupRecord(contactGroup.getName(), contacts); 
    			i++;
			}
		
    	
    	
    	for (ContactGroupRecord contactGroupRecord : contactGroupRecords) {
			System.out.println(contactGroupRecord.getAttribute("groupname"));
		}
    	
		return contactGroupRecords;
    	 
    }  
}
