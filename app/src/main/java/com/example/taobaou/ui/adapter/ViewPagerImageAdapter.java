package com.example.taobaou.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobaou.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;


public class ViewPagerImageAdapter extends RecyclerView.Adapter<ViewPagerImageAdapter.ViewHolder> {
    private Context mContext;
    private List<String> photoPaths;
    private List<String> photoContents;
    private List<String> photoTitles;

    public ViewPagerImageAdapter(Context context, List<String> photoPaths, List<String> photoTitles, List<String> photoContents) {
        mContext = context;
        this.photoPaths = photoPaths;
        this.photoContents = photoContents;
        this.photoTitles = photoTitles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_big_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(photoPaths.get(position)).placeholder(R.mipmap.image).into(holder.ivPhoto);
        holder.ivPhoto.setScale(1f);
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PhotoView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
        }
    }
}
