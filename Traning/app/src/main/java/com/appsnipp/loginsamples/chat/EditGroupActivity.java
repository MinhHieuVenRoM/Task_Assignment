package com.appsnipp.loginsamples.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ListAddUserGroupChatAdapter;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Chat.DataGroup;
import com.appsnipp.loginsamples.model.Chat.Group_chat;
import com.appsnipp.loginsamples.model.Chat.Group_chat_edit;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.ChipItem;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;
import static com.appsnipp.loginsamples.chat.ChatBoxGroupActivity.dsuser;

public class EditGroupActivity extends AppCompatActivity implements ManagementUserItemClicked {
    private ArrayList<UserModelDetail> mUsermodelDetails;
    private ListAddUserGroupChatAdapter mAdapter;
    private ChipGroup chipGroup;
    private ArrayList<String> list_create_user_group = new ArrayList<String>();
    private NestedScrollView mNestedScrollContainer;
    private EditText group_name_create;
    private User user;
    DataGroup ModelGroup;
    private  String ID_ROOM;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_chat);
        setupRecyclerView();
        setupChip();
        getListUser();
        getDataIntent();
        group_name_create=findViewById(R.id.group_name_create);
        group_name_create.setText(ModelGroup.getRoomName());

    }
    private void getDataIntent() {

        Intent intent = getIntent();
        ID_ROOM = (String) intent.getSerializableExtra("ID_Room");
        ModelGroup= (DataGroup) intent.getSerializableExtra("ModelGroup");
    }
    private void setupChip() {
        chipGroup = findViewById(R.id.chip_group);
        mNestedScrollContainer = findViewById(R.id.nsv_chip_container);
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = this.findViewById(R.id.rv_create_list_user_group);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(EditGroupActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ListAddUserGroupChatAdapter(new ArrayList<UserModelDetail>(), this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }

    private void getListUser() {
        user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
        String token = "";
        if (user != null) {
            token = user.getToken();
        }

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.getListUser(token)
                .enqueue(new Callback<ListUserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ListUserModel> call, @NonNull Response<ListUserModel> response) {
                        ListUserModel models = response.body();
                        if (models != null) {
                            mUsermodelDetails = models.getData();
                           for(int i=0;i<mUsermodelDetails.size();i++){
                               if(mUsermodelDetails.get(i).getId().equals(user.getId()))
                               {
                                   mUsermodelDetails.remove(i);
                               }
                               for(int j=0;j<ModelGroup.getUsers().size();j++){

                                   if((mUsermodelDetails.get(i).getId().equals(ModelGroup.getUsers().get(j)))){
                                       mUsermodelDetails.get(i).setChecked(true);
                                       Chip chip = new ChipItem().getChip(EditGroupActivity.this,  mUsermodelDetails.get(i).getName(),  mUsermodelDetails.get(i).getId());
                                       chipGroup.addView(chip);
                                       mNestedScrollContainer.fullScroll(View.FOCUS_DOWN);
                                   }

                               }

                           }




                            mAdapter.userModelList = mUsermodelDetails;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        Toast.makeText(EditGroupActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void back_group_chat(View view) {
        finish();
    }

    @Override
    public void onItemClickeduser(final int position, UserModelDetail model) {
        boolean isRemove = checkChipItem(chipGroup, model.getId());

        if (!isRemove) {
            final Chip chip = new ChipItem().getChip(this, model.getName(), model.getId());
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    for (int i = 0; i < mAdapter.userModelList.size(); i++) {
                        UserModelDetail row = mAdapter.userModelList.get(position);
                        if (row.getId().equals(tag)) {
                            mAdapter.notifyItemChanged(i);
                        }
                    }
                    chipGroup.removeView(chip);
                }
            });
            chipGroup.addView(chip);
            mNestedScrollContainer.fullScroll(View.FOCUS_DOWN);
        }

        mAdapter.notifyItemChanged(position);
    }

    private void edit_group() {
        list_create_user_group.clear();
        list_create_user_group.add(user.getId());

        for (int i = 0; i < mAdapter.userModelList.size(); i++) {
            UserModelDetail row = mAdapter.userModelList.get(i);
            if (row.isChecked()) {
                list_create_user_group.add(row.getId());
            }
        }

        User user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
        String token = "";
        if (user != null) {
            token = user.getToken();
        }


        if(list_create_user_group.size()>=3) {
            showLoading();
            RequestAPI service = APIClient.getClient().create(RequestAPI.class);
            service.edit_group_chat(token, ModelGroup.getId(), list_create_user_group, group_name_create.getText().toString())
                    .enqueue(new Callback<Group_chat_edit>() {
                        @Override
                        public void onResponse(@NonNull Call<Group_chat_edit> call, @NonNull Response<Group_chat_edit> response) {
                            Group_chat_edit models = response.body();
                            progressDialog.dismiss();
                            if (models != null) {
                                Toast.makeText(EditGroupActivity.this, "Success Edit Room", Toast.LENGTH_SHORT).show();
                                dsuser = list_create_user_group;
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Group_chat_edit> call, @NonNull Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(EditGroupActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            Toast.makeText(EditGroupActivity.this, "Group less than two people", Toast.LENGTH_SHORT).show();


        }

    }
    private void showLoading() {
        progressDialog = new ProgressDialog(EditGroupActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
    private boolean checkChipItem(ChipGroup chipGroup, String id) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            View chip = chipGroup.getChildAt(i);
            if (chip != null) {
                if (chip.getTag() == id) {
                    chipGroup.removeView(chip);
                    return true;
                }
            }
        }
        return false;
    }

    public void edit_group(View view) {
        edit_group();


    }
}
