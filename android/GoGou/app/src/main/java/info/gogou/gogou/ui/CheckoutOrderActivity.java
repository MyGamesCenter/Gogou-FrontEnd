package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderDescription;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.model.PaymentMethodList;
import info.gogou.gogou.model.PaymentType;
import info.gogou.gogou.payment.AliPayment;
import info.gogou.gogou.payment.PaymentResult;
import info.gogou.gogou.payment.PaymentResultListener;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class CheckoutOrderActivity extends RoboActionBarActivity {

    private static final String TAG = "CheckoutOrderActivity";

    // Views
    @InjectView(R.id.checkOutBackButton)
    private Button mBackBtn;
    @InjectView(R.id.orderNumber)
    private TextView mOrderNumber;
    @InjectView(R.id.trip_category1_edit)
    private TextView mTripCategory1;
    @InjectView(R.id.trip_category2_edit)
    private TextView mTripCategory2;
    @InjectView(R.id.trip_category3_edit)
    private TextView mTripCategory3;
    @InjectView(R.id.orderDetailProductName)
    private TextView mProductName;
    @InjectView(R.id.orderDetailProductBrand)
    private TextView mBrand;
    @InjectView(R.id.orderDetailDescription)
    private TextView mDescription;
    @InjectView(R.id.orderQuantity)
    private TextView mQuantity;
    @InjectView(R.id.orderPrice)
    private TextView mOrderPrice;
    @InjectView(R.id.buyerPriceRange)
    private RelativeLayout mPriceRangeLayout;
    @InjectView(R.id.finalPriceArea)
    private RelativeLayout mfinalPriceLayout;
    @InjectView(R.id.payActionBtn)
    private Button mPayButton;
    @InjectView(R.id.alipayLayout)
    private RelativeLayout mAliPayLayout;
    @InjectView(R.id.otherPaymentLayout)
    private RelativeLayout mOtherPayLayout;
    @InjectView(R.id.alipayRBtn)
    private RadioButton mAliPayRBtn;
    @InjectView(R.id.otherPaymentRBtn)
    private RadioButton mOtherPayRBtn;


    private ProgressDialog mProgressSpinner;
    private Order mOrder;
    private String mPayRequest;
    private boolean mIsLoading = false;

    private PaymentMethodList mPaymentList = new PaymentMethodList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_order_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mTripCategory1.setVisibility(View.GONE);
        mTripCategory2.setVisibility(View.GONE);
        mTripCategory3.setVisibility(View.GONE);
        mPriceRangeLayout.setVisibility(View.GONE);
        mfinalPriceLayout.setVisibility(View.VISIBLE);

        Intent order_detail_intent = this.getIntent();
        Bundle bundle = order_detail_intent.getExtras();
        mOrder = (Order)bundle.getSerializable(GoGouIntents.ORDER);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View.OnClickListener onClickRadioButton = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                switch(view.getId())
                {
                    case R.id.alipayLayout:
                    case R.id.alipayRBtn:
                    {
                        mAliPayRBtn.setChecked(true);
                        mOtherPayRBtn.setChecked(false);
                    }
                    break;
                    case R.id.otherPaymentLayout:
                    case R.id.otherPaymentRBtn:
                    {
                        mAliPayRBtn.setChecked(false);
                        mOtherPayRBtn.setChecked(true);
                    }
                    break;
                    default:
                }
            }
        };

        mAliPayLayout.setOnClickListener(onClickRadioButton);
        mOtherPayLayout.setOnClickListener(onClickRadioButton);
        mAliPayRBtn.setOnClickListener(onClickRadioButton);
        mOtherPayRBtn.setOnClickListener(onClickRadioButton);

        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLoading) return;

                if (mAliPayRBtn.isChecked())
                {
                    mIsLoading = true;
                    mProgressSpinner.show();

                    AliPayment payment = new AliPayment(CheckoutOrderActivity.this);
                    payment.payOrder(mOrder, new PaymentResultListener() {
                        @Override
                        public void onPaymentSuccess(PaymentResult result) {
                            mProgressSpinner.dismiss();
                            mIsLoading = false;
                            if (result.getStatus() == PaymentResult.PaymentStatus.PROCESSING)
                                Toast.makeText(CheckoutOrderActivity.this, R.string.notif_payment_in_process, Toast.LENGTH_LONG).show();
                            else {
                                Toast.makeText(CheckoutOrderActivity.this, R.string.notif_payment_succeed, Toast.LENGTH_LONG).show();
                                mOrder.setOrderStatus(OrderStatus.ORDERED);
                                mOrder.setPaymentType(PaymentType.ALIPAY);
                                performUpdateRequest(mOrder);
                            }
                        }

                        @Override
                        public void onPaymentFail(PaymentResult result) {
                            Toast.makeText(CheckoutOrderActivity.this, R.string.notif_payment_succeed, Toast.LENGTH_LONG).show();
                            mProgressSpinner.dismiss();
                            mIsLoading = false;
                        }
                    });
                }
                else
                {
                    if (mPaymentList.isEmpty()) {
                        Toast.makeText(CheckoutOrderActivity.this, R.string.notif_get_payment_list_failed, Toast.LENGTH_LONG).show();
                        return;
                    }
                    // To be continued
                }
            }
        });

        performRequest();
    }

    private void performRequest() {
        if (mIsLoading) return;

        mIsLoading = true;
        mProgressSpinner.show();

        RESTRequestUtils.performGetOrderRequest(mOrder.getId(), new RESTRequestListener<Order>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(CheckoutOrderActivity.this, R.string.notif_get_order_list_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
                mIsLoading = false;
            }

            @Override
            public void onGogouRESTRequestSuccess(Order order) {

                mOrder = order;
                List<OrderDescription> orderDescriptions = order.getOrderDescriptions();
                OrderDescription orderDescription = orderDescriptions.get(0);

                mOrderNumber.setText(String.valueOf(mOrder.getId()));
                mProductName.setText(orderDescription.getProductName());
                mBrand.setText(orderDescription.getBrand());
                mDescription.setText(orderDescription.getDescription());
                mQuantity.setText(String.valueOf(orderDescription.getQuantity()));
                mOrderPrice.setText(String.valueOf(order.getOrderTotal()));

                Category category = CacheManager.findCategoryByName(orderDescription.getCategoryName());
                mTripCategory1.setText(category.getDisplayName());
                mTripCategory1.setVisibility(View.VISIBLE);

                mProgressSpinner.dismiss();
                mIsLoading = false;

                /*RESTRequestUtils.performPaymentMethodListRequest(new RESTRequestListener<PaymentMethodList>() {
                    @Override
                    public void onGogouRESTRequestFailure() {
                        Toast.makeText(CheckoutOrderActivity.this, R.string.notif_get_payment_list_failed, Toast.LENGTH_LONG).show();
                        mProgressSpinner.dismiss();
                        mIsLoading = false;
                    }

                    @Override
                    public void onGogouRESTRequestSuccess(PaymentMethodList paymentMethodListList) {

                        if (paymentMethodListList.isEmpty()) {
                            Toast.makeText(CheckoutOrderActivity.this, R.string.notif_get_payment_list_failed, Toast.LENGTH_LONG).show();
                        } else  {
                            mPaymentList.addAll(paymentMethodListList);
                            mPaymentListAdapter.notifyDataSetChanged();
                        }

                        mProgressSpinner.dismiss();
                        mIsLoading = false;
                    }
                });*/
            }
        });
    }

    private void performUpdateRequest(Order order) {

        if (mIsLoading) return;

        mProgressSpinner.show();
        mIsLoading = true;
        RESTRequestUtils.performOrderUpdateRequest(order, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(CheckoutOrderActivity.this, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
                mIsLoading = false;
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse response) {
                if (response.getErrorType() != ErrorType.NONE) {
                    Toast.makeText(CheckoutOrderActivity.this, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CheckoutOrderActivity.this, R.string.notif_update_order_succeed, Toast.LENGTH_LONG).show();
                }

                mIsLoading = false;
                mProgressSpinner.dismiss();
            }
        });
    }
}
