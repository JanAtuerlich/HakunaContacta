package de.hakunacontacta.shared;

import java.util.ArrayList;


import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;


public class ContactSourceTypes2Tree {
	
	
	public Tree getTree(ArrayList<ContactSourceType> contactSourceTypes){
		
        Tree sources = new Tree();
        sources.setModelType(TreeModelType.CHILDREN);  
        sources.setNameProperty("Name");
        
        TreeNode rootNode = new TreeNode();
        rootNode.setAttribute("Name","root");
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

}
