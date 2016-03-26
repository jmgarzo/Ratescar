package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDCombustibles;
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Combustible;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.tools.ToolsConversiones;
import com.jmgarzo.tools.ToolsRespostaje;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FrmRepostaje extends Activity {

    private AdView adView;

    private BBDDRepostajes bbddRepostajes = new BBDDRepostajes(this);
    private BBDDCombustibles bbddCombustibles;
    private BBDDAjustesAplicacion bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);

    private EditText txtFechaRespostaje, txtKmRepostaje, txtPrecio, txtLitros, txtCosteRepostaje, txtVelocidadMedia, txtComentarios;
    private AutoCompleteTextView txtEstacionServicioAuto;

    private Spinner cmbCoches, cmbTipoCombustible, cmbTipoCarretera, cmbTipoPago;
    private RadioGroup rbTipoRepostaje, rbTipoConduccion;
    private RadioButton rbTipoRepostaje1, rbTipoRepostaje2, rbTipoConduccion1, rbTipoConduccion2, rbTipoConduccion3;
    private CheckBox chkEsAA, chkEsRemolque, chkEsBaca;

    private TextView lbKmRepostaje, lbPrecio, lbLitros, lbMonedaCosteRepostaje, lbMonedaPrecioRepostaje, lbUnidadLitros, lbUnidadDistanciaRepostaje, lbUnidadVelocidadRepostaje;

    private ArrayList<Combustible> listCombustibles;
    private ArrayList<String> listSubtipos;

    private Repostaje repostaje;
    private boolean esNuevo;
    private static String hora;


    private Date df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_repostaje);


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


        txtFechaRespostaje = (EditText) findViewById(R.id.txtFechaRespostaje);
        txtKmRepostaje = (EditText) findViewById(R.id.txtKmRepostaje);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);
        txtLitros = (EditText) findViewById(R.id.txtLitros);
        txtCosteRepostaje = (EditText) findViewById(R.id.txtCosteRepostaje);
        txtVelocidadMedia = (EditText) findViewById(R.id.txtVelocidadMedia);
//        txtEstacionServicio = (EditText) findViewById(R.id.txtEstacionServicio);
        txtEstacionServicioAuto = (AutoCompleteTextView) findViewById(R.id.txtEstacionServicioAuto);
        txtComentarios = (EditText) findViewById(R.id.txtComentarios);

        cmbCoches = (Spinner) findViewById(R.id.CmbCoches);
        cmbTipoCombustible = (Spinner) findViewById(R.id.cmbTipoCombustible);
        cmbTipoCarretera = (Spinner) findViewById(R.id.cmbTipoCarretera);
        cmbTipoPago = (Spinner) findViewById(R.id.cmbTipoPago);

        rbTipoRepostaje = (RadioGroup) findViewById(R.id.rbTipoRepostaje);
        rbTipoConduccion = (RadioGroup) findViewById(R.id.rbTipoConduccion);

        rbTipoRepostaje1 = (RadioButton) findViewById(R.id.rbTipoRepostaje1);
        rbTipoRepostaje2 = (RadioButton) findViewById(R.id.rbTipoRepostaje2);
        rbTipoConduccion1 = (RadioButton) findViewById(R.id.rbTipoConduccion1);
        rbTipoConduccion2 = (RadioButton) findViewById(R.id.rbTipoConduccion2);
        rbTipoConduccion3 = (RadioButton) findViewById(R.id.rbTipoConduccion3);

        chkEsAA = (CheckBox) findViewById(R.id.chkEsAA);
        chkEsRemolque = (CheckBox) findViewById(R.id.chkEsRemolque);
        chkEsBaca = (CheckBox) findViewById(R.id.chkEsBaca);

        //Ajustamos los label de la pantalla según los ajustes de la app


        String cantidadCombustibleSeleccionado;
        lbKmRepostaje = (TextView) findViewById(R.id.lbKmRepostaje);
        lbPrecio = (TextView) findViewById(R.id.lbPrecio);
        lbLitros = (TextView) findViewById(R.id.lbLitros);
        lbMonedaCosteRepostaje = (TextView) findViewById(R.id.lbMonedaCosteRepostaje);
        lbUnidadLitros = (TextView) findViewById(R.id.lbUnidadLitros);
        lbUnidadDistanciaRepostaje = (TextView) findViewById(R.id.lbUnidadDistanciaRepostaje);
        lbMonedaPrecioRepostaje = (TextView) findViewById(R.id.lbMonedaPrecioRepostaje);
        lbUnidadVelocidadRepostaje = (TextView) findViewById(R.id.lbUnidadVelocidadRepostaje);

        //Moneda

        lbMonedaCosteRepostaje.setText(bbddAjustesAplicacion.getValorMoneda());
        lbMonedaPrecioRepostaje.setText(bbddAjustesAplicacion.getValorMoneda());

        //Kilometros del repostaje
        if (bbddAjustesAplicacion.esKm()) {
            lbKmRepostaje.setText(getString(R.string.lb_km_repostaje));
            lbUnidadVelocidadRepostaje.setText(getString(R.string.velocidad_km_por_hora));
        } else if (bbddAjustesAplicacion.esMillas()) {
            lbKmRepostaje.setText(getString(R.string.lb_millas_repostaje));
            lbUnidadVelocidadRepostaje.setText(getString(R.string.velocidad_millas_por_hora));
        }

        //Unidad distancia

        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            lbUnidadDistanciaRepostaje.setText(getString(R.string.unidad_distancia_km));
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            lbUnidadDistanciaRepostaje.setText(getString(R.string.unidad_distancia_millas));
        }


        //PRECIO Y CANTIDAD DEL COMBUSTIBLE

        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            lbPrecio.setText(getString(R.string.lb_precio_por_litro));
            lbLitros.setText(getString(R.string.lb_cantidad_litros));
            lbUnidadLitros.setText(getString(R.string.lb_unidad_cantidad_litros));
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            lbPrecio.setText(getString(R.string.lb_precio_por_galon_uk));
            lbLitros.setText(getString(R.string.lb_cantidad_galones_uk));
            lbUnidadLitros.setText(getString(R.string.lb_unidad_cantidad_galones_uk));
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            lbPrecio.setText(getString(R.string.lb_precio_por_galon_us));
            lbLitros.setText(getString(R.string.lb_cantidad_galones_us));
            lbUnidadLitros.setText(getString(R.string.lb_unidad_cantidad_galones_us));
        }


        String idRepostaje = getIntent().getStringExtra("idRepostaje");

        bbddCombustibles = new BBDDCombustibles(this);

        repostaje = bbddRepostajes.getRepostaje(idRepostaje);
        if (null != repostaje) {
            esNuevo = false;
            repostaje.setIdRepostaje(Integer.parseInt(idRepostaje));
            rellenarFormulario(repostaje);
        } else {
            repostaje = new Repostaje();
            esNuevo = true;
            cargarDatosBasicos();
        }


        cmbCoches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Coche coche = (Coche) cmbCoches.getSelectedItem();


                if (esNuevo) {
                    if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                        txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(coche.getKmActuales()).toString());
                    } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                        txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(coche.getKmActuales())).toString());
                    }
                    listCombustibles = bbddCombustibles.getCombustibleByTipo(((Coche) cmbCoches.getSelectedItem()).getIdCombustible());
                    ArrayList<String> listSubtipos = new ArrayList<String>();

                    Combustible combustibleSeleccionado = null;
                    for (Combustible com : listCombustibles) {
                        listSubtipos.add(com.getSubtipo());
                        if (com.getIdCombustible() == repostaje.getIdCombustible()) {
                            combustibleSeleccionado = com;
                        }
                    }
                    ArrayAdapter<String> adaptadorCombustibles = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item, listSubtipos);
                    cmbTipoCombustible.setAdapter(adaptadorCombustibles);

                } else {
                    txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(repostaje.getKmRepostaje()).toString());

                    if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                        txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(repostaje.getKmRepostaje()).toString());
                    } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                        txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repostaje.getKmRepostaje())).toString());
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        txtFechaRespostaje.setFocusable(false);
        txtFechaRespostaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaRespostaje);
            }
        });


        txtPrecio.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!txtPrecio.getText().toString().isEmpty()) {
                    if (!txtCosteRepostaje.getText().toString().isEmpty()) {
                        BigDecimal dPrecio, dCoste, resultado;
                        dPrecio = new BigDecimal(txtPrecio.getText().toString());
                        dCoste = new BigDecimal(txtCosteRepostaje.getText().toString());
                        if (dPrecio.compareTo(BigDecimal.ZERO) != 0 && dCoste.compareTo(BigDecimal.ZERO) != 0) {
                            resultado = dCoste.divide(dPrecio, 5, BigDecimal.ROUND_HALF_UP);
                        } else {
                            resultado = BigDecimal.ZERO;
                        }
                        txtLitros.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else if (!txtLitros.getText().toString().isEmpty()) {
                        BigDecimal dPrecio, dLitros, resultado;
                        dPrecio = new BigDecimal(txtPrecio.getText().toString());
                        dLitros = new BigDecimal(txtLitros.getText().toString());
                        resultado = dLitros.multiply(dPrecio);
                        txtCosteRepostaje.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                    }
                }

            }
        });

        txtCosteRepostaje.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!txtCosteRepostaje.getText().toString().isEmpty()) {
                    if (!txtPrecio.getText().toString().isEmpty()) {
                        BigDecimal dPrecio, dCoste, resultado;
                        dPrecio = new BigDecimal(txtPrecio.getText().toString());
                        dCoste = new BigDecimal(txtCosteRepostaje.getText().toString());
                        if (dPrecio.compareTo(BigDecimal.ZERO) != 0 && dCoste.compareTo(BigDecimal.ZERO) != 0) {
                            resultado = dCoste.divide(dPrecio, 15, BigDecimal.ROUND_HALF_UP);
                        } else {
                            resultado = BigDecimal.ZERO;
                        }

                        txtLitros.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    } else if (!txtLitros.getText().toString().isEmpty()) {
                        BigDecimal dLitros, dCoste, resultado;
                        dLitros = new BigDecimal(txtLitros.getText().toString());
                        dCoste = new BigDecimal(txtCosteRepostaje.getText().toString());
                        if (dLitros.compareTo(BigDecimal.ZERO) != 0 && dCoste.compareTo(BigDecimal.ZERO) != 0) {
                            resultado = dCoste.divide(dLitros, 5, BigDecimal.ROUND_HALF_UP);
                        } else {
                            resultado = BigDecimal.ZERO;
                        }
                        txtPrecio.setText(resultado.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                    }

                }

            }
        });

        txtLitros.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!txtLitros.getText().toString().isEmpty()) {
                    if (!txtPrecio.getText().toString().isEmpty()) {
                        BigDecimal dLitros, dPrecio, resultado;
                        dLitros = new BigDecimal(txtLitros.getText().toString());
                        dPrecio = new BigDecimal(txtPrecio.getText().toString());
                        resultado = dPrecio.multiply(dLitros);
                        if (txtCosteRepostaje.getText().toString().isEmpty()) {
                            txtCosteRepostaje.setText(resultado.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                        }
                    } else if (!txtCosteRepostaje.getText().toString().trim().isEmpty()
                            && txtCosteRepostaje.getText().toString().trim().equalsIgnoreCase("0")) {
                        BigDecimal dLitros, dCoste, resultado;
                        dLitros = new BigDecimal(txtLitros.getText().toString());
                        dCoste = new BigDecimal(txtCosteRepostaje.getText().toString());
                        if (dLitros.compareTo(BigDecimal.ZERO) != 0 && dCoste.compareTo(BigDecimal.ZERO) != 0) {
                            resultado = dCoste.divide(dLitros, 5, BigDecimal.ROUND_HALF_UP);
                        } else {
                            resultado = BigDecimal.ZERO;
                        }
                        txtPrecio.setText(resultado.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                    }
                }
            }
        });

        /*************************AUTOCOMPLETE ESTACION SERVICIO **********************************/
        ArrayList<String> estaciones = bbddRepostajes.getEstaciones();
        ArrayAdapter<String> adapterEstaciones =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, estaciones);
        txtEstacionServicioAuto.setAdapter(adapterEstaciones);
        txtEstacionServicioAuto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (txtEstacionServicioAuto != null && txtEstacionServicioAuto.isPopupShowing()) {
//            txtEstacionServicioAuto.dismissDropDown();
//            txtEstacionServicioAuto.showDropDown();
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_repostaje, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<String> errores = new ArrayList<String>();
                if (esNuevo) {

                    Repostaje newRepostaje = new Repostaje();
                    newRepostaje = mappingRepostaje(newRepostaje);
                    errores = validar(newRepostaje);
                    if (errores.isEmpty()) {
                        bbddRepostajes.nuevoRepostaje(newRepostaje);
                        Intent intent = new Intent(FrmRepostaje.this, MainActivity.class);
                        intent.putExtra("Repostaje", "repostaje");
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
                    errores = validar(mappingRepostaje(repostaje));
                    if (errores.isEmpty()) {
                        bbddRepostajes.actualizarRespostaje(mappingRepostaje(repostaje));
                        Intent intent = new Intent(FrmRepostaje.this, MainActivity.class);
                        intent.putExtra("Repostaje", "repostaje");
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
                    DialogoDeleteRepostaje dialogoDelete = new DialogoDeleteRepostaje();
                    Bundle b = new Bundle();
                    b.putString("idRepostaje", repostaje.getIdRepostaje().toString());
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


    private Repostaje mappingRepostaje(Repostaje repostaje) {

        if (null != repostaje.getIdRepostaje()) {
            repostaje.setIdRepostaje(repostaje.getIdRepostaje());
        }

        /**** ID COCHE ****/
        Coche coche = (Coche) cmbCoches.getSelectedItem();
        repostaje.setIdCoche(coche.getIdCoche());
        /** ID COMBUSTIBLE **/

        String subtipo = (String) cmbTipoCombustible.getSelectedItem();
        Integer idCombustible = 0;
        for (Combustible combustible : listCombustibles) {
            if (combustible.getSubtipo().equalsIgnoreCase(subtipo)) {
                idCombustible = combustible.getIdCombustible();
            }
        }
        repostaje.setIdCombustible(idCombustible);
        if (null != txtKmRepostaje.getText().toString() && !"".equals(txtKmRepostaje.getText().toString())) {
            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                repostaje.setKmRepostaje(BigDecimal.valueOf(Double.valueOf(txtKmRepostaje.getText().toString())));
            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                BigDecimal kmRepostaje = ToolsConversiones.millasToKm(BigDecimal.valueOf(Double.valueOf(txtKmRepostaje.getText().toString())));
                repostaje.setKmRepostaje(kmRepostaje);
            }
        } else {
            repostaje.setKmRepostaje(BigDecimal.ZERO);
        }

        if (null != txtLitros.getText().toString() && !"".equals(txtLitros.getText().toString())) {
            if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                repostaje.setLitros(BigDecimal.valueOf(Double.valueOf(txtLitros.getText().toString())));
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                BigDecimal litros = ToolsConversiones.galonesUKToLitros(BigDecimal.valueOf(Double.valueOf(txtLitros.getText().toString())));
                repostaje.setLitros(litros);
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                BigDecimal litros = ToolsConversiones.galonesUSToLitros(BigDecimal.valueOf(Double.valueOf(txtLitros.getText().toString())));
                repostaje.setLitros(litros);
            }
        } else {
            repostaje.setLitros(BigDecimal.ZERO);
        }

        if (null != txtCosteRepostaje.getText().toString() && !"".equals(txtCosteRepostaje.getText().toString())) {
            repostaje.setCosteRepostaje(new BigDecimal(txtCosteRepostaje.getText().toString()));
        } else {
            repostaje.setCosteRepostaje(BigDecimal.ZERO);
        }

        if (null != txtPrecio.getText().toString() && !"".equals(txtPrecio.getText().toString())) {
            if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                repostaje.setPrecioLitro(new BigDecimal(txtPrecio.getText().toString()));
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                BigDecimal precioEnLitros = ToolsConversiones.precioPorGalonUKToPrecioPorLitro(new BigDecimal(txtPrecio.getText().toString()));
                repostaje.setPrecioLitro(precioEnLitros);
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                BigDecimal precioEnLitros = ToolsConversiones.precioPorGalonUSToPrecioPorLitro(new BigDecimal(txtPrecio.getText().toString()));
                repostaje.setPrecioLitro(precioEnLitros);
            }
        } else {
            repostaje.setPrecioLitro(BigDecimal.ZERO);
        }
        repostaje.setEsAA(chkEsAA.isChecked());
        repostaje.setEsRemolque(chkEsRemolque.isChecked());
        repostaje.setEsBaca(chkEsBaca.isChecked());

        if (cmbTipoCarretera.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_carretera_carretera))) {
            repostaje.setTipoCarretera("tipo_carretera_carretera");
        } else if (cmbTipoCarretera.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_carretera_ciudad))) {
            repostaje.setTipoCarretera("tipo_carretera_ciudad");
        } else if (cmbTipoCarretera.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_carretera_autopista))) {
            repostaje.setTipoCarretera("tipo_carretera_autopista");
        } else if (cmbTipoCarretera.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_carretera_mixto))) {
            repostaje.setTipoCarretera("tipo_carretera_mixto");
        } else {
            repostaje.setTipoCarretera("");
        }


//        repostaje.setTipoCarretera(cmbTipoCarretera.getSelectedItem().toString());
        if (cmbTipoPago.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_de_pago_efectivo))) {
            repostaje.setTipoPago("tipo_de_pago_efectivo");
        } else if (cmbTipoPago.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.tipo_de_pago_tarjeta))) {
            repostaje.setTipoPago("tipo_de_pago_tarjeta");
        } else {
            repostaje.setTipoPago("");
        }


        //repostaje.setTipoPago(cmbTipoPago.getSelectedItem().toString());
        if (null != txtVelocidadMedia.getText().toString() && !"".equals(txtVelocidadMedia.getText().toString())) {
            if (bbddAjustesAplicacion.esKm()) {
                repostaje.setVelocidadMedia(new BigDecimal(txtVelocidadMedia.getText().toString()));
            } else if (bbddAjustesAplicacion.esMillas()) ;
            {
                BigDecimal velocidadMedia = ToolsConversiones.millasToKm(new BigDecimal(txtVelocidadMedia.getText().toString()));
                repostaje.setVelocidadMedia(velocidadMedia);
            }
        } else {
            repostaje.setVelocidadMedia(BigDecimal.ZERO);
        }
        repostaje.setAreaServicio(txtEstacionServicioAuto.getText().toString());

        if (null != txtComentarios && !"".equals(txtComentarios.getText().toString())) {
            repostaje.setComentarios(txtComentarios.getText().toString());
        }

        String sfecha = txtFechaRespostaje.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(format.parse(sfecha));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // calendarFecha.getTimeInMillis();
        repostaje.setFechaRespostaje((cal.getTimeInMillis()));

        if (rbTipoConduccion1.isChecked()) {
            repostaje.setTipoConduccion("tipo_de_conduccion_normal");
        } else if (rbTipoConduccion2.isChecked()) {
            repostaje.setTipoConduccion("tipo_de_conduccion_economica");
        } else if (rbTipoConduccion3.isChecked()) {
            repostaje.setTipoConduccion("tipo_de_conduccion_deportiva");
        } else {
            repostaje.setTipoConduccion("tipo_de_conduccion_normal");
        }

        if (rbTipoRepostaje1.isChecked()) {
            repostaje.setEsCompleto(true);
        } else {
            repostaje.setEsCompleto(false);
        }


//        Repostaje repostajeAnterior = bbddRepostajes.getRepostajeAnteriorPorCoche(repostaje);
//
//            if (null != repostajeAnterior) {
//                repostaje.setKmRecorridos(BigDecimal.valueOf((repostaje.getKmRepostaje().doubleValue() - repostajeAnterior
//                        .getKmRepostaje().doubleValue())));
//
//                BigDecimal cien = BigDecimal.valueOf(100);
//                if (repostaje.getKmRecorridos().compareTo(BigDecimal.ZERO) != 0) {
//                    repostaje.setMediaConsumo((repostaje.getLitros().multiply(cien)).divide(repostaje.getKmRecorridos(), 2,
//                            BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
//                }
//            } else {
//                repostaje.setKmRecorridos(BigDecimal.ZERO);
//                repostaje.setMediaConsumo(BigDecimal.ZERO);
//            }

        ToolsRespostaje.calcularConsumoMedio(this, repostaje);

        return repostaje;
    }

    private void cargarDatosBasicos() {


        //********* CARGA del SPINNER COCHES *************//
        BBDDCoches bbddCoches = new BBDDCoches(this);
        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);
        Integer idCoche = bbddRepostajes.getUltimoCocheQueReposto();
        Integer posicion = -1;
        for (Coche coche : listaCoches) {
            posicion++;
            if (coche.getIdCoche() == idCoche) {
                break;
            }
        }
        cmbCoches.setSelection(posicion);


        /************* FECHA REPOSTAJE ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFechaRespostaje.setText(formatteDate);

        /************* KM REPOSTAJE ********************/

        Coche cocheSeleccionado = (Coche) cmbCoches.getSelectedItem();
        txtKmRepostaje.setText(ToolsConversiones.RedondearSinDecimales(cocheSeleccionado.getKmActuales()).toString());

        /************ TIPO REPOSTAJE ************/
        rbTipoRepostaje.check(rbTipoRepostaje1.getId());

        /************ TIPO DE COMBUSTIBLE *********/

        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
        listCombustibles = bbddCombustibles.getCombustibleByTipo(cocheSeleccionado.getIdCombustible());
        listSubtipos = new ArrayList<String>();
        for (Combustible com : listCombustibles) {
            listSubtipos.add(com.getSubtipo());
        }

        ArrayAdapter<String> adaptadorCombustibles = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSubtipos);
        cmbTipoCombustible.setAdapter(adaptadorCombustibles);

        /************ TIPO DE CONDUCCION ************/
        rbTipoConduccion.check(rbTipoConduccion1.getId());


        /*********** TIPO DE CARRETERA ****************/
        ArrayList<String> listTipoCarretera = new ArrayList<String>();
        listTipoCarretera.add("");
        listTipoCarretera.add(getString(R.string.tipo_carretera_carretera));
        listTipoCarretera.add(getString(R.string.tipo_carretera_ciudad));
        listTipoCarretera.add(getString(R.string.tipo_carretera_autopista));
        listTipoCarretera.add(getString(R.string.tipo_carretera_mixto));

        ArrayAdapter<String> adaptadorTipoCarretera = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTipoCarretera);
        cmbTipoCarretera.setAdapter(adaptadorTipoCarretera);


        /*********** TIPO DE PAGO ****************/
        ArrayList<String> listTipoPago = new ArrayList<String>();
        listTipoPago.add("");
        listTipoPago.add(getString(R.string.tipo_de_pago_efectivo));
        listTipoPago.add(getString(R.string.tipo_de_pago_tarjeta));

        ArrayAdapter<String> adaptadorTipoPago = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                listTipoPago);
        cmbTipoPago.setAdapter(adaptadorTipoPago);

    }

    private void rellenarFormulario(Repostaje repostaje) {


        /************* COCHE ********************/

        BBDDCoches bbddCoches = new BBDDCoches(this);

        ArrayList<Coche> listaCoches = bbddCoches.getTodosLosCoches();
        ArrayAdapter<Coche> adaptadorCoches = new ArrayAdapter<Coche>(this, android.R.layout.simple_spinner_item,
                listaCoches);
        cmbCoches.setAdapter(adaptadorCoches);

        Coche cocheSeleccionado = null;

        for (Coche coche : listaCoches) {
            if (coche.getIdCoche() == repostaje.getIdCoche()) {
                cocheSeleccionado = coche;
            }
        }

        if (null != cocheSeleccionado) {
            int posicion = adaptadorCoches.getPosition(cocheSeleccionado);
            cmbCoches.setSelection(posicion);
        }

        /************* FECHA REPOSTAJE ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date repostajeDate = new Date(
                repostaje.getFechaRespostaje());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(repostajeDate);

        txtFechaRespostaje.setText(formatteDate);

        // **********PRECIO REPOSTAJE************************///
        //TODO mirar a ver si se controla que no puedan llegar nulos, si no se controla, controlarlo aquí para todos los campos
        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            txtPrecio.setText(repostaje.getPrecioLitro().setScale(3, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            BigDecimal precioPorGalonUK = ToolsConversiones.precioPorLitroToPrecioPorGalonUK(repostaje.getPrecioLitro());
            txtPrecio.setText(precioPorGalonUK.toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            BigDecimal precioPorGalonUS = ToolsConversiones.precioPorLitroToPrecioPorGalonUS(repostaje.getPrecioLitro());
            txtPrecio.setText(precioPorGalonUS.toString());
        }
        // **********COSTE REPOSTAJE************************///

        txtCosteRepostaje.setText(repostaje.getCosteRepostaje().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        // **********CANTIDAD REPOSTAJE************************///
        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            txtLitros.setText(repostaje.getLitros().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            BigDecimal galonesUK = ToolsConversiones.litrosToGalonesUK(repostaje.getLitros()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtLitros.setText(galonesUK.toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            BigDecimal galonesUS = ToolsConversiones.litrosToGalonesUS(repostaje.getLitros()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtLitros.setText(galonesUS.toString());
        }

        rbTipoRepostaje2.setChecked(!repostaje.getEsCompleto());
        rbTipoRepostaje1.setChecked(repostaje.getEsCompleto());

        /************ TIPO DE COMBUSTIBLE *********/

//        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
        listCombustibles = bbddCombustibles.getCombustibleByTipo(cocheSeleccionado.getIdCombustible());
        ArrayList<String> listSubtipos = new ArrayList<String>();

        Combustible combustibleSeleccionado = null;
        for (Combustible com : listCombustibles) {
            listSubtipos.add(com.getSubtipo());
            if (com.getIdCombustible() == repostaje.getIdCombustible()) {
                combustibleSeleccionado = com;
            }
        }

        ArrayAdapter<String> adaptadorCombustibles = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listSubtipos);
        cmbTipoCombustible.setAdapter(adaptadorCombustibles);

        if (null != combustibleSeleccionado) {
            int position = adaptadorCombustibles
                    .getPosition(combustibleSeleccionado.getSubtipo());
            cmbTipoCombustible.setSelection(position);
            cmbTipoCombustible.refreshDrawableState();
        }

        chkEsAA.setChecked(repostaje.getEsAA());
        chkEsRemolque.setChecked(repostaje.getEsRemolque());
        chkEsBaca.setChecked(repostaje.getEsBaca());

        rbTipoConduccion1.setChecked(repostaje.getTipoConduccion()
                .equalsIgnoreCase("tipo_de_conduccion_normal"));
        rbTipoConduccion2.setChecked(repostaje.getTipoConduccion()
                .equalsIgnoreCase("tipo_de_conduccion_economica"));
        rbTipoConduccion3.setChecked(repostaje.getTipoConduccion()
                .equalsIgnoreCase("tipo_de_conduccion_deportiva"));

        /*********** TIPO DE CARRETERA ****************/
        ArrayList<String> listTipoCarretera = new ArrayList<String>();
        listTipoCarretera.add("");
        listTipoCarretera.add(getString(R.string.tipo_carretera_carretera));
        listTipoCarretera.add(getString(R.string.tipo_carretera_ciudad));
        listTipoCarretera.add(getString(R.string.tipo_carretera_autopista));
        listTipoCarretera.add(getString(R.string.tipo_carretera_mixto));
        ArrayAdapter<String> adaptadorTipoCarretera = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listTipoCarretera);
        cmbTipoCarretera.setAdapter(adaptadorTipoCarretera);

        String tipoCarreteraSeleccionado = "";
//        for (String tipoCarretera : listTipoCarretera) {
//            if (tipoCarretera.equals(repostaje.getTipoCarretera())) {
//                tipoCarreteraSeleccionado = (repostaje.getTipoCarretera());
//            }
//        }

        for (String tipoCarretera : listTipoCarretera) {
            if (repostaje.getTipoCarretera().equalsIgnoreCase("tipo_carretera_carretera")) {
                tipoCarreteraSeleccionado = getString(R.string.tipo_carretera_carretera);
            } else if (repostaje.getTipoCarretera().equalsIgnoreCase("tipo_carretera_ciudad")) {
                tipoCarreteraSeleccionado = getString(R.string.tipo_carretera_ciudad);
            } else if (repostaje.getTipoCarretera().equalsIgnoreCase("tipo_carretera_autopista")) {
                tipoCarreteraSeleccionado = getString(R.string.tipo_carretera_autopista);
            } else if (repostaje.getTipoCarretera().equalsIgnoreCase("tipo_carretera_mixto")) {
                tipoCarreteraSeleccionado = getString(R.string.tipo_carretera_mixto);
            } else {
                tipoCarreteraSeleccionado = "";
            }
        }
        if (!tipoCarreteraSeleccionado.equals("")) {
            cmbTipoCarretera.setSelection(adaptadorTipoCarretera
                    .getPosition(tipoCarreteraSeleccionado));
        }

        /*********** TIPO DE PAGO ****************/
        ArrayList<String> listTipoPago = new ArrayList<String>();
        listTipoPago.add("");
        listTipoPago.add(getString(R.string.tipo_de_pago_efectivo));
        listTipoPago.add(getString(R.string.tipo_de_pago_tarjeta));
        ArrayAdapter<String> adaptadorTipoPago = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTipoPago);
        cmbTipoPago.setAdapter(adaptadorTipoPago);

        String tipoPagoSeleccionado = "";
//        for (String tipoPago : listTipoPago) {
//            if (tipoPago.endsWith(repostaje.getTipoPago())) {
//                tipoPagoSeleccionado = tipoPago;
//            }
//        }
        for (String tipoPago : listTipoPago) {
            if (repostaje.getTipoPago().equalsIgnoreCase("tipo_de_pago_efectivo")) {
                tipoPagoSeleccionado = getString(R.string.tipo_de_pago_efectivo);
            } else if (repostaje.getTipoPago().equalsIgnoreCase("tipo_de_pago_tarjeta")) {
                tipoPagoSeleccionado = getString(R.string.tipo_de_pago_tarjeta);
            } else {
                tipoPagoSeleccionado = "";
            }
        }
        if (!tipoPagoSeleccionado.equals("")) {
            cmbTipoPago.setSelection(adaptadorTipoPago
                    .getPosition(tipoPagoSeleccionado));
        }


        if (bbddAjustesAplicacion.esKm()) {
            txtVelocidadMedia.setText(repostaje.getVelocidadMedia()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.esMillas()) {
            BigDecimal velocidadMedia = ToolsConversiones.kmToMillas(repostaje.getVelocidadMedia()).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtVelocidadMedia.setText(velocidadMedia.toString());
        }


        txtEstacionServicioAuto.setText(repostaje.getAreaServicio());
        txtComentarios.setText(repostaje.getComentarios());

    }

    /**
     * @param repostaje
     * @return boolean
     */


    private ArrayList<String> validar(Repostaje repostaje) {

        ArrayList<String> errores = new ArrayList<String>();

        if (null != repostaje) {
            BBDDRepostajes bbddRepostaje = new BBDDRepostajes(this);
            Repostaje repostajeAnterior = bbddRepostaje.getRepostajeAnteriorPorCoche(repostaje);

            if (null == repostajeAnterior) {
                if (null == repostaje.getFechaRespostaje()) {
                    errores.add(getString(R.string.error_fecha_repostaje));
                }
//                if (null == repostaje.getKmRepostaje()) {
//                    errores.add(getString(R.string.error_km_repostaje));
//                }
                if (null == repostaje.getKmRepostaje() || "".equals(repostaje.getKmRepostaje()) || BigDecimal.ZERO.compareTo(repostaje.getKmRepostaje()) == 0) {
                    errores.add(getString(R.string.error_km_repostaje));
                }
//                if(repostaje.getKmRecorridos().compareTo(BigDecimal.ZERO) != 1 ){
//                    errores.add(getString(R.string.error_km_recorridos));

                if (null == repostaje.getPrecioLitro() || repostaje.getPrecioLitro().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_precio_litro));
                }
                if (null == repostaje.getCosteRepostaje() || repostaje.getCosteRepostaje().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_coste_repostaje));
                }
                if (null == repostaje.getLitros() || repostaje.getLitros().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_litros));
                }
                if (null == repostaje.getEsCompleto()) {
                    errores.add(getString(R.string.error_es_completo));
                }
                if (null == repostaje.getIdCombustible()) {
                    errores.add("idCombustible");
                }


            } else {
                if (null == repostaje.getFechaRespostaje()) {
                    errores.add(getString(R.string.error_fecha_repostaje));
                }
//                if (null == repostaje.getKmRepostaje()) {
//                    errores.add(getString(R.string.error_km_repostaje));
//                }

                if (null == repostaje.getKmRepostaje() || "".equals(repostaje.getKmRepostaje()) || BigDecimal.ZERO.compareTo(repostaje.getKmRepostaje()) == 0) {
                    errores.add(getString(R.string.error_km_repostaje));
                }
                if (esNuevo) {
                    if (repostaje.getKmRecorridos().compareTo(BigDecimal.ZERO) != 1) {
                        errores.add(getString(R.string.error_km_recorridos));
                    }
                }
                if (null == repostaje.getPrecioLitro() || repostaje.getPrecioLitro().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_precio_litro));
                }
                if (null == repostaje.getCosteRepostaje() || repostaje.getCosteRepostaje().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_coste_repostaje));
                }
                if (null == repostaje.getLitros() || repostaje.getLitros().compareTo(BigDecimal.ZERO) != 1) {
                    errores.add(getString(R.string.error_litros));
                }
                if (null == repostaje.getEsCompleto()) {
                    errores.add(getString(R.string.error_es_completo));
                }
                if (null == repostaje.getIdCombustible()) {
                    errores.add("idCombustible");
                }

            }
        }

        return errores;
    }


}