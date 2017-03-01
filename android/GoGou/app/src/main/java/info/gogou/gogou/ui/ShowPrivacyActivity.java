package info.gogou.gogou.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class ShowPrivacyActivity extends RoboActionBarActivity {

    private static final String TAG = "ShowPrivacyActivity";

    @InjectView(R.id.privacyBackBtn)
    private Button privacyBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_privacy);



        privacyBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, ShowPrivacyActivity.this, true);
            }
        });

    }

}
