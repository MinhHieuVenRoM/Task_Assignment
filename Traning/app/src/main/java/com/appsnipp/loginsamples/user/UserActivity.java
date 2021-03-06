package com.appsnipp.loginsamples.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ManagementUserAdapter;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class UserActivity extends AppCompatActivity implements ManagementUserItemClicked {
    ProgressDialog progressDialog;

    private ArrayList<UserModelDetail> mUsermodelDetails;
    private ManagementUserAdapter mAdapter;
    private SwipeRefreshLayout swipeContainer;
    public static String UPDATE_USER_MODEL_KEY = "UPDATE_USER_MODEL_KEY";
    public static String POSITION_LIST_USER_KEY = "POSITION_LIST_USER_KEY";
    public static int REQUEST_CODE_USER_DETAIL = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        FloatingActionButton fab = findViewById(R.id.btn_add_user);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getlistuser();
        setupRecyclerView();

        showLoading();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_userlist);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ManagementUserAdapter(new ArrayList<UserModelDetail>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }


    private void getlistuser() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.getListUser(token)
                .enqueue(new Callback<ListUserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ListUserModel> call, @NonNull Response<ListUserModel> response) {
                        progressDialog.dismiss();
                        ListUserModel models = response.body();
                        if (models != null) {
                            mUsermodelDetails = models.getData();
                            mAdapter.userModelList = mUsermodelDetails;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UserActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(UserActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    @Override
    public void onItemClickeduser(int position, UserModelDetail model) {
        Toast.makeText(UserActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailUserAdminActivity.class);
        intent.putExtra(USER_MODEL_KEY, model);
        intent.putExtra(POSITION_LIST_USER_KEY, position);
//        startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE_USER_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER_DETAIL && resultCode == RESULT_OK && data != null) {
            String dataCode = data.getStringExtra(UPDATE_USER_MODEL_KEY);
            UserModelDetail userModelDetail = (UserModelDetail) data.getSerializableExtra(USER_MODEL_KEY);
            int position = data.getIntExtra(POSITION_LIST_USER_KEY, 0);
            if (dataCode.equals("Update")) {
                getlistuser();
                mAdapter.userModelList.set(position, userModelDetail);
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    public void back_home_list_user(View view) {
        finish();
    }
}
