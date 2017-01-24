package com.xiazihao.android.chatmessageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiazihao on 2017/1/11.
 */

public class MessageView extends RecyclerView {

    private static final int TIMEVIEW = 2;
    private boolean mIsOnSelectionMode = false;
    private List<Integer> mSelectDataset;

    /**
     * Set message conversation database
     *
     * @param conversations The database object which must implement MessageConversationDB interface
     */
    public void setConversations(MessageConversationDB conversations) {
        mConversations = conversations;
        if (this.getAdapter() == null) {
            this.setAdapter(new MessageAdapter());
            this.smoothScrollToPosition(this.getAdapter().getItemCount());
        }
    }

    private IconClick mMessageIconClick = new IconClick() {
        @Override
        public void onClick(View view, int position) {

        }
    };

    /**
     * Set selection mode. In selection mode, each item has a checkbox
     *
     * @param onSelectionMode true, get into selection mode, fase, to end selection mode.
     */
    public void setOnSelectionMode(boolean onSelectionMode) {
        mIsOnSelectionMode = onSelectionMode;
        getAdapter().notifyDataSetChanged();
        mSelectDataset = null;
    }

    /**
     * Get select data set which you select in selection mode
     *
     * @return A list which contain the index of item you selected.
     */
    public List<Integer> getSelectDataset() {
        return mSelectDataset;
    }

    /**
     * Set message head icon click listener, which whill invoke when clicking the circle iamge view.
     *
     * @param messageIconClick The click listener object.
     */
    public void setMessageIconClick(IconClick messageIconClick) {
        mMessageIconClick = messageIconClick;
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
        if (mConversations != null) {
            this.setAdapter(new MessageAdapter());
            this.scrollToPosition(this.getAdapter().getItemCount());
        }

    }

    public void scrollToEnd() {
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
            holder.bind(position);
            holder.setOnClickListener(mMessageIconClick);

        }

        @Override
        public int getItemCount() {
            return mConversations.size();
        }

    }

    private class MessageHolder extends RecyclerView.ViewHolder implements OnLongClickListener {

        private ImageView mImageView;
        private TextView mName;
        private TextView mText;
        private TextView mTime;
        private CheckBox mCheckBox;

        public MessageHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.message_image);
            mName = (TextView) itemView.findViewById(R.id.message_name);
            mText = (TextView) itemView.findViewById(R.id.message_text);
            mTime = (TextView) itemView.findViewById(R.id.message_time);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.message_checkbox);
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean b) {
                    if (b) {
                        mSelectDataset.add(getAdapterPosition());
                    } else {
                        mSelectDataset.remove((Integer) getAdapterPosition());
                    }
                }
            });
            if (mIsOnSelectionMode) {
                mCheckBox.setVisibility(View.VISIBLE);
            } else {
                mCheckBox.setVisibility(View.GONE);
            }
            itemView.setOnLongClickListener(this);
        }

        public void bind(int position) {
            mImageView.setImageDrawable(mConversations.getImage(position));
            mName.setText(mConversations.getName(position));
            mText.setText(mConversations.getDialogMessage(position));
            mTime.setText(mConversations.getDialogDateString(position));
            if (mIsOnSelectionMode) {
                mCheckBox.setVisibility(View.VISIBLE);
            } else {
                mCheckBox.setVisibility(View.GONE);
            }
            if (mSelectDataset == null) {
                mSelectDataset = new ArrayList<>();
            }
            mCheckBox.setChecked(mSelectDataset.contains((Integer) getAdapterPosition()));
        }


        public void setOnClickListener(final IconClick messageIconClick) {
            mImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = MessageHolder.this.getAdapterPosition();
                    messageIconClick.onClick(view, position);
                }
            });
        }


        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            setOnSelectionMode(true);
            return true;
        }
    }

    /**
     * Notify item removed, @note: You should remove database manually
     *
     * @param position The index of message item which you removed.
     */
    public void notifyRemove(int position) {
        this.getAdapter().notifyItemRemoved(position);
    }
}


