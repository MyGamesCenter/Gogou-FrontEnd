package info.gogou.gogou.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.database.CategoryContract.CategoryEntry;
import info.gogou.gogou.database.GoGouSQLiteHelper;
import info.gogou.gogou.model.Category;

/**
 * Created by lxu on 2016-01-16.
 */
public class CategoryDao {

    private static final String TAG = "CategoryDao";

    // Database fields
    private SQLiteDatabase mDatabase;
    private GoGouSQLiteHelper mDbHelper;

    private String[] allColumns = {
            CategoryEntry._ID,
            CategoryEntry.COLUMN_NAME,
            CategoryEntry.COLUMN_FILENAME,
            CategoryEntry.COLUMN_DISPLAY_NAME,
            CategoryEntry.COLUMN_LANGUAGE_CODE,
    };

    public CategoryDao(Context context)
    {
        mDbHelper = new GoGouSQLiteHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void saveCategory(Category category) {

        if (category == null)
            return;

        ContentValues values = new ContentValues();
        values.put(CategoryEntry._ID, category.getId());
        values.put(CategoryEntry.COLUMN_NAME, category.getName());
        values.put(CategoryEntry.COLUMN_FILENAME, category.getImagePath());
        values.put(CategoryEntry.COLUMN_DISPLAY_NAME, category.getDisplayName());
        values.put(CategoryEntry.COLUMN_LANGUAGE_CODE, category.getLanguageCode());

        mDatabase.insert(CategoryEntry.TABLE_NAME, null, values);
        return;
    }

    public List<Category> listAllCategories(String languageCode) {
        List<Category> categories = new ArrayList<Category>();

        String whereClause = CategoryEntry.COLUMN_LANGUAGE_CODE + "=?";
        String[] whereArgs = new String[] { languageCode };

        Cursor cursor = mDatabase.query(CategoryEntry.TABLE_NAME, allColumns, whereClause, whereArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category category = cursorToComment(cursor);
            categories.add(category);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return categories;
    }

    public int deleteCategories(String languageCode)
    {
        String whereClause = CategoryEntry.COLUMN_LANGUAGE_CODE + "=?";
        String[] whereArgs = new String[] { languageCode };

        return mDatabase.delete(CategoryEntry.TABLE_NAME, whereClause, whereArgs);
    }

    private Category cursorToComment(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getLong(0));
        category.setName(cursor.getString(1));
        category.setImagePath(cursor.getString(2));
        category.setDisplayName(cursor.getString(3));
        category.setLanguageCode(cursor.getString(4));

        return category;
    }


}
