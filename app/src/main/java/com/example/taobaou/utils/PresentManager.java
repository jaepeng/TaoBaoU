package com.example.taobaou.utils;

import com.example.taobaou.presenter.ICategoryPagerPresenter;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.presenter.impl.CategoryPagePresenterImp;
import com.example.taobaou.presenter.impl.HomePresentImpl;
import com.example.taobaou.presenter.impl.ISelectedPresenter;
import com.example.taobaou.presenter.impl.SelectedPagePresenterImpl;
import com.example.taobaou.presenter.impl.TicketPresentImp;

public class PresentManager {
    private static final PresentManager ourInstance=new PresentManager();
    private final ICategoryPagerPresenter mCategoryPagePresenterImp;
    private final IHomePresenter mHomePresent;
    private final ITicketPresenter mTicketPresentImp;
    private final ISelectedPresenter mSelectedPagePresenter;

    public static PresentManager getInstance(){
        return ourInstance;
    }
    private PresentManager(){
        mCategoryPagePresenterImp = new CategoryPagePresenterImp();
        mHomePresent = new HomePresentImpl();
        mTicketPresentImp = new TicketPresentImp();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();

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
}
