package io.verticon.dmescripts.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import io.verticon.dmescripts.Factory;

public class DefaultDataAccessService implements DataAccessService {

	// ************************** Patients ********************************

	private String fhirServerUrl = "http://fhirtest.uhn.ca/baseDstu3";
	private FhirContext ctx = FhirContext.forDstu3();
	private IGenericClient client = ctx.newRestfulGenericClient(fhirServerUrl);
	private IParser parser = ctx.newXmlParser().setPrettyPrint(true);

	@Override
	public List<Patient> getPatients() {
		List<Patient> patients = new ArrayList<Patient>();

		try {
			ICriterion<TokenClientParam> criterion = org.hl7.fhir.dstu3.model.Patient.IDENTIFIER.hasSystemWithAnyCode(Patient.system);
			Bundle bundle = client.search().forResource(org.hl7.fhir.dstu3.model.Patient.class).where(criterion).returnBundle(Bundle.class).execute();

			for (BundleEntryComponent entry : bundle.getEntry()) {
				if (entry.getResource().getResourceType() == ResourceType.Patient) {
					org.hl7.fhir.dstu3.model.Patient fhirPatient = (org.hl7.fhir.dstu3.model.Patient) entry.getResource();
					Patient patient = new Patient(fhirPatient);
					patients.add(patient);
					//System.out.printf("\nRetrieved patient %s\n%s\n", patient.getFullName(), parser.encodeResourceToString(fhirPatient));
				}
			}
		}
		catch(Exception e) {
			System.out.printf("Cannot find patients for %s\n%s\n", Patient.system, e.getMessage());
		}

		return patients;
	}
	
	@Override
	public void addPatient(Patient patient) {
		org.hl7.fhir.dstu3.model.Patient fhirPatient = patient.getFhirPatient();
		MethodOutcome outcome = client.create().resource(fhirPatient).execute();
		//System.out.printf("\nAdded %s (patient id = %s)\n%s\n", patient.getFullName(), outcome.getId().getIdPart(), parser.encodeResourceToString(fhirPatient));
	}

	@Override
	public void removePatient(Patient patient) {
		org.hl7.fhir.dstu3.model.Patient fhirPatient = patient.getFhirPatient();
		OperationOutcome outcome = (OperationOutcome) client.delete().resourceById(new IdDt("Patient", patient.getId())).execute();
		//System.out.printf("\nRemoved %s (patient id = %s)\n%s\n", patient.getFullName(), outcome.getId(), parser.encodeResourceToString(fhirPatient));
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
	public void addOrder(Order order) {
        EntityManager manager = Factory.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
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
