package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.objects.Operacion;

import java.util.ArrayList;


public class FrmOperacion extends Activity {

    private EditText txtNombreOperacion, txtDescripcionOperacion;
    //    private Spinner cmbPeriodicidadOperacion;
    private boolean esNuevo;
    private BBDDOperaciones bbddOperaciones;

    private Operacion operacion;

    private int idMantenimiento;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_operacion);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        String idOperacion="";

        Bundle b = getIntent().getExtras();
        if (null != b.getString("idOperacion")) {
            idOperacion = b.getString("idOperacion");
        }

        idMantenimiento = b.getInt("idMantenimiento");
        //        String idOperacion = getIntent().getStringExtra("idOperacion");
//        idMantenimiento = getIntent().getStringExtra("idMantenimiento");

        txtNombreOperacion = (EditText) findViewById(R.id.txtNombreOperacion);
        txtDescripcionOperacion = (EditText) findViewById(R.id.txtDescripcionOperacion);

        bbddOperaciones = new BBDDOperaciones(this);
        esNuevo = false;

        operacion = bbddOperaciones.getOperacion(idOperacion);
        if (null != operacion) {
            esNuevo = false;
            operacion.setIdOperacion(Integer.parseInt(idOperacion));
            rellenarFormulario(operacion);
        } else {
            operacion = new Operacion();
            esNuevo = true;

        }


//        txtPeriodicidadDigito = (EditText) findViewById(R.id.txtPeriodicidadDigito);
//        txtCoste = (EditText) findViewById(R.id.txtCosteOperacion);
//
//        cmbPeriodicidadOperacion = (Spinner) findViewById(R.id.cmbPeriodicidadUnidadOperacion);
//
//        ArrayList<String> listTipoPeriodicidad = new ArrayList<String>();
//        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_km));
//        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_meses));
//        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_ano));
//        ArrayAdapter<String> adaptadorPeriodicidad = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listTipoPeriodicidad);
//        cmbPeriodicidadOperacion.setAdapter(adaptadorPeriodicidad);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_operacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Operacion newOperacion = new Operacion();
                    newOperacion = mappingOperacion(newOperacion);
                    errores = validar(newOperacion);
                    if (errores.isEmpty()) {
                        bbddOperaciones.nuevaOperacion(newOperacion);
                        Intent intent = new Intent(FrmOperacion.this, LstOperaciones.class);
                        intent.putExtra("idMantenimiento",idMantenimiento);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        Bundle b = new Bundle();
                        b.putStringArrayList("errores", errores);
                        DialogoAlertaErrores dialogo = new DialogoAlertaErrores();
                        dialogo.setArguments(b);
                        dialogo.show(fragmentManager, "tagAlerta");
                    }
                } else {
                    errores = validar(operacion);
                    if (errores.isEmpty()) {
                        bbddOperaciones.actualizarOperacion(mappingOperacion(operacion));
                        Intent intent = new Intent(FrmOperacion.this, LstOperaciones.class);
                        intent.putExtra("idMantenimiento",idMantenimiento);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        Bundle b = new Bundle();
                        b.putStringArrayList("errores", errores);
                        DialogoAlertaErrores dialogo = new DialogoAlertaErrores();
                        dialogo.setArguments(b);
                        dialogo.show(fragmentManager, "tagAlerta");
                    }
                }
                return true;
            case R.id.action_delete:
                if (esNuevo) {
                    onBackPressed();
                } else {

                    //TODO mostrar dialogo
                    DialogoBorrar dialogoBorrar = new DialogoBorrar();
                    Bundle b = new Bundle();
                    b.putInt("idMantenimiento", idMantenimiento);
                    b.putInt("idOperacion",operacion.getIdOperacion());
                    dialogoBorrar.setArguments(b);
                    dialogoBorrar.show(getFragmentManager(), "Delete");



//                    bbddRepostajes.eliminarRepostaje(repostaje.getIdRepostaje());
//                    Repostaje repos = bbddRepostajes.getRepostajeAnteriorPorCoche(repostaje);
//                    if (null != repos) {
//                        BBDDCoches bbddCoches = new BBDDCoches(this);
//                        Coche coche = bbddCoches.getCoche(repostaje.getIdCoche().toString());
//                        coche.setKmActuales(repos.getKmRepostaje());
//                        bbddCoches.actualizarCoche(coche);
//                    }
                    //super.onBackPressed();
//
//                    Intent intent2 = new Intent(FrmRepostaje.this, MainActivity.class);
//                    intent2.putExtra("Repostaje", "repostaje");
//                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent2);
                }

                return true;

            case android.R.id.home:
                DialogoSalir dialogoSalirSinGuardar = new DialogoSalir();
                Bundle b = new Bundle();
                b.putInt("idMantenimiento", idMantenimiento);
                dialogoSalirSinGuardar.setArguments(b);
                dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void rellenarFormulario(Operacion operacion) {

        txtNombreOperacion.setText(operacion.getNombre());
        txtDescripcionOperacion.setText((operacion.getDescripcion()));

    }


    private Operacion mappingOperacion(Operacion operacion) {

        if (null != txtNombreOperacion && !"".equals(txtNombreOperacion.getText().toString())) {
            operacion.setNombre(txtNombreOperacion.getText().toString());
        }

        if (null != txtDescripcionOperacion && !"".equals(txtDescripcionOperacion.getText().toString())) {
            operacion.setDescripcion(txtDescripcionOperacion.getText().toString());
        }

        return operacion;

    }

    private ArrayList<String> validar(Operacion operacion) {
        //TODO realizar validaciones

        ArrayList<String> errores = new ArrayList<String>();

        if (null != operacion) {
            if (null == operacion.getNombre() || "".equals(operacion.getNombre())) {
                errores.add(getString(R.string.error_nombre_operacion));
            }


        }
        return errores;


    }

    @Override
    public void onBackPressed() {
        DialogoSalir dialogoSalirSinGuardar = new DialogoSalir();
        Bundle b = new Bundle();
        b.putInt("idMantenimiento", idMantenimiento);
        dialogoSalirSinGuardar.setArguments(b);
        dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");


    }

    public static class DialogoSalir extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Recuperamos los dos valores que indicarán que fragment debebemos cargar en el mainActivity

            builder.setMessage(R.string.dialogo_salir_sin_guardar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            int idMantenimiento = 0;
                            try {
                                Bundle b = getArguments();
                                idMantenimiento = b.getInt("idMantenimiento");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), LstOperaciones.class);
                            intent.putExtra("idMantenimiento", idMantenimiento);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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

    public static class DialogoBorrar extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Recuperamos los dos valores que indicarán que fragment debebemos cargar en el mainActivity

            builder.setMessage(R.string.dialogo_delete_operacion)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            int idMantenimiento = 0;
                            int idOperacion = 0;
                            try {
                                Bundle b = getArguments();
                                idMantenimiento = b.getInt("idMantenimiento");
                                idOperacion = b.getInt("idOperacion");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BBDDOperaciones bbddOperaciones = new BBDDOperaciones(getActivity());
                            bbddOperaciones.eliminarOperacion(idOperacion);

                            Intent intent = new Intent(getActivity(), LstOperaciones.class);
                            intent.putExtra("idMantenimiento", idMantenimiento);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
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
}
