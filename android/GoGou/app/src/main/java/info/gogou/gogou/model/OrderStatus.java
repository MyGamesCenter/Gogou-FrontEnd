package info.gogou.gogou.model;

/**
 * Created by grace on 16-3-17.
 */
public enum OrderStatus {

    PREORDERED("preordered"), //a buyer creates an order first in preordered status
    CONFIRMED("confirmed"), // the order which was in preordered status should be confirmed by traveler as in confirmed status
    ORDERED("ordered"), // the confirmed order should be paid by buyer as in ordered status
    DELIVERED("delivered"), // the paid order should be notified to traveler to deliver, then the traveler delivers the good and the order is in delivered status
    COLLECTED("collected"), // the buyer receives the good, and changes the order status to collected
    REFUNDED("refunded"), // the buyer asks for refund, and is successful, order is in refunded status
    ;

    private String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
