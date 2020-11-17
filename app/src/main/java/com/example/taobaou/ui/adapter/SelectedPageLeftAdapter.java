package com.example.taobaou.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobaou.R;
import com.example.taobaou.model.domain.SelectedPageCategory;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {
    private List<SelectedPageCategory.DataBean> mdata =new ArrayList<>();
    private int mCurrentSelectedPosition=0;
    private OnLeftfItemClickListener monLeftItemClickLisetner=null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPosition==position){
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.color_EFEEEEE_selected,null));
        }else{
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white,null));
        }

        SelectedPageCategory.DataBean dataBean = mdata.get(position);
        itemTv.setText(dataBean.getFavorites_title());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monLeftItemClickLisetner!=null){
                    //修改当前点击位置
                    if (mCurrentSelectedPosition!=position){

                        mCurrentSelectedPosition=position;
                    }
                    monLeftItemClickLisetner.onItemLeftClick(dataBean);
                    notifyDataSetChanged();
                }
                if (mdata.size()>0){
                    monLeftItemClickLisetner.onItemLeftClick(mdata.get(mCurrentSelectedPosition));
                }

            }
        });
    }

    /**
     * 设置数据
     * @return
     */
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setMdata(SelectedPageCategory categories) {
        List<SelectedPageCategory.DataBean> data = categories.getData();
        if ( data!= null) {
            this.mdata.clear();
            this.mdata.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setonLeftfItemClickListener(OnLeftfItemClickListener listener){

        this.monLeftItemClickLisetner=listener;

    }
    public interface OnLeftfItemClickListener {
        void onItemLeftClick(SelectedPageCategory.DataBean item);
    }
}
