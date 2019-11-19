package com.appsnipp.loginsamples.Managetment_Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.ManagementUserAdapter;
import com.appsnipp.loginsamples.adapter.ManagementUserItemClicked;
import com.appsnipp.loginsamples.attendance.AttendanceDetailActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagentTaskActivity extends AppCompatActivity implements ManagementUserItemClicked {

    ProgressDialog progressDialog;
    UserModelDetail modeluser;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    private ManagementUserAdapter mAdapter;
    ArrayList listUsers = new ArrayList<String>();
    Spinner spinnerUser;
    ArrayAdapter<String> spinnerAdapteruser;
    final String[] iduser = {null};
    final String[] nameuser = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getlistuser();
        setupRecyclerView();

        showLoading();
        getlistuserspinner();
        setOptionSpinner();
    }

    private void setOptionSpinner() {

        spinnerAdapteruser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUsers);
        spinnerAdapteruser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUser = findViewById(R.id.spinnerten_attendance);
        spinnerUser.setAdapter(spinnerAdapteruser);


        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iduser[0] = mUsermodelDetails.get(position).getId();
                nameuser[0] = mUsermodelDetails.get(position).getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getlistuserspinner() {
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
                            int i = 0;
                            for (UserModelDetail item : mUsermodelDetails) {
                                listUsers.add(item.getName());

                            }
                            spinnerAdapteruser.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        Toast.makeText(UserManagentTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setupRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.rv_Diemdanh_listuser);
        mRecyclerView.hasFixedSize();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ManagementUserAdapter(new ArrayList<UserModelDetail>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.itemClicked = this;
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
                            mAdapter.userModelList = mUsermodelDetails;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(UserManagentTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(UserManagentTaskActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    @Override
    public void onItemClickeduser(int position, UserModelDetail model) {
        Toast.makeText(UserManagentTaskActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProjectManagetmentActivity.class);
        intent.putExtra("usermodel", model);
        startActivity(intent);
    }
    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        setupRecyclerView();
        getlistuser();


        //Refresh your stuff here
    }

    public void back_attendance(View view) {
        finish();
    }
}
