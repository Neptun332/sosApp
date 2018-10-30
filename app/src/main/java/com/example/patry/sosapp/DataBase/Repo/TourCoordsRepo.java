package com.example.patry.sosapp.DataBase.Repo;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.patry.sosapp.DataBase.Dao.TourCoordsDao;
import com.example.patry.sosapp.DataBase.Dao.TourDao;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;
import com.example.patry.sosapp.DataBase.SosAppDatabase;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.List;

public class TourCoordsRepo {
    private TourCoordsDao tourCoordsDao;
    private LiveData<List<TourCoords>> allTourCoords;

    public TourCoordsRepo(Application application){
        SosAppDatabase database = SosAppDatabase.getSosAppDatabase(application);
        tourCoordsDao = database.tourCoordsDao();
        allTourCoords = tourCoordsDao.getAllTourCoords();
    }

    public void insert(TourCoords tourCoords){
        new InsertTourCoordsAsyncTask(tourCoordsDao).execute(tourCoords);
    }

    public void update(TourCoords tourCoords){
        new UpdateTourCoordsAsyncTask(tourCoordsDao).execute(tourCoords);
    }

    public void delete(TourCoords tourCoords){
        new DeleteTourCoordsAsyncTask(tourCoordsDao).execute(tourCoords);
    }

    public void deleteAllToursCoords(){
        new DeleteAllTourCoordsAsyncTask(tourCoordsDao).execute();
    }

    public void insertListToursCoords(ArrayList<TourCoords> tourCoordsList, int tourId){
        new InsertListToursCoordsAsyncTask(tourCoordsDao, tourId).execute(tourCoordsList);
    }

    public LiveData<List<TourCoords>> getAllTourCoords() {
        return allTourCoords;
    }

    private static class InsertTourCoordsAsyncTask extends AsyncTask<TourCoords,Void, Void>{

        private TourCoordsDao tourCoordsDao;

        private InsertTourCoordsAsyncTask(TourCoordsDao tourCoordsDao){
            this.tourCoordsDao = tourCoordsDao;
        }

        @Override
        protected Void doInBackground(TourCoords... tourCoords){

            tourCoordsDao.insert(tourCoords[0]);
            return null;
        }
    }
    private static class UpdateTourCoordsAsyncTask extends AsyncTask<TourCoords,Void, Void>{

        private TourCoordsDao tourCoordsDao;

        private UpdateTourCoordsAsyncTask(TourCoordsDao tourCoordsDao){
            this.tourCoordsDao = tourCoordsDao;
        }

        @Override
        protected Void doInBackground(TourCoords... tourCoords){

            tourCoordsDao.update(tourCoords[0]);
            return null;
        }
    }

    private static class DeleteTourCoordsAsyncTask extends AsyncTask<TourCoords,Void, Void>{

        private TourCoordsDao tourCoordsDao;

        private DeleteTourCoordsAsyncTask(TourCoordsDao tourCoordsDao){
            this.tourCoordsDao = tourCoordsDao;
        }

        @Override
        protected Void doInBackground(TourCoords... tourCoords){

            tourCoordsDao.delete(tourCoords[0]);
            return null;
        }
    }

    private static class DeleteAllTourCoordsAsyncTask extends AsyncTask<Void,Void, Void>{

        private TourCoordsDao tourCoordsDao;

        private DeleteAllTourCoordsAsyncTask(TourCoordsDao tourCoordsDao){
            this.tourCoordsDao = tourCoordsDao;
        }

        @Override
        protected Void doInBackground(Void... Void){

            tourCoordsDao.deleteAllTourCoords();
            return null;
        }
    }

    private static class InsertListToursCoordsAsyncTask extends AsyncTask<ArrayList<TourCoords>,Void, Void>{

        private TourCoordsDao tourCoordsDao;
        private int tourId;

        private InsertListToursCoordsAsyncTask(TourCoordsDao tourCoordsDao, int tourId){
            this.tourCoordsDao = tourCoordsDao;
            this.tourId = tourId;
        }

        @Override
        protected Void doInBackground(ArrayList<TourCoords>... tourCoordsList){
            for (int i =0; i<tourCoordsList[0].size() ; i++) {
                TourCoords tourCoords = tourCoordsList[0].get(i);
                tourCoords.setTourId(tourId);
                tourCoordsDao.insert(tourCoords);
            }
            return null;
        }
    }
}
