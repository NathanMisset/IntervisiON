package com.example.intervision

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class ActivityTutorial : AppCompatActivity() {
    private var viewPager2: ViewPager2? = null
    private var viewPagerItemArrayList: ArrayList<ItemViewPager>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        viewPager2 = findViewById(R.id.viewpager)
        val images = intArrayOf(
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6
        )
        val headings = arrayOf(
            getString(R.string.heading_1),
            getString(R.string.heading_2),
            getString(R.string.heading_3)
        )
        val desc = arrayOf(
            getString(R.string.content_1),
            getString(R.string.content_2),
            getString(R.string.content_3),
        )
        viewPagerItemArrayList = ArrayList()
        for (i in images.indices) {
            val viewPagerItem = ItemViewPager(images[i], headings[i], desc[i])
            viewPagerItemArrayList!!.add(viewPagerItem)
        }
        val vpAdapter = AdapterViewPager(viewPagerItemArrayList!!)
        viewPager2!!.adapter = vpAdapter
        viewPager2!!.clipToPadding = false
        viewPager2!!.clipChildren = false
        viewPager2!!.offscreenPageLimit = 2
        viewPager2!!.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER


        val toHomeButton = findViewById<View>(R.id.ToHomeButton) as Button
        toHomeButton.setOnClickListener {
            Log.d("BUTTONS", "User tapped the RegisterButton")
            toHome()
        }
    }

    private fun toHome(){
        val i = Intent(this, ActivityNavigation::class.java)

        this.startActivity(i)
    }

}