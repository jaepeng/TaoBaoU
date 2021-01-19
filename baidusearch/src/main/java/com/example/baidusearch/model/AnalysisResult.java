package com.example.baidusearch.model;

import android.util.Log;

import com.example.baidusearch.bean.SearchResultBean;
import com.example.baidusearch.utils.GsonUtils;

public class AnalysisResult {
    public String mSearchResultString;
    public SearchResultBean mSearchResultBean;

    public AnalysisResult(String searchResultBean) {
        Log.d("AnalysisResult", "AnalysisResult_searchResultBean: "+searchResultBean);
        if (searchResultBean.isEmpty()) {
            return;
        }
        mSearchResultString = searchResultBean;
        mSearchResultBean = GsonUtils.fromJson(searchResultBean, SearchResultBean.class);
    }

    public String getKeyWord(){
        for (SearchResultBean.ResultBean resultBean : mSearchResultBean.getResult()) {
            if (resultBean.getRoot().contains("商品")){
                return resultBean.getKeyword();
            }

        }
        return mSearchResultBean.getResult().get(0).getKeyword();
    }
}
