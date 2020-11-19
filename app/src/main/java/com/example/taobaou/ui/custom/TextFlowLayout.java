package com.example.taobaou.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobaou.R;
import com.example.taobaou.base.BaseApplication;
import com.example.taobaou.utils.LogUtils;
import com.example.taobaou.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {
    private static final int DEFAULT_SPACE = SizeUtils.dip2px(BaseApplication.getAppContext(),10);
    private int mItemHorizontalSpace =DEFAULT_SPACE;
    private int mItemVerticalSpace=DEFAULT_SPACE;
    private int mItemhorizontalSpace;
    private int mItemverticalSpace;
    private int mParetnSelfWidth;
    private onFlowTextItemClick mOnFlowTextItemClick=null;

    public int getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(int itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public int getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(int itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList=new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到相关属性
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.FlowTextStyle);
        mItemhorizontalSpace = (int)a.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemverticalSpace = (int)a.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        a.recycle();
        LogUtils.d(this,"mItemhorizontalSpace--------->"+mItemhorizontalSpace);
        LogUtils.d(this,"mItemverticalSpace--------->"+mItemverticalSpace);

    }
    public void setTextList(List<String> textList){
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        //遍历内容
        for (String text : mTextList) {
            //添加子View
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFlowTextItemClick!=null){
                        mOnFlowTextItemClick.onFlowItemClick(text);
                    }
                }
            });
            addView(item);

        }

    }



    private List<List<View>> lines=new ArrayList<>();//每一行是什么


    /**测量孩子 然后测量自己
     * 先把孩子的大小测量出来，然后通过将孩子的宽度相加，加上一些间隙的值，就是我自己（父控件）的大小
     * 同理，高度也是如此
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount()==0){//因为onMeasure是多次调用的,如果没有添加进来其实就还不用测量
            return;
        }
        //由于这个控件的width设置成match_paretn所以测出来是 的屏幕宽度,减去Padding值更加严谨，就是控件的宽度
        mParetnSelfWidth = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
        lines.clear();//否则就叠加了
        List<View> line=null;//一行是什么
        //测量孩子
        int childCount=getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemview = getChildAt(i);
            if (itemview.getVisibility()!=VISIBLE){
                continue;
            }
            //通过这个方法测量，本来是0，通过这个测量方法后，就能测出该控件实际占多少
            measureChild(itemview,widthMeasureSpec,heightMeasureSpec);
            if (line==null){
                //说明当前行为空，可以添加
                line=createNewLine(itemview);
            }else{
                //判断是否可以再添加
                if (canBeAdd(itemview,line)) {
                    //可以添加
                    line.add(itemview);
                }else{
                    //需要新创建一行,再添加
                    //
                    line=createNewLine(itemview);

                }
            }

        }
        //测量自己
        //将每一行的高度测量出来，加上中间间隙的高度，就能知道父控件的实际高度了
        int selfHeight=lines.size()*getChildAt(0).getMeasuredHeight()+mItemverticalSpace*(lines.size()+1);
        LogUtils.d(this,"selfHeight--------->"+selfHeight);
        setMeasuredDimension(mParetnSelfWidth,selfHeight);

    }

    private List<View> createNewLine(View itemview) {
        List<View>line = new ArrayList<>();
        line.add(itemview);
        lines.add(line);//
        return line;
    }

    /**
     * 判断当前行是否可以继续添加
     * @param itemview
     * @param line
     */
    private  boolean canBeAdd(View itemview, List<View> line) {
        //所有的已添加的view宽度都加起来，加上间距（(lines.size()+1)*horizontalSpace)+itemView.getMeasureWidth()

        int totalWidth=itemview.getMeasuredWidth();
        //遍历已存在view的宽度
        for (View view : line) {
            totalWidth+=view.getMeasuredWidth();
        }
        //添加间距
        totalWidth+=(line.size()+1)*mItemHorizontalSpace;
        LogUtils.d(this,"totalWidth------->"+totalWidth);

        //条件：如果小于等于当前空间宽度则可以添加，不然就不行
        return totalWidth<=mParetnSelfWidth;

    }

    //测量完 摆放孩子
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View item = getChildAt(0);
        int topOffset=mItemVerticalSpace;
        for (List<View> line : lines) {
            //line:每一行
            int leftOffset=mItemHorizontalSpace;
            for (View textItem : line) {
                //textItem：每一行中的子view
                textItem.layout(leftOffset,topOffset,leftOffset+textItem.getMeasuredWidth(),topOffset+textItem.getMeasuredHeight());
                leftOffset+=textItem.getMeasuredWidth()+mItemHorizontalSpace;

            }
            topOffset+=mItemHorizontalSpace+item.getMeasuredHeight();
        }

    }

    //暴露接口

    public void setOnFlowTextItemClick(onFlowTextItemClick onFlowTextItemClick){
        this.mOnFlowTextItemClick=onFlowTextItemClick;
    }
    public interface onFlowTextItemClick{
        void onFlowItemClick(String text);
    }
    public int getContentSize(){
        return mTextList.size();
    }


}
