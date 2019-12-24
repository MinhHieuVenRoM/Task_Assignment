package com.appsnipp.loginsamples.conclude;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.Managetment_Task.TaskManagementActivity;
import com.appsnipp.loginsamples.Managetment_Task.UserManagentTaskActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.TaskAdapter;
import com.appsnipp.loginsamples.adapter.TaskItemClicked;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Task_model.TaskListResponse;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.task.DetailTaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class SummaryTaskFragment extends Fragment implements TaskItemClicked {

    private View view;
    CalendarView calendarView;
    ProgressDialog progressDialog;
    private TaskAdapter mAdapter;
    private ArrayList<TaskModel> mDiemdanhModelList;
    private FloatingActionButton fab_detail_task_admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmenttask, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setupfisrtView();
        fab_detail_task_admin=view.findViewById(R.id.fab_detail_task_admin);
        fab_detail_task_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserManagentTaskActivity.class);
                startActivity(intent);
            }
        });
        calendarView=view.findViewById(R.id.calenderView_taskmanagement);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                String selectedDate="Date is : " + dayOfMonth +" / " + (month+1) + " / " + year;
                Toast.makeText(view.getContext(), selectedDate, Toast.LENGTH_SHORT).show();
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
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaskListResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_Task_date);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new TaskAdapter(new ArrayList<TaskModel>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
    }

    @Override
    public void onItemClickedTask(int position, TaskModel model) {
        Intent intent = new Intent(view.getContext(), DetailTaskActivity.class);
        intent.putExtra("taskmodel", model);
        intent.putExtra("UserNameOfTask", model.getUserDetail());
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
