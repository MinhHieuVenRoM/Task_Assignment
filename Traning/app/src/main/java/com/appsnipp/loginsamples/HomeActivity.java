package com.appsnipp.loginsamples;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.appsnipp.loginsamples.attendance.AttendanceDetailActivity;
import com.appsnipp.loginsamples.chat.ChatUserActivity;
import com.appsnipp.loginsamples.conclude.ConcludeActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Attendance.Attendance;
import com.appsnipp.loginsamples.model.Attendance.Attendance_checkout;
import com.appsnipp.loginsamples.model.Attendance.Check;
import com.appsnipp.loginsamples.model.Attendance.Data;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.personal_information.ProfileActivity;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.user.UserActivity;
import com.appsnipp.loginsamples.utils.LoginUser;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    User userModel;
    UserModelDetail modeluser;
    CardView car_summary,admin_home;
    TextView tv_username_main,tv_gmail_main;
    Data modelAttendance;
    private ProgressDialog progressDialog;
    private ArrayList<UserModelDetail> mUsermodelDetails;
    public static final String USER_MODEL_KEY = "user_model";
    public static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";
    public static final String USER_PASSWORD_KEY = "USER_PASSWORD_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkLogin();

        getDataIntent();
        checkInUser();


        getprofileuser();
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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tv_username_main=header.findViewById(R.id.tv_username_main);
        tv_username_main.setText(userModel.getName());
        tv_gmail_main=header.findViewById(R.id.tv_gmail_main);
        tv_gmail_main.setText(userModel.getEmail());
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.whiteTextColor));
        car_summary=findViewById(R.id.car_summary);
        admin_home=findViewById(R.id.admin_home);
//        tv_checkin_out =findViewById(R.id.tv_checkin_out);
        int role = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getRole();
        if (role == 0) {
            admin_home.setVisibility(View.VISIBLE);
            car_summary.setVisibility(View.VISIBLE);
        } else {
//            carview_checkin_out.setVisibility(View.VISIBLE);
        }
        showLoading();


        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        final WifiManager manager = (WifiManager) super.getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);

        WifiManager manager1 = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager1.getConnectionInfo();
        String address1 = info.getMacAddress();
    }

    private void checkLogin() {
        String email = SharedPrefs.getInstance().get(USER_EMAIL_KEY, String.class);
        String password = SharedPrefs.getInstance().get(USER_PASSWORD_KEY, String.class);
        if (email.isEmpty() || password.isEmpty()){
            goToLogin();
        }else {
            new LoginUser(this).login(email, password, false);
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showdialogCheckin(){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Confirm ...");
        alertDialog.setMessage("You want to check in");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checkAttendance();

                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    private void checkAttendance() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
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

    private void getprofileuser() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
        String email = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getEmail();
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.viewprofile(token,email)
                .enqueue(new Callback<ListUserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ListUserModel> call, @NonNull Response<ListUserModel> response) {
                        progressDialog.dismiss();
                        ListUserModel models = response.body();
                        if (models != null) {
                            mUsermodelDetails = models.getData();
                            // Toast.makeText(ProfileActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            modeluser = mUsermodelDetails.get(0);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkoutAttendance() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
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
                        if(!model.getMessage().equals("user already checkout")){
                            showdialogCheckin();

                        }

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
    private void checkInUser() {
        String token = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class).getToken();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String date = dateFormatter.format(today);

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<Check> call = service.checkin(token,date);
        call.enqueue(new Callback<Check>() {
            @Override
            public void onResponse(@NonNull Call<Check> call, @NonNull Response<Check> response) {
                progressDialog.dismiss();
                Check models = response.body();
                if (models != null && !models.getMessage().toString().equals("True")) {
                        showdialogCheckin();

                }else {
                    Toast.makeText(HomeActivity.this,"user already checkin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Check> call, @NonNull Throwable t) {
                Toast.makeText(HomeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void chatClicked(View view){
        Intent intent = new Intent(this, ChatUserActivity.class);
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
            case R.id.nav_attendance: {
                Intent intent = new Intent(HomeActivity.this, AttendanceDetailActivity.class);
                intent.putExtra("usermodel", (Serializable) modeluser);
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
                                SharedPrefs.getInstance().clear();
                                Toast.makeText(HomeActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
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
        userModel = SharedPrefs.getInstance().get(USER_MODEL_KEY, User.class);
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
}
