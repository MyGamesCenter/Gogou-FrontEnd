package info.gogou.gogou.ui;


import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.MyFragmentPageAadpter;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;


public class MyReleaseActivity extends RoboActionBarActivity {

    private static final String TAG = "MyReleaseActivity";

    @InjectView(R.id.myrelease_back_Button)
    private Button mBackBtn;
    @InjectView(R.id.myrelease_tabs)
    private TabLayout mTabLayout;
    @InjectView(R.id.myrelease_vPager)
    private ViewPager mPager;

    // Resources
    @InjectResource(R.drawable.shopping)
    private Drawable mShopping;
    @InjectResource(R.drawable.plane)
    private Drawable mPlane;

    private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();

    private boolean isLogin = false;
    private String mUserName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_release_layout);

        // read preferences to see if login/registration is already done
        SharedPreferences settings = getSharedPreferences(GoGouConstants.PREFS_NAME, MODE_PRIVATE);
        mUserName = settings.getString(GoGouConstants.KEY_USER_NAME, null);
        if (mUserName != null) {
            isLogin = true;
            Log.d(TAG, "Previous login exists user name: " + mUserName);
        }


        initViewPager();
        //mTabLayout.getTabAt(0).setIcon(mShopping);
       // mTabLayout.getTabAt(1).setIcon(mPlane);
        mTabLayout.setupWithViewPager(mPager);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, MyReleaseActivity.this, false);
            }
        });
    }

    private void initViewPager()
    {
        Bundle bundle = new Bundle();
        bundle.putString(GoGouConstants.KEY_USER_NAME, mUserName);
        MyReleaseDemandFg fg1 = new MyReleaseDemandFg();
        fg1.setArguments(bundle);
        MyReleaseTripFg fg2 = new MyReleaseTripFg();
        fg2.setArguments(bundle);
        mFragmentsList.add(fg1);
        mFragmentsList.add(fg2);
        String fragments [] = {"我的需求","我的行程"};
        MyFragmentPageAadpter adapter = new MyFragmentPageAadpter(getSupportFragmentManager(), mFragmentsList, fragments);
        mPager.setCurrentItem(1);
        mPager.setAdapter(adapter);
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
