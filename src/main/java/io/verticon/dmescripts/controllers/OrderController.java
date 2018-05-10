package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

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

	private Order newOrder;

	public String addNew() {
		newOrder = new Order();
        return null;
    }

    public String save() {
    	//dataService.addOrder(newOrder, patient, product);
        //table.add(newOrder);   
    	cancel();
    	
    	/*
		import javax.faces.context.ExternalContext;
		import javax.faces.context.FacesContext;
		import javax.servlet.http.HttpServletRequest;

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

        return null;
    }

    public String cancel() {
    	newOrder = null;
        return null;
    }

    public boolean getAddingNew() { return newOrder != null; }

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

    private List<Product> products;
    private TreeNode productTree;

    private void initProducts() {
    	products = dataService.getProducts();

    	productTree = new DefaultTreeNode("Products", null);
    	products.forEach(product -> {
    		TreeNode typeNode = getChild(productTree, product.getType());
    		TreeNode categoryNode = getChild(typeNode, product.getCategory());
    		TreeNode manufacturerNode = getChild(categoryNode, product.getManufacturer());
    		manufacturerNode.getChildren().add(new DefaultTreeNode(product));
    	});
    	productTree.setExpanded(true);
    }

    private TreeNode getChild(TreeNode parent, String name) {
    	for (TreeNode child : parent.getChildren()) {
    		if (((String) child.getData()).equals(name)) { return child; }
    	}
        TreeNode child = new DefaultTreeNode(name, parent);
    	return child;
    }

    public TreeNode getProductTree() {
        return productTree;
    }

    public void productSelectionChanged() {
        //System.out.printf("Selected product changed to %s\n", product.getName());
    }

    public String getProductImageUrl() {
    	return products.get(0).getImageUrl();
    }

}
