package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDSeguros;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Seguro;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmSeguro extends Activity {

    private AdView adView;

    private Spinner cmbCochesSeguro, cmbPeriodicidadUnidad;
    private EditText txtPrimaSeguro, txtFechaInicio, txtFechaVencimiento, txtPeriodicidadDigito, txtObservacionesSeguro;
    private Spinner cmbTipoSeguro;
    private AutoCompleteTextView txtCompaniaSeguroAuto, txtPolizaSeguroAuto;
    private ArrayList<String> listaTipoSeguros;
    private TextView lbUnidadMonedaSeguro;

    private BBDDSeguros bbddSeguros = new BBDDSeguros(this);
    private BBDDAjustesAplicacion bbddAjustesAplicacion;
    private Seguro seguro;
    private boolean esNuevo;

    private ArrayList<String> listTipoPeriodicidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_seguro);

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

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);

        esNuevo = false;

        cmbCochesSeguro = (Spinner) findViewById(R.id.cmbCochesSeguros);
        cmbPeriodicidadUnidad = (Spinner) findViewById(R.id.cmbPeriodicidadUnidad);
        txtPrimaSeguro = (EditText) findViewById(R.id.txtPrimaSeguro);
        txtCompaniaSeguroAuto = (AutoCompleteTextView) findViewById(R.id.txtCompaniaSeguroAuto);
        //txtTipoSeguro = (EditText) findViewById(R.id.txtTipoSeguro);
//        txtPolizaSeguro = (EditText) findViewById(R.id.txtPolizaSeguro);
        txtPolizaSeguroAuto = (AutoCompleteTextView) findViewById(R.id.txtPolizaSeguroAuto);
        txtFechaInicio = (EditText) findViewById(R.id.txtFechaInicioSeguro);
        txtFechaVencimiento = (EditText) findViewById(R.id.txtFechaVencimientoSeguro);
        txtPeriodicidadDigito = (EditText) findViewById(R.id.txtPeriodicidadDigito);
        txtObservacionesSeguro = (EditText) findViewById(R.id.txtObservacionesSeguro);
        cmbTipoSeguro = (Spinner) findViewById(R.id.cmbTipoSeguro);
        lbUnidadMonedaSeguro = (TextView) findViewById(R.id.lbUnidadMonedaSeguro);
        lbUnidadMonedaSeguro.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));

        String idSeguro = getIntent().getStringExtra("idSeguro");


        listTipoPeriodicidad = new ArrayList<String>();
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_seguro_dia));
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_seguro_mes));
        listTipoPeriodicidad.add(getString(R.string.tipo_periodicidad_seguro_ano));
        ArrayAdapter<String> adaptadorPeriodicidad = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTipoPeriodicidad);
        cmbPeriodicidadUnidad.setAdapter(adaptadorPeriodicidad);

        listaTipoSeguros = new ArrayList<String>();
        listaTipoSeguros.add(getString(R.string.tipo_seguro_a_terceros));
        listaTipoSeguros.add(getString(R.string.tipo_seguro_a_terceros_ampliado));
        listaTipoSeguros.add(getString(R.string.tipo_seguro_a_todo_riesgo));
        listaTipoSeguros.add(getString(R.string.tipo_seguro_a_todo_riesgo_franquicia));
        listaTipoSeguros.add(getString(R.string.tipo_seguro_complementario));

        ArrayAdapter<String> adaptadorTipoSeguro = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaTipoSeguros);
        cmbTipoSeguro.setAdapter(adaptadorTipoSeguro);

        seguro = bbddSeguros.getSeguro(idSeguro);

        if (null != seguro) {
            esNuevo = false;
            seguro.setIdSeguro(Integer.parseInt(idSeguro));
            rellenarFormulario(seguro);
        } else {
            seguro = new Seguro();
            esNuevo = true;
            cargarDatosBasicos();
        }

        txtFechaInicio.setFocusable(false);
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaInicio);
            }
        });

        txtFechaVencimiento.setFocusable(false);
        txtFechaVencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaVencimiento);
            }
        });


        cmbCochesSeguro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Coche coche = (Coche) cmbCochesSeguro.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        cmbTipoSeguro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Coche coche = (Coche) cmbCochesSeguro.getSelectedItem();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        ArrayList<String> listaPolizas = bbddSeguros.getPolizas();
        ArrayAdapter<String> adapterpolizas =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaPolizas);
        txtPolizaSeguroAuto.setAdapter(adapterpolizas);
        txtPolizaSeguroAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getWindowVisibility() != View.VISIBLE) {
                    return;
                }
                if (hasFocus) {
                    ((AutoCompleteTextView) v).showDropDown();
                } else {
                    ((AutoCompleteTextView) v).dismissDropDown();
                }

            }
        });

        ArrayList<String> listaCompanias = bbddSeguros.getCompanias();
        ArrayAdapter<String> adapterCompanias =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCompanias);
        txtCompaniaSeguroAuto.setAdapter(adapterCompanias);
        txtCompaniaSeguroAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getWindowVisibility() != View.VISIBLE) {
                    return;
                }
                if (hasFocus) {
                    ((AutoCompleteTextView) v).showDropDown();
                } else {
                    ((AutoCompleteTextView) v).dismissDropDown();
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_seguro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Seguro newSeguro = new Seguro();
                    newSeguro = mapping(newSeguro);
                    errores = validar(newSeguro);
                    if (errores.isEmpty()) {
                        bbddSeguros.nuevoSeguro(newSeguro);
                        Intent intent = new Intent(FrmSeguro.this, MainActivity.class);
                        intent.putExtra("Seguro", "seguro");
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
                    errores = validar(seguro);
                    if (errores.isEmpty()) {
                        bbddSeguros.actualizarSeguro(mapping(seguro));
                        Intent intent = new Intent(FrmSeguro.this, MainActivity.class);
                        intent.putExtra("Seguro", "seguro");
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
                    DialogoDeleteSeguro dialogoDelete = new DialogoDeleteSeguro();
                    Bundle b = new Bundle();
                    b.putString("idSeguro", seguro.getIdSeguro().toString());
                    dialogoDelete.setArguments(b);
                    dialogoDelete.show(getFragmentManager(), "delete");

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showDatePickerDialog(EditText v) {
        DatePickerFragment newFragment = new DatePickerFragment(this, v);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    private Seguro mapping(Seguro seguro) {

        // id_seguro, id_coche, compañia, prima, tipo_de_seguro, " +
        // " numero_poliza, fecha_inicio, fecha_vencimiento, periodicidad_digito, periodicidad_unidad,observaciones

        Coche coche = (Coche) cmbCochesSeguro.getSelectedItem();
        seguro.setIdCoche(coche.getIdCoche());

        if (null != txtCompaniaSeguroAuto && !"".equals(txtCompaniaSeguroAuto.getText().toString())) {
            seguro.setCompania(txtCompaniaSeguroAuto.getText().toString());
        }


        if (null != txtPrimaSeguro.getText().toString() && !"".equals(txtPrimaSeguro.getText().toString())) {
            seguro.setPrima(BigDecimal.valueOf(Double.valueOf(txtPrimaSeguro.getText().toString())));
        } else {
            seguro.setPrima(BigDecimal.ZERO);
        }

//        if(null!=cmbCochesSeguro.getSelectedItem()){
//            seguro.setTipoSeguro(cmbCochesSeguro.getSelectedItem().toString());
//        }else{
//            seguro.setTipoSeguro("");
//        }

//        if (null != txtTipoSeguro && !"".equals(txtTipoSeguro.getText().toString())) {
//            seguro.setTipoSeguro(txtTipoSeguro.getText().toString());
//        }

        if (null != cmbTipoSeguro.getSelectedItem()) {
            seguro.setTipoSeguro(cmbTipoSeguro.getSelectedItem().toString());
        } else {
            seguro.setTipoSeguro("");
        }

        if (null != txtPolizaSeguroAuto && !"".equals(txtPolizaSeguroAuto.getText().toString())) {
            seguro.setNumeroPoliza(txtPolizaSeguroAuto.getText().toString());
        }

        String sfechaInicio = txtFechaInicio.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfechaInicio));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        seguro.setFechaInicio((cal.getTimeInMillis()));

        String sfechaVencimiento = txtFechaVencimiento.getText().toString();
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal2 = new GregorianCalendar();
        try {
            cal2.setTime(format2.parse(sfechaVencimiento));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        seguro.setFechaVencimiento((cal2.getTimeInMillis()));


        if (null != txtPeriodicidadDigito && !"".equals(txtPeriodicidadDigito.getText().toString())) {
            seguro.setPeriodicidadDigito(Integer.parseInt(txtPeriodicidadDigito.getText().toString()));
        }

        String periodicidadUnidad = cmbPeriodicidadUnidad.getSelectedItem().toString();
        seguro.setPeriodicidadUnidad(periodicidadUnidad);

        if (null != txtObservacionesSeguro && !"".equals(txtObservacionesSeguro.getText().toString())) {
            seguro.setObservaciones(txtObservacionesSeguro.getText().toString());
        }

        return seguro;

    }

    private void cargarDatosBasicos() {


        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesSeguro.setAdapter(adaptadorCoches);


        /************* FECHA Inicio y vencimiento********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFechaInicio.setText(formatteDate);

        txtFechaVencimiento.setText(formatteDate);


    }

    private void rellenarFormulario(Seguro seguro) {


        // id_seguro, id_coche, compañia, prima, tipo_de_seguro, " +
        // " numero_poliza, fecha_inicio, fecha_vencimiento, periodicidad_digito, periodicidad_unidad,observaciones
        /************* COCHE ********************/

        BBDDCoches bbddCoches = new BBDDCoches(this);

        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesSeguro.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == seguro.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }

        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCochesSeguro.setSelection(posicion);
        }

        txtCompaniaSeguroAuto.setText(seguro.getCompania());
        txtPrimaSeguro.setText(seguro.getPrima().setScale(2, BigDecimal.ROUND_HALF_UP).toString());


        for (int i = 0; i < listaTipoSeguros.size(); i++) {
            if (seguro.getTipoSeguro().equals(listaTipoSeguros.get(i).toString())) {
                cmbTipoSeguro.setSelection(i);
            }
        }
//        txtTipoSeguro.setText(seguro.getTipoSeguro());
        txtPolizaSeguroAuto.setText(seguro.getNumeroPoliza());

        /************* FECHA INICIO ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date inicioDate = new Date(seguro.getFechaInicio());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(inicioDate);
        txtFechaInicio.setText(formatteDate);

        /************* FECHA Vencimiento ********************/
        Calendar cal2 = new GregorianCalendar();
        java.util.Date VencimientoDate = new Date(seguro.getFechaVencimiento());
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate2 = df2.format(VencimientoDate);
        txtFechaVencimiento.setText(formatteDate2);

        txtPeriodicidadDigito.setText(seguro.getPeriodicidadDigito().toString());


        ArrayAdapter<String> adaptadorPeriodicidad = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listTipoPeriodicidad);
        cmbPeriodicidadUnidad.setAdapter(adaptadorPeriodicidad);

        String periodicidadSeleccionado = "";
        for (String periodicidad : listTipoPeriodicidad) {
            if (periodicidad.equals(seguro.getPeriodicidadUnidad())) {
                periodicidadSeleccionado = (seguro.getPeriodicidadUnidad());
            }
        }
        if (!periodicidadSeleccionado.equals("")) {
            cmbPeriodicidadUnidad.setSelection(adaptadorPeriodicidad
                    .getPosition(periodicidadSeleccionado));
        }


        txtObservacionesSeguro.setText(seguro.getObservaciones());


    }

    private ArrayList<String> validar(Seguro seguro) {
        //TODO realizar validaciones

        ArrayList<String> errores = new ArrayList<String>();

        if (null != seguro) {
            if (null == seguro.getIdCoche() || "".equals(seguro.getIdCoche())) {
                errores.add(getString(R.string.error_coche_seguro));
            }
            if (null == seguro.getFechaInicio() || "".equals(seguro.getFechaInicio())) {
                errores.add(getString(R.string.error_fecha_inicio_seguro));
            }
            if (null == seguro.getFechaVencimiento() || "".equals(seguro.getFechaVencimiento())) {
                errores.add(getString(R.string.error_fecha_vencimiento_seguro));
            }
        }

        return errores;


    }
}
