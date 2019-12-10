package com.appsnipp.loginsamples.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.DiemDanhAdapter;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance_List;
import com.appsnipp.loginsamples.model.Attendance.DataAttendanceRespose;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {

    CalendarView calendarView;
    ProgressDialog progressDialog;
    private DiemDanhAdapter mAdapter;
    private ArrayList<DataAttendanceRespose> mDiemdanhModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        setupfisrtView();


        calendarView = findViewById(R.id.calenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                String selectedDate = "Date is : " + dayOfMonth + " / " + (month + 1) + " / " + year;
                Toast.makeText(AttendanceActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                setupRecyclerView();

                getAttendanceListData(date);
                showLoading();
            }
        });
//        final int flags =  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private void setupfisrtView(){
        Calendar cal = Calendar.getInstance();

        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        setupRecyclerView();

        getAttendanceListData(date);
        showLoading();
}

    private void getAttendanceListData(String date) {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
       String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();


       Call<Attendance_List> call = service.Attendance_list(token,date);
        ((Call) call).enqueue(new Callback<Attendance_List>() {
            @Override
            public void onResponse(@NonNull Call<Attendance_List> call, @NonNull Response<Attendance_List> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                Attendance_List models = response.body();
                if (models != null){
                    mDiemdanhModelList = models.getData();
                    mAdapter.diemdanhArrayList = mDiemdanhModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance_List> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AttendanceActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(AttendanceActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_Diemdanh);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter= new DiemDanhAdapter(new ArrayList<DataAttendanceRespose>());
        mRecyclerView.setAdapter(mAdapter);
    }
    public void backfromAttendanceAdmin(View view) {
            finish();
    }
    public void AttendanceClicked(View view) {
        Intent intent = new Intent(this, AttendanceReportActivity.class);
        startActivity(intent);
    }

}
