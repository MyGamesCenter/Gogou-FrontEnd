package info.gogou.gogou.ui.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouIntents;
import info.gogou.gogou.ui.zoom.PhotoView;
import info.gogou.gogou.ui.zoom.ViewPagerFixed;
import info.gogou.gogou.utils.photo.ImageItem;
import info.gogou.gogou.utils.photo.PhotoUtils;
import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

/**
 * Browse the image one by one from album
 *
 */
public class GalleryActivity extends RoboActionBarActivity {

	private static final String TAG = "GalleryActivity";

	@InjectView(R.id.imageGallery)
	private ViewPagerFixed mPager;
	@InjectView(R.id.gImageSelectedBtn)
	private Button mSelectedBtn;
    @InjectView(R.id.gImageSelectedGrayBtn)
    private Button mSelectedGrayBtn;
	@InjectView(R.id.galleryConfirmButton)
	private Button mConfirmBtn;
	@InjectView(R.id.galleryBackButton)
	private Button mBackBtn;

    private List<ImageItem> mSelectedImageItems;
    private int mPreviewPosition = 0;

	private ArrayList<View> mListViews = null;
	private MyPageAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_image_gallery);

		Intent intent = this.getIntent();
        mSelectedImageItems = (List<ImageItem>)intent.getSerializableExtra(GoGouIntents.IMAGE_SELECTED_LIST);

        initUIs();
	}

	private void initUIs() {

        OnClickListener selectedBtnListener = new OnClickListener() {
            public void onClick(View v) {
                ImageItem item = mSelectedImageItems.get(mPreviewPosition);
                item.setSelected(!item.isSelected());
                if (!item.isSelected()) {
                    mSelectedGrayBtn.setVisibility(View.VISIBLE);
                    mSelectedBtn.setVisibility(View.GONE);
                }
                else {
                    mSelectedBtn.setVisibility(View.VISIBLE);
                    mSelectedGrayBtn.setVisibility(View.GONE);
                }
            }
        };

        mSelectedBtn.setOnClickListener(selectedBtnListener);
        mSelectedGrayBtn.setOnClickListener(selectedBtnListener);

        mConfirmBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setUnSelectedItemsResult();
                finish();
            }
        });

		mBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

        mPager.addOnPageChangeListener(pageChangeListener);
        for (int i = 0; i < mSelectedImageItems.size(); i++) {
            initListViews(PhotoUtils.getThumbnailFromPath(mSelectedImageItems.get(i).getImagePath()));
        }
        mAdapter = new MyPageAdapter(mListViews);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
	}
	
	private void initListViews(Bitmap bm) {
		if (mListViews == null)
			mListViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mListViews.add(img);
	}

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            mPreviewPosition = arg0;
            if (!mSelectedImageItems.get(mPreviewPosition).isSelected()) {
                mSelectedGrayBtn.setVisibility(View.VISIBLE);
                mSelectedBtn.setVisibility(View.GONE);
            }
            else {
                mSelectedBtn.setVisibility(View.VISIBLE);
                mSelectedGrayBtn.setVisibility(View.GONE);
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setUnSelectedItemsResult() {
        ArrayList<Integer> unSelectedIndex = new ArrayList<Integer>();
        for (int i = 0; i < mSelectedImageItems.size(); i++) {
            ImageItem item = mSelectedImageItems.get(i);
            if (!item.isSelected()) {
                unSelectedIndex.add(i);
            }
        }
        Log.d(TAG, "unselected image number: " + unSelectedIndex.size());

        Intent intent = new Intent();
        intent.putExtra(GoGouIntents.IMAGE_UNSELECTED_LIST, unSelectedIndex);
        setResult(RESULT_OK, intent);
    }
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;
		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
