package de.hakunacontacta.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

public class Page2 extends Composite {
	private VerticalPanel page2 = new VerticalPanel();
	static private Page2 _instance = null;
	private static ClientEngine clientEngine;
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox addExportfieldTextBox = new TextBox();
	private Button addExportfieldButton = new Button("Add");
	private static Tree thisContactSourceTypesTree;

	private Page2() {
		initPage();
		initWidget(page2);
	}

	public static Page2 getInstance(ClientEngine cEngine, Tree contactSourceTypesTree) {
		thisContactSourceTypesTree = contactSourceTypesTree;
		clientEngine = cEngine;
		if (null == _instance) {
			_instance = new Page2();
		}
		return _instance;
	}

	private void initPage() {
		System.out.println("Check from Page2: " + clientEngine.check);
		page2.setPixelSize(500, 350);
		Button exportButton = new Button("Create Export");
		exportButton.addStyleName("exportButton");

		final String dateiendung = "csv";
		final String encoded = "QXVmZ2FiZW46DQoNCi0gRmlsZSBhbiBDbGllbnQgc2VuZGVuLCBiaXNoZXIgZ2lidHMgZXMgZWluZW4gZmVydGlnZW4gU3RyaW5nLiAtPiBSZWNoZXJjaGUsIFByb3RvdHlwZQ0KLSBHVUkgMyBtYWNoZW4NCi0gTWV0aG9kZW4gaW4gQ2xpZW50RW5naW5lIHp1ciBWZXJmw7xndW5nIHN0ZWxsZW4NCi0gTG9nbw0KLSBBbGxnLiBIb21lcGFnZSwgYnp3IGVyc3RlIFBhZ2UgDQotIA0KLSA=";

		// ------------------------------------
		
		//Linke Seite
		Tree sourceGridTree = thisContactSourceTypesTree;

		final TreeGrid sourceGrid = new TreeGrid();
		sourceGrid.setHeight(350);
		sourceGrid.setWidth(250);
		sourceGrid.setBorder("1px solid #ABABAB");
		sourceGrid.setDragDataAction(DragDataAction.COPY);
		sourceGrid.setCanDragRecordsOut(true);
		sourceGrid.setData(sourceGridTree);
		sourceGrid.getData().openAll();
		sourceGrid.setShowHeader(false);
		sourceGrid.setTreeFieldTitle("Quellfelder");
		
		//Rechte Seite
		final Tree grid2Tree = new Tree();
		grid2Tree.setModelType(TreeModelType.CHILDREN);
		grid2Tree.setNameProperty("Name");

		final TreeNode rootNode = new TreeNode();
		rootNode.setAttribute("Name", "root");
		grid2Tree.setRoot(rootNode);

		TreeNode childNode1 = new TreeNode();
		childNode1.setAttribute("Name", "Exportfeld1");
		childNode1.setAttribute("Feld1", "inhalt1");
		childNode1.setCanDrag(false);
		childNode1.setIsFolder(true);
		grid2Tree.add(childNode1, rootNode);

		TreeNode childNode2 = new TreeNode("Test");
		childNode2.setAttribute("Name", "Exportfeld2");
		childNode2.setCanDrag(false);
		childNode2.setIsFolder(true);
		grid2Tree.add(childNode2, rootNode);

		final TreeGrid grid2 = new TreeGrid();
		grid2.setBorder("1px solid #ABABAB");
		grid2.setData(grid2Tree);
		grid2.getData().openAll();
		grid2.setCanAcceptDroppedRecords(true);
		grid2.setCanRemoveRecords(true);
		grid2.setCanReorderRecords(true);
		grid2.setShowHeader(false);
		grid2.setCanAcceptDrop(false);
		grid2.setTreeFieldTitle("Exportfelder");
		grid2.setHeight(320);
		grid2.setWidth(250);
		grid2.setAlternateRecordStyles(true);
		grid2.addFolderDropHandler(new FolderDropHandler() {

			@Override
			public void onFolderDrop(FolderDropEvent folderDropEvent) {
				final TreeNode target = folderDropEvent.getFolder();
				if (target.getAttribute("Name").compareTo("root") == 0) {
					folderDropEvent.cancel();
				}
			}
		});

		//Dropdown-Menu
		final ListBox formatList = new ListBox();
		formatList.setTitle("Exportformat");
		formatList.addItem("CSV"); //Index 0
		formatList.addItem("CSV f\u00FCr Word-Serienbriefe"); //Index 1
		formatList.addItem("vCard"); //Index 2
		formatList.addItem("XML (xCard)"); //Index 3
		formatList.addStyleName("chooseFormat");
		formatList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = formatList.getSelectedIndex();
				if(selectedIndex == 0){
					//Methode für CSV
					clientEngine.writeExportTree(grid2Tree);
				}
				if(selectedIndex == 1){
					//Methode für CSV-Word-Serienbriefe
					clientEngine.writeExportTree(grid2Tree);
				}
			}
		});
		
		// Move cursor focus to the input box.
		addExportfieldTextBox.setFocus(true);

		// Listen for mouse events on the Add button.
		addExportfieldButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final String name = addExportfieldTextBox.getText().trim();
				addExportfieldTextBox.setFocus(true);

				// Stock code must be between 1 and 10 chars that are numbers,
				// letters, or dots.
				if (!name.matches("^[0-9A-Za-z\\.]{1,10}$")) {
					Window.alert("Der Exportfeldname \"" + name
							+ "\" enth\u00E4lt ung\u00FCltige Zeichen.");
					addExportfieldTextBox.selectAll();
					return;
				}

				// Don't add the stock if it's already in the table.
				if (grid2Tree.find("Name", name) != null) {
					Window.alert("Es ist bereits ein Exportfeld mit dem Namen \""
							+ name + "\" vorhanden.");
					return;
				}

				addExportfieldTextBox.setText("");

				TreeNode childNode = new TreeNode();
				childNode.setAttribute("Name", name);
				childNode.setCanDrag(false);
				childNode.setIsFolder(true);
				grid2Tree.add(childNode, rootNode);
			}
		});

		// Listen for keyboard events in the input box.
		addExportfieldTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					TreeNode childNode = new TreeNode();
					childNode.setAttribute("Name", "Exportfeld");
					childNode.setCanDrag(false);
					childNode.setIsFolder(true);
					grid2Tree.add(childNode, rootNode);
				}
			}
		});

		sourceGrid.setStyleName("sourceGrid");
		grid2.setStyleName("grid2");
		grid2.setBaseStyle("grid2records");


		addExportfieldButton.setStyleName("addExportfieldButton");
		addExportfieldTextBox.setStyleName("addExportfieldTextBox");
		addPanel.add(addExportfieldTextBox);
		addPanel.add(addExportfieldButton);
		addPanel.addStyleName("addPanel");

		final HTML tip2 = new HTML("<div id=\"tip2\"><p>Bitte w\u00E4hlen Sie links die Quellfelder aus, die Sie exportieren m\u00F6chten. Klappen Sie die entsprechenden Exportfelder durch Klicken auf und ziehen Sie dann per Drag and Drop das gew\u00FCnschte Quellfeld nach rechts in das vorher definierte Feld.<br/><br/>Wenn Sie  mehrere Quellfelder in ein Zielfeld ziehen werden diese falls keine Daten vorhanden sind der Reihe nach als Alternativen verwendet.</p></div>");
		tip2.setPixelSize(200, 350);
		HStack grids = new HStack(3);
		grids.addMember(sourceGrid);
		grids.addMember(tip2);
		grids.addMember(grid2);
		grids.setStyleName("grids2");
		grids.draw();
		final HTML exportFormat = new HTML("Exportformat: ");
		exportFormat.addStyleName("exportFormat");
		
		HTML gridHeaders2 = new HTML("<div id=\"gridHeader21\">Quellfelder</div><div id=\"gridHeader22\">Exportfelder</div>");
		gridHeaders2.setStyleName("gridHeaders");
		
		mainPanel.add(gridHeaders2);
		mainPanel.add(exportFormat);
		mainPanel.add(formatList);
		mainPanel.add(grids);
		mainPanel.add(addPanel);
		mainPanel.addStyleName("mainPanel");


		// ------------------------------------

		exportButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final HTML downloadLink = new HTML("<a download=\"Contactexport."
						+ dateiendung + "\" href=data:application/"
						+ dateiendung + ";base64," + encoded + ">Exportdatei</a>");
				// final HTML html = new HTML("<a download=\"MyFile." + dateiendung + "\" href=data:application/vnd.ms-excel;base64,77u/Vm9ybmFtZTtOYWNobmFtZTtBZHJlc3NlO1RlbGVmb25udW1tZXI7DQpNYXJjZWw7UHLDvGdlbDtUb25hdXN0ci4gNDEgNzIxODkgVsO2aHJpbmdlbjsgMDE3NjYxNjc3NTAxOw0KTWF4OyBNdXN0ZXJtYW5uOyBNdXN0ZXJzdHIuIDEgNzg0NjcgS29uc3Rhbno7IDAxMjU2NDU0NTU7DQo=>Download</a>");
				downloadLink.addStyleName("downloadLink");
				page2.add(downloadLink);

				// String uri
				// ="<a download=\"MyFile.csv\" href=data:text/csv;charset=utf-8,\"test\">Download</a>";
				// String uri ="href=data:text/csv;charset=utf-8,\"test\"";
				// Window.open(uri, "TEST", "");
				// History.newItem("page1", true);
			}
		});
		page2.add(mainPanel);
		page2.add(exportButton);
		page2.setStyleName("page2");


	}
}
