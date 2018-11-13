package com.example.patry.sosapp.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.patry.sosapp.Activities.MapsActivity;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.R;

public class ExportDeleteTourDialog extends AppCompatDialogFragment {

    private Tour tour;
    private ExportDeleteTourDialog.ExportDeleteTourDialogListener exportDeleteTourDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            exportDeleteTourDialogListener = (ExportDeleteTourDialog.ExportDeleteTourDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+
                    "must implement ExportDeleteTourDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.export_delete_dialog,null);

        builder.setView(view)
                .setTitle(tour.getName())
                .setNeutralButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("Export", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportDeleteTourDialogListener.ExportTour(tour);
                    }
                }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exportDeleteTourDialogListener.DeleteTour(tour);

                }});


        return  builder.create();
    }

    public interface ExportDeleteTourDialogListener{
        void ExportTour(Tour tour);
        void DeleteTour(Tour tour);
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

}
