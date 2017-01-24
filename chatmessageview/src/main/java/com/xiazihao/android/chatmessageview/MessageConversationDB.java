package com.xiazihao.android.chatmessageview;

import android.graphics.drawable.Drawable;

import java.util.Date;
import java.util.List;

/**
 * Created by xiazihao on 2017/1/11.
 */

public interface MessageConversationDB {
    int LEFT = 0;
    int RIGHT = 1;

    String getName(int position);

    Drawable getImage(int position);

    int getType(int postion);

    int size();

    String getDialogDateString(int position);

    Date getDialogDate(int position);

    String getDialogMessage(int position);

    void removeDialog(int position);

}
