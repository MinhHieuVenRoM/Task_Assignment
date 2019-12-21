package com.appsnipp.loginsamples.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appsnipp.loginsamples.HomeActivity;
import com.appsnipp.loginsamples.login.LoginActivity;
import com.appsnipp.loginsamples.model.API.APIClient;
import com.appsnipp.loginsamples.model.API.RequestAPI;
import com.appsnipp.loginsamples.model.Login.Login;
import com.appsnipp.loginsamples.model.User_model.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.appsnipp.loginsamples.HomeActivity.CHECK_STATUS_LOGIN;
import static com.appsnipp.loginsamples.HomeActivity.USER_EMAIL_KEY;
import static com.appsnipp.loginsamples.HomeActivity.USER_MODEL_KEY;
import static com.appsnipp.loginsamples.HomeActivity.USER_PASSWORD_KEY;

public class LoginUser {
    private ProgressDialog progressDialog;
    private Context mContext;
    private Login mModelLogin;

    public LoginUser(Context mContext) {
        this.mContext = mContext;
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
    }

    public void login(final String email, final String password, final boolean isFromLogin) {
        showLoading();
        RequestAPI service = APIClient.getClient().create(RequestAPI.class);
        Call<Login> call = service.getuserlogin(email, password);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
                progressDialog.dismiss();
                Login models = response.body();
                if (models != null) {
                    mModelLogin = models;
                    User userModel = mModelLogin.getData();
                    if (mModelLogin.getSuccess()) {

                        saveIntoMemory(userModel, email, password);
                        Toast.makeText(mContext, mModelLogin.getMessage(), Toast.LENGTH_SHORT).show();

                        if (isFromLogin) goToHome();
                    } else {
                        Toast.makeText(mContext, mModelLogin.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Gson gson = new Gson();
                        JSONObject object;
                        if (response.errorBody() != null) {
                            object = new JSONObject(response.errorBody().toString());
                            models = gson.fromJson(object.toString(), Login.class);
                            Toast.makeText(mContext, models.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Login> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
        ((LoginActivity) mContext).finish();
    }

    private void saveIntoMemory(User userModel, String email, String password) {
        SharedPrefs.getInstance().put(USER_MODEL_KEY, userModel);
        SharedPrefs.getInstance().put(USER_EMAIL_KEY, email);
        SharedPrefs.getInstance().put(USER_PASSWORD_KEY, password);
        SharedPrefs.getInstance().put(CHECK_STATUS_LOGIN, "1");

    }
}
