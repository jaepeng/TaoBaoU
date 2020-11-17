package com.example.taobaou.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobaou.model.domain.IBaseInfo;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.ui.activity.TicketActivity;

public class TickUtils {
    public static void toTicketPage(Context context,IBaseInfo baseInfo){
        //处理数据
        String title=baseInfo.getTitle();
        //领券地址
        //详情地址
        String url=baseInfo.getUrl();
        if (TextUtils.isEmpty(url)){
            url=baseInfo.getUrl();
        }
        String cover=baseInfo.getCover();


        ITicketPresenter ticketPresenter=PresentManager.getInstance().getTicketPresentImp();
        ticketPresenter.getTicket(title,url,cover);

        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
