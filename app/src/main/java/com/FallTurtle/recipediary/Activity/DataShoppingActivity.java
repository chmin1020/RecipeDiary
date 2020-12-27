package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.FallTurtle.recipediary.MVVM.RViewModel;
import com.FallTurtle.recipediary.RecycleView.Shopping;
import com.FallTurtle.recipediary.RecycleView.ShoppingAdapter;
import com.FallTurtle.recipediary.databinding.ActivityDataShoppingBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataShoppingActivity extends AppCompatActivity {
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
        final ActivityDataShoppingBinding binding = ActivityDataShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //뷰모델 설정(내부 데이터베이스를 효율적으로 활용하기 위함)
        if(viewModelFactory == null){
            viewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        }
        viewModel = new ViewModelProvider(this,viewModelFactory).get(RViewModel.class);

        //광고 넣기
        MobileAds.initialize(this,  new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        //인텐트로 데이터 받아오기
        final Intent intent = getIntent();
        final int DB_id = intent.getIntExtra("id",-9999);
        binding.tvTitle2.setText(intent.getStringExtra("title"));
        arrayList = intent.getStringArrayListExtra("itemList");
        arrayList2 = intent.getStringArrayListExtra("checkList");

        //리사이클러뷰 설정
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingAdapter = new ShoppingAdapter(arrayList,arrayList2);
        binding.recyclerView.setAdapter(shoppingAdapter);


        //버튼 설정
        binding.btAdd.setOnClickListener(new View.OnClickListener() { //추가 버튼 설정
            @Override
            public void onClick(View v) {
                shoppingAdapter.insert(binding.etItem.getText().toString());
                binding.etItem.setText("");
            }
        });

        binding.tvDelete.setOnClickListener(new View.OnClickListener() { //back 버튼 설정
            @Override
            public void onClick(View v) {
                //삭제 버튼 클릭 시 생성될 다이얼로그 설정
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DataShoppingActivity.this)
                        .setTitle("확인해주세요")  //다이얼로그의 제목
                        .setMessage("정말로 삭제하시겠습니까?"); //다이얼로그의 내용

                alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() { //다이얼로그 긍정대답버튼 정하기
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Shopping shopping = new Shopping(intent.getStringExtra("title"),
                                intent.getStringExtra("date"),
                                intent.getStringArrayListExtra("itemList"), intent.getStringArrayListExtra("checkList"));
                        shopping.setId(DB_id);
                        viewModel.delete(shopping);
                        Toast.makeText(DataShoppingActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() { //다이얼로그 부정대답버튼 정하기
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.create().show();
            }
        });

        binding.tvSave.setOnClickListener(new View.OnClickListener() { //save 버튼 설정
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String cur = sdf.format(date);

                Shopping shopping = new Shopping(binding.tvTitle2.getText().toString(),
                        cur, arrayList, arrayList2);
                shopping.setId(DB_id);

                viewModel.insert(shopping);

                Toast.makeText(DataShoppingActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
