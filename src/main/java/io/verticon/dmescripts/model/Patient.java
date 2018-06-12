package io.verticon.dmescripts.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="Patients")
//@EntityListeners(PatientListener.class)
public class Patient {

	public enum Gender {
		MALE, FEMALE;
		
		public String toString() {
	        return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

	private static Long nextId = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Patient() {}

	public Patient(String firstName, String lastName, Gender gender, Date birthDate) {
		id = nextId++;
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
		setBirthDate(birthDate);
	}
	
    public Long getId() {
    	return id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
    	this.birthDate = adjust(birthDate);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Patient.class.isAssignableFrom(that.getClass())) { return false; }

        final Patient thatPatient = (Patient) that;
        return thatPatient.getId() == this.getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", Patient.class.getSimpleName(), getFullName());
    }

    private Date adjust(Date birthDate) { // Comes in as midnight, i.e. 00:00:00 dd/mm/yyyy
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(birthDate);
    	calendar.add(Calendar.HOUR_OF_DAY, 12); // Move it to noon, i.e. 12:00:00 dd/mm/yyyy
    	Date adjustedDate = calendar.getTime();

    	/*
    	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    	System.out.printf("\nOriginal Birthday (%s) = %s\n", formatter.getTimeZone().getDisplayName(),  formatter.format(birthDate));
    	System.out.printf("Adjusted Birthday (%s) = %s\n", formatter.getTimeZone().getDisplayName(),  formatter.format(adjustedDate));
    	formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    	System.out.printf("Original Birthday (%s) = %s\n", formatter.getTimeZone().getDisplayName(),  formatter.format(birthDate));
    	System.out.printf("Adjusted Birthday (%s) = %s\n", formatter.getTimeZone().getDisplayName(),  formatter.format(adjustedDate));
    	*/
    	
    	return adjustedDate;
    }
}