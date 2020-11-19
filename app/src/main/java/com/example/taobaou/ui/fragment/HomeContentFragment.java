package com.example.taobaou.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.ui.activity.IMainActivity;
import com.example.taobaou.ui.adapter.HomePagerAdapter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeContentFragment extends BaseFragment implements IHomeCallback {


    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;
    @BindView(R.id.home_search_input_box)
    public EditText mSearchInputBox;

    private IHomePresenter mHomePresent;
    @BindView(R.id.home_page)
    public ViewPager homePager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenter() {
        //创建presenter
        mHomePresent = PresentManager.getInstance().getHomePresent();
        mHomePresent.registerViewCallback(this);
        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //跳转到SearchFragment
                FragmentActivity activity = getActivity();
                if (activity instanceof IMainActivity){
                    ((IMainActivity)activity).switch2Serch();
                }
            }
        });
        loadData();
    }

    @Override
    protected void onRetryClick() {
        //网络错误被点击重试
        //重新加载分类
        if (mHomePresent!=null) {
            mHomePresent.getCategories();
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {


        //加载数据
        mHomePresent.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        //加载的数据就会从这里回来
        if (categories!=null) {
//            homePager.setOffscreenPageLimit(categories.getData().size());
            mHomePagerAdapter.setCategories(categories);
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
        setUpState(State.EMPTY);
    }

    @Override
    protected void releas() {
        //取消注册
        if (mHomePagerAdapter!=null) {
            mHomePresent.unregisterViewCallback(this);
        }
    }

    //覆盖BaseFragment中的initView方法

    @Override
    protected void initView(View rootView) {
        //初始化各种控件
        mTabLayout.setupWithViewPager(homePager);
        //给viewPager设置适配器
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        //设置Adapter
        homePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d(this,"on create view....");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        LogUtils.d(this,"on destoryVIew..");
        super.onDestroy();

    }
}
