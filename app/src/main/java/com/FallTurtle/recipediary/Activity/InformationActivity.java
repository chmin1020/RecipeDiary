package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.FallTurtle.recipediary.databinding.ActivityInformationBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class InformationActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private int adCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInformationBinding binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //광고
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        final AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(InformationActivity.this,"광고를 시청해주셔서 감사합니다 :)",Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adCnt < 1) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                        adCnt++;

                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
                else{
                    Toast.makeText(InformationActivity.this,"그만 봐주셔도 됩니다! 감사합니다 :)",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
