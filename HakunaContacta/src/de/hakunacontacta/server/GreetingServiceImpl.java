package de.hakunacontacta.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.client.GreetingService;
import de.hakunacontacta.contactModule.ContactManager;
import de.hakunacontacta.exportModule.ExportManager;
import de.hakunacontacta.fileModule.FileCreator;
import de.hakunacontacta.shared.Constant;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportTypeEnum;
import de.hakunacontacta.shared.FieldVerifier;
import de.hakunacontacta.shared.LoginInfo;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private static Logger log = Logger.getLogger(GreetingServiceImpl.class.getCanonicalName());

	@Override
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public String getUserEmail(final String token) {
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		if (null != user) {
			return user.getEmail();
		} else {
			return "noreply@sample.com";
		}
	}

	@Override
	public LoginInfo login(final String requestUri) {
		System.out.println("Betrete Methode login()");
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		System.out.println("getCurrentUser: " + userService.getCurrentUser());
		final LoginInfo loginInfo = new LoginInfo();
		
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setName(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL("/logout.html"));
			System.out.println("LOGOUT-URL: " + userService.createLogoutURL("/logout.html"));
//			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));

		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(Constant.APP_LINK));
		}
		return loginInfo;
	}

	@Override
	public LoginInfo loginDetails(final String token) {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;
		
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("loginDetails(): die Session-ID lautet: " + session.getId());

		if (session.getAttribute("contactManager")==null) {
			ContactManager contactManager = new ContactManager();
			session.setAttribute("contactManager", contactManager);
			contactManager.load(token);
		}
		

		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		
//		Ersetzt in Session-Handling: Versuch 1
//		contactManager = new ContactManager();
		

		

		final StringBuffer r = new StringBuffer();
		try {
			final URL u = new URL(url);
			final URLConnection uc = u.openConnection();
			final int end = 1000;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(uc.getInputStream());
				br = new BufferedReader(isr);
				final int chk = 0;
				while ((url = br.readLine()) != null) {
					if ((chk >= 0) && ((chk < end))) {
						r.append(url).append('\n');
					}
				}
			} catch (final java.net.ConnectException cex) {
				r.append(cex.getMessage());
			} catch (final Exception ex) {
				log.log(Level.SEVERE, ex.getMessage());
			} finally {
				try {
					br.close();
				} catch (final Exception ex) {
					log.log(Level.SEVERE, ex.getMessage());
				}
			}
		} catch (final Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}

		final LoginInfo loginInfo = new LoginInfo();
		try {
			final JsonFactory f = new JsonFactory();
			JsonParser jp;
			jp = f.createJsonParser(r.toString());
			jp.nextToken();
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				final String fieldname = jp.getCurrentName();
				jp.nextToken();
				if ("picture".equals(fieldname)) {
					loginInfo.setPictureUrl(jp.getText());
				} else if ("name".equals(fieldname)) {
					loginInfo.setName(jp.getText());
				} else if ("email".equals(fieldname)) {
					loginInfo.setEmailAddress(jp.getText());
				}
			}
		} catch (final JsonParseException e) {
			log.log(Level.SEVERE, e.getMessage());
		} catch (final IOException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
		session.setAttribute("contactManager", contactManager);
		
		return loginInfo;
	}
 
	@Override
	public ArrayList<Contact> getContacts() {
		// TODO Auto-generated method stub
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("getContacts(): die Session-ID lautet: " + session.getId());
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");	
		System.out.println("TEST noch da ? Mal schauen: "+ session.getAttribute("TEST"));
		return contactManager.getContacts();
	}

	@Override
	public ArrayList<ContactGroup> getContactGroups() {
		// TODO Auto-generated method stub
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("getContactGroups(): die Session-ID lautet: " + session.getId());
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		
		return contactManager.getGroups();
	}
	
	@Override
	public void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("setSelections(): die Session-ID lautet: " + session.getId());
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");	
		
		contactManager.setSelections(contacts, contactGroups);
		
		session.setAttribute("contactManager", contactManager);
	}
	
	@Override
	public void exitSession(){
		HttpSession session = this.getThreadLocalRequest().getSession();
		session.invalidate();
	}
	
	@Override	
	public ArrayList<ContactSourceType> getContactSourceTypes() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("getContactSourceTypes(): die Session-ID lautet: " + session.getId());
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		for (Contact contact : contactManager.getSelectedContacts()) {
			System.out.println("selected Kontakte:"+contact.getName());
		}
		return contactManager.getSourceTypesOfSelectedContacts();
	}	// TODO #11:> end	

	@Override
	public void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("setExportFields(): die Session-ID lautet: " + session.getId());
		if(session.getAttribute("exportManager")==null){
			System.out.println("Neuer exportManager wird angelegt");
			session.setAttribute("exportManager", new ExportManager());
		}
		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");
		
		
		if (type == ExportTypeEnum.XML) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.XML);");
			exportManager.setExportFormat(ExportTypeEnum.XML);
		} else if (type == ExportTypeEnum.CSVWord) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.CSVWord);");
			exportManager.setExportFormat(ExportTypeEnum.CSVWord);
		} else if (type == ExportTypeEnum.vCard) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.vCard);");
			exportManager.setExportFormat(ExportTypeEnum.vCard);
		} else {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.CSV);");
			exportManager.setExportFormat(ExportTypeEnum.CSV);
		}

		session.setAttribute("exportManager", exportManager);
		exportManager.setExportField(exportFields);
	}
	
	public ArrayList<ExportField> getExportFields(ExportTypeEnum type, boolean firstload) {
		
		System.out.println("getExportFields aufgerufen! Übergabeparameter String type: " + type);
		
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("Ausgabe der Session in getExportFields" + session);
		if(session.getAttribute("exportManager")==null){
			System.out.println("Neuer exportManager wird angelegt");
			session.setAttribute("exportManager", new ExportManager());
		}
		
		if(firstload == true){
			System.out.println("FirstLoad! \n exportManager wird neu erstellt! \n Bisherige ExportSettings werden resettet!");
			session.setAttribute("exportManager", new ExportManager());
			
		}
		
		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");
		
				
		if (type == ExportTypeEnum.XML) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.XML);");
			exportManager.setExportFormat(ExportTypeEnum.XML);
		} else if (type == ExportTypeEnum.CSVWord) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.CSVWord);");
			exportManager.setExportFormat(ExportTypeEnum.CSVWord);
		} else if (type == ExportTypeEnum.vCard) {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.vCard);");
			exportManager.setExportFormat(ExportTypeEnum.vCard);
		} else {
			System.out.println(" - exportManager.setExportFormat(exportTypeEnum.CSV);");
			exportManager.setExportFormat(ExportTypeEnum.CSV);
		}

		session.setAttribute("exportManager", exportManager);
		return exportManager.getChosenExportFields();
	}
	
	public String getFile(){
		System.out.println("getFile - GreetingServiceImpl");
		
		HttpSession session = this.getThreadLocalRequest().getSession();
		System.out.println("Ausgabe der Session in getFile" + session);
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");
		if(session.getAttribute("fileCreator")==null){
			session.setAttribute("fileCreator", new FileCreator());
		}
		FileCreator fileCreator = (FileCreator)session.getAttribute("fileCreator");
		fileCreator.setFields(contactManager.getSelectedContacts(), exportManager.getChosenExportFields(), exportManager.getExportType());
//		session.setAttribute("fileCreator", fileCreator);
		
//		Ersetzt durch Session-Handling: Versuch 1
//		fileCreator = FileCreator.getInstance(contactManager.getSelectedContacts(), exportManager.getChosenExportFields(), exportManager.getExportType());
		
		String x = fileCreator.cleanseContacts();
		System.out.println(x);
		return x;
	}
	
	@Override
	public void exitSession(){
	HttpSession session = this.getThreadLocalRequest().getSession();
	session.invalidate();
	}
	
}
