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
public class DialogoIniciarBBDD extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> errores = getArguments().getStringArrayList("errores");

        if (errores.isEmpty()) {
            errores.add(getString(R.string.no_errores));
        }


        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage(errores.get(0).toString())
                .setTitle(getString(R.string.titulo_alerta_inicio_bbdd))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
