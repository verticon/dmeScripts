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

    // ********************************* The Products Table ***************************************

    private List<Product> table;

    private void initTable() { table = dataService.getProducts(); }

    public List<Product> getTable() { return table; }
}

