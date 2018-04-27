package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import io.verticon.dmescripts.Factory;
import io.verticon.dmescripts.model.*;

@Named
@ViewScoped
public class OrderController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DataAccessService dataService = Factory.sDataService;

    @PostConstruct
    public void init() {
        initPatients();
        initProducts();
        initOrders();
    }

    // ********************************* Add New Order ***************************************

	private Order newOrder;

	public String addNew() {
		newOrder = new Order();
        return null;
    }

    public String save() {
    	dataService.addOrder(newOrder, patient, product);
        table.add(newOrder);   
    	cancel();
    	
    	/*
		import javax.faces.context.ExternalContext;
		import javax.faces.context.FacesContext;
		import javax.servlet.http.HttpServletRequest;

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

        return null;
    }

    public String cancel() {
    	newOrder = null;
        return null;
    }

    public boolean getAddingNew() { return newOrder != null; }

    // ********************************* The Orders Table ***************************************

    private Order target;
    private List<Order> table;

    private void initOrders() { table = dataService.getOrders(); }

    public List<Order> getTable() { return table; }

    public void setTarget(Order order) { target = order; }

    public String delete() {
    	dataService.removeOrder(target);
        table.remove(target);
        return null;
    }

	/************************ The Patients Menu **********************************/

    private Patient patient;
    private List<Patient> patients;

    private void initPatients() {
    	patients = dataService.getPatients();
        if (patients.size() > 0) patient = patients.get(0);
    }

    public List<SelectItem> getPatients() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        patients.forEach(patient -> list.add(new SelectItem(patient.getId(), patient.getFullName())));
        return list;
    }

    public Long getPatientId() {
    	return patient == null ? 0 : patient.getId();
    }

    public void setPatientId(Long id) {
    	patients.forEach(patient -> {
    		if (patient.getId() == id) {
    	    	this.patient = patient;
    		}
    	});
    }

	/************************ The Products Menu **********************************/

	private Product product;
    private List<Product> products;

    private void initProducts() {
    	products = dataService.getProducts();
        if (products.size() > 0) product = products.get(0);
    }

    public List<SelectItem> getProducts() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        if (patient != null) {
            products.forEach(product -> {
            	if (product.getInsurance().equals(patient.getInsurance())) {
                    list.add(new SelectItem(product.getId(), product.getName()));
            	}
            });
        }
        return list;
    }

    public Long getProductId() {
    	return product == null ? 0 : product.getId();
    }

    public void setProductId(Long id) {
    	products.forEach(product -> {
    		if (product.getId() == id) {
    			this.product = product;
    		}
    	});
    }

}
