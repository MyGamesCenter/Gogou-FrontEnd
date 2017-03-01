package info.gogou.gogou.model;

import java.io.Serializable;


public class DemandFilter extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userName;

	private Integer currentPage;

	private Integer pageSize;

	private String serviceFeeOrder;

	private String ratingOrder;
	
	private String productname;
	
	private String destination;
	
	private String origin;

	private String categoryName;

	private String languageCode;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
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

	public String getServiceFeeOrder() {
		return serviceFeeOrder;
	}

	public void setServiceFeeOrder(String serviceFeeOrder) {
		this.serviceFeeOrder = serviceFeeOrder;
	}

	public String getRatingOrder() {
		return ratingOrder;
	}

	public void setRatingOrder(String ratingOrder) {
		this.ratingOrder = ratingOrder;
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

}
