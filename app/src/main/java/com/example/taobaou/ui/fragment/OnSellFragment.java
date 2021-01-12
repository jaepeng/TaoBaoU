package com.example.taobaou.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.IBaseInfo;
import com.example.taobaou.model.domain.OnSellContetn;
import com.example.taobaou.presenter.IOnSellPagePresenter;
import com.example.taobaou.ui.adapter.OnSellPageAdapter;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.SizeUtils;
import com.example.taobaou.utils.TickUtils;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.view.IOnSellPageCallbak;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallbak, OnSellPageAdapter.OnSellPageItemClickListener {

    private static final int DEFAULT_SPANCOUNT = 2;
    private IOnSellPagePresenter mOnSellPagePresenter;



    @BindView(R.id.onsell_contetn_list)
    public RecyclerView mContenList;
    @BindView(R.id.fragment_bar_titel_tv)
    public TextView fragment_bartitel;

    @BindView(R.id.onSell_refresh_layout)
    public TwinklingRefreshLayout mContetnTkRf;
    private OnSellPageAdapter mOnSellPageAdapter;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_onsell;
    }

    @Override
    protected void initView(View rootView) {

        fragment_bartitel.setText("特惠宝贝");
        mOnSellPageAdapter = new OnSellPageAdapter();
        //设置布局管理器
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),DEFAULT_SPANCOUNT);
        mContenList.setLayoutManager(gridLayoutManager);
        mContenList.setAdapter(mOnSellPageAdapter);
        mContenList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
               outRect.top= SizeUtils.dip2px(getContext(),2.5f);
               outRect.bottom= SizeUtils.dip2px(getContext(),2.5f);
               outRect.left= SizeUtils.dip2px(getContext(),2.5f);
               outRect.right= SizeUtils.dip2px(getContext(),2.5f);

            }
        });
        mContetnTkRf.setEnableLoadmore(true);
        mContetnTkRf.setEnableRefresh(false);
        mContetnTkRf.setEnableOverScroll(true);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mContetnTkRf.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //去加载更多数据
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });

        mOnSellPageAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresentManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnsellContent();
    }

    @Override
    public void onContentLoadSuccess(OnSellContetn result) {
        setUpState(State.SUCCESS);
        //数据从这里回来
        //更新UI
        mOnSellPageAdapter.setData(result);

    }


    @Override
    protected void releas() {
        super.releas();
       if (mOnSellPagePresenter!=null){
           mOnSellPagePresenter.unregisterViewCallback(this);
       }
    }

    @Override
    public void onMoreLoaded(OnSellContetn moreResult) {
        mContetnTkRf.finishLoadmore();
        //添加内容到列表中
        mOnSellPageAdapter.onMoreLoad(moreResult);

    }

    @Override
    public void onMoreLoadError() {
        mContetnTkRf.finishLoadmore();
        ToastUtsils.showToast("网络异常,请稍后重试");
    }

    @Override
    public void onMoreLoadEmpty() {
        mContetnTkRf.finishLoadmore();
        ToastUtsils.showToast("已经到底部了!");

    }

    @Override
    public void onError() {
        setUpState(State.ERROR);

    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo item) {
        //特惠列表内容被点击
        TickUtils.toTicketPage(getContext(),item);


    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container,false);
    }
}
