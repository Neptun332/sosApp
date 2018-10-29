package com.example.patry.sosapp.DataBase.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ToursCoords",
        indices = @Index(value = "tourId", name = "idx"),
        foreignKeys = @ForeignKey(entity = Tour.class,
        parentColumns = "id",
        childColumns = "tourId",
        onDelete = CASCADE))
public class TourCoords implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int tourId;
    private double latitude;
    private double longitude;
    private long timestamp;
    private String date;

    public TourCoords(int tourId, double latitude, double longitude, long timestamp, String date) {
        this.tourId = tourId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.date = date;
    }

    protected TourCoords(Parcel in) {
        id = in.readInt();
        tourId = in.readInt();
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTourId() {
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
        dest.writeInt(id);
        dest.writeInt(tourId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(timestamp);
        dest.writeString(date);
    }
}