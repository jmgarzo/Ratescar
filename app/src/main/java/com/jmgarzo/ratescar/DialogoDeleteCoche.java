package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Repostaje;

/**
 * Created by jmgarzo on 13/01/15.
 */
public class DialogoDeleteCoche extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialogo_delete_coche)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BBDDCoches bbddCoche = new BBDDCoches(getActivity());
                        bbddCoche.eliminarCoche(Integer.parseInt(getArguments().getString("idCoche")));


                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Coche", "coche");
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
