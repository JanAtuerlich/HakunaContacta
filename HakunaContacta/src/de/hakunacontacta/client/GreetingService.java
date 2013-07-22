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

	/**
	 * Diese Methode speichert die Login-E-Mail des Users zur Laufzeit im Objekt
	 * LoginInfo
	 * 
	 * @param requestUri
	 * @return LoginInfo enthält die Anmelde-E-Mail des Users und speichert den
	 *         Link zur Logout-Page
	 */
	LoginInfo login(String requestUri);

	/**
	 * Diese Methode stellt die Verbindung zum Server her und erstellt und merkt
	 * sich clientseitig einen ContactManager
	 * 
	 * @param token
	 *            wird zum identifizieren des Users benötigt
	 * @return LoginInfo enthält die Anmelde-E-Mail des Users und speichert den
	 *         Link zur Logout-Page
	 */
	LoginInfo loginDetails(String token);

	ArrayList<Contact> getContacts();

	ArrayList<ContactGroup> getContactGroups();

	/**
	 * Diese Methode setzt alle (angehakten) Kontakte und Gruppen auf dem Server
	 * auf selektiert
	 * 
	 * @param contacts
	 *            (angehakte) Kontakte
	 * @param contactGroups
	 *            (angehakte) Kontaktgruppen
	 */
	void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups);

	/**
	 * Diese Methode beendet die Session mit dem Aufruf invalidate()
	 */
	void exitSession();

	ArrayList<ContactSourceType> getContactSourceTypes();

	void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type);

	ArrayList<ExportField> getExportFields(ExportTypeEnum type, boolean firstload);

	/**
	 * Diese Methode erstellt einen FileCreator und übergibt ihm Export- und
	 * ContactManager der aktuellen Session. Auf dem FileCreator wird mit der
	 * cleanseContacts()-Methode ein String generiert, der Kontakte mit den
	 * jeweils am höchsten priorisierten ExportOptions als SourceField
	 * zurückgibt
	 * 
	 * @return Der zu exportierende String in Klartext
	 */
	String getFile();

}
