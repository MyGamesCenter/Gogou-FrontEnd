package info.gogou.gogou.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.constants.LoginType;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.LoginRequest;
import info.gogou.gogou.model.OAuth2Login;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.request.SubscriberRequest;
import info.gogou.gogou.utils.EmailUtil;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import info.gogou.gogou.utils.RongYunUtils;
import info.gogou.gogou.utils.photo.PhotoUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


/**
 * Created by grace on 15-10-21.
 */
public class UsrLoginActivity extends RoboActionBarActivity {

    private static final String TAG = "UsrLoginActivity";

    //Views
    @InjectView(R.id.loginNameText)
    private EditText mUserNameEdit;
    @InjectView(R.id.loginPasswordText)
    private EditText mPasswordEdit;
    @InjectView(R.id.loginButton)
    private Button mLoginBtn;
    @InjectView(R.id.loginRegisterButton)
    private TextView mRegisterBtn;
    @InjectView(R.id.loginBackButton)
    private Button mLoginBackBtn;
    @InjectView(R.id.wechatLoginButton)
    private ImageButton mWechatLoginButton;
    @InjectView(R.id.forgotPasswordButton)
    private TextView mForgotPasswordBtn;

    private String mEmailAddress;
    private String mUserName;
    private String mPassword;

    private ProgressDialog mProgressSpinner;

    private String mLastRequestCacheKey;

    public static String wechatCode;
    private LoginType mLoginType = LoginType.NORMAL;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_login_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchLoginMsg()) {
                    try {

                        LoginRequest loginRequest = new LoginRequest();
                        loginRequest.setUsername(mUserName);
                        loginRequest.setEmailAddress(mEmailAddress);
                        JNCryptor cryptor = new AES256JNCryptor();
                        byte[] cipherText = cryptor.encryptData(mPassword.getBytes(), GoGouConstants.SEED_VALUE.toCharArray());
                        String encryptedPassword = Base64.encodeToString(cipherText, Base64.NO_WRAP);
                        loginRequest.setPassword(encryptedPassword);
                        performLoginRequest(loginRequest);
                    } catch (CryptorException e) {
                        Log.e(TAG, "Password can NOT be encrypted: " + e.getMessage());
                    }
                }
            }
        });


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsrLoginActivity.this, UsrRegisterActivity.class);
                startActivity(intent);
            }
        });


        mLoginBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack2MainScreen(false);
            }
        });

        mWechatLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mLoginType = LoginType.WECHAT;
                mProgressSpinner.show();

                IWXAPI api = WXAPIFactory.createWXAPI(UsrLoginActivity.this, GoGouConstants.WEIXIN_APP_ID, true);
                api.registerApp(GoGouConstants.WEIXIN_APP_ID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_gogou";
                api.sendReq(req);

            }
        });

        mForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsrLoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLoginRequest(LoginRequest loginRequest) {
        mProgressSpinner.show();

        SubscriberRequest.SubscriberLoginRequest request = new SubscriberRequest.SubscriberLoginRequest(loginRequest);
        mLastRequestCacheKey = request.createCacheKey();
        GogouApplication.getInstance().getSpiceManager().execute(request, mLastRequestCacheKey,
                DurationInMillis.ONE_MINUTE, new RequestListener<Subscriber>() {

                    @Override
                    public void onRequestFailure(SpiceException e) {
                        Toast.makeText(UsrLoginActivity.this, R.string.notif_login_failed, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                        mProgressSpinner.dismiss();
                    }

                    @Override
                    public void onRequestSuccess(Subscriber subscriber) {
                        if (subscriber == null) {
                            Toast.makeText(UsrLoginActivity.this, R.string.notif_login_failed, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Login subscirber: " + mUserName + " failed");
                            mProgressSpinner.dismiss();

                        } else {
                            // save subscriber's information to preferences

                            mUserName = subscriber.getUserName();
                            String savedToken = GogouApplication.getInstance().getGcmToken();
                            GogouApplication.getInstance().updateSubscriber(subscriber);

                            // if the saved server token is not the local token, then we update it on server
                            if (subscriber.getGcmToken() == null || !subscriber.getGcmToken().equals(savedToken))
                            {
                                subscriber.setGcmToken(savedToken);
                                performUpdateSubscirber(subscriber);
                            }
                            else
                            {
                                RongYunUtils.rongYunIMconnect(UsrLoginActivity.this, subscriber.getImToken(), new RESTRequestListener<String>() {
                                    @Override
                                    public void onGogouRESTRequestFailure() {
                                        mProgressSpinner.dismiss();
                                        goBack2MainScreen(true);
                                    }

                                    @Override
                                    public void onGogouRESTRequestSuccess(String userid) {
                                        mProgressSpinner.dismiss();
                                        goBack2MainScreen(true);
                                    }
                                });
                            }
                        }
                    }
                }
        );
    }

    private void performOAuth2LoginRequest(String code) {
        mProgressSpinner.show();

        RESTRequestUtils.performOAuth2LoginRequest(code, new RESTRequestListener<OAuth2Login>() {
            @Override
            public void onGogouRESTRequestFailure() {
                mLoginType = LoginType.NORMAL;
                Toast.makeText(UsrLoginActivity.this, R.string.notif_wechat_login_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(OAuth2Login oAuth2Login) {

                if (oAuth2Login.getErrcode() != null) {
                    mLoginType = LoginType.NORMAL;
                    Toast.makeText(UsrLoginActivity.this, R.string.notif_wechat_login_failed, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "WXEntryActivity ThirdPartLoginRequest: " + oAuth2Login.getErrmsg());
                    mProgressSpinner.dismiss();
                } else {

                    Subscriber subscriber = new Subscriber();
                    subscriber.setUserName(mUserName = oAuth2Login.getNickname());
                    subscriber.setEmailAddress("");
                    subscriber.setGender(oAuth2Login.getGender());
                    subscriber.setEncodedPassword(oAuth2Login.getAccessToken());

                    // save image from oAuth2Login
                    String imagePath = PhotoUtils.saveBitmap(PhotoUtils.resizeImage(oAuth2Login.getFileOutput(),
                                    GoGouConstants.HEAD_IMAGE_WIDTH,
                                    GoGouConstants.HEAD_IMAGE_HEIGHT),
                            GoGouConstants.HEADIMAGE_NAME);
                    if (imagePath != null) {
                        subscriber.setHeadImage(imagePath);
                        Log.d(TAG, "Saved head image path: " + imagePath);
                    }

                    // save subscriber's information to preferences
                    GogouApplication.getInstance().updateSubscriber(subscriber);
                    GogouApplication.getInstance().setLoginType(mLoginType);
                    GogouApplication.getInstance().setOauth2AccessToken(oAuth2Login.getAccessToken());
                    GogouApplication.getInstance().setOauth2RefreshToken(oAuth2Login.getRefreshToken());
                    /*String savedToken = mPrefSetting.getValue(GoGouConstants.KEY_TOKEN, null);

                    // if the saved server token is not the local token, then we update it on server
                    if (subscriber.getGcmToken() == null || !subscriber.getGcmToken().equals(savedToken))
                    {
                        subscriber.setGcmToken(savedToken);
                        performUpdateRequest(subscriber);
                    }
                    else
                    {
                        mProgressSpinner.dismiss();
                        goBack2MainScreen(true);
                    }*/
                    mProgressSpinner.dismiss();
                    goBack2MainScreen(true);
                }
            }
        });
    }

    private void performUpdateSubscirber(final Subscriber subscriber) {

        mProgressSpinner.show();

        RESTRequestUtils.performUpdateSubscriberRequest(subscriber, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(UsrLoginActivity.this, R.string.notif_update_subscriber_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
                goBack2MainScreen(true);
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(UsrLoginActivity.this, R.string.notif_update_subscriber_succeed, Toast.LENGTH_LONG).show();
                GogouApplication.getInstance().updateSubscriber(subscriber);
                RongYunUtils.rongYunIMconnect(UsrLoginActivity.this, subscriber.getImToken(), new RESTRequestListener<String>() {
                    @Override
                    public void onGogouRESTRequestFailure() {
                        mProgressSpinner.dismiss();
                        goBack2MainScreen(true);
                    }

                    @Override
                    public void onGogouRESTRequestSuccess(String userid) {
                        mProgressSpinner.dismiss();
                        goBack2MainScreen(true);
                    }
                });
            }
        });
    }


    // 表单校验
    protected boolean matchLoginMsg() {

        String userId = mUserNameEdit.getText().toString().trim();

        if (userId.equals("")) {
            Toast.makeText(UsrLoginActivity.this, R.string.notif_account_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (EmailUtil.verifyEmail(userId)) {
            mEmailAddress = userId;
            mUserName = "";
        } else {
            mUserName = userId;
            mEmailAddress = "xxxxxx";
        }

        mPassword = mPasswordEdit.getText().toString().trim();
        if (mPassword.equals("")) {
            Toast.makeText(UsrLoginActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume(){
        if (mLoginType == LoginType.WECHAT) {
            performOAuth2LoginRequest(wechatCode);
        }
        super.onResume();
    }

    private void goBack2MainScreen(boolean isLogin)
    {
        Intent intent = new Intent(UsrLoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        intent.putExtra(GoGouIntents.LOGIN_SUCCEED_FLAG, isLogin);
        intent.putExtra(GoGouIntents.LOGIN_USER_NAME, mUserName);
        startActivity(intent);
    }
}
