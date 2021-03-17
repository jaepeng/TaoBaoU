package com.example.taobaou.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseFragment;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.User;
import com.example.taobaou.model.message.MessageCode;
import com.example.taobaou.model.message.MessageEvent;
import com.example.taobaou.ui.activity.ChangePswActivity;
import com.example.taobaou.ui.activity.LoginActivity;
import com.example.taobaou.ui.activity.RegisterActivity;
import com.example.taobaou.ui.activity.TicketHistoryActivity;
import com.example.taobaou.ui.activity.face.FaceRegisetrActivity;
import com.example.taobaou.utils.OtherRetrofitManager;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.SpConstans;
import com.example.taobaou.utils.engine.GlideEngine;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    @BindView(R.id.tv_myInfo_change_pwd)
    TextView tvChangePwd;
    @BindView(R.id.tv_bind_face)
    TextView tvBindFace;
    private String mLastUserAccount;
    public static final String TAG="MyInfoFragment";
    private Api mApiService;

    @Override
    protected int getRootViewResid() {
        return R.layout.fragment_my_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mApiService = OtherRetrofitManager.getInstance().getApiService();

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mLastUserAccount = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);

    }

    @Override
    protected void initView(View rootView) {
        //只有Success了才会显示界面信息
        setUpState(State.SUCCESS);
        tv_bar_title.setText("我的信息");
        isloginShow(false);
        if (!TextUtils.isEmpty(mLastUserAccount)){
            isloginShow(true);
            tv_username.setText(mLastUserAccount);
        }
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
                isloginShow(false);
                SharedPreferenceManager.getInstance().delete(SpConstans.LAST_USER_ACCOUNT);
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
        tvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePsw();
            }
        });
        tvBindFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<List<String>> task = mApiService.getAllFace();
                task.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.code()==200){
                            Log.d(TAG, "onResponse: "+response.body().size());
                            if (response.body().contains(SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT))){
                                ToastUtils.showShort("该账号已经绑定过了！");
                            }else{
                                String account = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);
                                FaceRegisetrActivity.startActivity(getContext(),account);
                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });


            }
        });

    }

    private void changePsw() {
            startActivity(new Intent(getActivity(), ChangePswActivity.class));
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
        startActivity(new Intent(getActivity(), TicketHistoryActivity.class));
    }

    //处理EventBus传递来的数据
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageCode()== MessageCode.LOGINUSERACCOUNT){
            //进入登录状态
            isloginShow(true);
            tv_username.setText(messageEvent.getMkeyword());
            SharedPreferenceManager.getInstance().putValue(SpConstans.LAST_USER_ACCOUNT,messageEvent.getMkeyword());
        }

        if (messageEvent.getMessageCode()==MessageCode.REGISTERUSERACCOUNT){
            //注册成功返回
            isloginShow(true);
            tv_username.setText(messageEvent.getMkeyword());
            SharedPreferenceManager.getInstance().putValue(SpConstans.LAST_USER_ACCOUNT,messageEvent.getMkeyword());
        }
        if (messageEvent.getMessageCode()==MessageCode.FACE_RECOGNIZED_SUCCESS){
            //todo:如果是人脸注册，则是先注册，让后再识别，人脸识别成功再走这条路
            Log.d("jae", "Register : "+messageEvent.getMkeyword());
            isloginShow(true);
            SharedPreferenceManager.getInstance().putValue(SpConstans.LAST_USER_ACCOUNT,messageEvent.getMkeyword());
            tv_username.setText(messageEvent.getMkeyword());
        }
        if (messageEvent.getMessageCode()==MessageCode.FACE_REGISTER_SUCCESS){
            //人脸注册成功
            Api api  = OtherRetrofitManager.getInstance().getApiService();
            Call<Boolean> task = api.addUser(new User(messageEvent.getMkeyword(), messageEvent.getMkeyword()));
            task.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }

        if (messageEvent.getMessageCode()==MessageCode.CHANGEPWSSUCCESS){
            //更改密码成功
            Log.d(TAG, "onEvent: 密码修改成功");
            isloginShow(false);
        }


    }

    private void goLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));


    }

    private void goRegister() {
        startActivity(new Intent(getActivity(), RegisterActivity.class));

    }

    public void isloginShow(Boolean isLogin){

        if (isLogin){
            //登录状态
            showViwe(iv_myImage,tv_username,tv_couponHistory,btnUnRegister,tvChangePwd,tvBindFace);
            hideView(btnLogin,btnRegister);

        }else{
            //未登录状态
            showViwe(btnLogin,btnRegister);
            hideView(iv_myImage,tv_username,tv_couponHistory,btnUnRegister,tvChangePwd,tvBindFace);

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
