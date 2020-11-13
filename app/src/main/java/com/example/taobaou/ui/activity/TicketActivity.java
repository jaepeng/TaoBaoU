package com.example.taobaou.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ITicketPagerCallback;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private ITicketPresenter mTicketPresentImp;


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
