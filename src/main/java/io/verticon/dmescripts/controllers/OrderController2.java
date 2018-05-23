package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import io.verticon.dmescripts.Factory;
import io.verticon.dmescripts.model.*;

@Named
@ViewScoped
public class OrderController2 implements Serializable {

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

    private TreeNode catheters;

    private String selectedHcpc;
    private int quantity;

    private int french;
    private int balloon;
 
    private int solutionType; // 1 = saline, 2 = sterile
    private int bagType; // 1 = leg, 2 = abdominal

    private int trayQty;
    private int syringeQty;
    private int solutionQty;
    private int bagQty;
    private int bedsideBagQty;

    private List<Integer> balloonChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(5); add(10); add(30); }
    };
    private List<String> accessories;

    private void initProducts() {

    	catheters = new DefaultTreeNode("Catheters", null);

    	TreeNode foley = new DefaultTreeNode("category", "Indwelling (foley) Catheter", catheters);
    	String[] hcpcs1 = {"A4338", "A4340", "A4344"}; 
    	for (String hcpc : hcpcs1) { new DefaultTreeNode("hcpc", hcpc, foley); }

    	TreeNode condom = new DefaultTreeNode("category", "Condom Catheter", catheters);
    	new DefaultTreeNode("hcpc", "A4349", condom);

    	TreeNode intermittent = new DefaultTreeNode("category", "Intermittent Catheter", catheters);
    	String[] hcpcs2 = {"A4351", "A4352", "A4353"}; 
    	for (String hcpc : hcpcs2) { new DefaultTreeNode("hcpc", hcpc, intermittent); }

    	catheters.setExpanded(true);
    }

    public TreeNode getCatheters() { return catheters; }

    public void onNodeExpanded(NodeExpandEvent event) {
    	TreeNode expandedNode = event.getTreeNode();
    	// System.out.printf("\nExpanded %s\n", expandedNode);
    
    	orderStatus = null;
    }

    public void onNodeCollapsed(NodeCollapseEvent event) {
    	TreeNode collapsededNode = event.getTreeNode();
    	// System.out.printf("\nCollapsed %s\n", collapsededNode);
    }

    public void onNodeSelected(NodeSelectEvent event) {
    	TreeNode selectedNode = event.getTreeNode();
    	// System.out.printf("\nSelected %s\n", selectedNode.getData());

    	if (selectedNode.getType().equals("hcpc")) {
    		selectedHcpc = selectedNode.getData().toString();
        	quantity = 1;

        	french = 6;
        	balloon = 5;

            trayQty = 0;
            syringeQty = 0;
            solutionQty = 0;
            bagQty = 0;
            bedsideBagQty = 0;

        	solutionType = 1;
        	bagType = 1;
    	}
    	else { selectedHcpc = null; }


    	orderStatus = null;
    }
    
    public String getSelectedProduct() { return selectedHcpc; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getFrench() { return french; }

    public void setFrench(int french) { this.french = french; }

    public List<Integer> getBalloonChoices() { return balloonChoices; }

    public int getBalloon() { return balloon; }

    public void setBalloon(int balloon) { this.balloon = balloon; }

    public int getSolutionType() { return solutionType; }

    public void setSolutionType(int type) { this.solutionType = type; }

    public int getBagType() { return bagType; }

    public void setBagType(int type) { this.bagType = type; }

    public int getBagQty() { return bagQty; }

    public void setBagQty(int qty) { this.bagQty = qty; }

    public int getBedsideBagQty() { return bedsideBagQty; }

    public void setBedsideBagQty(int qty) { this.bedsideBagQty = qty; }

    public int getTrayQty() { return trayQty; }

    public void setTrayQty(int qty) { this.trayQty = qty; }

    public int getSyringeQty() { return syringeQty; }

    public void setSyringeQty(int qty) { this.syringeQty = qty; }

    public int getSolutionQty() { return solutionQty; }

    public void setSolutionQty(int qty) { this.solutionQty = qty; }

    public List<String> getSelectedAccessories() {
    	return accessories;
    }

    public void setSelectedAccessories(List<String> accessories) {
    	this.accessories = accessories;
    }

    public void saveFovorite() {
    }

    public boolean displayPanel(int panelId) {
	   if (selectedHcpc == null) return false;

    	switch (selectedHcpc) {
		case "A4338":
		case "A4340":
		case "A4344":
			return panelId == 1;

		case "A4349":
			return panelId == 2;

		case "A4351":
		case "A4352":
		case "A4353":
			return panelId == 3;

		default: return false;
    	}
    }

    public String getMedicalNecessities() {
 	   if (selectedHcpc == null) return "";

	   	switch (selectedHcpc) {
			case "A4338":
				return "<Mica will provide>";
			case "A4340":
				return "Please indicate patient’s need for a specialty product. ex. \"difficulty inserting traditional indwelling catheter due to anatomy, patient requires a coude’ tipped indwelling catheter\"";
			case "A4344":
				return "Please document patient’s need for all-silicone (non-latex) device, such as a latex allergy";
	
			case "A4349":
				return "";
	
			case "A4351":
			case "A4352":
			case "A4353":
				return "";
	
			default: return "";
	   	}
    }
}
