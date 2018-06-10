package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import io.verticon.dmescripts.model.Patient;

@Named
@ViewScoped
public class IntermittentCatheterController implements ICatheterController, Serializable {

	private static final long serialVersionUID = 1L;

	public static void addNodes(TreeNode parent) {
    	TreeNode indwelling = new DefaultTreeNode("category", Product.Category.IntermittentCatheter, parent);
    	Product[] products = { new Product(Product.Category.IntermittentCatheter, "Straight", "A4351"), new Product(Product.Category.IntermittentCatheter, "Coude' Tip", "A4352") }; 
    	for (Product product : products) { new DefaultTreeNode("item", product, indwelling); }
	}

	public void getOrder(JsonObjectBuilder builder) {
		builder
			.add("hcpc", selectedItem.getHcpc())
			.add("quantity", quantity)
			.add("french", french)
			.add("length", length)
			.add("closedSystemOption", closedSystemOption);
	}

	private Product selectedItem;
    public Product getSelectedItem() { return selectedItem; }
    public void setSelectedItem(Product item) {
    	selectedItem = item;

		quantity = 1;
    	french = 6;
    	length = "Male 14-16\"";
    	closedSystemOption = false;
    }

	// Quantity
    private int quantity;
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

	// French
    private List<Integer> frenchChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(6); add(8); add(10); add(12); add(14); add(16); add(18); add(20); add(22); add(24); }
    };
    public List<Integer> getFrenchChoices() { return frenchChoices; }
    private int french;
    public int getFrench() { return french; }
    public void setFrench(int french) { this.french = french; }

	// Length
    private List<String> lengthChoices = new ArrayList<String>() {
    	private static final long serialVersionUID = 1L;
    	{ add("Pediatric 10\""); add("Female 6\""); add("Male 14-16\""); }
    };
    public List<String> getLengthChoices() { return lengthChoices; }
    private String length;
    public String getLength() { return length; }
    public void setLength(String length) { this.length = length; }

	// Closed System Option
    private boolean closedSystemOption;
    public boolean getClosedSystemOption() { return closedSystemOption; }
    public void setClosedSystemOption(boolean option) { this.closedSystemOption = option; }

    // Medical Necessities
    public String getMedicalNecessities() {
  	   if (getSelectedItem() == null) return "";

  	   String necessity = "";
  	   int count = 0;

 	   	switch (getSelectedItem().getHcpc()) {
 		case "A4352":
 			count = 1;
 			necessity = "1) Please indicate that patient has anatomy that makes passing a straight catheter difficult, such as an enlarged prostate or urethral stricture.<br/><br/>";
 		case "A4351":
 			necessity += String.format("%d) Please make sure that patient’s clinical notes reflect the frequency they will need to catheterize, as well as the length of need, and that the patient has urinary incontinence or retention.<br/><br/>", ++count);
 			if (closedSystemOption) {
 				necessity += String.format("%d) Please document patient’s history of urinary track infections.", ++count); 
 			}
 	   	}

 	   	return necessity;
    }

    // Validation
    public boolean validateQuantities() { return quantity <= 1; }
    public String getQuantityWarning() {
    	return  "Medicare requires a medical justification<br/>"
    		+   "for quantities greater than 1. Approval is<br/>"
			+   "not guaranteed. Place order anyway?<br/><br/>";
    }
}
