package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscriber extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String emailAddress;
	
	private String encodedPassword = null;
	
	private String clearPassword = null;

	private String userName;
	
	private boolean isPurchaser = true;
	
	private int age;
	
	private String firstName;
	
	private String lastName;
	
	private String gender;

	private String gcmToken;

	private String regCode;

    private String headImage;

	private String imToken;
	
	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getClearPassword() {
		return clearPassword;
	}

	public void setClearPassword(String clearPassword) {
		this.clearPassword = clearPassword;
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

	public void setGender(final String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }


    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

	public void setImToken(String imToken) {
		this.imToken = imToken;
	}

	public String getImToken() {
		return imToken;
	}

}
