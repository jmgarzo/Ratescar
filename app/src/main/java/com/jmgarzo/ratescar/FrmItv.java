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
import com.jmgarzo.bbdd.BBDDItvs;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Itv;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmItv extends Activity {

    private AdView adView;

    private Spinner cmbCochesItv;
    private EditText txtFechaItv, txtPrecioItv, txtObservaciones, txtKmItv;
    private AutoCompleteTextView txtEstacionItvAuto, txtResultadoItvAuto;
    private TextView lbUnidadDistanciaItv, lbUnidadMonedaItv;

    private BBDDItvs bbddItvs = new BBDDItvs(this);
    private Itv itv;
    private boolean esNuevo;
    private BBDDAjustesAplicacion bbddAjustesAplicacion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_itv);

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

        txtFechaItv = (EditText) findViewById(R.id.txtFechaItv);
        txtPrecioItv = (EditText) findViewById(R.id.txtPrecioItv);
        txtEstacionItvAuto = (AutoCompleteTextView) findViewById(R.id.txtEstacionItvAuto);
//        txtResultado = (EditText) findViewById(R.id.txtResultadoItv);
        txtResultadoItvAuto = (AutoCompleteTextView) findViewById(R.id.txtResultadoItvAuto);
        txtObservaciones = (EditText) findViewById(R.id.txtObservacionesItv);
        txtKmItv = (EditText) findViewById(R.id.txtKmItv);

        lbUnidadDistanciaItv = (TextView) findViewById(R.id.lbUnidadDistanciaItv);
        lbUnidadMonedaItv = (TextView) findViewById(R.id.lbUnidadMonedaItv);
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            lbUnidadDistanciaItv.setText(getString(R.string.unidad_distancia_km));
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            lbUnidadDistanciaItv.setText(getString(R.string.unidad_distancia_millas));
        }

        lbUnidadMonedaItv.setText((" ".concat(bbddAjustesAplicacion.getValorMoneda())));

        cmbCochesItv = (Spinner) findViewById(R.id.cmbCochesItv);


        String idItv = getIntent().getStringExtra("idItv");

        itv = bbddItvs.getItv(idItv);
        if (null != itv) {
            esNuevo = false;
            itv.setIdItv(Integer.parseInt(idItv));
            rellenarFormulario(itv);
        } else {
            itv = new Itv();
            esNuevo = true;
            cargarDatosBasicos();
        }

        txtFechaItv.setFocusable(false);
        txtFechaItv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaItv);
            }
        });


        cmbCochesItv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Coche coche = (Coche) cmbCochesItv.getSelectedItem();
                txtKmItv.setText(mostrarDistanciaSegunAjustes(coche.getKmActuales()).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*************************AUTOCOMPLETE ESTACION ITV **********************************/
        ArrayList<String> estaciones = bbddItvs.getNombreEstaciones();
        ArrayAdapter<String> adapterEstaciones =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, estaciones);
        txtEstacionItvAuto.setAdapter(adapterEstaciones);
        txtEstacionItvAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        /*************************AUTOCOMPLETE RESULTADO ITV **********************************/
        ArrayList<String> resultados = bbddItvs.getResultados();
        boolean hayFavorable = false;
        boolean hayDesfavorable = false;
        boolean hayNegativa = false;
        for (String resultado : resultados) {
            if (resultado.equalsIgnoreCase(getString(R.string.resultado_itv_favorable))) {
                hayFavorable = true;
            } else if (resultado.equalsIgnoreCase(getString(R.string.resultado_itv_desfavorable))) {
                hayDesfavorable = true;
            } else if (resultado.equalsIgnoreCase(getString(R.string.resultado_itv_negativa))) {
                hayNegativa = true;
            }
        }

        if (!hayFavorable) {
            resultados.add("Favorable");
        }
        if (!hayDesfavorable) {
            resultados.add("Desfavorable");
        }
        if (!hayNegativa) {
            resultados.add("Negativa");
        }
        ArrayAdapter<String> adapterResultado =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultados);
        txtResultadoItvAuto.setAdapter(adapterResultado);
        txtResultadoItvAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        getMenuInflater().inflate(R.menu.frm_itv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Itv newItv = new Itv();
                    newItv = mappingItv(newItv);
                    errores = validar(newItv);
                    if (errores.isEmpty()) {
                        bbddItvs.nuevaItv(newItv);
                        Intent intent = new Intent(FrmItv.this, MainActivity.class);
                        intent.putExtra("Itv", "itv");
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
                    errores = validar(itv);
                    if (errores.isEmpty()) {
                        bbddItvs.actualizarItv(mappingItv(itv));
                        Intent intent = new Intent(FrmItv.this, MainActivity.class);
                        intent.putExtra("Itv", "itv");
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
                    DialogoDeleteItv dialogoDelete = new DialogoDeleteItv();
                    Bundle b = new Bundle();
                    b.putString("idItv", itv.getIdItv().toString());
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

    private Itv mappingItv(Itv itv) {


        /**** ID COCHE ****/
        Coche coche = (Coche) cmbCochesItv.getSelectedItem();
        itv.setIdCoche(coche.getIdCoche());

        String sfecha = txtFechaItv.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        itv.setFechaItv((cal.getTimeInMillis()));

        if (null != txtPrecioItv.getText().toString() && !"".equals(txtPrecioItv.getText().toString())) {
            itv.setPrecio(BigDecimal.valueOf(Double.valueOf(txtPrecioItv.getText().toString())));
        } else {
            itv.setPrecio(BigDecimal.ZERO);
        }

        if (null != txtEstacionItvAuto && !"".equals(txtEstacionItvAuto.getText().toString())) {
            itv.setEstacion(txtEstacionItvAuto.getText().toString());
        }

        if (null != txtResultadoItvAuto && !"".equals(txtResultadoItvAuto.getText().toString())) {
            itv.setResultado(txtResultadoItvAuto.getText().toString());
        }

        if (null != txtObservaciones && !"".equals(txtObservaciones.getText().toString())) {
            itv.setObservaciones(txtObservaciones.getText().toString());
        }

        if (null != txtKmItv.getText().toString() && !"".equals(txtKmItv.getText().toString())) {
            itv.setKmItv(ajustarDistanciaParaBBDD(new BigDecimal(txtKmItv.getText().toString())));
        } else {
            itv.setPrecio(BigDecimal.ZERO);
        }

        return itv;

    }


    private void cargarDatosBasicos() {


        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesItv.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = (Coche) cmbCochesItv.getSelectedItem();
        txtKmItv.setText(mostrarDistanciaSegunAjustes(cocheSeleccionado.getKmActuales()).toString());

        /************* FECHA ITV ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFechaItv.setText(formatteDate);


    }


    private void rellenarFormulario(Itv itv) {


        /************* COCHE ********************/

        BBDDCoches bbddCoches = new BBDDCoches(this);

        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesItv.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == itv.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }

        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCochesItv.setSelection(posicion);
        }


        /************* FECHA ITV ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date itvDate = new Date(itv.getFechaItv());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(itvDate);

        txtFechaItv.setText(formatteDate);

        txtPrecioItv.setText(itv.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        txtEstacionItvAuto.setText(itv.getEstacion());
        txtResultadoItvAuto.setText((itv.getResultado()));
        txtObservaciones.setText(itv.getObservaciones());

        txtKmItv.setText(mostrarDistanciaSegunAjustes(itv.getKmItv()).toString());


    }


    private ArrayList<String> validar(Itv itv) {
        //TODO realizar validaciones

        ArrayList<String> errores = new ArrayList<String>();

        if (null != itv) {
            if (null == itv.getIdCoche() || "".equals(itv.getIdCoche())) {
                errores.add(getString(R.string.error_coche_itv));
            }
            if (null == itv.getFechaItv() || "".equals(itv.getFechaItv())) {
                errores.add(getString(R.string.error_fecha_itv));
            }

        }
        return errores;


    }

    private BigDecimal mostrarDistanciaSegunAjustes(BigDecimal kmOriginales) {
        BigDecimal kmAjustados = BigDecimal.ZERO;
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            kmAjustados = ToolsConversiones.kmToMillas(kmOriginales).setScale(0, BigDecimal.ROUND_HALF_UP);
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            kmAjustados = kmOriginales.setScale(0, BigDecimal.ROUND_HALF_UP);
        }
        return kmAjustados;
    }

    private BigDecimal ajustarDistanciaParaBBDD(BigDecimal distanciaOriginal) {
        BigDecimal distancia = distanciaOriginal;
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            distancia = ToolsConversiones.millasToKm(distanciaOriginal);
        }
        return distancia;
    }


}
