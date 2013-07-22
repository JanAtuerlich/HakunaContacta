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
 * Diese Klasse ist die serverseitige Implementierung des RPC-Service
 * @author MB
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private static Logger log = Logger.getLogger(GreetingServiceImpl.class.getCanonicalName());

	/* (non-Javadoc)
	 * @see de.hakunacontacta.client.GreetingService#login(java.lang.String)
	 */
	@Override
	public LoginInfo login(final String requestUri) {

		final UserService userService = UserServiceFactory.getUserService();

		final User user = userService.getCurrentUser();
		final LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setName(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL("/logout.html"));

		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(Constant.APP_LINK));
		}

		return loginInfo;
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.client.GreetingService#loginDetails(java.lang.String)
	 */
	@Override
	public LoginInfo loginDetails(final String token) {

		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;

		HttpSession session = this.getThreadLocalRequest().getSession();

		if (session.getAttribute("contactManager") == null) {
			ContactManager contactManager = new ContactManager();
			session.setAttribute("contactManager", contactManager);
			contactManager.load(token);
		}

		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");

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
		HttpSession session = this.getThreadLocalRequest().getSession();
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		return contactManager.getContacts();
	}

	@Override
	public ArrayList<ContactGroup> getContactGroups() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		return contactManager.getGroups();
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.client.GreetingService#setSelections(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		contactManager.setSelections(contacts, contactGroups);
		session.setAttribute("contactManager", contactManager);
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.client.GreetingService#exitSession()
	 */
	@Override
	public void exitSession() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		session.invalidate();
	}

	@Override
	public ArrayList<ContactSourceType> getContactSourceTypes() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		return contactManager.getSourceTypesOfSelectedContacts();
	}

	@Override
	public void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		if (session.getAttribute("exportManager") == null) {
			session.setAttribute("exportManager", new ExportManager());
		}
		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");

		if (type == ExportTypeEnum.XML) {
			exportManager.setExportFormat(ExportTypeEnum.XML);
		} else if (type == ExportTypeEnum.CSVWord) {
			exportManager.setExportFormat(ExportTypeEnum.CSVWord);
		} else if (type == ExportTypeEnum.vCard) {
			exportManager.setExportFormat(ExportTypeEnum.vCard);
		} else {
			exportManager.setExportFormat(ExportTypeEnum.CSV);
		}

		session.setAttribute("exportManager", exportManager);
		exportManager.setExportField(exportFields);
	}

	public ArrayList<ExportField> getExportFields(ExportTypeEnum type, boolean firstload) {

		HttpSession session = this.getThreadLocalRequest().getSession();
		if (session.getAttribute("exportManager") == null) {
			session.setAttribute("exportManager", new ExportManager());
		}

		if (firstload == true) {
			session.setAttribute("exportManager", new ExportManager());
		}

		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");

		if (type == ExportTypeEnum.XML) {
			exportManager.setExportFormat(ExportTypeEnum.XML);
		} else if (type == ExportTypeEnum.CSVWord) {
			exportManager.setExportFormat(ExportTypeEnum.CSVWord);
		} else if (type == ExportTypeEnum.vCard) {
			exportManager.setExportFormat(ExportTypeEnum.vCard);
		} else {
			exportManager.setExportFormat(ExportTypeEnum.CSV);
		}

		session.setAttribute("exportManager", exportManager);
		return exportManager.getChosenExportFields();
	}

	/* (non-Javadoc)
	 * @see de.hakunacontacta.client.GreetingService#getFile()
	 */
	public String getFile() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ContactManager contactManager = (ContactManager) session.getAttribute("contactManager");
		ExportManager exportManager = (ExportManager) session.getAttribute("exportManager");
		if (session.getAttribute("fileCreator") == null) {
			session.setAttribute("fileCreator", new FileCreator());
		}
		FileCreator fileCreator = (FileCreator) session.getAttribute("fileCreator");
		fileCreator.setFields(contactManager.getSelectedContacts(), exportManager.getChosenExportFields(), exportManager.getExportType());

		String x = fileCreator.cleanseContacts();
		return x;
	}

}
