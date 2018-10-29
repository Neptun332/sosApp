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

import com.example.patry.sosapp.R;

public class TourNameDialog extends AppCompatDialogFragment {

    private EditText tourName;
    private TourNameDialogListener tourNameDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            tourNameDialogListener = (TourNameDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+
            "must implement TourNameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Tour name")
                .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tour = tourName.getText().toString();
                        tourNameDialogListener.SaveTourName(tour);
                    }
                });

        tourName = view.findViewById(R.id.tourName);

        return  builder.create();
    }

    public interface TourNameDialogListener{
        void SaveTourName(String tourName);
    }

}
