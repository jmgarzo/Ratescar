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
import android.widget.EditText;
import android.widget.Spinner;

import com.jmgarzo.bbdd.BBDDCategorias;
import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.bbdd.BBDDSubcategorias;
import com.jmgarzo.objects.Categoria;
import com.jmgarzo.objects.Recambio;
import com.jmgarzo.objects.Subcategoria;

import java.util.ArrayList;


public class FrmRecambio extends Activity {


    private EditText txtNombre,txtFabricante,txtReferencia, txtDescripcion, txtComentarios;
    private Spinner cmbCategoria, cmbSubcategoria;
    private BBDDRecambios bbddRecambios;
    private BBDDCategorias bbddCategorias;
    private BBDDSubcategorias bbddSubcategorias;
    private boolean esNuevo;
    private Recambio recambio;
    private Integer idOperacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_recambio);
        getActionBar().setDisplayHomeAsUpEnabled(true);



        esNuevo = false;

        cmbCategoria = (Spinner) findViewById(R.id.cmbCategoriaRecambio);
        cmbSubcategoria= (Spinner) findViewById(R.id.cmbSubcategoriaRecambio);
        txtNombre = (EditText) findViewById(R.id.txtNombreRecambio);
        txtFabricante = (EditText) findViewById(R.id.txtFabricanteRecambio);
        txtReferencia = (EditText) findViewById(R.id.txtReferenciaRecambio);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcionRecambio);
        txtComentarios = (EditText) findViewById(R.id.txtComentariosRecambio);

        bbddRecambios = new BBDDRecambios(this);
        String idRecambio = getIntent().getStringExtra("idRecambio");
        idOperacion = getIntent().getIntExtra("idOperacion",-1);

        bbddCategorias = new BBDDCategorias(this);
        bbddSubcategorias = new BBDDSubcategorias(this);





        ArrayList<Categoria> listCategorias = bbddCategorias.getCategoriasOrdenAlfabetico();
        final ArrayList<String> listaNombreCategorias = new ArrayList<String>();
        for(Categoria cat : listCategorias){
            listaNombreCategorias.add(cat.getNombre());
        }

        final ArrayAdapter<String> adaptadorCategorias = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaNombreCategorias);
        cmbCategoria.setAdapter(adaptadorCategorias);



        ArrayList<String>listaNombreSubcategorias = null;
        final ArrayAdapter<String> adaptadorSubcategorias = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaNombreSubcategorias);

        cmbCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                ArrayList<Subcategoria> listaSubcategorias = bbddSubcategorias.getSubcategoriasPorNombreCategoria(cmbCategoria.getSelectedItem().toString());
                ArrayList<String>listaNombreSubcategorias2 = new ArrayList<String>();
                for (Subcategoria sub : listaSubcategorias) {
                    listaNombreSubcategorias2.add(sub.getNombre());
                }
                ArrayAdapter<String> adaptadorSubcategorias = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_spinner_item, listaNombreSubcategorias2);

                cmbSubcategoria.setAdapter(adaptadorSubcategorias);
                if(!esNuevo){
                    ArrayList<Subcategoria> listaSubcategorias3 = bbddSubcategorias.getSubcategoriasPorNombreCategoria(cmbCategoria.getSelectedItem().toString());
                    ArrayList<String>listaNombreSubcategorias3 = new ArrayList<String>();
                    for (Subcategoria sub : listaSubcategorias3) {
                        listaNombreSubcategorias3.add(sub.getNombre());
                    }
                    adaptadorSubcategorias = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, listaNombreSubcategorias3);

                    cmbSubcategoria.setAdapter(adaptadorSubcategorias);

                    String nombreSubcategoriaSeleccionado = bbddSubcategorias.getNombrePorId(recambio.getIdSubcategoria());
                    if (!nombreSubcategoriaSeleccionado.equals("")) {
                        cmbSubcategoria.setSelection(adaptadorSubcategorias
                                .getPosition(nombreSubcategoriaSeleccionado));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recambio = bbddRecambios.getRecambio(idRecambio);
        if (null != recambio) {
            recambio.setIdRecambio(Integer.parseInt(idRecambio));
            rellenarFormulario();
        } else {
            recambio = new Recambio();
            esNuevo = true;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_recambio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:

                recambio = mappingRecambio(recambio);
                if (esNuevo) {
                    bbddRecambios.nuevoRecambio(recambio);
                } else {
                    bbddRecambios.actualizar(recambio);
                }

                Intent intent = new Intent(FrmRecambio.this, LstRecambios.class);
                intent.putExtra("Recambio", "recambio");
                intent.putExtra("idOperacion",idOperacion);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case R.id.action_delete:
                Log.i("FrmDetalleCoche", "Pulsado action_delete");
                if(esNuevo){
                    onBackPressed();
                }
                else {
                    DialogoDeleteRecambio dialogoDeleteRecambio = new DialogoDeleteRecambio();
                    Bundle b = new Bundle();
                    b.putString("idRecambio",recambio.getIdRecambio().toString());
                    dialogoDeleteRecambio.setArguments(b);
                    dialogoDeleteRecambio.show(getFragmentManager(),"delete");

//                    bbddRecambios.eliminar(recambio.getIdRecambio().toString().trim());
//
//                    Intent intent2 = new Intent(FrmRecambio.this,MainActivity.class);
//                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent2);
                }


                return true;

            case android.R.id.home:
                Intent intent2 = new Intent(FrmRecambio.this, LstRecambios.class);
                intent2.putExtra("Recambio", "recambio");
                intent2.putExtra("idOperacion", idOperacion);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private Recambio mappingRecambio(Recambio recambio) {
        if (null != recambio.getIdRecambio()) {
            recambio.setIdRecambio(recambio.getIdRecambio());
        }
        recambio.setIdSubcategoria(bbddSubcategorias.getIdSubcategoriaByNombre(cmbSubcategoria.getSelectedItem().toString().trim()));
        recambio.setNombre(txtNombre.getText().toString());
        recambio.setFabricante(txtFabricante.getText().toString());
        recambio.setReferencia(txtReferencia.getText().toString());
        recambio.setDescripcion(txtDescripcion.getText().toString());
        recambio.setComentarios(txtComentarios.getText().toString());



        return recambio;
    }

    private void rellenarFormulario() {


        String nombreCategoria = bbddCategorias.getNombreCategoriaPorIdSubcategoria(recambio.getIdSubcategoria());

        ArrayList<Categoria> listCategorias = bbddCategorias.getCategoriasOrdenAlfabetico();
        ArrayList<String>listaNombreCategorias = new ArrayList<String>();
        for(Categoria cat : listCategorias){
            listaNombreCategorias.add(cat.getNombre());
        }

        final ArrayAdapter<String> adaptadorCategorias = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaNombreCategorias);
        cmbCategoria.setAdapter(adaptadorCategorias);

//
//        String tipoCategoriaSeleccionada = "";
//        for (String nombreCat : listaNombreCategorias) {
//            if (nombreCategoria.equals(nombreCat)) {
//                tipoCategoriaSeleccionada = (nombreCat);
//            }
//        }
        if (!nombreCategoria.equals("")) {
            cmbCategoria.setSelection(adaptadorCategorias
                    .getPosition(nombreCategoria));
        }


        ArrayList<Subcategoria> listaSubcategorias = bbddSubcategorias.getSubcategoriasPorNombreCategoria(cmbCategoria.getSelectedItem().toString());
        ArrayList<String>listaNombreSubcategorias2 = new ArrayList<String>();
        for (Subcategoria sub : listaSubcategorias) {
            listaNombreSubcategorias2.add(sub.getNombre());
        }
        ArrayAdapter<String> adaptadorSubcategorias = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listaNombreSubcategorias2);

        cmbSubcategoria.setAdapter(adaptadorSubcategorias);

        String nombreSubcategoriaSeleccionado = bbddSubcategorias.getNombrePorId(recambio.getIdSubcategoria());
        if (!nombreSubcategoriaSeleccionado.equals("")) {
            cmbSubcategoria.setSelection(adaptadorSubcategorias
                    .getPosition(nombreSubcategoriaSeleccionado));
        }

        txtNombre.setText(recambio.getNombre());
        txtFabricante.setText(recambio.getFabricante());
        txtReferencia.setText(recambio.getReferencia());
        txtDescripcion.setText(recambio.getDescripcion());
        txtComentarios.setText(recambio.getComentarios());

    }
}
