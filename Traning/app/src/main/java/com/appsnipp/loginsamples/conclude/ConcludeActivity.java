package com.appsnipp.loginsamples.conclude;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.adapter.SetionsPagerAdapterSummary;
import com.appsnipp.loginsamples.attendance.AttendanceActivity;
import com.google.android.material.tabs.TabLayout;

public class ConcludeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclude);
        setupViewPager();
    }
    private void setupViewPager() {
        SetionsPagerAdapterSummary sectionsPagerAdapter = new SetionsPagerAdapterSummary(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.vp_task);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tl_attendance);
        tabs.setupWithViewPager(viewPager);
    }
    public void AttendanceClicked(View view){
        Intent intent = new Intent(this, AttendanceActivity.class);
        startActivity(intent);
    }

    public void back_home_list_user_conclude(View view) {
        finish();
    }
}
