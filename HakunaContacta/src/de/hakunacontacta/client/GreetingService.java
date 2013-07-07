package de.hakunacontacta.client;

import java.util.ArrayList;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportTypeEnum;
import de.hakunacontacta.shared.LoginInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

	String greetServer(String name) throws IllegalArgumentException;

	// TODO #09: start create login helper methods in service interface
	String getUserEmail(String token);

	LoginInfo login(String requestUri);

	LoginInfo loginDetails(String token);

	ArrayList<Contact> getContacts();

	ArrayList<ContactGroup> getContactGroups();

	void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups);

	ArrayList<ContactSourceType> getContactSourceTypes();
	
	void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type);
	
	ArrayList<ExportField> getExportFields(ExportTypeEnum type);
	
	String getFile();
}
