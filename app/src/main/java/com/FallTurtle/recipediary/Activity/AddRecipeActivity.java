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

import com.FallTurtle.recipediary.MVVM.RecipeDTO;
import com.FallTurtle.recipediary.RecycleView.IngredientAdapter;
import com.FallTurtle.recipediary.RecycleView.RoutineAdapter;
import com.FallTurtle.recipediary.databinding.ActivityAddRecipeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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

public class AddRecipeActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private RecipeDTO recipeDTO = new RecipeDTO();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    StorageReference storageRef = storage.getInstance().getReferenceFromUrl("gs://recipediary-5afd1.appspot.com");

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
    private DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getEmail());
    Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddRecipeBinding binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage = FirebaseStorage.getInstance();

        //각각의 리사이클러뷰 연결
        ingredients = new ArrayList<>();
        routines = new ArrayList<>();

        binding.ingredientList.setLayoutManager(new LinearLayoutManager(this));
        ingredientAdapter = new IngredientAdapter(ingredients);
        binding.ingredientList.setAdapter(ingredientAdapter);

        binding.routineList.setLayoutManager(new LinearLayoutManager(this));
        routineAdapter = new RoutineAdapter(routines);
        binding.routineList.setAdapter(routineAdapter);

        ingredientAdapter.insert();
        routineAdapter.insert();

        //이미지뷰 연결
        iv_Image = binding.ivImage;

        //버튼 설정
        binding.tvBack.setOnClickListener(new View.OnClickListener() { //back 버튼 설정
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvSave.setOnClickListener(new View.OnClickListener() { //save 버튼 설정
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String cur = sdf.format(date);
                String id = binding.etFoodname.getText().toString()
                        + "-"+ new SimpleDateFormat("yyyyMMddHHmmss").format(date);

                if (selectedImageUri == null) {
                    Toast.makeText(AddRecipeActivity.this, "사진은 필수입니다!", Toast.LENGTH_SHORT).show();
                } else {
                    upload();

                    //fireStore에 저장할 db 값 생성 (HashMap으로)
                    HashMap<String, Object> recipe = new HashMap<>();
                    recipe.put("dbId", id);
                    recipe.put("image", downloadUri.getLastPathSegment());
                    recipe.put("title", binding.etFoodname.getText().toString());
                    recipe.put("date", cur);
                    recipe.put("category",binding.spCategory.getSelectedItem().toString());
                    recipe.put("ingredients", ingredients);
                    recipe.put("routines", routines);
                    recipe.put("memo", binding.etMemo.getText().toString());

                    // 이메일이 db에 없으면 알아서 추가하고 -> 추가 문서를 담는다.
                    docRef.collection("recipes").document(id).set(recipe);

                    Toast.makeText(AddRecipeActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        binding.ivImage.setOnClickListener(new View.OnClickListener() { //이미지뷰 역할 정의 -> 갤러리에서 사진을 받아옴
            @Override
            public void onClick(View v) { // 이미지 가져오기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, REQ_CAMERA_SELECT);
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
        //경로 및 참조 만들기
        File file =new File(getPath(selectedImageUri));
        downloadUri = Uri.fromFile(file);
        final StorageReference fileRef = storageRef.child("images").child(downloadUri.getLastPathSegment());

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

