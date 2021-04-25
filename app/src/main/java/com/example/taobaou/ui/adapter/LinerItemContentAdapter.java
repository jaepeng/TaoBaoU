package com.example.taobaou.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.model.domain.IBaseInfo;
import com.example.taobaou.model.domain.ILinerItemInfo;
import com.example.taobaou.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinerItemContentAdapter extends RecyclerView.Adapter<LinerItemContentAdapter.InnerHolder> {
    List<ILinerItemInfo> mdata =new ArrayList<>();
    private OnListItemClickListener mItemClickListener =null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liner_goods_content, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LinerItemContentAdapter.InnerHolder holder, int position) {
        //设置数据
        ILinerItemInfo dataBean = mdata.get(position);
        holder.setData(dataBean);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.onItemClickListener(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setData(List<? extends ILinerItemInfo> contents) {
        mdata.clear();
        mdata.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinerItemInfo> contents) {
        //添加之前,拿到之前的size
        int oldSize = mdata.size();
        if (contents!=null)
        mdata.addAll(contents);

        //更新Ui
        notifyItemRangeChanged(oldSize,contents.size());
    }



    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_cover)
        public ImageView coverImage;
         @BindView(R.id.goods_title)
        public TextView title;
         @BindView(R.id.goods_off_price)
        public TextView offPriceTv;
         @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;
         @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;
         @BindView(R.id.goods_sells_count)
        public TextView sellsCountTv;




        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(ILinerItemInfo dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
//            LogUtils.d(this,"url--->"+dataBean.getPict_url());
//            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
//            int width=layoutParams.width;
//            int height=layoutParams.height;
//            int coverSize=(width>height? width:height)/2;
//            LogUtils.d(this,"width---->"+width+"  height----->"+height);
            String cover = dataBean.getCover();
            if (!TextUtils.isEmpty(cover)) {

                String coverPath = UrlUtils.getCoverPath(dataBean.getCover());
                Glide.with(context).load(coverPath).into(this.coverImage);
            }else{
                coverImage.setImageResource(R.mipmap.ic_logo);
            }
//            LogUtils.d(HomePageContentAdapter.this,"coverPath----->"+coverPath);
            String finalPrice = dataBean.getfinalPrice();
            long couponAmount = dataBean.getCouponAmmount();
//            LogUtils.d(this,"final price---->"+finalPrice);
            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price),couponAmount));
            float resultPrise=Float.parseFloat(finalPrice)-couponAmount;
//            LogUtils.d(this,"result prse---->"+resultPrise);
            finalPriceTv.setText(String.format("%.2f",resultPrise));
            //设置中画线
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.setText(String.format(context.getString(R.string.text_goods_original_price),finalPrice));
            sellsCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count),dataBean.getVolume()));

        }

    }
    public void setOnListItemClickListener(OnListItemClickListener listener){
        this.mItemClickListener=listener;
    }
    public interface OnListItemClickListener {
        void onItemClickListener(IBaseInfo item);

    }
}
