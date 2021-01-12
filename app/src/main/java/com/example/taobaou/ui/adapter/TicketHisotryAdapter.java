package com.example.taobaou.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.example.taobaou.model.domain.TicketParams;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TicketHisotryAdapter extends RecyclerView.Adapter<TicketHisotryAdapter.InnerHolder> {
    Map<String,String> mMap=new LinkedHashMap<>();
    List<TicketParams> mList=new ArrayList<>();
    String[] urls;
    String[] codes;
    private View mItemView;
    private OnHisotryTicketClickListener mOnHisotryTicketClickListener=null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_history, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView itemTv=holder.itemView.findViewById(R.id.tv_ticket_history);
        ImageView itemIv=holder.itemView.findViewById(R.id.iv_ticket_history);
        itemTv.setText(codes[position]);
        Glide.with(mItemView).load(urls[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnHisotryTicketClickListener!=null){
                    mOnHisotryTicketClickListener.onItemHisotryClick(codes[position]);

                }

            }
        });

    }
    public void setOnHisotryTicketClickListener(OnHisotryTicketClickListener onHisotryTicketClickListener){
        this.mOnHisotryTicketClickListener=onHisotryTicketClickListener;
    }

    public interface OnHisotryTicketClickListener{
        void onItemHisotryClick(String code);
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }
    public void setData(LinkedHashMap<String,String> map){

        urls = (String[]) map.keySet().toArray();
        codes = (String[]) map.values().toArray();

    }

    class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
