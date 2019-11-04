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
    String[] status_item = {"Active", "Inactive"};
    String[] role_item = {"Admin", "User"};
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinnerstatus;
    ArrayAdapter<String> spinnerAdapterstatus;
    UserModelDetail modeluser;
    private int mYear, mMonth, mDay;
    private AppCompatTextView  tv_full_name,tv_birthday,tv_email,tv_sex,tv_mobile,tv_role_detail_admin,tv_detail_status_admin,tv_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_admin);

        getDataIntent();
        getbyid();
        tv_birthday.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        Setdatauser();

    }

    private void Setdatauser() {
        tv_full_name.setText(modeluser.getName());
        tv_birthday.setText(getdate(modeluser.getDob()));
        tv_email.setText(modeluser.getEmail());
        tv_mobile.setText(modeluser.getPhone());
        if(modeluser.getSex()==0){
            tv_sex.setText("Male");
        }else {
            tv_sex.setText("Female");
        }
        if(modeluser.getRole()==0){
            tv_role_detail_admin.setText("Admin");
        }else {
            tv_role_detail_admin.setText("User");
        }
        if(modeluser.getStatus()==1){
            tv_detail_status_admin.setText("Active");
        }else {
            tv_detail_status_admin.setText("InActive");
        }

    }

    private void getbyid() {
        tv_full_name=findViewById(R.id.tv_full_name_detail_admin);
        tv_birthday=findViewById(R.id.tv_birthday_detail_admin);
        tv_email=findViewById(R.id.tv_email_detail_admin);
        tv_mobile=findViewById(R.id.tv_mobile_detail_admin);
        tv_sex=findViewById(R.id.tv_sex_detail);
        tv_role_detail_admin=findViewById(R.id.tv_role_detail_admin);
        tv_detail_status_admin=findViewById(R.id.tv_detail_status_admin_);
        tv_edit=findViewById(R.id.tv_edit_admin);
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
        if(v==tv_edit){

            Intent intent = new Intent(this, EditUserAdminActivity.class);
            intent.putExtra("usermodel", modeluser);
            startActivity(intent);


        }
    }
    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0,2) + "-" + date[1] + "-" + date[0];
        return dob;
    }
}
