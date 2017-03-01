package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Address;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderDescription;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class OrderDetailActivity extends RoboActionBarActivity {

    private static final String TAG = "OrderDetailActivity";

    // Views
    @InjectView(R.id.orderDetailBackButton)
    private Button mBackBtn;
    @InjectView(R.id.orderDetailCollectInfoText)
    private TextView mCollectInfo;
    @InjectView(R.id.orderDetailPhoneText)
    private TextView mPhoneText;
    @InjectView(R.id.orderDetailAddressText)
    private TextView mAddressText;
    @InjectView(R.id.departureTV)
    private TextView mTripFrom;
    @InjectView(R.id.destinationTV)
    private TextView mTripTo;
    @InjectView(R.id.departDateTV)
    private TextView mDepartDate;
    @InjectView(R.id.arrivalDateTV)
    private TextView mArrivalDate;
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
    @InjectView(R.id.buyerMinPrice)
    private TextView mMinPrice;
    @InjectView(R.id.buyerMaxPrice)
    private TextView mMaxPrice;
    @InjectView(R.id.orderPrice)
    private TextView mOrderPrice;
    @InjectView(R.id.buyerPriceRange)
    private RelativeLayout mPriceRangeLayout;
    @InjectView(R.id.finalPriceArea)
    private RelativeLayout mfinalPriceLayout;
    @InjectView(R.id.negotiatedPriceLayout)
    private LinearLayout mNegotiatedPriceLayout;
    @InjectView(R.id.negotiatedPrice)
    private EditText mFinalPrice;
    @InjectView(R.id.sellerConfirmOrder)
    private Button mSellerConfirmBtn;


    private ProgressDialog mProgressSpinner;
    private Order mOrder;
    private boolean isToConfirmOrder = false;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mTripCategory1.setVisibility(View.GONE);
        mTripCategory2.setVisibility(View.GONE);
        mTripCategory3.setVisibility(View.GONE);

        Intent order_detail_intent = this.getIntent();
        Bundle bundle = order_detail_intent.getExtras();
        mOrder = (Order)bundle.getSerializable(GoGouIntents.ORDER);
        isToConfirmOrder = bundle.getBoolean(GoGouIntents.CONFIRM_ORDER);
        if (isToConfirmOrder) {
            mNegotiatedPriceLayout.setVisibility(View.VISIBLE);
            mSellerConfirmBtn.setVisibility(View.VISIBLE);
        }

        performRequest();

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSellerConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    mOrder.setOrderStatus(OrderStatus.CONFIRMED);
                    performUpdateRequest(mOrder);
                }
            }
        });
    }

    private void performRequest() {
        if (mIsLoading) return;

        mIsLoading = true;
        mProgressSpinner.show();

        RESTRequestUtils.performGetOrderRequest(mOrder.getId(), new RESTRequestListener<Order>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(OrderDetailActivity.this, R.string.notif_get_order_list_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
                mIsLoading = false;
            }

            @Override
            public void onGogouRESTRequestSuccess(Order order) {

                mOrder = order;
                Address address = order.getAddress();
                List<OrderDescription> orderDescriptions = order.getOrderDescriptions();
                OrderDescription orderDescription = orderDescriptions.get(0);

                mOrderNumber.setText(String.valueOf(mOrder.getId()));
                mProductName.setText(orderDescription.getProductName());
                mBrand.setText(orderDescription.getBrand());
                mDescription.setText(orderDescription.getDescription());
                mQuantity.setText(String.valueOf(orderDescription.getQuantity()));
                mMinPrice.setText(String.valueOf(orderDescription.getMinPrice()));
                mMaxPrice.setText(String.valueOf(orderDescription.getMaxPrice()));
                mOrderPrice.setText(String.valueOf(order.getOrderTotal()));
                mCollectInfo.setText(address.getLastName() + address.getFirstName());
                mPhoneText.setText(address.getTelephone());
                mAddressText.setText(address.getStreetAddress());

                Category category = CacheManager.findCategoryByName(orderDescription.getCategoryName());
                mTripCategory1.setText(category.getDisplayName());
                mTripCategory1.setVisibility(View.VISIBLE);

                if (order.getOrderStatus() == OrderStatus.PREORDERED) {
                    mPriceRangeLayout.setVisibility(View.VISIBLE);
                    mfinalPriceLayout.setVisibility(View.GONE);
                } else {
                    mPriceRangeLayout.setVisibility(View.GONE);
                    mfinalPriceLayout.setVisibility(View.VISIBLE);
                }

                RESTRequestUtils.performGetTripRequest(order.getTripId(), new RESTRequestListener<Trip>() {
                    @Override
                    public void onGogouRESTRequestFailure() {
                        Toast.makeText(OrderDetailActivity.this, R.string.notif_get_order_list_failed, Toast.LENGTH_LONG).show();
                        mProgressSpinner.dismiss();
                        mIsLoading = false;
                    }

                    @Override
                    public void onGogouRESTRequestSuccess(Trip trip) {
                        mProgressSpinner.dismiss();
                        mTripFrom.setText(trip.getOrigin());
                        mTripTo.setText(trip.getDestination());
                        mDepartDate.setText(trip.getDeparture());
                        mArrivalDate.setText(trip.getArrival());
                        mIsLoading = false;
                    }
                });
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
                Toast.makeText(OrderDetailActivity.this, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
                mIsLoading = false;
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse response) {
                if (response.getErrorType() != ErrorType.NONE) {
                    Toast.makeText(OrderDetailActivity.this, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OrderDetailActivity.this, R.string.notif_update_order_succeed, Toast.LENGTH_LONG).show();
                }

                mIsLoading = false;
                mProgressSpinner.dismiss();
            }
        });
    }

    protected boolean checkFields() {

        String finalPrice = mFinalPrice.getText().toString().trim();

        if (finalPrice.equals("")) {
            Toast.makeText(OrderDetailActivity.this, R.string.notif_final_price_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        OrderDescription orderDetail = mOrder.getOrderDescriptions().get(0);
        if (finalPrice != null && !finalPrice.isEmpty())
            orderDetail.setFinalPrice(new BigDecimal(Double.valueOf(finalPrice)));

        return true;
    }

}
