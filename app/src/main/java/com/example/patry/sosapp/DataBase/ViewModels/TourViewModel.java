package com.example.patry.sosapp.DataBase.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Repo.TourRepo;

import java.util.List;

public class TourViewModel extends AndroidViewModel {

    private TourRepo tourRepo;
    private LiveData<List<Tour>> allTours;

    public TourViewModel(@NonNull Application application) {
        super(application);
        tourRepo = new TourRepo(application);
        allTours = tourRepo.getAllTours();

    }

    public void insert(Tour tour){
        tourRepo.insert(tour);
    }

    public void update(Tour tour){
        tourRepo.update(tour);
    }

    public void delete(Tour tour){
        tourRepo.delete(tour);
    }

    public void deleteAllTours(){
        tourRepo.deleteAllTours();
    }

    public LiveData<List<Tour>> getAllTours(){
        return allTours;
    }
}
