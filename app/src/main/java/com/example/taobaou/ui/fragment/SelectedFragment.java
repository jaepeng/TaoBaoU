package com.example.taobaou.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.model.domain.SelectedPageCategory;
import com.example.taobaou.presenter.impl.ISelectedPresenter;
import com.example.taobaou.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.view.ISelctedPageCallBack;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelctedPageCallBack, SelectedPageLeftAdapter.OnLeftfItemClickListener {

    private ISelectedPresenter mSelectedPagePresenter;

    @BindView(R.id.left_cagegories)
    public RecyclerView left_categoriesList;

    @BindView(R.id.right_content)
    public RecyclerView right_contetnList;
    private SelectedPageLeftAdapter mLeftAdapter;

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
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setonLeftfItemClickListener(this);
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
        if (content!=null) {

            LogUtils.d(this,"onContentLoad"+content.toString());
        }
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
        super.releas();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onItemLeftClick(SelectedPageCategory.DataBean item) {
        //左边分类的点击
        LogUtils.d(this,"current position---->"+item.getFavorites_title());
    }
}
