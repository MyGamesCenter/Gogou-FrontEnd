package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Address;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class CreateAddressActivity extends RoboActionBarActivity {

    private static final String TAG = "CreateAddressActivity";

    // Views
    @InjectView(R.id.createAddressBackBtn)
    private Button mBackBtn;
    @InjectView(R.id.confirmAddressCreation)
    private Button mConfirmBtn;
    @InjectView(R.id.addressReceiverSurname)
    private TextView mReceiverSurname;
    @InjectView(R.id.addressReceiverName)
    private TextView mReceiverName;
    @InjectView(R.id.addressPhoneNumber)
    private TextView mPhoneNumber;
    @InjectView(R.id.addressDetail)
    private TextView mAddressDetail;

    private ProgressDialog mProgressSpinner;

    private Address mAddress = new Address();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_address_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(matchAddressMsg()){

                    performRequest(mAddress);
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void performRequest(Address address) {
        mProgressSpinner.show();

        RESTRequestUtils.performAddressCreateRequest(address, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(CreateAddressActivity.this, R.string.notif_request_failed_common, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(CreateAddressActivity.this, R.string.notif_create_address_succeed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();

                finish();
            }
        });
    }

    protected boolean matchAddressMsg() {

        String receiverSurname = mReceiverSurname.getText().toString().trim();
        String receiver = mReceiverName.getText().toString().trim();
        String phoneNumber = mPhoneNumber.getText().toString().trim();
        String addressDetail = mAddressDetail.getText().toString().trim();


        if (receiver.equals("")) {
            Toast.makeText(CreateAddressActivity.this, R.string.notif_address_receiver_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNumber.equals("")) {
            Toast.makeText(CreateAddressActivity.this, R.string.notif_address_phonenumber_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (addressDetail.equals("")) {
            Toast.makeText(CreateAddressActivity.this, R.string.notif_address_detail_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        mAddress.setUserName(GogouApplication.getInstance().getUserName());
        mAddress.setFirstName(receiver);
        mAddress.setLastName(receiverSurname);
        mAddress.setTelephone(phoneNumber);
        mAddress.setStreetAddress(addressDetail);

        return true;
    }

}