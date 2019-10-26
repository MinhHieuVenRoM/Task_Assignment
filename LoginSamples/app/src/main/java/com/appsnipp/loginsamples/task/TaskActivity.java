package com.appsnipp.loginsamples.task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.model.APIClient;
import com.appsnipp.loginsamples.model.ProjectModel;
import com.appsnipp.loginsamples.model.RequestAPI;
import com.appsnipp.loginsamples.model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private FloatingActionButton btn_adding_task;
    private TaskAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<TaskModel> mTaskModelList;
    ProgressDialog progressDialog;
    private ProjectModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách task trong"+model.getName());
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);

        setupRecyclerView();

        getTaskListData();
        showLoading();

        btn_adding_task = (FloatingActionButton) findViewById(R.id.btn_add_task);
        btn_adding_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddingTaskActivity();
            }
        });
    }

    public void goToAddingTaskActivity(){
        Intent intent = new Intent(TaskActivity.this, AddingTaskActivity.class);
        startActivity(intent);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        model = (ProjectModel) intent.getSerializableExtra("projectModel");
    }

    private void getTaskListData() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<ArrayList<TaskModel>> call = service.getalltask();
        call.enqueue(new Callback<ArrayList<TaskModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<TaskModel>> call, @NonNull Response<ArrayList<TaskModel>> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                ArrayList<TaskModel> models = response.body();
                if (models != null) {
                    mTaskModelList = models;
                    mAdapter.taskModelList = mTaskModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<TaskModel>> call, @NonNull Throwable t) {
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

    }


}
