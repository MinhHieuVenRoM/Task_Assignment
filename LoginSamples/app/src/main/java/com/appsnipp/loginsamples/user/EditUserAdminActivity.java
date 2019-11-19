package com.appsnipp.loginsamples.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserAdminActivity extends AppCompatActivity implements View.OnClickListener {
    String[] status_item = {"Inactive", "Active"};
    String[] role_item = {"Admin", "User"};
    String[] sex_item = {"Male", "Female"};
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    Spinner spinnerstatus;
    ArrayAdapter<String> spinnerAdapterstatus;
    UserModelDetail modeluser;
    Spinner spinnersex;
    ArrayAdapter<String> spinnerAdaptersex;
    private int mYear, mMonth, mDay;
    private AppCompatTextView tv_full_name, tv_birthday, tv_email_detail_edit;
    private EditText tv_mobile_edit_admin;
    RelativeLayout btn_edit;
    private ProgressDialog progressDialog;
    private int role_id = 0;
    private int sex_id = 0;
    private int status_id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_admin);
        getDataIntent();
        getbyid();
        tv_birthday.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        setOptionSpinnerrole(modeluser.getRole());
        setOptionSpinnerstatus(modeluser.getStatus());
        setOptionSpinnerSex(modeluser.getSex());
        Setdatauser();

    }

    private void Setdatauser() {
        tv_full_name.setText(modeluser.getName());
        tv_birthday.setText(getdate(modeluser.getDob()));
        tv_email_detail_edit.setText(modeluser.getEmail());
        tv_mobile_edit_admin.setText(modeluser.getPhone());
        role_id = modeluser.getRole();
        sex_id = modeluser.getSex();
        status_id = modeluser.getStatus();
    }

    private void getbyid() {
        tv_full_name = findViewById(R.id.tv_full_name_edit);
        tv_birthday = findViewById(R.id.tv_birthday_edit_admin);
        tv_email_detail_edit = findViewById(R.id.tv_email_edit_admin);
        tv_mobile_edit_admin = findViewById(R.id.tv_mobile_edit_admin);
        btn_edit = findViewById(R.id.rl_edit_profile_admin);


    }


    private void setOptionSpinnerrole(int position) {
        spinner = findViewById(R.id.spinner_user_role_edit);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, role_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOptionSpinnerSex(int position) {
        spinnersex = findViewById(R.id.spinner_user_sex);
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

    private void setOptionSpinnerstatus(int position) {
        spinnerstatus = (Spinner) findViewById(R.id.spinner_user_status);
        spinnerAdapterstatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status_item);

        spinnerAdapterstatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerstatus.setAdapter(spinnerAdapterstatus);
        spinnerstatus.setSelection(position);
        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDataIntent() {

        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra("usermodel");
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(EditUserAdminActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
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

        if (v == btn_edit) {
            showLoading();
            if (1 == 0) {

                Toast.makeText(EditUserAdminActivity.this, "Error Input !", Toast.LENGTH_SHORT).show();


            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditUserAdminActivity.this);
                alertDialog.setTitle("Confirm ...");
                alertDialog.setMessage("You want to save");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();
                                RequestAPI service = APIClient.getClient().create(RequestAPI.class);
                                service.edituser(token, modeluser.getId(), tv_mobile_edit_admin.getText().toString(), setendate(tv_birthday.getText().toString()), sex_id, role_id, status_id)
                                        .enqueue(new Callback<UserEditModel>() {
                                            @Override
                                            public void onResponse(@NonNull Call<UserEditModel> call, @NonNull Response<UserEditModel> response) {
                                                progressDialog.dismiss();
                                                UserEditModel models = response.body();
                                                if (models != null) {

                                                    Toast.makeText(EditUserAdminActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                                                    if (models.getSuccess() == true) {
                                                        finish();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<UserEditModel> call, @NonNull Throwable t) {
                                                Toast.makeText(EditUserAdminActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.dismiss();
                                dialog.cancel();
                            }
                        });
                progressDialog.dismiss();
                alertDialog.show();


            }
        }


    }

    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2] + "-" + date[1] + "-" + date[0];
        return dob;
    }

    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0, 2) + "-" + date[1] + "-" + date[0];
        return dob;
    }

    public void back_home_profile_admin(View view) {
        finish();
    }

    public void Back_edit_user_admin(View view) {
        finish();
    }
}
