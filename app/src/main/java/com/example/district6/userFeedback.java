package com.example.district6;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.CaptivePortal;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Fetches user input from the UI and saves it to a
    firebase database
 */
public class userFeedback extends AppCompatActivity {
    EditText namedata, emaildata, messagedata;
    Button send, returnHome;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        namedata = findViewById(R.id.namedata);
        emaildata = findViewById(R.id.emaildata);
        messagedata = findViewById(R.id.messagedata);

        ActionBar actionBar = getSupportActionBar();
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Leave Feedback");

        String regex= "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"; // used to validate email address
        Pattern pattern= Pattern.compile(regex);


        send = findViewById(R.id.btn_send);
        returnHome = findViewById(R.id.btn_home);

        Firebase.setAndroidContext(this);
        String UniqueID=
        Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID); //generate unique key per user

        firebase= new Firebase("https://district6-group3-default-rtdb.firebaseio.com/Users" + UniqueID);

        /*
            Checks if user input valid, if it is, upload to database,
            else, output error messages
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = namedata.getText().toString();
                final String email = emaildata.getText().toString();
                final String message = messagedata.getText().toString();

                Firebase child_name = firebase.child("Name");
                child_name.setValue(name);
                if (name.isEmpty()) {
                    namedata.setError("This is a required field!");
                    send.setEnabled(false);
                } else {
                    namedata.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_email = firebase.child("Email");
                child_email.setValue(email);
                Matcher matcher=pattern.matcher(email);
                if (email.isEmpty()) {
                    emaildata.setError("This is a required field!");
                    send.setEnabled(false);
                }
                else if (!matcher.matches()){
                    emaildata.setError("This isn't a valid email address!");
                    send.setEnabled(false);
                }
            else {
                    emaildata.setError(null);
                    send.setEnabled(true);
                }

                Firebase child_message = firebase.child("Message");
                child_message.setValue(message);
                if (message.isEmpty()) {
                    messagedata.setError("This is a required field!");
                    send.setEnabled(false);
                } else {
                    messagedata.setError(null);
                    send.setEnabled(true);
                }

                if ((!message.isEmpty()) && (!email.isEmpty()) && (!name.isEmpty()) && matcher.matches())
                    Toast.makeText(userFeedback.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();

            }
        });

        /*
            Returns to landing page
         */
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userFeedback.this, LandingPage.class);
                startActivity(intent);

            }
        });

    }

    /*
     Enable the back button
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
