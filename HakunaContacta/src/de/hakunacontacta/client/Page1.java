package de.hakunacontacta.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
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

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.shared.ContactData2Record;
import de.hakunacontacta.shared.ContactGroupData2Record;
import de.hakunacontacta.shared.ContactRecord;

public class Page1 extends Composite {
	
	private VerticalPanel page1 = new VerticalPanel();
	static private Page1 _instance = null;
	private static ClientEngine clientEngine;
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private ArrayList<Contact> contacts;
	private ArrayList<ContactGroup> contactGroups;
	
	private final ListGrid groupGrid = new ListGrid();
	private final ListGrid selectionGrid = new ListGrid();
	private final ListGrid contactGrid = new ListGrid();
	
	
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
		page1.setPixelSize(800, 400);
		contacts = clientEngine.getContacts();
		contactGroups = clientEngine.getContactGroups();

		
		//-----------------------------------------
		groupGrid.setWidth(200);
		groupGrid.setHeight(400);
		groupGrid.setEmptyMessage("Keine Gruppen vorhanden.");
		groupGrid.setShowAllRecords(true);
		groupGrid.setCanSort(true);
		groupGrid.setCanGroupBy(false);
		groupGrid.setSelectionType(SelectionStyle.SIMPLE);
		groupGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField groupnameField = new ListGridField("groupname",
				"Gruppenname");
		groupGrid.setFields(groupnameField);
		
		groupGrid.setData(ContactGroupData2Record.getNewRecords(contactGroups));
		
		contactGrid.setWidth(200);
		contactGrid.setHeight(400);
		contactGrid.setBorder("1px solid #ABABAB");
		contactGrid.setEmptyMessage("Keine Kontakte vorhanden.");
		contactGrid.setShowAllRecords(true);
		contactGrid.setCanSort(true);
		contactGrid.setCanGroupBy(false);
		contactGrid.setSelectionType(SelectionStyle.SIMPLE);
		contactGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		for (ContactRecord contactRecord: ContactData2Record.getNewRecords(contacts)){
			contactGrid.addData(contactRecord);
			if (contactRecord.getAttributeAsBoolean("selected")){
				contactGrid.selectRecord(contactRecord);
			}
		}

		ListGridField nameField = new ListGridField("name", "Kontaktnamen");
		contactGrid.setFields(nameField);

		selectionGrid.setWidth(250);
		selectionGrid.setHeight(400);
		selectionGrid.setBorder("1px solid #ABABAB");
		selectionGrid.setEmptyMessage("Keine Kontakte ausgew\u00E4hlt.");
		selectionGrid.setShowAllRecords(true);
		selectionGrid.setCanGroupBy(false);
		ListGridField selectedContactsField = new ListGridField("name",
				"Kontaktnamen");
		selectionGrid.setFields(selectedContactsField);
		
		
		contactGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getState() == true){
					contactSelection(event.getRecord().getAttribute("etag"), true);
					contactGrid.selectRecord(event.getRecord());
				}
				else { 
					contactSelection(event.getRecord().getAttribute("etag"), false);
					contactGrid.deselectRecord(event.getRecord());
				}
				reloadSelectionGrid();
			}
		});
		
		groupGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				ArrayList<Contact> contactsToShow;
				for (ContactGroup contactGroup: contactGroups){
					if (contactGroup.getName().equals(event.getRecord().getAttribute("groupname"))){
						contactsToShow = contactGroup.getContacts();
						reloadContactGrid(contactsToShow);
					}
				}
					
			}
		});
		
		groupGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getState()){
					groupSelection(event.getRecord().getAttribute("groupname"), true);
				} else {
					groupSelection(event.getRecord().getAttribute("groupname"), false);
				}
				reloadSelectionGrid();
			}
		});
		
		
		contactGrid.addStyleName("contactGrid");
		selectionGrid.addStyleName("selectionGrid");
		
		HTML gridHeaders = new HTML("<div id=\"gridHeader1\">Gruppenauswahl</div><div id=\"gridHeader2\">Kontaktauswahl</div><div id=\"gridHeader3\">Ausgew\u00E4hlte Kontakte</div>");
		gridHeaders.addStyleName("gridHeaders");
		
		HStack grids = new HStack(3);
		grids.addStyleName("grids");
		grids.addMember(groupGrid);
		grids.addMember(contactGrid);
		grids.addMember(selectionGrid);
		grids.draw();
		
		mainPanel.add(gridHeaders);
		mainPanel.add(grids);
		mainPanel.addStyleName("mainPanel");
		
		//------------------------------------------
		Button button = new Button("Weiter");
		button.addStyleName("next");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				clientEngine.setSelections(contacts, contactGroups);
				clientEngine.createPage2();			}
		});
		page1.add(mainPanel);
		page1.add(button);
		page1.addStyleName("page1");
	}
	
	private void contactSelection(String etag, boolean selected){
		for(Contact contact: contacts){
			if(contact.geteTag().equals(etag))
				contact.setSelected(selected);
		}
	}
	
	private void groupSelection(String name, boolean selected){
		for (ContactGroup contactGroup: contactGroups){
			if(contactGroup.getName().equals(name)){
				for (Contact contact: contactGroup.getContacts()){
					contact.setSelected(selected);
				}
			}
		}
	}
	
	private void reloadSelectionGrid(){
		for(ListGridRecord record: selectionGrid.getRecords()){
			selectionGrid.removeData(record);
		}
		for (ContactRecord contactRecord: ContactData2Record.getNewRecords(contacts)){
			if (contactRecord.getAttributeAsBoolean("selected")){
					selectionGrid.addData(contactRecord);
			}
		}
	}
	
	private void reloadContactGrid(ArrayList<Contact> contactsToShow){
		for(ListGridRecord record: contactGrid.getRecords()){
			contactGrid.removeData(record);
		}
		for (ContactRecord contactRecord: ContactData2Record.getNewRecords(contactsToShow)){
			contactGrid.addData(contactRecord);
			if (contactRecord.getAttributeAsBoolean("selected")){
				contactGrid.selectRecord(contactRecord);
			}
		}
	}
}
