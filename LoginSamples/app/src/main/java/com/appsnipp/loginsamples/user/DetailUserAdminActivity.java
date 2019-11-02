package com.appsnipp.loginsamples.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.Task_model.TaskModel;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;

import java.util.Calendar;

public class DetailUserAdminActivity extends AppCompatActivity implements View.OnClickListener {
    String[] status_item = { "Inactive","Active"};
    String[] role_item = {"Admin", "User"};
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinnerstatus;
    ArrayAdapter<String> spinnerAdapterstatus;
    UserModelDetail modeluser;
    private int mYear, mMonth, mDay;
    private AppCompatTextView  tv_full_name,tv_birthday,tv_email,tv_sex,tv_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_admin);

        getDataIntent();
        getbyid();
        tv_birthday.setOnClickListener(this);
        setOptionSpinnerrole(modeluser.getRole());
        setOptionSpinnerstatus(modeluser.getStatus());
        Setdatauser();

    }

    private void Setdatauser() {
        tv_full_name.setText(modeluser.getName());
        tv_birthday.setText(modeluser.getDob());
        tv_email.setText(modeluser.getEmail());
        tv_mobile.setText(modeluser.getPhone());
        tv_sex.setText(modeluser.getSex().toString());
    }

    private void getbyid() {
        tv_full_name=findViewById(R.id.tv_full_name_detail);
        tv_birthday=findViewById(R.id.tv_birthday_detail);
        tv_email=findViewById(R.id.tv_email_detail);
        tv_mobile=findViewById(R.id.tv_mobile_detail);
        tv_sex=findViewById(R.id.tv_sex_detail);

    }


    private void setOptionSpinnerrole(int position) {
        spinner = (Spinner) findViewById(R.id.spinner_user_role);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,role_item );

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
    private void setOptionSpinnerstatus(int position) {
        spinnerstatus = (Spinner) findViewById(R.id.spinner_user_status);
        spinnerAdapterstatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,status_item );

        spinnerAdapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerstatus.setAdapter(spinnerAdapterstatus);
        spinnerstatus.setSelection(position);
        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra("usermodel");
    }

    @Override
    public void onClick(View v) {
        if (v == tv_birthday) {

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

                            tv_birthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}
