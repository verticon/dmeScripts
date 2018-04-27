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
public class ProductController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DataAccessService dataService = Factory.sDataService;

    @PostConstruct
    public void init() {
        initTable();        
    }

    // ********************************* Add New Product ***************************************

	private Product newProduct;

    public String addNew() {
    	newProduct = new Product();
        return null;
    }

    public Product getNewProduct() { return newProduct; }

    public String save() {
    	dataService.addProduct(newProduct);
        table.add(newProduct);
        cancel();
        return null;
    }

    public String cancel() {
    	newProduct = null;
        return null;
    }

    public List<SelectItem> getInsuranceCompanies() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        Factory.insuranceCompanies.forEach(company -> list.add(new SelectItem(company, company)));
        return list;
    }

    public boolean getAddingNew() { return newProduct != null; }

    // ********************************* The Products Table ***************************************

    private Product target;
    private List<Product> table;

    private void initTable() { table = dataService.getProducts(); }

    public List<Product> getTable() { return table; }

    public void setTarget(Product product) { target = product; }

    public String delete() {
    	dataService.removeProduct(target);
        table.remove(target);
        return null;
    }

}

