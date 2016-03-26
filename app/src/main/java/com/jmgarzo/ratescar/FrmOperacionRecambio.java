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
import android.widget.EditText;
import android.widget.TextView;

import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.bbdd.BBDDOperacionesRecambios;
import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.objects.OperacionRecambio;
import com.jmgarzo.objects.Recambio;

import org.w3c.dom.Text;

import java.math.BigDecimal;


public class FrmOperacionRecambio extends Activity {

    private EditText txtCantidad, txtPrecio,txtCoste;

    private TextView txtNombre,txtFabricante,txtReferencia,txtDescripcion,txtComentarios;

    private Integer idRecambio, idOperacion;

    private Recambio recambio;

    private BBDDRecambios bbddRecambios;
    private BBDDOperacionesRecambios bbddOperacionesRecambios;

    private OperacionRecambio operacionRecambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_operacion_recambio);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        txtCantidad = (EditText) findViewById(R.id.txtCantidadOperacionRecambio);
        txtPrecio = (EditText) findViewById(R.id.txtPrecioOperacionRecambio);
        txtCoste = (EditText) findViewById(R.id.txtCosteOperacionRecambio);
        txtNombre = (TextView) findViewById(R.id.txtNombreOperacionRecambio);
        txtFabricante= (TextView) findViewById(R.id.txtFabricanteOperacionRecambio);
        txtReferencia = (TextView) findViewById(R.id.txtReferenciaOperacionRecambio);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcionDetalleOperacionRecambio);
        txtComentarios = (TextView) findViewById(R.id.txtComentariosOperacionRecambio);

        bbddRecambios = new BBDDRecambios(this);
        bbddOperacionesRecambios = new BBDDOperacionesRecambios(this);


        idRecambio = getIntent().getIntExtra("idRecambio",-1);
        idOperacion = getIntent().getIntExtra("idOperacion",-1);

        final Recambio recambio = bbddRecambios.getRecambio(idRecambio.toString());
        operacionRecambio = bbddOperacionesRecambios.getOperacionRecambiosById(idOperacion.toString(), idRecambio.toString());


        txtNombre.setText(recambio.getNombre());
        txtFabricante.setText(recambio.getFabricante());
        txtReferencia.setText(recambio.getReferencia());
        txtDescripcion.setText(recambio.getDescripcion());
        txtComentarios.setText(recambio.getComentarios());
        txtCantidad.setText(operacionRecambio.getCantidad().setScale(1, BigDecimal.ROUND_UP).toString());
        txtPrecio.setText(operacionRecambio.getPrecioUnidad().setScale(2,BigDecimal.ROUND_UP).toString());
        txtCoste.setText(operacionRecambio.getCoste().setScale(2,BigDecimal.ROUND_UP).toString());




        txtCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!txtCantidad.getText().toString().isEmpty()) {
                    if (!txtPrecio.getText().toString().isEmpty()) {
                        BigDecimal dPrecio, dCantidad, resultado;
                        dPrecio = BigDecimal.valueOf(Double.valueOf(txtPrecio.getText().toString()));
                        dCantidad = BigDecimal.valueOf(Double.valueOf(txtCantidad.getText().toString()));
                        resultado = dPrecio.multiply(dCantidad);

                        txtCoste.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }

            }
        });

        txtPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!txtPrecio.getText().toString().isEmpty()) {
                    if (!txtCantidad.getText().toString().isEmpty()) {
                        BigDecimal dPrecio, dCantidad, resultado;
                        dPrecio = BigDecimal.valueOf(Double.valueOf(txtPrecio.getText().toString()));
                        dCantidad = BigDecimal.valueOf(Double.valueOf(txtCantidad.getText().toString()));
                        resultado = dPrecio.multiply(dCantidad);

                        txtCoste.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_operacion_recambio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:


                bbddOperacionesRecambios.actualizarOperacionRecambio(mappingOperacionRecambio(operacionRecambio));
                Intent intent = new Intent(this, LstOperacionesRecambios.class);
                intent.putExtra("Origen", "FrmOperacionRecambio");
                intent.putExtra("idOperacion", operacionRecambio.getIdOperacion());
                intent.putExtra("idRecambio", operacionRecambio.getIdRecambio());
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
                b.putString("Origen", "FrmOperacionRecambio");
                b.putString("idOperacion",operacionRecambio.getIdOperacion().toString());
                b.putString("idRecambio",operacionRecambio.getIdRecambio().toString());
                dialogoBorrar.setArguments(b);
                dialogoBorrar.show(getFragmentManager(), "Borrar");

                return true;
            case android.R.id.home:
                onBackPressed();
//                DialogoSalir dialogoSalirSinGuardar = new DialogoSalir();
//                Bundle b2 = new Bundle();
//                b2.putString("Origen", "FrmMantenimientoOperacion");
//                b2.putString("idMantenimiento", mantenimientoOperacion.getIdMantenimiento().toString());
//                b2.putString("idOperacion", mantenimientoOperacion.getIdOperacion().toString());
//                dialogoSalirSinGuardar.setArguments(b2);
//                dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private OperacionRecambio mappingOperacionRecambio(OperacionRecambio operacionRecambio){
        if(null!= txtCantidad && !"".equals(txtCantidad.getText().toString())){
            operacionRecambio.setCantidad(BigDecimal.valueOf(Double.valueOf(txtCantidad.getText().toString())));
        }

        if(null!= txtPrecio && !"".equals(txtPrecio.getText().toString())){
            operacionRecambio.setPrecioUnidad(BigDecimal.valueOf(Double.valueOf(txtPrecio.getText().toString())));
        }

        if(null!= txtCoste && !"".equals(txtCoste.getText().toString())){
            operacionRecambio.setCoste(BigDecimal.valueOf(Double.valueOf(txtCoste.getText().toString())));
        }

        return operacionRecambio;

    }

    public static class DialogoBorrar extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Recuperamos los dos valores que indicarán que fragment debebemos cargar en el mainActivity

            builder.setMessage(R.string.dialogo_delete_operacion_recambio)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            String idOperacion="";
                            String idRecambio="";
                            try {
                                Bundle b = getArguments();
                                idOperacion = b.getString("idOperacion");
                                idRecambio= b.getString("idRecambio");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BBDDOperacionesRecambios bbddOperacionesRecambios = new BBDDOperacionesRecambios(getActivity());
                            bbddOperacionesRecambios.eliminar(idOperacion,idRecambio);

                            Intent intent = new Intent(getActivity(), LstOperacionesRecambios.class);
                            intent.putExtra("Origen","FrmOperacionRecambio");
                            intent.putExtra("idOperacion",Integer.valueOf(idOperacion));
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
