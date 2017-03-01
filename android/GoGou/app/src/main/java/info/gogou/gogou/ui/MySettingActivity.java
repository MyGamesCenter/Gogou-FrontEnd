package info.gogou.gogou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.netease.nim.uikit.NimUIKit;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.auth.AuthService;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.utils.PreferencesUtil;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


public class MySettingActivity extends RoboActionBarActivity {

    private static final String TAG = "MySettingActivity";

    @InjectView(R.id.mySettingBackButton)
    private Button mBackBtn;
    @InjectView(R.id.logoutButton)
    private Button mLogoutBtn;
    @InjectView(R.id.personal_document_text)
    private TextView personal_document;
    @InjectView(R.id.account_security_text)
    private TextView account_security;
    @InjectView(R.id.userInfoForward)
    private ImageView mUserInfoForwardBtn;
    @InjectView(R.id.accountSecurityForward)
    private ImageView mAccountSecurityForwardBtn;

    private PreferencesUtil mPrefSetting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goBack2MainScreen(true);

            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear subscriber's information to preferences
                GogouApplication.getInstance().updateLogout();

//                NIMClient.getService(AuthService.class).logout();
//                NimUIKit.clearCache();
                goBack2MainScreen(false);
            }
        });


        View.OnClickListener userInfoClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySettingActivity.this, PersonalDocumentActivity.class);
                startActivity(intent);
            }
        };
        personal_document.setOnClickListener(userInfoClickListener);
        mUserInfoForwardBtn.setOnClickListener(userInfoClickListener);

        View.OnClickListener accountSecurityClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySettingActivity.this, AccountSecurityActivity.class);
                startActivity(intent);
            }
        };
        account_security.setOnClickListener(accountSecurityClickListener);
        mAccountSecurityForwardBtn.setOnClickListener(accountSecurityClickListener);
    }

    private void goBack2MainScreen(boolean isLogin)
    {
        Intent intent = new Intent(MySettingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        intent.putExtra(GoGouIntents.LOGIN_SUCCEED_FLAG, isLogin);
        startActivity(intent);
    }
}
