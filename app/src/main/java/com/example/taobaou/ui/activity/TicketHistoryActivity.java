package com.example.taobaou.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.model.Api;
import com.example.taobaou.model.domain.TicketHistory;
import com.example.taobaou.ui.adapter.TicketHisotryAdapter;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.OtherRetrofitManager;
import com.example.taobaou.utils.SharedPreferenceManager;
import com.example.taobaou.utils.SpConstans;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.utils.UrlUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketHistoryActivity extends AppCompatActivity {

    private RecyclerView mRvHistory;
    private boolean mHasTaobao = false;
    private static final String TAG = "TicketHistoryActivity";
    private List<TicketHistory> mTicketHistoryList;
    private TicketHisotryAdapter mTicketHisotryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);
        initView();
        initPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String account = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);
        Call<List<TicketHistory>> task = OtherRetrofitManager.getInstance().getApiService().findAllTicketHistory(account);
        task.enqueue(new Callback<List<TicketHistory>>() {
            @Override
            public void onResponse(Call<List<TicketHistory>> call, Response<List<TicketHistory>> response) {
                Log.d(TAG, "onResponse: response.code" + response.code());
                Log.d(TAG, "onResponse: response.body" + response.body());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        mTicketHistoryList = response.body();
                        mTicketHisotryAdapter.setData(mTicketHistoryList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TicketHistory>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage(),t );
            }
        });
    }

    private void initPresenter() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobao = packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobao = false;
        }
    }

    private void initView() {
        mRvHistory = findViewById(R.id.rv_history);
        mRvHistory.setLayoutManager(new LinearLayoutManager(this));
        mTicketHisotryAdapter = new TicketHisotryAdapter(this);
        mRvHistory.setAdapter(mTicketHisotryAdapter);
        mTicketHisotryAdapter.setOnHisotryTicketClickListener(new TicketHisotryAdapter.OnHisotryTicketClickListener() {
            @Override
            public void onItemHisotryClick(String code, String coverpath) {
                Log.d(TAG, "onItemHisotryClick: code:" + code + " ,coverpath" + coverpath);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴表
                ClipData clipdata = ClipData.newPlainText("sob_tao_bao_ticket_code", code);
                cm.setPrimaryClip(clipdata);
                //判断是否有淘宝
                //如果有则打开淘宝,没有则提示复制成功
                String account = SharedPreferenceManager.getInstance().getString(SpConstans.LAST_USER_ACCOUNT);
                Api apiService = OtherRetrofitManager.getInstance().getApiService();
                Call<Boolean> task = apiService.addTicketHistory(new TicketHistory(UrlUtils.getCoverPath(coverpath), account, code));
                task.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            if (response.body().booleanValue()) {
                                Log.d(TAG, "onResponse: 添加领券记录成功");
                            } else {
                                Log.d(TAG, "onResponse: 添加领券失败");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
                if (mHasTaobao) {
                    Intent taobaoIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    ToastUtsils.showToast("复制成功!粘贴分享");
                }
            }
        });

    }

    public static LinkedHashMap<String, String> getMap(Context context) {
        LinkedHashMap<String, String> examMap = new LinkedHashMap<>();
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