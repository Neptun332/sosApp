package com.example.patry.sosapp.DataBase.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.patry.sosapp.DataBase.Entities.Tour;

import java.util.List;

@Dao
public interface TourDao {

    @Insert
    void insert(Tour tour);

    @Update
    void update(Tour tour);

    @Delete
    void delete(Tour tour);

    @Query("DELETE FROM Tours")
    void deleteAllTours();

    @Query("SELECT * FROM TOURS ORDER BY ID DESC")
    LiveData<List<Tour>> getAllTours();

}
