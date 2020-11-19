package com.example.taobaou.utils;

import com.example.taobaou.presenter.ICategoryPagerPresenter;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.presenter.IOnSellPagePresenter;
import com.example.taobaou.presenter.ISearchPresenter;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.presenter.impl.CategoryPagePresenterImp;
import com.example.taobaou.presenter.impl.HomePresentImpl;
import com.example.taobaou.presenter.ISelectedPresenter;
import com.example.taobaou.presenter.impl.OnSellPagePresenterImpl;
import com.example.taobaou.presenter.impl.SearchPresenterImpl;
import com.example.taobaou.presenter.impl.SelectedPagePresenterImpl;
import com.example.taobaou.presenter.impl.TicketPresentImp;

public class PresentManager {
    private static final PresentManager ourInstance=new PresentManager();
    private final ICategoryPagerPresenter mCategoryPagePresenterImp;
    private final IHomePresenter mHomePresent;
    private final ITicketPresenter mTicketPresentImp;
    private final ISelectedPresenter mSelectedPagePresenter;
    private final IOnSellPagePresenter mOnSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public static PresentManager getInstance(){
        return ourInstance;
    }


    private PresentManager(){
        mCategoryPagePresenterImp = new CategoryPagePresenterImp();
        mHomePresent = new HomePresentImpl();
        mTicketPresentImp = new TicketPresentImp();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mOnSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenterImpl();

    }

    public ITicketPresenter getTicketPresentImp() {
        return mTicketPresentImp;
    }

    public IHomePresenter getHomePresent() {
        return mHomePresent;
    }

    public ICategoryPagerPresenter getCategoryPagePresenter() {
        return mCategoryPagePresenterImp;
    }

    public ISelectedPresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }
    public IOnSellPagePresenter getOnSellPagePresenter(){
        return mOnSellPagePresenter;
    }
    public ISearchPresenter getSearchPresenter(){
        return mSearchPresenter;
    }

}
