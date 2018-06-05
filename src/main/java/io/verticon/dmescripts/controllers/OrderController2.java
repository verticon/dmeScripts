package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
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

	public class HCPC {
		public String hcpc;
		public String description;
		public HCPC(String hcpc, String description) {
			this.hcpc = hcpc;
			this.description = description;
		}
		@Override
		public String toString() { return description; }
	}
	private static final long serialVersionUID = 1L;
	
	private DataAccessService dataService = Factory.sDataService;

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
    	int limit = getLimit(selectedHcpc);
    	boolean isValid = validateQuantity ? quantity <= limit : true;

    	if(isValid) {
    		orderReceipt = String.format("Order %d Successfully Placed, %s", ++orderId, new Date());
    		RequestContext.getCurrentInstance().execute("PF('orderReceiptDialog').show();");
    	}
    	else {
    		quantityWarning = String.format("Medicare requires a medical justification<br/>"
    									+   "for quantities greater than %d. Approval is<br/>"
    									+   "not guaranteed. Place order anyway?<br/><br/>", limit);
    		RequestContext.getCurrentInstance().execute("PF('confirmQuantityDialog').show();");
    	}
    }

    public int getLimit(String hcpc) {
    	switch (hcpc) {
		case "A4338":
		case "A4340":
		case "A4344":
			return 1;

		case "A4349":
			return 35;

		case "A4351":
		case "A4352":
			return 1;

		default:
			return 1;
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
        //System.out.printf("Selected patient changed to %s\n", patient.getFullName());
    }

	/************************ The Product Tree **********************************/

    private TreeNode catheters;

    private String selectedHcpc;

    private int quantity;
    private int balloon;
    private List<Integer> balloonChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(5); add(10); add(30); }
    };

    private int french;
    private List<Integer> frenchChoicesIndwelling = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(6); add(8); add(10); add(12); add(14); add(16); add(18); add(20); add(22); add(24); add(26); add(28); }
    };
    private List<Integer> frenchChoicesIntermittant = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(6); add(8); add(10); add(12); add(14); add(16); add(18); add(20); add(22); add(24); }
    };
 
    private boolean sizeProvided;
    private int diameter;
    private List<Integer> diameterChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(25); add(29); add(32); add(36); add(41); }
    };

    private String length;
    private List<String> lengthChoices = new ArrayList<String>() {
    	private static final long serialVersionUID = 1L;
    	{ add("Pediatric 10\""); add("Female 6\""); add("Male 14-16\""); }
    };

    private boolean closedSystemOption;

    private int solutionType; // 1 = saline, 2 = sterile
    private int bagType; // 1 = leg, 2 = abdominal

    private int trayQty;
    private int syringeQty;
    private int solutionQty;
    private int bagQty;
    private int bedsideBagQty;
    private int tubingQty;
    private int wipesQty;

    private boolean hasLatexAllergy;

    private List<String> accessories;

    private void initProducts() {

    	catheters = new DefaultTreeNode("Catheters", null);

    	TreeNode foley = new DefaultTreeNode("category", "Indwelling (foley) Catheter", catheters);
    	HCPC[] hcpcs1 = { new HCPC("A4338", "Latex"), new HCPC("A4340", "Specialty"), new HCPC("A4344", "All Silicone") }; 
    	for (HCPC hcpc : hcpcs1) { new DefaultTreeNode("hcpc", hcpc, foley); }

    	TreeNode condom = new DefaultTreeNode("category", "Condom Catheter", catheters);
    	new DefaultTreeNode("hcpc", new HCPC("A4349", "External"), condom);

    	TreeNode intermittent = new DefaultTreeNode("category", "Intermittent Catheter", catheters);
    	HCPC[] hcpcs2 = { new HCPC("A4351", "Straight"), new HCPC("A4352", "Coude' Tip")}; 
    	for (HCPC hcpc : hcpcs2) { new DefaultTreeNode("hcpc", hcpc, intermittent); }

    	catheters.setExpanded(true);
    }

    public TreeNode getCatheters() { return catheters; }

    public String getSelectedProduct() { return selectedHcpc; }

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

    	if (selectedNode.getType().equals("hcpc")) {
    		selectedHcpc = ((HCPC)selectedNode.getData()).hcpc;
    		//System.out.printf("Selected %s\n", selectedHcpc);
 
    		quantity = 1;

        	french = 6;
        	balloon = 5;
        	length = "Male 14-16\"";
        	
        	diameter = 25;
        	sizeProvided = true;

            trayQty = 0;
            syringeQty = 0;
            solutionQty = 0;
            bagQty = 0;
            bedsideBagQty = 0;
            tubingQty = 0;
            wipesQty = 0;

        	solutionType = 1;
        	bagType = 1;
 
        	hasLatexAllergy = false;
        	closedSystemOption = false;
        }
    	else { selectedHcpc = null; }
    }


    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public List<Integer> getFrenchChoicesIndwelling() { return frenchChoicesIndwelling; }
    public List<Integer> getFrenchChoicesIntermittant() { return frenchChoicesIntermittant; }
    public int getFrench() { return french; }
    public void setFrench(int french) { this.french = french; }

    public List<Integer> getDiameterChoices() { return diameterChoices; }
    public int getDiameter() { return diameter; }
    public void setDiameter(int diameter) { this.diameter = diameter; }

    public boolean getSizeProvided() { return sizeProvided; }
    public void setSizeProvided(boolean provided) { this.sizeProvided = provided; }

    public List<Integer> getBalloonChoices() { return balloonChoices; }
    public int getBalloon() { return balloon; }
    public void setBalloon(int balloon) { this.balloon = balloon; }

    public List<String> getLengthChoices() { return lengthChoices; }
    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

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

    public int getTubingQty() { return tubingQty; }
    public void setTubingQty(int qty) { this.tubingQty = qty; }

    public int getWipesQty() { return wipesQty; }
    public void setWipesQty(int qty) { this.tubingQty = qty; }

    public List<String> getSelectedAccessories() { return accessories; }
    public void setSelectedAccessories(List<String> accessories) { this.accessories = accessories; }

    public boolean getHasLatexAllergy() { return hasLatexAllergy; }
    public void setHasLatexAllergy(boolean has) { this.hasLatexAllergy = has; }

    public boolean getClosedSystemOption() { return closedSystemOption; }
    public void setClosedSystemOption(boolean option) { this.closedSystemOption = option; }

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
			return panelId == 3;

		default:
			return false;
    	}
    }

    public String getMedicalNecessities() {
 	   if (selectedHcpc == null) return "";

 	   String necessity = "";
 	   int count = 0;

	   	switch (selectedHcpc) {
			case "A4338":
				return "<TBD>";
			case "A4340":
				return "Please indicate patient’s need for a specialty product. ex. \"difficulty inserting traditional indwelling catheter due to anatomy, patient requires a coude’ tipped indwelling catheter\"";
			case "A4344":
				return "Please document patient’s need for all-silicone (non-latex) device, such as a latex allergy.";
	
			case "A4349":
				return "";
	
			case "A4352":
				count = 1;
				necessity = "1) Please indicate that patient has anatomy that makes passing a straight catheter difficult, such as an enlarged prostate or urethral stricture.<br/><br/>";
			case "A4351":
				necessity += String.format("%d) Please make sure that patient’s clinical notes reflect the frequency they will need to catheterize, as well as the length of need, and that the patient has urinary incontinence or retention.<br/><br/>", ++count);
				if (closedSystemOption) {
					necessity += String.format("%d) Please document patient’s history of urinary track infections.", ++count); 
				}
				return necessity;
	
			default:
				return "";
	   	}
    }
}
