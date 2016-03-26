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
import com.jmgarzo.bbdd.BBDDPeajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Peaje;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmPeaje extends Activity {

    private AdView adView;

    private Spinner cmbCochesPeaje;
    private EditText txtFecha, txtComentarios;
    private AutoCompleteTextView txtNombreAuto, txtPrecioAuto, txtCarreteraAuto, txtDireccionAuto, txtUbicacionAuto;
    private TextView lbUnidadMonedaPeaje;

    private BBDDPeajes bbddPeajes = new BBDDPeajes(this);
    private BBDDAjustesAplicacion bbddAjustesAplicacion;
    private Peaje peaje;
    private boolean esNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_peaje);

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

        txtFecha = (EditText) findViewById(R.id.txtFechaPeaje);
//        txtNombre = (EditText) findViewById(R.id.txtNombrePeaje);
        txtNombreAuto = (AutoCompleteTextView) findViewById(R.id.txtNombrePeajeAuto);
//        txtPrecio= (EditText) findViewById(R.id.txtPrecioPeaje);
        txtPrecioAuto = (AutoCompleteTextView) findViewById(R.id.txtPrecioPeajeAuto);

//        txtCarretera = (EditText) findViewById(R.id.txtCarreteraPeaje);
        txtCarreteraAuto = (AutoCompleteTextView) findViewById(R.id.txtCarreteraPeajeAuto);
        txtDireccionAuto = (AutoCompleteTextView) findViewById(R.id.txtDireccionPeajeAuto);
        txtUbicacionAuto = (AutoCompleteTextView) findViewById(R.id.txtUbicacionPeajeAuto);
        txtComentarios = (EditText) findViewById(R.id.txtComentariosPeaje);
        lbUnidadMonedaPeaje = (TextView) findViewById(R.id.lbUnidadMonedaPeaje);
        lbUnidadMonedaPeaje.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));


        cmbCochesPeaje = (Spinner) findViewById(R.id.cmbCochesPeaje);

        String idPeaje = getIntent().getStringExtra("idPeaje");

        peaje = bbddPeajes.getPeaje(idPeaje);
        if (null != peaje) {
            esNuevo = false;
            peaje.setIdPeaje(Integer.parseInt(idPeaje));
            rellenarFormulario(peaje);
        } else {
            peaje = new Peaje();
            esNuevo = true;
            cargarDatosBasicos();
        }

        txtFecha.setFocusable(false);
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFecha);
            }
        });

        cmbCochesPeaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Coche coche = (Coche) cmbCochesPeaje.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*************************AUTOCOMPLETE NOMBRE**********************************/
        ArrayList<String> nombrePeajes = bbddPeajes.getNombrePeajes();
        ArrayAdapter<String> adapterNombres =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombrePeajes);
        txtNombreAuto.setAdapter(adapterNombres);
        txtNombreAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        /*************************AUTOCOMPLETE PRECIO**********************************/

        ArrayList<String> precioPeajes = bbddPeajes.getPrecioPeajes();
        ArrayAdapter<String> adapterPrecio =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, precioPeajes);
        txtPrecioAuto.setAdapter(adapterPrecio);
        txtPrecioAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        /*************************AUTOCOMPLETE CARRETERA**********************************/
        ArrayList<String> listaCarreteras = bbddPeajes.getCarreteras();
        ArrayAdapter<String> adapterCarreteras =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaCarreteras);
        txtCarreteraAuto.setAdapter(adapterCarreteras);
        txtCarreteraAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        /*************************AUTOCOMPLETE DIRECCION**********************************/
        ArrayList<String> listaDirecciones = bbddPeajes.getDirecciones();
        ArrayAdapter<String> adapterDirecciones =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaDirecciones);
        txtDireccionAuto.setAdapter(adapterDirecciones);
        txtDireccionAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        /*************************AUTOCOMPLETE UBICACION**********************************/
        ArrayList<String> listaUbicaciones = bbddPeajes.getUbicaciones();
        ArrayAdapter<String> adapterUbicaciones =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaUbicaciones);
        txtUbicacionAuto.setAdapter(adapterUbicaciones);
        txtUbicacionAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


    public void showDatePickerDialog(EditText v) {
        DatePickerFragment newFragment = new DatePickerFragment(this, v);
        newFragment.show(getFragmentManager(), "datePicker");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_peaje, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Peaje newPeaje = new Peaje();
                    newPeaje = mappingPeaje(newPeaje);
                    errores = validar(newPeaje);
                    if (errores.isEmpty()) {
                        bbddPeajes.nuevoPeaje(newPeaje);
                        Intent intent = new Intent(FrmPeaje.this, MainActivity.class);
                        intent.putExtra("Peaje", "peaje");
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
                    errores = validar(peaje);
                    if (errores.isEmpty()) {
                        bbddPeajes.actualizarPeaje(mappingPeaje(peaje));
                        Intent intent = new Intent(FrmPeaje.this, MainActivity.class);
                        intent.putExtra("Peaje", "peaje");
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
                    DialogoDeletePeaje dialogoDelete = new DialogoDeletePeaje();
                    Bundle b = new Bundle();
                    b.putString("idPeaje", peaje.getIdPeaje().toString());
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


    private Peaje mappingPeaje(Peaje peaje) {
        Coche coche = (Coche) cmbCochesPeaje.getSelectedItem();
        peaje.setIdCoche(coche.getIdCoche());

        String sfecha = txtFecha.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        peaje.setFecha((cal.getTimeInMillis()));


//        if (null != txtPrecio.getText().toString() && !"".equals(txtPrecio.getText().toString())) {
//            peaje.setPrecio(BigDecimal.valueOf(Double.valueOf(txtPrecio.getText().toString())));
//        } else {
//            peaje.setPrecio(BigDecimal.ZERO);
//        }
        if (null != txtPrecioAuto.getText().toString() && !"".equals(txtPrecioAuto.getText().toString())) {
            peaje.setPrecio(BigDecimal.valueOf(Double.valueOf(txtPrecioAuto.getText().toString())));
        } else {
            peaje.setPrecio(BigDecimal.ZERO);
        }

        if (null != txtNombreAuto && !"".equals(txtNombreAuto.getText().toString())) {
            peaje.setNombre(txtNombreAuto.getText().toString());
        }

        if (null != txtCarreteraAuto && !"".equals(txtCarreteraAuto.getText().toString())) {
            peaje.setCarretera(txtCarreteraAuto.getText().toString());
        }

        if (null != txtDireccionAuto && !"".equals(txtDireccionAuto.getText().toString())) {
            peaje.setDireccion(txtDireccionAuto.getText().toString());
        }

        if (null != txtUbicacionAuto && !"".equals(txtUbicacionAuto.getText().toString())) {
            peaje.setUbicacion(txtUbicacionAuto.getText().toString());
        }

        if (null != txtComentarios && !"".equals(txtComentarios.getText().toString())) {
            peaje.setComentarios(txtComentarios.getText().toString());
        }

        return peaje;
    }

    private void cargarDatosBasicos() {

        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesPeaje.setAdapter(adaptadorCoches);


        /************* FECHA Peaje ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFecha.setText(formatteDate);

    }

    private void rellenarFormulario(Peaje peaje) {


        /************* COCHE ********************/

        BBDDCoches bbddCoches = new BBDDCoches(this);

        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCochesPeaje.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == peaje.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }

        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCochesPeaje.setSelection(posicion);
        }

        /************* FECHA PEAJE ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date peajeDate = new Date(peaje.getFecha());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(peajeDate);

        txtFecha.setText(formatteDate);

        txtNombreAuto.setText(peaje.getNombre());
        txtPrecioAuto.setText(peaje.getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        txtCarreteraAuto.setText(peaje.getCarretera());
        txtDireccionAuto.setText(peaje.getDireccion());
        txtUbicacionAuto.setText(peaje.getUbicacion());
        txtComentarios.setText(peaje.getComentarios());

    }

    private ArrayList<String> validar(Peaje peaje) {
        //TODO realizar validaciones

        ArrayList<String> errores = new ArrayList<String>();

        if (null != peaje) {
            if (null == peaje.getIdCoche() || "".equals(peaje.getIdCoche())) {
                errores.add(getString(R.string.error_coche_peaje));
            }
            if (null == peaje.getFecha() || "".equals(peaje.getFecha())) {
                errores.add(getString(R.string.error_fecha_peaje));
            }
        }

        return errores;


    }
}
