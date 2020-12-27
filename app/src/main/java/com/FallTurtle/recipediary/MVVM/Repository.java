package com.FallTurtle.recipediary.MVVM;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.FallTurtle.recipediary.RecycleView.Shopping;

import java.util.List;

public class Repository {
    private RDatabase database;
    private ShoppingDao shoppingDao;
    private LiveData<List<Shopping>> list2;

    //생성자 (application 받아옴)
    Repository(Context application){
        database = RDatabase.getDB(application);
        shoppingDao = database.shoppingDao();
        list2 = shoppingDao.getAllShopping();
    }

    //view모델에서 db에 접근을 요청하면 실행될 함수
    public LiveData<List<Shopping>> getAllShopping(){
        return this.list2;
    }

    public void insert(final Shopping shopping) {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    shoppingDao.insert(shopping);
                }
            });
            thread.start();
        } catch (Exception e) { }
    }

    public void delete(final Shopping shopping) {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    shoppingDao.delete(shopping);
                }
            });
            thread.start();
        } catch (Exception e) { }
    }
}
