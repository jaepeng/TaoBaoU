package com.example.taobaou.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.baidusearch.model.AdvancedGeneral;
import com.example.baidusearch.model.AnalysisResult;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.domain.Categories;
import com.example.taobaou.presenter.IHomePresenter;
import com.example.taobaou.ui.activity.IMainActivity;
import com.example.taobaou.ui.adapter.HomePagerAdapter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.engine.GlideEngine;
import com.example.taobaou.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

import butterknife.BindView;

public class HomeContentFragment extends BaseFragment implements IHomeCallback, View.OnClickListener {


    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;
    @BindView(R.id.home_search_input_box)
    public EditText mSearchInputBox;
    @BindView(R.id.scan_icon)
    ImageView ivTakephoto;

    private IHomePresenter mHomePresent;
    @BindView(R.id.home_page)
    public ViewPager homePager;
    private HomePagerAdapter mHomePagerAdapter;
    private String mKeyWord;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                Log.d("jae_keyword", "要走了！ mKeyWord"+mKeyWord);
                FragmentActivity activity = getActivity();
                setUpState(State.SUCCESS);
                if (activity instanceof IMainActivity) {
                    ((IMainActivity) activity).switch2Serch(mKeyWord);
                }
            }
        }
    };


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
        ivTakephoto.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        //在数据返回前进入loding状态
        setUpState(State.LOADING);
        //点击了拍照按钮，需要拿到数据后进行跳转。
        EasyPhotos.createAlbum(this,true, GlideEngine.getInstance()).start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       String s = AdvancedGeneral.advancedGeneral(paths.get(0),getContext());
                       AnalysisResult analysisResult=new AnalysisResult(s);
                       mKeyWord= analysisResult.getKeyWord();
                       Log.d("jae_key", "run: "+mKeyWord);
                       Message message=mHandler.obtainMessage();
                       message.what=1;
                       mHandler.sendMessage(message);
                   }
               }).start();



            }
        });


    }


}
