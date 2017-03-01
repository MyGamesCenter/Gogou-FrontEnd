package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.RESTRequestUtils;
import roboguice.activity.RoboActionBarActivity;

public class PersonalDocumentActivity extends RoboActionBarActivity {

    private static final String TAG = "PersonalDocumentActivity";

    //@InjectView(R.id.userImage)
    //private ImageView mUserImage;
    //@InjectView(R.id.userNameText)
    private TextView mUserName;
    //@InjectView(R.id.userGenderText)
    private TextView mUserGender;
    //@InjectView(R.id.userInfoBackBtn)
    private Button mPersonalDocumentBack;
    //@InjectView(R.id.avatarForwardImage)
    //private ImageView changeAvatarBtn;
    //@InjectView(R.id.userGenderForwardImage)
    private ImageView changeGenderBtn;
    //@InjectView(R.id.userAddressForwardImage)
    private ImageView changeAddressBtn;

    private View mParentView;
    //private PopupWindow mImagePopupMenu;
    private PopupWindow mGenderPopupMenu;

    private ProgressDialog mProgressSpinner;

    private Subscriber mSubscriber;
    private String mPrevGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentView = getLayoutInflater().inflate(R.layout.personal_document_layout, null);
        setContentView(mParentView);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // read preferences to see if login/registration is already done
        mUserName = (TextView)findViewById(R.id.userNameText);
        mUserName.setText(GogouApplication.getInstance().getUserName());
        mUserGender = (TextView)findViewById(R.id.userGenderText);
        mUserGender.setText(GogouApplication.getInstance().getGenderStr());

        mPersonalDocumentBack = (Button)findViewById(R.id.userInfoBackBtn);
        mPersonalDocumentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changeGenderBtn = (ImageView)findViewById(R.id.userGenderForwardImage);
        changeAddressBtn = (ImageView)findViewById(R.id.userAddressForwardImage);

        initPopupMenus();

        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalDocumentActivity.this, MyAddressActivity.class);
                startActivity(intent);
            }
        });

        mSubscriber = GogouApplication.getInstance().getSubscriber();
        mPrevGender = mSubscriber.getGender();
    }

    private void initPopupMenus()
    {
        changeGenderBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mGenderPopupMenu.setAnimationStyle(R.style.releasepopwindow_anim_style);
                mGenderPopupMenu.showAtLocation(mParentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        int[] menuIds = {R.id.selectMaleBtn, R.id.selectFemaleBtn, R.id.selectGenderCancel};
        mGenderPopupMenu = new BottomPopupWindow(this,
                                                R.layout.gender_popupwindows,
                                                R.id.genderPopupMenu,
                                                menuIds,
                                                genderPopupMenuItemOnClick);
    }

    private View.OnClickListener genderPopupMenuItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.selectMaleBtn:
                    mGenderPopupMenu.dismiss();
                    mSubscriber.setGender("M");
                    break;
                case R.id.selectFemaleBtn:
                    mGenderPopupMenu.dismiss();
                    mSubscriber.setGender("F");
                    break;
                case R.id.selectGenderCancel:
                default:
                    mGenderPopupMenu.dismiss();
                    return;
            }
            performUpdateSubscirber(mSubscriber);
        }
    };

    private void performUpdateSubscirber(final Subscriber subscriber) {

        mProgressSpinner.show();

        RESTRequestUtils.performUpdateSubscriberRequest(subscriber, new RESTRequestListener<GenericResponse>() {

            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(PersonalDocumentActivity.this, R.string.notif_update_subscriber_failed, Toast.LENGTH_LONG).show();
                mSubscriber.setGender(mPrevGender);
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(GenericResponse genericResponse) {
                Toast.makeText(PersonalDocumentActivity.this, R.string.notif_update_subscriber_succeed, Toast.LENGTH_LONG).show();
                GogouApplication.getInstance().updateSubscriber(subscriber);
                mUserGender.setText(GogouApplication.getInstance().getGenderStr());
                mProgressSpinner.dismiss();
            }
        });
    }
 }
