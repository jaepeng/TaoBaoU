package com.example.taobaou.presenter;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();

}
