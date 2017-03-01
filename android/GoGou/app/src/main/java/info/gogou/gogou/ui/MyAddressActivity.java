package info.gogou.gogou.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import cn.trinea.android.common.view.DropDownListView;
import info.gogou.gogou.R;
import info.gogou.gogou.adapter.AddressListAdapter;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.AddressList;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by grace on 16-1-15.
 */
public class MyAddressActivity extends RoboActionBarActivity {

    private static final String TAG = "MyAddressActivity";

    @InjectView(R.id.myAddressBackButton)
    private Button mBackBtn;
    @InjectView(R.id.addressListView)
    private DropDownListView mAddressListView;
    @InjectView(R.id.createAddressBtn)
    private Button mCreateAddressBtn;

    private ProgressDialog mProgressSpinner;

    private AddressList mAddressList = new AddressList();
    private AddressListAdapter mAddressListAdapter;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_list_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCreateAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAddressActivity.this, CreateAddressActivity.class);
                startActivity(intent);
            }
        });

        initListView();
    }

    private void initListView() {

        mAddressListView.setHeaderDefaultText("");
        mAddressListView.setFooterDefaultText("");

        mAddressListAdapter = new AddressListAdapter(this, mAddressList);
        mAddressListView.setAdapter(mAddressListAdapter);
        mAddressListView.setOnScrollListener(new ManualScrollListener());
    }

    private void performRequest() {
        cleanCachedList();
        mProgressSpinner.show();

        mIsLoading = true;
        RESTRequestUtils.performAddressListRequest(new RESTRequestListener<AddressList>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(MyAddressActivity.this, R.string.notif_get_address_list_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(AddressList addressList) {

                if (addressList.isEmpty()) {
                    Toast.makeText(MyAddressActivity.this, R.string.notif_get_address_list_failed, Toast.LENGTH_LONG).show();
                } else  {
                    mAddressList.addAll(addressList);
                    mAddressListAdapter.notifyDataSetChanged();
                }
                mIsLoading = false;
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
        mAddressList.clear();
        mAddressListAdapter.notifyDataSetChanged();
    }

    private class ManualScrollListener implements AbsListView.OnScrollListener {

        private boolean performLoading = false;

        public ManualScrollListener() {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            Log.d(TAG, "First visible item index: " + firstVisibleItem);
            Log.d(TAG, "Number of visible cells: " + visibleItemCount);
            Log.d(TAG, "Number of items in the list adaptor: " + totalItemCount);

            // when the view reaches to the last item, a request is performed
            /*if ((firstVisibleItem + visibleItemCount) >= totalItemCount)
            {
                performLoading = true;
            }*/
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            Log.d(TAG, "Scroll state is changed to : " + scrollState);
            /*if (scrollState ==  SCROLL_STATE_TOUCH_SCROLL && !mIsLoading && performLoading)
            {
                Log.d(TAG, "Perform a read loading request...");
                mOrderFilter.setCurrentPage(mCurrentPage);
                mOrderFilter.setPageSize(mPageSize);
                mOrderFilter.setUsername(mUserName);
                performRequest();
                mIsLoading = true;
                performLoading = false;
            }*/
        }
    }
}
