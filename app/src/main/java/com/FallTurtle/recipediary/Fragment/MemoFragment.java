package com.FallTurtle.recipediary.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.FallTurtle.recipediary.Activity.AddShoppingActivity;
import com.FallTurtle.recipediary.MVVM.RViewModel;
import com.FallTurtle.recipediary.R;
import com.FallTurtle.recipediary.RecycleView.Shopping;
import com.FallTurtle.recipediary.RecycleView.SpListAdapter;

import java.util.List;

public class MemoFragment extends Fragment {
    private View view;
    private ImageView iv_add;

    //database
    private RViewModel viewModel;
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;

    //recyclerView
    private RecyclerView recyclerView;
    private SpListAdapter spListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_memo, container, false); //inflate -> 부풀리다, view xml을 실제 view 객체로 만들다
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        spListAdapter = new SpListAdapter();
        recyclerView.setAdapter(spListAdapter);

        //뷰모델 설정(내부 데이터베이스를 효율적으로 활용하기 위함)
        if(viewModelFactory == null){
            viewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication());
        }
        viewModel = new ViewModelProvider(this,viewModelFactory).get(RViewModel.class);

        //데이터베이스의 값이 바뀌면 관찰하여 바로 update 시켜준다.
        viewModel.getAllShopping().observe(this, new Observer<List<Shopping>>() {
            public void onChanged(List<Shopping> memos) {
                spListAdapter.update(viewModel.getAllShopping().getValue());
            }
        });

        //버튼
        iv_add = view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddShoppingActivity.class);
                startActivity(intent);
            }
        });
    }
}
