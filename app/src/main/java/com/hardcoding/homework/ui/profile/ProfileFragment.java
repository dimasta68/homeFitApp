package com.hardcoding.homework.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hardcoding.homework.BuildConfig;
import com.hardcoding.homework.Interface.MyInterfaceAnswer;
import com.hardcoding.homework.R;
import com.hardcoding.homework.RetroAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileFragment extends Fragment {

    private ProfileViewModel homeViewModel;
    ListView lv;
    ArrayList<HashMap<String, String>> productsList;
    private RetroAdapter retroAdapter;
    TextView TxtName, TxtAnswer;
    String textname, textAnswer;
    public String mail;
    String[] answerArr;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        TxtName = (TextView) root.findViewById(R.id.textMail);
        TxtAnswer = (TextView) root.findViewById(R.id.anketa);
        Paper.init(requireActivity());
        mail = Paper.book().read("mail").toString();
        Log.d("debug", "mail" + mail);
        getUp(mail);

        return root;

    }

    public void getUp(String username) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://homefit.beget.tech/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

        MyInterfaceAnswer myInterfaceAnswer = retrofit.create(MyInterfaceAnswer.class);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);

        Call<String> call = myInterfaceAnswer.getString(parameters);

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("debug", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            JSONObject jsonObj = new JSONObject(response.body());
                            JSONArray items = jsonObj.getJSONArray("products");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject c = items.getJSONObject(i);
                                String id = c.getString("id");
                                textname = c.getString("username");
                                textAnswer = c.getString("answer");
                                Log.d("debug", "texansrew" + textAnswer);
                                TxtName.setText(textname);

                            }
                            textAnswer=textAnswer.replace(",","\n\n")
                                    .replace("[","")
                                    .replace("]","");
                            TxtAnswer.setText(textAnswer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {

            }


        });
    }
}