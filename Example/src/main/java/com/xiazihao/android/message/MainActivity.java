package com.xiazihao.android.message;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiazihao.android.chatmessageview.Message;
import com.xiazihao.android.chatmessageview.MessageConversationDB;

public class MainActivity extends AppCompatActivity {

    private Button mLeftButton;
    private Button mRightButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Message message = (Message) findViewById(R.id.view);
        message.setConversations(new MessageConversationDB() {
            private String[] mDate = {"1:00", "2:00", "3:00", "4:00", "5:00"};
            private String[] mConversations = {"xia", "ega", "geage", "geag", "zi"};

            public String getName(int position) {
                return null;
            }

            @Override
            public Drawable getImage(int position) {
                if (position % 2 == 0) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        return getDrawable(R.drawable.face_2);
                    } else {
                        return getResources().getDrawable(R.drawable.face_2);
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        return getDrawable(R.drawable.face_1);
                    } else {
                        return getResources().getDrawable(R.drawable.face_1);
                    }
                }
            }

            @Override
            public int getType(int postion) {
                if (postion % 2 == 0) {
                    return LEFT;
                } else {
                    return RIGHT;
                }
            }

            @Override
            public int size() {
                return mDate.length;
            }

            @Override
            public String getDialogTime(int position) {
                return mDate[position];
            }

            @Override
            public String getDialogMessage(int position) {
                return mConversations[position];
            }
        });
        mLeftButton = (Button) findViewById(R.id.left_button);
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mRightButton = (Button) findViewById(R.id.right_button);
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
