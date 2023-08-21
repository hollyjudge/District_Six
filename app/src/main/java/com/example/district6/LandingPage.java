package com.example.district6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


/*
    creates a landing page for the application which includes a welcome message
    District Six banner and photo of seven steps
    link to District Six website
    and button to begin tour
    this button will first create a pop-up which prompts the user to accept the terms and conditions
    and then if accepted will display a pop up of the instructions for the app
    from here the user can move to the MapPage
 */
public class LandingPage extends AppCompatActivity {

    Button startTour;
    FragmentManager fm= getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);

        startTour = findViewById(R.id.welcome);
        startTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }

        });
    }

    /*
        handles buttons within pop-ups which switch to new pop-ups and then
        the MapView
     */
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the terms and conditions page
        LayoutInflater inflaterLandingPage = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflaterLandingPage.inflate(R.layout.accept_terms_popup, null);

        // create the popup window
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1500, true);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // inflate the layout of the instructions page
        LayoutInflater inflaterInstructionsPage = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView2 = inflaterInstructionsPage.inflate(R.layout.instructions, null);
        final PopupWindow popupWindow2 = new PopupWindow(popupView2, 1000, 1500, true);

        Button accept= (Button)popupView.findViewById(R.id.Accept);
        Button decline= (Button)popupView.findViewById(R.id.Decline);
        Button start= (Button)popupView2.findViewById(R.id.Begin);
        // dismiss the popup window when touched

        accept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0);
                return true;
            }
        });

        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(LandingPage.this, MapView.class);
                startActivity(intent);
                return true;
            }
        });



        decline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(LandingPage.this, "You can't use this app without accepting the terms and conditions", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
                return true;
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }
}


