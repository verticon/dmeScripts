package io.verticon.dmescripts.model;

import javax.persistence.*;

import io.verticon.dmescripts.controllers.Product;

@Entity
@Table(name="Orders")
public class Order {

	private static Long nextId = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Product.Category category;
    
    private String hcpc;
    private int quantity;
    private String patientId;
    private String details; // JSON

	public Order() {}

	public Order(Product.Category category, String hcpc, int quantity, String patientId, String details) {
		id = nextId++;
		this.category = category;
		this.hcpc = hcpc;
		this.quantity = quantity;
		this.patientId = patientId;
		this.details = details;
	}

    public Long getId() {
    	return id;
    }
    
    public Product.Category getCategory() {
		return category;
    }

    public void setCategory(Product.Category category) {
		this.category = category;
    }

    public String getHcpc() {
		return hcpc;
    }

    public void setHcpc(String hcpc) {
		this.hcpc = hcpc;
    }

    public int getQuantity() {
		return quantity;
    }

    public void setQuantity(int quantity) {
		this.quantity = quantity;
    }

    public String getPatientId() {
		return patientId;
    }

    public void setPatientId(String patientId) {
		this.patientId = patientId;
    }

    public String getDetails() {
		return details;
    }

    public void setDetails(String details) {
		this.details = details;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Order.class.isAssignableFrom(that.getClass())) { return false; }

        final Order thatOrder = (Order) that;
        return thatOrder.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return String.format("%s - hcpc: %s, quantity: %d, patient id: %s, details: %s\n", Order.class.getSimpleName(), getHcpc(), getQuantity(), getPatientId(), getDetails());
    }
}
