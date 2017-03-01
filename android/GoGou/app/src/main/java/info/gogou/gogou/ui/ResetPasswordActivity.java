package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.RecoverPwdRequest;
import info.gogou.gogou.utils.EmailUtil;
import info.gogou.gogou.utils.ErrorType;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class ResetPasswordActivity extends RoboActionBarActivity {

    private static final String TAG = "ResetPasswordActivity";

    @InjectView(R.id.resetPasswordUserIdLayout)
    private RelativeLayout mUserIdLayout;
    @InjectView(R.id.resetPasswordRecoverCodeLayout)
    private RelativeLayout mRecoverCodeLayout;
    @InjectView(R.id.resetPasswordLayout)
    private RelativeLayout mResetPasswordLayout;
    @InjectView(R.id.resetPassword2Layout)
    private RelativeLayout mResetPassword2Layout;
    @InjectView(R.id.resetPasswordUserText)
    private EditText mEmailUsername;
    @InjectView(R.id.recoverCode)
    private EditText mRecoverCodeEdit;
    //@InjectView(R.id.getRecoverCode)
    //private TextView mGetRecoverCodeBtn;
    @InjectView(R.id.resetPassword)
    private EditText mResetPassword;
    @InjectView(R.id.resetPassword2)
    private EditText mResetPassword2;
    @InjectView(R.id.resetPasswordNextStep)
    private Button mNextStep;
    @InjectView(R.id.resetPasswordConfirm)
    private Button mConfirm;
    @InjectView(R.id.resetPasswordBackBtn)
    private Button mBackBtn;

    private String mEmailAddress;
    private String mUsername;
    private String mPassword;
    private String mConfirmedPass;
    private String mRecoverCode;

    private ProgressDialog mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFormat(true)) {
                    performGetRecoverCodeRequest(mUsername, mEmailAddress, getResources().getString(R.string.language_code));
                }
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFormat(false)) {

                    try {

                        RecoverPwdRequest recoverPwdRequest = new RecoverPwdRequest();
                        recoverPwdRequest.setUsername(mUsername);
                        recoverPwdRequest.setEmailAddress(mEmailAddress);
                        JNCryptor cryptor = new AES256JNCryptor();
                        byte[] cipherText = cryptor.encryptData(mPassword.getBytes(), GoGouConstants.SEED_VALUE.toCharArray());
                        String encryptedPassword = Base64.encodeToString(cipherText, Base64.NO_WRAP);
                        recoverPwdRequest.setPassword(encryptedPassword);
                        recoverPwdRequest.setRecoverCode(mRecoverCode);

                        performGetRecoverCodeRequest(recoverPwdRequest);

                    } catch (CryptorException e) {
                        Log.e(TAG, "Password can NOT be encrypted: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void performGetRecoverCodeRequest(String username, String emailAddress, String languageCode) {
        mProgressSpinner.show();

        RESTRequestUtils.performGetRecoverCodeRequest(username, emailAddress, languageCode, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_get_reg_code_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {

                ErrorType retErrType = genericResponse.getErrorType();

                if (retErrType != ErrorType.NONE) {
                    int strResId = RESTRequestUtils.handleRequestError(retErrType);
                    Toast.makeText(ResetPasswordActivity.this, strResId, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, R.string.notif_get_reg_code_succeed, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                    updateUI();
                }
            }
        });
    }

    private void performGetRecoverCodeRequest(RecoverPwdRequest recoverPwdRequest) {
        mProgressSpinner.show();

        RESTRequestUtils.performRecoverPasswordRequest(recoverPwdRequest, new RESTRequestListener<GenericResponse>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_recover_password_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {

                ErrorType retErrType = genericResponse.getErrorType();

                if (retErrType != ErrorType.NONE) {
                    int strResId = RESTRequestUtils.handleRequestError(retErrType);
                    Toast.makeText(ResetPasswordActivity.this, strResId, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, R.string.notif_recover_password_succeed, Toast.LENGTH_LONG).show();
                    mProgressSpinner.dismiss();
                    finish();
                }
            }
        });
    }

    protected boolean checkFormat(boolean isGetRecoverCode) {

        if (isGetRecoverCode) {
            String userId = mEmailUsername.getText().toString().trim();

            if (userId.equals("")) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_username_notnull, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (EmailUtil.verifyEmail(userId)) {
                mEmailAddress = userId;
                mUsername = "";
            } else {
                mUsername = userId;
                mEmailAddress = "";
            }

            return true;
        } else {
            mPassword = mResetPassword.getText().toString().trim();
            mConfirmedPass = mResetPassword2.getText().toString().trim();
            mRecoverCode = mRecoverCodeEdit.getText().toString().trim();

            //数字和字母
            String r = "^(?=.*\\d.*)(?=.*[a-zA-Z].*).*$";

            if (mPassword.equals("")) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!mPassword.matches(r)) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_password_with_one_digit_letter, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (mConfirmedPass.length() < 8) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_password_more_than_eight, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!mPassword.equals(mConfirmedPass)) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_password_not_match, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (mRecoverCode.equals("")) {
                Toast.makeText(ResetPasswordActivity.this, R.string.notif_reg_code_notnull, Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
    }

    private void updateUI() {
        mUserIdLayout.setVisibility(View.GONE);
        mNextStep.setVisibility(View.GONE);

        mRecoverCodeLayout.setVisibility(View.VISIBLE);
        mResetPasswordLayout.setVisibility(View.VISIBLE);
        mResetPassword2Layout.setVisibility(View.VISIBLE);
        mConfirm.setVisibility(View.VISIBLE);
    }

}
