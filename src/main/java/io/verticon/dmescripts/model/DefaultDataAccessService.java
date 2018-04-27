package io.verticon.dmescripts.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import io.verticon.dmescripts.Factory;

public class DefaultDataAccessService implements DataAccessService {

	// ************************** Patients ********************************

	@Override
	public List<Patient> getPatients() {
        EntityManager manager = Factory.emf.createEntityManager();
        TypedQuery<Patient> query = manager.createQuery("select t from Patient t", Patient.class);
        List<Patient> patients = query.getResultList();
        manager.close();
 		return patients;
	}

	@Override
	public void addPatient(Patient patient) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.persist(patient);
        transaction.commit();
        manager.close();
	}

	@Override
	public void removePatient(Patient patient) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.remove(manager.getReference(Patient.class, patient.getId()));
        transaction.commit();
        manager.close();
	}

	// ************************** Products ********************************

	@Override
	public List<Product> getProducts() {
        EntityManager manager = Factory.emf.createEntityManager();
        TypedQuery<Product> query = manager.createQuery("select t from Product t", Product.class);
        List<Product> products = query.getResultList();
        manager.close();
 		return products;
	}

	@Override
	public void addProduct(Product product) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.persist(product);
        transaction.commit();
        manager.close();
	}

	@Override
	public void removeProduct(Product product) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.remove(manager.getReference(Product.class, product.getId()));
        transaction.commit();
        manager.close();
	}

	// ************************** Orders ********************************

	@Override
	public List<Order> getOrders() {
        EntityManager manager = Factory.emf.createEntityManager();
        TypedQuery<Order> query = manager.createQuery("select t from Order t", Order.class);
        List<Order> orders = query.getResultList();
        manager.close();
 		return orders;
	}

	@Override
	public void addOrder(Order order, Patient patient, Product product) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        order.setPatient(patient);
        patient.getOrders().add(order);
        order.setProduct(product);
        product.getOrders().add(order);
        manager.persist(order);
        transaction.commit();
        manager.close();

	}

	@Override
	public void removeOrder(Order order) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        manager.remove(manager.getReference(Order.class, order.getId()));
        transaction.commit();
        manager.close();
	}
}
