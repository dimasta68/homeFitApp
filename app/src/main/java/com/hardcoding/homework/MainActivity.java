package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.hardcoding.homework.ui.report.ReportFragment;

import java.util.Calendar;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public AlarmManager alarmManager;
    boolean alarm=true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Paper.init(getApplicationContext());
        if(Paper.book().read("alarm")!=null){
            alarm=  Paper.book().read("alarm");
        }

        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            if(alarm){
                scheduleAlarm();
            }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery,    R.id.nav_home,  R.id.nav_slideshow,R.id.nav_report)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Intent intent=getIntent();
        int myint=intent.getIntExtra("push",0);
        Log.d("debug","push"+myint);
        if(myint==1){
           navigationView.getMenu().findItem(R.id.nav_report);
            Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.nav_report);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @SuppressLint("ShortAlarm")
    public void scheduleAlarm() {
        Intent alarmIntent = new Intent(this, ReportReceiver.class);
        alarmIntent.setAction("com.hardcoding.homework.ReoprtReciver");
         int interval =1000*60*2*60;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar notifyTime = Calendar.getInstance();

        notifyTime.set(Calendar.HOUR_OF_DAY, 0);
        notifyTime.set(Calendar.MINUTE, 3);
        //notifyTime.set(Calendar.SECOND, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating
                    (AlarmManager.RTC_WAKEUP,
                            notifyTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        } else
            alarmManager.setInexactRepeating
                    (AlarmManager.RTC_WAKEUP,
                            notifyTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}