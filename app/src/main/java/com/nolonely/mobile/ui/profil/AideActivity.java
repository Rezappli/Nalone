package com.nolonely.mobile.ui.profil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nolonely.mobile.R;
import com.nolonely.mobile.util.Constants;


public class AideActivity extends AppCompatActivity {

    private ImageView buttonBack;
    private LinearLayout linearCGU;
    private LinearLayout linearIssues;
    private LinearLayout linearAboutUs;
    private LinearLayout linearSupportUs;
    private AdView ad;


    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_aide);
        
        buttonBack = findViewById(R.id.buttonBack);
        linearCGU = findViewById(R.id.linearCGU);
        linearIssues = findViewById(R.id.linearIssues);
        linearAboutUs = findViewById(R.id.linearAboutUs);
        linearSupportUs = findViewById(R.id.linearSupportUs);
        ad = findViewById(R.id.adView);

        buttonBack.setVisibility(View.VISIBLE);
        buttonBack.setOnClickListener(v -> onBackPressed());

        MobileAds.initialize(this, initializationStatus -> ad.loadAd(new AdRequest.Builder().build()));


        linearCGU.setOnClickListener(v -> startWebBrowserOnUrl(Constants.BASE_URL + "/CGU"));

        linearIssues.setOnClickListener(v -> startWebBrowserOnUrl(Constants.BASE_URL + "/issues"));

        linearAboutUs.setOnClickListener(v -> startWebBrowserOnUrl(Constants.BASE_URL + "/contact"));

        linearSupportUs.setOnClickListener(v -> startWebBrowserOnUrl(Constants.BASE_URL + "/support-us"));
    }

    private void startWebBrowserOnUrl(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }
}