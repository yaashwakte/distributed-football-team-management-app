package com.ysh.nufcapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.view.LayoutInflater;

public class Loading_Box_activity extends Application
{
    Activity activity;
    AlertDialog dialog;

    Loading_Box_activity(Activity loading_activity)
    {
        activity = loading_activity;
    }

    public void loading_box()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_box,null));
        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();
    }

    public void stop_loading()
    {
        dialog.dismiss();
    }
}
