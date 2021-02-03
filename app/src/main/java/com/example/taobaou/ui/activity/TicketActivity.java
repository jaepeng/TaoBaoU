package com.example.taobaou.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.base.BaseActivity;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.TicketHistory;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.model.domain.TicketResult;
import com.example.taobaou.model.domain.User;
import com.example.taobaou.presenter.ITicketPresenter;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.OtherRetrofitManager;
import com.example.taobaou.utils.PresentManager;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.utils.UrlUtils;
import com.example.taobaou.view.ITicketPagerCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback {

    private static final String TAG = "TicketActivity";
    private ITicketPresenter mTicketPresentImp;
    private Boolean mHasTaobao=false;
    private String mUrl="";
    private Map<String,String> historymap=new LinkedHashMap<>();


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
    public TicketParams mTicketParams;

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
                //todo:复制后,保留复制的历史记录.
                User lastUser = SharedPreferenceManager.getInstance().getLastUser();
                String account="";
                if (lastUser!=null){
                    account=lastUser.getAccount();
                }
                Api apiService = OtherRetrofitManager.getInstance().getApiService();
                Call<Boolean> task = apiService.addTicketHistory(new TicketHistory(UrlUtils.getCoverPath(mUrl), account,code ));
                task.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d(TAG, "onResponse: response.cod"+response.code());
                        if (response.code()==200){
                            if (response.body().booleanValue()){
                                Log.d(TAG, "onResponse: 添加领券记录成功");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d(TAG, "onFailure: e:"+t.getMessage());
                    }
                });

//                mTicketParams=new TicketParams(mUrl,code);
//                historymap.put(mUrl,code);
//                setMap(TicketActivity.this,historymap);



                //判断是否有淘宝
                //如果有则打开淘宝,没有则提示复制成功
                if (mHasTaobao){
                    Intent taobaoIntent=new Intent();
//                    taobaoIntent.setAction("android.intent.action.Main");
//                    taobaoIntent.addCategory("android.intent.category.LAUNCHER");
                    //todo:无法跳转到对应的领券界面,查看是否这里除了问题
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
            mUrl=cover;
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


    public static void setMap(Context context, Map<String, String> map) {
        if (map != null) {
            JSONStringer jsonStringer = new JSONStringer();
            try {
                jsonStringer.array();
                for (String string : map.keySet()) {
                    jsonStringer.object();
                    jsonStringer.key(Constants.SP_KEY_HISTORY_TIECKT_URL);
                    jsonStringer.value(string);
                    jsonStringer.key(Constants.SP_KEY_HISTORY_TIECKT_CODE);
                    jsonStringer.value(map.get(string));
                    jsonStringer.endObject();
                }
                jsonStringer.endArray();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = context.getSharedPreferences(Constants.SP_KEY_HISTORY_TIECKT, Context.MODE_PRIVATE);
            sp.edit().putString(Constants.SP_KEY_HISTORY_TIECKT_MAP, jsonStringer.toString()).apply();
        }
    }
    public static Map<String,String> getMap(Context context) {
        Map<String, String> examMap = new LinkedHashMap<>();
        SharedPreferences sp = context.getSharedPreferences(Constants.SP_KEY_HISTORY_TIECKT, Context.MODE_PRIVATE);
        String map = sp.getString(Constants.SP_KEY_HISTORY_TIECKT_MAP, "");
        if (map.length() > 0) {
            JSONTokener jsonTokener = new JSONTokener(map);
            try {
                JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    examMap.put(jsonObject.getString(Constants.SP_KEY_HISTORY_TIECKT_URL), jsonObject.getString(Constants.SP_KEY_HISTORY_TIECKT_CODE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return examMap;
    }
}
