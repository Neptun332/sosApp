package com.example.patry.sosapp.DataBase.Repo;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.patry.sosapp.DataBase.Dao.TourDao;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.SosAppDatabase;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

public class TourRepo {
    private TourDao tourDao;
    private LiveData<List<Tour>> allTours;
    private static long currentTourId;


    public TourRepo(Application application){
        SosAppDatabase database = SosAppDatabase.getSosAppDatabase(application);
        tourDao = database.tourDao();
        allTours = tourDao.getAllTours();
    }

    public void insert(Tour tour){
        new InsertTourAsyncTask(tourDao).execute(tour);
    }

    public void update(Tour tour){
        try {
            new UpdateTourAsyncTask(tourDao).execute(tour).get();
        }catch(Exception e){

        }
    }

    public void delete(Tour tour){
        new DeleteTourAsyncTask(tourDao).execute(tour);
    }

    public void deleteAllTours(){
        new DeleteAllTourAsyncTask(tourDao).execute();
    }

    public LiveData<List<Tour>> getAllTours() {
        return allTours;
    }

    public LiveData<Tour> getTourByTimeStamp(long timestamp) {

       return tourDao.getTourByTimeStamp(timestamp);
    }

    private static class InsertTourAsyncTask extends AsyncTask<Tour,Void, Void>{

        private TourDao tourDao;
        public long tourId;
        private InsertTourAsyncTask(TourDao tourDao){
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours){
            tourId = tourDao.insert(tours[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setCurrentTourId(tourId);
        }
    }

    private static class UpdateTourAsyncTask extends AsyncTask<Tour,Void, Void>{

        private TourDao tourDao;

        private UpdateTourAsyncTask(TourDao tourDao){
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours){

            tourDao.update(tours[0]);
            return null;
        }
    }

    private static class DeleteTourAsyncTask extends AsyncTask<Tour,Void, Void>{

        private TourDao tourDao;

        private DeleteTourAsyncTask(TourDao tourDao){
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Tour... tours){

            tourDao.delete(tours[0]);
            return null;
        }
    }

    private static class DeleteAllTourAsyncTask extends AsyncTask<Void,Void, Void>{

        private TourDao tourDao;

        private DeleteAllTourAsyncTask(TourDao tourDao){
            this.tourDao = tourDao;
        }

        @Override
        protected Void doInBackground(Void... Void){

            tourDao.deleteAllTours();
            return null;
        }
    }

    public static void setCurrentTourId(long id) {
        currentTourId = id;
    }

    public long getCurrentTourId() {
        return currentTourId;
    }
}
