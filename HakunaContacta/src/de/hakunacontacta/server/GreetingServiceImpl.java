/**
 * Copyright (C) 2012-2013, Markus Sprunck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided
 *   with the distribution.
 *
 * - The name of its contributor may be used to endorse or promote
 *   products derived from this software without specific prior
 *   written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */


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
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportTypeEnum;
import de.hakunacontacta.shared.FieldVerifier;
import de.hakunacontacta.shared.LoginInfo;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLConnection;

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
	ContactManager contactManager;
	ExportManager exportManager;
	FileCreator fileCreator;

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

	// TODO #11: implement login helper methods in service implementation	

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
		final UserService userService = UserServiceFactory.getUserService();
		final User user = userService.getCurrentUser();
		final LoginInfo loginInfo = new LoginInfo();
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setName(user.getEmail());
			loginInfo.setLogoutUrl(userService.createLogoutURL("http://127.0.0.1:8888/logout.html"));
		} else {
			loginInfo.setLoggedIn(false);
//			loginInfo.setLoginUrl(userService.createLoginURL("http://hakunacontacta.appspot.com/HakunaContacta.html"));
			loginInfo.setLoginUrl(userService.createLoginURL("http://127.0.0.1:8888/HakunaContacta.html?gwt.codesvr=127.0.0.1:9997"));
		}
		return loginInfo;
	}

	@Override
	public LoginInfo loginDetails(final String token) {
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token;
		
		contactManager = new ContactManager();
		
		contactManager.load(token);

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
		return loginInfo;
	}
 
	@Override
	public ArrayList<Contact> getContacts() {
		// TODO Auto-generated method stub
		return contactManager.getContacts();
	}

	@Override
	public ArrayList<ContactGroup> getContactGroups() {
		// TODO Auto-generated method stub
		return contactManager.getGroups();
	}
	@Override
	public void setSelections(ArrayList<Contact> contacts,
			ArrayList<ContactGroup> contactGroups) {
		contactManager.setSelections(contacts, contactGroups);		
	}
	@Override	public ArrayList<ContactSourceType> getContactSourceTypes() {
		return contactManager.getSourceTypesOfSelectedContacts();
	}	// TODO #11:> end	

	@Override
	public void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type) {
		System.out.println("setExportFields wurde aufgerufen:");
		
		exportManager = ExportManager.getExportManager();
		
		
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

		exportManager.setExportField(exportFields);
	}
	

	public ArrayList<ExportField> getExportFields(ExportTypeEnum type) {
		exportManager = ExportManager.getExportManager();
		
		System.out.println("getExportFields aufgerufen! Übergabeparameter String type: " + type);
		
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

		
		return exportManager.getChosenExportFields();
	}
	
	public String getFile(){
		System.out.println("getFile - GreetingServiceImpl");
		fileCreator = FileCreator.getInstance(contactManager.getSelectedContacts(), exportManager.getChosenExportFields(), exportManager.getExportType());
		
		String x = fileCreator.cleanseContacts();
		System.out.println(x);
		return x;
	}
	
}
