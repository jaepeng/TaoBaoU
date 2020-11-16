package com.example.taobaou.presenter.impl;

import com.example.taobaou.base.IBasePresenter;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.view.ISelctedPageCallBack;

public interface ISelectedPresenter extends IBasePresenter<ISelctedPageCallBack> {
    //获取分类
    void getCategories();

    /**
     * 根据分类,获取分类内容
     *
     * @param item
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    //重新加载内容
    void reloadContent();

}
