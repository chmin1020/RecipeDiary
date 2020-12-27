package com.FallTurtle.recipediary.MVVM;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.FallTurtle.recipediary.RecycleView.Shopping;

import java.util.List;

public class RViewModel extends AndroidViewModel {
    private Application application;
    private Repository  repository;
    private LiveData<List<Shopping>> list2;

    //생성자(activity의 context를 받으면 액티비티 종료 시 메모리 누수 발생 가능, 따라서 어플리케이션 context 사용)
    public RViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new Repository(application.getApplicationContext());
        list2 = repository.getAllShopping();
    }

    //쓰레드에서 접근할 메소드 생성
    public LiveData<List<Shopping>> getAllShopping() { return this.list2; }
    public void insert(Shopping shopping){
        repository.insert(shopping);
    }
    public void delete(Shopping shopping){
        repository.delete(shopping);
    }
}
