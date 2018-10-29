package com.example.patry.sosapp.DataBase.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.patry.sosapp.DataBase.Dao.TourCoordsDao;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;
import com.example.patry.sosapp.DataBase.Repo.TourCoordsRepo;
import com.example.patry.sosapp.DataBase.Repo.TourRepo;

import java.util.ArrayList;
import java.util.List;

public class TourCoordsViewModel extends AndroidViewModel {

    private TourCoordsRepo tourCoordsRepo;
    private LiveData<List<TourCoords>> allToursCoords;

    public TourCoordsViewModel(@NonNull Application application) {
        super(application);
        tourCoordsRepo = new TourCoordsRepo(application);
        allToursCoords = tourCoordsRepo.getAllTourCoords();

    }

    public void insert(TourCoords tourCoords) {
        tourCoordsRepo.insert(tourCoords);
    }

    public void update(TourCoords tourCoords) {
        tourCoordsRepo.update(tourCoords);
    }

    public void delete(TourCoords tourCoords) {
        tourCoordsRepo.delete(tourCoords);
    }

    public void deleteAllToursCoords() {
        tourCoordsRepo.deleteAllToursCoords();
    }

    public LiveData<List<TourCoords>> getAllToursCoords() {
        return allToursCoords;
    }

    public void insertList(ArrayList<TourCoords> tourCoordsList) {   tourCoordsRepo.insertListToursCoords(tourCoordsList); }
}