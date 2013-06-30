package de.hakunacontacta.client;

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
		page2.setBorderWidth(2);
		page2.setPixelSize(150, 150);
		HTML content = new HTML("This is page2. Click to move to back page1");
		Button button = new Button("Create Export");

		final String dateiendung = "csv";
		final String encoded = "QXVmZ2FiZW46DQoNCi0gRmlsZSBhbiBDbGllbnQgc2VuZGVuLCBiaXNoZXIgZ2lidHMgZXMgZWluZW4gZmVydGlnZW4gU3RyaW5nLiAtPiBSZWNoZXJjaGUsIFByb3RvdHlwZQ0KLSBHVUkgMyBtYWNoZW4NCi0gTWV0aG9kZW4gaW4gQ2xpZW50RW5naW5lIHp1ciBWZXJmw7xndW5nIHN0ZWxsZW4NCi0gTG9nbw0KLSBBbGxnLiBIb21lcGFnZSwgYnp3IGVyc3RlIFBhZ2UgDQotIA0KLSA=";

		// ------------------------------------
		System.out.println("getTree!");
		Tree grid1Tree = thisContactSourceTypesTree;
//		grid1Tree.setModelType(TreeModelType.CHILDREN);
//		grid1Tree.setNameProperty("Name");
//		grid1Tree.setRoot(new TreeNode("Root", new TreeNode("Bin 1",
//				new TreeNode("Blue Cube"), new TreeNode("Yellow Cube"),
//				new TreeNode("Green Cube"))));

		final TreeGrid grid1 = new TreeGrid();
		grid1.setHeight(300);
		grid1.setWidth(200);
		grid1.setDragDataAction(DragDataAction.COPY);
		grid1.setCanDragRecordsOut(true);
		grid1.setData(grid1Tree);
		grid1.getData().openAll();
		grid1.setShowHeader(false);
		grid1.setTreeFieldTitle("Quellfelder");

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
		grid2.setData(grid2Tree);
		grid2.getData().openAll();
		grid2.setCanAcceptDroppedRecords(true);
		grid2.setCanRemoveRecords(true);
		grid2.setCanReorderRecords(true);
		grid2.setShowHeader(false);
		grid2.setCanAcceptDrop(false);
		grid2.setTreeFieldTitle("Exportfelder");
		grid2.setHeight(300);
		grid2.setWidth(200);
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

		grid1.setStyleName("grid1");
		grid2.setStyleName("grid2");
		grid2.setBaseStyle("grid2records");

		addExportfieldButton.setStyleName("addExportfieldButton");
		addExportfieldTextBox.setStyleName("addExportfieldTextBox");
		addPanel.add(addExportfieldTextBox);
		addPanel.add(addExportfieldButton);
		addPanel.addStyleName("addPanel");

		HStack grids = new HStack(2);
		grids.addMember(grid1);
		grids.addMember(grid2);
		grids.setStyleName("grids");
		grids.draw();
		mainPanel.add(grids);
		mainPanel.add(addPanel);
		mainPanel.addStyleName("mainPanel");

		// ------------------------------------

		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final HTML html = new HTML("<a download=\"Contactexport."
						+ dateiendung + "\" href=data:application/"
						+ dateiendung + ";base64," + encoded + ">Download</a>");
				// final HTML html = new HTML("<a download=\"MyFile." +
				// dateiendung +
				// "\" href=data:application/vnd.ms-excel;base64,77u/Vm9ybmFtZTtOYWNobmFtZTtBZHJlc3NlO1RlbGVmb25udW1tZXI7DQpNYXJjZWw7UHLDvGdlbDtUb25hdXN0ci4gNDEgNzIxODkgVsO2aHJpbmdlbjsgMDE3NjYxNjc3NTAxOw0KTWF4OyBNdXN0ZXJtYW5uOyBNdXN0ZXJzdHIuIDEgNzg0NjcgS29uc3Rhbno7IDAxMjU2NDU0NTU7DQo=>Download</a>");
				page2.add(html);

				// String uri
				// ="<a download=\"MyFile.csv\" href=data:text/csv;charset=utf-8,\"test\">Download</a>";
				// String uri ="href=data:text/csv;charset=utf-8,\"test\"";
				//
				// Window.open(uri, "TEST", "");
				//
				// History.newItem("page1", true);
			}
		});
		page2.add(mainPanel);
		page2.add(content);
		page2.add(button);

	}
}
