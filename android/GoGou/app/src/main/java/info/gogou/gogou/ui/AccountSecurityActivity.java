package info.gogou.gogou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import info.gogou.gogou.R;
import roboguice.activity.RoboActionBarActivity;


public class AccountSecurityActivity extends RoboActionBarActivity {

    //@InjectView(R.id.modifyEmailLabel)
    private TextView mModifyEmail;
    //@InjectView(R.id.modifyPasswordLabel)
    private TextView mModifyPassword;
    //@InjectView(R.id.myPaymentLabel)
    private TextView mPaymentInfo;
    //@InjectView(R.id.accountSecurityBackBtn)
    private Button mAccountSecurityBack;
    //@InjectView(R.id.modifyEmailForwardImage)
    private ImageView mModifyEmailBtn;
    //@InjectView(R.id.modifyPwdForwardImage)
    private ImageView mModifyPwdBtn;
    //@InjectView(R.id.paymentForwardImage)
    private ImageView mAddPaymentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_security_layout);

        View.OnClickListener modifyEmailClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSecurityActivity.this, ModifyEmailActivity.class);
                startActivity(intent);
            }
        };
        mModifyEmail = (TextView)findViewById(R.id.modifyEmailLabel);
        mModifyEmailBtn = (ImageView)findViewById(R.id.modifyEmailForwardImage);
        mModifyEmail.setOnClickListener(modifyEmailClickListener);
        mModifyEmailBtn.setOnClickListener(modifyEmailClickListener);

        View.OnClickListener modifyPwdClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSecurityActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        };
        mModifyPassword = (TextView)findViewById(R.id.modifyPasswordLabel);
        mModifyPwdBtn = (ImageView)findViewById(R.id.modifyPwdForwardImage);
        mModifyPassword.setOnClickListener(modifyPwdClickListener);
        mModifyPwdBtn.setOnClickListener(modifyPwdClickListener);


        View.OnClickListener addPaymentClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSecurityActivity.this, MyPaymentActivity.class);
                startActivity(intent);
            }
        };
        mPaymentInfo = (TextView)findViewById(R.id.myPaymentLabel);
        mAddPaymentBtn = (ImageView)findViewById(R.id.paymentForwardImage);
        mPaymentInfo.setOnClickListener(addPaymentClickListener);
        mAddPaymentBtn.setOnClickListener(addPaymentClickListener);

        mAccountSecurityBack = (Button) findViewById(R.id.accountSecurityBackBtn);
        mAccountSecurityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
