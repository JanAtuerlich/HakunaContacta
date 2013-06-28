package de.hakunacontacta.client;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;

import de.hakunacontacta.contactmodule.Contact;
import de.hakunacontacta.contactmodule.ContactGroup;
import de.hakunacontacta.shared.ContactData2Record;
import de.hakunacontacta.shared.ContactGroupData2Record;
import de.hakunacontacta.shared.ContactGroupRecord;
import de.hakunacontacta.shared.ContactRecord;

public class Page1 extends Composite {
	
	private VerticalPanel page1 = new VerticalPanel();
	static private Page1 _instance = null;
	private static ClientEngine clientEngine;
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private Page1(){
		initPage();
		initWidget(page1);
	}

	public static Page1 getInstance(ClientEngine cEngine){
		clientEngine = cEngine;
        if(null == _instance) {
        	_instance = new Page1();
        }
        return _instance;
	}

	private void initPage() {
		page1.setBorderWidth(2);
		page1.setPixelSize(150, 150);
		HTML content = new HTML("This is page1. Click to move to page2");
		Collection<Contact> contacts = clientEngine.getContacts();
		Collection<ContactGroup> contactGroups = clientEngine.getContactGroups();

		
		//-----------------------------------------
		//addedthis
		final ListGrid groupGrid = new ListGrid();
		groupGrid.setWidth(250);
		groupGrid.setHeight(300);
		groupGrid.setShowAllRecords(true);
		groupGrid.setCanSort(true);
		groupGrid.setCanGroupBy(false);
		groupGrid.setSelectionType(SelectionStyle.SIMPLE);
		groupGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField groupnameField = new ListGridField("groupname",
				"Gruppenname");
		groupGrid.setFields(groupnameField);
		
		groupGrid.setData(ContactGroupData2Record.getNewRecords(contactGroups));
		
		final ListGrid contactGrid = new ListGrid();
		contactGrid.setWidth(250);
		contactGrid.setHeight(300);
		contactGrid.setOverflow(Overflow.AUTO);
		contactGrid.setShowAllRecords(true);
		contactGrid.setCanSort(true);
		contactGrid.setCanGroupBy(false);
		contactGrid.setSelectionType(SelectionStyle.SIMPLE);
		contactGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		contactGrid.setData(ContactData2Record.getNewRecords(contacts));

		ListGridField nameField = new ListGridField("name", "Kontaktnamen");
		contactGrid.setFields(nameField);

		final ListGrid selectionGrid = new ListGrid();
		selectionGrid.setWidth(250);
		selectionGrid.setHeight(300);
		selectionGrid.setShowAllRecords(true);
		ListGridField selectedCountriesField = new ListGridField("name",
				"Kontaktnamen");
		selectionGrid.setFields(selectedCountriesField);

		contactGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getState() == true) {
					if (!selectionGrid.getRecordList().contains(
							event.getRecord())) {
						selectionGrid.addData(event.getRecord());
					}
				} else if(event.getState() == false) {	
					for (ListGridRecord selectedRecord : selectionGrid.getRecords()) {
						if(!selectionGrid.getRecordList().contains(
								event.getRecord())){
							selectionGrid.removeData(event.getRecord());
						}
					}
				}
			}
		});

		groupGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				contactGrid.setData(new ListGridRecord[] {});
				ContactGroupRecord record = (ContactGroupRecord) event.getRecord();
				for (String contact : record.getAttributeAsStringArray("contacts")) {
					for (ContactRecord contactrecord : ContactData2Record.getRecords()) {
						if (contact.equals(contactrecord.getAttributeAsString("etag"))) {
							contactGrid.addData(contactrecord);
							contactGrid.selectRecord(contactrecord);
						}
					}
				}
			}
		});
		groupGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getState() == true) {
					for (ContactRecord contactrecord : ContactData2Record.getRecords()) {
						for (String groupname : contactrecord.getAttributeAsStringArray("groups")) {
							if (groupname.equals(event.getRecord()
									.getAttributeAsString("groupname"))) {
								if (!selectionGrid.getRecordList().contains(contactrecord)) {
									System.out.println(contactrecord.getAttribute("name"));
									selectionGrid.addData(contactrecord);
								}
							}
						}
					}
				} 
				else if (event.getState() == false) {
					for (ContactRecord contactrecord : ContactData2Record.getRecords()) {
						if (contactrecord.getAttributeAsString("groups")
								.contains(
										event.getRecord().getAttributeAsString(
												"groupname"))) {
							selectionGrid.removeData(contactrecord);
						}
					}
				}
			}

		});

		contactGrid.setStyleName("grid1");
		selectionGrid.setStyleName("grid2");

		HStack grids = new HStack(3);
		grids.addMember(groupGrid);
		grids.addMember(contactGrid);
		grids.addMember(selectionGrid);
		grids.draw();

		mainPanel.add(grids);
		mainPanel.addStyleName("mainPanel");
		RootPanel.get("content").add(mainPanel);
		
		
		
		
		
		
		//------------------------------------------
		Button button = new Button("Forward");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get().clear();
				History.newItem("page2", true); 
			}
		});
		page1.add(content);
		page1.add(button);
	}
}
