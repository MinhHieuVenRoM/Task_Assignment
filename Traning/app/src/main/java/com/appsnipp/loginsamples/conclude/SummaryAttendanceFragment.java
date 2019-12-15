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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.DiemDanhAdapter;
import com.appsnipp.loginsamples.attendance.AttendanceReportActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance_List;
import com.appsnipp.loginsamples.model.Attendance.DataAttendanceRespose;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;


public class SummaryAttendanceFragment extends Fragment {
    private View view;
    CalendarView calendarView;
    ProgressDialog progressDialog;
    private DiemDanhAdapter mAdapter;
    private ArrayList<DataAttendanceRespose> mDiemdanhModelList;
    private FloatingActionButton detail_attendance_admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        setupfisrtView();

        detail_attendance_admin=view.findViewById(R.id.detail_attendance_admin);
        detail_attendance_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AttendanceReportActivity.class);
                startActivity(intent);
            }
        });
        calendarView = view.findViewById(R.id.calenderView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                String selectedDate = "Date is : " + dayOfMonth + " / " + (month + 1) + " / " + year;
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
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();


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
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void setupRecyclerView() {
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_Diemdanh);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter= new DiemDanhAdapter(new ArrayList<DataAttendanceRespose>());
        mRecyclerView.setAdapter(mAdapter);
    }


}
