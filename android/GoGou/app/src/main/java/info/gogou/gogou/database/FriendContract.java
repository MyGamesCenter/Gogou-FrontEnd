package info.gogou.gogou.database;

import android.provider.BaseColumns;

/**
 * Created by Letian_Xu on 1/13/2016.
 */
public class FriendContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private FriendContract() {}

        /* Inner class that defines the table contents */

    public static abstract class FriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "friend";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_ISPURCHASER = "ispurchaser";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_LAST_LOGIN = "last_login";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_AVATAR = "avatar";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FriendEntry.TABLE_NAME + " (" +
                    FriendEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FriendEntry.COLUMN_EMAIL + " VARCHAR(255) NOT NULL, " +
                    FriendEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                    FriendEntry.COLUMN_ISPURCHASER + " BOOLEAN, " +
                    FriendEntry.COLUMN_AGE + " INT, " +
                    FriendEntry.COLUMN_GENDER + " TEXT, " +
                    FriendEntry.COLUMN_LAST_LOGIN + " DATETIME, " +
                    FriendEntry.COLUMN_AVATAR + " VARCHAR(255)" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FriendEntry.TABLE_NAME;
}
