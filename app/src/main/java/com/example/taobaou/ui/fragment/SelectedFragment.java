package com.example.taobaou.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.presenter.impl.ISelectedPresenter;
import com.example.taobaou.ui.activity.TicketActivity;
import com.example.taobaou.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaou.ui.adapter.SelectedPageRightAdapter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.SizeUtils;
import com.example.taobaou.view.ISelctedPageCallBack;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelctedPageCallBack, SelectedPageLeftAdapter.OnLeftfItemClickListener, SelectedPageRightAdapter.OnSelectedPageItemClick {

    private ISelectedPresenter mSelectedPagePresenter;

    @BindView(R.id.left_cagegories)
    public RecyclerView left_categoriesList;

    @BindView(R.id.right_content)
    public RecyclerView right_contetnList;


    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageRightAdapter mRightAdapter;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        left_categoriesList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        left_categoriesList.setAdapter(mLeftAdapter);
        //对右侧的数据及界面进行设置
        right_contetnList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageRightAdapter();
        right_contetnList.setAdapter(mRightAdapter);
        right_contetnList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBootm = SizeUtils.dip2px(getContext(), 4);
                int  leftAndRight=SizeUtils.dip2px(getContext(),6);
                outRect.top=topAndBootm;
                outRect.bottom=topAndBootm;
                outRect.left=leftAndRight;
                outRect.right=leftAndRight;
            }
        });

    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setonLeftfItemClickListener(this);
        mRightAdapter.setOnSelectedPageItemClick(this);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresentManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();


    }

    @Override
    public void onCategoryLoad(SelectedPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setMdata(categories);
        //数据在这里回来分类内容
        LogUtils.d(this,"categories:"+categories.toString());
        //todo: 更新UI
        //根据当前选中分类,获取分类内容
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));


    }

    @Override
    public void onContentLoad(SelectedContent content) {
        mRightAdapter.setData(content);
        right_contetnList.scrollToPosition(0);


    }

    @Override
    protected void onRetryClick() {
        //点击重试
        if (mSelectedPagePresenter!=null){
            mSelectedPagePresenter.reloadContent();
        }
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

    }

    @Override
    protected void releas() {
        super.releas();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onItemLeftClick(SelectedPageCategory.DataBean item) {
        //左边分类的点击
        mSelectedPagePresenter.getContentByCategory(item);
        LogUtils.d(this,"current position---->"+item.getFavorites_title());
    }

    @Override
    public void onItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {
        //右边内容点击
        handleItemClick(item);
    }
    private void handleItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {
        //处理数据
        String title=item.getTitle();
        //领券地址
        //详情地址
        String url=item.getCoupon_click_url();
        if (TextUtils.isEmpty(url)){
            url=item.getClick_url();
        }
        String cover=item.getPict_url();


        ITicketPresenter ticketPresenter=PresentManager.getInstance().getTicketPresentImp();
        ticketPresenter.getTicket(title,url,cover);

        startActivity(new Intent(getContext(), TicketActivity.class));

    }
}
