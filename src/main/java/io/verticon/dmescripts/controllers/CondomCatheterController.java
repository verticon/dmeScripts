package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import io.verticon.dmescripts.model.Order;
import io.verticon.dmescripts.model.Patient;

@Named
@ViewScoped
public class CondomCatheterController implements ICatheterController, Serializable {

	private static final long serialVersionUID = 1L;

	public static void addNodes(TreeNode parent) {
    	TreeNode condom = new DefaultTreeNode("category", Product.Category.CondomCatheter, parent);
    	Product[] products = {
    		new Product(Product.Category.CondomCatheter, Hcpcs.description("A4349"), "A4349")
    	}; 
    	for (Product product : products) { new DefaultTreeNode("item", product, condom); }
	}

	public Order getOrder(Patient patient) {
		JsonObjectBuilder builder = Json.createObjectBuilder()
				.add("sizeProvided", sizeProvided)
				.add("diameter", diameter)
				.add("A4331", tubingQty)
				.add("A5120", wipesQty)
				.add("A4358", bagQty)
				.add("bagType", bagType == 1 ? "leg" : "abdominal")
				.add("A4357", bedsideBagQty)
				.add("hasLatexAllergy", hasLatexAllergy);
		JsonObject orderObject = builder.build();

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(stringWriter);
        jsonWriter.writeObject(orderObject);
        jsonWriter.close();
        String details = stringWriter.getBuffer().toString();

        return new Order(selectedItem.getCategory(), selectedItem.getHcpc(), quantity, patient.getId(), details);
	}

	private Product selectedItem;
    public Product getSelectedItem() { return selectedItem; }
    public void setSelectedItem(Product item) {
    	selectedItem = item;

		quantity = 1;

    	diameter = 25;
    	sizeProvided = true;

        tubingQty = 0;
        wipesQty = 0;

        bagQty = 0;
    	bagType = 1;
        bedsideBagQty = 0;

    	hasLatexAllergy = false;
    }

	// Quantity
    private int quantity;
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

	// Size
    private boolean sizeProvided;
    public boolean getSizeProvided() { return sizeProvided; }
    public void setSizeProvided(boolean provided) { sizeProvided = provided; }
    private List<Integer> diameterChoices = new ArrayList<Integer>() {
    	private static final long serialVersionUID = 1L;
    	{ add(25); add(29); add(32); add(36); add(41); }
    };
    public List<Integer> getDiameterChoices() { return diameterChoices; }
    private int diameter;
    public int getDiameter() { return diameter; }
    public void setDiameter(int diameter) { this.diameter = diameter; }

	// Latex Allergy
    private boolean hasLatexAllergy;
    public boolean getHasLatexAllergy() { return hasLatexAllergy; }
    public void setHasLatexAllergy(boolean has) { this.hasLatexAllergy = has; }

	// Tubing
    private int tubingQty;
    public int getTubingQty() { return tubingQty; }
    public void setTubingQty(int qty) { this.tubingQty = qty; }

	// Wipes
    private int wipesQty;
    public int getWipesQty() { return wipesQty; }
    public void setWipesQty(int qty) { this.tubingQty = qty; }

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

    // Validation
    public boolean validateQuantities() { return quantity <= 35; }
    public String getQuantityWarning() {
    	return  "Medicare requires a medical justification<br/>"
    		+   "for quantities greater than 35. Approval is<br/>"
			+   "not guaranteed. Place order anyway?<br/><br/>";
    }
}
