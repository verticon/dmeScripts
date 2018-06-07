package io.verticon.dmescripts.model;

import java.util.List;

public interface DataAccessService {
	
	List<Patient> getPatients();
	void addPatient(Patient patient);
	void removePatient(Patient patient);

	List<Order> getOrders();
	void addOrder(Order order);
	void removeOrder(Order order);
}
