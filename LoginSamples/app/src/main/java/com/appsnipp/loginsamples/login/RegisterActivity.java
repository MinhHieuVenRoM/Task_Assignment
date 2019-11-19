package com.appsnipp.loginsamples.login;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.Login.Register;
import com.appsnipp.loginsamples.model.User_model.ListUserModel;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.model.User_model.UserModelDetail;
import com.appsnipp.loginsamples.task.AddingTaskActivity;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{


    private FirebaseAuth auth;
    private EditText inputEmail, inputPassword,tv_name_register,et_phone_register,et_dob_register,textsexregister;
    private Button btnSignUp;
    private ProgressDialog progressDialog;
    private int mYear, mMonth, mDay;
    private RadioGroup rdo_sex;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;
    private int sex=0;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getbyid();
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidEmail(inputEmail.getText().toString())) {
                    inputEmail.setError("Enter a valid address");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (inputEmail.getText().toString().equals("")) {
                    inputEmail.setError("Please enter your email.");
                }
                if (inputEmail.getText().toString().contains(".*[^a-z^0-9].*")) {
                    inputEmail.setError("Enter a valid address");
                }
            }
        });
        et_phone_register.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidPhone(et_phone_register.getText().toString())) {
                    inputEmail.setError("Enter a valid phone number");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (et_phone_register.getText().toString().equals("")) {
                    et_phone_register.setError("Please enter your phone number.");
                }
                if (et_phone_register.getText().toString().contains(".*[^a-z].*")) {
                    et_phone_register.setError("Enter a valid phone number");
                }
            }
        });
    }
    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static boolean isValidPhone(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return Patterns.PHONE.matcher(target).matches();
        }
    }
    private void getbyid() {
        inputEmail = (EditText) findViewById(R.id.editTextEmail_register);
        inputPassword = (EditText) findViewById(R.id.textpasswordregister);
        tv_name_register = (EditText) findViewById(R.id.tv_name_register);
        et_phone_register = (EditText) findViewById(R.id.et_phone_register);
        et_dob_register = (EditText) findViewById(R.id.et_dob_register);
        rdo_sex =  findViewById(R.id.radioButton_sex);
        this.radioButtonMale = this.findViewById(R.id.radioButton_male);
        this.radioButtonFemale  = this.findViewById(R.id.radioButton_female);
        this.radioButtonMale.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sex=0;

            }

        });
        this.radioButtonFemale.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sex=1;

            }

        });

        btnSignUp = (Button) findViewById(R.id.btn_Register);
        et_dob_register.setOnClickListener(this);
    }

    public void BackClicked(View view) {
        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void registerClicked(View view) throws ParseException {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String name = tv_name_register.getText().toString().trim();
        String dob = et_dob_register.getText().toString().trim();
        String phone = et_phone_register.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(dob)) {
            Toast.makeText(getApplicationContext(), "Enter Date of Birth!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Enter Phone!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading();
        adduser();
    }

    private void adduser() throws ParseException {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String name = tv_name_register.getText().toString().trim();
        String dob = setendate(et_dob_register.getText().toString().trim());
        String phone = et_phone_register.getText().toString().trim();


        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        service.adduser(email,name,password,sex,dob,phone)
                .enqueue(new Callback<Register>() {
                    @Override
                    public void onResponse(@NonNull Call<Register> call, @NonNull Response<Register> response) {
                        progressDialog.dismiss();
                        Register models = response.body();
                        if (models != null) {
                            Toast.makeText(getApplicationContext(), models.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            try {
                                Gson gson = new Gson();
                                JSONObject object = new JSONObject(response.errorBody().string());
                                models = gson.fromJson(object.toString(), Register.class);
                                Toast.makeText(RegisterActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Register> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String setendate(String trim) {
        String []date=trim.split("-");
        String dob=date[2]+"-"+date[1]+"-"+date[0];
                return dob;
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == et_dob_register) {

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

                            et_dob_register.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }
}
