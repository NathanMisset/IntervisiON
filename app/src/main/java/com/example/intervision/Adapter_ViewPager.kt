package com.example.intervision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter_ViewPager(var viewPagerItemsArrayList: ArrayList<Item_View_Pager>) :
    RecyclerView.Adapter<Adapter_ViewPager.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewPagerItem = viewPagerItemsArrayList[position]
        holder.imageView.setImageResource(viewPagerItem.imageID)
        holder.tvHeading.text = viewPagerItem.heading
        holder.tvDesc.text = viewPagerItem.description
    }

    override fun getItemCount(): Int {
        return viewPagerItemsArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var tvHeading: TextView
        var tvDesc: TextView

        init {
            imageView = itemView.findViewById(R.id.ivimage)
            tvHeading = itemView.findViewById(R.id.heading_register)
            tvDesc = itemView.findViewById(R.id.tvDesc)
        }
    }
}