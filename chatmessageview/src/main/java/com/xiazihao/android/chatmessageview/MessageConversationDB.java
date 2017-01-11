package com.xiazihao.android.chatmessageview;

import java.util.List;

/**
 * Created by xiazihao on 2017/1/11.
 */

public interface MessageConversationDB {
    public String getLeftName();
    public String getRightName();
    public String size();
    public String getDialogTime(int position);
    public String getDialogMessage(int position);
}
