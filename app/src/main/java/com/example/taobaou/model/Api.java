package com.example.taobaou.model;

import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.model.domain.OnSellContetn;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.model.domain.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {
    @GET("discovery/categories")
    Call<Categories> getCategory();

    @GET
    Call<HomePagerContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelcetdPageCategories();

    @GET()
    Call<SelectedContent> getSelectedContent(@Url String url);

    @GET()
    Call<OnSellContetn> getOnSellContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRcommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page")int page,@Query("keyword")String keyword);

    @POST("/firstdemo/add")
    Call<Boolean> addUser(@Body User user);


}
