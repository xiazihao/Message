package com.xiazihao.android.chatmessageview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Calendar;
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
        private Date mDate;
        private String mUser;

    }


    private List<MessageInfo> mMessageInfos = new ArrayList<>();
    private static final String mTableName = "message";
    private SQLiteDatabase mDatabase;
    private Handler mDbHandler;
    private static final int INSERT_TASK = 0;
    private static final int OPEN_TASK = 1;
    private static final int REMOVE_TASK = 2;

    private HandlerThread mThread = new HandlerThread("database thread");


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Cursor cursor = db.query(mTableName, new String[]{Schema.USER, Schema.TYPE, Schema.TEXT, Schema.DATE}, null, null, null, null, Schema.DATE + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MessageInfo newInfo = new MessageInfo();
            newInfo.mText = cursor.getString(cursor.getColumnIndex(Schema.TEXT));
            newInfo.mDate = new Date(cursor.getLong(cursor.getColumnIndex(Schema.DATE)));
            newInfo.mType = cursor.getInt(cursor.getColumnIndex(Schema.TYPE));
            newInfo.mUser = cursor.getString(cursor.getColumnIndex(Schema.USER));
            mMessageInfos.add(newInfo);
            cursor.moveToNext();
        }

    }

    public MessageDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        init(name);
    }

    public MessageDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        init(name);
    }

    private void init(String name) {
        mDatabase = this.getWritableDatabase();
        mThread.start();
        mDbHandler = new Handler(mThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                MessageInfo[] messageInfos;
                switch (msg.what) {
                    case INSERT_TASK:
                        messageInfos = (MessageInfo[]) msg.obj;
                        for (MessageInfo messageInfo : messageInfos) {
                            ContentValues values = new ContentValues();
                            values.put(Schema.DATE, messageInfo.mDate.getTime());
                            values.put(Schema.TEXT, messageInfo.mText);
                            values.put(Schema.TYPE, messageInfo.mType);
                            values.put(Schema.USER, messageInfo.mUser);
                            mDatabase.insert(mTableName, null, values);
                        }
                        break;
                    case OPEN_TASK:
                        break;
                    case REMOVE_TASK:
                        messageInfos = (MessageInfo[]) msg.obj;
                        mDatabase.beginTransaction();
                        for (MessageInfo messageInfo : messageInfos) {
                            messageInfo = messageInfos[0];
                            mDatabase.delete(mTableName, Schema.TEXT + " =? and " + Schema.DATE + " =?", new String[]{messageInfo.mText, messageInfo.mDate.toString()});
                            mDatabase.setTransactionSuccessful();
                        }
                        mDatabase.endTransaction();

                        break;
                }
            }
        };
    }

    public void newMessage(String text, String user, Date date, int type) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.mType = type;
        messageInfo.mUser = user;
        messageInfo.mText = text;
        messageInfo.mDate = date;
        mMessageInfos.add(messageInfo);
        Message message = Message.obtain(mDbHandler, INSERT_TASK, new MessageInfo[]{messageInfo});
        mDbHandler.sendMessage(message);
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
    public Date getDialogDate(int position) {
        return mMessageInfos.get(position).mDate;
    }

    @Override
    public String getDialogDateString(int position) {
        String sameDayFormate = "H:m:s";
        String sameWeekFormat = "d E H:m:s";
        String sameYearFormat = "d E H:m:s";
        String integerFormat = "MMM d E H:m:s";
        String formate = "MMM d E H:m:s";
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        Calendar messageDate = Calendar.getInstance();
        messageDate.setTime(mMessageInfos.get(position).mDate);
        if (currentDate.get(Calendar.YEAR) == messageDate.get(Calendar.YEAR)) {
            if (currentDate.get(Calendar.WEEK_OF_YEAR) == messageDate.get(Calendar.WEEK_OF_YEAR)) {
                if (currentDate.get(Calendar.DAY_OF_YEAR) == messageDate.get(Calendar.DAY_OF_YEAR)) {
                    formate = sameDayFormate;
                } else {
                    formate = sameWeekFormat;
                }
            } else {
                formate = sameWeekFormat;
            }
        } else {
            formate = integerFormat;
        }
        return DateFormat.format(formate, mMessageInfos.get(position).mDate).toString();
    }

    @Override
    public String getDialogMessage(int position) {
        return mMessageInfos.get(position).mText;
    }

    @Override
    public void removeDialog(int position) {
        MessageInfo messageInfo = mMessageInfos.get(position);
        MessageInfo[] messageInfos = new MessageInfo[]{messageInfo};
        Message message = Message.obtain(mDbHandler, REMOVE_TASK, messageInfos);
        mDbHandler.sendMessage(message);
        mMessageInfos.remove(position);
    }
}
