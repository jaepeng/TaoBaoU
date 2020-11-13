package com.example.taobaou.view;

import com.example.taobaou.base.IBaseCallBack;
import com.example.taobaou.model.domain.Categories;

public interface IHomeCallback extends IBaseCallBack {
    void onCategoriesLoaded(Categories categories);

}
