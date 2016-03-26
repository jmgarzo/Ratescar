package com.jmgarzo.ratescar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
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
public class DialogoDeleteRepostaje extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialogo_delete_repostaje)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(getActivity());
                        Repostaje repostajeABorrar = bbddRepostajes.getRepostaje(getArguments().getString("idRepostaje").toString());
                        bbddRepostajes.eliminarRepostaje(Integer.parseInt(getArguments().getString("idRepostaje").toString()));
                        Repostaje repos = bbddRepostajes.getRepostajeAnteriorPorCoche(repostajeABorrar);
                        if (null != repos) {
                            BBDDCoches bbddCoches = new BBDDCoches(getActivity());
                            Coche coche = bbddCoches.getCoche(repostajeABorrar.getIdCoche().toString());
                            coche.setKmActuales(repos.getKmRepostaje());
                            bbddCoches.actualizarCoche(coche);
                        }
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Repostaje", "repostaje");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent2 = new Intent(getActivity(), MainActivity.class);
                        intent2.putExtra("Repostaje", "repostaje");
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
