package es.udc.pa.pa006.cines.model.userservice;

public class UserProfileDetails {

	private String firstName;
	private String lastName;
	private String email;
	private String dni;

	public UserProfileDetails(String dni, String firstName, String lastName, String email) {
		this.dni = dni;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getDni() {
		return dni;
	}

}
