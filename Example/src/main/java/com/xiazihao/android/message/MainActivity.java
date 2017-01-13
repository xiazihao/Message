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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MessageView messageView = (MessageView) findViewById(R.id.view);
        mDB = new MessageDataBaseHelper(this, "test.db", null, 1) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public Drawable getImage(int position) {
                switch (getType(position)) {
                    case LEFT:
                        return MainActivity.this.getDrawable(R.drawable.face_1);
                    case RIGHT:
                        return MainActivity.this.getDrawable(R.drawable.face_2);
                }
                return MainActivity.this.getDrawable(R.drawable.face_2);
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
                messageView.smoothScrollToPosition(messageView.getAdapter().getItemCount());
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
                messageView.smoothScrollToPosition(messageView.getAdapter().getItemCount());
            }
        });
        messageView.setConversations(mDB);
        messageView.setMessageIconClick(new IconClick() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this,"icon on click " + position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
