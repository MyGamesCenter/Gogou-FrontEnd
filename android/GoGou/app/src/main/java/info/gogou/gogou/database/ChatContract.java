package info.gogou.gogou.database;

import android.provider.BaseColumns;

/**
 * Created by Letian_Xu on 1/13/2016.
 */
public final class ChatContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ChatContract() {}

        /* Inner class that defines the table contents */

    public static abstract class ChatEntry implements BaseColumns {
        public static final String TABLE_NAME = "chat_history";
        public static final String COLUMN_FROM = "chat_from";
        public static final String COLUMN_TO = "chat_to";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_POST_DATE = "post_date";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_LAYOUTID = "layout_id";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChatEntry.TABLE_NAME + " (" +
                    ChatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ChatEntry.COLUMN_FROM + " TEXT, " +
                    ChatEntry.COLUMN_TO + " TEXT, " +
                    ChatEntry.COLUMN_TYPE + " VARCHAR(8), " +
                    ChatEntry.COLUMN_POST_DATE + " DATETIME, " +
                    ChatEntry.COLUMN_CONTENT + " TEXT, " +
                    ChatEntry.COLUMN_LAYOUTID + " INTEGER NOT NULL" +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChatEntry.TABLE_NAME;
}
