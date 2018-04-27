package io.verticon.dmescripts.model;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PatientListener {

	private static List<PatientListenerSubscriber> subscribers = new ArrayList<PatientListenerSubscriber>();
	public static void addSubscriber(PatientListenerSubscriber subscriber) { subscribers.add(subscriber); }
	
    @PrePersist
 	public void methodInvokedBeforePersist(Patient patient) {
 		report(patient, "Before Persist");
 	}

 	@PostPersist
 	public void methodInvokedAfterPersist(Patient patient) {
 		report(patient, "After Persist");
 		subscribers.forEach(subscriber -> subscriber.inserted(patient) );
 	}

 	@PreUpdate
 	public void methodInvokedBeforeUpdate(Patient patient) {
 		report(patient, "Before Update");
 	}

 	@PostUpdate
 	public void methodInvokedAfterUpdate(Patient patient) {
 		report(patient, "After Update");
 		subscribers.forEach(subscriber -> subscriber.updated(patient) );
 	}

 	@PreRemove
 	private void methodInvokedBeforeRemove(Patient patient) {
 		report(patient, "Before Remove");
 	}

 	@PostRemove
 	public void methodInvokedAfterRemove(Patient patient) {
 		report(patient, "After Remove");
 		subscribers.forEach(subscriber -> subscriber.removed(patient) );
 	}

 	private void report(Patient patient, String action) {
 		//System.out.printf("%d %s: patient = %s\n", System.identityHashCode(this), action, patient.getName());
 	}
}
