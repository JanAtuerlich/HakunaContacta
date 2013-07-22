package de.hakunacontacta.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportTypeEnum;
import de.hakunacontacta.shared.LoginInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void getUserEmail(String token, AsyncCallback<String> callback);

	void login(String requestUri, AsyncCallback<LoginInfo> asyncCallback);

	void loginDetails(String token, AsyncCallback<LoginInfo> asyncCallback);

	void getContacts(AsyncCallback<ArrayList<Contact>> callback);

	void getContactGroups(AsyncCallback<ArrayList<ContactGroup>> callback);

	void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups, AsyncCallback<Void> callback);

	void getContactSourceTypes(AsyncCallback<ArrayList<ContactSourceType>> callback);

	void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type, AsyncCallback<Void> callback);

	void getExportFields(ExportTypeEnum type, boolean firstload, AsyncCallback<ArrayList<ExportField>> callback);

	void getFile(AsyncCallback<String> callback);

	void exitSession(AsyncCallback<Void> callback);

}
