package com.example.intervision

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class Activity_Tutorial : AppCompatActivity() {
    var viewPager2: ViewPager2? = null
    var viewPagerItemArrayList: ArrayList<Item_View_Pager>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        viewPager2 = findViewById(R.id.viewpager)
        val images = intArrayOf(
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.icon_registration
        )
        val headings = arrayOf(
            getString(R.string.heading_1),
            getString(R.string.heading_2),
            getString(R.string.heading_3),
            getString(R.string.heading_register)
        )
        val desc = arrayOf(
            getString(R.string.content_1),
            getString(R.string.content_2),
            getString(R.string.content_3),
            getString(R.string.content_register)
        )
        viewPagerItemArrayList = ArrayList()
        for (i in images.indices) {
            val viewPagerItem = Item_View_Pager(images[i], headings[i], desc[i])
            viewPagerItemArrayList!!.add(viewPagerItem)
        }
        val vpAdapter = Adapter_ViewPager(viewPagerItemArrayList!!)
        viewPager2!!.setAdapter(vpAdapter)
        viewPager2!!.setClipToPadding(false)
        viewPager2!!.setClipChildren(false)
        viewPager2!!.setOffscreenPageLimit(2)
        viewPager2!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
    }
}