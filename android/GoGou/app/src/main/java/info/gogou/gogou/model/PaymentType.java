package info.gogou.gogou.model;

/**
 * Created by grace on 16-3-17.
 */
public enum PaymentType {

    CREDITCARD("creditcard"),
    PAYPAL("paypal"),
    ALIPAY("alipay");

    private String _value;

    private PaymentType(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

}