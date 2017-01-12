package com.xiazihao.android.chatmessageview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiazihao on 2017/1/11.
 */

public class MessageView extends RecyclerView {
    private static final int TIMEVIEW = 2;

    public void setConversations(MessageConversationDB conversations) {
        mConversations = conversations;
        if(this.getAdapter() == null){
            this.setAdapter(new MessageAdapter());
            this.smoothScrollToPosition(this.getAdapter().getItemCount());
        }
    }

    private MessageConversationDB mConversations;

    public MessageView(Context context) {
        super(context);
        init(context);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    private void init(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        this.setLayoutManager(layoutManager);
        if(mConversations != null){
            this.setAdapter(new MessageAdapter());
            this.scrollToPosition(this.getAdapter().getItemCount());
        }


    }

    private class MessageAdapter extends Adapter<MessageHolder> {

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view;
            switch (viewType) {
                case MessageConversationDB.LEFT:
                    view = inflater.inflate(R.layout.message_view_left, parent, false);
                    return new MessageHolder(view);
                case MessageConversationDB.RIGHT:
                    view = inflater.inflate(R.layout.message_view_right, parent, false);
                    return new MessageHolder(view);
                case TIMEVIEW:
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return mConversations.getType(position);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
           holder.setImage( mConversations.getImage(position));
            holder.setName(mConversations.getName(position));
            holder.setText(mConversations.getDialogMessage(position));
            holder.setTime(mConversations.getDialogTime(position));
        }

        @Override
        public int getItemCount() {
            return mConversations.size();
        }
    }

    private class MessageHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mName;
        private TextView mText;
        private TextView mTime;

        public MessageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.message_image);
            mName = (TextView) itemView.findViewById(R.id.message_name);
            mText = (TextView) itemView.findViewById(R.id.message_text);
            mTime = (TextView) itemView.findViewById(R.id.message_time);
        }
        public void setImage(Drawable image){
            mImageView.setImageDrawable(image);
        }
        public void setName(String name){
            mName.setText(name);
        }
        public void setText(String text){
            mText.setText(text);
        }
        public void setTime(String time){
            mTime.setText(time);
        }
    }
}


