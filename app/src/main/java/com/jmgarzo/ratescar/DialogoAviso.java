package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 19/02/15.
 */
public class DialogoAviso extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String titulo = getArguments().getString("titulo");
        String error = getArguments().getString("error");

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage(error)
                .setTitle(titulo)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
