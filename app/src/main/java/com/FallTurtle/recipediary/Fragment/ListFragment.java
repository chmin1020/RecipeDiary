package com.FallTurtle.recipediary.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.FallTurtle.recipediary.Activity.AddRecipeActivity;
import com.FallTurtle.recipediary.Activity.GoogleActivity;
import com.FallTurtle.recipediary.MVVM.RecipeDTO;
import com.FallTurtle.recipediary.R;
import com.FallTurtle.recipediary.RecycleView.RecipeDtoAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements TextWatcher {
    private View view;
    private EditText et_search;
    private ImageView iv_add;

    //database
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser curUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;
    private List<RecipeDTO> recipeDTOS = new ArrayList<>();

    //recyclerView
    private RecyclerView recyclerView;
    private RecipeDtoAdapter recipeDtoAdapter;

    //앱 실행 전 권한을 받기 위한 다이얼로그
    private void showPermissionDialog() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
                startActivity(intent);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(),"권한이 없으면 레시피 저장 기능 사용이 불가능합니다.", Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(getActivity())
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false); //inflate -> 부풀리다, view xml을 실제 view 객체로 만들다
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //검색창과 textWatcher 연결
        et_search = getView().findViewById(R.id.et_search);
        et_search.addTextChangedListener(this);

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipeDtoAdapter = new RecipeDtoAdapter();
        recyclerView.setAdapter(recipeDtoAdapter);

        updateDB();

        //버튼 연결
        iv_add = getView().findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) { //구글 연동 페이지로 이동
                    intent = new Intent(getActivity(), GoogleActivity.class);
                    startActivity(intent);
                }
                else {//레시피 추가 페이지로 이동
                    showPermissionDialog();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        et_search.setText("");
        if (mAuth.getCurrentUser() != curUser) {
            updateDB();
            curUser = mAuth.getCurrentUser();
        }
    }

    //database를 갱신하는 메소드
    public void updateDB() {
        if(mAuth.getCurrentUser() != null)
            docRef = db.collection("users").document(mAuth.getCurrentUser().getEmail());
        recipeDtoAdapter.update(recipeDTOS);
        recipeDTOS.clear();
        if (mAuth.getCurrentUser() != null){
            docRef.collection("recipes").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                    recipeDTOS.clear();
                    for(QueryDocumentSnapshot doc : querySnapshot) {
                        recipeDTOS.add(doc.toObject(RecipeDTO.class));
                    }
                    recipeDtoAdapter.update(recipeDTOS);
                }
            });

        }
    }

    //텍스트와쳐의 메소드 오버라이드(글자가 바뀔 때 실행될 코드)
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        recipeDtoAdapter.getFilter().filter(et_search.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
