package com.FallTurtle.recipediary.RecycleView;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Shopping {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "itemList")
    private ArrayList<String> list;
    @ColumnInfo(name = "checkList")
    private ArrayList<String> list2;


    public Shopping(String title, String date, ArrayList<String> list, ArrayList<String> list2) {
        this.title = title;
        this.date = date;
        this.list = list;
        this.list2 = list2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList2() {
        return list2;
    }

    public void setList2(ArrayList<String> list2) {
        this.list2 = list2;
    }
}
