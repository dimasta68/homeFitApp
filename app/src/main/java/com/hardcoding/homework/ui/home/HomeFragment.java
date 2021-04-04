package com.hardcoding.homework.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hardcoding.homework.BuildConfig;
import com.hardcoding.homework.DetailsActivity;
import com.hardcoding.homework.Interface.LunchTask;
import com.hardcoding.homework.Interface.MyInterface;
import com.hardcoding.homework.ModelListView;
import com.hardcoding.homework.R;
import com.hardcoding.homework.RetroAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class HomeFragment extends Fragment {
    ListView lv;
    public ArrayList<HashMap<String, String>> productsList;
    private HomeViewModel homeViewModel;
    public RetroAdapter retroAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lv = view.findViewById(R.id.list);
        Paper.init(requireActivity());
        mSwipeRefreshLayout = view.findViewById(R.id.swap);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getTaskJSONResponse();
            }
        });
        ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();
        productsList = new ArrayList<HashMap<String, String>>();
        String username = Paper.book().read("mail");
        getTaskJSONResponse();
        //getStatus(username);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView title = (TextView) view.findViewById(R.id.title_task);
                TextView desc = (TextView) view.findViewById(R.id.desc_task);
                TextView pid = (TextView) view.findViewById(R.id.pid);
                TextView inactive = (TextView) view.findViewById(R.id.inactive);
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("descript", desc.getText().toString());
                intent.putExtra("pid", pid.getText().toString());
                intent.putExtra("inactive", inactive.getText().toString());
                Log.d("debug","positon click"+position);
               intent.putExtra("position",position);


                //Log.d("debug","title"+title.getText().toString());
                startActivity(intent);
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
                Log.d("debug", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("debug", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeListView(jsonresponse);

                    } else {
                        //   Log.d("debug", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
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
            if (obj.optString("success").equals("1")) {

                ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("products");

                for (int i = 0; i < dataArray.length(); i++) {

                    ModelListView modelListView = new ModelListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    Log.d("debug", "allfragmetn data " + dataobj);
                    //  modelListView.se(dataobj.getString("imgURL"));
                    modelListView.setId(dataobj.getString("id"));
                    modelListView.setCat(dataobj.getString("cat"));
                    modelListView.setTitle(dataobj.getString("title_task"));
                    modelListView.setDecsript(dataobj.getString("desc_task"));
                    modelListView.setLavel(dataobj.getString("lavel"));
                    modelListView.setInactive(dataobj.getString("inactive"));

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
                    // добавляем HashList в ArrayList
                    productsList.add(map);

                }

                retroAdapter = new RetroAdapter(getActivity(), modelListViewArrayList);
                lv.setAdapter(retroAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                Toast.makeText(getActivity(), obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}