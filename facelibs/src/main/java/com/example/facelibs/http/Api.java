package com.example.facelibs.http;

import com.example.facelibs.model.FaceRegisterInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {


    /**
     * 获取所有人脸数据
     */
    @GET("/faceRegister/getAllInfo")
    Call<List<FaceRegisterInfo>> findAllRegisterFace();

    @POST("/faceRegister/addRegisterInfo")
    Call<Boolean> addRegisterFace(@Body FaceRegisterInfo faceRegisterInfo);





}
