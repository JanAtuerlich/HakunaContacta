package de.hakunacontacta.shared;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import com.google.api.client.extensions.appengine.http.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public interface Constant {

	final static Level LOG_LEVEL = Level.FINE;
	
	/* session attributes */
	final static String TARGET_URI = "TargetUri";
	
	final static String AUTH_USER_ID = "UserEmail";
	
	final static String AUTH_USER_EMAIL = "UserEmail";
	
	final static String AUTH_USER_NICKNAME = "UserNickname";
	
	final static String GOOG_CREDENTIAL_STORE = "GoogleCredentialStore";
	
	final static String GOOG_CREDENTIAL = "GoogleCredentialL";
	
//Offline
	final static String GOOGLE_CLIENT_ID = "21423817686-ganfq9ap8qed1bffutk67eib8mvdsud6.apps.googleusercontent.com"; 
	final static String APP_LINK = "http://127.0.0.1:8888/HakunaContacta.html?gwt.codesvr=127.0.0.1:9997"; 
	
//	Online
	//final static String GOOGLE_CLIENT_ID = "163881565295-ngpkhb4hie9emdmki7mta52sitbiinbt.apps.googleusercontent.com"; 
	//final static String APP_LINK = "http://hakuna-contacta.appspot.com/HakunaContacta.html"; 
	
	/* end of session attributes */
	
	final static HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();
	
	final static JsonFactory JSON_FACTORY = new JacksonFactory();
	
	final static String AUTH_RESOURCE_LOC = "/client_secrets.json";
	
	final static String GOOGLE_CONTACT_IS_HOME = "http://schemas.google.com/g/2005#home";
	
	final static String GOOGLE_CONTACT_IS_WORK = "http://schemas.google.com/g/2005#work";
	
	final static String GOOGLE_CONTACT_IS_PAGER = "http://schemas.google.com/g/2005#pager";
	
	final static String GOOGLE_CONTACT_IS_MOBILE = "http://schemas.google.com/g/2005#mobile";
	
	final static String GOOGLE_CONTACT_IS_MAIN = "http://schemas.google.com/g/2005#main";
	
	final static String GOOGLE_CONTACT_IS_WORK_FAX = "http://schemas.google.com/g/2005#work_fax";
	
	final static String GOOGLE_CONTACT_IS_HOME_FAX = "http://schemas.google.com/g/2005#home_fax";
	
	final static String GOOGLE_WEBSITE_IS_PROFILE = "profile";
	
	final static String GOOGLE_WEBSITE_IS_BLOG = "blog";
	
	final static String GOOGLE_WEBSITE_IS_HOME_PAGE = "home-page";
	
	final static String GOOGLE_WEBSITE_IS_WORK = "work";
	
	final static String GOOGLE_IM_IS_GOOGLE_TALK = "http://schemas.google.com/g/2005#GOOGLE_TALK";
	
	final static String GOOGLE_IM_IS_AIM = "http://schemas.google.com/g/2005#AIM";
	
	final static String GOOGLE_IM_IS_YAHOO = "http://schemas.google.com/g/2005#YAHOO";
	
	final static String GOOGLE_IM_IS_SKYPE = "http://schemas.google.com/g/2005#SKYPE";
	
	final static String GOOGLE_IM_IS_QQ = "http://schemas.google.com/g/2005#QQ";
	
	final static String GOOGLE_IM_IS_MSN = "http://schemas.google.com/g/2005#MSN";
	
	final static String GOOGLE_IM_IS_ICQ = "http://schemas.google.com/g/2005#ICQ";
	
	final static String GOOGLE_IM_IS_JABBER = "http://schemas.google.com/g/2005#JABBER";
	
	
  final static List<String> SCOPES = Arrays.asList(
      "http://www.google.com/m8/feeds");
	
	// Use for running on GAE
	//final static String OATH_CALLBACK = "http://tennis-coachrx.appspot.com/authSub";
	
	// Use for local testing
	final static String OATH_CALLBACK = "http://localhost:8889/authSub";
	
}
