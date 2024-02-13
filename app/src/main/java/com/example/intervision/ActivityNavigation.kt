package com.example.intervision

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class ActivityNavigation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        // as soon as the application opens the first 
        // fragment should be shown to the user 
        // in this case it is algorithm fragment 
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentHome())
            .commit()
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            // By using switch we can easily get 
            // the selected fragment 
            // by using there id. 
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> {
                    selectedFragment = FragmentHome()
                }
                R.id.profile -> {
                    selectedFragment = FragmentProfile()
                }
                R.id.group -> {
                    selectedFragment = FragmentGroups()
                }
                R.id.feed -> {
                    selectedFragment = Fragment_Feed()
                }
            }
            // It will help to replace the  
            // one fragment to other. 
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment).commit()
            }
            true
        }
}