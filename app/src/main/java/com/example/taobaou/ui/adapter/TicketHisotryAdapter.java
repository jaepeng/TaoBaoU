package com.example.taobaou.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.taobaou.R;
import com.example.taobaou.model.domain.TicketHistory;
import com.example.taobaou.model.domain.TicketParams;
import com.example.taobaou.utils.GlideRoundTransform;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketHisotryAdapter extends RecyclerView.Adapter<TicketHisotryAdapter.InnerHolder> {
    Map<String, String> mMap = new LinkedHashMap<>();
    List<TicketParams> mList = new ArrayList<>();
    String[] urls;
    String[] codes;
    private List<TicketHistory> mdata = new ArrayList<>();
    private OnHisotryTicketClickListener mOnHisotryTicketClickListener = null;
    private static final String TAG = "TicketHisotryAdapter";
    private Context mContext;

    public TicketHisotryAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_history, parent, false);
        return new InnerHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.setData(mdata.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mOnHisotryTicketClickListener != null) {
                mOnHisotryTicketClickListener.onItemHisotryClick(mdata.get(position).getContent(), mdata.get(position).getCoverpath());

            }

        });

    }

    public void setOnHisotryTicketClickListener(OnHisotryTicketClickListener onHisotryTicketClickListener) {
        this.mOnHisotryTicketClickListener = onHisotryTicketClickListener;
    }

    public interface OnHisotryTicketClickListener {
        void onItemHisotryClick(String code, String url);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setData(List<TicketHistory> ticketHistoryList) {
        if (ticketHistoryList == null || ticketHistoryList.size() == 0) {
            return;
        }
        Log.d(TAG, "setData: " + ticketHistoryList);
        this.mdata.clear();
        this.mdata.addAll(ticketHistoryList);
        Log.d(TAG, "setData: mdata.size" + mdata.size());
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ticket_history)
        TextView itemTv;
        @BindView(R.id.iv_ticket_history)
        ImageView itemIv;

        public InnerHolder(@NonNull View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(TicketHistory ticketHistoryList) {
            Log.d(TAG, "holder.setData: " + ticketHistoryList.toString());
            itemTv.setText(ticketHistoryList.getContent());
            RequestOptions requestOptions = RequestOptions.bitmapTransform(new GlideRoundTransform(mContext));
            Glide.with(itemIv.getContext()).applyDefaultRequestOptions(requestOptions).load(ticketHistoryList.getCoverpath()).into(itemIv);
        }
    }

}
