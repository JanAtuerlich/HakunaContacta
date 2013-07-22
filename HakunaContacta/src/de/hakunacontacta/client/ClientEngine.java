package de.hakunacontacta.client;

import java.util.ArrayList;
import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.client.MyHistoryListener;
import de.hakunacontacta.shared.Constant;
import de.hakunacontacta.shared.ContactData2Record;
import de.hakunacontacta.shared.ContactGroupData2Record;
import de.hakunacontacta.shared.ContactGroupRecord;
import de.hakunacontacta.shared.ContactRecord;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ExportField;
import de.hakunacontacta.shared.ExportTreeManager;
import de.hakunacontacta.shared.ExportTypeEnum;
import de.hakunacontacta.shared.LoginInfo;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.smartgwt.client.widgets.tree.Tree;

/**
 * Die Klasse ClientEngine ist die "Main-Klasse" auf Client-Seite, über sie werden 
 * die RPC-Calls zum Server abgesetzt und die antworten bearbeitet. Des weiteren erstellt sie die Page1 und Page2
 */
public class ClientEngine implements EntryPoint {

	private ArrayList<Contact> contacts;
	private ArrayList<ContactGroup> contactGroups;
	private ContactRecord[] contactRecords;
	private ContactGroupRecord[] contactGroupRecords;

	private MyHistoryListener historyListener;
	private Tree contactSourceTypesTree = null;

	public MyHistoryListener getHistoryListener() {
		return historyListener;
	}

	private Page1 page1;
	private Page2 page2;
	private ExportTreeManager exportTreeManager = null;
	private ClientEngine thisClientEngine = this;
	private static final Auth AUTH = Auth.get();
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String GOOGLE_CLIENT_ID = Constant.GOOGLE_CLIENT_ID;
	private static final String PLUS_ME_SCOPE = "http://www.google.com/m8/feeds";
	private String user_token;

	private final HorizontalPanel loginPanel = new HorizontalPanel();
	private final Anchor signInLink = new Anchor("");
	private final Image loginImage = new Image();
	private String loginurl;
	private String logouturl;

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 *Die onModuleLoad-Methode wird als erstes beim Laden der Homepage aufgerufen.
	 * 
	 */
	@Override
	public void onModuleLoad() {
		Button loginButton = new Button("Einloggen");
		loginButton.addStyleName("loginlogoutButton");
		loginButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.assign(loginurl);
			}

		});
		signInLink.getElement().setClassName("login-area");
		signInLink.setTitle("sign out");
		loginImage.getElement().setClassName("login-image");
		loginPanel.add(loginButton);
		HTML starttext = new HTML("<h2>Willkommen bei Hakuna Contacta</h2><div id=\"startlogin\">So funktionierts:<br>1. Kontakte ausw\u00E4hlen<br>2. Exportfelder ausw\u00E4hlen<br>3. Download der Exportdatei<br>(4. Als Quelldatei f\u00FCr Word Serienbrief verwenden)</div><div id=\"gcontacts2word\"><img src=\"images/gcontacts2word.png\"><p class=\"whitetext\">Exportformate - CSV (Word-Serienbrief), CSV (Komma getrennt), vCARD, xCard (XML)  </p></div><div id=\"arrow1\"><img src=\"images/arrow1.png\"/></div>");
		RootPanel.get("content").add(starttext);

		RootPanel.get("loginPanelContainer").add(loginPanel);
		RootPanel.get("footer").clear();
		HTML footerimage = new HTML("<img src=\"images/1.jpg\">");
		RootPanel.get("footer").add(footerimage);
		final StringBuilder userEmail = new StringBuilder();
		greetingService.login(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					@Override
					public void onFailure(final Throwable caught) {
						GWT.log("login -> onFailure");
					}

					@Override
					public void onSuccess(final LoginInfo result) {
						if (result.getName() != null
								&& !result.getName().isEmpty()) {
							addGoogleAuthHelper();
							loadLogout(result);
							Button logoutButton = new Button("Ausloggen");
							logoutButton.addStyleName("loginlogoutButton");
							loginPanel.clear();
							logoutButton.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									thisClientEngine.exitSession();
								}

							});
							loginPanel.add(logoutButton);

						} else {
							loadLogin(result);

						}
						userEmail.append(result.getEmailAddress());
					}
				});

	}
	
	private void loadLogin(final LoginInfo loginInfo) {
		setLoginurl(loginInfo.getLoginUrl());
		signInLink.setHref(loginInfo.getLoginUrl());
		signInLink.setText("Einloggen");
		signInLink.setTitle("Einloggen");
	}

	private void loadLogout(final LoginInfo loginInfo) {
		setLogouturl(loginInfo.getLogoutUrl());
		signInLink.setHref(loginInfo.getLogoutUrl());
		signInLink.setText("Ausloggen");
		signInLink.setTitle("Ausloggen");
	}


	private void addGoogleAuthHelper() {
		final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL,
				GOOGLE_CLIENT_ID).withScopes(PLUS_ME_SCOPE);
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(final String token) {

				if (!token.isEmpty()) {
					user_token = token;
					greetingService.loginDetails(token,
							new AsyncCallback<LoginInfo>() {

								@Override
								public void onFailure(final Throwable caught) {
									GWT.log("loginDetails -> onFailure");
								}

								@Override
								public void onSuccess(final LoginInfo loginInfo) {
									greetingService.getContactGroups(new AsyncCallback<ArrayList<ContactGroup>>() {

												@Override
												public void onFailure(Throwable caught) {
												}

												@Override
												public void onSuccess(
														ArrayList<ContactGroup> result) {
													contactGroups = result;
													greetingService.getContacts(new AsyncCallback<ArrayList<Contact>>() {

																@Override
																public void onFailure(Throwable caught) {
																}

																@Override
																public void onSuccess(ArrayList<Contact> result) {
																	contacts = result;
																	for (ContactGroup contactGroup : contactGroups) {
																		ArrayList<Contact> tempContacts = new ArrayList<Contact>();
																		for (Contact groupContact : contactGroup
																				.getContacts()) {
																			for (Contact contact : contacts) {
																				if (groupContact.geteTag().equals(contact.geteTag())) {
																					tempContacts.add(contact);
																				}
																			}
																		}
																		contactGroup.setContacts(tempContacts);
																	}
																	ContactData2Record contactData2Record = new ContactData2Record();
																	ContactGroupData2Record contactGroupData2Record = new ContactGroupData2Record();

																	contactRecords = contactData2Record.getNewRecords(contacts);
																	contactGroupRecords = contactGroupData2Record.getNewRecords(contactGroups);

																	final String initToken = History.getToken();
																	historyListener = new MyHistoryListener();

																	page1 = new Page1(thisClientEngine);

																	if (initToken.length() == 0) {
																		History.newItem("page1");
																		historyListener.setPage1(page1);
																		History.addValueChangeHandler(historyListener);
																		History.fireCurrentHistoryState();
																	}

																	if (initToken.equals("page1")) {
																		historyListener.setPage1(page1);
																		History.addValueChangeHandler(historyListener);
																		History.fireCurrentHistoryState();
																	}

																	if (initToken.equals("page2")) {
																		historyListener.setPage1(page1);
																		History.addValueChangeHandler(historyListener);
																		reloadPage2();
																	}

																}
															});

												}

											});
								}
							});
				}
			}

			@Override
			public void onFailure(final Throwable caught) {
				GWT.log("Error -> loginDetails\n" + caught.getMessage());
			}
		});
	}
	
	/**
	 *createPage2 erstellt Page2 und holt sich dazu vom Server via RPC die SourceFelder der Kontakte,
	 *gibt diese an den TreeManager weiter und übergibt diesen Tree dann der page2.
	 *Des weiteren fügt sie die page2 zum HistoryHandler hinzu und öffnet sie.
	 * 
	 */
	public void createPage2() {
		greetingService
				.getContactSourceTypes(new AsyncCallback<ArrayList<ContactSourceType>>() {
					@Override
					public void onSuccess(ArrayList<ContactSourceType> result) {
						exportTreeManager = new ExportTreeManager();
						contactSourceTypesTree = exportTreeManager.getTree(result);

						page2 = new Page2(thisClientEngine,	contactSourceTypesTree);
						historyListener.setPage2(page2);
						page2.updateData();
						History.newItem("page2", true);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Problem beim Erstellen der ContactSourceTypes beim Client");
					}
				});
	}
	
	/**
	 *Wir beim aktuallisieren auf page2 aufgerufen und refrehed den Content
	 * 
	 */
	public void reloadPage2() {
		greetingService
				.getContactSourceTypes(new AsyncCallback<ArrayList<ContactSourceType>>() {
					@Override
					public void onSuccess(ArrayList<ContactSourceType> result) {
						exportTreeManager = new ExportTreeManager();
						contactSourceTypesTree = exportTreeManager.getTree(result);

						page2 = new Page2(thisClientEngine,	contactSourceTypesTree);
						historyListener.setPage2(page2);
						page2.updateData();
						History.newItem("page2", true);
						History.fireCurrentHistoryState();
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Problem beim Erstellen der ContactSourceTypes beim Client");
					}
				});
	}
	
	/**
	 *writeExportOption schickt die eingegeben ExportOptions für das entsprechende Format an den Server und holst das neue Format
	 * 
	 * @param ExportTree, last Format das Format unter dem der Tree gespeichert werden soll, newFormat das geladen werden soll
	 */
	public void writeExportOptions(Tree exportTree, ExportTypeEnum lastFormat,final ExportTypeEnum newFormat) {
			greetingService.setExportFields(exportTreeManager.writeExportTree(exportTree), lastFormat,	new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						thisClientEngine.getExportFields(newFormat, false);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("ClientEngine failed to call \"writeExportOptions\" !!");
					}
				});
	}
	
	/**
	 *getExportFields holt den ExportTree für das übergeben Format via RPC
	 *Ist firstload true wird ein neuer ExportManager mit den Standartfeldern erstellt
	 * 
	 * @param ExportTypeEnum gibt den gewünschen Tree an, firstload falls true wird resettet
	 */
	public void getExportFields(ExportTypeEnum type, boolean firstload) {
		greetingService.getExportFields(type, firstload,new AsyncCallback<ArrayList<ExportField>>() {

					@Override
					public void onSuccess(ArrayList<ExportField> result) {
						page2.setThisExportTypesTree(exportTreeManager.getExportFieldsTree(result));
						page2.updateData();
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("ClientEngine failed to call \"getExportFields\" !!");
					}

				});

	}
	
	/**
	 *getFile speichert nochmal die in der GUI eingegebenen ExportTree auf dem Server und lässt dann einer Datei im gewüschen
	 *Format erstellen. Diese Datei wird als String in page2 gesetzt und als Download dann bereitgestellt.
	 * 
	 * @param  exportTree aus der GUI, gewünschtes ExportFormat
	 */
	public void getFile(Tree exportTree, ExportTypeEnum lastFormat,
			final ExportTypeEnum newFormat) {
		greetingService.setExportFields(exportTreeManager.writeExportTree(exportTree), lastFormat, new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						greetingService.getFile(new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {

								result = de.hakunacontacta.client.Base64.encode(result);

								page2.setEncoded(result);
								page2.createDownloadLink();
							}

							@Override
							public void onFailure(Throwable caught) {
								System.out.println("ClientEnginegetFile failed to call \"getFiles\" !!");
							}
						});
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("ClientEngine failed to call \"setExportOptions\" !!");
					}
				});

	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}

	public ArrayList<ContactGroup> getContactGroups() {
		return contactGroups;
	}

	public ContactGroupRecord[] getContactGroupRecord() {
		return contactGroupRecords;
	}

	public ContactRecord[] getContactRecords() {
		return contactRecords;
	}

	
	

	/**
	 *Setzt die Selections der Kontakte und Kontaktgruppen und sendet diese an den Server
	 * 
	 */
	public void setSelections(ContactRecord[] contactRecords,
			ContactGroupRecord[] contactGroupRecords) {
		for (ContactGroupRecord contactGroupRecord : contactGroupRecords) {
			for (ContactGroup contactGroup : contactGroups) {
				if (contactGroupRecord.getGroupname().equals(
						contactGroup.getName())) {
					contactGroup.setSelected(contactGroupRecord.getSelected());
				}
			}
		}
		for (ContactRecord contactRecord : contactRecords) {
			for (Contact contact : contacts) {
				if (contactRecord.getEtag().equals(contact.geteTag())) {
					contact.setSelected(contactRecord.getSelected());
				}
			}
		}
		greetingService.setSelections(contacts, contactGroups,
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						thisClientEngine.createPage2();
					}
				});
	}
	
	
	/**
	 *exitSession() löscht die Session auf dem Server nachdem sich der Benutzer ausgeloggt hat
	 * 
	 */
	public void exitSession() {
		greetingService.exitSession(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("exitSession -> onFailure");

				System.out.println("Failed to call exitSession() on greetingService in ClientEngine");
			}

			@Override
			public void onSuccess(Void result) {
				AUTH.clearAllTokens();
				Window.Location.assign(thisClientEngine.getLogouturl());
			}
		});
	}

	public HorizontalPanel getLoginPanel() {
		return loginPanel;
	}

	public Anchor getSignInLink() {
		return signInLink;
	}

	public String getLoginurl() {
		return loginurl;
	}

	public void setLoginurl(String loginurl) {
		this.loginurl = loginurl;
	}

	public String getLogouturl() {
		return logouturl;
	}

	public void setLogouturl(String logouturl) {
		this.logouturl = logouturl;
	}

	public void setPage2(Page2 page2) {
		this.page2 = page2;
	}

	public static native String getBrowserName() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;

	public static boolean isChromeBrowser() {
		return getBrowserName().toLowerCase().contains("chrome");
	}

	public static boolean isFirefoxBrowser() {
		return getBrowserName().toLowerCase().contains("firefox");
	}

	public static boolean isIEBrowser() {
		return getBrowserName().toLowerCase().contains("msie");
	}

	public String getUser_token() {
		return user_token;
	}
	



}
