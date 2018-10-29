package com.example.patry.sosapp.DataBase.Entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "Tours")
public class Tour {



    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private long timeStamp;
    private String date;

    public Tour(String name, long timeStamp ,String date) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
