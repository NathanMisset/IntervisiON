package com.example.intervision;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);


        viewPager2 = findViewById(R.id.viewpager);
        int[] images = {R.drawable.image4, R.drawable.image5, R.drawable.image6};
        String[] headings = {getString(R.string.heading_1), getString(R.string.heading_2), getString(R.string.heading_3)};
        String[] desc = {getString(R.string.content_1), getString(R.string.content_2), getString(R.string.content_3)};

        viewPagerItemArrayList = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i], headings[i], desc[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }

        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        Button toRegisteryButton = (Button) findViewById(R.id.RegisterButton);
        toRegisteryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("BUTTONS", "User tapped the RegisterButton");
                launchRegister();
            }
        });
    }
    public void launchRegister(){
        //Launch register activity
        Intent i = new Intent(this, RegisterActivity.class);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivity(i);
        }
    }
}
