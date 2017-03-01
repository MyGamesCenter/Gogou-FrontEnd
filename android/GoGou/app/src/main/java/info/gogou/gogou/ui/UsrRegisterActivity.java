package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import info.gogou.gogou.utils.RongYunUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by grace on 15-10-22.
 */
public class UsrRegisterActivity extends RoboActionBarActivity {

    private static final String TAG = "UsrRegisterActivity";

    @InjectView(R.id.registerEmail)
    private EditText mEmailEdit;
    @InjectView(R.id.registerUsername)
    private EditText mUsernameEdit;
    @InjectView(R.id.registerPassword)
    private EditText mPasswordEdit;
    @InjectView(R.id.registerPasswordConfirm)
    private EditText mConfirmPasswordEdit;
    @InjectView(R.id.registerButton)
    private Button mRegisterBtn;
    @InjectView(R.id.registerBackButton)
    private Button mRegisterBackBtn;
    @InjectView(R.id.registerRegCode)
    private EditText mRegCodeEdit;
    @InjectView(R.id.registerGetRegCode)
    private TextView mGetRegCodeBtn;

    private String userName;
    private String passWord;
    private String passwordConfrim;
    private String userEmail;
    private String regCode;
    private Subscriber mSubscriber;

    private ProgressDialog mProgressSpinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usr_register_layout);

        // retrieve token
       final String token = GogouApplication.getInstance().getGcmToken();
       Log.d(TAG, "GCM Registration Token for subscriber registration: " + token);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchRegisterMsg(false)) {

                    try{
                        mSubscriber = new Subscriber();
                        mSubscriber.setEmailAddress(userEmail);
                        mSubscriber.setUserName(userName);
                        mSubscriber.setRegCode(regCode);
                        mSubscriber.setGender("M");
                        JNCryptor cryptor = new AES256JNCryptor();
                        byte[] cipherText = cryptor.encryptData(passWord.getBytes(), GoGouConstants.SEED_VALUE.toCharArray());
                        String encryptedPassword = Base64.encodeToString(cipherText, Base64.NO_WRAP);
                        mSubscriber.setEncodedPassword(encryptedPassword);
                        mSubscriber.setGcmToken(token);

                        performRegistrationRequest(mSubscriber);

                    } catch (CryptorException e) {
                        Log.e(TAG, "Password can NOT be encrypted: " + e.getMessage());
                    }
                }
            }
        });

        mGetRegCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchRegisterMsg(true)) {
                    performGetRegCodeRequest(userName, userEmail, getResources().getString(R.string.language_code));
                }
            }
        });

        mRegisterBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack2MainScreen(false);

            }
        });
    }

    private void performGetRegCodeRequest(String username, String emailAddress, String languageCode) {
        mProgressSpinner.show();

        RESTRequestUtils.performGetRegCodeRequest(username, emailAddress, languageCode, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(UsrRegisterActivity.this, R.string.notif_get_reg_code_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {

                ErrorType retErrType = genericResponse.getErrorType();

                if (retErrType != ErrorType.NONE) {
                    int strResId = RESTRequestUtils.handleRequestError(retErrType);
                    Toast.makeText(UsrRegisterActivity.this, strResId, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                } else {
                    Toast.makeText(UsrRegisterActivity.this, R.string.notif_get_reg_code_succeed, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                }
            }
        });
    }

    private void performRegistrationRequest(Subscriber subscriber) {
        mProgressSpinner.show();

        RESTRequestUtils.performCreateSubscriberRequest(subscriber, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(UsrRegisterActivity.this, R.string.notif_create_subscriber_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {

                ErrorType retErrType = genericResponse.getErrorType();

                if (retErrType != ErrorType.NONE) {
                    int strResId = RESTRequestUtils.handleRequestError(retErrType);
                    Toast.makeText(UsrRegisterActivity.this, strResId, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                } else {
                    // save subscriber's information to preferences
                    // get message content which is IM token returned by server
                    Log.d(TAG, "IM token returned by server: " + genericResponse.getMessage());
                    mSubscriber.setImToken(genericResponse.getMessage());
                    GogouApplication.getInstance().updateSubscriber(mSubscriber);
                    RongYunUtils.rongYunIMconnect(UsrRegisterActivity.this, mSubscriber.getImToken(), new RESTRequestListener<String>() {
                        @Override
                        public void onGogouRESTRequestFailure() {
                            mProgressSpinner.dismiss();
                            goBack2MainScreen(true);
                        }

                        @Override
                        public void onGogouRESTRequestSuccess(String s) {
                            mProgressSpinner.dismiss();
                            goBack2MainScreen(true);
                        }
                    });
                }
            }
        });
    }

    protected boolean matchRegisterMsg(boolean isGetRegCode) {

        userEmail = mEmailEdit.getText().toString().trim();
        userName = mUsernameEdit.getText().toString().trim();
        passWord = mPasswordEdit.getText().toString().trim();
        passwordConfrim = mConfirmPasswordEdit.getText().toString().trim();
        regCode = mRegCodeEdit.getText().toString().trim();
        //数字和字母
        String r = "^(?=.*\\d.*)(?=.*[a-zA-Z].*).*$";

        //数字，字母，下划线
        //String r = "^(?=.*\\d.*)(?=.*[a-zA-Z].*)(?=.*[-`~!@#$%^&*()_+\\|\\\\=,./?><\\{\\}\\[\\]].*).*$";

        if (userEmail.equals("")) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_email_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userName.equals("")) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_username_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isGetRegCode) return true;

        if (passWord.equals("")) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passWord.matches(r)) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_password_with_one_digit_letter, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passWord.length() < 8) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_password_more_than_eight, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passWord.equals(passwordConfrim)) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_password_not_match, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (regCode.equals("")) {
            Toast.makeText(UsrRegisterActivity.this, R.string.notif_reg_code_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void goBack2MainScreen(boolean isLogin)
    {
        Intent intent = new Intent(UsrRegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        intent.putExtra(GoGouIntents.LOGIN_SUCCEED_FLAG, isLogin);
        intent.putExtra(GoGouIntents.LOGIN_USER_NAME, userName);
        startActivity(intent);
    }
}

