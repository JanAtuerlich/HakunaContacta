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

import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

import de.hakunacontacta.shared.ExportTypeEnum;


public class Page2 extends Composite {
	private VerticalPanel page2 = new VerticalPanel();
	static private Page2 _instance = null;
	private static ClientEngine clientEngine;
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox addExportfieldTextBox = new TextBox();
	private Button addExportfieldButton = new Button("Add");
	private static Tree thisSourceTypesTree = null;
	private static Tree thisExportTypesTree = null;
	TreeGrid sourceGrid = null;
	TreeGrid exportGrid = null;
	private static ExportTypeEnum currentFormat = ExportTypeEnum.CSV;
	private String dateiendung = "csv";
	private String encoded = "";

	private Page2() {
		initPage();
		initWidget(page2);
	}

	public void setThisExportTypesTree(Tree thisExportTypesTree) {
		Page2.thisExportTypesTree = thisExportTypesTree;
	}

	public static Page2 getInstance(ClientEngine cEngine, Tree contactSourceTypesTree) {
		thisSourceTypesTree = contactSourceTypesTree;

		clientEngine = cEngine;
		if (null == _instance) {
			_instance = new Page2();
		}
		return _instance;
	}

	public void updateData() { // wird beim erneuten Seitenaufbau geladen um den
								// Inhalt der Grids zu aktuallisieren

		sourceGrid.setData(thisSourceTypesTree);
		sourceGrid.getData().openAll();
		exportGrid.setData(thisExportTypesTree);
		exportGrid.getData().openAll();

	}
	
	public void createDownloadLink(){
		if (currentFormat == ExportTypeEnum.CSV) {
			dateiendung = "csv";
		} else if (currentFormat == ExportTypeEnum.XML) {
			dateiendung = "xml";
		} else if (currentFormat == ExportTypeEnum.vCard) {
			dateiendung = "vCard";
		} else if (currentFormat == ExportTypeEnum.CSVWord) {
			dateiendung = "csv";
		}
		HTML html = new HTML("<a download=\"Contactexport." + dateiendung + "\" href=data:application/" + dateiendung + ";base64," + encoded + ">Download</a>");
		page2.add(html);
		//Downloadlink wird erstellt
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}

	private void initPage() {
<<<<<<< HEAD
		
		clientEngine.setPage2(_instance);
//		System.out.println("Check from Page2: " + clientEngine.check);
		page2.setPixelSize(500, 350);
		Button exportButton = new Button("Create Export");
		exportButton.addStyleName("exportButton");
=======
		page2.setBorderWidth(2);
		page2.setPixelSize(150, 150);
		HTML content = new HTML("This is page2. Click to move to back page1");
		Button button = new Button("Create Export");
		exportButton.addStyleName("exportButton");
>>>>>>> Page2Construction

		
		// ------------------------------------

		// Linke Seite

		sourceGrid = new TreeGrid();
		sourceGrid.setHeight(300);
		sourceGrid.setWidth(200);
>>>>>>> Page2Construction
		sourceGrid.setDragDataAction(DragDataAction.COPY);
		sourceGrid.setCanDragRecordsOut(true);
		sourceGrid.setData(thisSourceTypesTree);
		sourceGrid.getData().openAll();
		sourceGrid.setShowHeader(false);
		sourceGrid.setTreeFieldTitle("Quellfelder");
<<<<<<< HEAD
		
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
=======

		// Rechte Seite

		exportGrid = new TreeGrid();
		exportGrid.setCanAcceptDroppedRecords(true);
		exportGrid.setCanRemoveRecords(true);
		exportGrid.setCanReorderRecords(true);
		exportGrid.setShowHeader(false);
		exportGrid.setCanAcceptDrop(false);
		exportGrid.setTreeFieldTitle("Exportfelder");
		exportGrid.setHeight(300);
		exportGrid.setWidth(200);
		exportGrid.setAlternateRecordStyles(true);
		exportGrid.addFolderDropHandler(new FolderDropHandler() {
>>>>>>> Page2Construction

			@Override
			public void onFolderDrop(FolderDropEvent folderDropEvent) {
				final TreeNode target = folderDropEvent.getFolder();
				if (target.getAttribute("Name").compareTo("root") == 0) {
					folderDropEvent.cancel();
				}
			}
		});

		clientEngine.getExportFields(ExportTypeEnum.CSV);

		// Dropdown-Menu
		final ListBox formatList = new ListBox();
		formatList.setTitle("Exportformat");
<<<<<<< HEAD
		formatList.addItem("CSV"); //Index 0
		formatList.addItem("CSV f\u00FCr Word-Serienbriefe"); //Index 1
		formatList.addItem("vCard"); //Index 2
		formatList.addItem("XML (xCard)"); //Index 3
		formatList.addStyleName("chooseFormat");
=======
		formatList.addItem("CSV"); // Index 0
		formatList.addItem("CSV f\u00FCr Word-Serienbriefe"); // Index 1
		formatList.addItem("vCard"); // Index 2
		formatList.addItem("XML (xCard)"); // Index 3
		

>>>>>>> Page2Construction
		formatList.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = formatList.getSelectedIndex();
				System.out.println("Dropdown change to: " + selectedIndex);

				if (selectedIndex == 0) {
					// Methode für CSV
					clientEngine.writeExportOptions(thisExportTypesTree, currentFormat, ExportTypeEnum.CSV);
					currentFormat = ExportTypeEnum.CSV;
				}
				if (selectedIndex == 1) {
					// Methode für CSV-Word-Serienbriefe
					clientEngine.writeExportOptions(thisExportTypesTree, currentFormat, ExportTypeEnum.CSVWord);
					currentFormat = ExportTypeEnum.CSVWord;
				}
				if (selectedIndex == 2) {
					// Methode für vCard
					clientEngine.writeExportOptions(thisExportTypesTree, currentFormat, ExportTypeEnum.vCard);
					currentFormat = ExportTypeEnum.vCard;
				}
				if (selectedIndex == 3) {
					// Methode für XML
					clientEngine.writeExportOptions(thisExportTypesTree, currentFormat, ExportTypeEnum.XML);
					currentFormat = ExportTypeEnum.XML;
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

				// Stock code must be between 1 and 15 chars that are numbers,
				// letters, or dots.
				if (!name.matches("^[0-9A-Za-z\\.]{1,15}$")) {
					Window.alert("Der Exportfeldname \"" + name + "\" enth\u00E4lt ung\u00FCltige Zeichen.");
					addExportfieldTextBox.selectAll();
					return;
				}

				// Don't add the stock if it's already in the table.
				if (thisExportTypesTree.find("Name", name) != null) {
					Window.alert("Es ist bereits ein Exportfeld mit dem Namen \"" + name + "\" vorhanden.");
					return;
				}

				addExportfieldTextBox.setText("");

				TreeNode childNode = new TreeNode();
				childNode.setAttribute("Name", name);
				childNode.setCanDrag(false);
				childNode.setIsFolder(true);
				thisExportTypesTree.add(childNode, thisExportTypesTree.getRoot());
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
					thisExportTypesTree.add(childNode, thisExportTypesTree.getRoot());
				}
			}
		});

		sourceGrid.setStyleName("sourceGrid");
		exportGrid.setStyleName("exportGrid");
		exportGrid.setBaseStyle("grid2records");


		addExportfieldButton.setStyleName("addExportfieldButton");
		addExportfieldTextBox.setStyleName("addExportfieldTextBox");
		addPanel.add(addExportfieldTextBox);
		addPanel.add(addExportfieldButton);
		addPanel.addStyleName("addPanel");

		final HTML tip2 = new HTML("<div id=\"tip2\"><p>Bitte w\u00E4hlen Sie links die Quellfelder aus, die Sie exportieren m\u00F6chten. Klappen Sie die entsprechenden Exportfelder durch Klicken auf und ziehen Sie dann per Drag and Drop das gew\u00FCnschte Quellfeld nach rechts in das vorher definierte Feld.<br/><br/>Wenn Sie  mehrere Quellfelder in ein Zielfeld ziehen werden diese falls keine Daten vorhanden sind der Reihe nach als Alternativen verwendet.</p></div>");
		tip2.setPixelSize(200, 350);
		HStack grids = new HStack(3);
		grids.addMember(sourceGrid);
<<<<<<< HEAD
		grids.addMember(tip2);
		grids.addMember(grid2);
		grids.setStyleName("grids2");
=======
		grids.addMember(exportGrid);
		grids.setStyleName("grids");
>>>>>>> Page2Construction
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
<<<<<<< HEAD
				final HTML downloadLink = new HTML("<a download=\"Contactexport."
						+ dateiendung + "\" href=data:application/"
						+ dateiendung + ";base64," + encoded + ">Exportdatei</a>");
				// final HTML html = new HTML("<a download=\"MyFile." + dateiendung + "\" href=data:application/vnd.ms-excel;base64,77u/Vm9ybmFtZTtOYWNobmFtZTtBZHJlc3NlO1RlbGVmb25udW1tZXI7DQpNYXJjZWw7UHLDvGdlbDtUb25hdXN0ci4gNDEgNzIxODkgVsO2aHJpbmdlbjsgMDE3NjYxNjc3NTAxOw0KTWF4OyBNdXN0ZXJtYW5uOyBNdXN0ZXJzdHIuIDEgNzg0NjcgS29uc3Rhbno7IDAxMjU2NDU0NTU7DQo=>Download</a>");
				downloadLink.addStyleName("downloadLink");
				page2.add(downloadLink);
=======
				
				clientEngine.getFile(thisExportTypesTree, currentFormat, currentFormat);				
				
				// final HTML html = new HTML("<a download=\"MyFile." +
				// dateiendung +
				// "\" href=data:application/vnd.ms-excel;base64,77u/Vm9ybmFtZTtOYWNobmFtZTtBZHJlc3NlO1RlbGVmb25udW1tZXI7DQpNYXJjZWw7UHLDvGdlbDtUb25hdXN0ci4gNDEgNzIxODkgVsO2aHJpbmdlbjsgMDE3NjYxNjc3NTAxOw0KTWF4OyBNdXN0ZXJtYW5uOyBNdXN0ZXJzdHIuIDEgNzg0NjcgS29uc3Rhbno7IDAxMjU2NDU0NTU7DQo=>Download</a>");
				
>>>>>>> Page2Construction

				// String uri
				// ="<a download=\"MyFile.csv\" href=data:text/csv;charset=utf-8,\"test\">Download</a>";
				// String uri ="href=data:text/csv;charset=utf-8,\"test\"";
				// Window.open(uri, "TEST", "");
				// History.newItem("page1", true);
			}
		});
		page2.add(mainPanel);
<<<<<<< HEAD
		page2.add(exportButton);
		page2.setStyleName("page2");

		
=======
		page2.add(content);
		page2.add(button);

>>>>>>> Page2Construction
	}
	
}
