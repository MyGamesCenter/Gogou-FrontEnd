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
public class MyOrderActivity extends RoboActionBarActivity {

    private static final String TAG = "MyOrderActivity";

    // Views
    @InjectView(R.id.myOrderTabs)
    private TabLayout mMyOrderTabs;
    @InjectView(R.id.myOrderVPager)
    private ViewPager mMyOrderPager;
    @InjectView(R.id.myOrderBackButton)
    private Button mBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_layout);

        initViewPager();

        String titles [] = {
                getResources().getString(R.string.all_orders),
                getResources().getString(R.string.order_to_pay),
                getResources().getString(R.string.order_to_collect),
                getResources().getString(R.string.order_to_comment)
        };

        for (int i = 0; i < titles.length; i++) {
            mMyOrderTabs.addTab(mMyOrderTabs.newTab().setText(titles[i]));
        }

        mMyOrderTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMyOrderPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


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
        OrderListFragment allOrderListFg = new OrderListFragment();
        OrderListFragment orderToPayListFg = OrderListFragment.newInstance(OrderStatus.CONFIRMED);
        OrderListFragment orderToCollectListFg = OrderListFragment.newInstance(OrderStatus.DELIVERED);
        OrderListFragment orderToCommentListFg = OrderListFragment.newInstance(OrderStatus.COLLECTED);

        fragmentsList.add(allOrderListFg);
        fragmentsList.add(orderToPayListFg);
        fragmentsList.add(orderToCollectListFg);
        fragmentsList.add(orderToCommentListFg);


        MyFragmentPageAadpter adapter = new MyFragmentPageAadpter(getSupportFragmentManager(), fragmentsList);
        mMyOrderPager.setAdapter(adapter);
        mMyOrderPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMyOrderTabs));
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
