package info.gogou.gogou.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.gogou.gogou.database.ChatContract.ChatEntry;
import info.gogou.gogou.database.GoGouSQLiteHelper;
import info.gogou.gogou.model.Chat;

/**
 * Created by lxu on 2016-01-16.
 */
public class ChatDao {

    private static final String TAG = "ChatDao";

    // Database fields
    private SQLiteDatabase mDatabase;
    private GoGouSQLiteHelper mDbHelper;

    private String[] allColumns = {
            ChatEntry._ID,
            ChatEntry.COLUMN_FROM,
            ChatEntry.COLUMN_TO,
            ChatEntry.COLUMN_TYPE,
            ChatEntry.COLUMN_POST_DATE,
            ChatEntry.COLUMN_CONTENT,
            ChatEntry.COLUMN_LAYOUTID
    };

    public ChatDao(Context context)
    {
        mDbHelper = new GoGouSQLiteHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public void postChat(Chat chat) {

        if (chat == null)
            return;

        ContentValues values = new ContentValues();
        //values.put(ChatEntry._ID, chat.getId());
        values.put(ChatEntry.COLUMN_FROM, chat.getFrom());
        values.put(ChatEntry.COLUMN_TO, chat.getTo());
        values.put(ChatEntry.COLUMN_TYPE, chat.getType());
        values.put(ChatEntry.COLUMN_POST_DATE, chat.getPostDate());
        values.put(ChatEntry.COLUMN_CONTENT, chat.getContent());
        values.put(ChatEntry.COLUMN_LAYOUTID, chat.getLayoutId());

        mDatabase.insert(ChatEntry.TABLE_NAME, null, values);
        return;
    }

    public void deleteFriend(Chat chat) {
        long id = chat.getId();
        Log.d(TAG, "Deleted chat with id: " + id);
        mDatabase.delete(ChatEntry.TABLE_NAME, ChatEntry._ID + " = " + id, null);
    }

    public List<Chat> listAllChats(String from, String to, boolean isMutual) {
        List<Chat> chats = new ArrayList<Chat>();
        Log.d(TAG, "List the chats from: " + from + " to: " + to);
;
        String selection = isMutual ?
                        ChatEntry.COLUMN_FROM + " IN ('" + from + "', '" + to + "') AND " + ChatEntry.COLUMN_TO + " IN ('" + from + "', '" + to + "')"
                :       ChatEntry.COLUMN_FROM + " = '" + from + "' AND " + ChatEntry.COLUMN_TO + " = '" + to + "'";

        Cursor cursor = mDatabase.query(ChatEntry.TABLE_NAME, allColumns,
                selection,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Chat chat = cursorToComment(cursor);
            chats.add(chat);
            Log.d(TAG, "Found chat message: " + chat.toString());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return chats;
    }

    private Chat cursorToComment(Cursor cursor) {
        Chat chat = new Chat();
        chat.setId(cursor.getLong(0));
        chat.setFrom(cursor.getString(1));
        chat.setTo(cursor.getString(2));
        chat.setType(cursor.getString(3));
        chat.setPostDate(cursor.getString(4));
        chat.setContent(cursor.getString(5));
        chat.setLayoutId(cursor.getInt(6));
        return chat;
    }


}
