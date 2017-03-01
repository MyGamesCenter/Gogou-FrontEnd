package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.trinea.android.common.view.DropDownListView;
import info.gogou.gogou.R;
import info.gogou.gogou.adapter.PaymentListAdapter;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.PaymentMethodList;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;

public class MyPaymentActivity extends RoboActionBarActivity {

    private static final String TAG = "MyPaymentActivity";

    //@InjectView(R.id.paymentInfoBackBtn)
    private Button mBackBtn;
    //@InjectView(R.id.paymentListView)
    private DropDownListView mPaymentListView;
    //@InjectView(R.id.addPaymentBtn)
    private Button mAddPaymentBtn;

    private ProgressDialog mProgressSpinner;

    private PaymentMethodList mPaymentList = new PaymentMethodList();
    private PaymentListAdapter mPaymentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_payment_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mBackBtn = (Button)findViewById(R.id.paymentInfoBackBtn);
        mPaymentListView = (DropDownListView)findViewById(R.id.paymentListView);
        mAddPaymentBtn = (Button)findViewById(R.id.addPaymentBtn);

        mBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAddPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "add payment button is pressed.");
                //Intent intent = new Intent(MyPaymentActivity.this, AddPaymentActivity.class);
                //startActivity(intent);
            }
        });

        initListView();
    }

    private void initListView() {

        mPaymentListView.setHeaderDefaultText("");
        mPaymentListView.setFooterDefaultText("");

        mPaymentListAdapter = new PaymentListAdapter(this, mPaymentList);
        mPaymentListView.setAdapter(mPaymentListAdapter);
    }

    private void performRequest() {
        cleanCachedList();
        mProgressSpinner.show();

        RESTRequestUtils.performPaymentMethodListRequest(new RESTRequestListener<PaymentMethodList>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(MyPaymentActivity.this, R.string.notif_get_payment_list_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(PaymentMethodList paymentMethodListList) {

                if (paymentMethodListList.isEmpty()) {
                    Toast.makeText(MyPaymentActivity.this, R.string.notif_get_payment_list_failed, Toast.LENGTH_LONG).show();
                } else  {
                    mPaymentList.addAll(paymentMethodListList);
                    mPaymentListAdapter.notifyDataSetChanged();
                }

                mProgressSpinner.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        performRequest();
    }

    private void cleanCachedList() {
        mPaymentList.clear();
        mPaymentListAdapter.notifyDataSetChanged();
    }

}
