package com.appsnipp.loginsamples.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ListAddUserGroupChatAdapter;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
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

public class CreateGroupActivity extends AppCompatActivity implements ManagementUserItemClicked {
    private ArrayList<UserModelDetail> mUsermodelDetails;
    private ListAddUserGroupChatAdapter mAdapter;
    private ChipGroup chipGroup;
    private NestedScrollView mNestedScrollContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_chat);
        getListUser();
        setupRecyclerView();
        setupChip();
    }

    private void setupChip() {
        chipGroup = findViewById(R.id.chip_group);
        mNestedScrollContainer = findViewById(R.id.nsv_chip_container);
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = this.findViewById(R.id.rv_create_list_user_group);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(CreateGroupActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ListAddUserGroupChatAdapter(new ArrayList<UserModelDetail>(),this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }

    private void getListUser() {
        User user = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
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
                            mAdapter.userModelList = mUsermodelDetails;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        Toast.makeText(CreateGroupActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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

    private boolean checkChipItem(ChipGroup chipGroup, String id) {
        for (int i = 0; i< chipGroup.getChildCount(); i++){
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
}
