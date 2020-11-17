package com.example.taobaou.ui.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.model.domain.IBaseInfo;
import com.example.taobaou.presenter.ICategoryPagerPresenter;
import com.example.taobaou.ui.adapter.HomePageContentAdapter;
import com.example.taobaou.ui.adapter.LooperPagerAdapter;
import com.example.taobaou.ui.custom.AutoLoopViewPager;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.SizeUtils;
import com.example.taobaou.utils.TickUtils;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollVIew;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePageContentAdapter.OnListeItemClickListener, LooperPagerAdapter.OnLooperItemClickListener {

    private ICategoryPagerPresenter mCatgoryPagePresenter;
    private int mMaterialId;
    private HomePageContentAdapter mContetnADapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category){
        HomePagerFragment homePagerFragment = new HomePagerFragment();

        Bundle bundle=new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGE_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGE_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);

        return homePagerFragment;
    }
    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;
    @BindView(R.id.home_Pager_title)
    public TextView currentCuategoryTitleTv;
    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;
    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;
    @BindView(R.id.home_page_parent)
    public LinearLayout homePagerParent;
    @BindView(R.id.home_pager_neted_scroll)
    public TbNestedScrollVIew homePagerNestedView;
    @BindView(R.id.home_pager_header_containar)
    public LinearLayout homeHeaderContainer;




    @Override
    protected void loadData() {
        Bundle arguments=getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGE_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGE_MATERIAL_ID);
        //TODO:加载数据
//        LogUtils.d(this,"title--->"+title);
//        LogUtils.d(this,"id--->"+ mMaterialId);
        if (mCatgoryPagePresenter!=null) {
            mCatgoryPagePresenter.getConteantByCategoryId(mMaterialId);
        }
        if (currentCuategoryTitleTv!=null) {
            currentCuategoryTitleTv.setText(title);
        }

    }

    @Override
    protected void initPresenter() {
        mCatgoryPagePresenter = PresentManager.getInstance().getCategoryPagePresenter();
        mCatgoryPagePresenter.registerViewCallback(this);

    }

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_home_pager;
    }


    @Override
    protected void initView(View rootView) {

        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top=8;
                outRect.bottom=8;
            }
        });
        //创建适配器
        mContetnADapter = new HomePageContentAdapter();

        //设置适配器
        mContentList.setAdapter(mContetnADapter);

        //创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置适配器

        looperPager.setAdapter(mLooperPagerAdapter);

        //设置刷新相关内容
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableLoadmore(true);
//        mTwinklingRefreshLayout.setBottomView();



    }

    @Override
    protected void initListener() {

        mContetnADapter.setOnListeItemClickListener(this);


        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //布局改变时
                if (homeHeaderContainer==null){
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measuredHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)mContentList.getLayoutParams();
                layoutParams.height=measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight!=0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换指示器
                if (mLooperPagerAdapter.getDataSize()==0){
                    return;
                }
                int targetPosition=position%mLooperPagerAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLooperPagerAdapter.setOnLooperItemClickListener(this);
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(this,"触发load more");
                //加载更多内容
                if (mCatgoryPagePresenter!=null) {
                    mCatgoryPagePresenter.loadMore(mMaterialId);
                }


            }


            //下拉刷新只不过是将数据放到头部去 addAll(0,contest)这样就可以了.
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//            }
        });
    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */

    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point =looperPointContainer.getChildAt(i);
            if (i==targetPosition){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else{

                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }

    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
            //数据列表接在
        //TODO:更新UI

        mContetnADapter.setMdata(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public void onLoading() {

        setUpState(State.LOADING);

    }

    @Override
    public void onError() {

        setUpState(State.ERROR);

    }

    @Override
    public void onEmpty() {

        setUpState(State.EMPTY);

    }

    @Override
    public void onLoaderMoreError() {
        ToastUtsils.showToast("网络异常,请稍后重试");
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }

    }

    @Override
    public void onLoaderMoreempty() {
    //给一个提示
        ToastUtsils.showToast("没有数据了");
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }


    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的底部
        mContetnADapter.addData(contents);
        if (mTwinklingRefreshLayout!=null) {
            mTwinklingRefreshLayout.finishLoadmore();
        }
        ToastUtsils.showToast("加载了+"+contents.size()+"个数据");


    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(this,"loop--->"+contents.size());
        if(contents.size()==0){
            return;
        }
        Context mcontext=getContext();
        mLooperPagerAdapter.setData(contents);
        //设置到中间点——因为MAxValue/2不一定能%5所以，可能不是第一张图片
        //处理
        int dx=(Integer.MAX_VALUE/2)%contents.size();
        int targetPosition = (Integer.MAX_VALUE / 2) - dx;
        looperPager.setCurrentItem(targetPosition);

        looperPointContainer.removeAllViews();

//        normalDrawable.setColor(Color.rgb(255,255,255));
        int size = SizeUtils.dip2px(mcontext, 8);


        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View point=new View(mcontext);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin=SizeUtils.dip2px(mcontext,5);
            layoutParams.rightMargin=SizeUtils.dip2px(mcontext,5);
            point.setLayoutParams(layoutParams);
            if (i==0){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else{
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }

            looperPointContainer.addView(point);
            
        }

    }

    @Override
    protected void releas() {
        if (mCatgoryPagePresenter!=null) {
            mCatgoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始轮播
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    public void onItemClickListener(IBaseInfo item) {
        //当前列表内容
        LogUtils.d(this,"item click--->"+item.getTitle());
        handleItemClick(item);
    }


    @Override
    public void onLooperTimeClick(IBaseInfo item) {
        LogUtils.d(this,"loop click--->"+item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(IBaseInfo item) {

        TickUtils.toTicketPage(getContext(),item);

    }
}
