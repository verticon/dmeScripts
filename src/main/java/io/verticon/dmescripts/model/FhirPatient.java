package io.verticon.dmescripts.model;

import java.util.Date;

import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.Enumerations;

public class FhirPatient {

	public enum Gender {
		MALE, FEMALE;
		
		public String toString() {
	        return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

	public static final String system = "http://dmw.levy.com/mrn";

    private org.hl7.fhir.dstu3.model.Patient patient;

    public FhirPatient(org.hl7.fhir.dstu3.model.Patient patient) { this.patient = patient; }

	public FhirPatient(String firstName, String lastName, Gender gender, Date birthDate) {
		patient = new org.hl7.fhir.dstu3.model.Patient();

		patient.addName().setFamily(lastName).addGiven(firstName);
		patient.addIdentifier().setSystem(system).setValue("12345");
		patient.setGender(gender == Gender.MALE ? Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE);
		patient.setBirthDateElement(new DateType(birthDate));
	}

	public org.hl7.fhir.dstu3.model.Patient getFhirPatient() { return patient; }
	
    public Long getId() {
    	return new Long(patient.getIdElement().getIdPart());
    }
    
    public String getFirstName() {
        return patient.getName().get(0).getGivenAsSingleString();
    }

    public String getLastName() {
        return patient.getName().get(0).getFamily();
    }

    public String getFullName() {
        return patient.getName().get(0).getNameAsSingleString();
    }

    public Date getBirthDate() {
        return patient.getBirthDate();
    }

    public Gender getGender() {
        return patient.getGender() == Enumerations.AdministrativeGender.MALE ? Gender.MALE : Gender.FEMALE;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!FhirPatient.class.isAssignableFrom(that.getClass())) { return false; }

        final FhirPatient thatPatient = (FhirPatient) that;
        return thatPatient.getId() == this.getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", FhirPatient.class.getSimpleName(), getFullName());
    }
}
