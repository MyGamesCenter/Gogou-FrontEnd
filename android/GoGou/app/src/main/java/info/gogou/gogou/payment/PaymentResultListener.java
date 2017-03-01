package info.gogou.gogou.payment;

/**
 * Created by Letian_Xu on 6/16/2016.
 */
public interface PaymentResultListener {

    void onPaymentSuccess(PaymentResult result);

    void onPaymentFail(PaymentResult result);
}
