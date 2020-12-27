package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.FallTurtle.recipediary.MVVM.RViewModel;
import com.FallTurtle.recipediary.RecycleView.Shopping;
import com.FallTurtle.recipediary.RecycleView.ShoppingAdapter;
import com.FallTurtle.recipediary.databinding.ActivityAddShoppingBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddShoppingActivity extends AppCompatActivity {
    //리사이클러뷰 변수
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList2 = new ArrayList<>();
    ShoppingAdapter shoppingAdapter;

    //database
    private RViewModel viewModel;
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddShoppingBinding activity_add_shopping = ActivityAddShoppingBinding.inflate(getLayoutInflater());
        setContentView(activity_add_shopping.getRoot());

        //광고 넣기
        MobileAds.initialize(this,  new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        activity_add_shopping.adView.loadAd(adRequest);


        //뷰모델 설정(내부 데이터베이스를 효율적으로 활용하기 위함)
        if(viewModelFactory == null){
            viewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        }
        viewModel = new ViewModelProvider(this,viewModelFactory).get(RViewModel.class);

        //리사이클뷰 설정
        activity_add_shopping.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingAdapter = new ShoppingAdapter(arrayList, arrayList2);
        activity_add_shopping.recyclerView.setAdapter(shoppingAdapter);

        //버튼 설정
        activity_add_shopping.btAdd.setOnClickListener(new View.OnClickListener() { //추가 버튼 설정
            @Override
            public void onClick(View v) {
                shoppingAdapter.insert(activity_add_shopping.etItem.getText().toString());
                activity_add_shopping.etItem.setText("");
            }
        });

        activity_add_shopping.tvBack.setOnClickListener(new View.OnClickListener() { //back 버튼 설정
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activity_add_shopping.tvSave.setOnClickListener(new View.OnClickListener() { //save 버튼 설정
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String cur = sdf.format(date);

                viewModel.insert(new Shopping(activity_add_shopping.etTitle.getText().toString(),
                        cur, arrayList, arrayList2));

                Toast.makeText(AddShoppingActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
