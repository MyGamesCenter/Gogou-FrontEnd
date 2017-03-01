package info.gogou.gogou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.ui.photo.AsyncImageView;
import info.gogou.gogou.utils.photo.ImageItem;


/**
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 */
public class AlbumGridViewAdapter extends BaseAdapter {

	private static final String TAG = "AlbumGridViewAdapter";

	private Context mContext;
	private List<ImageItem> mImageItems;

	public AlbumGridViewAdapter(Context c, List<ImageItem> imageItems, List<ImageItem> selectedImageItems) {
		mContext = c;
		this.mImageItems = imageItems;
	}

	public int getCount() {
		return mImageItems.size();
	}

	public Object getItem(int position) {
		return mImageItems.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}
	
	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public AsyncImageView imageView;
		public Button imageCheckBox;
        public boolean mIsChecked = false;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.select_image_from_album_imageview, parent, false);
			viewHolder.imageView = (AsyncImageView) convertView.findViewById(R.id.albumImageView);
            // set imageview square size which is accroding to screen size
			viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(
					(int)mContext.getResources().getDimension(R.dimen.gridview_image_width),
					(int)mContext.getResources().getDimension(R.dimen.gridview_image_height)));
			viewHolder.imageCheckBox = (Button) convertView.findViewById(R.id.imageSelectedBtn);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

        final ViewHolder tmpViewHolder = viewHolder;
        final ImageItem item = mImageItems.get(position);
        if (item.isSelected()) {
            viewHolder.imageCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageCheckBox.setVisibility(View.GONE);
        }
        viewHolder.imageView.setTag(item.getImagePath());
		viewHolder.imageView.loadImageFromPath(item.getImagePath());

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mImageItems != null && mOnItemClickListener != null && position < mImageItems.size()) {
                    tmpViewHolder.mIsChecked = !tmpViewHolder.mIsChecked;
                    Log.d(TAG, "Image poistion " + position + " is checked: " + tmpViewHolder.mIsChecked);
                    mOnItemClickListener.onItemClick(position, tmpViewHolder.mIsChecked, tmpViewHolder.imageCheckBox);
                }
            }
        });

		viewHolder.imageCheckBox.setTag(position);
		/*if (mSelectedImageItems.contains(mImageItems.get(position))) {
            viewHolder.mIsChecked = true;
			viewHolder.imageCheckBox.setVisibility(View.VISIBLE);
		} else {
            viewHolder.mIsChecked = false;
			viewHolder.imageCheckBox.setVisibility(View.GONE);
		}*/
		return convertView;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		void onItemClick(int position, boolean isChecked, Button checkBox);
	}

}
