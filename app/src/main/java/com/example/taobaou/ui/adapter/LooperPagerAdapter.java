package com.example.taobaou.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobaou.model.domain.HomePagerContent;
import com.example.taobaou.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
   private List<HomePagerContent.DataBean> mData=new ArrayList<>();
    private OnLooperItemClickListener onLooperItemClickListener =null;

    @Override
    public int getCount() {
//        return mData.size();
        return Integer.MAX_VALUE;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //数据越界处理
        int realPosition= position%mData.size();
        HomePagerContent.DataBean dataBean=mData.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        int ivSize=(measuredHeight>measuredWidth?measuredHeight:measuredWidth);
        String coverUrl= UrlUtils.getCoverPath(dataBean.getPict_url(),ivSize);
        ImageView iv=new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        container.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLooperItemClickListener!=null){
                    HomePagerContent.DataBean item = mData.get(realPosition);
                    onLooperItemClickListener.onLooperTimeClick(item);
                }
            }
        });
        return iv;
    }
    public int getDataSize(){
        return mData.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int realPosition, @NonNull Object object) {
       container.removeView((View)object);
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }
    public void setOnLooperItemClickListener(OnLooperItemClickListener listener){
        this.onLooperItemClickListener=listener;
    }
    public interface OnLooperItemClickListener{
        void onLooperTimeClick(HomePagerContent.DataBean item);
    }
}
