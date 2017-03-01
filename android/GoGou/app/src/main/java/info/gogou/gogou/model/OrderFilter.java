package info.gogou.gogou.model;

import java.io.Serializable;

/**
 * Created by grace on 16-3-25.
 */
public class OrderFilter extends Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;

    private Long tripId;

    private OrderStatus orderStatus;

    private String orderType;

    private Integer currentPage;

    private Integer pageSize;

    private String sellerId;

    public String getUsername() {
        return username;
    }

    public Long getTripId() {
        return tripId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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


    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
