package com.FallTurtle.recipediary.MVVM;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.FallTurtle.recipediary.RecycleView.Shopping;

import java.util.List;

@Dao
public interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Shopping shopping);
    @Delete
    public void delete(Shopping shopping);
    @Query("select * from Shopping")
    public LiveData<List<Shopping>> getAllShopping();
}
