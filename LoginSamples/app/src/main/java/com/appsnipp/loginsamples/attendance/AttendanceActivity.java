package com.appsnipp.loginsamples.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.DiemDanhAdapter;
import com.appsnipp.loginsamples.adapter.ProjectAdapter;
import com.appsnipp.loginsamples.model.APIClient;
import com.appsnipp.loginsamples.model.DIEMDANH;
import com.appsnipp.loginsamples.model.ProjectModel;
import com.appsnipp.loginsamples.model.RequestAPI;
import com.appsnipp.loginsamples.project.ProjectActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {

    CalendarView calendarView;
    ProgressDialog progressDialog;
    private DiemDanhAdapter mAdapter;
    private ArrayList<DIEMDANH> mDiemdanhModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        calendarView=findViewById(R.id.calenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                String selectedDate="Date is : " + dayOfMonth +" / " + (month+1) + " / " + year;
                Toast.makeText(AttendanceActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
                setupRecyclerView();

                getProjectListData();
                showLoading();
            }
        });
    }
    private void getProjectListData() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<ArrayList<DIEMDANH>> call = service.getalldiemdanh();
        call.enqueue(new Callback<ArrayList<DIEMDANH>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DIEMDANH>> call, @NonNull Response<ArrayList<DIEMDANH>> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                ArrayList<DIEMDANH> models = response.body();
                if (models != null){
                    mDiemdanhModelList = models;
                    mAdapter.diemdanhArrayList = mDiemdanhModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DIEMDANH>> call, @NonNull Throwable t) {
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
        mAdapter= new DiemDanhAdapter(new ArrayList<DIEMDANH>());
        mRecyclerView.setAdapter(mAdapter);
    }
}
