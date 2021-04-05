package com.itheamc.financialterms;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itheamc.financialterms.utilities.NetworkUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // the status bar.
        hideSystemUI();
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (NetworkUtil.isConnected(MainActivity.this)) {
                    startSearchActivity();
                } else {
                    Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, 2000);

    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }




    // Function to start new activity
    private void startSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        this.finish();
    }


}