package com.appsnipp.loginsamples.conclude;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.appsnipp.loginsamples.Managetment_Task.TaskManagementActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.attendance.AttendanceActivity;

public class ConcludeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclude);
    }

    public void AttendanceClicked(View view){
        Intent intent = new Intent(this, AttendanceActivity.class);
        startActivity(intent);
    }


    public void Managetment_task(View view) {
        Intent intent = new Intent(this, TaskManagementActivity.class);
        startActivity(intent);
    }
}
