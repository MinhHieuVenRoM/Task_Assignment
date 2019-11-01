package com.appsnipp.loginsamples.task;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTaskActivity extends AppCompatActivity  implements View.OnClickListener {

    String[] status_item = {"Open", "Fixing", "Fixed", "Done"};
    ArrayList listUsers = new ArrayList<String>();
    private TaskModel model;
    private EditText tv_nguoiduocgiaotask, et_task_detail;
    private AppCompatTextView tv_hancuoitask;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    Spinner spinner;
    private int mYear, mMonth, mDay;


    ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);

        getDataIntent();
        tv_nguoiduocgiaotask = findViewById(R.id.et_nguoiduocgiaotask);
        tv_nguoiduocgiaotask.setText(model.getUserDetail());
        tv_hancuoitask = findViewById(R.id.tv_hancuoitask);
        String deadline= model.getEndDate().substring(0, 10);
        deadline=deadline.replace("-","/");
        tv_hancuoitask.setText(deadline);
        et_task_detail = findViewById(R.id.et_task_detail);
        et_task_detail.setText(model.getContent());
        tv_hancuoitask.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Chi tiết task");

        setOptionSpinner(model.getStatus());
        getlistuser();


    }

    private void findViewByIds() {
        //TODO add find view by id
    }

    private void getDataIntent() {

        Intent intent = getIntent();
        model = (TaskModel) intent.getSerializableExtra("taskmodel");
    }

    private void setOptionSpinner(int position) {
        spinner = (Spinner) findViewById(R.id.optionStatus);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,status_item );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        Toast.makeText(DetailTaskActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickedAssigneeTask(View view) {
        final String[] iduser = {null};
        final String[] nameuser = {null};

        ArrayAdapter<String> spinnerAdapteruser;
        Spinner spinnerUser;

        LayoutInflater inflater = LayoutInflater.from(DetailTaskActivity.this);
        View customDialogView = inflater.inflate(R.layout.popup_listuser, null);

        spinnerAdapteruser = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listUsers);
        spinnerAdapteruser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUser = customDialogView.findViewById(R.id.spinner_user_task);
        spinnerUser.setAdapter(spinnerAdapteruser);
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iduser[0] = mUsermodelDetails.get(position).getId();
                nameuser[0]=mUsermodelDetails.get(position).getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailTaskActivity.this);
        alertDialogBuilder.setView(customDialogView);


        alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        tv_nguoiduocgiaotask.setText( nameuser[0]);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v == tv_hancuoitask) {

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

                            tv_hancuoitask.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    }
    public void backtask_detailtask(View view){
        finish();
    }
}

