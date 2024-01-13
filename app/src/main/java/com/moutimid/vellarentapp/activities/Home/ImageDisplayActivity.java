package com.moutimid.vellarentapp.activities.Home;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.moutamid.vellarentapp.R;

import java.util.ArrayList;
import java.util.List;


public class ImageDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        ViewPager viewPager = findViewById(R.id.viewPager);

        // Get image URLs and position from the intent
        List<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        int position = getIntent().getIntExtra("position", 0);

        // Set up the ViewPager with the ImagePagerAdapter
        com.moutamid.vellarentapp.adapter.ImagePagerAdapter imagePagerAdapter = new com.moutamid.vellarentapp.adapter.ImagePagerAdapter(this, imageUrls);
        viewPager.setAdapter(imagePagerAdapter);

        // Set the starting position
        viewPager.setCurrentItem(position);
    }
}
