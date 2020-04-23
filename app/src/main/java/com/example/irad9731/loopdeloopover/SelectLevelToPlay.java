package com.example.irad9731.loopdeloopover;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SelectLevelToPlay extends DialogFragment {//a fragment that is a popup where the user can choose the level he wants to play in
    public PlayHandleButtons handler;
    public SelectLevelToPlay(){

    }

    @Override
    public void onAttach(Context context) {
        this.handler=(PlayHandleButtons) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.select_level_layout,null);
        ((Button)(view.findViewById(R.id.select5x5BT))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.playPress5X5();
                dismiss();
            }
        });
        ((Button)(view.findViewById(R.id.select7x7BT))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.playPress7X7();
                dismiss();
            }
        });
        ((Button)(view.findViewById(R.id.select9x9BT))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.playPress9X9();
                dismiss();
            }
        });
        ((Button)(view.findViewById(R.id.cancelLevelSelectBT))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectLevelToPlay.this.getDialog().cancel();
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view).setTitle("select level");

        return builder.create();
    }


    public interface PlayHandleButtons{
        void playPress5X5();
        void playPress7X7();
        void playPress9X9();
    }


}
