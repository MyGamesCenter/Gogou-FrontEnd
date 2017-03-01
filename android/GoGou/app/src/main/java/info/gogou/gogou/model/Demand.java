package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Demand extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productname;

	private Integer quantity;
	
	private String brand;
	
	private String description;
	
	private String destination;
	
	private String origin;
	
	private BigDecimal serviceFee;

	private BigDecimal acceptedPrice;

	private String userName;

	private String expectTime;

    private String categoryName;

    private String languageCode;

	private List<byte[]> fileOutputs;

	private String subscriberId;

	private List<String> photoPaths;

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getBrand() {
		return brand;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public BigDecimal getServiceFee() {
		return serviceFee;
	}

	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

    public BigDecimal getAcceptedPrice() {
        return this.acceptedPrice;
    }

    public void setAcceptedPrice(BigDecimal acceptedPrice) {
        this.acceptedPrice = acceptedPrice;
    }

	public String getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(String expectTime) {
		this.expectTime = expectTime;
	}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

    public List<String> getPhotoPaths() {
        return photoPaths;
    }

    public void setPhotoPaths(List<String> photoPaths) {
        this.photoPaths = photoPaths;
    }

}
