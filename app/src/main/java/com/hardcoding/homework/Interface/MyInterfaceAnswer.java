package com.hardcoding.homework.Interface;

import com.hardcoding.homework.Post;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MyInterfaceAnswer {


    @GET("setanswer.php")
    Call<Post> getPosts(
            @Query("username") String username,
            @Query("answer") String answer
    );

    @GET("setanswer.php")
    Call<Post> getPosts(@QueryMap Map<String, String> parameters);

    @GET("setanswer.php")
    Call<String> getString(@QueryMap Map<String, String> parameters);

}
