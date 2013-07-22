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

	// TODO #10: create login helper methods in service asynchronous interface	
	void getUserEmail(String token, AsyncCallback<String> callback);

	void login(String requestUri, AsyncCallback<LoginInfo> asyncCallback);

	void loginDetails(String token, AsyncCallback<LoginInfo> asyncCallback);

	void getContacts(AsyncCallback<ArrayList<Contact>> callback);

	void getContactGroups(AsyncCallback<ArrayList<ContactGroup>> callback);

	void setSelections(ArrayList<Contact> contacts,
			ArrayList<ContactGroup> contactGroups, AsyncCallback<Void> callback);
	
	void getContactSourceTypes(AsyncCallback<ArrayList<ContactSourceType>> callback);
	
	void setExportFields(ArrayList<ExportField> exportFields, ExportTypeEnum type, AsyncCallback<Void> callback);
	
	void getExportFields(ExportTypeEnum type, boolean firstload, AsyncCallback<ArrayList<ExportField>> callback);
	
	void getFile(AsyncCallback<String> callback);
	
	void exitSession(AsyncCallback<Void> callback);

	void exitSession(AsyncCallback<Void> callback);

}
