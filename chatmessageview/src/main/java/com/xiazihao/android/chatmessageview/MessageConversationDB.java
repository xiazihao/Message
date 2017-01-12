package com.xiazihao.android.chatmessageview;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by xiazihao on 2017/1/11.
 */

public interface MessageConversationDB {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public String getName(int position);
    public Drawable getImage(int position);
    public int getType(int postion);
    public int size();
    public String getDialogTime(int position);
    public String getDialogMessage(int position);
}
