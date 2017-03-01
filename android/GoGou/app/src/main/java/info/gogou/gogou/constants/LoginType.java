package info.gogou.gogou.constants;

public enum LoginType {

	WECHAT("wechat"),
	QQ("qq"),
	SINA("sina"),
	FACEBOOK("facebook"),
	NORMAL("normal");
	
	private String value;
	
	private LoginType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}

}
