package com.hardcoding.homework.ui.report;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.hardcoding.homework.BuildConfig;
import com.hardcoding.homework.Interface.LunchTask;
import com.hardcoding.homework.Interface.MyInterface;
import com.hardcoding.homework.MainActivity;
import com.hardcoding.homework.ModelListView;
import com.hardcoding.homework.Post;
import com.hardcoding.homework.R;
import com.hardcoding.homework.ReportAdapter;
import com.hardcoding.homework.ReportReceiver;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReportFragment extends Fragment {

    private ReportViewModel homeViewModel;
    ListView lv;
    ArrayList<String> checked;
    public ArrayList<HashMap<String, String>> productsList;
    public ReportAdapter retroAdapter;
    List<Integer> reportfav = new ArrayList<>();
    Button btnReport;
    Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
    Map<Integer, Integer> ProgressMap = new HashMap<Integer, Integer>();
    int pos;
    int complite;
    String mail, progressData;
    public AlarmManager alarmManager;
    int reportBtn;
    EditText quest;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ReportViewModel.class);
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        lv = view.findViewById(R.id.reportList);
        productsList = new ArrayList<HashMap<String, String>>();
        btnReport = (Button) view.findViewById(R.id.buttonReport);
        quest = (EditText) view.findViewById(R.id.question);
        if (Paper.book().read("quest") != null) {
            quest.setText(Paper.book().read("quest").toString());
        }
        quest.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                Paper.book().write("quest",quest.getText().toString());
                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        Paper.init(requireContext());
        checked = new ArrayList<>();
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
      //  scheduleAlarm();
        mail = Paper.book().read("mail");

        if (Paper.book().read("progressData") != null) {
            progressData = Paper.book().read("progressData");
           // Log.d("debug", "progressData" + progressData);
        }
        if (Paper.book().read("ProgressMap") != null) {
            ProgressMap = Paper.book().read("ProgressMap");
          //  Log.d("debug", "ProgressMap" + ProgressMap);
        }
        if (Paper.book().read("complite") != null) {
            complite = Paper.book().read("complite");
        }
        if (Paper.book().read("hashMap") != null) {
            hashMap = Paper.book().read("hashMap");
        }
        if (Paper.book().read("reportfav") != null) {
            reportfav = Paper.book().read("reportfav");
            if (reportfav.isEmpty()) {
                lv.setVisibility(View.GONE);
            } else {
                getTaskJSONResponse();
            }
        } else {
            lv.setVisibility(View.GONE);
        }

        btnReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Paper.book().write("complite", complite);
                checked = Paper.book().read("checked");
            //    Log.d("debug", "cheked report Frag" + checked);
                for (int i = 0; i < checked.size(); i++) {
                    int value = Integer.parseInt(checked.get(i));
                    ProgressMap.get(value);
                  //  Log.d("debug", "ProgressMap click " + ProgressMap.get(value));
                    complite = ProgressMap.get(value) + 1;
                    ProgressMap.put(value, complite);
                    hashMap.put(value, complite);
                }
                Paper.book().write("hashMap", hashMap);
                Paper.book().write("ProgressMap", ProgressMap);
                Date dateNow = new Date();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatForDateNow = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss");
                String date = formatForDateNow.format(dateNow);
                String question = quest.getText().toString();
                getReport(mail, date, progressData, question);/// get server report
                Toast.makeText(getActivity(), "отчтет принят", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), MainActivity.class);
                startActivity(intent1);
            }
        });
        return view;

    }

    private void getTaskJSONResponse() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MyInterface api = retrofit.create(MyInterface.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                //   Log.d("debug", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //   Log.d("debug", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeListView(jsonresponse);

                    } else {
                        //  Log.d("debug", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

            }
        });
    }

    private void writeListView(String response) {

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            Paper.book().write("reportData", obj);
            if (obj.optString("success").equals("1")) {

                ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("products");

                for (int i = 0; i < reportfav.size(); i++) {
                    ModelListView modelListView = new ModelListView();
                    //Log.d("debug","pid fav"+reportfav.get(i));
                    int repi = reportfav.get(i) - 1;
                    JSONObject dataobj = dataArray.getJSONObject(repi);

                    //Log.d("debug","title_object jsonfav "+  dataArray.getJSONObject(reportfav.get(i)).getString("title_task"));
                    //    Log.d("debug", "allfragmetn data " + dataobj);
                    //  modelListView.se(dataobj.getString("imgURL"));
                    //   int report=reportfav.get(i)
                    // int id=Integer.parseInt(dataobj.getString("id"));
                    //  if(id==reportfav.get(i)){
                    modelListView.setId(dataobj.getString("id"));
                    modelListView.setCat(dataobj.getString("cat"));
                    modelListView.setTitle(dataobj.getString("title_task"));
                    modelListView.setDecsript(dataobj.getString("desc_task"));
                    modelListView.setLavel(dataobj.getString("lavel"));
                    modelListView.setInactive(dataobj.getString("inactive"));
                    modelListView.setLead_time(dataobj.getString("lead_time"));
                    modelListView.setTime(dataobj.getString("time"));
                    modelListView.setPeriod(dataobj.getString("period"));
                  //  Log.d("debug", "title_task fav" + dataobj.getString("title_task"));
                    //  }


                    modelListViewArrayList.add(modelListView);
                    // Создаем новый HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    // добавляем каждый елемент в HashMap ключ => значение
                    map.put("id", dataobj.getString("id"));
                    map.put("title_task", dataobj.getString("title_task"));
                    map.put("cat", dataobj.getString("cat"));
                    map.put("desc_task", dataobj.getString("desc_task"));
                    map.put("lavel", dataobj.getString("lavel"));
                    map.put("inactive", dataobj.getString("inactive"));
                    map.put("lead_time", dataobj.getString("lead_time"));
                    map.put("time", dataobj.getString("time"));
                    map.put("period", dataobj.getString("period"));
                    // добавляем HashList в ArrayList
                    productsList.add(map);

                }
                retroAdapter = new ReportAdapter(getActivity(), modelListViewArrayList);
                lv.setAdapter(retroAdapter);
            } else {
                Toast.makeText(getActivity(), obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        // Log.d("debug", "data result _ok");
        getTaskJSONResponse();
    }

    @SuppressLint("ShortAlarm")
    private void scheduleAlarm() {
        Intent alarmIntent = new Intent(getActivity(), ReportReceiver.class);
        alarmIntent.setAction("com.hardcoding.homework.ReoprtReciver");
        // int interval =1000*60*2;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                            notifyTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void getReport(String username, String title_task, String status, String question) {

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

                    //Log.d("debug", "respose" + response.body().toString());
                    return;
                }

                List<Post> posts = response.body();

            }

            @Override
            public void onFailure(@NotNull Call<List<Post>> call, @NotNull Throwable t) {
                //Log.d("debug", "respose error");
            }
        });
    }
}