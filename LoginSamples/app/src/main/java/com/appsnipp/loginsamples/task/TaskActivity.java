package com.appsnipp.loginsamples.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.adapter.TaskItemClicked;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Result;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity implements TaskItemClicked {
    private TaskAdapter mAdapter;
    private RecyclerView recyclerView;
    private TaskListResponse mTaskModelList;
    private TaskModel taskModel;
    private ProgressDialog progressDialog;
    private ProjectModel model;
    private TextView title;
    private static final int RESULT=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        title=findViewById(R.id.toolbar_title_task);
        title.setText(model.getName());
        
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);

        setupRecyclerView();

        getTaskListData();
        showLoading();

        findViewById(R.id.btn_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToAddingTaskActivity();
            }
        });
    }

    public void goToAddingTaskActivity(){

        Intent intent = new Intent(TaskActivity.this, AddingTaskActivity.class);
        intent.putExtra("projectModel", model);
        startActivity(intent);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        model = (ProjectModel) intent.getSerializableExtra("projectModel");
    }

    private void getTaskListData() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();


        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<TaskListResponse> call = service.gettaskofproject(token,model.getId());
        call.enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TaskListResponse> call, @NonNull Response<TaskListResponse> response) {
                progressDialog.dismiss();
                TaskListResponse models = response.body();
                if (models != null) {
                    mTaskModelList = models;
                    mAdapter.taskModelList = mTaskModelList.getDataTask();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskListResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(TaskActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_project);
        //mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(new ArrayList<TaskModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked=this;
    }

//    private void getTaskData(String id) {
//        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
//        Call<TaskModel> call = service.getalltaskdetail(id);
//        call.enqueue(new Callback<TaskModel>() {
//            @Override
//            public void onResponse(@NonNull Call<TaskModel> call, @NonNull Response<TaskModel> response) {
//                progressDialog.dismiss();
////                generateDataList(response.body());
//                TaskModel models = response.body();
//                if (models != null) {
//                    taskModel = models;
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<TaskModel> call, @NonNull Throwable t) {
//                progressDialog.dismiss();
//                Toast.makeText(TaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== RESULT && resultCode == Activity.RESULT_OK){
            assert data != null;
            String temp = data.getStringExtra("taskmodel");
            assert temp != null;
            if (temp.equals("what do you want")){

            }
        }
    }

    @Override
    public void onItemClickedTask(int position, TaskModel modeltask) {
        Toast.makeText(TaskActivity.this, modeltask.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailTaskActivity.class);
        intent.putExtra("taskmodel", modeltask);
        startActivityForResult(intent,RESULT);
    }

    public void backProject_Task(View view){
        finish();
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();

        setupRecyclerView();
        getTaskListData();


        //Refresh your stuff here
    }
}
