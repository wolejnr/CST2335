package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.util.*;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmailR;
    private ImageButton buttonSnap;
    private Button chatButton;
    private Button weatherButton;
    private Button toolbarButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.e(ACTIVITY_NAME, " in function: " + "onCreate");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmailR = (EditText) findViewById(R.id.editTextEmailR);
        buttonSnap = (ImageButton) findViewById(R.id.buttonSnap);

        chatButton = (Button) findViewById(R.id.buttonChat);

        weatherButton = (Button) findViewById(R.id.buttonWeather);

        toolbarButton = (Button) findViewById(R.id.buttonToolbar);

        // get intent from MainActivity
        Intent mainIntent = getIntent();
        editTextEmailR.setText(mainIntent.getStringExtra("email"));

        buttonSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        // Implement Go to Chat functionality
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatActivity = new Intent(ProfileActivity.this, ChatRoomActivity.class);
                startActivity(chatActivity);
            }
        });

        // Implement Weather Forecast button functionality
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherActivity = new Intent(ProfileActivity.this, WeatherForecast.class);
                startActivity(weatherActivity);
            }
        });

        // Implement the Go To Toolbar page button
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toolbarActivity = new Intent(ProfileActivity.this, TestToolbar.class);
                startActivity(toolbarActivity);
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, " in function: " + "onActivityResult");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            buttonSnap.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, " in function: " + "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, " in function: " + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, " in function: " + "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, " in function: " + "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, " in function: " + "onResume");
    }
}
