package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.FallTurtle.recipediary.databinding.ActivityGoogle2Binding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Google2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://recipediary-5afd1.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityGoogle2Binding binding = ActivityGoogle2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvNickname.setText(mAuth.getCurrentUser().getDisplayName());
        binding.tvEmail.setText(mAuth.getCurrentUser().getEmail());


        //광고 넣기
        MobileAds.initialize(this,  new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        //버튼 구현
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   mAuth.getInstance().signOut();
                   Intent intent = new Intent(Google2Activity.this, ProgressDialogActivity.class);
                   startActivity(intent);
                   Toast.makeText(Google2Activity.this,"로그아웃 완료", Toast.LENGTH_SHORT).show();
                   finish();
            }
        });

    }
}
