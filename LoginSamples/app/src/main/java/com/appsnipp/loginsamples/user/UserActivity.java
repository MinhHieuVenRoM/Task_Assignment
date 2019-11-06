package com.appsnipp.loginsamples.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.appsnipp.loginsamples.adapter.ManagementUserAdapter;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.task.DetailTaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity implements ManagementUserItemClicked {
    ProgressDialog progressDialog;

    private ArrayList<UserModelDetail> mUsermodelDetails;
    private ManagementUserAdapter mAdapter;
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

        FloatingActionButton fab = findViewById(R.id.btn_add_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getlistuser();
        setupRecyclerView();

        showLoading();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_managementuser);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ManagementUserAdapter(new ArrayList<UserModelDetail>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }


    private void getlistuser() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

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
        intent.putExtra("usermodel", model);
        startActivity(intent);
    }
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        setupRecyclerView();
        getlistuser();


        //Refresh your stuff here
    }

    public void back_home_list_user(View view) {
        finish();
    }
}
