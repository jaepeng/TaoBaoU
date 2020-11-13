package com.example.taobaou.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.taobaou.R;

/**
 * 自动轮播的功能
 *
 */
public class AutoLoopViewPager extends ViewPager {
    public static final int DEFAULT_DURATION=3000;
    private long mDuration=DEFAULT_DURATION;
    public AutoLoopViewPager(@NonNull Context context) {
        this(context,null);
    }


    public AutoLoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //读取属性
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoLopperStyle);
        //获取属性
        mDuration= t.getInteger(R.styleable.AutoLopperStyle_duration, DEFAULT_DURATION);



        t.recycle();
    }

    private boolean isLoop=false;
    public void startLoop(){
        //先拿到当前的位置
        isLoop=true;
        post(task);
    }
    private Runnable task=new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            setCurrentItem(++currentItem);
            if (isLoop){
                postDelayed(this,mDuration);
            }
        }
    };
    //切换间隔时长

    /**
     *
     * @param duration:设置切换轮播图的时长,单位是毫秒
     */
    public void setDruatio(Long duration){
        this.mDuration=duration;
    }
    public void stopLoop(){
        removeCallbacks(task);
        isLoop=false;

    }

}
