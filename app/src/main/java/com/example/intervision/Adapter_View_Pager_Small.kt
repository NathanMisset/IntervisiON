package com.example.intervision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter_View_Pager_Small(var viewPagerItemsArrayList: ArrayList<Item_View_Pager_Small>) :
    RecyclerView.Adapter<Adapter_View_Pager_Small.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager_small, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewPagerItem = viewPagerItemsArrayList[position]
        holder.question.text = viewPagerItem.question
        holder.statement.text = viewPagerItem.statement
    }

    override fun getItemCount(): Int {
        return viewPagerItemsArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question: TextView
        var statement: TextView

        init {
            question = itemView.findViewById(R.id.question_viewpager_small_item)
            statement = itemView.findViewById(R.id.statement_viewpager_small_item)
        }
    }
}