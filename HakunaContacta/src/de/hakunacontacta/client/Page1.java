package de.hakunacontacta.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RemoveRecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;

import de.hakunacontacta.shared.ContactGroupRecord;
import de.hakunacontacta.shared.ContactRecord;

/**
 * @author MB
 * @category GUI
 * 
 */
public class Page1 extends Composite {

	private VerticalPanel page1 = new VerticalPanel();
	private ClientEngine clientEngine;
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

	/**
	 * Der Konstruktor von Page1 erwartet eine ClientEngine, welche der
	 * Kontaktpunkt zur GreetingServiceImpl und damit zur Server-Seite ist.
	 * 
	 * @param cEngine
	 *            ist der Kontaktpunkt des Clients zum Server
	 */
	public Page1(ClientEngine cEngine) {
		clientEngine = cEngine;
		initPage();
		initWidget(page1);
	}

	/**
	 * Diese Methode erstellt alle Elemente, Widgets und Handler auf der ersten
	 * Seite und verwaltet die Kommunikation mit der ClientEngine
	 */
	private void initPage() {

		page1.setPixelSize(1000, 400);

		// Die Kontakte und Gruppen werden bezogen (in Form von Record Objekten)
		contacts = clientEngine.getContactRecords();
		contactGroups = clientEngine.getContactGroupRecord();

		// Dieses Grid beinhaltet alles Gruppen
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
		groupGrid.setSortDirection(SortDirection.ASCENDING);

		loadGroupGrid();

		// Dieses Grid beinhaltet alle Kontakte der Gruppe, die markiert ist
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
		contactGrid.setSortDirection(SortDirection.ASCENDING);

		// Standardmößig werden alle Kontakte angezeigt
		loadContactGrid("ALL");

		// Dieses Grid beinhaltet alle Kontakte, die selektiert wurden, anfangs
		// ist dieses leer
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
		selectionGrid.setSortDirection(SortDirection.ASCENDING);
		loadSelectionGrid();

		// Dieser Handler wird aufgerufen, wenn im Group Grid eine Gruppe
		// angehakt wird
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

		// Dieser Handler wird aufgerufen, wenn im Group Grid eine Gruppe
		// markiert wird
		groupGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				markedGroup = event.getRecord().getAttribute("groupname");
				loadContactGrid(markedGroup);
			}
		});

		// Dieser Handler wird aufgerufen, wenn im Kontakt Grid ein Kontakt
		// angehakt wird
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

		// Dieser Handler wird aufgerufen, wenn im Selection Grid ein Kontakt
		// entfernt wird
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

		// Ein Weiter-Button wird eingefügt, um auf die nächste Seite zu
		// wechseln
		Button button = new Button("Weiter <span id=\"arrow\">\u2192</span>");
		button.setStyleName("next");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Durch das Aufrufen der nächsten Seite werden alle
				// Selektierung an den ContactManager gesendet und dort
				// übernommen
				clientEngine.setSelections(contacts, contactGroups);
			}
		});

		page1.add(mainPanel);
		HTML info = new HTML("<div id=\"p1info\">Bitte w\u00E4hlen Sie die Kontakte aus die Sie exportieren m\u00F6chten!</div>");
		page1.add(info);
		page1.add(button);
		page1.setStyleName("page1");
	}

	/**
	 * Diese Methode lädt die Gruppen neu
	 */
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

	/**
	 * Diese Methode lädt die Kontakte neu, entweder alle Kontakte oder nur die
	 * einer bestimmten Gruppe
	 * 
	 * @param groupName
	 *            Falls groupName mit "ALL" beleget ist werden alle Kontakte neu
	 *            geladen, ansonsten nur die Kontakte diese Gruppe
	 */
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
				if (groups.contains(groupName + ",")) {
					contactGrid.addData(record);
					if (record.getSelected()) {
						contactGrid.selectRecord(record);
					}
				}
			}
		}
		select = true;
	}

	/**
	 * Diese Methode lädt die selektierten Kontakte neu
	 */
	private void loadSelectionGrid() {
		selectionGrid.setData(new ContactRecord[] {});
		for (ContactRecord contactRecord : contacts) {
			if (contactRecord.getSelected()) {
				selectionGrid.addData(contactRecord);
			}
		}
	}

	/**
	 * Diese Methode de/selektiert die ausgewählte Gruppe und alle darin
	 * enthaltenen Kontakte
	 * 
	 * @param groupname
	 *            de/selektiert alle Kontakte dieser Gruppe
	 * @param selected
	 *            true setzt alle Kontakte der Gruppe auf selektiert, false
	 *            deselektiert alle Kontakte dieser Gruppe
	 */
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

	/**
	 * Diese Methode de/selektiert einen einzelnen Kontakt
	 * 
	 * @param etag
	 *            der Kontakt mit diesem eindeutigen Identifikator wird
	 *            de/selektiert
	 * @param selected
	 *            true setzt dem Kontakt auf selektiert, false deselektiert den
	 *            Kontakt
	 */
	private void contactSelection(String etag, boolean selected) {
		for (ContactRecord contactRecord : contacts) {
			if (contactRecord.getEtag().equals(etag)) {
				contactRecord.setSelected(selected);
			}
		}
	}

	/**
	 * Diese Methode überprüft alle Kontakte einer Gruppe, sind alle Kontakte
	 * selektiert, wird auch die Gruppe selektiert
	 */
	private void checkGroupsForSelection() {
		for (ContactGroupRecord groupRecord : contactGroups) {
			String contactsInGroup = groupRecord.getAttributeAsString("contacts") + ",";
			boolean allContactsAreSelected = true;
			boolean isEmpty = true;
			for (ContactRecord contactRecord : contacts) {
				if (contactsInGroup.contains(contactRecord.getEtag() + ",")) {
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
