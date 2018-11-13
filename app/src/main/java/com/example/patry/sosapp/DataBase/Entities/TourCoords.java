package com.example.patry.sosapp.DataBase.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ToursCoords",
        indices ={@Index("tourId")}, foreignKeys = @ForeignKey(entity = Tour.class,
        parentColumns = "id",
        childColumns = "tourId",
        onDelete = CASCADE))
public class TourCoords implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "tourId")
    private long tourId;
    private double latitude;
    private double longitude;
    private long timestamp;
    private String date;

    public TourCoords(long tourId, double latitude, double longitude, long timestamp, String date) {
        this.tourId = tourId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.date = date;
    }

    protected TourCoords(Parcel in) {
        id = in.readLong();
        tourId = in.readLong();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
        date = in.readString();
    }

    public static final Creator<TourCoords> CREATOR = new Creator<TourCoords>() {
        @Override
        public TourCoords createFromParcel(Parcel in) {
            return new TourCoords(in);
        }

        @Override
        public TourCoords[] newArray(int size) {
            return new TourCoords[size];
        }
    };

    public void setId(long id) {
        this.id = id;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }

    public long getId() {
        return id;
    }

    public long getTourId() {
        return tourId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(tourId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(timestamp);
        dest.writeString(date);
    }
}