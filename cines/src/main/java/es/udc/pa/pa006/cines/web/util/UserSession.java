package es.udc.pa.pa006.cines.web.util;


public class UserSession {

	private Long userProfileId;
	private String firstName;
	private String role;

	public Long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(Long userProfileId) {
		this.userProfileId = userProfileId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isAdmin() {
		return (role != null && role.equals("admin"));
	}
	
	public boolean isSalesman() {
		return (role != null && role.equals("salesman"));
	}

}
