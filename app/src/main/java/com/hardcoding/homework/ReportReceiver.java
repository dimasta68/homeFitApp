package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.hardcoding.homework.Interface.LunchTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
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

public class ReportReceiver extends BroadcastReceiver {
    int reportBtn;
    String quest;
    ArrayList<String> checked;
    Map<Integer, Integer> ProgressMap = new HashMap<Integer, Integer>();
    int complite;
    boolean alarm;
    String mail, progressData;
    Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Paper.init(context);
       Log.d("debug","report TEST RECEIVER");
        if (Paper.book().read("progressData") != null) {
            progressData = Paper.book().read("progressData");
            Log.d("debug", "progressData" + progressData);
        }
        if (Paper.book().read("ProgressMap") != null) {
            ProgressMap = Paper.book().read("ProgressMap");
            Log.d("debug", "ProgressMap" + ProgressMap);
        }
        if (Paper.book().read("complite") != null) {
            complite = Paper.book().read("complite");
        }
        if (Paper.book().read("hashMap") != null) {
            hashMap = Paper.book().read("hashMap");
        }
            //// get server report
        mail = Paper.book().read("mail");
        Paper.book().write("complite", complite);
        checked = Paper.book().read("checked");
        Log.d("debug", "cheked report Frag" + checked);
        if(checked!=null){
            for (int i = 0; i < checked.size(); i++) {
                int value = Integer.parseInt(checked.get(i));
                ProgressMap.get(value);
                //   Log.d("debug", "ProgressMap click " + ProgressMap.get(value));
                complite = ProgressMap.get(value) + 1;
                ProgressMap.put(value, complite);
                hashMap.put(value, complite);
            }


        Paper.book().write("hashMap", hashMap);
        Paper.book().write("ProgressMap", ProgressMap);
        Date dateNow = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss");
        String date = formatForDateNow.format(dateNow);
        quest=Paper.book().read("quest");

        getReport(mail, date, progressData,quest);/// get server report
        checked.clear();
        Paper.book().write("checked",checked);
        Toast.makeText(context, "отчтет принят", Toast.LENGTH_SHORT).show();
        }
        alarm=false;
        Paper.book().write("alarm",alarm);
      //  Intent intent1 = new Intent(context, MainActivity.class);
       // context.startActivity(intent1);

    }
    public static void getReport(String username, String title_task, String status,String question) {
        Log.d("debug","report get report started");
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
        parameters.put("data", title_task);
        parameters.put("status", status);
        parameters.put("question", question);


        Call<List<Post>> call = lunchTask.getReports(parameters);

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NotNull Call<List<Post>> call, @NotNull Response<List<Post>> response) {

                if (!response.isSuccessful()) {

                    Log.d("debug", "respose" + response.body().toString());
                    return;
                }

                List<Post> posts = response.body();

            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                Log.d("debug", "respose error");
            }
        });
    }
}
