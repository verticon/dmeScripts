package io.verticon.dmescripts.model;

import java.util.List;

public interface DataAccessService {

	List<Patient> getPatients();
	void addPatient(Patient patient);
	void removePatient(Patient patient);
	
	List<Product> getProducts();
	void addProduct(Product product);
	void removeProduct(Product product);
	
	List<Order> getOrders();
	void addOrder(Order order, Patient patient, Product product);
	void removeOrder(Order order);
}
