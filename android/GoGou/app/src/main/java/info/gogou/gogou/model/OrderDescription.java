package info.gogou.gogou.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by grace on 16-3-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDescription extends Entity implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal finalPrice;

    private String productName;

    private Integer quantity;

    private String brand;

    private String origin;

    private String name;

    private String title;

    private String description;

    private String languageCode;

    private String categoryName;

    public String getTitle() {
        return title;
    }

    public String getOrigin() {
        return origin;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getMinPrice() {
        return this.minPrice;
    }

    public BigDecimal getMaxPrice() {
        return this.maxPrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public BigDecimal getFinalPrice() {
        return this.finalPrice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

}