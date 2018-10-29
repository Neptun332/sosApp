package com.example.patry.sosapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.patry.sosapp.DataBase.Entities.Tour;

import java.util.ArrayList;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourHolder> {

    private List<Tour> tours = new ArrayList<>();

    @NonNull
    @Override
    public TourHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tour_item,viewGroup,false);

        return new TourHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TourHolder tourHolder, int i) {
        Tour currentTour = tours.get(i);
        tourHolder.textViewName.setText(currentTour.getName());
        tourHolder.textViewDate.setText(currentTour.getDate());
        tourHolder.textViewId.setText(currentTour.getId()+ "");

    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public void setTours(List<Tour> tours){
        this.tours = tours;
        notifyDataSetChanged();
    }

    class TourHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewDate;
        private TextView textViewId;

        public TourHolder(View itemView){
            super(itemView);
            textViewName = itemView.findViewById(R.id.tour_name);
            textViewDate = itemView.findViewById(R.id.tour_date);
            textViewId = itemView.findViewById(R.id.tour_id);
        }
    }



}
