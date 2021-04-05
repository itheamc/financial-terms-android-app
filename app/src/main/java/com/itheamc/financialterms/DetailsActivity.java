package com.itheamc.financialterms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Random;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private TextView title;
    private TextView desc;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = findViewById(R.id.post_title);
        desc = findViewById(R.id.post_desc);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: " + initializationStatus.toString());

            }
        });

        mAdView = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (generateRandInt() == 1) requestInterstitialAds(adRequest);

        setText(getIntent());

    }

    // Load interstitial ads
    private void requestInterstitialAds(AdRequest adRequest) {
        InterstitialAd.load(this, getResources().getString(R.string.interstitial_ad_unit_id), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                showInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    // Function to show the interstitial ads
    private void showInterstitialAds() {
        if (mInterstitialAd != null) {
            // Set fullscreen content callback to interstitial ads
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    mInterstitialAd = null;
                }
            });


            mInterstitialAd.show(DetailsActivity.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    // Function to set the text on the textview
    private void setText(Intent intent) {
        title.setText(intent.getStringExtra("title"));
        desc.setText(intent.getStringExtra("desc"));
//        setTitle(intent.getStringExtra("title"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onNavigateUp() {
        supportFinishAfterTransition();
        return true;
    }


    // Function to generate random number
    private int generateRandInt() {
        Random random = new Random();
        int rand = random.nextInt(100);
        return rand % 3;
    }
}