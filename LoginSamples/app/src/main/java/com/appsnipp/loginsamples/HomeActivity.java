package com.appsnipp.loginsamples;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.appsnipp.loginsamples.chat.ChatActivity;
import com.appsnipp.loginsamples.conclude.ConcludeActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance;
import com.appsnipp.loginsamples.model.Attendance.Attendance_checkout;
import com.appsnipp.loginsamples.model.Attendance.Data;
import com.appsnipp.loginsamples.model.Attendance.Datacheckout;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;
import com.appsnipp.loginsamples.personal_information.ProfileActivity;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.task.TaskActivity;
import com.appsnipp.loginsamples.user.UserActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    User userModel;
    CardView carview_checkin_out,admin_home;
    TextView tv_checkin_out;
    Data modelAttendance;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getDataIntent();
        checkAttendance();

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
//        carview_checkin_out=findViewById(R.id.carview_checkin_out);
        admin_home=findViewById(R.id.admin_home);
//        tv_checkin_out =findViewById(R.id.tv_checkin_out);
        int role = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getRole();
        if(role!=0){
            carview_checkin_out.setVisibility(View.VISIBLE);
        }else{
            admin_home.setVisibility(View.VISIBLE);
        }
        showLoading();
    }

    private void checkAttendance() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String date = dateFormatter.format(today);

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<Attendance> call = service.Attendance(token,date);
        call.enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(@NonNull Call<Attendance> call, @NonNull Response<Attendance> response) {

                progressDialog.dismiss();
                Attendance models = response.body();
                if (models != null) {
                    modelAttendance=models.getData();
                    Toast.makeText(HomeActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void checkoutAttendance() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String date = dateFormatter.format(today);

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<Attendance_checkout> call = service.Attendancecheckout(token,date);
        call.enqueue(new Callback<Attendance_checkout>() {
            @Override
            public void onResponse(@NonNull Call<Attendance_checkout> call, @NonNull Response<Attendance_checkout> response) {
                progressDialog.dismiss();
                Attendance_checkout models = response.body();
                if (models != null) {
                    Toast.makeText(HomeActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    try {
                        Gson gson = new Gson();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        Attendance_checkout model = gson.fromJson(object.toString(), Attendance_checkout.class);
                        Toast.makeText(HomeActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Attendance_checkout> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }
//    public void CheckInClicked(View view){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
//        alertDialog.setTitle("Confirm ...");
//        alertDialog.setMessage("Do you really want to check out attendance?");
//        alertDialog.setIcon(R.mipmap.ic_launcher);
//        alertDialog.setPositiveButton("YES",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        showLoading();
//                        checkoutAttendance();
//
//                    }
//                });
//        alertDialog.setNegativeButton("NO",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
//
//
//    }



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

    public void AdminClicked(View view){
        Intent intent = new Intent(this, UserActivity.class);
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
            case R.id.nav_checkout: {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Confirm ...");
                alertDialog.setMessage("You want to Check out");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

//                                Toast.makeText(HomeActivity.this, "You have been check out", Toast.LENGTH_SHORT).show();
                                showLoading();
                                checkoutAttendance();
                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

                break;
            }
            case R.id.nav_logout: {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                alertDialog.setTitle("Confirm ...");
                alertDialog.setMessage("You want to log out");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(HomeActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
                break;

            }
            default: return false;
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

    private void showLoading() {
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
}
