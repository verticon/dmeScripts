package io.verticon.dmescripts.model;

import javax.persistence.*;

@Entity
@Table(name="Orders")
public class Order {

	private static Long nextId = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jsonDocument;

	public Order() {}

	public Order(String jsonDocument) {
		id = nextId++;
		this.jsonDocument = jsonDocument;
	}

    public Long getId() {
    	return id;
    }
    
    public String getDocument() {
		return jsonDocument;
    }

    public void setDocument(String jsonDocument) {
		this.jsonDocument = jsonDocument;
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
        return String.format("%s: %s\n", Order.class.getSimpleName(), getDocument());
    }
}
