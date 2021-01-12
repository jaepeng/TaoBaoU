package com.example.taobaou.ui.adapter;

import android.graphics.Paint;
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
import com.example.taobaou.model.domain.OnSellContetn;
import com.example.taobaou.utils.ToastUtsils;
import com.example.taobaou.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellPageAdapter extends RecyclerView.Adapter<OnSellPageAdapter.InnerHolder> {
    public List<OnSellContetn.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mdata=new ArrayList<>();
    private OnSellPageItemClickListener mOnSellItemClickLisetener=null;

    @NonNull
    @Override
    public OnSellPageAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);

        return new InnerHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull OnSellPageAdapter.InnerHolder holder, int position) {
    //绑定数据
        OnSellContetn.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mdata.get(position);
        holder.setData(mapDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSellItemClickLisetener!=null){
                    mOnSellItemClickLisetener.onSellItemClick(mapDataBean);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setData(OnSellContetn result) {
        this.mdata.clear();
        mdata.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据到列表中
     * @param moreResult
     */

    public void onMoreLoad(OnSellContetn moreResult) {
        List<OnSellContetn.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moredta = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //原数据长度
        int originSize = mdata.size();
        this.mdata.addAll(moredta);
        notifyItemRangeChanged(originSize-1,moredta.size());
        ToastUtsils.showToast("加载了"+moredta.size()+"条数据");
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.onsell_cover)
        public ImageView cover;

        @BindView(R.id.onSell_Title_tv)
        public TextView title_tv;

        @BindView(R.id.onsell_origin_price)
        public TextView originPrice_tv;
        @BindView(R.id.onsell_off_price)
        public TextView offPirce_tv;



        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void setData( OnSellContetn.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data){
            title_tv.setText(data.getTitle());
            String pict_url = data.getPict_url();
            String coverPath = UrlUtils.getCoverPath(pict_url);
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            originPrice_tv.setText("原价:"+data.getZk_final_price()+"元");
            originPrice_tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmmount=data.getCoupon_amount();
            float originPriseFloat=Float.parseFloat(data.getZk_final_price());
            float finalPrise=originPriseFloat-couponAmmount;
            offPirce_tv.setText("折后价:"+String.format("%.2f",finalPrise)+"元");

        }
    }

    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener){
        this.mOnSellItemClickLisetener=listener;
    }
    public interface OnSellPageItemClickListener{
        void onSellItemClick(IBaseInfo dataBean);
    }
}
