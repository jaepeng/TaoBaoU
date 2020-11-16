package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;

public interface ISelctedPageCallBack extends IBaseCallBack {
    //分类内容获取
    void onCategoryLoad(SelectedPageCategory categories);

    //内容
    void onContentLoad(SelectedContent content);


}
