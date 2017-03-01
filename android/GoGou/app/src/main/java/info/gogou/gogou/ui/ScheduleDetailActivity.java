package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.netease.nim.uikit.NimUIKit;
//import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.ui.photo.ShowGalleryActivity;
import info.gogou.gogou.utils.RESTRequestUtils;
import info.gogou.gogou.utils.photo.PhotoUtils;
import io.rong.imkit.RongIM;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


/**
 * Created by grace on 15-11-6.
 */
public class ScheduleDetailActivity extends RoboActionBarActivity {

    private static final String TAG = "ItiDetailActivity";

    // Views
    @InjectView(R.id.itiDetailToolbar)
    private Toolbar mToolBar;
    @InjectView(R.id.itiContactButton)
    private Button mContactBtn;
    @InjectView(R.id.itiDetailBackButton)
    private Button mBackBtn;
    @InjectView(R.id.itiOwnerNameLabel)
    private TextView mUserNameText;
    @InjectView(R.id.itiDetailFromLabel)
    private TextView mFromText;
    @InjectView(R.id.itiDetailDepartureTimeLabel)
    private TextView mDepartDateText;
    @InjectView(R.id.itiDetailToLabel)
    private TextView mToText;
    @InjectView(R.id.itiDetailArrivalTimeLabel)
    private TextView mArrivalDateText;
    @InjectView(R.id.itiDetailMaxWeightLabel)
    private TextView mMaxWeightText;
    @InjectView(R.id.itiDetailMaxVolumnLabel)
    private TextView mMaxVolumnText;
    @InjectView(R.id.itiDetailPrioCategoryLabel)
    private TextView mPrioCategoryText;
    @InjectView(R.id.itiDetailDescriptionLabel)
    private TextView mDetailText;
    @InjectView(R.id.itiDetailImageView)
    private ImageView mPhotoImage;
    @InjectView(R.id.itiDetailNoImageText)
    private TextView mNoImageText;

    private boolean mIsLogin = false;
    private String mChatTo;
    private String mChatToName;
    private String mChatToId;
    private String mChatFrom;
    private long mTripId;
    private Trip mTrip;

    private ArrayList<String> imagesCache = new ArrayList<String>();

    private ProgressDialog mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_detail_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Intent intent = getIntent();
        mTrip = (Trip)intent.getSerializableExtra(GoGouIntents.TRIP);
        if (mTrip != null) {
            mChatTo = mTrip.getUserName();
            mTripId = mTrip.getId();
        }
        Log.d(TAG, "Received trip ID: " + mTripId);

        performRequest();

        // read preferences to see if login/registration is already done
        mChatFrom = GogouApplication.getInstance().getUserName();
        if (mChatFrom != null) {
            mIsLogin = true;
            Log.d(TAG, "Previous login exists user name: " + mChatFrom);
        }

        setSupportActionBar(mToolBar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowTitleEnabled(false);

        mContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLogin) {
                    go2LoginScreen();
                } else {

                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(ScheduleDetailActivity.this, mChatToId, mChatToName);
                    }else{
                        Toast.makeText(ScheduleDetailActivity.this, R.string.notif_im_disabled, Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryActivity();
            }
        });

        updateUIs();
    }


    private void updateUIs() {
        //对聊天对象是否时用户自身进行判断
        if(mChatTo.equals(mChatFrom)){
            mContactBtn.setVisibility(View.GONE);
        }else {
            mContactBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_detail_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_home:
                GoGouIntents.goBack2MainScreen(TAG, this, true);
                break;
            case R.id.menu_order:
                if (!mIsLogin) {
                    // go to login page if user needs to login
                    go2LoginScreen();
                } else {
                    Intent create_order_intent = new Intent(ScheduleDetailActivity.this, CreateOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GoGouIntents.TRIP, mTrip);
                    create_order_intent.putExtras(bundle);
                    startActivity(create_order_intent);
                    finish();
                }
                break;
            case R.id.menu_favorite:
                if (!mIsLogin) {
                    // go to login page if user needs to login
                    go2LoginScreen();
                } else {

                    write(String.valueOf(mTripId));
                    write(" ");

                    Intent my_favorite_intent = new Intent(ScheduleDetailActivity.this, MyFavoriteActivity.class);
                    //应该添加一个将收藏的行程id存储起来的动作
                    startActivity(my_favorite_intent);
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void write(String msg){
        // 步骤1：获取输入值
        if(msg == null) return;
        try {
            // 步骤2:创建一个FileOutputStream对象,MODE_APPEND追加模式
            FileOutputStream fos = openFileOutput("Trip.txt",
                    MODE_APPEND);
            // 步骤3：将获取过来的值放入文件
            fos.write(msg.getBytes());
            // 步骤4：关闭数据流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Long> read() {
        try {
            FileInputStream inStream = this.openFileInput("Trip.txt");
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            String regex = ",|，|\\s+";
            String strAry[] = sb.toString().split(regex);
            List<Long> list = new ArrayList<Long>();

            for (int i = 0; i < strAry.length; i++) {
                list.add(Long.parseLong(strAry[i]));
            }

            inStream.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onResume() {

        super.onResume();
    }


    private void performRequest() {
        mProgressSpinner.show();

        RESTRequestUtils.performGetTripRequest(mTripId, new RESTRequestListener<Trip>() {
            @Override
            public void onGogouRESTRequestFailure() {
                Toast.makeText(ScheduleDetailActivity.this,  R.string.notif_get_trip_failed, Toast.LENGTH_LONG)
                        .show();
                mProgressSpinner.dismiss();
            }

            @Override
            public void onGogouRESTRequestSuccess(Trip trip) {
                mProgressSpinner.dismiss();
                Log.d(TAG, "Get trip id: " + trip.getId());
                mTrip = trip;

                mChatTo = trip.getUserName();
                mChatToId = trip.getSubscriberId();
                mChatToName = trip.getUserName();
                mUserNameText.setText(trip.getUserName());
                mFromText.setText(trip.getOrigin());
                mDepartDateText.setText(trip.getDeparture());
                mToText.setText(trip.getDestination());
                mArrivalDateText.setText(trip.getArrival());
                mMaxWeightText.setText(String.valueOf(trip.getMaxweight()));
                mMaxVolumnText.setText(String.valueOf(trip.getMaxlength()) + " x " +
                        String.valueOf(trip.getMaxwidth()) + " x " +
                        String.valueOf(trip.getMaxheight()));
                mPrioCategoryText.setText("");
                mDetailText.setText(trip.getDescription());

                Log.d(TAG, "Get PrioCategory: " + trip.getCategoryNames());

                StringBuilder temp = new StringBuilder();
                if(trip.getCategoryNames() != null){
                    for (String categoryName : trip.getCategoryNames()) {
                        temp.append(categoryName);
                        temp.append(' ');
                    }
                    mPrioCategoryText.setText(temp.toString());
                }

                List<byte[]> listBytes = trip.getFileOutputs();

                byte[] imageData = null;

                Bitmap bitmap = null;

                String path = null;

                if(listBytes != null){
                    mNoImageText.setVisibility(View.INVISIBLE);
                    for(int i = 0; i<listBytes.size(); i++){
                        imageData = listBytes.get(i);
                        bitmap = PhotoUtils.resizeImage(imageData, 250, 400);
                        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";
                        path = null;
                        try{
                            path = saveBitmap(bitmap,fileName);
                            Log.d(TAG, "image path" + path);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        imagesCache.add(path);
                    }

                    mPhotoImage.setImageBitmap(bitmap);
                }
            }
        });
    }

    private String saveBitmap(Bitmap bitmap,String bitName) throws IOException
    {
        File file = new File("/sdcard/DCIM/Camera/"+bitName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
               {
                    out.flush();
                    out.close();
                }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        String path = file.getAbsolutePath();

        return path;
    }


    public void startGalleryActivity() {

        if(imagesCache.size() != 0){
            Intent intent = new Intent(ScheduleDetailActivity.this, ShowGalleryActivity.class);
            intent.putStringArrayListExtra(ShowGalleryActivity.EXTRA_NAME, imagesCache);
            startActivity(intent);
        }
    }

    private void go2LoginScreen()
    {
        Intent intent = new Intent(ScheduleDetailActivity.this, UsrLoginActivity.class);
        startActivity(intent);
    }
}


