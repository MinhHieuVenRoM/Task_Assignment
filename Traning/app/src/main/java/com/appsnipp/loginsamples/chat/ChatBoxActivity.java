package com.appsnipp.loginsamples.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ChatBoxAdapter;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.Message;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatBoxActivity extends AppCompatActivity {
    private String nickname;
    public RecyclerView myRecylerView;
    public ArrayList<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageText;
    public Button send;

    public static Socket mSocket;

    public void init() {
        try {
            mSocket = IO.socket("http://192.168.42.36:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //create connection

        mSocket.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        init();

        messageText = findViewById(R.id.message);
        send = findViewById(R.id.send);

// get the nickame of the user
        String name = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getName();
        nickname = name;
//
        MessageList = new ArrayList<>();

        setupRecyclerView();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messageText.getText().toString().isEmpty()){
                mSocket.emit("messagedetection", nickname, messageText.getText().toString());

                messageText.setText(" ");
            }
        });

        mSocket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        // get the extra data from the fired event and display a toast
                        Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mSocket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetection
                if (!messageText.getText().toString().isEmpty()) {
                    mSocket.emit("messagedetection", nickname, messageText.getText().toString());
                    messageText.setText(" ");
                }
            }
        });

        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event
                            String nickname = data.getString("senderNickname");
                            String message = data.getString("message");

                            // make instance of message
                            Message m = new Message(nickname, message);

                            //add the message to the messageList
                            MessageList.add(m);

                            // add the new updated list to the adapter

                            // notify the adapter to update the recycler view

                            chatBoxAdapter.notifyItemInserted(MessageList.size());
                            //set the adapter for the recycler view

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setupRecyclerView() {
        myRecylerView = findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
//        myRecylerView.setItemAnimator(new DefaultItemAnimator());
        chatBoxAdapter = new ChatBoxAdapter(MessageList, nickname);
        myRecylerView.setAdapter(chatBoxAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}