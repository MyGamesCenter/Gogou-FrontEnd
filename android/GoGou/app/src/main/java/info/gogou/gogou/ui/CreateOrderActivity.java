package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.CategorySpinnerAdapter;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Address;
import info.gogou.gogou.model.AddressList;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderDescription;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.OrderUtils;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class CreateOrderActivity extends RoboActionBarActivity {

    private static final String TAG = "CreateOrderActivity";

    // Views
    @InjectView(R.id.create_order_back_Button)
    private Button create_order_back_btn;
    @InjectView(R.id.create_order_submit_Button)
    private Button create_order_submit_btn;
    @InjectView(R.id.addressReceiverSurname)
    private TextView mSurnameText;
    @InjectView(R.id.addressReceiverName)
    private TextView mNameText;
    @InjectView(R.id.addressPhoneNumber)
    private TextView mPhoneNumberText;
    @InjectView(R.id.addressDetail)
    private TextView mAddressDetailText;
    @InjectView(R.id.departureTV)
    private TextView trip_departure_edit;
    @InjectView(R.id.departDateTV)
    private TextView trip_departure_time_edit;
    @InjectView(R.id.destinationTV)
    private TextView trip_destination_edit;
    @InjectView(R.id.arrivalDateTV)
    private TextView trip_arrival_time_edit;
    @InjectView(R.id.trip_category1_edit)
    private TextView trip_category1_edit;
    @InjectView(R.id.trip_category2_edit)
    private TextView trip_category2_edit;
    @InjectView(R.id.trip_category3_edit)
    private TextView trip_category3_edit;
    @InjectView(R.id.create_order_productName_edit)
    private EditText create_order_productName_edit;
    @InjectView(R.id.create_order_category_spinner)
    private Spinner create_order_category_spinner;
    @InjectView(R.id.create_order_quantity_delete_Button)
    private Button create_order_quantity_delete_btn;
    @InjectView(R.id.create_order_quantity_add_Button)
    private Button create_order_quantity_add_btn;
    @InjectView(R.id.create_order_quantity_text)
    private TextView create_order_quantity;
    @InjectView(R.id.create_order_details_edit)
    private EditText create_order_details_edit;
    @InjectView(R.id.create_order_low_price_edit)
    private EditText create_order_low_price_edit;
    @InjectView(R.id.create_order_high_price_edit)
    private EditText create_order_high_price_edit;

    private ProgressDialog mProgressSpinner;

    private Order mOrder;

    private String mProductName;
    private String mCategory;
    private String mQuantity;
    private String mBrand;
    private String mDescription;
    private String mMinPrice;
    private String mMaxPrice;
    private int mQuantityNum = 1;
    private Trip mTrip;
    private Address mAddress;

    private String mUserName = null;
    private String mEncodedPassword = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_order_layout);

        // read preferences to see if login/registration is already done
        mUserName = GogouApplication.getInstance().getUserName();
        mEncodedPassword = GogouApplication.getInstance().getPassword();
        mNameText.setText(mUserName);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mTrip = (Trip)bundle.getSerializable(GoGouIntents.TRIP);

        trip_departure_edit.setText(mTrip.getOrigin());
        trip_destination_edit.setText(mTrip.getDestination());

        trip_departure_time_edit.setText(mTrip.getDeparture());
        trip_arrival_time_edit.setText(mTrip.getArrival());

        // TODO (lxu) auto-detect phones' dimens and display
        switch (mTrip.getCategoryNames().size())
        {
            case 0:
                trip_category1_edit.setVisibility(View.GONE);
                trip_category2_edit.setVisibility(View.GONE);
                trip_category3_edit.setVisibility(View.GONE);
                break;
            case 1:
                trip_category1_edit.setText(mTrip.getCategoryNames().get(0));
                trip_category2_edit.setVisibility(View.GONE);
                trip_category3_edit.setVisibility(View.GONE);
                break;
            case 2:
                trip_category1_edit.setText(mTrip.getCategoryNames().get(0));
                trip_category2_edit.setText(mTrip.getCategoryNames().get(1));
                trip_category3_edit.setVisibility(View.GONE);
                break;
            default:
                trip_category1_edit.setText(mTrip.getCategoryNames().get(0));
                trip_category2_edit.setText(mTrip.getCategoryNames().get(1));
                trip_category3_edit.setVisibility(View.GONE);
                break;
        }

        create_order_quantity_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuantityNum > 0) mQuantityNum--;
                create_order_quantity.setText(String.valueOf(mQuantityNum));
            }
        });

        create_order_quantity_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantityNum++;
                create_order_quantity.setText(String.valueOf(mQuantityNum));
            }
        });


        create_order_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(matchOrderMsg()){

                    mOrder = new Order();
                    mOrder.setId(Long.valueOf(OrderUtils.getOrderNumber()));
                    mOrder.setUsername(mUserName);
                    mOrder.setTripId(mTrip.getId());
                    mOrder.setOrderStatus(OrderStatus.PREORDERED);
                    mOrder.setServiceFee(new BigDecimal(0.0));
                    mOrder.setSellerId(mTrip.getUserName());
                    mOrder.setAddress(mAddress);

                    List<OrderDescription> orderDescriptions = new ArrayList<OrderDescription>();

                    OrderDescription orderDescription = new OrderDescription();
                    orderDescription.setProductName(mProductName);
                    orderDescription.setName(mUserName);
                    orderDescription.setBrand(mBrand);
                    if (mMinPrice != null && !mMinPrice.isEmpty())
                        orderDescription.setMinPrice(new BigDecimal(Double.valueOf(mMinPrice)));
                    if (mMaxPrice != null && !mMaxPrice.isEmpty())
                        orderDescription.setMaxPrice(new BigDecimal(Double.valueOf(mMaxPrice)));
                    orderDescription.setQuantity(Integer.parseInt(mQuantity));
                    orderDescription.setCategoryName(mCategory);
                    orderDescription.setLanguageCode(getResources().getString(R.string.language_code));
                    orderDescription.setDescription(create_order_details_edit.getText().toString().trim());
                    orderDescription.setOrigin(mTrip.getOrigin());

                    orderDescriptions.add(orderDescription);

                    mOrder.setOrderDescriptions(orderDescriptions);

                    performRequest(mOrder);
                }
            }
        });


        create_order_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initCategorySpinner();
        perfromGetDefaultAddress();
    }

    private void initCategorySpinner()
    {
        CategoryList categories = CacheManager.getCachedCategories(this);

        create_order_category_spinner.setAdapter(new CategorySpinnerAdapter(this, R.layout.gogou_spinner_row, categories));

        create_order_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    mCategory = category.getName();
                    Log.d(TAG, "Selected category name is: " + mCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void perfromGetDefaultAddress() {
        mProgressSpinner.show();
        RESTRequestUtils.performAddressListRequest(new RESTRequestListener<AddressList>() {
            @Override
            public void onGogouRESTRequestFailure() {
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(AddressList addresses) {
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    mSurnameText.setText(address.getLastName());
                    mNameText.setText(address.getFirstName());
                    mPhoneNumberText.setText(address.getTelephone());
                    mAddressDetailText.setText(address.getStreetAddress());
                }
                mProgressSpinner.dismiss();
            }
        });
    }

    private void performRequest(Order order) {
        mProgressSpinner.show();

        RESTRequestUtils.performOrderCreateRequest(order, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(CreateOrderActivity.this, R.string.notif_create_order_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(CreateOrderActivity.this, R.string.notif_create_order_succeed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();

                goBack2MainScreen(mOrder);
            }
        });
    }

    protected boolean matchOrderMsg() {

        mDescription = trip_destination_edit.getText().toString().trim();
        mQuantity = create_order_quantity.getText().toString().trim();
        mProductName = create_order_productName_edit.getText().toString().trim();
        mMinPrice  = create_order_low_price_edit.getText().toString().trim();
        mMaxPrice  = create_order_high_price_edit.getText().toString().trim();

        String surname = mSurnameText.getText().toString().trim();
        String name = mNameText.getText().toString().trim();
        String phoneNumber = mPhoneNumberText.getText().toString().trim();
        String addressDetail = mAddressDetailText.getText().toString().trim();

        if (TextUtils.isEmpty(mProductName.trim())) {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_productname_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mQuantity.trim())) {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_quantity_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mMinPrice.trim())) {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_min_price_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mMaxPrice.trim())) {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_max_price_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.valueOf(mMinPrice) > Double.valueOf(mMaxPrice)) {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_minmax_price_notgood, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(surname.trim()) || TextUtils.isEmpty(name.trim()) ||
                TextUtils.isEmpty(phoneNumber.trim()) || TextUtils.isEmpty(addressDetail.trim()))
        {
            Toast.makeText(CreateOrderActivity.this, R.string.notif_get_address_list_failed, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            mAddress = new Address();
            mAddress.setLastName(surname);
            mAddress.setFirstName(name);
            mAddress.setTelephone(phoneNumber);
            mAddress.setStreetAddress(addressDetail);
        }

        return true;
    }

    private void goBack2MainScreen(Order order)
    {
        Intent intent = new Intent(CreateOrderActivity.this, MyOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        if (null != order)
            intent.putExtra(GoGouIntents.ORDER, order);
        startActivity(intent);
        finish();
    }

}