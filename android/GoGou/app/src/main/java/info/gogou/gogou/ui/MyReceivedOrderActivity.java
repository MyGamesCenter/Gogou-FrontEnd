package info.gogou.gogou.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.MyFragmentPageAadpter;
import info.gogou.gogou.model.OrderStatus;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by grace on 15-11-10.
 */
public class MyReceivedOrderActivity extends RoboActionBarActivity {

    private static final String TAG = "MyReceivedOrderActivity";

    @InjectView(R.id.myReceivedOrderTabs)
    private TabLayout mMyOrderTabs;
    @InjectView(R.id.myReceivedOrderVPager)
    private ViewPager mMyOrderPager;
    @InjectView(R.id.myReceivedOrderBackButton)
    private Button mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_received_order_layout);

        initViewPager();
        mMyOrderTabs.setupWithViewPager(mMyOrderPager);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager()
    {
        ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
        ReceivedOrderListFragment allOrderListFg = new ReceivedOrderListFragment();
        ReceivedOrderListFragment orderToConfirmListFg = ReceivedOrderListFragment.newInstance(OrderStatus.PREORDERED);
        ReceivedOrderListFragment orderToDeliverListFg = ReceivedOrderListFragment.newInstance(OrderStatus.ORDERED);
        ReceivedOrderListFragment orderToCommentListFg = ReceivedOrderListFragment.newInstance(OrderStatus.DELIVERED);

        fragmentsList.add(allOrderListFg);
        fragmentsList.add(orderToConfirmListFg);
        fragmentsList.add(orderToDeliverListFg);
        fragmentsList.add(orderToCommentListFg);

        String titles [] = {
                getResources().getString(R.string.all_orders),
                getResources().getString(R.string.order_to_confirm),
                getResources().getString(R.string.order_to_deliver),
                getResources().getString(R.string.order_to_comment)
        };

        MyFragmentPageAadpter adapter = new MyFragmentPageAadpter(getSupportFragmentManager(), fragmentsList, titles);
        mMyOrderPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
