package info.gogou.gogou.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Created by grace on 16-3-17.
 */
public class Order extends Entity implements Serializable{

    private static final long serialVersionUID = 1L;

    private String username;

    private Long tripId;

    private OrderStatus orderStatus;

    private BigDecimal serviceFee;

    private List<OrderDescription> orderDescriptions;

    private Long currencyId;

    private BigDecimal currencyValue;

    private Locale locale;

    private BigDecimal orderTotal;

    private String orderType;

    private String paymentModuleCode;

    private PaymentType paymentType;

    private String shippingModuleCode;

    private String sellerId;

    private Address address;

    public String getUsername() {
        return username;
    }

    public Long getTripId() {
        return tripId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public List<OrderDescription> getOrderDescriptions() {
        return orderDescriptions;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public BigDecimal getCurrencyValue() {
        return currencyValue;
    }

    public Locale getLocale() {
        return locale;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getPaymentModuleCode() {
        return paymentModuleCode;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public String getShippingModuleCode() {
        return shippingModuleCode;
    }
    

    public void setShippingModuleCode(String shippingModuleCode) {
        this.shippingModuleCode = shippingModuleCode;
    }

    public void setPaymentModuleCode(String paymentModuleCode) {
        this.paymentModuleCode = paymentModuleCode;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setCurrencyValue(BigDecimal currencyValue) {
        this.currencyValue = currencyValue;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public void setOrderDescriptions(List<OrderDescription> orderDescriptions) {
        this.orderDescriptions = orderDescriptions;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}


