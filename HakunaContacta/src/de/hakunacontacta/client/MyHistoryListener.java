package de.hakunacontacta.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class MyHistoryListener implements ValueChangeHandler<String> {

	public Page1 page1;
	public Page2 page2;
	
	public void setPage1(Page1 page1){
		this.page1 = page1;
	}
	
	public void setPage2(Page2 page2){
		this.page2 = page2;
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println("Current State : " + event.getValue());
		if (event.getValue().equals("page1")){
    		goToPage1();
		}
		
		if (event.getValue().equals("page2")){
			goToPage2();
		}

	}
	
	public void goToPage1(){
		RootPanel.get("content").clear();
		RootPanel.get("content").add(page1);
		RootPanel.get("footer").clear();
		HTML footerimage = new HTML("<img src=\"images/2.jpg\">");
		RootPanel.get("footer").add(footerimage);	
	}
	public void goToPage2(){
		RootPanel.get("content").clear();
		RootPanel.get("content").add(page2);
		RootPanel.get("footer").clear();
		HTML footerimage = new HTML("<img src=\"images/3.jpg\">");
		RootPanel.get("footer").add(footerimage);	
	}

}
