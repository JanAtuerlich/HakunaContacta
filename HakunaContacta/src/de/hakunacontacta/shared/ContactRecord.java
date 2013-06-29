package de.hakunacontacta.shared;

import com.smartgwt.client.widgets.grid.ListGridRecord;  

public class ContactRecord extends ListGridRecord {  
  
    public ContactRecord() {  
    }  
  
    public ContactRecord(String etag, String name, boolean selected, String[] groups) {  
    	setEtag(etag);  
        setName(name);
        setSelected(selected);
        setGroups(groups);   
    }   
    
    public void setSelected(boolean selected) {
		setAttribute("selected", selected);		
	}
    
    public boolean getSelected(){
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
