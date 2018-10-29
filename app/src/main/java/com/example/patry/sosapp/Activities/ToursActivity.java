package com.example.patry.sosapp.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.ViewModels.TourViewModel;
import com.example.patry.sosapp.R;
import com.example.patry.sosapp.TourAdapter;

import java.util.List;

public class ToursActivity extends AppCompatActivity {

    private TourViewModel tourViewModel;

    private final TourAdapter adapter = new TourAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        tourViewModel = ViewModelProviders.of(this).get(TourViewModel.class);
        tourViewModel.getAllTours().observe(this, new Observer<List<Tour>>() {
            @Override
            public void onChanged(@Nullable List<Tour> tours) {

                adapter.setTours(tours);

            }
        });
    }

}
