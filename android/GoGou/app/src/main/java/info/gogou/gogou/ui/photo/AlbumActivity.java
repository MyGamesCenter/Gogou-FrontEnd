package info.gogou.gogou.ui.photo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.adapter.AlbumGridViewAdapter;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.utils.photo.AlbumHelper;
import info.gogou.gogou.utils.photo.ImageItem;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;


/**
 * Display all images in personal album
 */
public class AlbumActivity extends RoboActionBarActivity {

    private static final String TAG = "AlbumActivity";

    @InjectView(R.id.albumGridView)
    private GridView mGridView;
    @InjectView(R.id.noImageText)
    private TextView mNoImageText;
    @InjectView(R.id.imagePreviewButton)
    private Button mImagePreviewBtn;
    @InjectView(R.id.albumConfirmButton)
    private Button mConfirmBtn;
    @InjectView(R.id.albumBackButton)
    private Button mBackBtn;

    // Adapter for image grid view
    private AlbumGridViewAdapter mGridImageAdapter;
    private AlbumHelper mAlbumHelper;
    private List<ImageItem> mImageItems;
    private ArrayList<ImageItem> mSelectedImageItems;
    private int mMaxSelectedImageNum = GoGouConstants.SELECTED_IMAGE_MAX_NUM;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_from_album);

        Intent intent = this.getIntent();
        int selectedImageNum = intent.getIntExtra(GoGouIntents.IMAGE_SELECTED_NUM, 0);
        mMaxSelectedImageNum -= selectedImageNum;

        init();
        //这个函数主要用来控制预览和完成按钮的状态
        modifyButtonStates();
    }

    private void init() {

        mAlbumHelper = AlbumHelper.getHelper();
        mAlbumHelper.init(getApplicationContext());

        // Retrieves all the images in a list
        mImageItems = mAlbumHelper.getImageList(false);
        mSelectedImageItems = new ArrayList<ImageItem>();
        mBackBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        mImagePreviewBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedImageItems.size() > 0) {
                    Intent intent = new Intent(AlbumActivity.this, GalleryActivity.class);
                    intent.putExtra(GoGouIntents.IMAGE_SELECTED_LIST, mSelectedImageItems);
                    intent.putExtra(GoGouIntents.IMAGE_MAXNUM_AVAILABLE, mMaxSelectedImageNum);
                    startActivityForResult(intent, GoGouIntents.PREVIEW_IMAGES_REQUEST);
                }
            }
        });

        mConfirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(GoGouIntents.IMAGE_SELECTED_LIST, mSelectedImageItems);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mGridImageAdapter = new AlbumGridViewAdapter(this, mImageItems, mSelectedImageItems);
        mGridView.setAdapter(mGridImageAdapter);
        mGridView.setEmptyView(mNoImageText);

        mGridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, boolean isChecked, Button checkBox) {
                if (mSelectedImageItems.size() >= mMaxSelectedImageNum) {
                    checkBox.setVisibility(View.GONE);
                    if (!removeOneData(mImageItems.get(position))) {
                        Toast.makeText(AlbumActivity.this,
                                getResources().getString(R.string.only_choose_num1) +
                                        mMaxSelectedImageNum +
                                        getResources().getString(R.string.only_choose_num2),
                                Toast.LENGTH_SHORT).show();
                    }
                    modifyButtonStates();
                    return;
                }
                if (isChecked) {
                    checkBox.setVisibility(View.VISIBLE);
                    ImageItem item = mImageItems.get(position);
                    item.setSelected(true);
                    mSelectedImageItems.add(item);
                } else {
                    removeOneData(mImageItems.get(position));
                    checkBox.setVisibility(View.GONE);
                }
                modifyButtonStates();
            }
        });
    }


    private boolean removeOneData(ImageItem imageItem) {
        if (mSelectedImageItems.contains(imageItem)) {
            imageItem.setSelected(false);
            mSelectedImageItems.remove(imageItem);
            return true;
        }
        return false;
    }

    private void setConfirmButtonText() {
        mConfirmBtn.setText(getResources().getString(R.string.finish) + "(" + mSelectedImageItems.size() + "/" + mMaxSelectedImageNum + ")");
    }

    private void modifyButtonStates() {
        if (mSelectedImageItems.size() > 0) {
            mImagePreviewBtn.setEnabled(true);
            mConfirmBtn.setEnabled(true);
            mImagePreviewBtn.setClickable(true);
            mConfirmBtn.setClickable(true);
            mConfirmBtn.setTextColor(Color.WHITE);
            mImagePreviewBtn.setTextColor(Color.WHITE);
        } else {
            mImagePreviewBtn.setEnabled(false);
            mImagePreviewBtn.setClickable(false);
            mConfirmBtn.setEnabled(false);
            mConfirmBtn.setClickable(false);
            mConfirmBtn.setTextColor(getResources().getColor(R.color.gray));
            mImagePreviewBtn.setTextColor(getResources().getColor(R.color.gray));
        }
        setConfirmButtonText();
    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case GoGouIntents.PREVIEW_IMAGES_REQUEST:
                if (resultCode == RESULT_OK) {
                    List<Integer> unSelectedIndex = (List<Integer>) data.getSerializableExtra(
                            GoGouIntents.IMAGE_UNSELECTED_LIST);
                    List<ImageItem> unSelectedImages = new ArrayList<ImageItem>();
                    for (int i : unSelectedIndex) {
                        unSelectedImages.add(mSelectedImageItems.get(i));
                    }
                    // remove them from selected list
                    for (ImageItem item : unSelectedImages) {
                        removeOneData(item);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {

        modifyButtonStates();
        mGridImageAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
