package com.appsnipp.loginsamples;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.appsnipp.loginsamples.chat.ChatActivity;
import com.appsnipp.loginsamples.conclude.ConcludeActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.personal_information.ProfileActivity;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getDataIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        toolbar.setNavigationIcon(R.drawable.ic_list_shit_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));

    }
    public void chatClicked(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
    public void ProjectClicked(View view){
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
    }
    public void ConcludeClicked(View view){
        Intent intent = new Intent(this, ConcludeActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_profile: {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("usermodel", (Serializable) userModel);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getDataIntent() {
//        Intent intent = getIntent();
//        userModel = (User) intent.getSerializableExtra("usermodel");
        userModel = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class);
    }
}
