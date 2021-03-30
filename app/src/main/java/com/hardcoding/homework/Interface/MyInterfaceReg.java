package com.hardcoding.homework.Interface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterfaceReg {

    String JSONURL = "http://homefit.beget.tech/api/";

    @GET("reg.php")
    Call<String> getString();

}
