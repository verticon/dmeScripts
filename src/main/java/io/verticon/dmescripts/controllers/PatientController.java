package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import io.verticon.dmescripts.*;
import io.verticon.dmescripts.model.*;

@Named
@ViewScoped
public class PatientController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DataAccessService dataService = Factory.sDataService;

    @PostConstruct
    public void init() {
        initTable();
    }

    // ********************************* Add New Patient ***************************************

    private Patient newPatient;

    public String addNew() {
    	newPatient = new Patient();
        return null;
    }

    public Patient.Gender[] getGenders() { return Patient.Gender.values(); }

    public Patient getNewPatient() { return newPatient; }

    public String save() {
    	dataService.addPatient(newPatient); 
        table.add(newPatient);
        cancel();
        return null;
    }

    public String cancel() {
    	newPatient = null;
        return null;
    }

    public List<String> getInsuranceCompanies() {
        return Factory.insuranceCompanies;
    }

    public boolean getAddingNew() { return newPatient != null; }

    // ********************************* The Patients Table ***************************************

    private Patient target;
    private List<Patient> table = new ArrayList<Patient>();

    private void initTable() { table = dataService.getPatients(); }

    public List<Patient> getTable() { return table; }

    public void setTarget(Patient patient) { target = patient; }

    public String delete() {
    	dataService.removePatient(target);
        table.remove(target);
        return null;
    }

}
