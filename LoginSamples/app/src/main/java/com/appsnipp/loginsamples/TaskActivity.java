package com.appsnipp.loginsamples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appsnipp.loginsamples.adapter.MainItemClicked;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.model.APIClient;
import com.appsnipp.loginsamples.model.ProjectModel;
import com.appsnipp.loginsamples.model.RequestAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity implements MainItemClicked {

    private TaskAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private ArrayList<ProjectModel> mProjectModelList;
    ProgressDialog progressDialog;
    AppCompatTextView mPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = findViewById(R.id.toolbar_project);
        toolbar.setTitle("Danh sách Project");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);

        setupRecyclerView();

        getProjectListData();
        showLoading();
    }

    private void getProjectListData() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<ArrayList<ProjectModel>> call = service.getALLProject();
        call.enqueue(new Callback<ArrayList<ProjectModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProjectModel>> call, Response<ArrayList<ProjectModel>> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                ArrayList<ProjectModel> models = response.body();
                if (models != null){
                    mProjectModelList = models;
                    mAdapter.projectModelList = mProjectModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProjectModel>> call, Throwable t) {
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
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(new ArrayList<ProjectModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked=this;
    }

    @Override
    public void onItemClicked(int position) {

    }
}
