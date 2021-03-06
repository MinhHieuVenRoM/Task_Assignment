package com.appsnipp.loginsamples.Managetment_Task;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.adapter.TaskItemClicked;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.task.DetailTaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class TaskManagementActivity extends AppCompatActivity implements TaskItemClicked {


    CalendarView calendarView;
    ProgressDialog progressDialog;
    private TaskAdapter mAdapter;
    private String UserNameTask;
    private ArrayList<TaskModel> mDiemdanhModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);
        setupfisrtView();
        calendarView=findViewById(R.id.calenderView_taskmanagement);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                String selectedDate="Date is : " + dayOfMonth +" / " + (month+1) + " / " + year;
                Toast.makeText(TaskManagementActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
                String date=year+"-"+(month+1)+"-"+dayOfMonth;
                setupRecyclerView();

                getTaskListData(date);
                showLoading();
            }
        });
//        final int flags =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        getWindow().getDecorView().setSystemUiVisibility(flags);
    }
    private void getTaskListData(String date) {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();


        Call<TaskListResponse> call = service.gettask_date(token,date);
        ((Call) call).enqueue(new Callback<TaskListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TaskListResponse> call, @NonNull Response<TaskListResponse> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                TaskListResponse models = response.body();
                if (models != null){
                    mDiemdanhModelList = models.getDataTask();
                    mAdapter.taskModelList = mDiemdanhModelList;
                    UserNameTask= models.getDataTask().get(0).getUserDetail();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskListResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TaskManagementActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(TaskManagementActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_Task_date);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(new ArrayList<TaskModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }
    public void back_home_taskmanagetment(View view) {
        finish();
    }
    public void AttendanceClicked(View view) {
        Intent intent = new Intent(this, UserManagentTaskActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClickedTask(int position, TaskModel model) {
        Intent intent = new Intent(this, DetailTaskActivity.class);
        intent.putExtra("taskmodel", model);
        startActivity(intent);
    }
    private void setupfisrtView(){
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        setupRecyclerView();

        getTaskListData(date);
        showLoading();
    }
}
