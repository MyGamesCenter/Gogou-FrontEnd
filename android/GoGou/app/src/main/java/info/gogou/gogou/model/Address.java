package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String userName;
	
	private String city;

	private String company;

	private String firstName;

	private String lastName;

	private String postcode;

	private String state;

	private String streetAddress;

	private String telephone;
	
	private String country;

	public String getUserName() {
		return userName;
	}

	public String getCity() {
		return city;
	}

	public String getCompany() {
		return company;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getState() {
		return state;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
