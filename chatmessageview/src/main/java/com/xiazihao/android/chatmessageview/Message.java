package com.xiazihao.android.chatmessageview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiazihao on 2017/1/11.
 */

public class Message extends RecyclerView {

    private static final int LEFTMESSAGE = 0;
    private static final int RIGHTMESSAGE = 1;
    private static final int TIMEVIEW = 2;
    public Message(Context context) {
        super(context);
        init(context);
    }

    public Message(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Message(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }
    private void init(Context context){
        this.setLayoutManager(new LinearLayoutManager(context));
        this.setAdapter(new MessageAdapter());
        this.smoothScrollToPosition(this.getAdapter().getItemCount());
    }
    private class MessageAdapter extends Adapter<MessageHolder>{

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view;
            switch (viewType){
                case LEFTMESSAGE:
                    view = inflater.inflate(R.layout.message_view_left,parent,false);
                    return new MessageHolder(view);
                case RIGHTMESSAGE:
                    view = inflater.inflate(R.layout.message_view_right,parent,false);
                    return new MessageHolder(view);
                case TIMEVIEW:
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            if(position % 2 == 0){
                return LEFTMESSAGE;
            }else
                return RIGHTMESSAGE;
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
    private class MessageHolder extends RecyclerView.ViewHolder{

        public MessageHolder(View itemView) {
            super(itemView);
        }
    }
}


