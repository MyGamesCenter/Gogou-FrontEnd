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
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.request.DemandRequest;
import info.gogou.gogou.ui.photo.ShowGalleryActivity;
import info.gogou.gogou.utils.photo.PhotoUtils;
import io.rong.imkit.RongIM;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


/**
 * Created by grace on 15-11-6.
 */
public class RequirementDetailActivity extends RoboActionBarActivity {

    private static final String TAG = "DemandDetailActivity";

    @InjectView(R.id.demandDetailToolbar)
    private Toolbar mToolBar;
    @InjectView(R.id.demandBackButton)
    private Button mBackBtn;
    @InjectView(R.id.demandContactButton)
    private Button mContactBtn;
    @InjectView(R.id.demandOwnerNameLabel)
    private TextView mUserNameText;
    @InjectView(R.id.demandDetailProductNameLabel)
    private TextView mProductNameText;
    @InjectView(R.id.demandDetailCategoryLabel)
    private TextView mCategoryText;
    @InjectView(R.id.demandDetailProductOriginLabel)
    private TextView mProductOriginText;
    @InjectView(R.id.demandDetailQuantityLabel)
    private TextView mNumberText;
    @InjectView(R.id.demandDetailCollectLocationLabel)
    private TextView mDestinationText;
    @InjectView(R.id.demandDetailBrandLabel)
    private TextView mBrandText;
    @InjectView(R.id.demandDetailPriceLabel)
    private TextView mAcceptedPriceText;
    @InjectView(R.id.demandDetailExpectedDateLabel)
    private TextView mExpectedDateText;
    @InjectView(R.id.demandDetailDescriptionLabel)
    private TextView mDetailText;
    @InjectView(R.id.demandDetailNoImageText)
    private TextView mNoImageText;
    @InjectView(R.id.demandDetailImageView)
    private ImageView mDemandPhoto;

    private boolean mIsLogin = false;
    private String mChatTo;
    private String mChatToName;
    private String mChatFrom;
    private String mChatToId;
    private Demand mDemand;
    private long mDemandId;

    private ArrayList<String> imagesCache = new ArrayList<String>();

    private ProgressDialog mProgressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requirement_detail_layout);

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Intent intent = getIntent();
        mDemand = (Demand)intent.getSerializableExtra(GoGouIntents.DEMAND);
        if (mDemand != null) {
            mChatTo = mDemand.getUserName();
            mDemandId = mDemand.getId();
        }
        Log.d(TAG, "Received demand ID: " + mDemandId);

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

                    //NimUIKit.startChatting(RequirementDetailActivity.this, mChatToId, SessionTypeEnum.P2P, null);
                    //NimUIKit.startChatting(ScheduleDetailActivity.this, mChatToId, SessionTypeEnum.P2P, null);

                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(RequirementDetailActivity.this, mChatToId, mChatToName);
                    }else{
                        Toast.makeText(RequirementDetailActivity.this, R.string.notif_im_disabled, Toast.LENGTH_LONG).show();
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

        mDemandPhoto.setOnClickListener(new View.OnClickListener() {
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
        getMenuInflater().inflate(R.menu.demand_detail_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_favorite:
                if (!mIsLogin) {
                    // go to login page if user needs to login
                    go2LoginScreen();
                } else {
                    write(String.valueOf(mDemandId));
                    write(" ");
                    Intent my_favorite_intent = new Intent(RequirementDetailActivity.this, MyFavoriteActivity.class);
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

    private void write(String msg){
        // 步骤1：获取输入值
        if(msg == null) return;
        try {
            // 步骤2:创建一个FileOutputStream对象,MODE_APPEND追加模式
            FileOutputStream fos = openFileOutput("Demand.txt",
                    MODE_APPEND);
            // 步骤3：将获取过来的值放入文件
            fos.write(msg.getBytes());
            // 步骤4：关闭数据流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void performRequest() {
        mProgressSpinner.show();
        DemandRequest.GetDemandRequest request = new DemandRequest.GetDemandRequest(mDemandId);
        GogouApplication.getInstance().getSpiceManager().execute(request, new GetDemandRequestListener());
    }

    private class GetDemandRequestListener implements
            RequestListener<Demand> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(RequirementDetailActivity.this, R.string.notif_get_demand_failed, Toast.LENGTH_LONG)
                    .show();
            Log.d(TAG, "Error during request: " + e.getLocalizedMessage());
            mProgressSpinner.dismiss();
        }

        @Override
        public void onRequestSuccess(Demand demand) {

            if (demand == null)
            {
                Toast.makeText(RequirementDetailActivity.this, R.string.notif_get_demand_failed, Toast.LENGTH_LONG).show();
                Log.d(TAG, "Getting demand failed");
                mProgressSpinner.dismiss();
            }
            else
            {
                mProgressSpinner.dismiss();
                Log.d(TAG, "Get demand id: " + demand.getId());

                mChatTo = demand.getUserName();
                mChatToId = demand.getSubscriberId();
                mChatToName = demand.getUserName();
                mUserNameText.setText(demand.getUserName());
                mProductNameText.setText(demand.getProductname());
                mCategoryText.setText(demand.getCategoryName());
                mProductOriginText.setText(demand.getOrigin());
                mNumberText.setText(String.valueOf(demand.getQuantity()));
                mDestinationText.setText(demand.getDestination());
                mBrandText.setText(demand.getBrand());
                if( demand.getAcceptedPrice() == null){
                    mAcceptedPriceText.setText(String.valueOf(0.0));
                }
                else{
                    mAcceptedPriceText.setText(String.valueOf(demand.getAcceptedPrice()));
                }
                mExpectedDateText.setText(demand.getExpectTime());
                mDetailText.setText(demand.getDescription());

                List<byte[]> listBytes = demand.getFileOutputs();

                byte[] imageData = null;

                Bitmap bitmap = null;

                String path =null;

                if(listBytes != null) {
                    mNoImageText.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < listBytes.size(); i++) {
                        imageData = listBytes.get(i);
                        bitmap = PhotoUtils.resizeImage(imageData, 250, 400);
                        String fileName = String.valueOf(System.currentTimeMillis()) + ".png";

                        try{
                            path = saveBitmap(bitmap,fileName);
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                        imagesCache.add(path);
                    }

                    mDemandPhoto.setImageBitmap(bitmap);
                }
            }
        }
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


    private void startGalleryActivity() {

        if(imagesCache.size() != 0){
            Intent intent = new Intent(RequirementDetailActivity.this, ShowGalleryActivity.class);
            intent.putStringArrayListExtra(ShowGalleryActivity.EXTRA_NAME, imagesCache);
            startActivity(intent);
        }
    }

    private void go2LoginScreen()
    {
        Intent intent = new Intent(RequirementDetailActivity.this, UsrLoginActivity.class);
        startActivity(intent);
    }
}
