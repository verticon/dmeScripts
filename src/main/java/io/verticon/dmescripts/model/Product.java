package io.verticon.dmescripts.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="Products")
//@EntityListeners(ProductListener.class)
public class Product {
	private static Long idCounter = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String insurance;

	@OneToMany(mappedBy = "product")
	private List<Order> orders;

	public Product() {}

	public Product(String name, String insurance) {
		id = idCounter++;
		this.name = name;
		this.insurance = insurance;
	}
	

	public Long getId() {
    	return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public List<Order> getOrders() {
    	return orders;
    }

    public void setOrders(List<Order> orders) {
    	this.orders = orders;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Product.class.isAssignableFrom(that.getClass())) { return false; }

        final Product thatProduct = (Product) that;
        return thatProduct.getId() == this.getId();
    }

    @Override
    public String toString() {
        return String.format("%s: Name=%s Insurance=%s", Product.class.getSimpleName(), name, insurance);
    }

}
