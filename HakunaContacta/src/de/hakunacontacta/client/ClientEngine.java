package de.hakunacontacta.client;


import java.util.ArrayList;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.client.MyHistoryListener;
import de.hakunacontacta.shared.ContactSourceField;
import de.hakunacontacta.shared.ContactSourceType;
import de.hakunacontacta.shared.ContactSourceTypes2Tree;
import de.hakunacontacta.shared.LoginInfo;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.smartgwt.client.widgets.tree.Tree;

 
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClientEngine implements EntryPoint {

	// TODO #05: add constants for OAuth2 (don't forget to update GOOGLE_CLIENT_ID)
	private ArrayList<Contact> contacts;
	private ArrayList<ContactGroup> contactGroups;
	private MyHistoryListener historyListener;
	private Tree contactSourceTypesTree = null;
	public MyHistoryListener getHistoryListener() {
		return historyListener;
	}
	
	private Page1 page1;
	private Page2 page2;
	public boolean check = false;
	private ClientEngine thisClientEngine = this;
	private static final Auth AUTH = Auth.get();
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String GOOGLE_CLIENT_ID = "21423817686-ganfq9ap8qed1bffutk67eib8mvdsud6.apps.googleusercontent.com";
	private static final String PLUS_ME_SCOPE = "http://www.google.com/m8/feeds";
	// TODO #05:> end

	// TODO #06: define controls for login
	private final HorizontalPanel loginPanel = new HorizontalPanel();
	private final Anchor signInLink = new Anchor("");
	private final Image loginImage = new Image();
//	private final TextBox nameField = new TextBox();
	// TODO #06:> end

	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	// TODO #07: add helper methods for Login, Logout and AuthRequest

	private void loadLogin(final LoginInfo loginInfo) {
		signInLink.setHref(loginInfo.getLoginUrl());
		signInLink.setText("Einloggen");
		signInLink.setTitle("Einloggen");
	}

	private void loadLogout(final LoginInfo loginInfo) {
		signInLink.setHref(loginInfo.getLogoutUrl());
		signInLink.setText(loginInfo.getName()+"Ausloggen");
		signInLink.setTitle("Ausloggen");
	}

	private void addGoogleAuthHelper() {
		final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, GOOGLE_CLIENT_ID)
				.withScopes(PLUS_ME_SCOPE);
		AUTH.login(req, new Callback<String, Throwable>() {
			@Override
			public void onSuccess(final String token) {

				if (!token.isEmpty()) {
					greetingService.loginDetails(token, new AsyncCallback<LoginInfo>() {
						@Override
						public void onFailure(final Throwable caught) {
							GWT.log("loginDetails -> onFailure");
						}
			
						@Override
						public void onSuccess(final LoginInfo loginInfo) {
							greetingService.getContactGroups(new AsyncCallback<ArrayList<ContactGroup>>(){
							
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									System.out.println("Fehler bei der Rückgabe der Gruppen.");
								}

								@Override
								public void onSuccess(ArrayList<ContactGroup> result) {
									contactGroups = result;
									greetingService.getContacts(new AsyncCallback<ArrayList<Contact>>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											System.out.println("Fehler bei der Rückgabe der Kontakte.");
										}

										@Override
										public void onSuccess(ArrayList<Contact> result) {
											contacts = result;
											for (ContactGroup contactGroup: contactGroups){
												ArrayList<Contact> tempContacts = new ArrayList<Contact>();
												for (Contact groupContact: contactGroup.getContacts()){
													for (Contact contact: contacts){
														if (groupContact.geteTag().equals(contact.geteTag())){
															tempContacts.add(contact);
														}
													}
												}
												contactGroup.setContacts(tempContacts);
											}
											final String initToken = History.getToken();
											
											page1 = Page1.getInstance(thisClientEngine);
									    	
										    if (initToken.length() == 0) {
										      History.newItem("page1");
										    }
										    
										    historyListener = new MyHistoryListener();
										    historyListener.setPage1(page1);
										    
									    	History.addValueChangeHandler(historyListener);
									    	History.fireCurrentHistoryState(); 
										}
									});
									
								}
								
							});
							

							
							signInLink.setText(loginInfo.getName());
							signInLink.setStyleName("login-area");
							loginImage.setVisible(false);
							loginPanel.add(loginImage);
							loginImage.addLoadHandler(new LoadHandler() {
								@Override
								public void onLoad(final LoadEvent event) {
									final int newWidth = 24;
									final com.google.gwt.dom.client.Element element = event
											.getRelativeElement();
									if (element.equals(loginImage.getElement())) {
										final int originalHeight = loginImage.getOffsetHeight();
										final int originalWidth = loginImage.getOffsetWidth();
										if (originalHeight > originalWidth) {
											loginImage.setHeight(newWidth + "px");
										} else {
											loginImage.setWidth(newWidth + "px");
										}
										loginImage.setVisible(true);
									}
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
	
	public void createPage2() {

		greetingService.getContactSourceTypes(new AsyncCallback<ArrayList<ContactSourceType>>() {
					@Override
					public void onSuccess(ArrayList<ContactSourceType> result) {
						ContactSourceTypes2Tree contactSourceTypes2Tree = new ContactSourceTypes2Tree();
						
//						ContactSourceType c = new ContactSourceType();
//						c.setType("Tel");
//						ContactSourceField sf = new ContactSourceField();
//						sf.setName("Private");
//						sf.setValue("0054");
//						sf.setAnzahl(1);
//						c.addSourceField(sf);
//						result.add(c);
						
						contactSourceTypesTree = contactSourceTypes2Tree.getTree(result);
						page2 = Page2.getInstance(thisClientEngine, contactSourceTypesTree);
						historyListener.setPage2(page2);
						History.newItem("page2", true);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("Problem beim Erstellen der ContactSourceTypes beim Client");
					}
				});
	}

	public ArrayList<Contact> getContacts() {
		return contacts;
	}
	
	public ArrayList<ContactGroup> getContactGroups() {
		return contactGroups;
	}
	
	public void sendExportFields(Tree exportTree){
		
	}
	
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		

		signInLink.getElement().setClassName("login-area");
		signInLink.setTitle("sign out");
		loginImage.getElement().setClassName("login-area");
		loginPanel.add(signInLink);
		HTML start = new HTML("<p>Startseite Inhalt YEEEHAAA</p>");
		RootPanel.get("content").add(start);
		RootPanel.get("loginPanelContainer").add(loginPanel);
		RootPanel.get("footer").clear();
		HTML footerimage = new HTML("<img src=\"images/1.jpg\">");
		RootPanel.get("footer").add(footerimage);
		final StringBuilder userEmail = new StringBuilder();
		greetingService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(final Throwable caught) {
				GWT.log("login -> onFailure");
			}

			@Override
			public void onSuccess(final LoginInfo result) {
				if (result.getName() != null && !result.getName().isEmpty()) {
					addGoogleAuthHelper();
					loadLogout(result);



				} else {
					loadLogin(result);

				}
				userEmail.append(result.getEmailAddress());
			}
		});
		
	}
	
	public void setSelections(ArrayList<Contact> contacts, ArrayList<ContactGroup> contactGroups){
		greetingService.setSelections(contacts, contactGroups, new AsyncCallback<Void>() {

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
}
