package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Trip extends Entity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String arrival;

	private String departure;

	private String destination;

	private String origin;
    
	private String description;
	
	private Integer maxheight;
	
	private Integer maxlength;
	
	private Integer maxweight;
	
	private Integer maxwidth;

	private String userName;

    private List<String> categoryNames;

	private List<byte[]> fileOutputs;

	private List<String> photoPaths;

	private String subscriberId;

	private String languageCode;

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public Integer getMaxheight() {
		return maxheight;
	}

	public void setMaxheight(Integer maxheight) {
		this.maxheight = maxheight;
	}

	public Integer getMaxlength() {
		return maxlength;
	}

	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	public Integer getMaxweight() {
		return maxweight;
	}

	public void setMaxweight(Integer maxweight) {
		this.maxweight = maxweight;
	}

	public Integer getMaxwidth() {
		return maxwidth;
	}

	public void setMaxwidth(Integer maxwidth) {
		this.maxwidth = maxwidth;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

	public void setFileOutputs(List<byte[]> fileOutputs) {
		this.fileOutputs = fileOutputs;
	}

	public List<byte[]> getFileOutputs() {
		return fileOutputs;
	}


	public void setPhotoPaths(List<String> photoPaths) {
		this.photoPaths = photoPaths;
	}

	public List<String> getPhotoPaths() {
		return photoPaths;
	}


	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
}
