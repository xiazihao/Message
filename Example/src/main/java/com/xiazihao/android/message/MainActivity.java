package com.xiazihao.android.message;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiazihao.android.chatmessageview.IconClick;
import com.xiazihao.android.chatmessageview.MessageDataBaseHelper;
import com.xiazihao.android.chatmessageview.MessageView;
import com.xiazihao.android.chatmessageview.MessageConversationDB;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button mLeftButton;
    private Button mRightButton;
    private MessageDataBaseHelper mDB;
    private Button mUnselectButton;
    private Button mSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MessageView messageView = (MessageView) findViewById(R.id.view);
        mDB = new MessageDataBaseHelper(this, "test.db", null, 1) {
            @Override
            public Drawable getImage(int position) {
                switch (getType(position)) {
                    case LEFT:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            return MainActivity.this.getDrawable(R.drawable.face_1);
                        } else {
                            return getResources().getDrawable(R.drawable.face_1);
                        }
                    case RIGHT:
                        return getResources().getDrawable(R.drawable.face_2);
                }
                return getResources().getDrawable(R.drawable.face_2);
            }
        };

        mLeftButton = (Button) findViewById(R.id.left_button);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                mDB.newMessage("test" + date.toString(), "xiazihao", date, MessageConversationDB.LEFT);
                RecyclerView.Adapter adapter = messageView.getAdapter();
                adapter.notifyItemInserted(adapter.getItemCount());
                messageView.scrollToPosition(messageView.getAdapter().getItemCount());
            }
        });
        mRightButton = (Button) findViewById(R.id.right_button);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                mDB.newMessage("test" + date.toString(), "xiazihao", date, MessageConversationDB.RIGHT);
                RecyclerView.Adapter adapter = messageView.getAdapter();
                adapter.notifyItemInserted(adapter.getItemCount());
                messageView.scrollToPosition(messageView.getAdapter().getItemCount());
            }
        });
        messageView.setConversations(mDB);
        messageView.setMessageIconClick(new IconClick() {
            @Override
            public void onClick(View view, int position) {
                mDB.removeDialog(position);
                messageView.notifyRemove(position);


            }
        });
        mSelectButton = (Button) findViewById(R.id.select_button);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageView.setOnSelectionMode(true);
            }
        });
        mUnselectButton = (Button) findViewById(R.id.unselect_button);
        mUnselectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageView.setOnSelectionMode(false);
            }
        });
    }
}
