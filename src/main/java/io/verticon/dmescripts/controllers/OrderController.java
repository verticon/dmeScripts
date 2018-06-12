package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import io.verticon.dmescripts.Factory;
import io.verticon.dmescripts.controllers.Product.Category;
import io.verticon.dmescripts.model.*;

@Named
@ViewScoped
public class OrderController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataAccessService dataService = Factory.sDataService;

	@Inject
    private IndwellingCatheterController indwellingCatheterController;

    @Inject
    private CondomCatheterController condomCatheterController;

    @Inject
    private IntermittentCatheterController intermittentCatheterController;

    private ICatheterController catheterController;

    @PostConstruct
    public void init() {
        initPatients();
        initProducts();
        initOrders();
    }

    // ********************************* Add New Order ***************************************

    private String orderReceipt;
    private static int orderId = 0;
    private String quantityWarning;

    public void placeOrder(boolean validateQuantity) {
    	boolean isValid = catheterController.validateQuantities();

    	if(isValid) {
            Order order = catheterController.getOrder(patient);
            Factory.sDataService.addOrder(order);
            orders.add(order);

            orderReceipt = String.format("Order %d Successfully Placed, %s", ++orderId, new Date());
    		RequestContext.getCurrentInstance().execute("PF('orderReceiptDialog').show();");
    	}
    	else {
    		quantityWarning = catheterController.getQuantityWarning();
    		RequestContext.getCurrentInstance().execute("PF('confirmQuantityDialog').show();");
    	}
    }

    public String getOrderReceipt() {
    	return orderReceipt;
    }

    public String getQuantityWarning() {
    	return quantityWarning;
    }

    // ********************************* The Orders Table ***************************************

    private Order target;
    private List<Order> orders;

    private void initOrders() { orders = dataService.getOrders(); }

    public List<Order> getOrders() {
    	List<Order> filteredOrders = new ArrayList<Order>();
    	orders.forEach(order -> {
    		if (order.getCategory() == selectedCategory && order.getPatientId().equals(patient.getId())) {
    			filteredOrders.add(order);
    		}
    	});
    	return filteredOrders;
    }

    public void setTarget(Order order) { target = order; }

    public String delete() {
    	dataService.removeOrder(target);
        orders.remove(target);
        return null;
    }

    public String describeHcpc(String hcpc) { return Hcpcs.description(hcpc); }

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

    public Patient getPatient() {
        System.out.printf("Getting patient %s\n", patient.getFullName());
    	return patient;
    }

    public String getPatientId() {
    	return patient == null ? null : patient.getId();
    }

    public void setPatientId(String id) {
    	patients.forEach(patient -> {
    		if (patient.getId().equals(id)) {
    	    	this.patient = patient;
    		}
    	});
    }

    public void patientSelectionChanged() {
        System.out.printf("Selected patient changed to %s\n", patient.getFullName());
    }

	/************************ The Product Tree **********************************/

    private TreeNode catheters;

    private Product.Category selectedCategory;

    private void initProducts() {
    	catheters = new DefaultTreeNode("Catheters", null);

    	IndwellingCatheterController.addNodes(catheters);
    	CondomCatheterController.addNodes(catheters);
    	IntermittentCatheterController.addNodes(catheters);

    	catheters.setExpanded(true);
    }

    public TreeNode getCatheters() {
    	return catheters;
    }

    public Product.Category getSelectedCategory() {
    	return selectedCategory;
    }

    public Product getSelectedItem() {
    	return catheterController != null ? catheterController.getSelectedItem() : null;
    }

    public boolean isSelectedCategory(String categoryName) {
	   return selectedCategory != null ? selectedCategory.name().equals(categoryName) : false;
    }

    public void onNodeExpanded(NodeExpandEvent event) {
    	//TreeNode expandedNode = event.getTreeNode();
    	// System.out.printf("\nExpanded %s\n", expandedNode);
    }

    public void onNodeCollapsed(NodeCollapseEvent event) {
    	//TreeNode collapsededNode = event.getTreeNode();
    	// System.out.printf("\nCollapsed %s\n", collapsededNode);
    }

    public void onNodeSelected(NodeSelectEvent event) {
    	TreeNode selectedNode = event.getTreeNode();

    	if (selectedNode.getType().equals("category")) {
    		selectedCategory = (Category) selectedNode.getData();
    		switch (selectedCategory) {
    		case IndwellingCatheter:
    			catheterController = indwellingCatheterController;
    			break;
			case CondomCatheter:
    			catheterController = condomCatheterController;
				break;
			case IntermittentCatheter:
    			catheterController = intermittentCatheterController;
				break;
    		}
    		catheterController.setSelectedItem(null);
    	}
    	else if (selectedNode.getType().equals("item")) {
    		
    		Product product = (Product) selectedNode.getData();
    		catheterController.setSelectedItem(product);
    		//System.out.printf("Selected %s\n", product);
         }
    }
}
