package com.appsnipp.loginsamples.personal_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.user.EditUserAdminActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private AppCompatTextView tv_userrole_edituser,tv_birthday_edit_user,tv_email_edit_user,tv_full_name_user_edit;
    private EditText et_mobile_edit_user;
    private int mYear, mMonth, mDay;
    UserModelDetail modeluser;
    String[] sex_item = {"Male", "Female"};
    Spinner spinnersex;
    ArrayAdapter<String> spinnerAdaptersex;
    private int sex_id = 0;
    ProgressDialog progressDialog;
    RelativeLayout rl_cancel_edit_user,rl_save_edit_profile_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        getbyid();
        getDataIntent();
        Setdatauser();
        setOptionSpinnerSex(modeluser.getSex());
        tv_birthday_edit_user.setOnClickListener(this);
        rl_cancel_edit_user.setOnClickListener(this);
        rl_save_edit_profile_user.setOnClickListener(this);


    }

    private void getbyid() {
        tv_full_name_user_edit=findViewById(R.id.tv_full_name_edit);
        tv_userrole_edituser=findViewById(R.id.tv_userrole_edituser);
        tv_birthday_edit_user=findViewById(R.id.tv_birthday_edit_user);
        tv_email_edit_user=findViewById(R.id.tv_email_edit_user);
        et_mobile_edit_user=findViewById(R.id.et_mobile_edit_user);
        spinnersex=findViewById(R.id.spinner_user_sex_edit);
        rl_cancel_edit_user=findViewById(R.id.rl_cancel_edit_user);
        rl_save_edit_profile_user=findViewById(R.id.rl_save_edit_profile_user);
    }
    private void Setdatauser() {
        tv_full_name_user_edit.setText(modeluser.getName());
        tv_birthday_edit_user.setText(getdate(modeluser.getDob()));
        tv_email_edit_user.setText(modeluser.getEmail());
        et_mobile_edit_user.setText(modeluser.getPhone());

        if(modeluser.getRole()==0){
            tv_userrole_edituser.setText("Admin");
        }else {
            tv_userrole_edituser.setText("User");
        }
    }

    private void setOptionSpinnerSex(int position) {
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


    private void getDataIntent() {
        Intent intent = getIntent();
        modeluser = (UserModelDetail) intent.getSerializableExtra("usermodel");
    }
    private void showLoading() {
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
    @Override
    public void onClick(View v) {
        if (v == tv_birthday_edit_user) {

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

                            tv_birthday_edit_user.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


        }
        if (v == rl_save_edit_profile_user) {
            showLoading();
            if (1 == 0) {

                Toast.makeText(EditProfileActivity.this, "Error Input !", Toast.LENGTH_SHORT).show();


            } else {



                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
                alertDialog.setTitle("Confirm ...");
                alertDialog.setMessage("You want to log out");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();
                                int role = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getRole();
                                int status = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getStatus();
                                RequestAPI service = APIClient.getClient().create(RequestAPI.class);
                                service.edituser(token, modeluser.getId(),et_mobile_edit_user.getText().toString(),setendate(tv_birthday_edit_user.getText().toString()),sex_id,role,status)
                                        .enqueue(new Callback<UserEditModel>() {
                                            @Override
                                            public void onResponse(@NonNull Call<UserEditModel> call, @NonNull Response<UserEditModel> response) {
                                                progressDialog.dismiss();
                                                UserEditModel models = response.body();
                                                if (models != null) {

                                                    Toast.makeText(EditProfileActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                                                    if (models.getSuccess() == true) {
                                                        finish();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<UserEditModel> call, @NonNull Throwable t) {
                                                Toast.makeText(EditProfileActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
                alertDialog.show();


            }
        }
        if (v == rl_cancel_edit_user){
            finish();

        }

    }


    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2] + "-" + date[1] + "-" + date[0];
        return dob;
    }

    private String getdate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0,2) + "-" + date[1] + "-" + date[0];
        return dob;
    }
}
