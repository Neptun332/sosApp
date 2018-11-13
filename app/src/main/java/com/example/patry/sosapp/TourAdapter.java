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
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public TourHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tour_item,viewGroup,false);

        return new TourHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TourHolder tourHolder, int i) {
        Tour currentTour = tours.get(i);
        tourHolder.textViewName.setText(currentTour.getName());
        tourHolder.textViewDate.setText(currentTour.getDate());
        tourHolder.textViewId.setText(i+ "");

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

        public TourHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            textViewName = itemView.findViewById(R.id.tour_name);
            textViewDate = itemView.findViewById(R.id.tour_date);
            textViewId = itemView.findViewById(R.id.tour_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public  void filterList(ArrayList<Tour> filtredList){
        tours = filtredList;
        notifyDataSetChanged();
    }

}
