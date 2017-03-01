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
import info.gogou.gogou.model.PaymentMethod;
import info.gogou.gogou.model.PaymentType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class AddPaymentActivity extends RoboActionBarActivity {

    private static final String TAG = "AddPaymentActivity";

    // Views
    @InjectView(R.id.addPaymentBackButton)
    private Button mBackBtn;
    @InjectView(R.id.confirmAddPaymentButton)
    private Button mConfirmBtn;
    @InjectView(R.id.paymentAccount)
    private TextView mPaymentAccount;
    @InjectView(R.id.paymentLoginPassword)
    private TextView mPaymentLoginPwd;

    private ProgressDialog mProgressSpinner;

    private PaymentMethod mPaymentMethod = new PaymentMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(matchAddressMsg()){

                    performRequest(mPaymentMethod);
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


    private void performRequest(PaymentMethod paymentMethod) {
        mProgressSpinner.show();

        RESTRequestUtils.performPaymentMethodCreateRequest(paymentMethod, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(AddPaymentActivity.this, R.string.notif_request_failed_common, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(AddPaymentActivity.this, R.string.notif_add_payment_method_succeed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();

                finish();
            }
        });
    }

    protected boolean matchAddressMsg() {

        String paymentAccount = mPaymentAccount.getText().toString().trim();
        String paymentPassword = mPaymentLoginPwd.getText().toString().trim();


        if (paymentAccount.equals("")) {
            Toast.makeText(AddPaymentActivity.this, R.string.notif_payment_account_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (paymentPassword.equals("")) {
            Toast.makeText(AddPaymentActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        mPaymentMethod.setUserName(GogouApplication.getInstance().getUserName());
        mPaymentMethod.setPaymentType(PaymentType.ALIPAY);
        mPaymentMethod.setPaymentId(paymentAccount);
        mPaymentMethod.setPaymentToken(paymentPassword);

        return true;
    }

}