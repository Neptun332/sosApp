package com.example.patry.sosapp.DataBase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.patry.sosapp.DataBase.Dao.TourCoordsDao;
import com.example.patry.sosapp.DataBase.Dao.TourDao;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;

import java.util.Date;


@Database(entities = {Tour.class, TourCoords.class}, version = 1)
public abstract class SosAppDatabase extends RoomDatabase {

    public abstract TourDao tourDao();
    public abstract TourCoordsDao tourCoordsDao();

    private static volatile SosAppDatabase DBInstance;

    public static SosAppDatabase getSosAppDatabase(final Context context) {
        if(DBInstance == null) {
            synchronized (SosAppDatabase.class) {
                if(DBInstance == null) {
                    //create DB here
                    DBInstance = Room.databaseBuilder(context.getApplicationContext(), SosAppDatabase.class,"sosApp")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return  DBInstance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(DBInstance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private TourDao tourDao;

        private PopulateDbAsyncTask(SosAppDatabase db){
            tourDao = db.tourDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tourDao.insert(new Tour("zbc", 123,"data 1"));
            tourDao.insert(new Tour("asd", 1234,"data 2"));
            tourDao.insert(new Tour("zbqwec", 12345,"data 3"));
            return null;
        }
    }
}
