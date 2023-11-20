package com.example.intervision;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SetSingleItemView extends AppCompatActivity {

    public SetSingleItemView(int imageID, View layout) {
        ImageView view = layout.findViewById(R.id.ImageView_single_image_item);
        view.setImageResource(imageID);
    }

}
