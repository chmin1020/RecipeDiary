package com.FallTurtle.recipediary.MVVM;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.FallTurtle.recipediary.RecycleView.Shopping;

@Database(entities = {Shopping.class}, version = 5)
@TypeConverters(Converters.class)
public abstract class RDatabase extends RoomDatabase {
   abstract ShoppingDao shoppingDao();
    static RDatabase INSTANCE = null;


    public static RDatabase getDB(Context context){
        if(INSTANCE == null){
            synchronized (RDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RDatabase.class, "RDB").fallbackToDestructiveMigration()
                        .build();

            }
        }
        return INSTANCE;
    }
}
