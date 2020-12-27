package com.FallTurtle.recipediary.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.FallTurtle.recipediary.Fragment.ListFragment;
import com.FallTurtle.recipediary.Fragment.MemoFragment;
import com.FallTurtle.recipediary.Fragment.SearchFragment;
import com.FallTurtle.recipediary.R;
import com.FallTurtle.recipediary.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {
    private long pressedTime = 0;
    //프래그먼트 관련 변수
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ListFragment listFragment;
    private SearchFragment searchFragment;
    private MemoFragment memoFragment;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //플래그를 받아서 어떤 프래그먼트를 화면에 보여줄지 결정해주는 메소드, 하단 네비게이션 메뉴에서 실행됨
    public void setFmFlag(int flag){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (flag){

            case 1:
                fragmentTransaction.replace(R.id.frameLayout, listFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, searchFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction.replace(R.id.frameLayout, memoFragment);
                fragmentTransaction.commit();
                break;
            default:
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //인트로 액티비티 실행
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);

        //코드의 간략화를 위한 뷰바인딩
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //광고 넣기
        MobileAds.initialize(this,  new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);


        //일반 메뉴 리스너 지정
        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.google:
                        if(mAuth.getCurrentUser() == null){
                            Intent one = new Intent(MainActivity.this, GoogleActivity.class);
                            startActivity(one);
                        }
                        else{
                            Intent one = new Intent(MainActivity.this, Google2Activity.class);
                            startActivity(one);
                        }
                        break;
                    case R.id.info:
                        Intent three = new Intent(MainActivity.this, InformationActivity.class);
                        startActivity(three);
                        break;
                }
                return true;
            }
        });

        //네비게이션 메뉴 리스너를 지정하여 클릭 시마다 setFmFlag 메소드를 통해 프래그먼트가 변경될 수 있도록 한다.
        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.list:
                        setFmFlag(1);
                        break;
                    case R.id.search:
                        setFmFlag(2);
                        break;
                    case R.id.memo:
                        setFmFlag(3);
                        break;
                }
                return true;
            }
        });
        listFragment = new ListFragment();
        searchFragment = new SearchFragment();
        memoFragment = new MemoFragment();
        //디폴트 프래그먼트는 list!
        setFmFlag(1);


    }

    public void onBackPressed(){
        long curTime = System.currentTimeMillis();
        long TimeGap = curTime - pressedTime;

        if(TimeGap >=0 && TimeGap <= 2000){
            finish();
        }
        else{
            pressedTime = curTime;
            Toast.makeText(this,"뒤로 버튼을 한 번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
