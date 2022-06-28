package com.example.staynoted.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.staynoted.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    ArrayList<String> imgUrls;

    public ImagesAdapter(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Picasso.get()
                .load(imgUrls.get(position))
                .error(R.drawable.image_placeholder)
                .resize(400, 400)
                .centerCrop()
//                .centerCrop()
//                .centerInside()
                .into(holder.ivImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.pbImg.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        holder.pbImg.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        ProgressBar pbImg;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            pbImg = itemView.findViewById(R.id.pbImg);
        }
    }
}
