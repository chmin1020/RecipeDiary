package com.FallTurtle.recipediary.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.FallTurtle.recipediary.RecycleView.IngredientAdapter;
import com.FallTurtle.recipediary.RecycleView.RoutineAdapter;
import com.FallTurtle.recipediary.databinding.ActivityUpdateBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    //이미지뷰를 위한 변수
    private final int REQ_CAMERA_SELECT = 11;
    private Uri selectedImageUri = null;
    private ImageView iv_Image;
    //리사이클러뷰 변수
    private ArrayList<String> ingredients;
    private ArrayList<String> routines;
    private IngredientAdapter ingredientAdapter;
    private RoutineAdapter routineAdapter;
    //database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Uri downloadUri;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityUpdateBinding binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iv_Image = binding.ivImage;
        //인텐트를 통해 데이터베이스 저장 값 받아옴
        final Intent intent = getIntent();
        final StorageReference imageRef = storageRef.child("images").child(intent.getStringExtra("image"));

        image = intent.getStringExtra("image");
        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(UpdateActivity.this)
                            .load(task.getResult())
                            .into(binding.ivImage);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                    Toast.makeText(UpdateActivity.this, "사진을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.etFoodname.setText(intent.getStringExtra("title"));
        for(int i = 0; i < binding.spCategory.getCount(); i++){
            if(binding.spCategory.getItemAtPosition(i).toString().equals(intent.getStringExtra("category"))){
                binding.spCategory.setSelection(i);
                break;
            }
        }
        binding.etMemo.setText(intent.getStringExtra("memo"));
        ingredients = intent.getStringArrayListExtra("ingredients");
        routines = intent.getStringArrayListExtra("routines");

        //리사이클러뷰 연결
        binding.ingredientList.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new IngredientAdapter(ingredients);
        binding.ingredientList.setAdapter(ingredientAdapter);

        binding.routineList.setLayoutManager(new LinearLayoutManager(this));
        routineAdapter = new RoutineAdapter(routines);
        binding.routineList.setAdapter(routineAdapter);

        //버튼 설정
        binding.ivImage.setOnClickListener(new View.OnClickListener() { //이미지뷰 역할 정의 -> 갤러리에서 사진을 받아옴
            @Override
            public void onClick(View v) { // 이미지 가져오기
                iv_Image = binding.ivImage;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,REQ_CAMERA_SELECT);
            }
        });

        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); //뒤로 버튼 -> 액티비티 종료

        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //저장 버튼 -> 변경 내용을 담은 entity를 생성하여 db에 저장
                if(selectedImageUri !=null) {
                    imageRef.delete();
                    upload();
                }

                //현재 시간을 구함
                Date current = new Date(System.currentTimeMillis());
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String now = sdfNow.format(current);

                Map<String, Object> taskMap = new HashMap<>();
                taskMap.put("image", image);
                taskMap.put("date", now);
                taskMap.put("title",binding.etFoodname.getText().toString());
                taskMap.put("category",binding.spCategory.getSelectedItem().toString());
                taskMap.put("ingredients",ingredients);
                taskMap.put("routines",routines);
                taskMap.put("memo",binding.etMemo.getText().toString());

                //수정하기
                db.collection("users").document(mAuth.getCurrentUser().getEmail())
                        .collection("recipes").document(intent.getStringExtra("dbid")).update(taskMap);
                Log.d("tagtag","1115");
                //수정 완료 코드를 보내기 위한 인텐트 구문
                Intent intent1 = new Intent();
                Toast.makeText(UpdateActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent1);
                finish();

            }
        });

        binding.btnIngredientAdd.setOnClickListener(new View.OnClickListener() { //ingredient 추가 버튼 설정
            @Override
            public void onClick(View v) {
                ingredientAdapter.insert();
            }
        });

        binding.btnRoutineAdd.setOnClickListener(new View.OnClickListener() { //routine 추가 버튼 설정
            @Override
            public void onClick(View v) {
                routineAdapter.insert();
            }
        });
    }

    @Override //인텐트로 넘어갔다가 다시 돌아온 결과
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA_SELECT) {
            if (resultCode == RESULT_OK) {
                File f = new File(getPath(data.getData()));
                selectedImageUri = data.getData();
                iv_Image.setImageURI(selectedImageUri);
            }
        }
    }

    //제대로 된 uri 가져오기
    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    //사진 업로드
    public void upload() {
        //파이어베이스 스토리지에 사진 업로드

        //경로 및 참조 만들기
        File file =new File(getPath(selectedImageUri));
        downloadUri = Uri.fromFile(file);
        final StorageReference fileRef = storageRef.child("images/" + downloadUri.getLastPathSegment());
        image = downloadUri.getLastPathSegment();

        try { //스트림을 통한 파일 업로드
            InputStream stream = new FileInputStream(file);
            UploadTask uploadTask = fileRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
