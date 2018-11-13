package com.example.patry.sosapp;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.patry.sosapp.Activities.MapsActivity;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GPXExporter {

    private static final String TAG = "MEDIA";
    private final  String FOLDER_NAME = "/SOS_tracker";
    private final  String BEGINING_OF_XML_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private final String BEGINING_GPX = "<gpx version=\"1.1\" creator=\"SOS Tracker\">";
    private final String BEGINING_TRK = "<trk>";
    private final String BEGINING_TRKSEG = "<trkseg>";
    private final String BEGINING_TRKPT = "<trkpt "; //without > cause added later
    private final String BEGINING_ELE = "<ele>";
    private final String BEGINING_TIME = "<time>";

    private  final String NAME = "<name>SOS tracker</name>";


    private final String ENDING_TRKPT = "</trkpt>";
    private final String ENDING_ELE = "</ele>";
    private final String ENDING_TIME = "</time>";
    private final String ENDING_TRKSEG = "</trkseg>";
    private final String ENDING_TRK = "</trk>";
    private final String ENDING_GPX = "</gpx>";


    private PrintWriter printWriter;

    private Tour tour;
    private List<TourCoords> currentTourCoordsList;

    private static final SimpleDateFormat gpxDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GPXExporter(Tour tour, List<TourCoords> currentTourCoordsList) {
        this.tour = tour;
        this.currentTourCoordsList = currentTourCoordsList;
    }

    public String writeToSDFile(){

        File root = Environment.getExternalStorageDirectory();

        File dir = new File (root.getAbsolutePath() + FOLDER_NAME);
        dir.mkdirs();
        File file = new File(dir,  tour.getName()+tour.getTimeStamp()+".gpx");

        try {
            FileOutputStream f = new FileOutputStream(file);
            printWriter = new PrintWriter(f);
            fillPrintWriter(printWriter);


            printWriter.flush();
            printWriter.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FOLDER_NAME;
    }

    public void fillPrintWriter(PrintWriter printWriter){

        printWriter.println(BEGINING_OF_XML_FILE);
        printWriter.println(BEGINING_GPX);
        printWriter.println(BEGINING_TRK);
            printWriter.println(NAME);
            printWriter.println(BEGINING_TRKSEG);

            for(int i =0 ; i<currentTourCoordsList.size(); i++){
                printWriter.println(BEGINING_TRKPT + "lon=\"" +
                        currentTourCoordsList.get(i).getLongitude() +
                        "\" lat=\"" +
                        currentTourCoordsList.get(i).getLatitude() + "\">");
                    printWriter.println(BEGINING_TIME);
                        printWriter.println(convertDataForGPX( currentTourCoordsList.get(i).getTimestamp()));
                    printWriter.println(ENDING_TIME);
                printWriter.println(ENDING_TRKPT);
            }
        printWriter.println(ENDING_TRKSEG);
        printWriter.println(ENDING_TRK);
        printWriter.println(ENDING_GPX);
    }

    public String convertDataForGPX(long timeStamp){
        Date date = new Date(timeStamp);
        String gpxDate = gpxDateFormat.format(date);
        return gpxDate;
    }

}
