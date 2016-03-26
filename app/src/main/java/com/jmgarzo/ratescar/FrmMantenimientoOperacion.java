package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jmgarzo.bbdd.BBDDMantenimientoOperacion;
import com.jmgarzo.bbdd.BBDDMantenimientos;
import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.objects.MantenimientoOperacion;
import com.jmgarzo.objects.Operacion;

import java.math.BigDecimal;
import java.util.ArrayList;


public class FrmMantenimientoOperacion extends Activity {

    private TextView textViewNombreDetalleMantenimientoOperacion, txtDescripcion, txtComentarios;
    private EditText txtPeriodicidadDigito, txtCoste;
    private Spinner cmbPeriodicidadUnidad;
    private Button btnRecambios;

    private BBDDMantenimientos bbddMantenimientos;
    private BBDDMantenimientoOperacion bbddMantenimientoOperacion;
    private BBDDOperaciones bbddOperaciones;

    private MantenimientoOperacion mantenimientoOperacion;
    //private Mantenimiento mantenimiento;
    private Operacion operacion;

    private boolean esNuevo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_mantenimiento_operacion);

        getActionBar().setDisplayHomeAsUpEnabled(true);

//        bbddMantenimientos = new BBDDMantenimientos(this);
        bbddMantenimientoOperacion = new BBDDMantenimientoOperacion(this);
        bbddOperaciones = new BBDDOperaciones(this);


        String origen = getIntent().getStringExtra("Origen");

        Integer idMantenimiento = getIntent().getIntExtra("idMantenimiento", -1);
        Integer idOperacion = getIntent().getIntExtra("idOperacion", -1);


        mantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperacion(idMantenimiento.toString(), idOperacion.toString());

//        if (null != mantenimientoOperacion) {
//            esNuevo = false;
//            rellenarFormulario(mantenimiento);
//        } else {
//            mantenimiento = new Mantenimiento();
//            esNuevo = true;
//            cargarDatosBasicos();
//        }

//        mantenimiento = bbddMantenimientos.getMantenimiento(idMantenimiento.toString());
        operacion = bbddOperaciones.getOperacion(idOperacion.toString());

        textViewNombreDetalleMantenimientoOperacion = (TextView) findViewById(R.id.txtNombreDetalleMantenimientoOperacion);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcionDetalleMantenimientoOperacion);
        txtPeriodicidadDigito = (EditText) findViewById(R.id.txtPeriodicidadDigitoDetalleMantenimientoOperacion);
        txtCoste = (EditText) findViewById(R.id.txtCosteDetalleMantenimientOperacion);
        txtComentarios = (TextView) findViewById(R.id.txtComentariosDetalleMantenimientoOperacion);
        cmbPeriodicidadUnidad = (Spinner) findViewById(R.id.cmbPeriodicidadUnidadDetalleMantenimientoOperacion);
        btnRecambios = (Button) findViewById(R.id.btnRecambios);

        ArrayList<String> listTipoPeriodicidad = new ArrayList<String>();
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_km));
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_millas));
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_meses));
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_operacion_ano));
        ArrayAdapter<String> adaptadorPeriodicidad = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTipoPeriodicidad);
        cmbPeriodicidadUnidad.setAdapter(adaptadorPeriodicidad);

        String periodicidadSeleccionada = "";
        for (String tipoPeriodicidad : listTipoPeriodicidad) {
            if (tipoPeriodicidad.equals(mantenimientoOperacion.getPeriodicidadUnidad())) {
                periodicidadSeleccionada = mantenimientoOperacion.getPeriodicidadUnidad();
            }
        }
        if (periodicidadSeleccionada != "") {
            cmbPeriodicidadUnidad.setSelection(adaptadorPeriodicidad.getPosition(periodicidadSeleccionada));
        }

        textViewNombreDetalleMantenimientoOperacion.setText(operacion.getNombre());
        txtDescripcion.setText(operacion.getDescripcion());
        txtPeriodicidadDigito.setText(mantenimientoOperacion.getPeriodicidadDigito().toString());
        txtCoste.setText(mantenimientoOperacion.getCoste().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        txtComentarios.setText(mantenimientoOperacion.getComentarios());


        btnRecambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<String> errores = new ArrayList<String>();
//                Long resultado = new Long(-1);


//                errores = validar(mappingMantenimiento(mantenimiento));
//                if (errores.isEmpty()) {
                bbddMantenimientoOperacion.actualizarMantenimientoOperacion(mantenimientoOperacion);
                Intent intent = new Intent(FrmMantenimientoOperacion.this, LstOperacionesRecambios.class);
                intent.putExtra("Origen", "FrmMantenimientoOperacion");
                intent.putExtra("idOperacion", mantenimientoOperacion.getIdOperacion());
                // intent.putExtra("idMantenimiento", mantenimiento.getIdMantenimiento());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                } else {
//                    FragmentManager fragmentManager = getFragmentManager();
//                    Bundle b = new Bundle();
//                    b.putStringArrayList("errores", errores);
//                    DialogoAlertaErrores dialogo = new DialogoAlertaErrores();
//                    dialogo.setArguments(b);
//                    dialogo.show(fragmentManager, "tagAlerta");
//                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_mantenimiento_operacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:


                bbddMantenimientoOperacion.actualizarMantenimientoOperacion(mappingMantenimientoOperacion(mantenimientoOperacion));
                Intent intent = new Intent(FrmMantenimientoOperacion.this, LstMantenimientoOperaciones.class);
                intent.putExtra("Origen", "FrmMantenimientoOperacion");
                intent.putExtra("idMantenimiento", mantenimientoOperacion.getIdMantenimiento());
                intent.putExtra("idOperacion", mantenimientoOperacion.getIdOperacion());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//                    }

                return true;


//            case R.id.action_delete:
//                if (esNuevo) {
//                    onBackPressed();
//                } else {
//                    //TODO mostrar dialogo
////                    DialogoDeleteItv dialogoDelete = new DialogoDeleteItv();
////                    Bundle b = new Bundle();
////                    b.putString("idItv", itv.getIdItv().toString());
////                    dialogoDelete.setArguments(b);
////                    dialogoDelete.show(getFragmentManager(), "delete");
//                    onBackPressed();
//
//
////                    bbddRepostajes.eliminarRepostaje(repostaje.getIdRepostaje());
////                    Repostaje repos = bbddRepostajes.getRepostajeAnteriorPorCoche(repostaje);
////                    if (null != repos) {
////                        BBDDCoches bbddCoches = new BBDDCoches(this);
////                        Coche coche = bbddCoches.getCoche(repostaje.getIdCoche().toString());
////                        coche.setKmActuales(repos.getKmRepostaje());
////                        bbddCoches.actualizarCoche(coche);
////                    }
//                    //super.onBackPressed();
////
////                    Intent intent2 = new Intent(FrmRepostaje.this, MainActivity.class);
////                    intent2.putExtra("Repostaje", "repostaje");
////                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                    startActivity(intent2);
//                }
//
//                return true;

            case R.id.action_delete:
                DialogoBorrar dialogoBorrar = new DialogoBorrar();
                Bundle b = new Bundle();
                b.putString("Origen", "FrmMantenimientoOperacion");
                b.putString("idMantenimiento", mantenimientoOperacion.getIdMantenimiento().toString());
                b.putString("idOperacion", mantenimientoOperacion.getIdOperacion().toString());
                dialogoBorrar.setArguments(b);
                dialogoBorrar.show(getFragmentManager(), "Borrar");

                return true;
            case android.R.id.home:
                DialogoSalir dialogoSalirSinGuardar = new DialogoSalir();
                Bundle b2 = new Bundle();
                b2.putString("Origen", "FrmMantenimientoOperacion");
                b2.putString("idMantenimiento", mantenimientoOperacion.getIdMantenimiento().toString());
                b2.putString("idOperacion", mantenimientoOperacion.getIdOperacion().toString());
                dialogoSalirSinGuardar.setArguments(b2);
                dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private MantenimientoOperacion mappingMantenimientoOperacion(MantenimientoOperacion mantenimientoOperacion) {


        if (null != txtPeriodicidadDigito && !"".equals(txtPeriodicidadDigito.getText().toString())) {
            mantenimientoOperacion.setPeriodicidadDigito(Integer.valueOf(txtPeriodicidadDigito.getText().toString()));
        }
        mantenimientoOperacion.setPeriodicidadUnidad(cmbPeriodicidadUnidad.getSelectedItem().toString());

        if (null != txtCoste && !"".equals(txtCoste.getText().toString())) {
            mantenimientoOperacion.setCoste(BigDecimal.valueOf(Double.valueOf(txtCoste.getText().toString())));
        }
        if (null != txtComentarios && !"".equals(txtComentarios.getText().toString())) {
            mantenimientoOperacion.setComentarios(txtComentarios.getText().toString());
        }

        return mantenimientoOperacion;

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
                            Bundle b = getArguments();
                            String idMantenimiento = b.getString("idMantenimiento");
                            String idOperacion = b.getString("idOperacion");

                            Intent intent = new Intent(getActivity(), LstMantenimientoOperaciones.class);
                            intent.putExtra("Origen", "FrmMantenimientoOperacion");
                            intent.putExtra("idMantenimiento", Integer.valueOf(idMantenimiento));
                            intent.putExtra("idOperacion", idOperacion);
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

            builder.setMessage(R.string.dialogo_delete_mantenimiento)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle b = getArguments();
                            String idMantenimiento = b.getString("idMantenimiento");
                            String idOperacion = b.getString("idOperacion");
                            BBDDMantenimientoOperacion bbddMantenimientoOperacion = new BBDDMantenimientoOperacion(getActivity());

                            bbddMantenimientoOperacion.eliminar(idMantenimiento, idOperacion);
                            Intent intent = new Intent(getActivity(), LstMantenimientoOperaciones.class);
                            intent.putExtra("Origen", "FrmMantenimientoOperacion");
                            intent.putExtra("idMantenimiento", Integer.valueOf(idMantenimiento));
                            //intent.putExtra("idOperacion",idOperacion);
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
