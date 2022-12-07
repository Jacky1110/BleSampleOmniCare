package com.asuslife.sampleapps.blesampleomnicare.util;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;

public class MyProgressDialog {
    private final Context mContext;
    private final String mTitle;
    private final String mMessage;
    private ProgressDialog progressDialog;


    public MyProgressDialog(@NonNull Context context, String title, String message) {
        this.mContext = context;
        this.mTitle = title;
        this.mMessage = message;
    }

    public void show(boolean enableProgressBar) {

        progressDialog = new ProgressDialog(mContext);

        if (!TextUtils.isEmpty(mTitle)) {
            progressDialog.setTitle(mTitle);
        }
        if (!TextUtils.isEmpty(mMessage)) {
            progressDialog.setMessage(mMessage);
        }if (enableProgressBar){
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
        }

        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismiss() {
        progressDialog.dismiss();
    }

    public boolean isShowing(){
        return progressDialog.isShowing();
    }

    public void setProgress(int progress){
        progressDialog.setProgress(progress);
    }
}
