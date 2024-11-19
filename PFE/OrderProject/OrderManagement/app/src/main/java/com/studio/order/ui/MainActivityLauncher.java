package com.studio.order.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.studio.order.R;

public class MainActivityLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_launcher);
        // Delay the execution of checkLoginStatus() by 3 seconds
        Handler handler = new Handler();
        handler.postDelayed(this::checkLoginStatus, 3000);
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPrefs = getSharedPreferences("session", Context.MODE_PRIVATE);

        // Check if the userId exists in the shared preferences
        if (sharedPrefs.contains("userId")) {
            // User is already logged in, navigate to the desired activity
            Intent intent = new Intent(MainActivityLauncher.this, MainActivityCommands.class);
            startActivity(intent);
            finish();
        }else {

            Intent intent = new Intent(MainActivityLauncher.this, MainActivityLogin.class);
            startActivity(intent);
            finish();
        }
    }
}