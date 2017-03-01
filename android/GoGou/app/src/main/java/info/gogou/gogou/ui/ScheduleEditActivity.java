package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.CategorySpinnerAdapter;
import info.gogou.gogou.adapter.ImageGridForPublishAdapter;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.request.TripRequest;
import info.gogou.gogou.ui.photo.AlbumActivity;
import info.gogou.gogou.ui.photo.GalleryActivity;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.photo.ImageItem;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Created by grace on 15-10-27.
 */
public class ScheduleEditActivity extends RoboActionBarActivity {

    private static final String TAG = "ScheduleEditActivity";

    @InjectView(R.id.itiCreateConfirmButton)
    private Button mConfirmBtn;
    @InjectView(R.id.itiCreateBackButton)
    private Button mBackBtn;
    @InjectView(R.id.itiCreateFrom)
    private EditText mItiFromEdit;
    @InjectView(R.id.itiDepartureTime)
    private TextView mDepartureTimeEdit;
    @InjectView(R.id.itiCreateTo)
    private EditText mItiToEdit;
    @InjectView(R.id.itiCreateArrivalTime)
    private TextView mArrivalTimeEdit;
    @InjectView(R.id.itiCreateDescription)
    private EditText mDescriptionEdit;
    @InjectView(R.id.itiCreateMaxWeight)
    private EditText mMaxWeightEdit;
    @InjectView(R.id.itiCreateLength)
    private EditText mMaxLengthEdit;
    @InjectView(R.id.itiCreateWidth)
    private EditText mMaxWidthEdit;
    @InjectView(R.id.itiCreateHeight)
    private EditText mMaxHeightEdit;
    @InjectView(R.id.itiCreatePriorityCategorySpinner)
    private Spinner mPriorityCategorySpinner;
    @InjectView(R.id.itiCreateSelectImageBtn)
    private ImageView mSelectImageBtn;
    @InjectView(R.id.itiCreatePhotoGridView)
    private GridView mPhotoGridView;

    private ProgressDialog mProgressSpinner;

    private Trip mTrip;
    private String mDepartureTimeText;
    private String mArrivalTimeText;
    private String mItiFrom;
    private String mItiTo;
    private List<String> mCategoryNames = new ArrayList<String>();
    private String mMaxWeight;
    private String mMaxLength;
    private String mMaxWidth;
    private String mMaxHeight;

    private Date mDepartureTime;
    private Date mArrivalTime;

    private String mUserName = null;
    private String mEncodedPassword = null;

    private ImageGridForPublishAdapter mAdapter;
    private View mParentView;
    private PopupWindow mImagePopupMenu;

    private ArrayList<ImageItem> mSelectedPhotos = new ArrayList<ImageItem>();

    private Uri mPhotoURI;//自动获取照片uri

    //对页面中的控件进行响应
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.itiDepartureTime:
                    DatePickerDialog departDatePickerDialog = new DatePickerDialog(
                            ScheduleEditActivity.this, mDepartureTimeText);
                    departDatePickerDialog.datePickerDialog(mDepartureTimeEdit);
                    break;
                case R.id.itiCreateArrivalTime:
                    DatePickerDialog arrive_datePickerDialog = new DatePickerDialog(
                            ScheduleEditActivity.this, mArrivalTimeText);
                    arrive_datePickerDialog.datePickerDialog(mArrivalTimeEdit);
                    break;
                case R.id.itiCreateConfirmButton:
                    mDepartureTimeText = mDepartureTimeEdit.getText().toString();
                    mArrivalTimeText = mArrivalTimeEdit.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
                    try {
                        mDepartureTime = sdf.parse(mDepartureTimeText);
                        mArrivalTime = sdf.parse(mArrivalTimeText);
                    } catch (ParseException e) {
                        Log.i("ParseException", "parseException");
                    }

                    if (matchScheduleEditMsg()) {

                        mTrip = new Trip();
                        mTrip.setUserName(mUserName);
                        mTrip.setArrival(mArrivalTimeText);
                        mTrip.setDeparture(mDepartureTimeText);
                        mTrip.setDestination(mItiTo);
                        mTrip.setOrigin(mItiFrom);
                        mTrip.setDescription(mDescriptionEdit.getText().toString().trim());
                        if (mMaxWeight != null && !mMaxWeight.isEmpty())
                            mTrip.setMaxweight(Integer.valueOf(mMaxWeight));
                        if (mMaxLength != null && !mMaxLength.isEmpty())
                            mTrip.setMaxlength(Integer.valueOf(mMaxLength));
                        if (mMaxWidth != null && !mMaxWidth.isEmpty())
                            mTrip.setMaxwidth(Integer.valueOf(mMaxWidth));
                        if (mMaxHeight != null && !mMaxHeight.isEmpty())
                            mTrip.setMaxheight(Integer.valueOf(mMaxHeight));
                        mTrip.setCategoryNames(mCategoryNames);
                        mTrip.setLanguageCode(getResources().getString(R.string.language_code));

                        List<String> photoPaths = new ArrayList<String>();
                        for (ImageItem image : mSelectedPhotos) {
                            photoPaths.add(image.getImagePath());
                        }
                        mTrip.setPhotoPaths(photoPaths);

                        performRequest(mTrip);
                    }
                    break;
                case R.id.itiCreateBackButton:
                    goBack2MainScreen(null);
                    break;
                case R.id.itiCreateSelectImageBtn:
                    mImagePopupMenu.setAnimationStyle(R.style.releasepopwindow_anim_style);
                    mImagePopupMenu.showAtLocation(mParentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnClickListener popupMenuItemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.selectImageCancel:
                    mImagePopupMenu.dismiss();
                    break;
                case R.id.selectImageFromCamera:
                    takePhoto();
                    mImagePopupMenu.dismiss();
                    break;
                case R.id.selectImageFromAlbum:
                    Intent intent = new Intent(ScheduleEditActivity.this, AlbumActivity.class);
                    intent.putExtra(GoGouIntents.IMAGE_SELECTED_NUM, mSelectedPhotos.size());
                    startActivityForResult(intent, GoGouIntents.SELECT_IMAGE_REQUEST);
                    mImagePopupMenu.dismiss();
                    break;
            }
        }
    };

    private static final String STATE_DEPART_CITY = "STATE_DEPART_CITY";
    private static final String STATE_DEPART_TIME = "STATE_DEPART_TIME";
    private static final String STATE_ARRIVAL_CITY = "STATE_ARRIVAL_CITY";
    private static final String STATE_ARRIVAL_TIME = "STATE_ARRIVAL_TIME";
    private static final String STATE_MAX_WEIGHT = "STATE_MAX_WEIGHT";
    private static final String STATE_MAX_LENGTH = "STATE_MAX_LENGTH";
    private static final String STATE_MAX_WIDTH = "STATE_MAX_WIDTH";
    private static final String STATE_MAX_HEIGHT = "STATE_MAX_HEIGHT";
    private static final String STATE_CATEGORY= "STATE_CATEGORY";
    private static final String STATE_DESCRIPTION = "STATE_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentView = getLayoutInflater().inflate(R.layout.schedule_edit_layout, null);
        setContentView(mParentView);

        // read preferences to see if login/registration is already done
        mUserName = GogouApplication.getInstance().getUserName();
        mEncodedPassword = GogouApplication.getInstance().getPassword();

        initUIs();
        initCategorySpinner();

        if (savedInstanceState != null)
        {
            populateSavedStates(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState)
    {
        outState.putString(STATE_DEPART_CITY, mItiFromEdit.getText().toString().trim());
        outState.putString(STATE_DEPART_TIME, mDepartureTimeEdit.getText().toString().trim());
        outState.putString(STATE_ARRIVAL_CITY, mItiToEdit.getText().toString().trim());
        outState.putString(STATE_ARRIVAL_TIME, mArrivalTimeEdit.getText().toString().trim());
        outState.putString(STATE_MAX_WEIGHT, mMaxWeightEdit.getText().toString().trim());
        outState.putString(STATE_MAX_LENGTH, mMaxLengthEdit.getText().toString().trim());
        outState.putString(STATE_MAX_WIDTH, mMaxWidthEdit.getText().toString().trim());
        outState.putString(STATE_MAX_HEIGHT, mMaxHeightEdit.getText().toString().trim());
        outState.putInt(STATE_CATEGORY, mPriorityCategorySpinner.getSelectedItemPosition());
        outState.putString(STATE_DESCRIPTION, mDescriptionEdit.getText().toString().trim());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState (Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        populateSavedStates(savedInstanceState);
    }

    private void populateSavedStates(Bundle savedInstanceState)
    {
        mItiFromEdit.setText(savedInstanceState.getString(STATE_DEPART_CITY));
        mDepartureTimeEdit.setText(savedInstanceState.getString(STATE_DEPART_TIME));
        mItiToEdit.setText(savedInstanceState.getString(STATE_ARRIVAL_CITY));
        mArrivalTimeEdit.setText(savedInstanceState.getString(STATE_ARRIVAL_TIME));
        mMaxWeightEdit.setText(savedInstanceState.getString(STATE_MAX_WEIGHT));
        mMaxLengthEdit.setText(savedInstanceState.getString(STATE_MAX_LENGTH));
        mMaxWidthEdit.setText(savedInstanceState.getString(STATE_MAX_WIDTH));
        mMaxHeightEdit.setText(savedInstanceState.getString(STATE_MAX_HEIGHT));
        mPriorityCategorySpinner.setSelection(savedInstanceState.getInt(STATE_CATEGORY));
        mDescriptionEdit.setText(savedInstanceState.getString(STATE_DESCRIPTION));
    }

    private void initUIs() {

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mConfirmBtn.setOnClickListener(itemsOnClick);
        mBackBtn.setOnClickListener(itemsOnClick);
        mDepartureTimeEdit.setOnClickListener(itemsOnClick);
        mArrivalTimeEdit.setOnClickListener(itemsOnClick);
        mSelectImageBtn.setOnClickListener(itemsOnClick);

        int[] menuIds = {R.id.selectImageFromCamera, R.id.selectImageFromAlbum, R.id.selectImageCancel};
        mImagePopupMenu = new BottomPopupWindow(ScheduleEditActivity.this,
                                                R.layout.image_popupwindows,
                                                R.id.imagePopupMenu,
                                                menuIds,
                                                popupMenuItemOnClick);

        mPhotoGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = new ImageGridForPublishAdapter(this, mSelectedPhotos);
        mPhotoGridView.setAdapter(mAdapter);
        mPhotoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == mSelectedPhotos.size()) {

                    mImagePopupMenu.setAnimationStyle(R.style.releasepopwindow_anim_style);
                    mImagePopupMenu.showAtLocation(mParentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                } else {
                    Intent intent = new Intent(ScheduleEditActivity.this, GalleryActivity.class);
                    intent.putExtra(GoGouIntents.IMAGE_SELECTED_LIST, mSelectedPhotos);
                    startActivity(intent);
                }
            }
        });
    }

    private void initCategorySpinner()
    {
        CategoryList categories = new CategoryList();

        Category head = new Category();
        head.setName(getString(R.string.fillWithPriorityThing));
        categories.add(head);
        categories.addAll(info.gogou.gogou.model.CacheManager.getCachedCategories(this));

        mPriorityCategorySpinner.setAdapter(new CategorySpinnerAdapter(this, R.layout.gogou_spinner_row, categories));

        mPriorityCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    mCategoryNames.add(category.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void performRequest(Trip trip) {
        mProgressSpinner.show();
        TripRequest.CreateTripRequest request = new TripRequest.CreateTripRequest(mUserName, mEncodedPassword, trip);
        GogouApplication.getInstance().getSpiceManager().execute(request, new CreateTripRequestListener());
    }

    private class CreateTripRequestListener implements
            RequestListener<GenericResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_create_iti_failed, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
            mProgressSpinner.dismiss();
        }

        @Override
        public void onRequestSuccess(GenericResponse response) {

            Toast.makeText(ScheduleEditActivity.this, R.string.notif_create_iti_succeed, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Create itinerary: " + response.getMessage());
            mProgressSpinner.dismiss();

            goBack2MainScreen(mTrip);
        }
    }

    // 表单校验
    protected boolean matchScheduleEditMsg() {
        mItiFrom = mItiFromEdit.getText().toString().trim();
        mItiTo = mItiToEdit.getText().toString().trim();
        mMaxWeight = mMaxWeightEdit.getText().toString().trim();
        mMaxLength = mMaxLengthEdit.getText().toString().trim();
        mMaxWidth = mMaxWidthEdit.getText().toString().trim();
        mMaxHeight = mMaxHeightEdit.getText().toString().trim();

        if (mItiFrom.length() == 0) {
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_departure_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mItiTo.length() == 0) {
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_destination_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mDepartureTimeText.length() == 0){
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_startTime_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(mArrivalTimeText.length() == 0){
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_arriveTime_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mArrivalTime.before(mDepartureTime)) {
            Toast.makeText(ScheduleEditActivity.this, R.string.notif_startTime_before_arriveTime, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void goBack2MainScreen(Trip trip) {
        Intent intent = new Intent(ScheduleEditActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        if (null != trip)
            intent.putExtra(GoGouIntents.TRIP, trip);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {

        updateGridViewVisibility();
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // 点击拍照
    private void takePhoto() {
        //执行拍照前，应该先判断SD卡是否存在
        String sdcardState = Environment.getExternalStorageState();
        if (sdcardState.equals(Environment.MEDIA_MOUNTED))//如果有媒体安装的环境
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//传拍的照
            /***
             * 需要说明一下，以下操作使用+拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();//contentProvider存储？
            mPhotoURI = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
            startActivityForResult(intent, GoGouIntents.TAKE_PHOTO_REQUEST);
        }else{
            Toast.makeText(this, R.string.notif_sdcard_invalid, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case GoGouIntents.SELECT_IMAGE_REQUEST:
                if (mSelectedPhotos.size() < 9 && resultCode == RESULT_OK) {
                    List<ImageItem> selectedImages = (List<ImageItem>) data.getSerializableExtra(GoGouIntents.IMAGE_SELECTED_LIST);
                    mSelectedPhotos.addAll(selectedImages);
                    Log.d(TAG, "Selected " + mSelectedPhotos.size() + " images");
                }
                break;
            case GoGouIntents.TAKE_PHOTO_REQUEST:
                if (mSelectedPhotos.size() < 9 && resultCode == RESULT_OK) {
                    Log.d(TAG, "Taking photo returns OK.");
                    AsyncTask scaleImageTask = new AsyncTask<Object, Integer, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Object[] objects) {
                            String[] pojo = {MediaStore.Images.Media.DATA};
                            String imagePath = null;
                            Cursor cursor = getContentResolver().query(mPhotoURI, pojo, null, null,null);//Uri 和 图片数据   得到  picPath
                            if(cursor != null ){
                                int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                                cursor.moveToFirst();
                                imagePath = cursor.getString(columnIndex);
                                cursor.close();
                            }

                            if (imagePath != null){

                                ImageItem takePhoto = new ImageItem();
                                takePhoto.setImagePath(imagePath);
                                mSelectedPhotos.add(takePhoto);

                            }else{
                                return false;
                            }
                            return true;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            if (!result) {
                                Toast.makeText(ScheduleEditActivity.this, R.string.notif_take_photo_invalid, Toast.LENGTH_LONG).show();
                            } else {
                                Log.d(TAG, "Scaling image is done notify adapter.");
                                updateGridViewVisibility();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    };
                    scaleImageTask.execute();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void updateGridViewVisibility() {
        if (mSelectedPhotos.size() == 0) {
            mSelectImageBtn.setVisibility(View.VISIBLE);
            mPhotoGridView.setVisibility(View.GONE);
            Log.d(TAG, "Update image grid view invisible");
        } else {
            mSelectImageBtn.setVisibility(View.GONE);
            mPhotoGridView.setVisibility(View.VISIBLE);
            Log.d(TAG, "Update image grid view visible");
        }
    }
}
