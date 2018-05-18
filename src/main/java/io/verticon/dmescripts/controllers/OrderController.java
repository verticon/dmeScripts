package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import io.verticon.dmescripts.Factory;
import io.verticon.dmescripts.model.*;

@Named
@ViewScoped
public class OrderController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataAccessService dataService = Factory.sDataService;

    @PostConstruct
    public void init() {
        initPatients();
        initProducts();
        initOrders();
    }

    // ********************************* Add New Order ***************************************

    private String orderStatus;

    public void orderItem() {
    	orderStatus = String.format("   Order Placed %s", new Date());
    }

    public String getOrderStatus() {
    	return orderStatus;
    }

    // ********************************* The Orders Table ***************************************

    private Order target;
    private List<Order> table;

    private void initOrders() { table = dataService.getOrders(); }

    public List<Order> getTable() { return table; }

    public void setTarget(Order order) { target = order; }

    public String delete() {
    	dataService.removeOrder(target);
        table.remove(target);
        return null;
    }

	/************************ The Patients Menu **********************************/

    private Patient patient;
    private List<Patient> patients;

    private void initPatients() {
    	patients = dataService.getPatients();
        if (patients.size() > 0) patient = patients.get(0);
    }

    public List<Patient> getPatients() {
    	return patients;
    }

    public Long getPatientId() {
    	return patient == null ? 0 : patient.getId();
    }

    // Note: When the patient is changed, the list of candidate products is also potentially changed based on the insurance.
    // Thus the currently selected product might no longer be valid; this is not being handled at the moment. However, it
    // seems that the whole insurance thing is going to be eliminated.
    public void setPatientId(Long id) {
    	patients.forEach(patient -> {
    		if (patient.getId() == id) {
    	    	this.patient = patient;
    		}
    	});
    }

    public void patientSelectionChanged() {
        // System.out.printf("Selected patient changed to %s\n", patient.getFullName());
    }

	/************************ The Product Tree **********************************/

    private TreeNode categories;
    private TreeNode items;
    private String selectedProduct;

    private void initProducts() {
    	List<Product> products = dataService.getProducts();

    	categories = new DefaultTreeNode("Categories", null);
    	items = new DefaultTreeNode("Items", null);
    	products.forEach(product -> {
    		TreeNode typeNode = getChild(categories, "type", product.getType());
    		TreeNode categoryNode = getChild(typeNode, "category", product.getCategory());
    		getChild(categoryNode, "manufacturer", product.getManufacturer());

    		items.getChildren().add(new DefaultTreeNode("item", product, items));
    	});
    	categories.setExpanded(true);
    	items.setExpanded(true);
    }

    private TreeNode getChild(TreeNode parent, String type, Object data) {
    	for (TreeNode child : parent.getChildren()) {
    		if (child.getData().equals(data)) { return child; }
    	}
        return new DefaultTreeNode(type, data, parent);
    }

    public TreeNode getCategories() {
        return categories;
    }

    public TreeNode getItems() {
        return items;
    }

    public void onCategoryNodeExpanded(NodeExpandEvent event) {
    	TreeNode expandedNode = event.getTreeNode();
    	//System.out.printf("\nExpanded %s\n", expandedNode);

    	TreeNode parent = expandedNode.getParent();
    	if (parent != null) {
        	for (TreeNode sibling : parent.getChildren()) {
        		if (sibling.isExpanded() && sibling != expandedNode) {
        			sibling.setExpanded(false);
        	    	//System.out.printf("Collapsed %s\n", sibling);
        		}
        	}
    	}
    
    	orderStatus = null;
    }

    public void onProductNodeSelected(NodeSelectEvent event) {
    	TreeNode selectedNode = event.getTreeNode();
    	//System.out.printf("Selected %s\n", selectedNode.getData());
    	if (selectedNode.getType().equals("category") || (selectedNode.getData() instanceof Product)) {
    		selectedProduct = selectedNode.getData().toString();
    	}
    	else {
    		selectedProduct = null;
    	}

    	orderStatus = null;
    }

    public String getSelectedProduct() { return selectedProduct; }

    public void saveFovorite() {
    	System.out.printf("Fovorited %s\n", selectedProduct);
    }

}
