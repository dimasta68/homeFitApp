package com.hardcoding.homework.Interface;

import com.hardcoding.homework.Interface.JsonPlaceHolderApi;
import com.hardcoding.homework.Post;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface JsonPlaceHolderApi {

    @GET("answer.php")
    Call<List<Post>> getPosts(
            @Query("username") String username,
            @Query("answer") String answer
    );

    @GET("answer.php")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);



}
