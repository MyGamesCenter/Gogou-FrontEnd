package info.gogou.gogou.payment;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import info.gogou.gogou.R;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderDescription;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.model.PaymentMethodList;
import info.gogou.gogou.model.SignatureRequest;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.OrderUtils;
import info.gogou.gogou.utils.RESTRequestUtils;

/**
 * Created by Letian_Xu on 6/16/2016.
 */
public class AliPayment implements Payment {
    private static final String TAG = "AliPayment";

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    private static final String PARTNER = "2088221737897644";

    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    private static final String SELLER = "letianxu@gmail.com";

    // 字符编码格式 目前支持 gbk 或 utf-8
    private static final String INPUT_CHARSET = "utf-8";

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler;
    private Activity mContext;
    private PaymentResultListener mListener;

    public AliPayment(Activity context) {
        mContext = context;
        mHandler = new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PaymentResult payResult = parsePaymentResult((String) msg.obj);
                        if (mListener != null) {
                            if (payResult.getStatus() == PaymentResult.PaymentStatus.FAILURE)
                                mListener.onPaymentFail(payResult);
                            else
                                mListener.onPaymentSuccess(payResult);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void payOrder(Order order, PaymentResultListener listener) {
        mListener = listener;
        final String payRequest = getAliPayRequest(order);
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setContent(payRequest);
        RESTRequestUtils.performRSASignatureRequest(signatureRequest, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                if (mListener != null) {
                    PaymentResult result = new PaymentResult();
                    result.setStatus(PaymentResult.PaymentStatus.FAILURE);
                    mListener.onPaymentFail(result);
                }
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse response) {
                if (response.getErrorType() != ErrorType.NONE) {
                    if (mListener != null) {
                        PaymentResult result = new PaymentResult();
                        result.setStatus(PaymentResult.PaymentStatus.FAILURE);
                        mListener.onPaymentFail(result);
                    }
                } else {
                    String signature = response.getMessage();
                    Log.d(TAG, "Remote signature is: " + signature);

                    try {
                        /**
                         * 仅需对sign 做URL编码
                         */
                        signature = URLEncoder.encode(signature, INPUT_CHARSET);
                    } catch (UnsupportedEncodingException e) {
                        if (mListener != null) {
                            PaymentResult result = new PaymentResult();
                            result.setStatus(PaymentResult.PaymentStatus.FAILURE);
                            mListener.onPaymentFail(result);
                        }
                        return;
                    }

                    /**
                     * 完整的符合支付宝参数规范的订单信息
                     */
                    final String payInfo = payRequest + "&sign=\"" + signature + "\"&" + getSignType();
                    Log.d(TAG, "Alipay request param: " + payInfo);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(mContext);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(payInfo, true);

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }
        });
    }

    @Override
    public PaymentResult parsePaymentResult(String rawResult) {

        if (TextUtils.isEmpty(rawResult))
            return null;

        String[] resultParams = rawResult.split(";");
        PaymentResult paymentResult = new PaymentResult();
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                String resultStatus = gatValue(resultParam, "resultStatus");
                if (TextUtils.equals(resultStatus, "9000")) {
                    paymentResult.setStatus(PaymentResult.PaymentStatus.SUCCESS);
                } else if (TextUtils.equals(resultStatus, "8000")) {
                    paymentResult.setStatus(PaymentResult.PaymentStatus.PROCESSING);
                } else {
                    paymentResult.setStatus(PaymentResult.PaymentStatus.FAILURE);
                }
            }
            if (resultParam.startsWith("result")) {
                String result = gatValue(resultParam, "result");
                paymentResult.setResult(result);
            }
            if (resultParam.startsWith("memo")) {
                String memo = gatValue(resultParam, "memo");
                paymentResult.setMemo(memo);
            }
        }
        return paymentResult;
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }

    private static String getAliPayRequest(Order order) {

        OrderDescription orderDescription = order.getOrderDescriptions().get(0);

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getId() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + orderDescription.getProductName() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" +
                ((orderDescription.getDescription() != null && !orderDescription.getDescription().isEmpty()) ?
                        orderDescription.getDescription() : "dummy") +
                "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=" + "\"" + INPUT_CHARSET + "\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        //orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private static String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
