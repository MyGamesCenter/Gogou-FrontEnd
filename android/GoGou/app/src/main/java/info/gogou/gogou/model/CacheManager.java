package info.gogou.gogou.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.util.List;

import info.gogou.gogou.R;
import info.gogou.gogou.dao.CategoryDao;

/**
 * Created by lxu on 2016-01-03.
 */
public class CacheManager {

    private static final String TAG = "CacheManager";
    private static CategoryList localCategoryList = new CategoryList();

    private static LruCache<String, Bitmap> imageCache;

    public static void saveAsLocalCategory(CategoryList categories)
    {
        localCategoryList.addAll(categories);
    }

    public static CategoryList getCachedCategories(Context context)
    {
        if (localCategoryList.isEmpty())
        {
            CategoryDao categoryDao = new CategoryDao(context);
            categoryDao.open();
            List<Category> categories = categoryDao.listAllCategories(context.getResources().getString(R.string.language_code));
            localCategoryList.addAll(categories);
            categoryDao.close();
        }
        return localCategoryList;
    }

    public static Category findCategoryByName(String name)
    {
        for (Category category : localCategoryList)
        {
            Log.d(TAG, "Searching category by name and the image name of category is: " + category.getImagePath());
            if (category.getName().equals(name))
                return category;
        }
        return null;
    }

    public static void initImageCache() {
        if (imageCache == null) {
            // Get max available VM memory, exceeding this amount will throw an
            // OutOfMemory exception. Stored in kilobytes as LruCache takes an
            // int in its constructor.
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;

            imageCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in kilobytes rather than
                    // number of items.
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public static void clearImageCache() {
        if (imageCache != null) {
            imageCache.evictAll();
        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (imageCache.get(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return imageCache.get(key);
    }
}
