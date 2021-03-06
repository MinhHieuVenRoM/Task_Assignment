package com.appsnipp.loginsamples.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.adapter.TaskItemClicked;
import com.appsnipp.loginsamples.adapter.TaskbyUserAdapter;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.Project_model.Project_edit_model;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class TaskActivity extends AppCompatActivity implements TaskItemClicked {
    private TaskAdapter mAdapter;
    private TaskbyUserAdapter mAdapteruser;
    private RecyclerView recyclerView;
    private TaskListResponse mTaskModelList;
    private TaskModel taskModel;
    private ProgressDialog progressDialog;
    private ProjectModel model;
    private TextView title;
    private static final int RESULT = 1;
    private TextInputEditText tv_enddate_addproject, tv_full_name_project_addproject;
    private Spinner spinner;
    String[] status_item = {"OPEN", "DONE"};
    private int mYear, mMonth, mDay;
    ArrayAdapter<String> spinnerAdapter;
    int status_id = 0;
    private SwipeRefreshLayout swipeContainer;


    public TaskActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        getDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        title = findViewById(R.id.toolbar_title_task);
        title.setText(model.getName());

        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);


        final int role = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getRole();

        setupRecyclerView();
        getTaskListData();


        showLoading();
        AppCompatImageView img_editproject = findViewById(R.id.img_editproject);
        FloatingActionButton btn_add_task = findViewById(R.id.btn_add_task);
        if (role == 0 && model.getStatus() == 0) {

            img_editproject.setVisibility(View.VISIBLE);
            btn_add_task.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btn_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goToAddingTaskActivity();
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoading();
                getTaskListData();
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void goToAddingTaskActivity() {

        Intent intent = new Intent(TaskActivity.this, AddingTaskActivity.class);
        intent.putExtra("projectModel", model);


        startActivity(intent);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        model = (ProjectModel) intent.getSerializableExtra("projectModel");
    }

    /*    private void getTaskListDatauser() {
            String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
            String id = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getId();

            RequestAPI service = APIClient.getClient().create(RequestAPI.class);
            Call<TaskListResponse> call = service.getTasktofUser(token,id, model.getId());
            call.enqueue(new Callback<TaskListResponse>() {
                @Override
                public void onResponse(@NonNull Call<TaskListResponse> call, @NonNull Response<TaskListResponse> response) {
                    progressDialog.dismiss();
                    TaskListResponse models = response.body();
                    if (models != null) {
                        mTaskModelList = models;
                        mAdapteruser.taskModelList = mTaskModelList.getDataTask();
                        mAdapteruser.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TaskListResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(TaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    private void getTaskListData() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();


        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<TaskListResponse> call = service.gettaskofproject(token, model.getId());
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
        mAdapter.itemClicked = this;
    }

    private void setupRecyclerViewuser() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_project);
        String name = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getName();

        //mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapteruser = new TaskbyUserAdapter(new ArrayList<TaskModel>(), name);
        mRecyclerView.setAdapter(mAdapteruser);
        mAdapteruser.itemClicked = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT && resultCode == Activity.RESULT_OK) {
            assert data != null;
            String temp = data.getStringExtra("taskmodel");
            assert temp != null;
            if (temp.equals("what do you want")) {

            }
        }
    }

    @Override
    public void onItemClickedTask(int position, TaskModel modeltask) {
        Toast.makeText(TaskActivity.this, modeltask.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DetailTaskActivity.class);
        intent.putExtra("taskmodel", modeltask);
        intent.putExtra("UserNameOfTask", modeltask.getUserDetail());
        startActivityForResult(intent, RESULT);
    }

    public void backProject_Task(View view) {
        finish();
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        title.setText(model.getName());
//        setupRecyclerView();
        getTaskListData();


        //Refresh your stuff here
    }

    public void Editproject(View view) {

        LayoutInflater inflater = LayoutInflater.from(TaskActivity.this);
        View dialogVieEditProject = inflater.inflate(R.layout.dialog_edit_project, null);
        tv_full_name_project_addproject = dialogVieEditProject.findViewById(R.id.tv_full_name_project_edit_project);
        tv_enddate_addproject = dialogVieEditProject.findViewById(R.id.tv_end_date_edit_project);
        status_id = model.getStatus();
        tv_full_name_project_addproject.setText(model.getName());
        tv_enddate_addproject.setText(getdate(model.getEndDate()));

        spinner = dialogVieEditProject.findViewById(R.id.spinner_status_edit_project);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(model.getStatus());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tv_enddate_addproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                tv_enddate_addproject.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskActivity.this);
        alertDialogBuilder.setView(dialogVieEditProject);
        alertDialogBuilder.setTitle("Edit Project" + model.getName().toString());
        alertDialogBuilder.setPositiveButton("Edit", null)
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
                            edit_project(tv_full_name_project_addproject.getText().toString(), tv_enddate_addproject.getText().toString(), status_id, dialog);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void edit_project(final String name, final String end_Date, int status_id, final AlertDialog dialog) {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.editproject(token, model.getId(), name, setendate(end_Date), status_id)
                .enqueue(new Callback<Project_edit_model>() {
                    @Override
                    public void onResponse(@NonNull Call<Project_edit_model> call, @NonNull Response<Project_edit_model> response) {
                        progressDialog.dismiss();
                        Project_edit_model models = response.body();
                        if (models != null) {
                            Toast.makeText(TaskActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            model.setName(name);

                            model.setEndDate(models.getDataProject().getEndDate());

                            onResume();
                        } else {
                            Toast.makeText(TaskActivity.this, "Error edit project", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Project_edit_model> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(TaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2] + "-" + date[1] + "-" + date[0];
        return dob;
    }

    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0, 2) + "-" + date[1] + "-" + date[0];
        return dob;
    }

    public void back_project_task(View view) {

        finish();
    }


}

