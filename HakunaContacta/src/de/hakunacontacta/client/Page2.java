package de.hakunacontacta.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
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
		Button button = new Button("Back");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("page1", true); 
			}
		});
		page2.add(content);
		page2.add(button);
		
	}
}
