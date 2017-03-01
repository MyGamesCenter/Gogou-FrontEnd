package info.gogou.gogou.model;

import java.io.Serializable;

public class Friend extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String emailAddress;

	private String userName;
	
	private boolean isPurchaser = false;
	
	private int age;

    // Date and time of last login
	private String last_login;

	private String gender;

	private String avatar;

	
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmailAddress() {
		return emailAddress;
	}

	
	public void setUserName(final String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	
	public void setIsPurchaser() {
		this.isPurchaser = true;
	}
	public boolean getIsPurchaser() {
		return isPurchaser;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return age;
	}

	public String getLastLogin() {
		return last_login;
	}
	public void setLastLogin(String date) {
		this.last_login = date;
	}

	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setGender(final String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}
}
