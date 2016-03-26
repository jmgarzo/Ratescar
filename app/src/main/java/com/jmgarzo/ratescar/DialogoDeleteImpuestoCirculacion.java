package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.jmgarzo.bbdd.BBDDImpuestosCirculacion;

/**
 * Created by jmgarzo on 11/03/15.
 */
public class DialogoDeleteImpuestoCirculacion extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialogo_delete_impuesto_circulacion)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BBDDImpuestosCirculacion bbddImpuesto = new BBDDImpuestosCirculacion(getActivity());
                        bbddImpuesto.eliminar(getArguments().getString("idImpuestoCirculacion"));

                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("ImpuestoCirculacion", "impuestoCirculacion");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("ImpuestoCirculacion", "impuestoCirculacion");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
