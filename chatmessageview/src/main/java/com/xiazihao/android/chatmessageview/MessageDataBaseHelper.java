package com.xiazihao.android.chatmessageview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiazihao on 2017/1/11.
 */

public class MessageDataBaseHelper extends SQLiteOpenHelper implements MessageConversationDB {
    public class Schema {
        static final String TEXT = "text";
        static final String DATE = "date";
        static final String USER = "user";
        static final String TYPE = "type";
    }

    public class MessageInfo {
        private String mText;
        private int mType;
        private Date mTime;
        private String mUser;

    }

    private List<MessageInfo> mMessageInfos = new ArrayList<>();
    private String mDbName;
    private static final String mTableName = "message";
    private SQLiteDatabase mDatabase;
    private Context mContext;

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Cursor cursor = db.query(mTableName, new String[]{Schema.USER, Schema.TYPE, Schema.TEXT, Schema.DATE}, null, null, null, null, Schema.DATE + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MessageInfo newInfo = new MessageInfo();
            newInfo.mText = cursor.getString(cursor.getColumnIndex(Schema.TEXT));
            newInfo.mTime = new Date(cursor.getLong(cursor.getColumnIndex(Schema.DATE)));
            newInfo.mType = cursor.getInt(cursor.getColumnIndex(Schema.TYPE));
            newInfo.mUser = cursor.getString(cursor.getColumnIndex(Schema.USER));
            mMessageInfos.add(newInfo);
            cursor.moveToNext();
        }

    }

    public MessageDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mDbName = name;
        mContext = context;
        mDatabase = this.getWritableDatabase();
    }

    public MessageDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mDbName = name;
        mContext = context;
        mDatabase = this.getWritableDatabase();
    }

    public void newMessage(String text, String user, Date date, int type) {
        ContentValues values = new ContentValues();
        values.put(Schema.DATE, date.getTime());
        values.put(Schema.TEXT, text);
        values.put(Schema.TYPE, type);
        values.put(Schema.USER, user);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.mType = type;
        messageInfo.mUser = user;
        messageInfo.mText = text;
        messageInfo.mTime = date;
        mMessageInfos.add(messageInfo);
        mDatabase.insert(mTableName, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + mTableName + " ( _id integer primary key autoincrement, " +
                Schema.DATE + " ," +
                Schema.TEXT + " ," +
                Schema.TYPE + " ," +
                Schema.USER + " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }

    @Override
    public String getName(int position) {

        return mMessageInfos.get(position).mUser;

    }

    @Override
    public Drawable getImage(int position) {
        return null;
    }

    @Override
    public int getType(int postion) {
        return mMessageInfos.get(postion).mType;
    }

    @Override
    public int size() {
        return mMessageInfos.size();
    }

    @Override
    public String getDialogTime(int position) {
        String fomate = "MMM d E H:m:s";
        return DateFormat.format(fomate, mMessageInfos.get(position).mTime).toString();
    }

    @Override
    public String getDialogMessage(int position) {
        return mMessageInfos.get(position).mText;
    }
}
