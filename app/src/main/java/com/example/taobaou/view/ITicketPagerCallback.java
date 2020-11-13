package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallBack {

    //加载结果
    void onTicketLoader(String cover, TicketResult ticketResult);
}
