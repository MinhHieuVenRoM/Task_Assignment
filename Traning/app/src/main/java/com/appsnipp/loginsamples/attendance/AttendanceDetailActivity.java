package com.appsnipp.loginsamples.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.AttendanceAdapter;
import com.appsnipp.loginsamples.adapter.DiemDanhAdapter;
import com.appsnipp.loginsamples.chat.ChatActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance_List;
import com.appsnipp.loginsamples.model.Attendance.Attendance_list_detail;
import com.appsnipp.loginsamples.model.Attendance.DataAttendanceRespose;
import com.appsnipp.loginsamples.model.Attendance.DataDetailAttendance;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.task.DetailTaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;

public class AttendanceDetailActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private AttendanceAdapter mAdapter;
    private ArrayList<DataDetailAttendance> mDiemdanhModelList;
    UserModelDetail modeluser;
    Spinner spinnerMonth;
    ArrayAdapter<String> spinnerAdaptermonth;
    Spinner spinnerYear;
    ArrayAdapter<String> spinnerAdapteryear;
    String[] month = {null};
    String[] year = {null};
    private int month_id=0;
    private int year_id=0;
    private int start=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);

        getDataIntent();
        setupRecyclerView();
        showLoading();
        getAttendanceListData(modeluser.getId());

        setOptionSpinner();


    }
    private void setOptionSpinner() {
        month= new String[]{"   ALL   ","    1    ", "   2   ", "   3   ", "   4   ", "   5   ", "   6   ", "   7   ", "   8   ", "   9   ", "   10   ", "   11   ", "   12   "};
        year= new String[]{"   ALL   ","   2019   ", "   2020   ", "   2021   ", "   2022   ", "   2023   ", "   2024   ", "    2025   ", "   2026   ", "   2027   ", "   2028   ", "   2029   ", "   2030   "};
        //thang
        spinnerAdaptermonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        spinnerAdaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth = findViewById(R.id.spinner_thang);
        spinnerMonth.setAdapter(spinnerAdaptermonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month_id=position;
                if(year_id!=0 && month_id!=0){
                    showLoading();
                    getAttendanceListDataMonth(modeluser.getId(),month_id,year_id);
                    start=1;
                }
                if(year_id!=0&& month_id==0){
                    showLoading();
                    getAttendanceListDataYear(modeluser.getId(),year_id);
                }
                if(year_id==0 && month_id==0&&start!=0){
                    showLoading();
                    getAttendanceListData(modeluser.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //nam
        spinnerAdapteryear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
        spinnerAdapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear = findViewById(R.id.spinner_nam);
        spinnerYear.setAdapter(spinnerAdapteryear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_id=position;
                if(year_id!=0 && month_id!=0){
                    showLoading();
                    getAttendanceListDataMonth(modeluser.getId(),month_id,year_id);
                    start=1;
                }
                if(year_id!=0&& month_id==0){
                    showLoading();
                    getAttendanceListDataYear(modeluser.getId(),year_id);
                }
                if(year_id==0 && month_id==0&&start!=0){
                    showLoading();
                    getAttendanceListData(modeluser.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getAttendanceListData(String id) {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();


        Call<Attendance_list_detail> call = service.Attendance_list_detail(token,id);
        ((Call) call).enqueue(new Callback<Attendance_list_detail>() {
            @Override
            public void onResponse(@NonNull Call<Attendance_list_detail> call, @NonNull Response<Attendance_list_detail> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                Attendance_list_detail models = response.body();
                if (models != null){
                    mDiemdanhModelList = models.getData();
                    mAdapter.diemdanhArrayList = mDiemdanhModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance_list_detail> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AttendanceDetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAttendanceListDataMonth(String id,int month,int year) {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
        String date=(year+2018)+"-"+(month)+"-1";

        Call<Attendance_list_detail> call = service.Attendance_list_detail_user_month(token,id,date);
        ((Call) call).enqueue(new Callback<Attendance_list_detail>() {
            @Override
            public void onResponse(@NonNull Call<Attendance_list_detail> call, @NonNull Response<Attendance_list_detail> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                Attendance_list_detail models = response.body();
                if (models != null){
                    mDiemdanhModelList = models.getData();
                    mAdapter.diemdanhArrayList = mDiemdanhModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance_list_detail> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AttendanceDetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getAttendanceListDataYear(String id,int year) {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
        String date=(year+2018)+"-1-1";

        Call<Attendance_list_detail> call = service.Attendance_list_detail_user_year(token,id,date);
        ((Call) call).enqueue(new Callback<Attendance_list_detail>() {
            @Override
            public void onResponse(@NonNull Call<Attendance_list_detail> call, @NonNull Response<Attendance_list_detail> response) {
                progressDialog.dismiss();
//                generateDataList(response.body());
                Attendance_list_detail models = response.body();
                if (models != null){
                    mDiemdanhModelList = models.getData();
                    mAdapter.diemdanhArrayList = mDiemdanhModelList;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance_list_detail> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AttendanceDetailActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showLoading() {
        progressDialog = new ProgressDialog(AttendanceDetailActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_Diemdanh_listuser_detail);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter= new AttendanceAdapter(new ArrayList<DataDetailAttendance>());
        mRecyclerView.setAdapter(mAdapter);
    }
    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra("usermodel");
    }
    public void back_attendance_report(View view){
      finish();
    }

}
