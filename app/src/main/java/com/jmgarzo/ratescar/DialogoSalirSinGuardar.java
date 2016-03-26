package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.jmgarzo.bbdd.BBDDCoches;

/**
 * Created by jmgarzo on 18/03/15.
 */
public class DialogoSalirSinGuardar extends DialogFragment {
    private String keyFragment = "";
    private String valueFragment = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Recuperamos los dos valores que indicarán que fragment debebemos cargar en el mainActivity
        keyFragment = getArguments().getString("keyFragment");
        valueFragment = getArguments().getString("valueFragment");


        builder.setMessage(R.string.dialogo_salir_sin_guardar)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra(keyFragment, valueFragment);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


}
