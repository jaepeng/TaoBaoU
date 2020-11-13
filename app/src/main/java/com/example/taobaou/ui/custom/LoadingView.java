package com.example.taobaou.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.taobaou.R;

public class LoadingView extends AppCompatImageView {
    private float mDegres=30;
    private boolean mNeedRoate=true;



    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
        startRoate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startRoate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRoate();
    }

    private void stopRoate() {
        mNeedRoate=false;
    }

    private void startRoate() {
        mNeedRoate=true;
        post(new Runnable() {
            @Override
            public void run() {
                mDegres+=10;
                if (mDegres>=360){
                    mDegres=0;
                }
                invalidate();
                //判断是否要继续旋转
                //如果不可见或二者已经DetachFromWindow
                if (getVisibility()!=VISIBLE&&!mNeedRoate){
                    removeCallbacks(this);
                }else{

                    postDelayed(this,20);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegres,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);

    }
}
