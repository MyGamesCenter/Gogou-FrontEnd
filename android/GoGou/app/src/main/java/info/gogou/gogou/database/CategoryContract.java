package info.gogou.gogou.database;

import android.provider.BaseColumns;

/**
 * Created by lxu on 2016-01-16.
 */
public class CategoryContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private CategoryContract() {}

        /* Inner class that defines the table contents */

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "CATEGORY";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_FILENAME = "FILENAME";
        public static final String COLUMN_DISPLAY_NAME = "DISPLAY_NAME";
        public static final String COLUMN_LANGUAGE_CODE = "LANGUAGE_CODE";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    CategoryEntry.COLUMN_FILENAME + " VARCHAR(255), " +
                    CategoryEntry.COLUMN_DISPLAY_NAME + " VARCHAR(255) NOT NULL, " +
                    CategoryEntry.COLUMN_LANGUAGE_CODE + " VARCHAR(255) NOT NULL" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;
}
