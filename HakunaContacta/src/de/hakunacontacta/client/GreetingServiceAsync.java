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

	/**
	 * Diese Methode speichert die Login-E-Mail des Users zur Laufzeit im Objekt
	 * LoginInfo
	 * 
	 * @param requestUri
	 * @param asyncCallback
	 */
	void login(String requestUri, AsyncCallback<LoginInfo> asyncCallback);

	/**
	 * Diese Methode stellt die Verbindung zum Server her und erstellt und merkt
	 * sich clientseitig einen ContactManager
	 * 
	 * @param token
	 *            wird zum identifizieren des Users benötigt
	 * @param asyncCallback
	 */
	void loginDetails(String token, AsyncCallback<LoginInfo> asyncCallback);

	void getContacts(AsyncCallback<ArrayList<Contact>> callback);

	void getContactGroups(AsyncCallback<ArrayList<ContactGroup>> callback);

	/**
	 * Diese Methode setzt alle (angehakten) Kontakte und Gruppen auf dem Server
	 * auf selektiert
	 * 
	 * @param contacts
	 *            (angehakte) Kontakte
	 * @param contactGroups
	 *            (angehakte) Kontaktgruppen
	 * @param callback
	 */
	void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups, AsyncCallback<Void> callback);

	/**
	 * Diese Methode beendet die Session mit dem Aufruf invalidate()
	 * 
	 * @param callback
	 */
	void exitSession(AsyncCallback<Void> callback);

	void getContactSourceTypes(AsyncCallback<ArrayList<ContactSourceType>> callback);

	void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type, AsyncCallback<Void> callback);

	void getExportFields(ExportTypeEnum type, boolean firstload, AsyncCallback<ArrayList<ExportField>> callback);

	/**
	 * Diese Methode erstellt einen FileCreator und übergibt ihm Export- und
	 * ContactManager der aktuellen Session. Auf dem FileCreator wird mit der
	 * cleanseContacts()-Methode ein String generiert, der Kontakte mit den
	 * jeweils am höchsten priorisierten ExportOptions als SourceField
	 * zurückgibt
	 * 
	 * @param callback
	 */
	void getFile(AsyncCallback<String> callback);

}
