package com.appsnipp.loginsamples.personal_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.Project_model.ProjectAddResponse;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserEditModel;
import com.appsnipp.loginsamples.project.ProjectActivity;
import com.appsnipp.loginsamples.user.EditUserAdminActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    User modeluser;

    private TextView tv_full_name,tv_email,tv_userrole,tv_sex,tv_birthday,tv_mobile,btn_edit_user;
    RelativeLayout rl_change_password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBinding();
        getDataIntent();
        getdatauser();
        btn_edit_user.setOnClickListener(this);
        rl_change_password.setOnClickListener(this);

    }

    private void getdatauser() {
        tv_full_name.setText(modeluser.getName());

        if(modeluser.getRole()==0)
        {
            tv_userrole.setText("Admin");
        }else {
            tv_userrole.setText("User");
        }
        tv_email.setText(modeluser.getEmail());

        if(modeluser.getSex()==0)
        {

            tv_sex.setText("Male");
        }else {

            tv_sex.setText("Female");
        }
        tv_birthday.setText(setendate(modeluser.getDob()));
        tv_mobile.setText((modeluser.getPhone()));

    }

    private void setupBinding() {
        tv_full_name=findViewById(R.id.tv_full_name_user_edit);
        tv_userrole=findViewById(R.id.tv_userrole);
        tv_email=findViewById(R.id.tv_email);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_sex=findViewById(R.id.tv_sex);
        tv_birthday=findViewById(R.id.tv_birthday);
        btn_edit_user=findViewById(R.id.btn_edit_user);
        rl_change_password=findViewById(R.id.rl_change_password);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        modeluser = (User) intent.getSerializableExtra("usermodel");
    }
    private String setendate(String trim) {
        String[] date = trim.split("-");
        String dob = date[2].substring(0,2) + "-" + date[1] + "-" + date[0];
        return dob;
    }

    @Override
    public void onClick(View v) {
        if(v==btn_edit_user){

            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("usermodel", modeluser);
            startActivity(intent);


        }
        if(v==rl_change_password){
            LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
            View dialogViewAddProject = li.inflate(R.layout.dialog_change_password, null);
            final TextInputEditText tv_pass_current = dialogViewAddProject.findViewById(R.id.tv_pass_current);
            final TextInputEditText tv_newpass = dialogViewAddProject.findViewById(R.id.tv_newpass);
            final TextInputEditText tv_newpass_again = dialogViewAddProject.findViewById(R.id.tv_newpass_again);


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
            alertDialogBuilder.setView(dialogViewAddProject);
            alertDialogBuilder.setTitle("Change Password");
            alertDialogBuilder.setPositiveButton("Save", null)
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog dialog = alertDialogBuilder.create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialogInterface) {

                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // TODO Do something
                            if(tv_newpass.getText().toString().equals(tv_newpass_again.getText().toString())==false){

                                Toast.makeText(ProfileActivity.this, "The Confirm password new is different from the new password", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                showLoading();
                                changepasswped(tv_pass_current.getText().toString(),tv_newpass.getText().toString(),dialog);

                            }


                        }
                    });
                }
            });
            dialog.show();
        }
    }

    private void changepasswped(String Pass_current, String New_pass, final Dialog changepass_dialog) {

        String token = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getToken();
        String name = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getName();
        String id = SharedPrefs.getInstance().get(LoginActivity.USER_MODEL_KEY, User.class).getId();

        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.changepassword(token,id,name, Pass_current,New_pass)
                .enqueue(new Callback<UserEditModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UserEditModel> call, @NonNull Response<UserEditModel> response) {
                        progressDialog.dismiss();
                        UserEditModel models = response.body();
                        if (models != null) {
                            Toast.makeText(ProfileActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            changepass_dialog.dismiss();
                        }else {
                            try {
                                Gson gson = new Gson();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                UserEditModel  model = gson.fromJson(object.toString(), UserEditModel.class);
                                Toast.makeText(ProfileActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserEditModel> call, @NonNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void showLoading() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }
}
