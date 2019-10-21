package com.appsnipp.loginsamples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appsnipp.loginsamples.adapter.MainItemClicked;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.model.APIClient;
import com.appsnipp.loginsamples.model.ProjectModel;
import com.appsnipp.loginsamples.model.RequestAPI;
import com.appsnipp.loginsamples.model.TaskModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity  implements MainItemClicked{

    private TaskAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private ArrayList<TaskModel> mTaskModelList;
    ProgressDialog progressDialog;
    AppCompatTextView mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách TASK");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);

        setupRecyclerView();

        getTaskListData();
        showLoading();
    }

    private void getTaskListData() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<ArrayList<TaskModel>> call = service.getalltask();
        call.enqueue(new Callback<ArrayList<TaskModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TaskModel>> call, Response<ArrayList<TaskModel>> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                ArrayList<TaskModel> models = response.body();
                if (models != null){
                    mTaskModelList = models;
                    mAdapter.taskModelList = mTaskModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TaskModel>> call, Throwable t) {
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
        mRecyclerView = findViewById(R.id.rv_project);
        //mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(new ArrayList<TaskModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked=this;
    }

    @Override
    public void onItemClicked(int position) {

    }
}
