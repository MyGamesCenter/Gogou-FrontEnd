package info.gogou.gogou.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.database.FriendContract.FriendEntry;
import info.gogou.gogou.database.GoGouSQLiteHelper;
import info.gogou.gogou.model.Friend;
import info.gogou.gogou.model.Subscriber;

/**
 * Created by lxu on 2016-01-16.
 */
public class FriendDao {

    private static final String TAG = "FriendDao";

    // Database fields
    private SQLiteDatabase mDatabase;
    private GoGouSQLiteHelper mDbHelper;

    private String[] allColumns = {
            FriendEntry._ID,
            FriendEntry.COLUMN_EMAIL,
            FriendEntry.COLUMN_USERNAME,
            FriendEntry.COLUMN_ISPURCHASER,
            FriendEntry.COLUMN_AGE,
            FriendEntry.COLUMN_GENDER,
            FriendEntry.COLUMN_LAST_LOGIN,
            FriendEntry.COLUMN_AVATAR
    };

    public FriendDao(Context context)
    {
        mDbHelper = new GoGouSQLiteHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Friend addFriend(Subscriber subscriber, String date) {

        if (subscriber == null)
            return null;

        ContentValues values = new ContentValues();
        values.put(FriendEntry._ID, subscriber.getId());
        values.put(FriendEntry.COLUMN_EMAIL, subscriber.getEmailAddress());
        values.put(FriendEntry.COLUMN_USERNAME, subscriber.getUserName());
        values.put(FriendEntry.COLUMN_ISPURCHASER, subscriber.getIsPurchaser());
        values.put(FriendEntry.COLUMN_AGE, subscriber.getAge());
        values.put(FriendEntry.COLUMN_GENDER, subscriber.getGender());
        values.put(FriendEntry.COLUMN_LAST_LOGIN, date);
        values.put(FriendEntry.COLUMN_AVATAR, "");

        long insertId = mDatabase.insert(FriendEntry.TABLE_NAME, null, values);
        Cursor cursor = mDatabase.query(FriendEntry.TABLE_NAME, allColumns, FriendEntry._ID + " = " + insertId,
                null, null, null, null);
        cursor.moveToFirst();
        Friend newFriend = cursorToComment(cursor);
        cursor.close();
        return newFriend;
    }

    public void deleteFriend(Friend friend) {
        long id = friend.getId();
        Log.d(TAG, "Deleted friend with id: " + id);
        mDatabase.delete(FriendEntry.TABLE_NAME, FriendEntry._ID + " = " + id, null);
    }

    public boolean isFriend(Subscriber subscriber)
    {
        Cursor cursor = mDatabase.query(FriendEntry.TABLE_NAME, allColumns,
                        FriendEntry.COLUMN_USERNAME + " = '" + subscriber.getUserName() + "' AND " +
                        FriendEntry.COLUMN_EMAIL + " = '" + subscriber.getEmailAddress() + "'",
                        null, null, null, null);
        boolean isExsiting = cursor.getCount() > 0;
        cursor.close();
        return isExsiting;
    }

    public List<Friend> listAllFriends() {
        List<Friend> friends = new ArrayList<Friend>();

        Cursor cursor = mDatabase.query(FriendEntry.TABLE_NAME, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Friend friend = cursorToComment(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return friends;
    }

    private Friend cursorToComment(Cursor cursor) {
        Friend friend = new Friend();
        friend.setId(cursor.getLong(0));
        friend.setEmailAddress(cursor.getString(1));
        friend.setUserName(cursor.getString(2));
        if (cursor.getInt(3) == 1)
            friend.setIsPurchaser();
        friend.setAge(cursor.getInt(4));
        friend.setGender(cursor.getString(5));
        friend.setLastLogin(cursor.getString(6));
        friend.setAvatar(cursor.getString(7));
        return friend;
    }


}
