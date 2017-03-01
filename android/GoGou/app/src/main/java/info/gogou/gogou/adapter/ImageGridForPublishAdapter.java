package info.gogou.gogou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.utils.photo.ImageItem;
import info.gogou.gogou.utils.photo.PhotoUtils;

/**
 * Created by lxu on 2016-04-18.
 */
public class ImageGridForPublishAdapter extends BaseAdapter {

    private Context mContext;
    private int mSelectedPosition = -1;
    private final List<ImageItem> mSelectedImages;
    private LayoutInflater mLayoutInflater;

    public ImageGridForPublishAdapter(Context context, List<ImageItem> selectedImages) {
        mContext = context;
        this.mSelectedImages = selectedImages;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if(mSelectedImages.size() == GoGouConstants.SELECTED_IMAGE_MAX_NUM){  //最多选9张图片
            return GoGouConstants.SELECTED_IMAGE_MAX_NUM;
        }
        return (mSelectedImages.size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setmSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public int getmSelectedPosition() {
        return mSelectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_published_grida, parent, false);
            image = (ImageView) convertView.findViewById(R.id.choosenImageItem);
            convertView.setTag(image);
        } else {
            image = (ImageView) convertView.getTag();
        }

        if (position == mSelectedImages.size()) {
            image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.add_more_image));

            if (position == GoGouConstants.SELECTED_IMAGE_MAX_NUM) {  //只能选9张图片
                image.setVisibility(View.GONE);
            }
        } else {
            String imageFilePath = mSelectedImages.get(position).getImagePath();
            Bitmap bitmap = CacheManager.getBitmapFromMemCache(imageFilePath);
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            } else {
                image.setImageBitmap(PhotoUtils.resizeImage(imageFilePath,
                                                            PhotoUtils.DEFAULT_GOGOU_GRID_IMAGE_WIDTH,
                                                            PhotoUtils.DEFAULT_GOGOU_GRID_IMAGE_HEIGHT));
            }
        }

        return convertView;
    }
}
