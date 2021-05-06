package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.hardcoding.homework.Interface.LunchTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    Button btnStart;
    String pid ,progText;
    String inactive, username;
    TextView periodText, lead_timeText, timeText, descript, title,pogresText;
    public List<Integer> positionfav = new ArrayList<>();
    public List<Integer> reportfav = new ArrayList<>();
    Map<Integer, Integer> ProgressMap = new HashMap<Integer, Integer>();
    public List<Integer> progress = new ArrayList<>();
    String time, lead_time, period;
    public AlarmManager alarmManager;
    private AlarmReceiver alarm;

    //  int pos;
    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = (TextView) findViewById(R.id.title);
        descript = (TextView) findViewById(R.id.descriptions);
        btnStart = (Button) findViewById(R.id.btnStart);
        periodText = (TextView) findViewById(R.id.perios);
        lead_timeText = (TextView) findViewById(R.id.lead_times);
        timeText = (TextView) findViewById(R.id.times);
        pogresText = (TextView) findViewById(R.id.progress);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm = new AlarmReceiver();
        Paper.init(getApplicationContext());
        if (Paper.book().read("positionfav") != null) {
            positionfav = Paper.book().read("positionfav");
        }
        if (Paper.book().read("reportfav") != null) {
            reportfav = Paper.book().read("reportfav");
        }if (Paper.book().read("progress") != null) {
            progress = Paper.book().read("progress");
        }

        //Log.d("debug", "positionfav  " + positionfav);
        Intent intent = getIntent();
        String titles = intent.getStringExtra("title");
        String descrip = intent.getStringExtra("descript");
        time = intent.getStringExtra("time");
        period = intent.getStringExtra("period");
        lead_time = intent.getStringExtra("lead_time");
        progText = intent.getStringExtra("progText");
        pogresText.setText(progText);
        Log.d("debug", "progText  " + progText);
        periodSet(period);
        timeSet(time);
        leadSet(lead_time);

        //  Log.d("debug", "details position=" + intent.getIntExtra("position", 0));
        // Log.d("debug", "time" + time);
        inactive = intent.getStringExtra("inactive");
        pid = intent.getStringExtra("pid");
        username = Paper.book().read("mail");
        if (inactive.equals("Активно")) {
            btnStart.setText("Активно");
        } else {
            btnStart.setText("Не активно");
        }

        // Log.d("debug", "inactive " + inactive);

        title.setText(titles);
        descript.setText(Html.fromHtml(descrip), null);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                int pos = intent.getIntExtra("position", 0);
                int myNum = Integer.parseInt(pid);
                int progressint = Integer.parseInt(progText);
                if (btnStart.getText().equals("Активно")) { /// remove position fav
                    positionfav.remove((Integer) pos);
                    reportfav.remove((Integer) myNum);
                  //  ProgressMap.remove()
                    Log.d("debug","remove element "+ProgressMap);
                    Paper.book().write("positionfav", positionfav);
                    Paper.book().write("reportfav", reportfav);
                   // Paper.book().write("ProgressMap", ProgressMap);
                    getStop(username, pid);
                    CancelAlarm(pos);
                    //cancelRepeatingTimer(pos);
                    setResult(RESULT_OK, intent);
                    btnStart.setText("Не активно");
                    btnStart.setBackgroundColor(R.color.colorAccent);
                } else if (btnStart.getText().equals("Не активно")) { ////add position lunch
                    String titles = title.getText().toString();
                    scheduleAlarm(time, pos, titles);
                    //startRepeatingTimer(time,pos,titles);
                    getLunch(username, pid);
                    if (Paper.book().read("positionfav") == null) {
                        positionfav.add(pos);
                    } else {
                        positionfav = Paper.book().read("positionfav");
                        positionfav.add(pos);
                    }
                    if (Paper.book().read("reportfav") == null) {
                        reportfav.add(myNum);
                    } else {
                        reportfav = Paper.book().read("reportfav");
                        reportfav.add(myNum);
                    }
                    if (Paper.book().read("ProgressMap") == null) {
                        ProgressMap.put(myNum,progressint);
                    } else {
                        ProgressMap = Paper.book().read("ProgressMap");
                        ProgressMap.put(myNum,progressint);
                    }
                    setResult(RESULT_OK, intent);
                    Paper.book().write("positionfav", positionfav);
                    Paper.book().write("reportfav", reportfav);
                    Paper.book().write("ProgressMap", ProgressMap);
                    btnStart.setText("Активно");
                }
            }
        });

    }

    public static void getLunch(String username, String id) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://homefit.beget.tech/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        LunchTask lunchTask = retrofit.create(LunchTask.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("id", id);


        Call<List<Post>> call = lunchTask.getPosts(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {

                if (!response.isSuccessful()) {


                    return;
                }

                List<Post> posts = response.body();

            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {

            }
        });
    }

    public static void getStop(String username, String id) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://homefit.beget.tech/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        LunchTask lunchTask = retrofit.create(LunchTask.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("id", id);


        Call<List<Post>> call = lunchTask.getStop(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {

                if (!response.isSuccessful()) {


                    return;
                }

                List<Post> posts = response.body();

            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {

            }
        });
    }

    public void periodSet(String period) {

        switch (period) {
            case "1":
                periodText.setText("переодичность:ежедневно");
                break;
            case "2":
                periodText.setText("переодичность:еженедельно");
                break;
            case "3":
                periodText.setText("переодичность:ежемесячно");
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    public void timeSet(String time) {

        switch (time) {

            case "1":
                lead_timeText.setText("срок:21 день");
                break;
            case "2":
                lead_timeText.setText("срок:12 недель");
                break;
            case "3":
                lead_timeText.setText("срок:24 недели");
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    public void leadSet(String lead) {

        switch (lead) {
            case "1":
                timeText.setText("пвремя выполнения: утро");
                break;
            case "2":
                timeText.setText("время выполнения: день");
                break;
            case "3":
                timeText.setText("время выполнения: вечер");
                break;
        }

    }


    @SuppressLint("ShortAlarm")
    private void scheduleAlarm(String hh, int id, String title) {
        int H = Integer.parseInt(hh);
        int minut = 0;
        if (H == 1) {
            H = Paper.book().read("mhour");
            minut = Paper.book().read("mminute");
        } else if (H == 2) {
            H = 12;//12
            minut = 0;
        } else if (H == 3) {
            H = 21;///21
            minut = 0;
        }

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction("com.hardcoding.homework.AlarmReceiver");
        alarmIntent.putExtra("title", title);
        alarmIntent.putExtra("id", id);
        // int interval =1000*60*2;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar notifyTime = Calendar.getInstance();

        notifyTime.set(Calendar.HOUR_OF_DAY, H);
        notifyTime.set(Calendar.MINUTE, minut);
        //notifyTime.set(Calendar.SECOND, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating
                    (AlarmManager.RTC_WAKEUP,
                            notifyTime.getTimeInMillis(), /*1000 * 60*/AlarmManager.INTERVAL_DAY, pendingIntent);
        } else
            alarmManager.setInexactRepeating
                    (AlarmManager.RTC_WAKEUP,
                            notifyTime.getTimeInMillis(), /*1000 * 60*/AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void CancelAlarm(int id) {
        // Log.d("debug", "cancel alarm ");
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.setAction("com.hardcoding.homework.AlarmReceiver");
        alarmIntent.putExtra("cancelId", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, id, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

    }

}