package info.gogou.gogou.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class HelpActivity extends RoboActionBarActivity {

    private static final String TAG = "HelpActivity";

    @InjectView(R.id.helBackBtn)
    private Button helBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);



        helBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, HelpActivity.this, true);

            }
        });
    }
}
