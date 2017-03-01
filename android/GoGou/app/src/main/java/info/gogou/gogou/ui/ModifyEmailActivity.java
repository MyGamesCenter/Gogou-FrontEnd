package info.gogou.gogou.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.gogou.gogou.R;
import roboguice.activity.RoboActionBarActivity;

public class ModifyEmailActivity extends RoboActionBarActivity {

    //@InjectView(R.id.newEmailText)
    //private EditText mNewEmail;
    //@InjectView(R.id.modifyEmailBackBtn)
    private Button mModifyEmailBack;
    //@InjectView(R.id.modifyEmailNextStepButton)
    private Button mNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_email_layout);


        mNextStep = (Button)findViewById(R.id.modifyEmailNextStepButton);
        mNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mModifyEmailBack = (Button)findViewById(R.id.modifyEmailBackBtn);
        mModifyEmailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
