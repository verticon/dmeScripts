package io.verticon.dmescripts.model;

public interface PatientListenerSubscriber {
	void inserted(Patient patient);
	void updated(Patient patient);
	void removed(Patient patient);
}
