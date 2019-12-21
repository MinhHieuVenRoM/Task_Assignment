package com.appsnipp.loginsamples.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.loginsamples.R;
import com.appsnipp.loginsamples.utils.LoginUser;


public class LoginActivity extends AppCompatActivity {
    private TextView txtEmail, txtPassword;
//    private final String TAG = "LoginTag";

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
            String email = txtEmail.getText().toString();
            String pass = txtPassword.getText().toString();
            new LoginUser(this).login(email, pass, true);
        }
    }


    public void viewRegisterClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
