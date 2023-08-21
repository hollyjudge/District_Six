package com.example.district6;

import static com.example.district6.MapView.EXTRA_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

/*
    Retrives images associated with each site via an image array
    and loads them into a ViewPagerItem object
    which is then loaded into a VPAdapter object
 */
public class ImageActivity extends AppCompatActivity {

    //reference variable for viewpager
    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    //declare images array
    int[] images;

    /*
        assocaites array of images with each site based on number passed to it
        by MapView which is based on the site tapped or visited via location
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //get landmark number
        Intent intent = getIntent();
        int landmark = intent.getIntExtra(EXTRA_NUMBER,0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
      //
        viewPager2  = findViewById(R.id.viewpager);

        switch(landmark)
        {
            case 0:
                // st marks church
                images = new int[]{R.drawable.sm1,R.drawable.sm2,R.drawable.sm3,R.drawable.sm4};
                actionBar.setTitle("St Mark's Church");
                break;

            case 1:
                // star cinema
                images = new int[]{R.drawable.sc1,R.drawable.sc2};
                actionBar.setTitle("Star Cinema");
                break;

            case 2:
                // hanover street
                images = new int[]{R.drawable.hs1,R.drawable.hs2,R.drawable.hs3};
                actionBar.setTitle("Hanover Street");
                break;

            case 3:
                // seven steps
                images = new int[]{R.drawable.ss1,R.drawable.ss2,R.drawable.ss3};
                actionBar.setTitle("Seven Steps");
                break;

            case 4:
                // public wash house
                images = new int[]{R.drawable.pwh1,R.drawable.pwh2};
                actionBar.setTitle("Public Wash House");
                break;

            default:
                // display district 6 media
                images = new int[]{R.drawable.hs1,R.drawable.pwh1,R.drawable.ss2,R.drawable.sc1,R.drawable.sm3};

        }


        //initiate the array
        viewPagerItemArrayList = new ArrayList<>();
        //use for loop to populate arraylist
        for(int i =0 ; i< images.length; i++)
        {
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }

        //create object of the adapter class
        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        //set attributes to viewpager
        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    /*
        Enables back button
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}