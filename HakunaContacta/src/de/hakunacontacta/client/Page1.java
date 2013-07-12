package de.hakunacontacta.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.AutoFitEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.HeaderDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.HeaderDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;

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

	private String markedGroup;
	private boolean select = true;

	public Page1() {
		initPage();
		initWidget(page1);
	}

	public static Page1 getInstance(ClientEngine cEngine) {
		clientEngine = cEngine;
		if (null == _instance) {
			_instance = new Page1();
		}
		return _instance;
	}

	private void initPage() {
		page1.setPixelSize(800, 400);
		contacts = clientEngine.getContactRecords();
		contactGroups = clientEngine.getContactGroupRecord();

		// -----------------------------------------
		groupGrid.setWidth(300);
		groupGrid.setHeight(400);
		groupGrid.setEmptyMessage("Keine Gruppen vorhanden.");
		groupGrid.setShowHeaderContextMenu(false);
		groupGrid.setShowHeaderMenuButton(false);
		groupGrid.setShowAllRecords(true);
		groupGrid.setCanSort(true);
		groupGrid.setCanGroupBy(false);
		groupGrid.setSelectionType(SelectionStyle.SIMPLE);
		groupGrid.setShowSelectedStyle(false);
		groupGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		ListGridField groupnameField = new ListGridField("displayedName", "Gruppenname");
		groupGrid.setFields(groupnameField);

		loadGroupGrid();

		contactGrid.setWidth(200);
		contactGrid.setHeight(400);
		contactGrid.setBorder("1px solid #ABABAB");
		contactGrid.setEmptyMessage("Keine Kontakte vorhanden.");
		contactGrid.setShowAllRecords(true);
		contactGrid.setShowHeaderContextMenu(false);
		contactGrid.setShowHeaderMenuButton(false);
		contactGrid.setCanSort(true);
		contactGrid.setCanGroupBy(false);
		contactGrid.setSelectionType(SelectionStyle.SIMPLE);
		contactGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		contactGrid.setShowSelectedStyle(false);
		ListGridField nameField = new ListGridField("name", "Kontaktnamen");
		contactGrid.setFields(nameField);

		loadContactGrid("ALL");

		selectionGrid.setWidth(250);
		selectionGrid.setHeight(400);
		selectionGrid.setBorder("1px solid #ABABAB");
		selectionGrid.setEmptyMessage("Keine Kontakte ausgew\u00E4hlt.");
		selectionGrid.setShowHeaderContextMenu(false);
		selectionGrid.setShowHeaderMenuButton(false);
		selectionGrid.setShowAllRecords(true);
		selectionGrid.setCanRemoveRecords(true);
		selectionGrid.setCanGroupBy(false);
		ListGridField selectedContactsField = new ListGridField("name", "Kontaktnamen");
		selectionGrid.setFields(selectedContactsField);

		groupGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				if (select) {
					ContactGroupRecord record = (ContactGroupRecord) event.getRecord();
					if (event.getState()) {
						groupGrid.selectRecord(record);
						groupSelection(record.getGroupname(), true);
					} else {
						groupGrid.deselectRecord(record);
						groupSelection(record.getGroupname(), false);
					}
					checkGroupsForSelection();
					loadGroupGrid();
					loadSelectionGrid();
				}
			}
		});

		groupGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				markedGroup = event.getRecord().getAttribute("groupname");
				loadContactGrid(markedGroup);
			}
		});

		contactGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			public void onSelectionChanged(SelectionEvent event) {
				if (select) {
					ContactRecord record = (ContactRecord) event.getRecord();
					if (event.getState()) {
						contactGrid.selectRecord(record);
						contactSelection(record.getEtag(), true);
					} else {
						contactGrid.deselectRecord(record);
						contactSelection(record.getEtag(), false);
					}
					checkGroupsForSelection();
					loadGroupGrid();
					loadSelectionGrid();
				}
			}
		});

		selectionGrid.addRemoveRecordClickHandler(new RemoveRecordClickHandler() {

			@Override
			public void onRemoveRecordClick(RemoveRecordClickEvent event) {
				ContactRecord record = (ContactRecord) selectionGrid.getRecord(event.getRowNum());
				contactSelection(record.getEtag(), false);
				checkGroupsForSelection();
				loadGroupGrid();
				loadContactGrid(markedGroup);
			}
		});

		contactGrid.setStyleName("contactGrid");
		selectionGrid.setStyleName("selectionGrid");

		HTML gridHeaders1 = new HTML("<div id=\"gridHeader1\">Gruppenauswahl</div><div id=\"gridHeader2\">Kontaktauswahl</div><div id=\"gridHeader3\">Ausgew\u00E4hlte Kontakte</div>");
		gridHeaders1.setStyleName("gridHeaders");

		HStack grids = new HStack(3);
		grids.setStyleName("grids1");
		grids.addMember(groupGrid);
		grids.addMember(contactGrid);
		grids.addMember(selectionGrid);
		grids.draw();

		mainPanel.add(gridHeaders1);
		mainPanel.add(grids);
		mainPanel.setStyleName("mainPanel");

		// ------------------------------------------
		Button button = new Button("Weiter <span id=\"arrow\">\u2192</span>");
		button.setStyleName("next");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				clientEngine.setSelections(contacts, contactGroups);
			}
		});

		page1.add(mainPanel);
		page1.add(button);
		page1.setStyleName("page1");
	}

	private void addHeaderDoubleClickHandler(HeaderDoubleClickHandler headerDoubleClickHandler) {
		// TODO Auto-generated method stub

	}

	private void loadGroupGrid() {
		select = false;
		groupGrid.setData(contactGroups);
		for (ContactGroupRecord contactGroupRecord : contactGroups) {
			if (contactGroupRecord.getSelected()) {
				groupGrid.selectRecord(contactGroupRecord);
			}
		}
		select = true;
	}

	private void loadContactGrid(String groupName) {
		select = false;
		if (groupName.equals("ALL")) {
			contactGrid.setData(contacts);
			for (ContactRecord record : contacts) {
				if (record.getSelected()) {
					contactGrid.selectRecord(record);
				}
			}
		} else {
			contactGrid.setData(new ContactRecord[] {});
			for (ContactRecord record : contacts) {
				String groups = record.getAttributeAsString("groups") + ",";
				if(groups.contains(groupName + ",")){
					contactGrid.addData(record);
					if (record.getSelected()) {
						contactGrid.selectRecord(record);
					}
				}
			}
		}
		select = true;
	}

	private void loadSelectionGrid() {
		selectionGrid.setData(new ContactRecord[] {});
		for (ContactRecord contactRecord : contacts) {
			if (contactRecord.getSelected()) {
				selectionGrid.addData(contactRecord);
			}
		}
	}

	private void groupSelection(String groupname, boolean selected) {
		for (ContactGroupRecord groupRecord : contactGroups) {
			if (groupRecord.getGroupname().equals(groupname)) {
				groupRecord.setSelected(selected);
				String groupContacts = groupRecord.getAttributeAsString("contacts") + ",";
				for (ContactRecord contactRecord : contacts) {
					if (groupContacts.contains(contactRecord.getEtag() + ",")) {
						contactRecord.setSelected(selected);
					}
				}
			}
		}
	}

	private void contactSelection(String etag, boolean selected) {
		for (ContactRecord contactRecord : contacts) {
			if (contactRecord.getEtag().equals(etag)) {
				contactRecord.setSelected(selected);
			}
		}
	}

	private void checkGroupsForSelection() {
		for (ContactGroupRecord groupRecord : contactGroups) {
			String contactsInGroup = groupRecord.getAttributeAsString("contacts") + ",";
			boolean allContactsAreSelected = true;
			boolean isEmpty = true;
			for(ContactRecord contactRecord : contacts){
				if (contactsInGroup.contains(contactRecord.getEtag() + ",")){
					isEmpty = false;
					if (!contactRecord.getSelected()) {
						allContactsAreSelected = false;
					}
				}
			}
			if (!isEmpty) {
				groupRecord.setSelected(allContactsAreSelected);
			}
		}
	}

}
