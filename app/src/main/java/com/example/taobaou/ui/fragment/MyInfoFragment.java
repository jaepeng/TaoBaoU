package com.example.taobaou.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.ui.activity.LoginActivity;
import com.example.taobaou.ui.activity.RegisterActivity;
import com.example.taobaou.utils.engine.GlideEngine;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class MyInfoFragment extends BaseFragment {
    @BindView(R.id.iv_myInfo_Image)
    ImageView iv_myImage;
    @BindView(R.id.tv_myInfo_userName)
    TextView tv_username;
    @BindView(R.id.tv_myInfo_coupon_history)
    TextView tv_couponHistory;
    @BindView(R.id.btn_myinfo_login)
    Button btnLogin;
    @BindView(R.id.fragment_bar_titel_tv)
    TextView tv_bar_title;
    @BindView(R.id.btn_myinfo_register)
    Button btnRegister;
    @BindView(R.id.btn_myinfo_unregister)
    Button btnUnRegister;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_my_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(View rootView) {
        //只有Success了才会显示界面信息
        setUpState(State.SUCCESS);
        tv_bar_title.setText("我的信息");
        loginHide(false);
        //点击注册按钮进入注册界面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRegister();
            }
        });
        //点击登录按钮,进入登录界面
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLogin();

            }
        });
        btnUnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHide(false);
            }
        });
        tv_couponHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHistory();
            }
        });
        iv_myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeadImage();
            }
        });

    }

    private void setHeadImage() {
        EasyPhotos.createAlbum(this,true, GlideEngine.getInstance()).start(new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                Glide.with(getActivity()).load(paths.get(0)).into(iv_myImage);
            }
        });
    }

    private void goHistory() {
//        startActivity(new Intent(getActivity(), TicketHistoryActivity.class));
    }

    //处理EventBus传递来的数据
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageCode()== MessageCode.LOGINUSERACCOUNT){
            //进入登录状态
            loginHide(true);
            tv_username.setText(messageEvent.getMkeyword());
        }

        if (messageEvent.getMessageCode()==MessageCode.REGISTERUSERACCOUNT){
            //注册完了
            loginHide(true);
            tv_username.setText(messageEvent.getMkeyword());
        }
        if (messageEvent.getMessageCode()==MessageCode.FACEREGISTERSUCCESS){
            Log.d("jae", "Register success onEvent: "+messageEvent.getMkeyword());
            loginHide(true);
            tv_username.setText(messageEvent.getMkeyword());
        }


    }

    private void goLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));


    }

    private void goRegister() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));

    }

    public void loginHide(Boolean isLogin){

        if (isLogin){
            //登录状态
            showViwe(iv_myImage,tv_username,tv_couponHistory,btnUnRegister);
            hideView(btnLogin,btnRegister);

        }else{
            //未登录状态
            showViwe(btnLogin,btnRegister);
            hideView(iv_myImage,tv_username,tv_couponHistory,btnUnRegister);

        }

    }
    /*
    让View不可见
     */
    public void hideView(View... views){
        for (View view : views) {

            if (view.getVisibility()==View.VISIBLE){
                view.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void showViwe(View... views){
        for (View view : views) {

            if (view.getVisibility()==View.INVISIBLE){
                view.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
