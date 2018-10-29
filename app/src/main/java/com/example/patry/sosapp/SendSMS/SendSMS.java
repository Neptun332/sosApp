package com.example.patry.sosapp.SendSMS;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendSMS {

    private Handler handler = new Handler();
    private int sendSMSEveryInSeconds = 30;
    private Context context;

    public SendSMS(Context context) {
        this.context = context;
        handler.post(periodicUpdate);
    }

    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            // scheduled another events to be in 10 seconds later
            handler.postDelayed(periodicUpdate, sendSMSEveryInSeconds * 1000);
            // below is whatever you want to do
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        if( preferences.getBoolean("EnableSMSSend",false)==true && preferences.getString("phoneNumber","")!= "") {
//            Location userLocation = locationUpdate.getUserLocation();
//
//            SmsManager.getDefault().sendTextMessage(
//                    "+48"+preferences.getString("phoneNumber",""),
//                    null,
//                    "Latitude: "+userLocation.getLatitude() + " Longitude: " + userLocation.getLongitude() + " Time: " + ConvertTimeToSimpleDateFormat(userLocation.getTime()),
//                    null,
//                    null);
//        }


        }
    };

    public String ConvertTimeToSimpleDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        return sdf.format(date);
    }

}
