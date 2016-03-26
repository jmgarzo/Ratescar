package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDMantenimientos;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Mantenimiento;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmMantenimiento extends Activity {

    private AdView adView;

    private BBDDMantenimientos bbddMantenimientos = new BBDDMantenimientos(this);
    private BBDDAjustesAplicacion bbddAjustesAplicacion;


    private EditText txtFechaMantenimiento, txtKmMantenimiento,
            txtCosteFinal, txtComentarios;
    private TextView lbUnidadMonedaMantenimiento;

    private Button btnOperaciones;

    //    private EditText txtCosteIva, txtCosteSinIva, txtTantoCienIva, txtDescuentoTotal;
    private Spinner cmbCoches;

    private Mantenimiento mantenimiento;
    private boolean esNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_mantenimiento);

        /**PUBLICIDAD**/

        adView = new AdView(this);
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        // Buscar LinearLayout suponiendo que se le ha asignado
        // el atributo android:id="@+id/mainLayout".
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.banner);

        // Añadirle adView.
        linearLayout.addView(adView);

        // Iniciar una solicitud genérica.
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
                .build();

        // Cargar adView con la solicitud de anuncio.
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/

        getActionBar().setDisplayHomeAsUpEnabled(true);
        esNuevo = false;

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);

        cmbCoches = (Spinner) findViewById(R.id.CmbCochesMantenimiento);
//        txtTipoMantenimiento = (EditText) findViewById(R.id.txtTipoMantenimiento);
//        txtNombreMantenimiento = (EditText) findViewById(R.id.txtNombreMantenimiento);
        txtFechaMantenimiento = (EditText) findViewById(R.id.txtFechaMantenimiento);
        txtKmMantenimiento = (EditText) findViewById(R.id.txtKmMantenimiento);
//        txtCosteIva = (EditText) findViewById(R.id.txtCosteIva);
//        txtCosteSinIva = (EditText) findViewById(R.id.txtCosteSinIva);
//        txtTantoCienIva = (EditText) findViewById(R.id.txtTantoCienIva);
//        txtDescuentoTotal = (EditText) findViewById(R.id.txtDescuentoTotal);
        txtCosteFinal = (EditText) findViewById(R.id.txtCosteFinal);
        txtComentarios = (EditText) findViewById(R.id.txtComentarios);
        btnOperaciones = (Button) findViewById(R.id.btnOperaciones);
        lbUnidadMonedaMantenimiento = (TextView) findViewById(R.id.lbUnidadMonedaMantenimiento);
        lbUnidadMonedaMantenimiento.setText(bbddAjustesAplicacion.getValorMoneda());



        int idMantenimiento = getIntent().getIntExtra("idMantenimiento",-1);

        mantenimiento = bbddMantenimientos.getMantenimiento(idMantenimiento);
        if (null != mantenimiento) {
            esNuevo = false;
            rellenarFormulario(mantenimiento);
        } else {
            mantenimiento = new Mantenimiento();
            esNuevo = true;
            cargarDatosBasicos();
        }

        txtFechaMantenimiento.setFocusable(false);
        txtFechaMantenimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaMantenimiento);
            }
        });


        cmbCoches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(esNuevo) {
                    Coche coche = (Coche) cmbCoches.getSelectedItem();
                    txtKmMantenimiento.setText(getDistanciaSegunConfiguracion(coche.getKmActuales()).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btnOperaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> errores = new ArrayList<String>();
                Long resultado = new Long(-1);

                if (esNuevo) {

                    Mantenimiento newMantenimiento = new Mantenimiento();
                    newMantenimiento = mappingMantenimiento(newMantenimiento);
                    errores = validar(newMantenimiento);
                    if (errores.isEmpty()) {
                        resultado= bbddMantenimientos.nuevoMantenimiento(newMantenimiento);
                        Intent intent = new Intent(FrmMantenimiento.this, LstMantenimientoOperaciones.class);
                        intent.putExtra("idMantenimiento", resultado.intValue());
                        intent.putExtra("Origen","FrmMantenimiento");
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

                    errores = validar(mappingMantenimiento(mantenimiento));
                    if (errores.isEmpty()) {
                        bbddMantenimientos.actualizarMantenimiento(mappingMantenimiento(mantenimiento));
                        Intent intent = new Intent(FrmMantenimiento.this, LstMantenimientoOperaciones.class);
                        intent.putExtra("Origen", "FrmMantenimiento");
                        intent.putExtra("idMantenimiento",mantenimiento.getIdMantenimiento());
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



//                Intent intent = new Intent(FrmMantenimiento.this, LstMantenimientoOperaciones.class);
//                intent.putExtra("Origen","FrmMantenimiento");

//                if(resultado != -1 ) {
//                    if(null != bbddMantenimientos.getUltimoMantenimiento()) {
//                        intent.putExtra("idMantenimiento", bbddMantenimientos.getUltimoMantenimiento().getIdMantenimiento());
//                    }
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });


//        /*************************AUTOCOMPLETE TIPO MANTENIMIENTO**********************************/
//        ArrayList<String> descripcionMantenimientos = bbddMantenimientos.getDescripcionesMantenimientos();
//        ArrayAdapter<String> adapterDescripciones =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, descripcionMantenimientos);
//        txtTipoMantenimientoAuto.setAdapter(adapterDescripciones);
//        txtTipoMantenimientoAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (v.getWindowVisibility() != View.VISIBLE) {
//                    return;
//                }
//                if (hasFocus) {
//                    ((AutoCompleteTextView) v).showDropDown();
//                } else {
//                    ((AutoCompleteTextView) v).dismissDropDown();
//                }
//
//            }
//        });



    }

    public void showDatePickerDialog(EditText v) {
        DatePickerFragment newFragment = new DatePickerFragment(this, v);
        newFragment.show(getFragmentManager(), "datePicker");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frm_mantenimiento, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Mantenimiento newMantenimiento = new Mantenimiento();
                    newMantenimiento = mappingMantenimiento(newMantenimiento);
                    errores = validar(newMantenimiento);
                    if (errores.isEmpty()) {
                        bbddMantenimientos.nuevoMantenimiento(newMantenimiento);
                        Intent intent = new Intent(FrmMantenimiento.this, MainActivity.class);
                        intent.putExtra("Mantenimiento", "mantenimiento");
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

                    errores = validar(mappingMantenimiento(mantenimiento));
                    if (errores.isEmpty()) {
                        bbddMantenimientos.actualizarMantenimiento(mappingMantenimiento(mantenimiento));
                        Intent intent = new Intent(FrmMantenimiento.this, MainActivity.class);
                        intent.putExtra("Mantenimiento", "mantenimiento");
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
                Log.i("FrmDetalleCoche", "Pulsado action_delete");
                if (esNuevo) {
                    onBackPressed();
                } else {


                    DialogoBorrar dialogoBorrar = new DialogoBorrar();
                    Bundle b = new Bundle();
                    b.putInt("idMantenimiento", mantenimiento.getIdMantenimiento());
                    dialogoBorrar.setArguments(b);
                    dialogoBorrar.show(getFragmentManager(), "Delete");

//                    bbddMantenimientos.eliminarMantenimiento(mantenimiento.getIdMantenimiento());
//                    BBDDRepostajes bbddRepostajes = new BBDDRepostajes(this);
//                    BBDDCoches bbddCoches = new BBDDCoches(this);
//                    BigDecimal kmUltimoRespostaje = bbddRepostajes.getKmUltimoRespostajePorCoche(mantenimiento.getIdCoche());
//
//                    Coche coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());
//                    if (coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
//                        //podemos borrar el mantenimiento, no hay que hacer nada
//                        bbddMantenimientos.eliminarMantenimiento(mantenimiento.getIdMantenimiento());
//                    } else if (coche.getKmActuales() == mantenimiento.getKmMatenimiento() || kmUltimoRespostaje.compareTo(coche.getKmActuales()) < 0) {
//                        //Los km del coche se deben al ultimo mantenimiento, se debe poner
//                        //los km mayores del último respostaje o del último mantenimiento
//                        bbddMantenimientos.eliminarMantenimiento(mantenimiento.getIdMantenimiento());
//                        BigDecimal kmUltimoMantenimiento = bbddMantenimientos.getKmUltimoMatenimientoPorCoche(mantenimiento.getIdCoche());
//
//                        if (kmUltimoMantenimiento.compareTo(kmUltimoRespostaje) > 0) {
//                            coche.setKmActuales(kmUltimoMantenimiento);
//                            bbddCoches.actualizarCoche(coche);
//                        } else {
//                            coche.setKmActuales(kmUltimoRespostaje);
//                            bbddCoches.actualizarCoche(coche);
//                        }
//                    } else {
//                        //Si no cuadra en ninguna opcion borramos
//                        bbddMantenimientos.eliminarMantenimiento(mantenimiento.getIdMantenimiento());
//                    }
//                    super.onBackPressed();
//
//                    Intent intent2 = new Intent(FrmMantenimiento.this, MainActivity.class);
//                    intent2.putExtra("Mantenimiento", "mantenimiento");
//                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent2);
                }

                return true;

            case android.R.id.home:
                Intent intent = new Intent(FrmMantenimiento.this, MainActivity.class);
                intent.putExtra("Mantenimiento", "mantenimiento");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private Mantenimiento mappingMantenimiento(Mantenimiento mantenimiento) {

        if (null != mantenimiento.getIdMantenimiento()) {
            mantenimiento.setIdMantenimiento(mantenimiento.getIdMantenimiento());
        }
        /**** ID COCHE ****/
        Coche coche = (Coche) cmbCoches.getSelectedItem();
        mantenimiento.setIdCoche(coche.getIdCoche());

        String sfecha = txtFechaMantenimiento.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mantenimiento.setFechaMantenimiento((cal.getTimeInMillis()));

        if (null != txtKmMantenimiento && !"".equals(txtKmMantenimiento.getText().toString())) {
            mantenimiento.setKmMatenimiento(getDistanciaEnKm((new BigDecimal(txtKmMantenimiento.getText().toString()))));
        } else {
            mantenimiento.setKmMatenimiento(BigDecimal.ZERO);
        }


//        if (null != txtCosteIva.getText().toString() && !"".equals(txtCosteIva.getText().toString())) {
//            mantenimiento.setCosteIva(BigDecimal.valueOf(Double.valueOf(txtCosteIva.getText().toString())));
//        } else {
//            mantenimiento.setCosteIva(BigDecimal.ZERO);
//        }
//
//        if (null != txtCosteSinIva.getText().toString() && !"".equals(txtCosteSinIva.getText().toString())) {
//            mantenimiento.setCosteSinIva(BigDecimal.valueOf(Double.valueOf(txtCosteSinIva.getText().toString())));
//        } else {
//            mantenimiento.setCosteSinIva(BigDecimal.ZERO);
//        }
//
//        if (null != txtTantoCienIva.getText().toString() && !"".equals(txtTantoCienIva.getText().toString())) {
//            mantenimiento.setTantoCienIva(BigDecimal.valueOf(Double.valueOf(txtTantoCienIva.getText().toString())));
//        } else {
//            mantenimiento.setTantoCienIva(BigDecimal.ZERO);
//        }
//
//        if (null != txtDescuentoTotal.getText().toString() && !"".equals(txtDescuentoTotal.getText().toString())) {
//            mantenimiento.setDescuentoTotal(BigDecimal.valueOf(Double.valueOf(txtDescuentoTotal.getText().toString())));
//        } else {
//            mantenimiento.setDescuentoTotal(BigDecimal.ZERO);
//        }

        if (null != txtCosteFinal.getText().toString() && !"".equals(txtCosteFinal.getText().toString())) {
            mantenimiento.setCosteFinal(BigDecimal.valueOf(Double.valueOf(txtCosteFinal.getText().toString())));
        } else {
            mantenimiento.setCosteFinal(BigDecimal.ZERO);
        }

        if (null != txtComentarios) {
            mantenimiento.setComentarios(txtComentarios.getText().toString().trim());
        }

        return mantenimiento;

    }

    private void cargarDatosBasicos() {

        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = (Coche) cmbCoches.getSelectedItem();
        txtKmMantenimiento.setText(getDistanciaSegunConfiguracion(cocheSeleccionado.getKmActuales()).toString());


        /************* FECHA REPOSTAJE ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFechaMantenimiento.setText(formatteDate);

    }

    private void rellenarFormulario(Mantenimiento mantenimiento) {
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == mantenimiento.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }
        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCoches.setSelection(posicion);
        }


        /************* FECHA MANTENIMIENTO ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date mantenimientoDate = new Date(
                mantenimiento.getFechaMantenimiento());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(mantenimientoDate);

        txtFechaMantenimiento.setText(formatteDate);

        txtKmMantenimiento.setText(getDistanciaSegunConfiguracion(mantenimiento.getKmMatenimiento().setScale(2, BigDecimal.ROUND_HALF_UP)).toString());

//        txtCosteIva.setText(mantenimiento.getCosteIva().setScale(3, BigDecimal.ROUND_HALF_UP).toString());
//        txtCosteSinIva.setText(mantenimiento.getCosteSinIva().setScale(3, BigDecimal.ROUND_HALF_UP).toString());
//        txtTantoCienIva.setText(mantenimiento.getTantoCienIva().setScale(3, BigDecimal.ROUND_HALF_UP).toString());
//        txtDescuentoTotal.setText(mantenimiento.getDescuentoTotal().setScale(3, BigDecimal.ROUND_HALF_UP).toString());
        txtCosteFinal.setText(mantenimiento.getCosteFinal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        txtComentarios.setText(mantenimiento.getComentarios());


    }


    private ArrayList<String> validar(Mantenimiento mantenimiento) {
        ArrayList<String> errores = new ArrayList<String>();

        if (null != mantenimiento) {
            if (null == mantenimiento.getIdCoche() || "".equals(mantenimiento.getIdCoche())) {
                errores.add(getString(R.string.error_coche_mantenimiento));
            }
            if (null == mantenimiento.getFechaMantenimiento() || "".equals(mantenimiento.getFechaMantenimiento())) {
                errores.add(getString(R.string.error_fecha_mantenimiento));
            }
            if (null == mantenimiento.getKmMatenimiento() || "".equals(mantenimiento.getKmMatenimiento()) || BigDecimal.ZERO.compareTo(mantenimiento.getKmMatenimiento()) == 0) {
                errores.add(getString(R.string.error_km_mantenimiento));
            }

        }

        return errores;
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

                            int idMantenimiento = 0;
                            try {
                                Bundle b = getArguments();
                                idMantenimiento = b.getInt("idMantenimiento");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BBDDMantenimientos bbddMantenimientos = new BBDDMantenimientos(getActivity());
                            bbddMantenimientos.eliminarMantenimiento(idMantenimiento);

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("Mantenimiento","mantenimiento");
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

    private BigDecimal getDistanciaSegunConfiguracion(BigDecimal distanciaOriginal) {
        BigDecimal distancia = BigDecimal.ZERO;
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            distancia = ToolsConversiones.RedondearSinDecimales(distanciaOriginal);
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            distancia = ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(distanciaOriginal));
        }
        return distancia;
    }

    private BigDecimal getDistanciaEnKm(BigDecimal distanciaOriginal) {
        BigDecimal distancia = BigDecimal.ZERO;
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            distancia = ToolsConversiones.RedondearSinDecimales(distanciaOriginal);
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            distancia = ToolsConversiones.RedondearSinDecimales(ToolsConversiones.millasToKm(distanciaOriginal));
        }
        return distancia;
    }
}
