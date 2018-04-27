package io.verticon.dmescripts.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.verticon.dmescripts.Factory;

public class DummyDataAccessService implements DataAccessService {

	private List<Patient> patients = new ArrayList<Patient>();
	private List<Product> products = new ArrayList<Product>();
	private List<Order> orders = new ArrayList<Order>();
	
	public DummyDataAccessService() {
		Random random = new Random();

		makeEntries("Robert", "Vaessen", "30/01/1954", Patient.Gender.MALE, "Bandage", random);
		makeEntries("Ravi", "Thakkar", "23/12/1971", Patient.Gender.MALE, "Catheter", random);
	}
	
	private void makeEntries(String firstName, String lastName, String birthDay, Patient.Gender gender, String productName, Random random) {
		List<String> insuranceCompanies = Factory.insuranceCompanies;
		String company = insuranceCompanies.get(random.nextInt(insuranceCompanies.size()));

        Date birthDate;
        try { birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(birthDay); }
        catch (Exception e) { birthDate = new Date(); }

		Patient patient = new Patient(firstName, lastName, gender, birthDate, company);;
		patients.add(patient);
		
		Product product = new Product(productName, company);;
		products.add(product);

		orders.add(new Order(patient, product));
	}

	// ************************** Patients ********************************

	@Override
	public List<Patient> getPatients() {
 		return patients;
	}

	@Override
	public void addPatient(Patient patient) {
		patients.add(patient);
	}

	@Override
	public void removePatient(Patient patient) {
		patients.remove(patient);
	}

	// ************************** Products ********************************

	@Override
	public List<Product> getProducts() {
 		return products;
	}

	@Override
	public void addProduct(Product product) {
		products.add(product);
	}

	@Override
	public void removeProduct(Product product) {
		products.remove(product);
	}

	// ************************** Orders ********************************

	@Override
	public List<Order> getOrders() {
 		return orders;
	}

	@Override
	public void addOrder(Order order, Patient patient, Product product) {
        order.setPatient(patient);
        patient.getOrders().add(order);
        order.setProduct(product);
        product.getOrders().add(order);
        orders.add(order);
	}

	@Override
	public void removeOrder(Order order) {
		orders.remove(order);
	}
}
