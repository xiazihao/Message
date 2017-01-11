package com.xiazihao.android.chatmessageview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiazihao on 2017/1/11.
 */

public class Message extends View {

    public Message(Context context) {
        super(context);
    }

    public Message(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Message(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Message(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
