package com.example.intervision

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

class Item_Spinner(items: ArrayList<String>, parent: ViewGroup, activity: Activity) {
    var parent: View
    var layout: View
    var activity: Activity
    var dropdown: Spinner? = null
    var adapter: ArrayAdapter<String>? = null
    var items: ArrayList<String> = items

    init {
        this.parent = parent
        this.activity = activity
        layout = LayoutInflater.from(parent.context).inflate(
            R.layout.item_users_spinner,
            parent,
            false
        )
    }

    fun init() {
        dropdown = layout.findViewById(R.id.spinner_user_spinner_item)
        adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, items)
        dropdown!!.setAdapter(adapter)
        (parent as ViewGroup).addView(layout)
    }

    fun Enable() {
        dropdown!!.visibility = View.VISIBLE
    }

    fun Disable() {
        dropdown!!.visibility = View.GONE
    }

    companion object {
        private const val TAG = "ItemSpinner"
    }
}