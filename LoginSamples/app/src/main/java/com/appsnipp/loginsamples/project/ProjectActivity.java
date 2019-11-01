package com.appsnipp.loginsamples.project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.chat.ChatActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.Project_model.ProjectListResponse;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.task.TaskActivity;
import com.appsnipp.loginsamples.adapter.ProjectItemClicked;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.appsnipp.loginsamples.utils.ToastUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity implements ProjectItemClicked {

    private FloatingActionButton btn_add;
    private ProjectAdapter mAdapter;
    private ArrayList<ProjectModel> mProjectModelList;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách Project");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Danh sách Project");

        getlistuser();
        setupRecyclerView();

        showLoading();

        btn_add = findViewById(R.id.btn_add_project);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ProjectActivity.this);
                View dialogViewAddProject = li.inflate(R.layout.dialog_add_project, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProjectActivity.this);
                alertDialogBuilder.setView(dialogViewAddProject);

                alertDialogBuilder.setCancelable(false).setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new ToastUtil().showToast(getApplicationContext().getString(R.string.create_project_success));
                            }
                        })
                        .setNeutralButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Toast.makeText(ProjectActivity.this, "Bạn đã HUỶ tạo project mới", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });
                alertDialogBuilder.show();
            }
        });
    }

/*    private void setupToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách Project");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
    }*/

    private void getProjectListData() {

        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);

        service.getALLProject(token)
                .enqueue(new Callback<ProjectListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProjectListResponse> call, @NonNull Response<ProjectListResponse> response) {
                progressDialog.dismiss();
                ProjectListResponse models = response.body();
                if (models != null){
                    mProjectModelList = models.getData_project();
                    mAdapter.projectModelList = mProjectModelList;
                    mAdapter.userModelDetailList=mUsermodelDetails;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProjectListResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProjectActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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
                if (models != null){
                    mUsermodelDetails = models.getData();
                    getProjectListData();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProjectActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(ProjectActivity.this);
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
        mAdapter.itemClicked=this;
    }

    @Override
    public void onItemClickedProject(int position, ProjectModel model) {

        Toast.makeText(ProjectActivity.this, model.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("projectModel", model);
        startActivity(intent);
    }


    public void addprojectClicked(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}
