package info.gogou.gogou.model;

public class RecoverPwdRequest {

    private String username;
    
    private String emailAddress;

    private String password;

    private String recoverCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getRecoverCode() {
		return recoverCode;
	}

	public void setRecoverCode(String recoverCode) {
		this.recoverCode = recoverCode;
	}
}
