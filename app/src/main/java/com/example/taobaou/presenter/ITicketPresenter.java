package com.example.taobaou.presenter;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.view.ITicketPagerCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {
    //获取优惠券,生成淘口令
    void getTicket(String title,String url,String cover);
}
