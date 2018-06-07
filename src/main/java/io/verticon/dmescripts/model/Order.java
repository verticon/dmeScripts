package io.verticon.dmescripts.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="Orders")
public class Order {

	@Embeddable
	public static class Item {
		public String hcpc;
		public int quantity;
		
		public Item() {}
		
		public Item(String hcpc, int quantity) {
			this.hcpc = hcpc;
			this.quantity = quantity;
		}
	}

	private static Long idCounter = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientId;

    @ElementCollection
    @CollectionTable(name="ITEMS", joinColumns=@JoinColumn(name="ORDER_ID"))
    private List<Item> items;

	public Order() {}

	public Order(String patientId, List<Item> items) {
		id = idCounter++;
		this.patientId = patientId;
		this.items = items;
	}

    public Long getId() {
    	return id;
    }
    
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setProduct(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Order.class.isAssignableFrom(that.getClass())) { return false; }

        final Order thatOrder = (Order) that;
        return thatOrder.getId() == this.getId();
    }

    @Override
    public String toString() {
        return String.format("%s - %s, %s", Order.class.getSimpleName(), patientId, items);
    }
}
