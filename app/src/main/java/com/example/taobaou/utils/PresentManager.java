package com.example.taobaou.utils;

import com.example.taobaou.presenter.ICategoryPagerPresenter;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.presenter.impl.CategoryPagePresenterImp;
import com.example.taobaou.presenter.impl.HomePresentImpl;
import com.example.taobaou.presenter.impl.TicketPresentImp;

public class PresentManager {
    private static final PresentManager ourInstance=new PresentManager();
    private final ICategoryPagerPresenter mCategoryPagePresenterImp;
    private final IHomePresenter mHomePresent;
    private final ITicketPresenter mTicketPresentImp;

    public static PresentManager getInstance(){
        return ourInstance;
    }
    private PresentManager(){
        mCategoryPagePresenterImp = new CategoryPagePresenterImp();
        mHomePresent = new HomePresentImpl();
        mTicketPresentImp = new TicketPresentImp();

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
}
