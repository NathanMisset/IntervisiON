package com.example.intervision;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemsArrayList) {
        this.viewPagerItemsArrayList = viewPagerItemsArrayList;
    }

    ArrayList<ViewPagerItem> viewPagerItemsArrayList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPagerItem viewPagerItem = viewPagerItemsArrayList.get(position);

        holder.imageView.setImageResource(viewPagerItem.imageID);
        holder.tvHeading.setText(viewPagerItem.heading);
        holder.tvDesc.setText(viewPagerItem.description);
    }

    @Override
    public int getItemCount() {
        return viewPagerItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvHeading, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivimage);
            tvHeading = itemView.findViewById(R.id.heading_register);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

}
