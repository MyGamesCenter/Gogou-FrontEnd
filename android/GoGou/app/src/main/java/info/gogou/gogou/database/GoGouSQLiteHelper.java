package info.gogou.gogou.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Letian_Xu on 1/13/2016.
 */
public class GoGouSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "GoGouSQLiteHelper";

    // If you change the database schema, you must increment the database version.
    public static final int GOGOU_DB_VERSION = 1;
    public static final String GOGOU_DB_NAME = "gogou.db";

    private final Context _context;

    public GoGouSQLiteHelper(Context context)
    {
        super(context, GOGOU_DB_NAME, null, GOGOU_DB_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategoryContract.SQL_CREATE_ENTRIES);
        db.execSQL(FriendContract.SQL_CREATE_ENTRIES);
        //db.execSQL(ChatContract.SQL_CREATE_ENTRIES);

        // initialize locally
        localInitCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(CategoryContract.SQL_DELETE_ENTRIES);
        //db.execSQL(FriendContract.SQL_DELETE_ENTRIES);
        //db.execSQL(ChatContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    private void localInitCategories(SQLiteDatabase database)
    {
        final String INSERT_HEAD = "INSERT INTO " + CategoryContract.CategoryEntry.TABLE_NAME + " (" +
                CategoryContract.CategoryEntry.COLUMN_NAME + ", " +
                CategoryContract.CategoryEntry.COLUMN_FILENAME + ", " +
                CategoryContract.CategoryEntry.COLUMN_DISPLAY_NAME + ", " +
                CategoryContract.CategoryEntry.COLUMN_LANGUAGE_CODE + ") VALUES ";
        database.execSQL(INSERT_HEAD + "('All','all','All','en-us')");
        database.execSQL(INSERT_HEAD + "('Tobaco & Alcohol','alcohol','Tobaco & Alcohol','en-us')");
        database.execSQL(INSERT_HEAD + "('Mother & Baby Product','babyproduct','Mother & Baby Product','en-us')");
        database.execSQL(INSERT_HEAD + "('Beauty Product','makeup','Beauty Product','en-us')");
        database.execSQL(INSERT_HEAD + "('Health Product','health','Health Product','en-us')");
        database.execSQL(INSERT_HEAD + "('Electronics','electronics','Electronics','en-us')");
        database.execSQL(INSERT_HEAD + "('Fashion & Bag','fashion','Fashion & Bag','en-us')");
        database.execSQL(INSERT_HEAD + "('Jewellery','luxury','Jewellery','en-us')");
        database.execSQL(INSERT_HEAD + "('Duty-free Goods','dutyfree','Duty-free Goods','en-us')");
        database.execSQL(INSERT_HEAD + "('Outlets','outlet','Outlets','en-us')");
        database.execSQL(INSERT_HEAD + "('Toys','toy','Toys','en-us')");
        database.execSQL(INSERT_HEAD + "('Local specialties','special','Local specialties','en-us')");
        database.execSQL(INSERT_HEAD + "('Others','others','Others','en-us')");

        database.execSQL(INSERT_HEAD + "('All','all','全部','zh-s')");
        database.execSQL(INSERT_HEAD + "('Tobaco & Alcohol','alcohol','烟酒','zh-s')");
        database.execSQL(INSERT_HEAD + "('Mother & Baby Product','babyproduct','母婴产品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Beauty Product','makeup','化妆护肤品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Health Product','health','营养保健品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Electronics','electronics','电子数码产品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Fashion & Bag','fashion','潮流服饰箱包','zh-s')");
        database.execSQL(INSERT_HEAD + "('Jewellery','luxury','奢侈品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Duty-free Goods','dutyfree','机场免税店产品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Outlets','outlet','奥特莱斯折扣店产品','zh-s')");
        database.execSQL(INSERT_HEAD + "('Toys','toy','玩具','zh-s')");
        database.execSQL(INSERT_HEAD + "('Local specialties','special','土特产','zh-s')");
        database.execSQL(INSERT_HEAD + "('Others','others','其他','zh-s')");
    }
}
