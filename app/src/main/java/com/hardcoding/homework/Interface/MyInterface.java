package com.hardcoding.homework.Interface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface {

    String JSONURL = "http://homefit.beget.tech/api/";

    @GET("task.php")
    Call<String> getString();

    @GET("status.php")
    Call<String> getStat();

}
