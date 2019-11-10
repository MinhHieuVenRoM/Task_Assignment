package com.appsnipp.loginsamples.personal_information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private AppCompatTextView tv_userrole_edituser,tv_birthday_edit_user,tv_email_edit_user,tv_full_name_user_edit;
    private EditText et_mobile_edit_user;
    private int mYear, mMonth, mDay;
    User modeluser;
    String[] sex_item = {"Male", "Female"};
    Spinner spinnersex;
    ArrayAdapter<String> spinnerAdaptersex;
    private int sex_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        getbyid();
        getDataIntent();
        Setdatauser();
        setOptionSpinnerSex(modeluser.getSex());
        tv_birthday_edit_user.setOnClickListener(this);

    }

    private void getbyid() {
        tv_full_name_user_edit=findViewById(R.id.tv_full_name_edit);
        tv_userrole_edituser=findViewById(R.id.tv_userrole_edituser);
        tv_birthday_edit_user=findViewById(R.id.tv_birthday_edit_user);
        tv_email_edit_user=findViewById(R.id.tv_email_edit_user);
        et_mobile_edit_user=findViewById(R.id.et_mobile_edit_user);
        spinnersex=findViewById(R.id.spinner_user_sex_edit);
    }
    private void Setdatauser() {
        tv_full_name_user_edit.setText(modeluser.getName());
        tv_birthday_edit_user.setText(getdate(modeluser.getDob()));
        tv_email_edit_user.setText(modeluser.getEmail());
        et_mobile_edit_user.setText(modeluser.getPhone());

        if(modeluser.getRole()==0){
            tv_userrole_edituser.setText("Admin");
        }else {
            tv_userrole_edituser.setText("User");
        }
    }

    private void setOptionSpinnerSex(int position) {
        spinnerAdaptersex = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sex_item);

        spinnerAdaptersex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnersex.setAdapter(spinnerAdaptersex);
        spinnersex.setSelection(position);
        spinnersex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getDataIntent() {
        Intent intent = getIntent();
        modeluser = (User) intent.getSerializableExtra("usermodel");
    }

    @Override
    public void onClick(View v) {
        if (v == tv_birthday_edit_user) {

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

                            tv_birthday_edit_user.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


        }

    }
    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0,2) + "-" + date[1] + "-" + date[0];
        return dob;
    }
}
