package info.gogou.gogou.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class AboutActivity extends RoboActionBarActivity {

    private static final String TAG = "AboutActivity";

    @InjectView(R.id.aboutBackBtn)
    private Button aboutBackBtn;
    @InjectView(R.id.showTermConditions)
    private TextView showTermConditionsText;
    @InjectView(R.id.showPrivacy)
    private TextView showPrivacyText;
    @InjectView(R.id.tcForward)
    private ImageView tcForwardBtn;
    @InjectView(R.id.privacyForward)
    private ImageView privacyForwardBtn;
    @InjectView(R.id.gogouVersion)
    private TextView gogouVersionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);


        aboutBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GoGouIntents.goBack2MainScreen(TAG, AboutActivity.this, true);
            }
        });

        View.OnClickListener tcClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, ShowTCActivity.class);
                startActivity(intent);
            }
        };

        View.OnClickListener privacyClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, ShowPrivacyActivity.class);
                startActivity(intent);
            }
        };

        showTermConditionsText.setOnClickListener(tcClickListener);
        tcForwardBtn.setOnClickListener(tcClickListener);
        showPrivacyText.setOnClickListener(privacyClickListener);
        privacyForwardBtn.setOnClickListener(privacyClickListener);

        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            gogouVersionText.setText(packageInfo.versionName);

        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            Log.e(TAG, "Can NOT get GoGou version: " + e.getMessage());
        }


    }
}
