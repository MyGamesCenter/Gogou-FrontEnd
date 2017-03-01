package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.File;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String imagePath;

	private String languageCode;

	private File image;

	private byte fileOutput[];

	private String username;

	private String displayName;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public File getImage() {
		return image;
	}

	public byte[] getFileOutput() {
		return fileOutput;
	}

	public String getUsername() {
		return username;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public void setFileOutput(byte[] fileOutput) {
		this.fileOutput = fileOutput;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
