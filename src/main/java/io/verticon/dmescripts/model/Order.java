package io.verticon.dmescripts.model;

import javax.persistence.*;

@Entity
@Table(name="Orders")
public class Order {
	
	private static Long idCounter = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
	@JoinColumn(name = "patientid")
    private Patient patient;

	@ManyToOne
	@JoinColumn(name = "productid")
    private Product product;

	public Order() {}

	public Order(Patient patient, Product product) {
		id = idCounter++;
		this.patient = patient;
		this.product = product;
	}

    public Long getId() {
    	return id;
    }
    
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return String.format("%s - %s, %s", Order.class.getSimpleName(), patient, product);
    }

}
