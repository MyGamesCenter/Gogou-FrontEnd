package info.gogou.gogou.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class ShowTCActivity extends RoboActionBarActivity {

    private static final String TAG = "ShowTCActivity";

    @InjectView(R.id.tcBackBtn)
    private Button tcBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_terms_conditions);


        tcBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, ShowTCActivity.this, true);
            }
        });

    }

}
