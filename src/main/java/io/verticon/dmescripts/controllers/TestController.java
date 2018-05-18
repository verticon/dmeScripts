package io.verticon.dmescripts.controllers;
 
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.NodeExpandEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
 
@Named
@ViewScoped
public class TestController implements Serializable {
     
	private static final long serialVersionUID = 1L;
	private TreeNode root;
     
    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        
        TreeNode nodeA = new DefaultTreeNode("visible", "A", root);
        {
            TreeNode nodeA1 = new DefaultTreeNode("visible", "A1", nodeA);
            {
                TreeNode nodeA1_1 = new DefaultTreeNode("visible", "A1.1", nodeA1);
                TreeNode nodeA1_2 = new DefaultTreeNode("visible", "A1.2", nodeA1);
            }
            TreeNode nodeA2 = new DefaultTreeNode("visible", "A2", nodeA);
            {
                TreeNode nodeA2_1 = new DefaultTreeNode("visible", "A2.1", nodeA2);
            }
        }

        TreeNode nodeB = new DefaultTreeNode("visible", "B", root);
        {
            TreeNode nodeB1 = new DefaultTreeNode("visible", "B1", nodeB);
            {
                TreeNode nodeA1_1 = new DefaultTreeNode("visible", "B1.1", nodeB1);
            }
            TreeNode nodeB2 = new DefaultTreeNode("visible", "B2", nodeB);
        }
         
        TreeNode nodeC = new DefaultTreeNode("visible", "C", root);


        root.setExpanded(true);
    }

    public TreeNode getRoot() {
        return root;
    }

    public void onNodeExpanded(NodeExpandEvent event) {
    	TreeNode expandedNode = event.getTreeNode();
    	System.out.printf("\nExpanded %s\n", expandedNode);
 
    	TreeNode parent = expandedNode.getParent();
    	if (parent != null) {
        	for (TreeNode sibling : parent.getChildren()) {
        		if (sibling.isExpanded() && sibling != expandedNode) {
        			sibling.setExpanded(false);
        	    	System.out.printf("Collapsed %s\n", sibling);
        		}
        	}
    	}
    }
}