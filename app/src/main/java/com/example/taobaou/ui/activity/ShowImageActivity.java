package com.example.taobaou.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.taobaou.R;
import com.example.taobaou.ui.adapter.ViewPagerImageAdapter;
import com.example.taobaou.utils.SpConstans;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 展示图片类型消息使用
 */
public class ShowImageActivity extends AppCompatActivity {
    public static final String TAG = ShowImageActivity.class.getSimpleName();

    private Unbinder mBind;
    @BindView(R.id.rv_image)
    ViewPager2 rvImage;
    ArrayList<String> photoPaths = new ArrayList<>();
    ArrayList<String> photoTitles = new ArrayList<>();
    ArrayList<String> photoContents = new ArrayList<>();
    private ViewPagerImageAdapter mViewPagerImageAdapter;

    private String mMsgTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        mBind = ButterKnife.bind(this);
        initData();
        initView();
    }

    @OnClick(R.id.iv_back)
    public void back() {
        Log.d(TAG, "back: ");
        finish();
    }

    private void initView() {
        mViewPagerImageAdapter = new ViewPagerImageAdapter(this, photoPaths,photoTitles,photoContents);
        rvImage.setAdapter(mViewPagerImageAdapter);
        rvImage.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        rvImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
//                int childCount = rvImage.getChildCount();
//                for (int i = 0; i < childCount; i++) {
//                    //获取当前页面的view
//                    View child = rvImage.getChildAt(i);
//                    //获取当前页面中的PhotoView
//                    PhotoView photoView = child.findViewById(R.id.iv_photo);
//                    if (photoView != null) {
//                        //获取photoView创建的PhotoViewAttacher
//                        PhotoViewAttacher photoViewAttacher = (PhotoViewAttacher) photoView.getAttacher();
//                        //第一个参数是获取photoViewAttacher自带的缩放大小最小值，第二个和第三个参数设置缩放中心
//                        photoViewAttacher.setScale(photoViewAttacher.getMinimumScale(), 0f, 0f, true);
//                    }
//                }

            }
        });
    }


    private void initData() {
        Intent intent = getIntent();
        photoPaths.clear();
        photoPaths.addAll(intent.getStringArrayListExtra(SpConstans.IMAGE_URL_LIST));
        Log.d(TAG, "initData: photoPathsize"+photoPaths.size());
//        photoTitles.clear();
//        photoPaths.addAll(intent.getStringArrayListExtra(Constant.IMAGE_TITLE_LIST));
//
//        photoContents.clear();
//        photoPaths.addAll(intent.getStringArrayListExtra(Constant.IMAGE_CONTENT_LIST));

//        mMsgTime = intent.getStringExtra(Constant.IMAGE_MSG_TIEM);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: event:" + event.getAction());
        return super.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
