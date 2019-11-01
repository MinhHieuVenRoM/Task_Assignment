package com.appsnipp.loginsamples.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public ToastUtil(){}
    Context mContext;
    public void showToast(String toast){
         mContext = GlobalApplication.self();

        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
