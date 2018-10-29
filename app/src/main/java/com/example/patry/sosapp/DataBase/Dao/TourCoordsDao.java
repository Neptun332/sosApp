package com.example.patry.sosapp.DataBase.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;

import java.util.List;

@Dao
public interface TourCoordsDao {

    @Insert
    void insert(TourCoords tourCoords);

    @Update
    void update(TourCoords tourCoords);

    @Delete
    void delete(TourCoords tourCoords);

    @Query("DELETE FROM ToursCoords")
    void deleteAllTourCoords();

    @Query("SELECT * FROM ToursCoords ORDER BY Id DESC")
    LiveData<List<TourCoords>> getAllTourCoords();

}
