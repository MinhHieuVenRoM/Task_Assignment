package com.appsnipp.loginsamples.personal_information;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.User_model.User;

public class ProfileActivity extends AppCompatActivity {
    User modeluser;

    private TextView tv_full_name, tv_username,tv_email,tv_userrole,tv_sex,tv_birthday,tv_mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBinding();
        getDataIntent();
        getdatauser();
    }

    private void getdatauser() {
        tv_full_name.setText(modeluser.getName());

        if(modeluser.getRole()==0)
        {
            tv_userrole.setText("Admin");
        }else {
            tv_userrole.setText("User");
        }
        tv_email.setText(modeluser.getEmail());

        if(modeluser.getSex()==0)
        {

            tv_sex.setText("Male");
        }else {

            tv_sex.setText("Female");
        }
        tv_birthday.setText(setendate(modeluser.getDob()));
        tv_mobile.setText((modeluser.getPhone()));

    }

    private void setupBinding() {
        tv_full_name=findViewById(R.id.tv_full_name);
        tv_userrole=findViewById(R.id.tv_userrole);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_sex=findViewById(R.id.tv_sex);
        tv_birthday=findViewById(R.id.tv_birthday);


    }

    private void getDataIntent() {
        Intent intent = getIntent();
        modeluser = (User) intent.getSerializableExtra("usermodel");
    }
    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2] + "-" + date[1] + "-" + date[0];
        return dob;
    }
}
