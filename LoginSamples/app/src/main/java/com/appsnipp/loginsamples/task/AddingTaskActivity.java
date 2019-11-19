package com.appsnipp.loginsamples.task;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Project_model.ProjectModel;
import com.appsnipp.loginsamples.model.Task_model.TaskaddResponse;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddingTaskActivity extends AppCompatActivity  implements View.OnClickListener{
    ArrayList listUsers = new ArrayList<String>();
    private ArrayList<UserModelDetail> mUsermodelDetails;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinner;
    private int mYear, mMonth, mDay;
    private EditText task_name, task_deadline,task_detail;
    Button btnadd;
    final String[] iduser = {null};
    private ProjectModel modelproject;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_task);
        getDataIntent();
        task_name = findViewById(R.id.task_name);
        task_deadline = findViewById(R.id.task_deadline);
        task_detail = findViewById(R.id.task_detail);
        btnadd=findViewById(R.id.addtask_detail);
        task_deadline.setOnClickListener(this);

        btnadd.setOnClickListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create Task");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Create Task");
        getListUsers();
        setOptionSpinner();

    }

    private void getListUsers() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.getListUser(token)
                .enqueue(new Callback<ListUserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ListUserModel> call, @NonNull Response<ListUserModel> response) {

                        ListUserModel models = response.body();
                        if (models != null) {
                            mUsermodelDetails = models.getData();
                            int i = 0;
                            for (UserModelDetail item : mUsermodelDetails) {
                                listUsers.add(item.getName());
                            }
                            spinnerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        Toast.makeText(AddingTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setOptionSpinner() {
        spinner = findViewById(R.id.spinner_user_task_add);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUsers);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iduser[0] = mUsermodelDetails.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    @Override
    public void onClick(View v) {
        if (v == task_deadline) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            task_deadline.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }
        if (v == btnadd) {
            showLoading();
            String s = task_name.getText().toString();
            if (task_name.getText().toString().equals("") || task_deadline.getText().toString().equals("") || task_detail.getText().toString().equals("") || iduser[0].equals("")) {

                Toast.makeText(AddingTaskActivity.this, "Error Input !", Toast.LENGTH_SHORT).show();


            } else {


                String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();


                RequestAPI service = APIClient.getClient().create(RequestAPI.class);
                service.addtask(token, task_name.getText().toString(), setendate(task_deadline.getText().toString()), modelproject.getId(), task_detail.getText().toString(), iduser[0])
                        .enqueue(new Callback<TaskaddResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<TaskaddResponse> call, @NonNull Response<TaskaddResponse> response) {
                                progressDialog.dismiss();
                                TaskaddResponse models = response.body();
                                if (models != null) {

                                    Toast.makeText(AddingTaskActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (models.getSuccess() == true) {
                                        finish();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<TaskaddResponse> call, @NonNull Throwable t) {
                                Toast.makeText(AddingTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        }

    }
    private void getDataIntent() {
        Intent intent = getIntent();
        modelproject = (ProjectModel) intent.getSerializableExtra("projectModel");
    }
    private void showLoading() {
        progressDialog = new ProgressDialog(AddingTaskActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
    public void backtask_addtask(View view){
        finish();
    }
    private String setendate(String trim) {
        String []date=trim.split("-");
        String dob=date[2]+"-"+date[1]+"-"+date[0];
        return dob;
    }
}
