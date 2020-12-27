package com.FallTurtle.recipediary.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.FallTurtle.recipediary.Activity.WebActivity;
import com.FallTurtle.recipediary.R;


public class SearchFragment extends Fragment {
    private View view;
    private EditText et_searchGoogle;
    private EditText et_searchYoutube;
    private ImageView iv_searchGoogle;
    private ImageView iv_searchYoutube;

    //상수
    private final int CODE_FOR_GOOGLE = 11;
    private final int CODE_FOR_YOUTUBE = 12;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false); //inflate -> 부풀리다, view xml을 실제 view 객체로 만들다
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //위젯과 코드 연결
        et_searchGoogle = view.findViewById(R.id.et_searchGoogle);
        et_searchYoutube = view.findViewById(R.id.et_searchYoutube);
        iv_searchGoogle = view.findViewById(R.id.iv_searchGoogle);
        iv_searchYoutube = view.findViewById(R.id.iv_searchYoutube);

        //버튼 설정
        final Intent intent = new Intent(getActivity(), WebActivity.class);
        iv_searchGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("search", et_searchGoogle.getText().toString());
                intent.putExtra("code", CODE_FOR_GOOGLE);
                startActivity(intent);
            }
        });

        iv_searchYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("search", et_searchYoutube.getText().toString());
                intent.putExtra("code", CODE_FOR_YOUTUBE);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        et_searchYoutube.setText("");
        et_searchGoogle.setText("");

    }
}
