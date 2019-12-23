package com.appsnipp.loginsamples.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ChatBoxAdapter;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance_checkout;
import com.appsnipp.loginsamples.model.Chat.DataGroup;
import com.appsnipp.loginsamples.model.Chat.Messenger;
import com.appsnipp.loginsamples.model.Message;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class ChatBoxGroupActivity extends AppCompatActivity {
    private String nickname;
    public RecyclerView myRecylerView;
    public ArrayList<Message> MessageList;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messageText;
    public Button send;
    private TextView toolbar_title_chatuser,tv_typing;
    User modeluser;
    AppCompatImageView im_edit_group_chat;
    DataGroup ModelGroup;
    private  String ID_ROOM;

    public static Socket mSocket;

    public void init() {
        try {
            mSocket = IO.socket("https://task-assignment-app.herokuapp.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //create connection

        mSocket.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box_group);

        init();
        getDataIntent();
        messageText = findViewById(R.id.message);
        send = findViewById(R.id.send);
        toolbar_title_chatuser=findViewById(R.id.toolbar_title_chatuser);
        toolbar_title_chatuser.setText(ModelGroup.getRoomName());
        im_edit_group_chat=findViewById(R.id.im_edit_group_chat);

// get the nickame of the user
        String name = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getName();
        final String idusersend = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getId();
        nickname = name;
        if(idusersend.equals(ModelGroup.getCreatedBy())){
            im_edit_group_chat.setVisibility(View.VISIBLE);
        }


         tv_typing=findViewById(R.id.tv_typing);
        MessageList = new ArrayList<>();

        setupRecyclerView();
        checkoutAttendance();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve the nickname and the message content and fire the event messagedetectionif(!messageText.getText().toString().isEmpty()){
                mSocket.emit("messagedetection", nickname, idusersend, messageText.getText().toString());

                messageText.setText(" ");
            }
        });
        User user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
        mSocket.emit("join", modeluser.getId(),user.getId(),ID_ROOM);
        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSocket.emit("typing", nickname,ID_ROOM);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        mSocket.on("userjoinedthechat", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        String data = (String) args[0];
//                        // get the extra data from the fired event and display a toast
//                        Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });


//        mSocket.on("userdisconnect", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String data = (String) args[0];
//
//                        Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        });
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
                            myRecylerView.scrollToPosition(MessageList.size()-1);
                            //set the adapter for the recycler view

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mSocket.on("notifyTyping", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event
                            String nicknametemp = data.getString("senderNickname");
                            String message = data.getString("message");
                            if(!nicknametemp.equals(nickname)){
                                tv_typing.setText(nicknametemp+message);
                                // Toast.makeText(ChatBoxActivity.this, nicknametemp+message, Toast.LENGTH_SHORT).show();
                                CountDownTimer countDownTimer=new CountDownTimer(2000,1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        tv_typing.setText("");
                                        cancel();
                                    }
                                };
                                countDownTimer.start();
                            }


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
        //mSocket.disconnect();
        super.onDestroy();
    }
    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (User) intent.getSerializableExtra(USER_MODEL_KEY);
        ID_ROOM = (String) intent.getSerializableExtra("ID_Room");
        ModelGroup= (DataGroup) intent.getSerializableExtra("Group");
    }

    public void backuser_chat(View view) {
        //mSocket.disconnect();
        finish();

    }

    private void checkoutAttendance() {
       // showLoading();
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<Messenger> call = service.get_chat_history_twouser(token,ID_ROOM );
        call.enqueue(new Callback<Messenger>() {
            @Override
            public void onResponse(@NonNull Call<Messenger> call, @NonNull Response<Messenger> response) {
              //  progressDialog.dismiss();
                Messenger models = response.body();
                if (models != null) {
                    for(int i=0;i<models.getData().size();i++){

                        String name=models.getData().get(i).getUserDetail().getName();
                        String message=models.getData().get(i).getMessage();
                        // make instance of message
                        Message m = new Message(name, message);

                        //add the message to the messageList
                        MessageList.add(m);
                        chatBoxAdapter.notifyItemInserted(MessageList.size());
                        myRecylerView.scrollToPosition(MessageList.size()-1);
                    }

                }
                else {
                    try {
                        Gson gson = new Gson();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Attendance_checkout model = gson.fromJson(object.toString(), Attendance_checkout.class);

                        if (!model.getMessage().equals("Error")) {
                          //  showdialogCheckin();

                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Messenger> call, @NonNull Throwable t) {

                   }
        });


    }

    public void Edit_group(View view) {

        Intent i  = new Intent(ChatBoxGroupActivity.this, EditGroupActivity.class);
        i.putExtra("ID_Room", ID_ROOM);
        i.putExtra("ModelGroup", ModelGroup);
        startActivity(i);

    }
}