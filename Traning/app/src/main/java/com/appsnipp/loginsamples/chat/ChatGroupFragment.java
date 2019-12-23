package com.appsnipp.loginsamples.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ChatGroupAdaterUserList;
import com.appsnipp.loginsamples.adapter.ManagementGroupItemClicked;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance_checkout;
import com.appsnipp.loginsamples.model.Chat.DataGroup;
import com.appsnipp.loginsamples.model.Chat.DataGroup;
import com.appsnipp.loginsamples.model.Chat.Datum;
import com.appsnipp.loginsamples.model.Chat.Find_Group_Chat;
import com.appsnipp.loginsamples.model.Chat.Messenger;
import com.appsnipp.loginsamples.model.Message;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class ChatGroupFragment extends Fragment implements ManagementGroupItemClicked {
    private View view;
    private ProgressDialog progressDialog;
    private ArrayList<DataGroup> mGroupchat;
    private ChatGroupAdaterUserList mAdapter;
    private FloatingActionButton btn_add;
    ArrayList<String> message;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        return view;
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_list_user_chat_group);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ChatGroupAdaterUserList(new ArrayList<DataGroup>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListUser();
        setupRecyclerView();
        showLoading();
        btn_add = view.findViewById(R.id.btn_add_group_chat);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(i);
            }
        });



    }

    private void getListUser() {
        User user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
        String token = "";
        if (user != null){
            token = user.getToken();
        }

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.find_group_chat(token,user.getId())
                .enqueue(new Callback<Find_Group_Chat>() {
                    @Override
                    public void onResponse(@NonNull Call<Find_Group_Chat> call, @NonNull Response<Find_Group_Chat> response) {
                        progressDialog.dismiss();
                        Find_Group_Chat models = response.body();
                        if (models != null) {
                            mGroupchat = (ArrayList<DataGroup>) models.getData();

                            mAdapter.userModelList = mGroupchat;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Find_Group_Chat> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(view.getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override


    public void onItemClickeduser(int position, DataGroup model) {
        Toast.makeText(view.getContext(), model.getRoomName(), Toast.LENGTH_SHORT).show();
        Intent i  = new Intent(getActivity(), ChatBoxGroupActivity.class);
        User user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
        i.putExtra(USER_MODEL_KEY,user );
        i.putExtra("Group",  model);
        i.putExtra("ID_Room", model.getId());
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListUser();


    }

    private void showLoading() {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }



}
