package com.hardcoding.homework;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.hardcoding.homework.Interface.JsonPlaceHolderApi;
import com.hardcoding.homework.Interface.MyInterfaceReg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RegActivity extends AppCompatActivity {
    private ArrayList<View> allEds;
    ArrayList<String> mapS;
    LinearLayout layout;
    private Object TextView;
    private int counter = 0;
    EditText editmail;
    String answers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        layout = (LinearLayout) findViewById(R.id.regViewScrol);
        editmail = (EditText) findViewById(R.id.editmail);
        getRegJSONResponse();
        allEds = new ArrayList<View>();
        mapS = new ArrayList<>();
        Paper.init(getApplicationContext());
    }

    private void getRegJSONResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterfaceReg.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MyInterfaceReg api = retrofit.create(MyInterfaceReg.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                assert response.body() != null;
                Log.d("debug", " response.body()" + response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("debug", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject jsonObj = new JSONObject(response.body());
                            JSONArray items = jsonObj.getJSONArray("products");
                            for (int i = 0; i < items.length(); i++) {
                                counter++;
                                final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);
                                ToggleButton btn = (ToggleButton) view.findViewById(R.id.togg);
                                EditText text = (EditText) view.findViewById(R.id.editText);
                                TextView textView = (TextView) view.findViewById(R.id.answer);
                                JSONObject c = items.getJSONObject(i);
                                textView.setText(c.getString("title_interview"));
                                if (c.getString("answer").equals("2")) {
                                    allEds.add(view);

                                    ((ViewGroup) btn.getParent()).removeView(btn);
                                    layout.addView(view);
                                } else if (c.getString("answer").equals("3")) {
                                    ((ViewGroup) text.getParent()).removeView(text);
                                    allEds.add(view);
                                    layout.addView(view);
                                }


                            }
                            Button btn = new Button(getApplicationContext());
                            btn.setBackgroundColor(R.color.color1);
                            btn.setText("отправить");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String[] TextView = new String[allEds.size()];
                                    for (int i = 0; i < allEds.size(); i++) {
                                        TextView[i] = ((TextView) allEds.get(i).findViewById(R.id.answer)).getText().toString();
                                        mapS.add(((TextView) allEds.get(i).findViewById(R.id.answer)).getText().toString());
                                        //   Log.d("debug", ((TextView) allEds.get(i).findViewById(R.id.answer)).getText().toString());
                                        if (((ToggleButton) allEds.get(i).findViewById(R.id.togg)) != null) {
                                            mapS.add(((ToggleButton) allEds.get(i).findViewById(R.id.togg)).getText().toString());
                                            //  Log.d("debug", ((ToggleButton) allEds.get(i).findViewById(R.id.togg)).getText().toString());
                                        } else if (((EditText) allEds.get(i).findViewById(R.id.editText)) != null) {
                                            mapS.add(((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString());
                                            //    Log.d("debug", ((EditText) allEds.get(i).findViewById(R.id.editText)).getText().toString());
                                        }
                                    }
                                    Paper.book().write("mail", editmail.getText().toString());
                                    //   Paper.book().write("answer", answers);
                                   // Log.d("debug", "mailuser" + editmail.getText());
                                  //  Log.d("debug", "answerString" + answers);

                                    Intent mainIntent = new Intent(RegActivity.this, MainActivity.class);
                                    RegActivity.this.startActivity(mainIntent);
                                    RegActivity.this.finish();
                                    getUp(editmail.getText().toString(), mapS.toString());

                                }
                            });
                            layout.addView(btn);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }  //   Log.d("debug", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

            }
        });
    }

    public static void getUp(String username, String answer) {

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

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("answer", answer);


        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

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