package info.gogou.gogou.ui.photo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import info.gogou.gogou.R;
import info.gogou.gogou.model.CacheManager;
import info.gogou.gogou.utils.photo.PhotoUtils;

/**
 * Created by Letian_Xu on 6/8/2016.
 */
public class AsyncImageView extends ImageView {

    private Bitmap mPlaceHolderBitmap;
    private int mImageWidth;
    private int mImageHeight;

    public AsyncImageView(Context context) {
        super(context);
        init();
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        mImageWidth = (int)getResources().getDimension(R.dimen.gridview_image_width);
        mImageHeight = (int)getResources().getDimension(R.dimen.gridview_image_height);
    }

    public void loadImageFromPath(String imageFilePath) {
        final Bitmap bitmap = CacheManager.getBitmapFromMemCache(imageFilePath);
        if (bitmap != null) {
            setImageBitmap(bitmap);
            setScaleType(ScaleType.CENTER_CROP);
        } else {
            if (cancelPotentialWork(imageFilePath)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(this);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(getResources(), mPlaceHolderBitmap, task);
                setImageDrawable(asyncDrawable);
                task.execute(imageFilePath);
            }
        }
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private boolean cancelPotentialWork(String imageFilePath) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask();

        if (bitmapWorkerTask != null) {
            final String preFielPath = bitmapWorkerTask.imageFilePath;
            if (preFielPath != null && !preFielPath.equals(imageFilePath)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private BitmapWorkerTask getBitmapWorkerTask() {
        final Drawable drawable = getDrawable();
        if (drawable instanceof AsyncDrawable) {
            final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
            return asyncDrawable.getBitmapWorkerTask();
        }
        return null;
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        String imageFilePath = null;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imageFilePath = params[0];
            final Bitmap bitmap = PhotoUtils.resizeImage(imageFilePath, mImageWidth, mImageHeight);
            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask();
                if (this == bitmapWorkerTask && imageView != null) {
                    CacheManager.addBitmapToMemoryCache(imageFilePath, bitmap);
                    imageView.setImageBitmap(bitmap);
                    imageView.setScaleType(ScaleType.CENTER_CROP);
                }
            }
        }
    }
}
