package com.appsnipp.loginsamples.task;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsnipp.loginsamples.R;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class DetailTaskActivity extends AppCompatActivity {

    String items[]={"Open","Fixing","Fixed","Done"};
    Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chi tiết task");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Chi tiết task");

        setOptionSpinner();
    }

    private <ViewGroup> void setOptionSpinner(){
        Spinner s = (Spinner) findViewById(R.id.optionStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

}
