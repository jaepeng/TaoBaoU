package com.example.taobaou.model;

import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.model.domain.OnSellContetn;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.model.domain.TicketHistory;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.model.domain.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    /**
     * 注册用户
     * @param user
     * @return
     */
    @POST("/firstdemo/add")
    Call<Boolean> addUser(@Body User user);


    /**
     * 登录判断
     * @param useraccount
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/firstdemo/login")
    Call<User> login(@Field("username") String useraccount,@Field("password") String password);

    @GET("/firstdemo/findByName")
    Call<User> findUserByName(@Query("username")String useraccont);

    /**
     *
     * 添加领券记录
     * @param ticketHistory
     * @return
     */
    @POST("/ticketHistory/add")
    Call<Boolean> addTicketHistory(@Body TicketHistory ticketHistory);


    /**
     * 根据用户名查找所有领券数据
     * @param account
     * @return
     */
    @GET("/ticketHistory/findAll?")
    Call<List<TicketHistory>> findAllTicketHistory(@Query("username") String account);

    @GET("/firstdemo/getAllName")
    Call<List<String>> getAllUserName();

}
