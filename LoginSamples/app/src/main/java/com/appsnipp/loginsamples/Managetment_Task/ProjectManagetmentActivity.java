package com.appsnipp.loginsamples.Managetment_Task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
import com.appsnipp.loginsamples.adapter.ProjectItemClicked;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Project_model.ProjectListResponse;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.task.TaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectManagetmentActivity extends AppCompatActivity implements ProjectItemClicked {


    private FloatingActionButton btn_add;
    private ProjectAdapter mAdapter;
    private ArrayList<ProjectModel> mProjectModelList;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    ProgressDialog progressDialog;
    private int mYear, mMonth, mDay;
    private TextView tv_enddate_addproject, tv_full_name_project_addproject;
    private SwipeRefreshLayout swipeContainer;
    UserModelDetail modeluser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        btn_add = findViewById(R.id.btn_add_project);
        int role = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getRole();
        if (role == 0) {

            btn_add.setVisibility(View.VISIBLE);

        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("List Project");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("List Project");

        getDataIntent();
        getProjectListData();
        setupRecyclerView();

        showLoading();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoading();
                getProjectListData();
                setupRecyclerView();
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra("usermodel");
    }
    private void getProjectListData() {

        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);

        service.getProjectofUser(token,modeluser.getId())
                .enqueue(new Callback<ProjectListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProjectListResponse> call, @NonNull Response<ProjectListResponse> response) {
                        progressDialog.dismiss();
                        ProjectListResponse models = response.body();
                        if (models != null) {
                            mProjectModelList = models.getData_project();
                            mAdapter.projectModelList = mProjectModelList;
                            mAdapter.userModelDetailList = mUsermodelDetails;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProjectListResponse> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProjectManagetmentActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showLoading() {
        progressDialog = new ProgressDialog(ProjectManagetmentActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_project);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProjectAdapter(new ArrayList<ProjectModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }

    @Override
    public void onItemClickedProject(int position, ProjectModel model) {

        Toast.makeText(ProjectManagetmentActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Detail_Task_ManagetmentActivity.class);
        intent.putExtra("usermodel", modeluser);
        intent.putExtra("projectModel", model);
        startActivity(intent);
    }


    public void back_home_project(View view) {
        finish();
    }

    @Override
    public void onResume() {  // After a pause OR at startup
//        setupRecyclerView();
//        getProjectListData();
        super.onResume();

        //Refresh your stuff here
    }
}
