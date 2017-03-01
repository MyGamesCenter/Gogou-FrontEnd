package info.gogou.gogou.ui;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.CategorySpinnerAdapter;
import info.gogou.gogou.adapter.ImageGridForPublishAdapter;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.model.Category;
import info.gogou.gogou.model.CategoryList;
import info.gogou.gogou.model.Demand;
import info.gogou.gogou.request.DemandRequest;
import info.gogou.gogou.ui.photo.AlbumActivity;
import info.gogou.gogou.ui.photo.GalleryActivity;
import info.gogou.gogou.utils.GenericResponse;
import info.gogou.gogou.utils.photo.ImageItem;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


/**
 * Created by grace on 15-10-23.
 */
public class RequirementEditActivity extends RoboActionBarActivity {

    private static final String TAG = "RequirementEditActivity";

    @InjectView(R.id.demandCreateConfirmButton)
    private Button mConfirmBtn;
    @InjectView(R.id.demandCreateBackButton)
    private Button mBackBtn;
    @InjectView(R.id.demandCreateProductName)
    private EditText mProductNameEdit;
    @InjectView(R.id.demandCategorySpinner)
    private Spinner mCategorySpinner;
    @InjectView(R.id.demandCreateCollectLocation)
    private EditText mCollectLocationEdit;
    @InjectView(R.id.demandExpectedTime)
    private TextView mExpectedTimeEdit;
    @InjectView(R.id.demandCreateBrand)
    private EditText mBrandEdit;
    @InjectView(R.id.demandCreateProductOrigin)
    private EditText mOriginEdit;
    @InjectView(R.id.demandCreateQuantity)
    private EditText mQuantityEdit;
    @InjectView(R.id.demandAcceptedPrice)
    private EditText mAcceptedPriceEdit;
    @InjectView(R.id.demandCreateDescription)
    private EditText mDescriptionEdit;
    @InjectView(R.id.demandCreateSelectImageBtn)
    private ImageView mSelectImageBtn;
    @InjectView(R.id.demandPhotoGridView)
    private GridView mPhotoGridView;

    private ProgressDialog mProgressSpinner;

    private Demand mDemand;
    private String mProductName;
    private String mCategory;
    private String mCollectLocation;
    private String mBrand;
    private String mOrigin;
    private String mQuantity;
    private String mAcceptedPrice;
    private String mDescription;
    private String mExpectTime;

    private String mUserName;
    private String mEncodedPassword;

    private ImageGridForPublishAdapter mAdapter;
    private View mParentView;
    private PopupWindow mImagePopupMenu;

    private List<String> mCategoryList = new ArrayList<String>();
    private ArrayList<ImageItem> mSelectedPhotos = new ArrayList<ImageItem>();

    private Uri mPhotoURI;//自动获取照片uri

    private static final String STATE_PRODUCT_NAME = "STATE_PRODUCT_NAME";
    private static final String STATE_CATEGORY = "STATE_CATEGORY";
    private static final String STATE_COLLECT_LOCATION = "STATE_COLLECT_LOCATION";
    private static final String STATE_EXPECTED_TIME = "STATE_EXPECTED_TIME";
    private static final String STATE_BRAND = "STATE_BRAND";
    private static final String STATE_ORIGIN = "STATE_ORIGIN";
    private static final String STATE_QUANTITY = "STATE_QUANTITY";
    private static final String STATE_ACCEPTED_PRICE = "STATE_ACCEPTED_PRICE";
    private static final String STATE_DESCRIPTION = "STATE_ACCEPTED_PRICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParentView = getLayoutInflater().inflate(R.layout.requirement_edit_layout, null);
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
        outState.putString(STATE_PRODUCT_NAME, mProductNameEdit.getText().toString().trim());
        outState.putInt(STATE_CATEGORY, mCategorySpinner.getSelectedItemPosition());
        outState.putString(STATE_COLLECT_LOCATION, mCollectLocationEdit.getText().toString().trim());
        outState.putString(STATE_EXPECTED_TIME, mExpectedTimeEdit.getText().toString().trim());
        outState.putString(STATE_BRAND, mBrandEdit.getText().toString().trim());
        outState.putString(STATE_ORIGIN, mOriginEdit.getText().toString().trim());
        outState.putString(STATE_QUANTITY, mQuantityEdit.getText().toString().trim());
        outState.putString(STATE_ACCEPTED_PRICE, mAcceptedPriceEdit.getText().toString().trim());
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
        mProductNameEdit.setText(savedInstanceState.getString(STATE_PRODUCT_NAME));
        mCategorySpinner.setSelection(savedInstanceState.getInt(STATE_CATEGORY));
        mCollectLocationEdit.setText(savedInstanceState.getString(STATE_COLLECT_LOCATION));
        mExpectedTimeEdit.setText(savedInstanceState.getString(STATE_EXPECTED_TIME));
        mBrandEdit.setText(savedInstanceState.getString(STATE_BRAND));
        mOriginEdit.setText(savedInstanceState.getString(STATE_ORIGIN));
        mQuantityEdit.setText(savedInstanceState.getString(STATE_QUANTITY));
        mAcceptedPriceEdit.setText(savedInstanceState.getString(STATE_ACCEPTED_PRICE));
        mDescriptionEdit.setText(savedInstanceState.getString(STATE_DESCRIPTION));
    }

    private void initUIs() {

        mProgressSpinner = new ProgressDialog(this, R.style.MyTheme);
        mProgressSpinner.setIndeterminate(false);
        mProgressSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchDemandEditMsg()) {
                    mDemand = new Demand();
                    mDemand.setUserName(mUserName);
                    mDemand.setProductname(mProductName);
                    mDemand.setDestination(mCollectLocation);
                    mDemand.setBrand(mBrand);
                    mDemand.setOrigin(mOrigin);
                    mDemand.setQuantity(Integer.valueOf(mQuantity));
                    if (mAcceptedPrice != null && !mAcceptedPrice.isEmpty())
                        mDemand.setAcceptedPrice(new BigDecimal(Double.valueOf(mAcceptedPrice)));
                    mDemand.setDescription(mDescription);
                    mDemand.setExpectTime(mExpectTime);
                    mDemand.setCategoryName(mCategory);
                    mDemand.setLanguageCode(getResources().getString(R.string.language_code));

                    List<String> photoPaths = new ArrayList<String>();
                    for (ImageItem image : mSelectedPhotos) {
                        photoPaths.add(image.getImagePath());
                    }
                    mDemand.setPhotoPaths(photoPaths);

                    performRequest(mDemand);
                }
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack2MainScreen(null);
            }
        });

        //choose the expect time
        mExpectedTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RequirementEditActivity.this, mExpectTime);
                datePickerDialog.datePickerDialog(mExpectedTimeEdit);
            }
        });

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mImagePopupMenu.setAnimationStyle(R.style.releasepopwindow_anim_style);
                mImagePopupMenu.showAtLocation(mParentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        // select images from album or take photo popup
        int[] menuIds = {R.id.selectImageFromCamera, R.id.selectImageFromAlbum, R.id.selectImageCancel};
        mImagePopupMenu = new BottomPopupWindow(RequirementEditActivity.this,
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
                    Intent intent = new Intent(RequirementEditActivity.this, GalleryActivity.class);
                    intent.putExtra(GoGouIntents.IMAGE_SELECTED_LIST, mSelectedPhotos);
                    startActivity(intent);
                }
            }
        });

    }

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
                    Intent intent = new Intent(RequirementEditActivity.this, AlbumActivity.class);
                    intent.putExtra(GoGouIntents.IMAGE_SELECTED_NUM, mSelectedPhotos.size());
                    startActivityForResult(intent, GoGouIntents.SELECT_IMAGE_REQUEST);
                    mImagePopupMenu.dismiss();
                    break;
            }
        }
    };

    private void initCategorySpinner()
    {
        CategoryList categories = new CategoryList();

        Category head = new Category();
        head.setName(getString(R.string.fillWithKind));
        categories.add(head);
        categories.addAll(info.gogou.gogou.model.CacheManager.getCachedCategories(this));

        mCategorySpinner.setAdapter(new CategorySpinnerAdapter(this, R.layout.gogou_spinner_row, categories));

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Category category = (Category) parent.getItemAtPosition(position);
                    mCategory = category.getName();
                    mCategoryList.add(category.getDisplayName());
                    Log.d(TAG, "Selected category name is: " + mCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void performRequest(Demand demand) {
        mProgressSpinner.show();

        DemandRequest.CreateDemandRequest request = new DemandRequest.CreateDemandRequest(mUserName, mEncodedPassword, demand);
        GogouApplication.getInstance().getSpiceManager().execute(request, new CreateDemandRequestListener());
    }

    private class CreateDemandRequestListener implements
            RequestListener<GenericResponse> {

        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_create_demand_failed, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
            mProgressSpinner.dismiss();
        }

        @Override
        public void onRequestSuccess(GenericResponse response) {

            Toast.makeText(RequirementEditActivity.this, R.string.notif_create_demand_succeed, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Create demand: " + response.getMessage());
            mProgressSpinner.dismiss();

            GoGouIntents.goBack2MainScreen(TAG, RequirementEditActivity.this, true);
        }
    }

    protected boolean matchDemandEditMsg() {

        mCollectLocation = mCollectLocationEdit.getText().toString().trim();
        mProductName = mProductNameEdit.getText().toString().trim();
        mBrand = mBrandEdit.getText().toString().trim();
        mOrigin = mOriginEdit.getText().toString().trim();
        mQuantity = mQuantityEdit.getText().toString().trim();
        mAcceptedPrice = mAcceptedPriceEdit.getText().toString().trim();
        mDescription = mDescriptionEdit.getText().toString().trim();
        mExpectTime = mExpectedTimeEdit.getText().toString().trim();

        if (mProductName.equals("")) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_productname_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mCollectLocation.length() == 0) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_collectionLocation_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mOrigin.length() == 0) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_origin_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mExpectTime.length() == 0) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_expectTime_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mBrand.length() == 0) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_brand_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mQuantity.length() == 0) {
            Toast.makeText(RequirementEditActivity.this, R.string.notif_quantity_notnull, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void goBack2MainScreen(Demand demand)
    {
        Intent intent = new Intent(RequirementEditActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(GoGouIntents.UNIQUE_ID, TAG);
        if (null != demand)
            intent.putExtra(GoGouIntents.DEMAND, demand);
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
        if(sdcardState.equals(Environment.MEDIA_MOUNTED))//如果有媒体安装的环境
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
                    List<ImageItem> selectedImages = (List<ImageItem>)data.getSerializableExtra(GoGouIntents.IMAGE_SELECTED_LIST);
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
                                Toast.makeText(RequirementEditActivity.this, R.string.notif_take_photo_invalid, Toast.LENGTH_LONG).show();
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
