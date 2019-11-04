package com.appsnipp.loginsamples.project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
import com.appsnipp.loginsamples.adapter.ProjectItemClicked;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Project_model.ProjectAddResponse;
import com.appsnipp.loginsamples.model.Project_model.ProjectListResponse;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.task.TaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity implements ProjectItemClicked {

    private FloatingActionButton btn_add;
    private ProjectAdapter mAdapter;
    private ArrayList<ProjectModel> mProjectModelList;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    ProgressDialog progressDialog;
    private int mYear, mMonth, mDay;
    private TextView tv_enddate_addproject, tv_full_name_project_addproject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        btn_add = findViewById(R.id.btn_add_project);
        int role = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getRole();
        if(role==0){

            btn_add.setVisibility(View.VISIBLE);

        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Danh sách Project");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Danh sách Project");
        getProjectListData();

        //getlistuser();
        setupRecyclerView();

        showLoading();


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(ProjectActivity.this);
                View dialogViewAddProject = li.inflate(R.layout.dialog_add_project, null);
                tv_full_name_project_addproject = dialogViewAddProject.findViewById(R.id.tv_full_name_project_add_project);
                tv_enddate_addproject = dialogViewAddProject.findViewById(R.id.tv_end_date_add_project);

                tv_enddate_addproject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        updateDateUI(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProjectActivity.this);
                alertDialogBuilder.setView(dialogViewAddProject);
                alertDialogBuilder.setTitle(getString(R.string.creae_new_project));
                alertDialogBuilder.setPositiveButton("Create", null)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                final AlertDialog dialog = alertDialogBuilder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                if (!tv_full_name_project_addproject.getText().toString().equals("") && !tv_enddate_addproject.getText().toString().equals("")) {
                                    showLoading();
                                    add_project(tv_full_name_project_addproject.getText().toString(), tv_enddate_addproject.getText().toString(), dialog);
                                }


//                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private void updateDateUI(String dateText) {
        tv_enddate_addproject.setText(dateText);
    }

    private void getProjectListData() {

        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);

        service.getALLProject(token)
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
                        if (models != null) {
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
        mAdapter.itemClicked = this;
    }

    @Override
    public void onItemClickedProject(int position, ProjectModel model) {

        Toast.makeText(ProjectActivity.this, model.getId(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("projectModel", model);
        startActivity(intent);
    }

    private void add_project(String name, String end_Date, final AlertDialog dialog) {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.addproject(token, name, setendate(end_Date))
                .enqueue(new Callback<ProjectAddResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProjectAddResponse> call, @NonNull Response<ProjectAddResponse> response) {
                        progressDialog.dismiss();
                        ProjectAddResponse models = response.body();
                        if (models != null) {
                            Toast.makeText(ProjectActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProjectAddResponse> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProjectActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2] + "-" + date[1] + "-" + date[0];
        return dob;
    }

    public void back_home_project(View view) {

        finish();


    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        setupRecyclerView();
        getProjectListData();


        //Refresh your stuff here
    }
}
