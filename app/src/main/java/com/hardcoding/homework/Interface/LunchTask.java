package com.hardcoding.homework.Interface;

import com.hardcoding.homework.Post;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface LunchTask {

    @GET("setlunch.php")
    Call<List<Post>> getPosts(
            @Query("username") String username,
            @Query("id") String id
    );

    @GET("setlunch.php")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);


    @GET("setstop.php")
    Call<List<Post>> getStop(
            @Query("username") String username,
            @Query("id") String id
    );
    @GET("report.php")
    Call<List<Post>> getReports(
            @Query("username") String username,
            @Query("data") String data,
            @Query("status") String status,
            @Query("question") String question
    );
    @GET("setstop.php")
    Call<List<Post>> getStop(@QueryMap Map<String, String> parameters);

    @GET("statustask.php")
    Call<List<Post>> getStat(
            @Query("username") String username
    );

    @GET("statustask.php")
    Call<List<Post>> getStat(@QueryMap Map<String, String> parameters);

    @GET("statustask.php")
    Call<String> getStatus(@QueryMap Map<String, String> parameters);

    @GET("report.php")
    Call<List<Post>> getReports(@QueryMap Map<String, String> parameters);
}
