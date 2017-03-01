package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;

public class ModifyPasswordActivity extends RoboActionBarActivity {

    private static final String TAG = "ModifyPasswordActivity";

    //@InjectView(R.id.oldPasswordText)
    private EditText mOriginPassword;
    //@InjectView(R.id.newPasswordText)
    private EditText mNewPassword;
    //@InjectView(R.id.confirmNewPasswordText)
    private EditText mConfirmPassword;
    //@InjectView(R.id.modify_password_submit_Button)
    private Button mModifyPassSubmit;
    //@InjectView(R.id.modify_password_back_Button)
    private Button mModifyPassBack;

    private ProgressDialog mProgressSpinner;
    private Subscriber mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password_layout);

        mOriginPassword = (EditText)findViewById(R.id.oldPasswordText);
        mNewPassword = (EditText)findViewById(R.id.newPasswordText);
        mConfirmPassword = (EditText)findViewById(R.id.confirmNewPasswordText);
        mModifyPassSubmit = (Button) findViewById(R.id.modify_password_submit_Button);
        mModifyPassBack = (Button)findViewById(R.id.modify_password_back_Button);


        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        mModifyPassSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFormat()) {

                    performUpdateSubscirber(mSubscriber);
                }
            }
        });

        mModifyPassBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void performUpdateSubscirber(final Subscriber subscriber) {

        mProgressSpinner.show();

        RESTRequestUtils.performUpdateSubscriberRequest(subscriber, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(ModifyPasswordActivity.this, R.string.notif_update_subscriber_failed, Toast.LENGTH_LONG).show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(ModifyPasswordActivity.this, R.string.notif_update_subscriber_succeed, Toast.LENGTH_LONG).show();
                subscriber.setEncodedPassword(subscriber.getClearPassword());
                subscriber.setClearPassword(null);
                GogouApplication.getInstance().updateSubscriber(subscriber);
                mProgressSpinner.dismiss();
            }
        });
    }

    protected boolean checkFormat() {

        String originPassword = mOriginPassword.getText().toString().trim();
        String newPassword = mNewPassword.getText().toString().trim();
        String confirmNewPassword= mConfirmPassword.getText().toString().trim();

        //数字和字母
        String r = "^(?=.*\\d.*)(?=.*[a-zA-Z].*).*$";

        if (originPassword.equals("")) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!originPassword.matches(r)) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_with_one_digit_letter, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (originPassword.length() < 8) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_more_than_eight, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.equals("")) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.matches(r)) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_with_one_digit_letter, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.length() < 8) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_more_than_eight, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_not_match, Toast.LENGTH_SHORT).show();
            return false;
        }

        mSubscriber = GogouApplication.getInstance().getSubscriber();
        try {

            JNCryptor cryptor = new AES256JNCryptor();
            byte[] storedPassword = cryptor.decryptData(Base64.decode(mSubscriber.getEncodedPassword(), Base64.NO_WRAP), GoGouConstants.SEED_VALUE.toCharArray());
            String storedPasswordStr = new String(storedPassword);
            if (!originPassword.equals(storedPasswordStr)) {
                Toast.makeText(ModifyPasswordActivity.this, R.string.notif_password_not_match, Toast.LENGTH_SHORT).show();
                return false;
            }

            byte[] newCipherText = cryptor.encryptData(newPassword.getBytes(), GoGouConstants.SEED_VALUE.toCharArray());
            String newEncryptedPassword = Base64.encodeToString(newCipherText, Base64.NO_WRAP);
            mSubscriber.setClearPassword(newEncryptedPassword);

        } catch (CryptorException e) {
            Log.e(TAG, "Password can NOT be encrypted: " + e.getMessage());
        }

        return true;
    }

}
