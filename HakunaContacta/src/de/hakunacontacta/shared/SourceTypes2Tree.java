package de.hakunacontacta.shared;

import java.util.ArrayList;


import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

public class SourceTypes2Tree {
	
	
	public static Tree getTree(ArrayList<ContactSourceType> contactSourceTypes){
		
        Tree sources = new Tree();
        sources.setModelType(TreeModelType.CHILDREN);  
        sources.setNameProperty("Name");
        
        TreeNode rootNode = new TreeNode();
        rootNode.setAttribute("Name","root");
        sources.setRoot(rootNode);
        
        for (ContactSourceType contactSourceType : contactSourceTypes) {
        	
        	TreeNode sourceType = new TreeNode();
        	sourceType.setName(contactSourceType.getType());
        	sourceType.setCanDrag(false);
	        sourceType.setIsFolder(true);
	        sources.add(sourceType, rootNode);
	        
	        
	        for (ContactSourceField contactSourceField : contactSourceType.getSourceFields()) {
	        	TreeNode sourceField = new TreeNode();
	        	sourceField.setName(contactSourceField.getName() + " (" + contactSourceField.getAnzahl() + ")");
	        	sourceField.setAttribute("SourceType", contactSourceType.getType());
	        	sourceField.setAttribute("SourceFieldName", contactSourceField.getName());
		        sources.add(sourceType, rootNode);
	        	
				
			}
        	
			
		}
        
        
        
		
        
//        final Tree grid2Tree = new Tree();  
//        grid2Tree.setModelType(TreeModelType.CHILDREN);  
//        grid2Tree.setNameProperty("Name");  
//        
//	        final TreeNode rootNode = new TreeNode();
//	        rootNode.setAttribute("Name","root");
//	        grid2Tree.setRoot(rootNode);
//	                
//	        TreeNode childNode1 = new TreeNode();
//	        childNode1.setAttribute("Name","Exportfeld1");
//	        childNode1.setAttribute("Feld1", "inhalt1");
//	        childNode1.setCanDrag(false);
//	        childNode1.setIsFolder(true);
//	        grid2Tree.add(childNode1, rootNode);
	        	     	        
		
		
		
		
		
		
		return null;		
	}

}
