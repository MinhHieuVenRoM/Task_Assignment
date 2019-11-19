package com.appsnipp.loginsamples.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.user.UserActivity.POSITION_LIST_USER_KEY;
import static com.appsnipp.loginsamples.user.UserActivity.UPDATE_USER_MODEL_KEY;
import static com.appsnipp.loginsamples.user.UserActivity.USER_MODEL_KEY;

public class DetailUserAdminActivity extends AppCompatActivity implements View.OnClickListener {
    String[] status_item = {"Active", "Inactive"};
    String[] role_item = {"Admin", "User"};
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinnerstatus;
    ArrayAdapter<String> spinnerAdapterstatus;
    UserModelDetail modeluser;
    private int mYear, mMonth, mDay;
    private AppCompatTextView tv_full_name, tv_birthday, tv_email, tv_sex, tv_mobile, tv_role_detail_admin, tv_detail_status_admin, tv_edit, tv_reset;
    private ArrayList<UserModelDetail> mUsermodelDetails;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_admin);
        getbyid();
        tv_birthday.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        getDataIntent();
        showLoading();
        getprofileuser();

    }
    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra(USER_MODEL_KEY);
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(DetailUserAdminActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void Setdatauser() {
        tv_full_name.setText(modeluser.getName());
        tv_birthday.setText(getdate(modeluser.getDob()));
        tv_email.setText(modeluser.getEmail());
        tv_mobile.setText(modeluser.getPhone());
        if (modeluser.getSex() == 0) {
            tv_sex.setText("Male");
        } else {
            tv_sex.setText("Female");
        }
        if (modeluser.getRole() == 0) {
            tv_role_detail_admin.setText("Admin");
        } else {
            tv_role_detail_admin.setText("User");
        }
        if (modeluser.getStatus() == 1) {
            tv_detail_status_admin.setText("Active");
        } else {
            tv_detail_status_admin.setText("InActive");
        }

    }

    private void getbyid() {
        tv_full_name = findViewById(R.id.tv_full_name_detail_admin);
        tv_birthday = findViewById(R.id.tv_birthday_detail_admin);
        tv_email = findViewById(R.id.tv_email_detail_admin);
        tv_mobile = findViewById(R.id.tv_mobile_detail_admin);
        tv_sex = findViewById(R.id.tv_sex_detail);
        tv_role_detail_admin = findViewById(R.id.tv_role_detail_admin);
        tv_detail_status_admin = findViewById(R.id.tv_detail_status_admin_);
        tv_edit = findViewById(R.id.tv_edit_admin);
        tv_reset=findViewById(R.id.tv_reset_password);
    }

    private void getprofileuser() {
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.viewprofile(token,modeluser.getEmail())
                .enqueue(new Callback<ListUserModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ListUserModel> call, @NonNull Response<ListUserModel> response) {
                        progressDialog.dismiss();
                        ListUserModel models = response.body();
                        if (models != null) {
                            mUsermodelDetails = models.getData();
                            // Toast.makeText(ProfileActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            modeluser = mUsermodelDetails.get(0);
                            Setdatauser();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListUserModel> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailUserAdminActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
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
        if (v == tv_edit) {

            Intent intent = new Intent(this, EditUserAdminActivity.class);
            intent.putExtra("usermodel", modeluser);
            startActivity(intent);


        }
        if (v == tv_reset) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailUserAdminActivity.this);
            alertDialog.setTitle("Confirm ...");
            alertDialog.setMessage("You want to save");
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showLoading();
                            reset_password();
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.dismiss();
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }
    private void reset_password() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();

        Call<Login> call = service.resetpassword(token, modeluser.getEmail());
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                progressDialog.dismiss();
                Login models = response.body();
                if (models != null) {

                        Toast.makeText(DetailUserAdminActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        models = gson.fromJson(object.toString(), Login.class);
                        Toast.makeText(DetailUserAdminActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(DetailUserAdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailUserAdminActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        getprofileuser();
        super.onResume();
    }

    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0, 2) + "-" + date[1] + "-" + date[0];
        return dob;
    }

    public void back_home_profile(View view) {
        Intent intent = new Intent();
        intent.putExtra(UPDATE_USER_MODEL_KEY, "Update");
        intent.putExtra(POSITION_LIST_USER_KEY, getIntent().getIntExtra(POSITION_LIST_USER_KEY, 0));
        intent.putExtra(USER_MODEL_KEY, modeluser);
        setResult(RESULT_OK, intent);
        finish();
    }
}
