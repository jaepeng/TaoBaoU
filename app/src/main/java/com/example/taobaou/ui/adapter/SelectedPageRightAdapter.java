package com.example.taobaou.ui.adapter;

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
import com.example.taobaou.model.domain.SelectedContent;
import com.example.taobaou.utils.Constants;
import com.example.taobaou.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedPageRightAdapter extends RecyclerView.Adapter<SelectedPageRightAdapter.InnerHolder> {
    private List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mdata=new ArrayList<>();
    private OnSelectedPageItemClick mOnSelectedPageItemClick=null;

    @NonNull
    @Override
    public SelectedPageRightAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectedpage_right_content, parent, false);

        return new InnerHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPageRightAdapter.InnerHolder holder, int position) {
        //绑定数据
        SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData = mdata.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSelectedPageItemClick!=null){
                    mOnSelectedPageItemClick.onItemClick(itemData);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setData(SelectedContent content) {
        if (content.getCode()== Constants.SUCCESS_CODE){
            List<SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> map_data = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            this.mdata.clear();
            this.mdata.addAll(map_data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.selected_cover)
        public ImageView cover;

        @BindView(R.id.selected_off_price)
        public TextView offPrice;

        @BindView(R.id.selected_title_tv)
        public TextView title;

        @BindView(R.id.selected_buy_tv)
        public TextView goBuyBtn;

        @BindView(R.id.selected_origin_price)
        public TextView originPice;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }

        public void setData(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData) {
           title.setText(itemData.getTitle());
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originPice.setText("晚啦!没有券了");
                goBuyBtn.setVisibility(View.GONE);
            }else{
                originPice.setText("原价:"+itemData.getZk_final_price()+"元");
                goBuyBtn.setVisibility(View.VISIBLE);

            }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPrice.setVisibility(View.GONE);;

            }else{
                offPrice.setVisibility(View.VISIBLE);
                offPrice.setText("领券立减"+ itemData.getCoupon_amount()+"元");
            }
           String picurl=itemData.getPict_url();
            String selectedCover = UrlUtils.getSelectedCover(picurl);
            Glide.with(itemView.getContext()).load(selectedCover).into(cover);

        }
    }

    public void setOnSelectedPageItemClick(OnSelectedPageItemClick listener){
        this.mOnSelectedPageItemClick=listener;
    }
    public interface OnSelectedPageItemClick{
        void onItemClick(SelectedContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item);
    }
}
