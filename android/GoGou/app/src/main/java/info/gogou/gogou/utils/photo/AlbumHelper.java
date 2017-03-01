package info.gogou.gogou.utils.photo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AlbumHelper {

	private static final String TAG = "AlbumHelper";
	private Context mContext;
	private ContentResolver mContentResolver;
	
	private HashMap<String, String> mThumbnailMap = new HashMap<String, String>();
	private HashMap<String, ImageBucket> mBucketMap = new HashMap<String, ImageBucket>();

    private boolean mIsBucketBuilt = false;
	private static AlbumHelper _instance = null;

	private AlbumHelper() {
	}

	public static AlbumHelper getHelper() {
		if (_instance == null) {
            _instance = new AlbumHelper();
		}
		return _instance;
	}

	public void init(Context context) {
		if (this.mContext == null) {
			this.mContext = context;
			mContentResolver = context.getContentResolver();
		}
        cleanup();
	}

    private void cleanup() {

        mThumbnailMap.clear();
        mBucketMap.clear();
        mIsBucketBuilt = false;
    }

	private void getThumbnails() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
		getThumbnailColumnData(cursor);
	}

	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int id;
			int imageId;
			String imagePath;
			int idColumn = cur.getColumnIndex(Thumbnails._ID);
			int imageIdColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				id = cur.getInt(idColumn);
				imageId = cur.getInt(imageIdColumn);
				imagePath = cur.getString(dataColumn);

				// Debug
				// Log.i(TAG, id + " imageId: " + imageId + " path: " + imagePath + "---");
				mThumbnailMap.put("" + imageId, imagePath);
			} while (cur.moveToNext());
		}
	}

	void buildImagesBucketList() {
		long startTime = System.currentTimeMillis();

		getThumbnails();

		String columns[] = new String[] { Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA,
                Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		Cursor cur = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

		if (cur.moveToFirst()) {
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			int totalNum = cur.getCount();

			do {
				String id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);

				Log.i(TAG, id + ", bucketId: " + bucketId + ", picasaId: "
						+ picasaId + " name:" + name + " path:" + path
						+ " title: " + title + " size: " + size + " bucket: "
						+ bucketName + "---");

				ImageBucket bucket = mBucketMap.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					mBucketMap.put(bucketId, bucket);
					bucket.imageList = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				ImageItem imageItem = new ImageItem();
				imageItem.setImageId(id);
				imageItem.setImagePath(path);
				imageItem.setThumbnailPath(mThumbnailMap.get(id));
				bucket.imageList.add(imageItem);

			} while (cur.moveToNext());
		}

        // Debug
		/*Iterator<Entry<String, ImageBucket>> itr = mBucketMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr.next();
			ImageBucket bucket = entry.getValue();
			Log.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", " + bucket.count + " ---------- ");
			for (int i = 0; i < bucket.imageList.size(); ++i) {
				ImageItem image = bucket.imageList.get(i);
				Log.d(TAG, "----- " + image.getImageId() + ", " + image.getImagePath()
						+ ", " + image.getThumbnailPath());
			}
		}*/
		mIsBucketBuilt = true;
		long endTime = System.currentTimeMillis();
		Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
	}


	public List<ImageItem> getImageList(boolean refresh) {
        List<ImageItem> imageList = new ArrayList<ImageItem>();
		if (refresh || (!refresh && !mIsBucketBuilt)) {
			buildImagesBucketList();
		}

		Iterator<Entry<String, ImageBucket>> itr = mBucketMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = (Map.Entry<String, ImageBucket>) itr.next();
			ImageBucket bucket = entry.getValue();
            imageList.addAll(bucket.imageList);
		}
		return imageList;
	}
}
