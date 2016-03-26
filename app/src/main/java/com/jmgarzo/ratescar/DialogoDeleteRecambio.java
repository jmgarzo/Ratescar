package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.bbdd.BBDDRepostajes;

/**
 * Created by jmgarzo on 13/01/15.
 */
public class DialogoDeleteRecambio extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialogo_delete_recambio)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        BBDDRecambios bbddRecambios = new BBDDRecambios(getActivity());

                        bbddRecambios.eliminar(getArguments().getString("idRecambio"));


                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Recambio", "recambio");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Recambio", "recambio");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
