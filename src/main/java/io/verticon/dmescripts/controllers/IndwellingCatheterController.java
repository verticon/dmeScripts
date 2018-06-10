package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class IndwellingCatheterController implements ICatheterController, Serializable {

	private static final long serialVersionUID = 1L;

	public static void addNodes(TreeNode parent) {
    	TreeNode intermittent = new DefaultTreeNode("category", Product.Category.IndwellingCatheter, parent);
    	Product[] products = { new Product(Product.Category.IndwellingCatheter, "Latex", "A4338"), new Product(Product.Category.IndwellingCatheter, "Specialty", "A4340"), new Product(Product.Category.IndwellingCatheter, "All Silicone", "A4344") }; 
    	for (Product product : products) { new DefaultTreeNode("item", product, intermittent); }
	}

	public void getOrder(JsonObjectBuilder builder) {
		builder
			.add("hcpc", selectedItem.getHcpc())
			.add("quantity", quantity)
			.add("french", french)
			.add("balloon", balloon)
			.add("A4310", trayQty)
			.add("A4213", syringeQty)
			.add("A4217", solutionQty)
			.add("solutionType", solutionType == 1 ? "saline" : "sterile")
			.add("A4358", bagQty)
			.add("bagType", bagType == 1 ? "leg" : "abdominal")
			.add("A4357", bedsideBagQty);
	}

	private Product selectedItem;
    public Product getSelectedItem() { return selectedItem; }
    public void setSelectedItem(Product item) {
    	selectedItem = item;

		quantity = 1;

    	french = 6;
    	balloon = 5;

        trayQty = 0;
        syringeQty = 0;

        solutionQty = 0;
    	solutionType = 1;

        bagQty = 0;
    	bagType = 1;
        bedsideBagQty = 0;
    }

	// Quantity
    private int quantity;
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

	// French
    private List<Integer> frenchChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(6); add(8); add(10); add(12); add(14); add(16); add(18); add(20); add(22); add(24); add(26); add(28); }
    };
    public List<Integer> getFrenchChoices() { return frenchChoices; }
    private int french;
    public int getFrench() { return french; }
    public void setFrench(int french) { this.french = french; }

	// Balloon
    private List<Integer> balloonChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(5); add(10); add(30); }
    };
    public List<Integer> getBalloonChoices() { return balloonChoices; }
    private int balloon = 5;
    public int getBalloon() { return balloon; }
    public void setBalloon(int balloon) { this.balloon = balloon; }

	// Trays
    private int trayQty;
    public int getTrayQty() { return trayQty; }
    public void setTrayQty(int qty) { this.trayQty = qty; }

	// Syringes
    private int syringeQty;
    public int getSyringeQty() { return syringeQty; }
    public void setSyringeQty(int qty) { this.syringeQty = qty; }

	// Solution
    private int solutionQty;
    public int getSolutionQty() { return solutionQty; }
    public void setSolutionQty(int qty) { this.solutionQty = qty; }
    private int solutionType; // 1 = saline, 2 = sterile
    public int getSolutionType() { return solutionType; }
    public void setSolutionType(int type) { this.solutionType = type; }

    // Bags
    private int bagQty;
    public int getBagQty() { return bagQty; }
    public void setBagQty(int qty) { this.bagQty = qty; }
    private int bagType; // 1 = leg, 2 = abdominal
    public int getBagType() { return bagType; }
    public void setBagType(int type) { this.bagType = type; }
    private int bedsideBagQty;
    public int getBedsideBagQty() { return bedsideBagQty; }
    public void setBedsideBagQty(int qty) { this.bedsideBagQty = qty; }

    // Medical Necessities
    public String getMedicalNecessities() {
  	   if (selectedItem == null) return "";

 	   	switch (selectedItem.getHcpc()) {
 			case "A4338":
 				return "[TBD]";
 			case "A4340":
 				return "Please indicate patient’s need for a specialty product. ex. \"difficulty inserting traditional indwelling catheter due to anatomy, patient requires a coude’ tipped indwelling catheter\"";
 			case "A4344":
 				return "Please document patient’s need for all-silicone (non-latex) device, such as a latex allergy.";
 	
  			default:
 				return "";
 	   	}
    }

    // Validation
    public boolean validateQuantities() { return quantity <= 1; }
    public String getQuantityWarning() {
    	return  "Medicare requires a medical justification<br/>"
    		+   "for quantities greater than 1. Approval is<br/>"
			+   "not guaranteed. Place order anyway?<br/><br/>";
    }
}
