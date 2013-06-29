package de.hakunacontacta.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Page2 extends Composite {
	private VerticalPanel page2 = new VerticalPanel();
	static private Page2 _instance = null;
	
	private Page2(){
		initPage();
		initWidget(page2);
	}

	public static Page2 getInstance(){
        if(null == _instance) {
        	_instance = new Page2();
        }
        return _instance;
	}

	private void initPage() {
		page2.setBorderWidth(2);
		page2.setPixelSize(150, 150);
		HTML content = new HTML("This is page2. Click to move to back page1");
		Button button = new Button("Create Export");
		
		final String dateiendung = "csv";
		final String encoded = "QXVmZ2FiZW46DQoNCi0gRmlsZSBhbiBDbGllbnQgc2VuZGVuLCBiaXNoZXIgZ2lidHMgZXMgZWluZW4gZmVydGlnZW4gU3RyaW5nLiAtPiBSZWNoZXJjaGUsIFByb3RvdHlwZQ0KLSBHVUkgMyBtYWNoZW4NCi0gTWV0aG9kZW4gaW4gQ2xpZW50RW5naW5lIHp1ciBWZXJmw7xndW5nIHN0ZWxsZW4NCi0gTG9nbw0KLSBBbGxnLiBIb21lcGFnZSwgYnp3IGVyc3RlIFBhZ2UgDQotIA0KLSA=";
		
		
		
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final HTML html = new HTML("<a download=\"Contactexport." + dateiendung + "\" href=data:application/" +dateiendung + ";base64," + encoded + ">Download</a>");
				//final HTML html = new HTML("<a download=\"MyFile." + dateiendung + "\" href=data:application/vnd.ms-excel;base64,77u/Vm9ybmFtZTtOYWNobmFtZTtBZHJlc3NlO1RlbGVmb25udW1tZXI7DQpNYXJjZWw7UHLDvGdlbDtUb25hdXN0ci4gNDEgNzIxODkgVsO2aHJpbmdlbjsgMDE3NjYxNjc3NTAxOw0KTWF4OyBNdXN0ZXJtYW5uOyBNdXN0ZXJzdHIuIDEgNzg0NjcgS29uc3Rhbno7IDAxMjU2NDU0NTU7DQo=>Download</a>");
				page2.add(html);
				
				

//				String uri ="<a download=\"MyFile.csv\" href=data:text/csv;charset=utf-8,\"test\">Download</a>";
//				String uri ="href=data:text/csv;charset=utf-8,\"test\"";
//				
//				Window.open(uri, "TEST", "");
//				
//				History.newItem("page1", true); 
			}
		});
		page2.add(content);
		page2.add(button);

	}
}
