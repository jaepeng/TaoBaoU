package com.example.taobaou.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.ui.fragment.HomeContentFragment;
import com.example.taobaou.ui.fragment.OnSellFragment;
import com.example.taobaou.ui.fragment.SearchFragment;
import com.example.taobaou.ui.fragment.SelectedFragment;
import com.example.taobaou.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements IMainActivity{


    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeContentFragment mHomeFragment;
    private OnSellFragment mRedPackageFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;
    private Unbinder mbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }



    @Override
    protected void initPresenter() {

    }

    private void initFragment() {
        mHomeFragment = new HomeContentFragment();
        mRedPackageFragment = new OnSellFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    @Override
    protected void initEvent() {
        initlistener();
    }

    @Override
    protected void initView() {
        initFragment();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void initlistener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              //  LogUtils.d(MainActivity.class,"item.id:"+item.getItemId()+"  ,item.title:"+item.getTitle());
                if (item.getItemId()==R.id.home){
                    LogUtils.d(this,"首页");
                    switchFragment(mHomeFragment);

                }else if (item.getItemId()==R.id.selected) {
                   LogUtils.i(this,"精选");
                   switchFragment(mSelectedFragment);
                }else if (item.getItemId()==R.id.search) {
                    LogUtils.w(this,"搜索");
                    switchFragment(mSearchFragment);
                }else if(item.getItemId()==R.id.red_package){
                    LogUtils.e(this,"特惠");
                    switchFragment(mRedPackageFragment);

                }

                return true;//这里默认返回false,如果返回ture则表示消费该方法，那么图标才会开始变化

                }
        });
    }
//上一次显示的Fragment
    private BaseFragment lastoneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //修改成add和Hide的方式,来控制Fragment的切换
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        } else {


            fragmentTransaction.show(targetFragment);

        }
        if (lastoneFragment != null&&targetFragment!=lastoneFragment) {
            fragmentTransaction.hide(lastoneFragment);
        }
        lastoneFragment = targetFragment;
        fragmentTransaction.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mbind!=null) {
            mbind.unbind();
        }
    }


    /**
     * 跳转到SearchFragment
     * 实现主界面点击搜索框能够跳到SearchFragmnet
     */
    @Override
    public void switch2Serch(){
//        switchFragment(mSearchFragment);
        //切花下面的图标
        mNavigationView.setSelectedItemId(R.id.search);

    }
}
