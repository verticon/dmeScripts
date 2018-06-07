package io.verticon.dmescripts.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
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

    private boolean addingNew;
    private String firstName;
    private String lastName;
    private Patient.Gender gender;
    private Date birthday;

    public String addNew() {
    	firstName = null;
    	lastName = null;
    	gender = null;
    	birthday = null;
    	addingNew = true;
        return null;
    }

    public Patient.Gender[] getGenders() { return Patient.Gender.values(); }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Patient.Gender getGender() { return gender; }
    public void setGender(Patient.Gender gender) { this.gender = gender; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public String save() {
    	Patient patient = new Patient(firstName, lastName, gender, birthday);
    	dataService.addPatient(patient); 
        table.add(patient);
        return cancel();
    }

    public String cancel() {
    	addingNew = false;
        return null;
    }

    public boolean getAddingNew() { return addingNew; }

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
