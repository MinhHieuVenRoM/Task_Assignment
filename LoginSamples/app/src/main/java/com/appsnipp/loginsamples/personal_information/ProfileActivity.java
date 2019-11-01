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
        tv_userrole.setText(modeluser.getRole().toString());
        tv_email.setText(modeluser.getEmail());
        tv_sex.setText(modeluser.getSex().toString());
        tv_birthday.setText(modeluser.getDob());
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
}
