package info.gogou.gogou.payment;

import info.gogou.gogou.model.Order;

/**
 * Created by Letian_Xu on 6/16/2016.
 */
public interface Payment {

    void payOrder(Order order, PaymentResultListener listener);

    PaymentResult parsePaymentResult(String rawResult);
}
