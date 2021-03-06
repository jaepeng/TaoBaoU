package com.example.taobaou.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.ui.fragment.HomeContentFragment;
import com.example.taobaou.ui.fragment.MyInfoFragment;
import com.example.taobaou.ui.fragment.OnSellFragment;
import com.example.taobaou.ui.fragment.SearchFragment;
import com.example.taobaou.ui.fragment.SelectedFragment;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;

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
    private MyInfoFragment mMyInfoFragment;
    public static final String TAG="MainActivity";
    //人脸识别使用权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    boolean libraryExists = true;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;





    public static void startActivity(Context context, String string){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtra(Constants.RETURN_MAIN_FROM_OTHER,string);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate::"+SharedPreferenceManager.getInstance().isFirstLogin());
//        if (){
//            activeEngine(null);
//        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent=getIntent();
        String string = intent.getStringExtra(Constants.RETURN_MAIN_FROM_OTHER);
        if (TextUtils.isEmpty(string)){
            return;
        }else{
            if (Integer.valueOf(string)==Constants.GO_TO_MYINFO_FRAGMENT){
                //跳转到我的信息界面
                switchFragment(mMyInfoFragment);
            }
        }

    }

    @Override
    protected void initPresenter() {

    }

    private void initFragment() {
        mHomeFragment = new HomeContentFragment();
        mRedPackageFragment = new OnSellFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mMyInfoFragment = new MyInfoFragment();
        mFm = getSupportFragmentManager();
        //切换到搜索碎片，然后再直接切回来，这样SearchFragment就初始化好了。
        switchFragment(mSearchFragment);
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

                }else if (item.getItemId()==R.id.myinfo){
                    switchFragment(mMyInfoFragment);
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
        //切换下面的图标
        mNavigationView.setSelectedItemId(R.id.search);

    }

    @Override
    public void switch2Serch(String keyword) {
        Log.d(TAG, "switch2Serch: before");
        EventBus.getDefault().postSticky(new MessageEvent(MessageCode.ANALYSRESULTSEARCH,keyword));
        Log.d(TAG, "switch2Serch: after");
        mNavigationView.setSelectedItemId(R.id.search);

    }
    /**
     * 激活人脸识别SDK
     */


}
