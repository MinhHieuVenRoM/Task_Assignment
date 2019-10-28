package com.appsnipp.loginsamples.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView txtEmail, txtPassword;
    private final String TAG = "LoginTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail= findViewById(R.id.editTextEmail);

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!isValidEmail(txtEmail.getText().toString())){
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
        setupFirebaseAuth();
        checkLogin();
        setupBinding();
    }


    public static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    private void checkLogin() {
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null){
//            updateUI(mUser);
        }
    }

    private void setupBinding() {
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
    }

    public void loginClicked(View view){
        if (txtEmail != null && txtPassword != null){
            mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //Toast.makeText(this, "Authentication failed",  Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    private void updateUI() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("hieu",1);
        startActivity(intent);
        finish();
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void viewRegisterClicked(View view){
        Toast.makeText(getApplicationContext(), "Sign up clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
