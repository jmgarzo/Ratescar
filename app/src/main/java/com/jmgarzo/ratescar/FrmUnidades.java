package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDMonedas;
import com.jmgarzo.objects.AjustesAplicacion;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Moneda;

import java.util.ArrayList;


public class FrmUnidades extends Activity {

    private Spinner cmbMonedas, cmbCantidadCombustible, cmbAjusteDistancia;
//            cmbMonedas, cmbPaises, cmbPrecioCombustible, cmbCantidadCombustible,
//            cmbAjusteDistancia, cmbAjusteVelocidad, cmbAjusteConsumo;

    private ArrayList<Moneda> listMonedas = null;
    //    private ArrayList<Pais> listPaises = null;
//    private ArrayList<String> listPrecioCombustible = null;
    private ArrayList<String> listCantidadCombustible = null;
    private ArrayList<String> listAjusteDistancia = null;
//    private ArrayList<String> listAjusteVelocidad = null;
//    private ArrayList<String> listAjusteConsumo = null;

    private BBDDAjustesAplicacion bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);
    private ArrayList<AjustesAplicacion> listaAjustesAplicacionBBDD = new ArrayList<AjustesAplicacion>();
    private AjustesAplicacion moneda = null;
    private AjustesAplicacion cantidadCombustible = null;
    private AjustesAplicacion distancia = null;
    private AjustesAplicacion inicializados = null;
    private boolean isfirstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_unidades);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        cmbMonedas = (Spinner) findViewById(R.id.cmbMonedas);
//        cmbPaises = (Spinner) findViewById(R.id.cmbPaises);
//        cmbPrecioCombustible = (Spinner) findViewById(R.id.cmbPrecioCombustible);
        cmbCantidadCombustible = (Spinner) findViewById(R.id.cmbCantidadCombustible);
        cmbAjusteDistancia = (Spinner) findViewById(R.id.cmbAjusteDistancia);
//        cmbAjusteVelocidad = (Spinner) findViewById(R.id.cmbAjusteVelocidad);
//        cmbAjusteConsumo = (Spinner) findViewById(R.id.cmbAjusteConsumo);


        listaAjustesAplicacionBBDD = bbddAjustesAplicacion.getAjustesAplicacionOrderByNombre();


        for (AjustesAplicacion ajuste : listaAjustesAplicacionBBDD) {
            if (ajuste.getNombre().equalsIgnoreCase("cantidadCombustible")) {
                cantidadCombustible = ajuste;
            } else if (ajuste.getNombre().equalsIgnoreCase("distancia")) {
                distancia = ajuste;
            } else if (ajuste.getNombre().equalsIgnoreCase("moneda")) {
                moneda = ajuste;
            } else if (ajuste.getNombre().equalsIgnoreCase("inicializados")) {
                inicializados = ajuste;
            }

        }
        if (inicializados.getValor().equalsIgnoreCase("no")) {
            isfirstTime = true;
        }


        //MONEDAS
        BBDDMonedas bbddMonedas = new BBDDMonedas(this);
        listMonedas = bbddMonedas.getTodasLasMonedasOrderCode();

        ArrayList<String> codeList = new ArrayList<String>();
        for (Moneda mon : listMonedas) {
            codeList.add(mon.getCode());
        }

        ArrayAdapter<String> adaptadorMonedas = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, codeList);
        cmbMonedas.setAdapter(adaptadorMonedas);

        Moneda monedaSeleccionada = null;
        for (Moneda mon : listMonedas) {
            if (moneda.getValor().equalsIgnoreCase(mon.getCode())) {
                monedaSeleccionada = mon;
            }
        }
        if (null != monedaSeleccionada) {
            int posicion = adaptadorMonedas.getPosition(monedaSeleccionada.getCode());
            cmbMonedas.setSelection(posicion);
        }


//        //PAISES
//        BBDDPaises bbddPaises = new BBDDPaises(this);
//        listPaises = bbddPaises.getPaisesOrderByNombre();
//
//        ArrayList<String> listNombrePaises = new ArrayList<String>();
//        for (Pais pais : listPaises) {
//            listNombrePaises.add(pais.getNombre().concat(" (")
//                    .concat(pais.getNombreIS0().concat(")")));
//        }
//
//        ArrayAdapter<String> adaptadorPaises = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, listNombrePaises);
//        cmbPaises.setAdapter(adaptadorPaises);


        //CANTIDAD COMBUSTIBLE

        listCantidadCombustible = new ArrayList<String>();

        listCantidadCombustible.add(getString(R.string.unidad_volumen_litros));
        listCantidadCombustible.add(getString(R.string.unidad_volumen_galones_UK));
        listCantidadCombustible.add(getString(R.string.unidad_volumen_galones_US));


        ArrayAdapter<String> adaptadorCantidadCombustible = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listCantidadCombustible);
        cmbCantidadCombustible.setAdapter(adaptadorCantidadCombustible);

        String cantidadCombustibleSeleccionado = null;
        if (cantidadCombustible.getValor().equalsIgnoreCase(Constantes.LITROS)) {
            cantidadCombustibleSeleccionado = getString(R.string.unidad_volumen_litros);
        } else if (cantidadCombustible.getValor().equalsIgnoreCase(Constantes.GALONES_UK)) {
            cantidadCombustibleSeleccionado = getString(R.string.unidad_volumen_galones_UK);
        } else if (cantidadCombustible.getValor().equalsIgnoreCase(Constantes.GALONES_US)) {
            cantidadCombustibleSeleccionado = getString(R.string.unidad_volumen_galones_US);
        }
//        String cantidadCombustibleSeleccionado = null;
//        for (String cantidad : listCantidadCombustible){
//            if(cantidadCombustible.getValor().equalsIgnoreCase(cantidad)){
//                cantidadCombustibleSeleccionado= cantidad;
//            }
//        }
        if (null != cantidadCombustibleSeleccionado) {
            int posicion = adaptadorCantidadCombustible.getPosition(cantidadCombustibleSeleccionado);
            cmbCantidadCombustible.setSelection(posicion);
        }


        //PRECIO DE COMBUSTIBLE


//        listPrecioCombustible = new ArrayList<String>();
//        listPrecioCombustible.add("EUR/litro");
//        listPrecioCombustible.add("EUR/litre");
//        listPrecioCombustible.add("GBP/litre");
//        listPrecioCombustible.add("cents/litre");
//        listPrecioCombustible.add("GBP/Gallon(US)");
//        listPrecioCombustible.add("GBP/Gallon(UK)");
//
//
//        ArrayAdapter<String> adaptadorPrecioCombustible = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, listPrecioCombustible);
//        cmbPrecioCombustible.setAdapter(adaptadorPrecioCombustible);


        //DISTANCIA

        listAjusteDistancia = new ArrayList<String>();
        listAjusteDistancia.add(getString(R.string.unidad_distancia_km));
        listAjusteDistancia.add(getString(R.string.unidad_distancia_millas));

        ArrayAdapter<String> adaptadorAjusteDistancia = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, listAjusteDistancia);
        cmbAjusteDistancia.setAdapter(adaptadorAjusteDistancia);

        String distanciaSeleccionada = null;
        if (distancia.getValor().equalsIgnoreCase(Constantes.KM)) {
            distanciaSeleccionada = getString(R.string.unidad_distancia_km);
        } else if (distancia.getValor().equalsIgnoreCase(Constantes.MILLAS)) {
            distanciaSeleccionada = getString(R.string.unidad_distancia_millas);
        }
//        for (String distan : listAjusteDistancia){
//            if(distancia.getValor().equalsIgnoreCase(distan)){
//                distanciaSeleccionada= distan;
//            }
//        }
        if (null != distanciaSeleccionada) {
            int posicion = adaptadorAjusteDistancia.getPosition(distanciaSeleccionada);
            cmbAjusteDistancia.setSelection(posicion);
        }


        //VELOCIDAD

//        listAjusteVelocidad = new ArrayList<String>();
//        listAjusteVelocidad.add("Km/h");
//        listAjusteVelocidad.add("mph");
//
//        ArrayAdapter<String> adaptadorAjusteVelocidad = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, listAjusteVelocidad);
//        cmbAjusteVelocidad.setAdapter(adaptadorAjusteVelocidad);


//        //CONSUMO
//
//        listAjusteConsumo = new ArrayList<String>();
//        listAjusteConsumo.add("l/100Km");
//        listAjusteConsumo.add("mpg(US)");
//        listAjusteConsumo.add("mpg(Imp)");
//
//        ArrayAdapter<String> adaptadorAjusteConsumo = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, listAjusteConsumo);
//        cmbAjusteConsumo.setAdapter(adaptadorAjusteConsumo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_unidades, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<AjustesAplicacion> ajustesActualizar = new ArrayList<AjustesAplicacion>();

                moneda.setValor(cmbMonedas.getSelectedItem().toString());
                ajustesActualizar.add(moneda);

                if (cmbCantidadCombustible.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.unidad_volumen_litros).toString())) {
                    cantidadCombustible.setValor(Constantes.LITROS);
                } else if (cmbCantidadCombustible.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.unidad_volumen_galones_UK).toString())) {
                    cantidadCombustible.setValor(Constantes.GALONES_UK);
                } else if (cmbCantidadCombustible.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.unidad_volumen_galones_US).toString())) {
                    cantidadCombustible.setValor(Constantes.GALONES_US);
                }
                //cantidadCombustible.setValor(cmbCantidadCombustible.getSelectedItem().toString());
                ajustesActualizar.add(cantidadCombustible);

                distancia.setValor(cmbAjusteDistancia.getSelectedItem().toString());
                if (cmbAjusteDistancia.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.unidad_distancia_km))) {
                    distancia.setValor(Constantes.KM);
                } else if (cmbAjusteDistancia.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.unidad_distancia_millas))) {
                    distancia.setValor(Constantes.MILLAS);
                }

                ajustesActualizar.add(distancia);

                inicializados.setValor("si");
                ajustesActualizar.add(inicializados);

                bbddAjustesAplicacion.actualizarAjustesAplicacion(ajustesActualizar);
                if (isfirstTime) {
                    Intent intent = new Intent(FrmUnidades.this, MainActivity.class);
                    intent.putExtra("Inicio", "inicio");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FrmUnidades.this, MainActivity.class);
                    intent.putExtra("Ajustes", "ajustes");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;


            case android.R.id.home:

                inicializados.setValor("si");
                ArrayList<AjustesAplicacion> ajustesActualizar2 = new ArrayList<AjustesAplicacion>();
                ajustesActualizar2.add(inicializados);
                bbddAjustesAplicacion.actualizarAjustesAplicacion(ajustesActualizar2);

                if (isfirstTime) {
                    Intent intent = new Intent(FrmUnidades.this, MainActivity.class);
                    intent.putExtra("Inicio", "inicio");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(FrmUnidades.this, MainActivity.class);
                    intent.putExtra("Ajustes", "ajustes");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }


    }

}
