package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hardcoding.homework.Interface.LunchTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    String pid;
    String inactive, username;
    public List<Integer> positionfav= new ArrayList<>();


    //  int pos;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView title = (TextView) findViewById(R.id.title);
        TextView descript = (TextView) findViewById(R.id.descriptions);
        btnStart = (Button) findViewById(R.id.btnStart);
        Paper.init(getApplicationContext());
        positionfav = Paper.book().read("positionfav");
        Log.d("debug", "positionfav  " + positionfav);
        Intent intent = getIntent();
        String titles = intent.getStringExtra("title");
        String descrip = intent.getStringExtra("descript");
        Log.d("debug", "details position=" + intent.getIntExtra("position", 0));
        inactive = intent.getStringExtra("inactive");
        pid = intent.getStringExtra("pid");

        Paper.init(getApplicationContext());
        username = Paper.book().read("mail");
        if (inactive.equals("Активно")) {
            btnStart.setText("Активно");
        } else {
            btnStart.setText("Не активно");
        }
        Log.d("debug", "inactive " + inactive);

        title.setText(titles);
        descript.setText(Html.fromHtml(descrip), null);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                int pos = intent.getIntExtra("position", 0);
                if (btnStart.getText().equals("Активно")) { /// remove position fav
                    positionfav.remove((Integer) pos);
                    Paper.book().write("positionfav", positionfav);
                    getStop(username, pid);
                    btnStart.setText("Не активно");
                } else if (btnStart.getText().equals("Не активно")) { ////add position lunch
                    getLunch(username, pid);
                    positionfav = Paper.book().read("positionfav");
                    positionfav.add(pos);

                    Paper.book().write("positionfav", positionfav);
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
}