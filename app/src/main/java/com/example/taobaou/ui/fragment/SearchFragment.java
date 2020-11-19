package com.example.taobaou.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseApplication;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.SearchRcommend;
import com.example.taobaou.model.domain.SearchResult;
import com.example.taobaou.presenter.ISearchPresenter;
import com.example.taobaou.ui.custom.TextFlowLayout;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.view.ISearchPageCallBack;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchPageCallBack, TextFlowLayout.onFlowTextItemClick {

    private ISearchPresenter mSearchPresenter;
    public TextFlowLayout  mTextFlowLayout;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_serarch;
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        mTextFlowLayout=rootView.findViewById(R.id.textflow_tfl);
        List<String> textList=new ArrayList<>();
        textList.add("键盘");
        textList.add("鼠标");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");
        textList.add("联想小新pro14");


        mTextFlowLayout.setTextList(textList);
        mTextFlowLayout.setOnFlowTextItemClick(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_bar_layout,container,false);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresentManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取推荐关键字：判断一下有没有搜索记录
        mSearchPresenter.getRecommendWords();
        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistories();
    }

    @Override
    public void onHistoriesLoad(List<String> histories) {
        //历史记录数据从这里回来
        LogUtils.d(this,"histories-------->"+histories);

    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchLoadSuccess(SearchResult result) {
   //查找数据从这里回来
        LogUtils.d(this,"result-------->"+result);

    }

    @Override
    public void onMoreLoadError() {

    }

    @Override
    public void onMoreLoadEmpty() {

    }

    @Override
    public void onRecommandWordsLoader(List<SearchRcommend.DataBean> recommandList) {
        //推荐词的数据从这里回来
        LogUtils.d(this,"recommendSize--------->"+recommandList.size());
    }

    @Override
    public void onMoreLoaded(SearchResult moreresult) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void releas() {
        mSearchPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onFlowItemClick(String text) {
        //历史记录中的item被点击
        Toast.makeText(BaseApplication.getAppContext(), text+"被点击了", Toast.LENGTH_SHORT).show();
//        mSearchPresenter.doSearch(text);
    }
}
