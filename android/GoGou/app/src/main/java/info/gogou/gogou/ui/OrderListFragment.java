package info.gogou.gogou.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trinea.android.common.view.DropDownListView;
import info.gogou.gogou.R;
import info.gogou.gogou.adapter.OrderListAdapter;
import info.gogou.gogou.adapter.OrderListAdapter.OrderOperation;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderDescription;
import info.gogou.gogou.model.OrderFilter;
import info.gogou.gogou.model.OrderList;
import info.gogou.gogou.model.OrderStatus;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.fragment.RoboFragment;

/**
 * Created by grace on 16-1-15.
 */
public class OrderListFragment extends RoboFragment {

    private static final String TAG = "OrderListFragment";


    private Activity mActivity;
    private DropDownListView mOrderListView;

    private ArrayList<HashMap<String, String>> mOrderList = new ArrayList<HashMap<String, String>>();

    private OrderList mCachedList =  new OrderList();
    private OrderListAdapter mOrderListAdapter;

    private OrderFilter mOrderFilter = new OrderFilter();

    private int mPageSize = 10; //number of items to load for one REST request
    private int mCurrentPage = 1; // current item index from where REST request loads items
    private int mSizeofCurPage = -1; // number of item in current page
    private boolean mIsLoading = false;

    private String mUserName;
    private OrderStatus mOrderStatus = null;

    private ProgressDialog mProgressSpinner;

    static OrderListFragment newInstance(OrderStatus orderStatus) {
        OrderListFragment f = new OrderListFragment();

        Bundle args = new Bundle();
        args.putSerializable(GoGouConstants.KEY_ORDER_STATUS, orderStatus);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mOrderStatus = (OrderStatus)args.getSerializable(GoGouConstants.KEY_ORDER_STATUS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View orderView = inflater.inflate(R.layout.order_list_layout, container,false);

        mProgressSpinner = new ProgressDialog(mActivity, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mUserName = GogouApplication.getInstance().getUserName();

        mOrderListView = (DropDownListView)orderView.findViewById(R.id.myOrderListView);
        mOrderListView.setHeaderDefaultText("");
        mOrderListView.setFooterDefaultText("");

        mOrderListAdapter = new OrderListAdapter(mActivity, OrderListAdapter.OrderType.CREATED_ORDER, mOrderList);
        mOrderListAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderOperation operation, int position) {
                Log.d(TAG, "Order item: " + position + " is clicked.");
                Order order = mCachedList.get(position);
                Log.d(TAG, "Order id is: " + order.getId());
                if (operation == OrderOperation.GET_DETAIL_ORDER) {
                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                    intent.putExtra(GoGouIntents.ORDER, order);
                    startActivity(intent);
                }
                else if (operation == OrderOperation.PAY_ORDER) {
                    Intent intent = new Intent(mActivity, CheckoutOrderActivity.class);
                    intent.putExtra(GoGouIntents.ORDER, order);
                    startActivity(intent);
                }
                else if (operation == OrderOperation.COLLECT_ORDER) {
                    order.setOrderStatus(OrderStatus.COLLECTED);
                    performUpdateRequest(order);
                }
                else if (operation == OrderOperation.CANCEL_ORDER) {
                    order.setOrderStatus(OrderStatus.PREORDERED);
                    performUpdateRequest(order);
                }
            }
        });

        mOrderListView.setAdapter(mOrderListAdapter);
        mOrderListView.setOnScrollListener(new ManualScrollListener());

        // set drop down listener
        mOrderListView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                Log.d(TAG, "It should be only called when reaching top header.");
                if (!mIsLoading)
                    performRequest(true);
            }
        });

        // set on bottom listener
        mOrderListView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "It should be only called when reaching bottom.");
                if (!mIsLoading)
                    performRequest(false);
            }
        });

        mOrderFilter.setPageSize(mPageSize);
        mOrderFilter.setUsername(mUserName);
        mOrderFilter.setOrderStatus(mOrderStatus);

        return orderView;
    }

    // Add a order item to the adapter
    public void addOrderItemToList(Order order)
    {
        // if number of item in current page is already equal to loading page size,
        // then the item could be retrieved to display from next list request
        if (mSizeofCurPage < mPageSize && mSizeofCurPage > 0) {
            addOrderItemToListInternal(order);
            mCachedList.add(order);
            mSizeofCurPage++;
        }
        mOrderListAdapter.notifyDataSetChanged();
    }

    private void addOrderItemToListInternal(Order order)
    {
        HashMap<String,String> orderItem=new HashMap<String, String>();

        List<OrderDescription> orderDescriptions = order.getOrderDescriptions();
        OrderDescription orderDescription = null;

        Log.d(TAG, "order id: "+order.getId());


        OrderStatus orderStatus = order.getOrderStatus();

        for(int i = 0; i < orderDescriptions.size(); i++){
            orderDescription = orderDescriptions.get(i);
        }

        orderItem.put(GoGouConstants.KEY_ORDER_TRAVELLER, order.getSellerId());
        orderItem.put(GoGouConstants.KEY_ORDER_STATUS, getOrderStatusString(orderStatus));
        orderItem.put(GoGouConstants.KEY_ORDER_STATUS_EN, orderStatus.getValue());
        orderItem.put(GoGouConstants.KEY_ORDER_PRODUCT, orderDescription.getProductName());
        orderItem.put(GoGouConstants.KEY_ORDER_PRODUCT_DETAIL, orderDescription.getDescription());
        orderItem.put(GoGouConstants.KEY_ORDER_QUANTITY, String.valueOf(orderDescription.getQuantity()));
        orderItem.put(GoGouConstants.KEY_ORDER_MIN_PRICE, String.valueOf(orderDescription.getMinPrice()));
        orderItem.put(GoGouConstants.KEY_ORDER_MAX_PRICE, String.valueOf(orderDescription.getMaxPrice()));
        orderItem.put(GoGouConstants.KEY_ORDER_PRICE, String.valueOf(order.getOrderTotal()));
        Category category = info.gogou.gogou.model.CacheManager.findCategoryByName(orderDescription.getCategoryName());
        orderItem.put(GoGouConstants.KEY_IMAGE_NAME, category.getImagePath());

        mOrderList.add(orderItem);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }


    @Override
    public void onResume() {
        Log.d(TAG, "Order fragment is resumed!");
        super.onResume();

        performRequest(true);
    }

    private void performRequest(final boolean isDropDown) {
        if (isDropDown) {
            cleanCachedList();
        }
        mOrderFilter.setCurrentPage(mCurrentPage);

        mIsLoading = true;
        RESTRequestUtils.performOrderListRequest(mOrderFilter, new RESTRequestListener<OrderList>() {
            @Override
            public void onGogouRESTRequestFailure() {
                if (isDropDown) {
                    mOrderListView.onDropDownComplete();
                } else {
                    mOrderListView.onBottomComplete();
                }
                Toast.makeText(mActivity, R.string.notif_get_order_list_failed, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGogouRESTRequestSuccess(OrderList orderList) {

                Log.d(TAG, "There are " + orderList.size() + " orders");
                // only retrieved list size is greater than 0, increase the current page
                if (orderList.size() > 0)
                {
                    mCurrentPage++;
                    mCachedList.addAll(orderList);
                    mSizeofCurPage = orderList.size();
                }
                for (Order order : orderList)
                {
                    addOrderItemToListInternal(order);
                }
                mOrderListAdapter.notifyDataSetChanged();
                mIsLoading = false;
                if (isDropDown) {
                    mOrderListView.onDropDownComplete();
                } else {
                    mOrderListView.onBottomComplete();
                }
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
                Toast.makeText(mActivity, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse response) {
                if (response.getErrorType() != ErrorType.NONE) {
                    Toast.makeText(mActivity, R.string.notif_update_order_failed, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mActivity, R.string.notif_update_order_succeed, Toast.LENGTH_LONG).show();
                }

                mIsLoading = false;
                mProgressSpinner.dismiss();
                mOrderListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void cleanCachedList() {
        mCurrentPage = 1;
        mCachedList.clear();
        mOrderList.clear();
        mOrderListAdapter.notifyDataSetChanged();
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

    private String getOrderStatusString(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.PREORDERED) {
            return getResources().getString(R.string.order_to_confirm);
        }
        else if (orderStatus == OrderStatus.CONFIRMED) {
            return getResources().getString(R.string.order_to_pay);
        }
        else if (orderStatus == OrderStatus.ORDERED) {
            return getResources().getString(R.string.order_to_deliver);
        }
        else if (orderStatus == OrderStatus.DELIVERED) {
            return getResources().getString(R.string.order_to_collect);
        }
        else if (orderStatus == OrderStatus.COLLECTED) {
            return getResources().getString(R.string.order_to_comment);
        }
        else
            return null;
    }

    private OrderStatus getOrderStatusFromString(String orderStatusStr) {
        if (orderStatusStr.equals(OrderStatus.PREORDERED.getValue())) {
            return OrderStatus.PREORDERED;
        }
        else if (orderStatusStr.equals(OrderStatus.ORDERED.getValue())) {
            return OrderStatus.ORDERED;
        }
        else if (orderStatusStr.equals(OrderStatus.DELIVERED.getValue())) {
            return OrderStatus.DELIVERED;
        }
        else if (orderStatusStr.equals(OrderStatus.COLLECTED.getValue())) {
            return OrderStatus.COLLECTED;
        }
        else
            return null;
    }
}
