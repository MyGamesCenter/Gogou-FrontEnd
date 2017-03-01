package info.gogou.gogou.utils.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.gogou.gogou.constants.GoGouConstants;

/**
 * This is image utils supplementing cn.trinea.android.common.util.ImageUtils
 */
public class PhotoUtils {

	private static final String TAG = "PhotoUtils";

    public static final int DEFAULT_GOGOU_GRID_IMAGE_WIDTH = 80;
    public static final int DEFAULT_GOGOU_GRID_IMAGE_HEIGHT = 80;
	public static final int DEFAULT_GOGOU_IMAGE_FILE_SIZE = 300 * 1024;
    private static final String IMAGE_FILE_EXT = ".jpg";

	/**
	 * Creates image file with file name format: JPEG_yyyyMMdd_HHmmss_ and returns
	 * file's Uri.
	 *
	 * @return Uri
	 */
	public static Uri createImageFile() {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File image = null;
		try {
			image = File.createTempFile(imageFileName,  /* prefix */
										IMAGE_FILE_EXT,         /* suffix */
										new File(GoGouConstants.GOGOU_IMAGE_DIR)      /* directory */
			);
		} catch (IOException e) {
			Log.e(TAG, "Creates image file error: " + e.getLocalizedMessage());
		}

		// Save a file: path for use with ACTION_VIEW intents
		return Uri.fromFile(image);
	}

	/**
	 * Saves Bitmap image with file name and returns absolute path.
	 * @param bm
	 * @param imageName
     * @return
     */
	public static String saveBitmap(Bitmap bm, String imageName) {
		if (bm == null) return null;
		try {
			File f = new File(GoGouConstants.GOGOU_IMAGE_DIR, imageName + IMAGE_FILE_EXT);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Can NOT find file: " + imageName + ". " + e.getLocalizedMessage());
		} catch (IOException e) {
			Log.e(TAG, "Saves Bitmap image error: " + e.getLocalizedMessage());
		}

		return GoGouConstants.GOGOU_IMAGE_DIR + imageName + IMAGE_FILE_EXT;

	}

	public static Bitmap getThumbnailFromPath(String imageFilePath) {
        if (!isImagePathValid(imageFilePath)) return null;

        Bitmap bitmap = null;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(imageFilePath)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000)
                        && (options.outHeight >> i <= 1000)) {
                    in = new BufferedInputStream(new FileInputStream(new File(imageFilePath)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        } catch (IOException e) {

        }
        return bitmap;
    }

    /**
     * Resizes the image pointed by file path with the limited image size
     * @param imageFilePath
     * @param imageSize
     * @return
     */
    public static File resizeImage(String imageFilePath, long imageSize) {

        if (!isImagePathValid(imageFilePath)) return null;

        File outputFile = new File(imageFilePath);

        long fileSize = outputFile.length();
        if (fileSize >= imageSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFilePath, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / imageSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, options);
            outputFile = new File(PhotoUtils.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();
            } catch (IOException e) {
                Log.e(TAG, "Saves compressed bitmap to file on error" + e.getLocalizedMessage());
            }
            Log.d(TAG, "Bitmap is compressed with file size: " + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return outputFile;
    }

	/**
	 * Resizes image as byte array with calculated sample size.
	 *
	 * @param imageData
	 * @param targetWidth
	 * @param targetHeight
     * @return
     */
	public static Bitmap resizeImage(byte[] imageData, int targetWidth, int targetHeight) {
		// Use BitmapFactory to decode the image
		BitmapFactory.Options options = new BitmapFactory.Options();
		// inSampleSize is used to sample smaller versions of the image
		options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
		// Decode bitmap with inSampleSize and target dimensions set
		options.inJustDecodeBounds = false;

		Bitmap reducedBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
        if (reducedBitmap != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(reducedBitmap, targetWidth, targetHeight, false);
            return resizedBitmap;
        }
        return null;
	}

	/**
	 * Resizes image as image path with calculated sample size.
	 *
	 * @param imageFilePath
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static Bitmap resizeImage(String imageFilePath, int targetWidth, int targetHeight) {
        if (!isImagePathValid(imageFilePath)) return null;
		// Use BitmapFactory to decode the image
		BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);
		// inSampleSize is used to sample smaller versions of the image
		options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
		// Decode bitmap with inSampleSize and target dimensions set
		options.inJustDecodeBounds = false;

		Bitmap reducedBitmap = BitmapFactory.decodeFile(imageFilePath, options);

        if (reducedBitmap != null) {
            Bitmap rotatedBitmap = rotateImage(reducedBitmap, getExifRotationAngle(imageFilePath));
            int width = rotatedBitmap.getWidth();
            int height = rotatedBitmap.getHeight();
            if (targetWidth != 0 && width > targetWidth) {
                float factor = (float)targetWidth / (float)width;
                targetHeight = (int)(height * factor);
            } else if (targetHeight != 0 && height > targetHeight) {
                float factor = (float)targetHeight / (float)height;
                targetWidth = (int)(width * factor);
            } else {
                targetWidth = DEFAULT_GOGOU_GRID_IMAGE_WIDTH;
                targetHeight = DEFAULT_GOGOU_GRID_IMAGE_HEIGHT;
            }
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(rotatedBitmap, targetWidth, targetHeight, false);
            return resizedBitmap;
        }
        return null;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

    public static Drawable getDrawableFromFilePath(String imageFilePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
        if (bitmap != null) {
            return new BitmapDrawable(bitmap);
        }
        return null;
    }

    public static float getExifRotationAngle(String imageFilePath) {

        try {
            ExifInterface ei = new ExifInterface(imageFilePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                default:
                    return 0;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in creating exif with file: " + imageFilePath);
            return 0;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    private static boolean isImagePathValid(String imageFilePath) {
        if (imageFilePath == null)
            return false;

        File outputFile = new File(imageFilePath);
        if (!outputFile.exists()) return false;

        return true;
    }
}
