package com.example.patry.sosapp.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;
import com.example.patry.sosapp.DataBase.ViewModels.TourCoordsViewModel;
import com.example.patry.sosapp.DataBase.ViewModels.TourViewModel;
import com.example.patry.sosapp.Dialogs.ExportDeleteTourDialog;
import com.example.patry.sosapp.Dialogs.TourNameDialog;
import com.example.patry.sosapp.GPXExporter;
import com.example.patry.sosapp.R;
import com.example.patry.sosapp.TourAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ToursActivity extends AppCompatActivity implements ExportDeleteTourDialog.ExportDeleteTourDialogListener {

    private TourViewModel tourViewModel;
    private TourCoordsViewModel tourCoordsViewModel;

    private final TourAdapter adapter = new TourAdapter();

    private List<Tour> allTours;
    private List<TourCoords> currentTourCoordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        tourViewModel = ViewModelProviders.of(this).get(TourViewModel.class);
        tourCoordsViewModel = ViewModelProviders.of(this).get(TourCoordsViewModel.class);
        tourViewModel.getAllTours().observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(@Nullable List<Tour> tours) {
                allTours = tours;
                adapter.setTours(tours);

            }
        });

        EditText editText = findViewById(R.id.search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        adapter.setOnItemClickListener(new TourAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Tour tour = allTours.get(position);
                openDialog(tour);
            }
        });
    }

    private void filter(String text){
        ArrayList<Tour> filteredList = new ArrayList<>();

        for (Tour item:allTours){
            if(item.getDate().toLowerCase().contains(text)){
                filteredList.add(item);
            }
            if(item.getName().toLowerCase().contains(text)){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);

    }

    private void openDialog(Tour tour){
        ExportDeleteTourDialog exportDeleteTourDialog = new ExportDeleteTourDialog();
        exportDeleteTourDialog.show(getSupportFragmentManager(),"Export delete tour dialog");
        exportDeleteTourDialog.setTour(tour);
    }

    @Override
    public void ExportTour(Tour tour) {
        if(tour.isSaved()) {
            currentTourCoordList = tourCoordsViewModel.getAllTourCoordsByTourId(tour.getId());
            GPXExporter gpxExporter = new GPXExporter(tour, currentTourCoordList);
            String a = gpxExporter.writeToSDFile();
            Toast.makeText(this, "file saved in " + a, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Tour still in progress", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void DeleteTour(Tour tour ) {
        if( tour.isSaved()) {
            tourViewModel.delete(tour);
        }
        else{
            Toast.makeText(this, "Tour still in progress", Toast.LENGTH_LONG).show();
        }
    }

}
