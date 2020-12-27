package com.FallTurtle.recipediary.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.FallTurtle.recipediary.GlideApp;
import com.FallTurtle.recipediary.RecycleView.IngredientResultAdapter;
import com.FallTurtle.recipediary.RecycleView.RoutineResultAdapter;
import com.FallTurtle.recipediary.databinding.ActivityDataBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {
    private static final int REQUEST_TEST = 0; //반드시 0으로 설정할 것!!!
    //리사이클러뷰 변수
    private ArrayList<String> ingredients;
    private ArrayList<String> routines;
    private IngredientResultAdapter ingredientResultAdapter;
    private RoutineResultAdapter routineResultAdapter;
    //데이터베이스
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://recipediary-5afd1.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ActivityDataBinding binding = ActivityDataBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //progress
        Intent progress = new Intent(this,ProgressDialogActivity.class);
        startActivity(progress);

        //데이터 내용 구성
       final Intent intent = getIntent();
        // Reference to an image file in Cloud Storage
        final StorageReference imageRef = storageRef.child("images").child(intent.getStringExtra("image"));


        //사진
        GlideApp.with(this).load(imageRef).into(binding.ivImage);

        binding.tvFoodname.setText(intent.getStringExtra("title"));
        binding.tvCategory2.setText(intent.getStringExtra("category"));
        binding.tvMemo2.setText(intent.getStringExtra("memo"));
        ingredients = intent.getStringArrayListExtra("ingredients");
        routines = intent.getStringArrayListExtra("routines");

        //각각의 리사이클러뷰 연결
        binding.ingredientList.setLayoutManager(new LinearLayoutManager(this));
        binding.routineList.setLayoutManager(new LinearLayoutManager(this));
        ingredientResultAdapter = new IngredientResultAdapter(ingredients);
        routineResultAdapter = new RoutineResultAdapter(routines);
        binding.ingredientList.setAdapter(ingredientResultAdapter);
        binding.routineList.setAdapter(routineResultAdapter);



        //버튼 설정
        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //삭제 버튼 클릭 시 생성될 다이얼로그 설정
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DataActivity.this)
                        .setTitle("확인해주세요")  //다이얼로그의 제목
                        .setMessage("정말로 삭제하시겠습니까?"); //다이얼로그의 내용

                alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() { //다이얼로그 긍정대답버튼 정하기
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent1 = new Intent(DataActivity.this, ProgressDialogActivity.class);
                        startActivity(intent1);

                        //삭제
                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                 db.collection("users").document(mAuth.getCurrentUser().getEmail())
                                        .collection("recipes").document(intent.getStringExtra("dbid")).delete();
                            }
                        });

                        Toast.makeText(DataActivity.this,"삭제되었습니다.",Toast.LENGTH_SHORT).show();
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

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //권한리스너 생성
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent1 = new Intent(DataActivity.this, UpdateActivity.class);
                        intent1.putExtra("dbid",intent.getStringExtra("dbid"));
                        intent1.putExtra("image",intent.getStringExtra("image"));
                        intent1.putExtra("title",binding.tvFoodname.getText());
                        intent1.putExtra("category",binding.tvCategory2.getText());
                        intent1.putExtra("ingredients",ingredients);
                        intent1.putExtra("routines",routines);
                        intent1.putExtra("memo",binding.tvMemo2.getText());
                        startActivityForResult(intent1,REQUEST_TEST);
                    }
                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(DataActivity.this,"권한이 없으면 레시피 저장 기능 사용이 불가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                };

                //권한리스너 통과 시에만 넘어감
                new TedPermission(DataActivity.this)
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();


            }
        });
    }

    //수정한 메모의 내용을 받아 내용 수정 완료
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TEST && resultCode == RESULT_OK && data != null){ //수정 버튼을 통해 실행한 인텐트가 맞고, 정상적으로 보내졌다면
            finish();
        }
    }
}
