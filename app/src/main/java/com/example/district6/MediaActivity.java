package com.example.district6;

import static com.example.district6.MapView.EXTRA_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/*
    Displays photos and descriptions of the sites
 */
public class MediaActivity extends AppCompatActivity {

    ImageView iv;
    //array that will contain the id's of the images
    int[] drawables = {R.drawable.stmarkschurch,R.drawable.starcinema,R.drawable.hanoverstreet,R.drawable.sevensteps,R.drawable.publicwashhouse,R.drawable.district6};

    TextView tv1;
    String[] titles = {"St Marks Church", "Star Cinema", "Hanover Street", "Seven Steps", "Public Wash House", "District 6"};
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        //get landmark number
        // Get the Intent that started this activity and extract the number
        Intent intent = getIntent();
        int landmark = intent.getIntExtra(EXTRA_NUMBER,0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //for media display of specific landmark
        iv=(ImageView) findViewById(R.id.topImage);
        //for text display
        tv1 = findViewById(R.id.titleTextView);
        tv2 = findViewById(R.id.descTextView);

        switch(landmark)
        {
            case 0:
                // st marks church
                displayImage(drawables[0]);
                displayLandmark(0);
                tv2.setText(getResources().getString(R.string.stmarks_desc));
                actionBar.setTitle("St Mark's Church");

                break;
            case 1:
                // star cinema
                displayImage(drawables[1]);
                displayLandmark(1);
                actionBar.setTitle("Star Cinema");
                tv2.setText(getResources().getString(R.string.starcinema_desc));
                break;
            case 2:
                // hanover street
                displayImage(drawables[2]);
                displayLandmark(2);
                actionBar.setTitle("Hanover Street");
                tv2.setText(getResources().getString(R.string.hanover_desc));
                break;
            case 3:
                // severn steps
                displayImage(drawables[3]);
                displayLandmark(3);
                actionBar.setTitle("Seven Steps");
                tv2.setText(getResources().getString(R.string.sevensteps_desc));
                break;
            case 4:
                // public wash house
                displayImage(drawables[4]);
                displayLandmark(4);
                actionBar.setTitle("Public Wash House");
                tv2.setText(getResources().getString(R.string.publicwashhouse_desc));


                break;
            default:
                // display district 6 media
                displayImage(drawables[5]);
                displayLandmark(5);
                tv2.setText(getResources().getString(R.string.district6_desc));
        }

        //to open video player activity
        Button videoButton = findViewById(R.id.videoButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(MediaActivity.this,VideoActivity.class);
                videoIntent.putExtra(EXTRA_NUMBER, landmark);
                startActivity(videoIntent);
            }
        });

        //to open image viewing
        Button imagesButton = findViewById(R.id.imageButton);
        imagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(MediaActivity.this,ImageActivity.class);
                imageIntent.putExtra(EXTRA_NUMBER, landmark);
                startActivity(imageIntent);
            }
        });

    }

    private void displayLandmark(int position) {
        tv1.setText(titles[position]);
    }

    private void displayImage(int image) {
        iv.setImageResource(image);
    }

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
