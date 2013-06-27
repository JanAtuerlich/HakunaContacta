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


package de.hakunacontacta.client;


import java.util.Collection;
import java.util.HashSet;

import de.hakunacontacta.client.MyHistoryListener;
import de.hakunacontacta.shared.Contact;
import de.hakunacontacta.shared.ContactGroup;
import de.hakunacontacta.shared.LoginInfo;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;

 
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClientEngine implements EntryPoint {

	// TODO #05: add constants for OAuth2 (don't forget to update GOOGLE_CLIENT_ID)
	private Collection<Contact> contacts;
	private Collection<ContactGroup> contactGroups;
	private MyHistoryListener historyListener;
	private Page1 page1;
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
		signInLink.setText("Please, sign in with your Google Account");
		signInLink.setTitle("Sign in");
	}

	private void loadLogout(final LoginInfo loginInfo) {
		signInLink.setHref(loginInfo.getLogoutUrl());
		signInLink.setText(loginInfo.getName());
		signInLink.setTitle("Sign out");
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
							greetingService.getContactGroups(new AsyncCallback<Collection<ContactGroup>>(){
							
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									System.out.println("Fehler bei der Rückgabe der Gruppen.");
								}

								@Override
								public void onSuccess(Collection<ContactGroup> result) {
									contactGroups = result;
									greetingService.getContacts(new AsyncCallback<Collection<Contact>>() {

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											System.out.println("Fehler bei der Rückgabe der Kontakte.");
										}

										@Override
										public void onSuccess(Collection<Contact> result) {
											contacts = result;
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

	public Collection<Contact> getContacts() {
		return contacts;
	}
	
	public Collection<ContactGroup> getContactGroups() {
		return contactGroups;
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
		RootPanel.get("loginPanelContainer").add(loginPanel);
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
}
