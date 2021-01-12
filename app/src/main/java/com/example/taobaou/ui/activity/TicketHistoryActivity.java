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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.ui.adapter.TicketHisotryAdapter;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.ToastUtsils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.LinkedHashMap;

public class TicketHistoryActivity extends AppCompatActivity {

    private RecyclerView mRvHistory;
    private boolean mHasTaobao=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);
        initView();
        initPresenter();
    }

    private void initPresenter() {
        PackageManager pm=getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobao=packageInfo!=null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaobao=false;
        }
    }

    private void initView() {
        mRvHistory = findViewById(R.id.rv_history);
        TicketHisotryAdapter ticketHisotryAdapter = new TicketHisotryAdapter();
        ticketHisotryAdapter.setData(getMap(this));
        mRvHistory.setAdapter(ticketHisotryAdapter);
        ticketHisotryAdapter.setOnHisotryTicketClickListener(new TicketHisotryAdapter.OnHisotryTicketClickListener() {
            @Override
            public void onItemHisotryClick(String code) {
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
    public static LinkedHashMap<String,String> getMap(Context context) {
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