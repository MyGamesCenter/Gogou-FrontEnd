package info.gogou.gogou.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.MyFragmentPageAadpter;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.RongYunUtils;
import info.gogou.gogou.utils.photo.PhotoUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity {

    private static final String TAG = "MainActivity";

    // Views
    @InjectView(R.id.mainActivityToolbar)
    private Toolbar mToolbar;
    @InjectView(R.id.demandBtn)
    private ImageView mDemandBtn;
    @InjectView(R.id.scheduleBtn)
    private ImageView mScheduleBtn;
	@InjectView(R.id.vPager)
    private ViewPager mPager;
    @InjectView(R.id.mainDrawerLayout)
    private DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigationView)
    private NavigationView mNavView;
    @InjectView(R.id.releaseBtn)
    private Button mReleaseBtn;

    // Resources
    @InjectResource(R.drawable.user)
    private Drawable mUsrImage;
    @InjectResource(R.drawable.user_gray)
    private Drawable mUsrImageGray;
    @InjectResource(R.drawable.plane)
    private Drawable mPlaneDrawable;
    @InjectResource(R.drawable.plane_gray)
    private Drawable mPlaneGrayDrawable;
    @InjectResource(R.drawable.shopping)
    private Drawable mShoppingDrawable;
    @InjectResource(R.drawable.shopping_gray)
    private Drawable mShoppingGrayDrawable;

    private FragmentManager mFragmentManager;
    private boolean isLogin = false;
    private String mUserName = null;
    private TextView mNavHeaderText;
    private ImageView mNavHeaderImage;
    private ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private PopupWindow mPublishMenuWindow;
    private Subscriber mSubscriber;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUserInfo();
        if (mUserName != null) {
            isLogin = true;
            Log.d(TAG, "Previous login exists user name: " + mUserName);
        }

        ActionOnClick actionClick = new ActionOnClick();
        mDemandBtn.setOnClickListener(actionClick);
        mScheduleBtn.setOnClickListener(actionClick);

        mFragmentManager = getSupportFragmentManager();

        mReleaseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin) {
                    go2LoginScreen();
                } else {
                    //实例化SelectPicPopupWindow
                    int[] menuIds = {R.id.releaseDemandButton, R.id.releaseItineraryButton, R.id.releaseCancelButton};
                    mPublishMenuWindow = new BottomPopupWindow(MainActivity.this,
                                                            R.layout.release_choice_layout,
                                                            R.id.releasePopupMenu,
                                                            menuIds,
                            popupMenuItemOnClick);
                    mPublishMenuWindow.setAnimationStyle(R.style.releasepopwindow_anim_style);
                    mPublishMenuWindow.showAtLocation(MainActivity.this.findViewById(R.id.releaseBtn), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        initToolbar();
        initViewPager();
        setupNavigationView(mNavView);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (GogouApplication.getInstance().isTokenSent()) {
                    Toast.makeText(MainActivity.this, R.string.gcm_send_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.token_error_message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        //registerReceiver(broadcastReceiver, new IntentFilter(GoGouConstants.MSG_ACTION_MESSAGE));


    }

    private void initToolbar()
    {
        if (isLogin)
            mToolbar.setNavigationIcon(mUsrImage);
        else
            mToolbar.setNavigationIcon(mUsrImageGray);
        //mToolbar.inflateMenu(R.menu.main_actionbar_actions);
        /* First time to enter user login screen */
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLogin) {
                    go2LoginScreen();
                }
            }
        });
    }

	private void initViewPager()
    {
        DemandFragment fg1 = new DemandFragment();
        TripFragment fg2 = new TripFragment();
        mFragmentsList.add(fg1);
        mFragmentsList.add(fg2);
        MyFragmentPageAadpter adapter = new MyFragmentPageAadpter(mFragmentManager, mFragmentsList);
        mPager.setCurrentItem(1);
        mPager.setAdapter(adapter);
        MyPageChangeListener myPageChange = new MyPageChangeListener();
        mPager.addOnPageChangeListener(myPageChange);
	}

    private class ActionOnClick implements OnClickListener
	{
		@Override
		public void onClick(View view) {
            toolBarActionChange(view.getId());
		}
	}

	private class MyPageChangeListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			if(arg0 == 2)
			{
				int i = mPager.getCurrentItem();
                toolBarActionChange(i);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		@Override
		public void onPageSelected(int index){}

	}

	private void toolBarActionChange(int num)
	{
		switch (num) {
            case R.id.demandBtn:case 0:
                mPager.setCurrentItem(0);
                mDemandBtn.setImageDrawable(mShoppingDrawable);
                mScheduleBtn.setImageDrawable(mPlaneGrayDrawable);
                break;
            case R.id.scheduleBtn:case 1:
                mPager.setCurrentItem(1);
                mDemandBtn.setImageDrawable(mShoppingGrayDrawable);
                mScheduleBtn.setImageDrawable(mPlaneDrawable);
                break;
		}
	}

    /*private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle data = intent.getBundleExtra(GoGouIntents.CHAT_MESSAGE);

            // add as friend
            FriendDao friendDao = new FriendDao(MainActivity.this);
            friendDao.open();
            // TODO (lxu) retrieve from back-end
            Subscriber subscriber = new Subscriber();
            subscriber.setEmailAddress("fake@fake.com");
            subscriber.setUserName(data.getString(GoGouConstants.MSG_KEY_FROM_SUBSCRIBER));
            DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format2));
            String date = df.format(new Date());
            if (!friendDao.isFriend(subscriber))
                friendDao.addFriend(subscriber, date);
            friendDao.close();

            ChatDao chatDao = new ChatDao(MainActivity.this);
            chatDao.open();
            Chat chatEntry = new Chat();
            chatEntry.setFrom(data.getString(GoGouConstants.MSG_KEY_FROM_SUBSCRIBER));
            chatEntry.setTo(data.getString(GoGouConstants.MSG_KEY_TO_SUBSCRIBER));
            chatEntry.setLayoutId(R.layout.list_say_he_item);
            chatEntry.setPostDate(date);
            chatEntry.setContent(data.getString(GoGouConstants.MSG_KEY_CONTENT));
            chatDao.postChat(chatEntry);
            chatDao.close();
            Log.d(TAG, "onReceive: " + data.getString(GoGouConstants.MSG_KEY_CONTENT));
            Intent chatIntent = new Intent(GoGouConstants.MSG_ACTION_MESSAGE_ADD);
            chatIntent.putExtra(GoGouIntents.CHAT_MESSAGE, data);
            context.sendBroadcast(chatIntent);
        }
    };*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Log.d(TAG, "MainActivity is brought to top");

        String uniqueId = intent.getStringExtra(GoGouIntents.UNIQUE_ID);
        if ("UsrLoginActivity".equals(uniqueId) || "UsrRegisterActivity".equals(uniqueId))
        {
            isLogin = intent.getBooleanExtra(GoGouIntents.LOGIN_SUCCEED_FLAG, false);
            if (isLogin)
            {
                updateUserInfo();
                mReleaseBtn.setEnabled(true);
                String userName = intent.getStringExtra(GoGouIntents.LOGIN_USER_NAME);
                Log.d(TAG, "Login user name is: " + userName);
                mToolbar.setNavigationIcon(mUsrImage);
                mNavHeaderImage.setImageDrawable(mUsrImage);
                mNavHeaderText.setText(userName);
                mUserName = userName;
            }
        }
        else if ("RequirementEditActivity".equals(uniqueId))
        {
            mPager.setCurrentItem(0);
        }
        else if ("ScheduleEditActivity".equals(uniqueId))
        {
            mPager.setCurrentItem(1);
        }
        else if ("MySettingActivity".equals(uniqueId))
        {
            isLogin = intent.getBooleanExtra(GoGouIntents.LOGIN_SUCCEED_FLAG, false);
            if (!isLogin)
            {
                Log.d(TAG, "User is logout!");
                mToolbar.setNavigationIcon(mUsrImageGray);
                mNavHeaderImage.setImageDrawable(mUsrImageGray);
                mNavHeaderText.setText(R.string.login_now);
            }
        }
    }

    //release_btn的点击事件,发布行程,发布需求
    private OnClickListener popupMenuItemOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.releaseCancelButton:
                    mPublishMenuWindow.dismiss();
                    break;
                case R.id.releaseDemandButton:
                    startActivity(new Intent(MainActivity.this,RequirementEditActivity.class));
                    mPublishMenuWindow.dismiss();
                    break;
                case R.id.releaseItineraryButton:
                    startActivity(new Intent(MainActivity.this,ScheduleEditActivity.class));
                    mPublishMenuWindow.dismiss();
                    break;
            }
        }
    };

    private void setupNavigationView(NavigationView navigationView) {
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin) {
                    go2LoginScreen();
                }
            }
        });
        mNavView.addHeaderView(header);
        mNavView.setItemIconTintList(null);
        mNavHeaderImage = (ImageView) header.findViewById(R.id.nav_header_image);
        mNavHeaderText = (TextView) header.findViewById(R.id.nav_header_text);
        if (isLogin) {
            mNavHeaderImage.setImageDrawable(mUsrImage);
            mNavHeaderText.setText(mUserName);
        } else {
            mNavHeaderImage.setImageDrawable(mUsrImageGray);
        }

        //设置导航栏点击事件
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int menuItemId = menuItem.getItemId();
                        if (menuItemId == R.id.nav_myRelease || menuItemId == R.id.nav_myOrder ||
                                menuItemId == R.id.nav_myFavorite || menuItemId == R.id.nav_myMessage ||
                                menuItemId == R.id.nav_mySetting || menuItemId == R.id.nav_myReceivedOrder)
                        {
                            if (!isLogin) {
                                go2LoginScreen();
                                return true;
                            }
                        }
                        switch (menuItemId) {
                            case R.id.nav_myRelease:
                                menuItem.setChecked(true);
                                Intent my_release_intent = new Intent(MainActivity.this, MyReleaseActivity.class);
                                my_release_intent.putExtra(GoGouIntents.LOGIN_USER_NAME, mUserName);
                                startActivity(my_release_intent);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_myOrder:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, MyOrderActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_myReceivedOrder:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, MyReceivedOrderActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_myFavorite:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, MyFavoriteActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_myMessage:
                                menuItem.setChecked(true);

                                if (RongIM.getInstance() != null) {
                                    RongIM.getInstance().startConversationList(MainActivity.this);
                                }else{
                                    Toast.makeText(MainActivity.this, R.string.notif_im_disabled, Toast.LENGTH_LONG).show();
                                }
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_help:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_mySetting:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, MySettingActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_about:
                                menuItem.setChecked(true);
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        return true;
                    }
                });
    }


    private void go2LoginScreen()
    {
        Intent intent = new Intent(MainActivity.this, UsrLoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {

        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GoGouConstants.REGISTRATION_COMPLETE));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "I am destroyed!");
        gogouCleanup();
        super.onDestroy();
    }

    private void gogouCleanup() {

//        NIMClient.getService(AuthService.class).logout();
//        NimUIKit.clearCache();

        if (GogouApplication.getInstance().getSpiceManager().isStarted())
            GogouApplication.getInstance().getSpiceManager().shouldStop();
        //unregisterReceiver(broadcastReceiver);
    }

    private void updateUserInfo() {
        mSubscriber = GogouApplication.getInstance().getSubscriber();
        mUserName = mSubscriber.getUserName();
        if (mSubscriber.getHeadImage() != null) {
            mUsrImage = PhotoUtils.getDrawableFromFilePath(mSubscriber.getHeadImage());
        }

        if (mSubscriber.getId() != null && mSubscriber.getUserName() != null)
        {
            RongYunUtils.updateRongYunUserInfo(new UserInfo(Long.toString(mSubscriber.getId()), mSubscriber.getUserName(), null));
        }
    }
}
