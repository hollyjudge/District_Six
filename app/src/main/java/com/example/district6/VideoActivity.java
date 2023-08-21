package com.example.district6;

import static com.example.district6.MapView.EXTRA_NUMBER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

/*
    associates video to site based on extra number passed to it
    where 0=st marks
    1=star cinema
    2= hanover street
    3= seven steps
    4=public wash house
 */
public class VideoActivity extends AppCompatActivity {

    VideoView vView;
    TextView tView;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //for action bar on top
        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get landmark number
        Intent intent = getIntent();
        int landmark = intent.getIntExtra(EXTRA_NUMBER,0);

        tView = findViewById(R.id.textView);
        vView = findViewById(R.id.videoView);


        switch(landmark)
        {
            case 0:
                // st marks church
                playMedia(R.raw.stmarks);
                actionBar.setTitle("St Mark's Church");
                break;

            case 1:
                // star cinema
                displayVideoMessage();
                actionBar.setTitle("Star Cinema");
                break;

            case 2:
                // hanover street
                playMedia(R.raw.hanoverstreet);
                actionBar.setTitle("Hanover Street");
                break;

            case 3:
                // seven steps
                playMedia(R.raw.sevensteps);
                actionBar.setTitle("Seven Steps");
                break;

            case 4:
                // public wash house
                displayVideoMessage();
                actionBar.setTitle("Public Wash House");
                break;

            default:
                // display district 6 media
                displayVideoMessage();
        }

    }

    /*
        displayed for sites that do not have videos currently
     */
    private void displayVideoMessage() {
        tView.setText(getResources().getString(R.string.video_message));
    }

    /*
        plays the video for the site
     */
    private void playMedia(int vid) {
        //int video = vid;
        videoPath = "android.resource://"+getPackageName()+"/"+ vid;
        vView.setVideoURI(Uri.parse(videoPath));
        //for video controls
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vView);
        vView.setMediaController(mediaController);
    }

    /*
        enables back button
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
