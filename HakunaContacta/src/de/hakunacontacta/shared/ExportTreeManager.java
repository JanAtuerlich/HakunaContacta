package de.hakunacontacta.shared;

import java.util.ArrayList;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ExportTreeManager {

	public Tree getTree(ArrayList<ContactSourceType> contactSourceTypes) {

		Tree sources = new Tree();
		sources.setModelType(TreeModelType.CHILDREN);
		sources.setNameProperty("Name");

		TreeNode rootNode = new TreeNode();
		rootNode.setAttribute("Name", "root");
		sources.setRoot(rootNode);

		for (ContactSourceType contactSourceType : contactSourceTypes) {

			TreeNode sourceType = new TreeNode();
			sourceType.setName(contactSourceType.getType());
			sourceType.setAttribute("Name", contactSourceType.getType());
			sourceType.setCanDrag(false);
			sourceType.setIsFolder(true);
			sources.add(sourceType, rootNode);

			for (ContactSourceField contactSourceField : contactSourceType.getSourceFields()) {
				TreeNode sourceField = new TreeNode();
				sourceField.setName(contactSourceField.getName() + " (" + contactSourceField.getAnzahl() + ")");
				sourceField.setAttribute("Name", contactSourceField.getName() + " (" + contactSourceField.getAnzahl() + ")");
				sourceField.setAttribute("SourceType", contactSourceType.getType());
				sourceField.setAttribute("SourceFieldName", contactSourceField.getName());
				sources.add(sourceField, sourceType);

			}

		}

		return sources;
	}

	public ArrayList<ExportField> writeExportTree(Tree exportTree) {
		TreeNode rootnode = exportTree.getRoot();

		ArrayList<ExportField> exportFields = new ArrayList<ExportField>();

		for (TreeNode node : exportTree.getChildren(rootnode)) {

			ExportField exportField = new ExportField();
			exportField.setName(node.getAttribute("Name"));

			int prio = 1;
			for (TreeNode leaf : exportTree.getChildren(node)) {
				exportField.addExportOption(leaf.getAttribute("SourceType"), leaf.getAttribute("SourceFieldName"), leaf.getAttribute("Name"), prio);
				prio++;
			}
			exportFields.add(exportField);

		}

		return exportFields;

	}

	public Tree getExportFieldsTree(ArrayList<ExportField> exportFields) {

		final Tree exportFieldTree = new Tree();
		exportFieldTree.setModelType(TreeModelType.CHILDREN);
		exportFieldTree.setNameProperty("Name");

		TreeNode rootNode = new TreeNode();
		rootNode.setAttribute("Name", "root");
		exportFieldTree.setRoot(rootNode);

		for (ExportField exportField : exportFields) {

			TreeNode exportFieldNode = new TreeNode();
			exportFieldNode.setAttribute("Name", exportField.getName());
			exportFieldNode.setCanDrag(false);
			exportFieldNode.setIsFolder(true);
			exportFieldTree.add(exportFieldNode, rootNode);

			for (ExportOption exportOption : exportField.getExportOptions()) {

				TreeNode exportOptionLeaf = new TreeNode();
				exportOptionLeaf.setName(exportOption.getDisplayName());
				exportOptionLeaf.setAttribute("Name", exportOption.getDisplayName());
				exportOptionLeaf.setAttribute("SourceType", exportOption.getSourceType());
				exportOptionLeaf.setAttribute("SourceFieldName", exportOption.getSourceField());
				exportFieldTree.add(exportOptionLeaf, exportFieldNode);
			}

		}

		return exportFieldTree;
	}

}
