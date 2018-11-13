package com.example.patry.sosapp.DataBase.Entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "Tours")
public class Tour {



    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private long timeStamp;
    private String date;
    private boolean isSaved;

    public Tour(String name, long timeStamp ,String date, boolean isSaved) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.date = date;
        this.isSaved = isSaved;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public long getId() {
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

    public boolean isSaved() {
        return isSaved;
    }
}
