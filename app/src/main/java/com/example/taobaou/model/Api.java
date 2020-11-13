package com.example.taobaou.model;

import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {
    @GET("discovery/categories")
    Call<Categories> getCategory();

    @GET
    Call<HomePagerContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);
}
