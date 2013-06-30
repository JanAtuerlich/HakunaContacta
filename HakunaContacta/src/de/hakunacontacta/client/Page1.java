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
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;

import de.hakunacontacta.contactModule.Contact;
import de.hakunacontacta.contactModule.ContactGroup;
import de.hakunacontacta.shared.ContactData2Record;
import de.hakunacontacta.shared.ContactGroupData2Record;
import de.hakunacontacta.shared.ContactGroupRecord;
import de.hakunacontacta.shared.ContactRecord;

public class Page1 extends Composite {
	
	private VerticalPanel page1 = new VerticalPanel();
	static private Page1 _instance = null;
	private static ClientEngine clientEngine;
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private ContactRecord[] contacts;
	private ContactGroupRecord[] contactGroups;
	
	private final ListGrid groupGrid = new ListGrid();
	private final ListGrid selectionGrid = new ListGrid();
	private final ListGrid contactGrid = new ListGrid();
	
	private boolean select = true;
	
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
		ArrayList<Contact> importContacts = clientEngine.getContacts();
		ArrayList<ContactGroup> importContactGroups = clientEngine.getContactGroups();
		
		contacts = ContactData2Record.getNewRecords(importContacts);
		contactGroups = ContactGroupData2Record.getNewRecords(importContactGroups);
		
		
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
		
		loadGroupGrid();
		
		contactGrid.setWidth(200);
		contactGrid.setHeight(400);
		contactGrid.setBorder("1px solid #ABABAB");
		contactGrid.setEmptyMessage("Keine Kontakte vorhanden.");
		contactGrid.setShowAllRecords(true);
		contactGrid.setCanSort(true);
		contactGrid.setCanGroupBy(false);
		contactGrid.setSelectionType(SelectionStyle.SIMPLE);
		contactGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		loadContactGrid("ALL");

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
		
		
		groupGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			public void onSelectionChanged(SelectionEvent event) {
				if (select){
					ContactGroupRecord record = (ContactGroupRecord) event.getRecord();
					if (event.getState()) {
						groupGrid.selectRecord(record);
						groupSelection(record.getAttributeAsString("groupname"), true);
					} else {
						groupGrid.deselectRecord(record);
						groupSelection(record.getAttributeAsString("groupname"), false);
					}
					checkGroupsForSelection();
					loadGroupGrid();
					loadSelectionGrid();
					test();
				}
			}
		});
		
		
		groupGrid.addRecordClickHandler(new RecordClickHandler() {
			
			public void onRecordClick(RecordClickEvent event) {
				loadContactGrid(event.getRecord().getAttributeAsString("groupname"));
			}
		});
		
		
		contactGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			public void onSelectionChanged(SelectionEvent event) {
				if (select){
					ContactRecord record = (ContactRecord) event.getRecord();
					if (event.getState()) {
						contactGrid.selectRecord(record);
						contactSelection(record.getAttributeAsString("etag"), true);
					} else {
						contactGrid.deselectRecord(record);
						contactSelection(record.getAttributeAsString("etag"), false);
					}
					checkGroupsForSelection();
					loadGroupGrid();
					loadSelectionGrid();
					test();
				}
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

//			clientEngine.setSelections(contacts, contactGroups);
			}
		});
		page1.add(mainPanel);
		page1.add(button);
		page1.addStyleName("page1");
	}
	
	private void loadGroupGrid(){
		select = false;
		groupGrid.setData(contactGroups);
		for (ContactGroupRecord contactGroupRecord : contactGroups) {
			if(contactGroupRecord.getSelected()){
				groupGrid.selectRecord(contactGroupRecord);
			}
		}
		select = true;
	}
	
	private void loadContactGrid(String groupName){
		select = false;
		if (groupName.equals("ALL")) {
			contactGrid.setData(contacts);
			for (ContactRecord record : contacts) {
				if (record.getSelected()) {
					contactGrid.selectRecord(record);
				}				
			}
		} else {
			contactGrid.setData(new ContactRecord[]{});
			for (ContactRecord record : contacts) {
				if(record.getAttributeAsString("groups").contains(groupName)){
					contactGrid.addData(record);
					if(record.getSelected()){
						contactGrid.selectRecord(record);
					}
				}
			}
		}
		select = true;
	}
	
	private void loadSelectionGrid(){
		selectionGrid.setData(new ContactRecord[]{});
		for (ContactRecord contactRecord : contacts) {
			if (contactRecord.getSelected()) {
				selectionGrid.addData(contactRecord);
			}			
		}
	}
	
	private void groupSelection(String groupname, boolean selected){
		for (ContactGroupRecord groupRecord : contactGroups) {
			if (groupRecord.getAttributeAsString("groupname").equals(groupname)) {
				groupRecord.setSelected(selected);
				String groupContacts = groupRecord.getAttributeAsString("contacts");
				for (ContactRecord contactRecord: contacts){
					if (groupContacts.contains(contactRecord.getAttributeAsString("etag"))) {
						contactRecord.setSelected(selected);
					}
				}
			}
		}
	}
	
	private void contactSelection(String etag, boolean selected){
		for (ContactRecord contactRecord : contacts) {
			if(contactRecord.getAttributeAsString("etag").equals(etag)){
				contactRecord.setSelected(selected);
			}
		}
	}
	
	private void checkGroupsForSelection(){
		for (ContactGroupRecord groupRecord : contactGroups) {
			String contactsInGroup = groupRecord.getAttributeAsString("contacts");
			boolean allContactsAreSelected = true;
			boolean isEmpty = true;
			for(ContactRecord contactRecord : contacts){
				if (contactsInGroup.contains(contactRecord.getEtag())){
					isEmpty = false;
					if (!contactRecord.getSelected()){
						allContactsAreSelected = false;
					}
				}	
			}
			if(!isEmpty){
				groupRecord.setSelected(allContactsAreSelected);
			}
		}
	}
	
	private void test(){
		for (ContactGroupRecord record : contactGroups) {
			System.out.println(record.getAttributeAsString("groupname") + ": \t\t" + (record.getSelected()? "X" : ""));
		}
		for (ContactRecord record : contacts) {
			System.out.println(record.getAttributeAsString("name") + ": \t\t" + (record.getSelected()? "X" : ""));
		}
	}
	
	
}
