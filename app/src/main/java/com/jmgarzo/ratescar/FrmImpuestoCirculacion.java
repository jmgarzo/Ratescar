package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDImpuestosCirculacion;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.ImpuestoCirculacion;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmImpuestoCirculacion extends Activity {

    private AdView adView;

    private Spinner cmbCoches;
    private EditText txtImporte, txtAnualidad, txtFechaFinPago, txtComentarios;
    private TextView lbUnidadMonedaImpuesto;

    private BBDDImpuestosCirculacion bbddImpuestosCirculacion = new BBDDImpuestosCirculacion(this);
    private BBDDAjustesAplicacion bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);
    private ImpuestoCirculacion impuestoCirculacion;
    private boolean esNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_impuesto_circulacion);


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

        txtImporte = (EditText) findViewById(R.id.txtImporteImpuestoCirculacion);
        txtAnualidad = (EditText) findViewById(R.id.txtAnualidadImpuestoCirculacion);
        txtFechaFinPago = (EditText) findViewById(R.id.txtFechaFinPagoImpuestoCirculacion);
        txtComentarios = (EditText) findViewById(R.id.txtComentariosImpuestoCirculacion);
        lbUnidadMonedaImpuesto = (TextView) findViewById(R.id.lbUnidadMonedaImpuesto);
        lbUnidadMonedaImpuesto.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));

        cmbCoches = (Spinner) findViewById(R.id.cmbCochesImpuestoCirculacion);

        //TODO mirar que esto funcione
        String idImpuestoCirculacion = getIntent().getStringExtra("idImpuestoCirculacion");

        impuestoCirculacion = bbddImpuestosCirculacion.getImpuesto(idImpuestoCirculacion);
        if (null != impuestoCirculacion) {
            esNuevo = false;
            impuestoCirculacion.setIdImpuesto(Integer.parseInt(idImpuestoCirculacion));
            rellenarFormulario(impuestoCirculacion);
        } else {
            impuestoCirculacion = new ImpuestoCirculacion();
            esNuevo = true;
            cargarDatosBasicos();
        }


        txtFechaFinPago.setFocusable(false);
        txtFechaFinPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaFinPago);
            }
        });
//
//
//        cmbCoches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Coche coche = (Coche) cmbCoches.getSelectedItem();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_impuesto_circulacion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    ImpuestoCirculacion newImpuesto = new ImpuestoCirculacion();
                    newImpuesto = mappingImpuestoCirculacion(newImpuesto);
                    errores = validar(newImpuesto);
                    if (errores.isEmpty()) {
                        bbddImpuestosCirculacion.nuevoImpuesto(newImpuesto);
                        Intent intent = new Intent(FrmImpuestoCirculacion.this, MainActivity.class);
                        intent.putExtra("ImpuestoCirculacion", "impuestoCirculacion");
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
                    errores = validar(impuestoCirculacion);
                    if (errores.isEmpty()) {
                        bbddImpuestosCirculacion.actualizarImpuesto(mappingImpuestoCirculacion(impuestoCirculacion));
                        Intent intent = new Intent(FrmImpuestoCirculacion.this, MainActivity.class);
                        intent.putExtra("ImpuestoCirculacion", "impuestoCirculacion");
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
                    DialogoDeleteImpuestoCirculacion dialogoDelete = new DialogoDeleteImpuestoCirculacion();
                    Bundle b = new Bundle();
                    b.putString("idImpuestoCirculacion", impuestoCirculacion.getIdImpuesto().toString());
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


    private ImpuestoCirculacion mappingImpuestoCirculacion(ImpuestoCirculacion impuestoCirculacion) {


        /**** ID COCHE ****/
        Coche coche = (Coche) cmbCoches.getSelectedItem();
        impuestoCirculacion.setIdCoche(coche.getIdCoche());

        if (null != txtImporte.getText().toString() && !"".equals(txtImporte.getText().toString())) {
            impuestoCirculacion.setImporte(BigDecimal.valueOf(Double.valueOf(txtImporte.getText().toString())));
        } else {
            impuestoCirculacion.setImporte(BigDecimal.ZERO);
        }


        String sfecha = txtAnualidad.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        impuestoCirculacion.setAnualidad((cal.getTimeInMillis()));


        String sfechaFin = txtFechaFinPago.getText().toString();
        SimpleDateFormat formatFin = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar calFin = new GregorianCalendar();
        try {
            calFin.setTime(formatFin.parse(sfechaFin));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        impuestoCirculacion.setFechaFinPago((calFin.getTimeInMillis()));


        if (null != txtComentarios && !"".equals(txtComentarios.getText().toString())) {
            impuestoCirculacion.setComentarios(txtComentarios.getText().toString());
        }

        return impuestoCirculacion;

    }


    private void cargarDatosBasicos() {

        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);

        /************* ANUALIDAD ********************/
        Calendar calAnualidad = new GregorianCalendar();
        java.util.Date currentDate = calAnualidad.getTime();
        SimpleDateFormat dfAnualidad = new SimpleDateFormat("yyyy");
        String formatteDateAnualidad = dfAnualidad.format(currentDate);

        txtAnualidad.setText(formatteDateAnualidad);


        /************* FECHA FIN PAGO ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate2 = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate2);

        txtFechaFinPago.setText(formatteDate);


    }


    private void rellenarFormulario(ImpuestoCirculacion impuestoCirculacion) {


        /************* COCHE ********************/

        BBDDCoches bbddCoches = new BBDDCoches(this);

        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == impuestoCirculacion.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }

        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCoches.setSelection(posicion);
        }


        txtImporte.setText(impuestoCirculacion.getImporte().setScale(2, BigDecimal.ROUND_HALF_UP).toString());


        /************* ANUALIDAD ********************/
        Calendar calAnualidad = new GregorianCalendar();
        java.util.Date dateAnualidad = new Date(impuestoCirculacion.getAnualidad());
        SimpleDateFormat dfAnualidad = new SimpleDateFormat("yyyy");
        String formatteDateAnualidad = dfAnualidad.format(dateAnualidad);

        txtAnualidad.setText(formatteDateAnualidad);


        /************* FECHA FIN PAGO ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date itvDate = new Date(impuestoCirculacion.getFechaFinPago());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(itvDate);

        txtFechaFinPago.setText(formatteDate);


        txtComentarios.setText(impuestoCirculacion.getComentarios());
    }


    private ArrayList<String> validar(ImpuestoCirculacion impuestoCirculacion) {
        //TODO realizar validaciones

        ArrayList<String> errores = new ArrayList<String>();

        if (null != impuestoCirculacion) {
            if (null == impuestoCirculacion.getIdCoche() || "".equals(impuestoCirculacion.getIdCoche())) {
                errores.add(getString(R.string.error_coche_impuesto));
            }
        }
        return errores;
    }
}
