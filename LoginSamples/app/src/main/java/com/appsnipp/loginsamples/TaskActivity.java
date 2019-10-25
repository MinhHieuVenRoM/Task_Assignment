package com.appsnipp.loginsamples;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appsnipp.loginsamples.adapter.MainItemClicked;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
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

public class TaskActivity extends AppCompatActivity  implements MainItemClicked{

    private FloatingActionButton btn_adding_task;
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

        btn_adding_task = (FloatingActionButton) findViewById(R.id.btn_add_task);
        btn_adding_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddingTaskActivity();

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TaskActivity.this);
//                alertDialog.setTitle("Xác nhận ...");
//                alertDialog.setMessage("Bạn có thực sự muốn xóa?");
//                alertDialog.setIcon(R.mipmap.ic_launcher);
//                alertDialog.setPositiveButton("YES",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(TaskActivity.this, "Bạn đã chọn YES", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                alertDialog.show();
            }
        });
    }

    public void goToAddingTaskActivity(){
        Intent intent = new Intent(TaskActivity.this, AddingTaskActivity.class);
        startActivity(intent);
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
