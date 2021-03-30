package com.hardcoding.homework;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

import io.paperdb.Paper;


public class SplashActivity extends AppCompatActivity {
    //Логическая переменная для статуса соединения
    boolean originalapp;
    boolean hash = false;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private PackageManager packageManager = null;
    public List<ApplicationInfo> applist = null;
    public ProgressDialog progress = null;
    AlertDialog alertDialog;
    String AppString;

    String statusEntenet = "0";///0-отключен хеша нет,1-отключен хеш есть,2-есть интернет
    String locate;
    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        Paper.init(getApplicationContext());


        ///init alert dialog
        alertDialog = new AlertDialog.Builder(this).create();
        //Настраиваем название Alert Dialog:
        alertDialog.setTitle(getResources().getString(R.string.error_coonnect));///нет соеденения string

        //Настраиваем сообщение:
        alertDialog.setMessage(getResources().getString(R.string.error_coonnect2));// msg Интернет соединение отсутствует

        //Настраиваем иконки, можете выбрать другие или добавить свои (мне лень):
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });

        cd = new ConnectionDetector(getApplicationContext());
        //Получаем статус Интернет
        isInternetPresent = cd.ConnectingToInternet();
        Log.d("debug", "isInternetPresent= " + isInternetPresent);
        //Проверяем Интернет статус:
        if (isInternetPresent) {
            /**
             * Duration of wait
             **/
            int SPLASH_DISPLAY_LENGTH = 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(Paper.book().read("mail")!=null){
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        mainIntent.putExtra("internet", "2");
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }else{
                        Intent mainIntent = new Intent(SplashActivity.this, RegActivity.class);
                        mainIntent.putExtra("internet", "2");
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }


                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    }
}


