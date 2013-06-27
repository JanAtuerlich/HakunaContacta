package de.hakunacontacta.shared;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ContactGroupRecord  extends ListGridRecord {

    public ContactGroupRecord() {  
    }  
  
    public ContactGroupRecord(String groupname, String[] contacts) {  
        setGroupname(groupname);  
        setContacts(contacts);    
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
  
}
