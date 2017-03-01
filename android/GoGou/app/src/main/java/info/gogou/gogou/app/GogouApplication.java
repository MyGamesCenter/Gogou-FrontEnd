package info.gogou.gogou.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.util.List;

import cn.trinea.android.common.util.FileUtils;
import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.LoginType;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.PreferencesUtil;
import info.gogou.gogou.utils.RongYunUtils;
import info.gogou.gogou.utils.SystemUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lxu on 2016-04-14.
 */

public class GogouApplication extends MultiDexApplication  {

    private static GogouApplication instance;

    private SpiceManager mSpiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);

    private PreferencesUtil mPrefSetting = null;


    private Subscriber mSubscirber = null;
    private String mLoginType = "normal";
    private String mOauth2AccessToken;
    private String mOauth2RefreshToken;
    private boolean mIsLoggedOnNM = false;
    private List<Long> favorite_trip_list = null;
    private List<Long> favorite_demand_list = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // install multidex
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        mSpiceManager.start(this);
        mPrefSetting = PreferencesUtil.getInstance(this);
        mSubscirber = new Subscriber();
        mSubscirber.setId(mPrefSetting.getLongValue(GoGouConstants.KEY_SUBSCRIBER_ID, 0L));
        mSubscirber.setEmailAddress(mPrefSetting.getValue(GoGouConstants.KEY_EMAIL_ADDRESS, null));
        mSubscirber.setUserName(mPrefSetting.getValue(GoGouConstants.KEY_USER_NAME, null));
        mSubscirber.setEncodedPassword(mPrefSetting.getValue(GoGouConstants.KEY_PASSWORD, null));
        if (mPrefSetting.getValue(GoGouConstants.KEY_PURCHASER_FLAG, false)) mSubscirber.setIsPurchaser();
        mSubscirber.setAge(mPrefSetting.getIntValue(GoGouConstants.KEY_USER_AGE, 0));
        mSubscirber.setGender(mPrefSetting.getValue(GoGouConstants.KEY_GENDER, null));
        mSubscirber.setGcmToken(mPrefSetting.getValue(GoGouConstants.KEY_TOKEN, null));
        mSubscirber.setHeadImage(mPrefSetting.getValue(GoGouConstants.KEY_HEADIMAGE_PATH, null));
        mSubscirber.setImToken(mPrefSetting.getValue(GoGouConstants.KEY_IM_TOKEN, null));

        mLoginType = mPrefSetting.getValue(GoGouConstants.KEY_LOGIN_TYPE, null);
        mOauth2AccessToken = mPrefSetting.getValue(GoGouConstants.KEY_OAUTH2_TOKEN, null);
        mOauth2RefreshToken = mPrefSetting.getValue(GoGouConstants.KEY_OAUTH2_REFRESH_TOKEN, null);

        // initialize gogou file system
        setupAppFileSystem();

        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(SystemUtil.getProcessName(getApplicationContext())) ||
                "io.rong.push".equals(SystemUtil.getProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }

        CacheManager.initImageCache();
    }

    public static GogouApplication getInstance() {

        return instance;
    }

    public SpiceManager getSpiceManager() {
        return mSpiceManager;
    }

    public Subscriber getSubscriber() {
        return mSubscirber;
    }

    public String getUserName() { return mSubscirber.getUserName(); }

    public String getPassword() { return mSubscirber.getEncodedPassword(); }

    public String getGcmToken() {
        return mPrefSetting.getValue(GoGouConstants.KEY_TOKEN, null);
    }

    public void setGcmToken(String token) {
        mSubscirber.setGcmToken(token);
        mPrefSetting.setValue(GoGouConstants.KEY_TOKEN, mSubscirber.getGcmToken());
    }

    public String getGenderStr() {
        return mSubscirber.getGender().equals("F") ? getResources().getString(R.string.female) :
                                                     getResources().getString(R.string.male);
    }

    public boolean isTokenSent() {
        return mPrefSetting.getValue(GoGouConstants.SENT_TOKEN_TO_SERVER, false);
    }

    public void setTokenSent(boolean flag) {
        mPrefSetting.setValue(GoGouConstants.SENT_TOKEN_TO_SERVER, flag);
    }

    public boolean isLoggedOnNM() {
        return mIsLoggedOnNM;
    }

    public void setLoggedOnNM(boolean isLogged) {
        mIsLoggedOnNM = isLogged;
    }

    public void updateSubscriber(Subscriber subscriber) {

        mSubscirber = subscriber;
        mPrefSetting.setValue(GoGouConstants.KEY_SUBSCRIBER_ID, subscriber.getId());
        mPrefSetting.setValue(GoGouConstants.KEY_EMAIL_ADDRESS, subscriber.getEmailAddress());
        mPrefSetting.setValue(GoGouConstants.KEY_USER_NAME, subscriber.getUserName());
        mPrefSetting.setValue(GoGouConstants.KEY_PASSWORD, subscriber.getEncodedPassword());
        mPrefSetting.setValue(GoGouConstants.KEY_PURCHASER_FLAG, subscriber.getIsPurchaser());
        mPrefSetting.setValue(GoGouConstants.KEY_USER_AGE, subscriber.getAge());
        mPrefSetting.setValue(GoGouConstants.KEY_GENDER, subscriber.getGender() != null ? subscriber.getGender():"M");
        mPrefSetting.setValue(GoGouConstants.KEY_TOKEN, subscriber.getGcmToken());
        mPrefSetting.setValue(GoGouConstants.KEY_HEADIMAGE_PATH, subscriber.getHeadImage());
        mPrefSetting.setValue(GoGouConstants.KEY_IM_TOKEN, subscriber.getImToken());

        if (mSubscirber.getId() != null && mSubscirber.getUserName() != null)
        {
            RongYunUtils.updateRongYunUserInfo(new UserInfo(Long.toString(mSubscirber.getId()), mSubscirber.getUserName(), null));
        }
    }

    public void updateLogout() {
        mPrefSetting.clear();
        mSubscirber = new Subscriber();
        mIsLoggedOnNM = false;
    }

    public String getLoginType() {
        return mLoginType;
    }

    public void setLoginType(LoginType type) {
        mLoginType = type.getValue();
        mPrefSetting.setValue(GoGouConstants.KEY_LOGIN_TYPE, mLoginType);
    }

    public String getOauth2AccessToken() {
        return mOauth2AccessToken;
    }

    public void setOauth2AccessToken(String accessToken) {
        mOauth2AccessToken = accessToken;
        mPrefSetting.setValue(GoGouConstants.KEY_OAUTH2_TOKEN, mOauth2AccessToken);
    }

    public String getOauth2RefreshToken() {
        return mOauth2RefreshToken;
    }

    public void setOauth2RefreshToken(String refreshToken) {
        mOauth2RefreshToken = refreshToken;
        mPrefSetting.setValue(GoGouConstants.KEY_OAUTH2_REFRESH_TOKEN, mOauth2RefreshToken);
    }


    private boolean isInMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    private void setupAppFileSystem() {
        FileUtils.makeDirs(GoGouConstants.GOGOU_IMAGE_DIR);
        FileUtils.makeDirs(GoGouConstants.GOGOU_FILE_DIR);
        FileUtils.makeDirs(GoGouConstants.GOGOU_AUDIO_DIR);
    }

    public void setFavorite_trip_list(List<Long> favorite_trip_list) {
        this.favorite_trip_list = favorite_trip_list;
    }

    public void setFavorite_demand_list(List<Long> favorite_demand_list) {
        this.favorite_demand_list = favorite_demand_list;
    }

    public List<Long> getFavorite_trip_list() {
        return favorite_trip_list;
    }

    public List<Long> getFavorite_demand_list() {
        return favorite_demand_list;
    }
}
