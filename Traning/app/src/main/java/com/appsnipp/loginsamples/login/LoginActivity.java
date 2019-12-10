package com.appsnipp.loginsamples.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.User_model.User;
import com.appsnipp.loginsamples.utils.SharedPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private TextView txtEmail, txtPassword;
    private final String TAG = "LoginTag";
    ProgressDialog progressDialog;
    Login mModelLogin;

    public static final String USER_MODEL_KEY = "user_model";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupBinding();
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidEmail(txtEmail.getText().toString())) {
                    txtEmail.setError("Enter a valid address");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (txtEmail.getText().toString().equals("")) {
                    txtEmail.setError("Please enter your email.");
                }
                if (txtEmail.getText().toString().contains(".*[^a-z^0-9].*")) {
                    txtEmail.setError("Enter a valid address");
                }
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    private void checkLoginuser() {
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        String email = txtEmail.getText().toString();
        String pass = txtPassword.getText().toString();

        Call<Login> call = service.getuserlogin(email, pass);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                progressDialog.dismiss();
                Login models = response.body();
                if (models != null) {
                    mModelLogin = models;
                    User usermodel = mModelLogin.getData();
                    if (mModelLogin.getSuccess() == true) {

                        saveIntoMemory(usermodel);

                        Toast.makeText(LoginActivity.this, mModelLogin.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                        intent.putExtra("usermodel", usermodel);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, mModelLogin.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject object = new JSONObject(response.errorBody().string());
                        models = gson.fromJson(object.toString(), Login.class);
                        Toast.makeText(LoginActivity.this, models.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveIntoMemory(User usermodel) {
        SharedPrefs.getInstance().put(USER_MODEL_KEY, usermodel);
    }


    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void setupBinding() {
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
    }

    public void loginClicked(View view) {
        if (txtEmail != null && txtPassword != null) {
            showLoading();
            checkLoginuser();

        }
    }


    public void viewRegisterClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
