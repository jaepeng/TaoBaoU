package com.example.taobaou.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private static final String TAG = "TicketActivity";
    private ITicketPresenter mTicketPresentImp;
    private Boolean mHasTaobao=false;


    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_or_open)
    public Button mOpenOrCopy;

    @BindView(R.id.ticket_back_imv)
    public ImageView backImage;

    @BindView(R.id.ticket_cover_loading)
    public View loadView;

    @BindView(R.id.ticket_load_retry)
    public TextView loadErrorText;

    @Override
    protected void initPresenter() {
        mTicketPresentImp = PresentManager.getInstance().getTicketPresentImp();
        if (mTicketPresentImp!=null){

            mTicketPresentImp.registerViewCallback(this);
        }
        //判断是否安装有淘宝
        //通过adb命令拿到淘宝包名com.taobao.taobao
        //检查是否安装
        PackageManager pm=getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobao=packageInfo!=null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobao=false;
        }
        Log.d(TAG, "initPresenter: hasTaobao:"+mHasTaobao);
        //根据这个值修改UI
       mOpenOrCopy.setText(mHasTaobao?"打开淘宝领券":"复制口令");

    }



    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOpenOrCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //复制淘口令
                //拿到内容
                String code = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.class,"code:"+code);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴表
                ClipData clipdata = ClipData.newPlainText("sob_tao_bao_ticket_code", code);
                cm.setPrimaryClip(clipdata);

                //判断是否有淘宝
                //如果有则打开淘宝,没有则提示复制成功
                if (mHasTaobao){
                    Intent taobaoIntent=new Intent();
//                    taobaoIntent.setAction("android.intent.action.Main");
//                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                    ComponentName componentName=new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                }else{
                    ToastUtsils.showToast("复制成功!粘贴分享");
                }


            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoader(String cover, TicketResult ticketResult) {

        if (mCover!=null&& !TextUtils.isEmpty(cover)) {

            cover=UrlUtils.getCoverPath(cover);
            Glide.with(this).load(cover).into(mCover);

        }
        if (ticketResult!=null&&ticketResult.getData().getTbk_tpwd_create_response()!=null){
            mTicketCode.setText(ticketResult.getData().getTbk_tpwd_create_response().getData().getModel());

        }
        if (loadView!=null&&mCover.getVisibility()==View.VISIBLE){
            loadView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void release() {
        if (mTicketPresentImp != null) {
            mTicketPresentImp.unregisterViewCallback(this);
        }
    }

    @Override
    public void onError() {
        if (loadView!=null){
            loadView.setVisibility(View.GONE);
        }
        if (loadErrorText != null) {
            loadErrorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        loadView.setVisibility(View.VISIBLE);
        if (loadErrorText != null) {
            loadErrorText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
