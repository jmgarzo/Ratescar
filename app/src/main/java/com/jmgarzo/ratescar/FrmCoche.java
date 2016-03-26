package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDCombustibles;
import com.jmgarzo.bbdd.BBDDImagenes;
import com.jmgarzo.bbdd.BBDDMarcas;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Combustible;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Imagen;
import com.jmgarzo.objects.Marca;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

//import com.jmgarzo.tools.ToolsImagenes;


public class FrmCoche extends Activity {

    private AdView adView;
    LinearLayout linearLayoutPubli;

    private Spinner cmbMarcas, cmbCombustibles;

    private EditText txtMatricula, txtNombreCoche,
            txtModeloCoche, txtVersionCoche, txtTamanoDeposito, txtCc,
            txtKmActuales, txtComentarios, txtFechaCompra, txtFechaMatriculacion;// txtFechaFabricacion;
    private TextView lbUnidadDistanciaKmActuales, lbUnidadTamanoDeposito, lbKmActuales;


    //    private FrameLayout frameLayoutFoto;

   //Descomentar para volver a poner la funcionalidad de sacar foto
   // private ImageView imgFotoCoche;

    private ArrayList<Marca> listMarcas;
    private ArrayList<Combustible> listCombustibles;

    private Coche coche;

    private BBDDCoches bbddCoche = null;
    private BBDDImagenes bbddImagenes = null;
    private BBDDAjustesAplicacion bbddAjustesAplicacion = null;
    private boolean esNuevo;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_SELECT_PHOTO = 0;
//    private String photoPath;
//    Imagen imagen;

    private ArrayList<Imagen> listaImagenes;
    Bitmap foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_coche);

        /**PUBLICIDAD**/

        adView = new AdView(this);
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        linearLayoutPubli = (LinearLayout) this.findViewById(R.id.banner);

        linearLayoutPubli.addView(adView);


//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
//                .build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/

        getActionBar().setDisplayHomeAsUpEnabled(true);
        esNuevo = false;

        bbddCoche = new BBDDCoches(this);
        bbddImagenes = new BBDDImagenes(this);

        txtMatricula = (EditText) findViewById(R.id.txtMatricula);
        txtNombreCoche = (EditText) findViewById(R.id.txtNombreCoche);
        cmbMarcas = (Spinner) findViewById(R.id.CmbMarcas);
        txtModeloCoche = (EditText) findViewById(R.id.txtModeloCoche);
        txtVersionCoche = (EditText) findViewById(R.id.txtVersionCoche);
        txtTamanoDeposito = (EditText) findViewById(R.id.txtTamanoDeposito);
//        txtKmIniciales = (EditText) findViewById(R.id.txtKmIniciales);
        cmbCombustibles = (Spinner) findViewById(R.id.cmbCombustibles);
        txtCc = (EditText) findViewById(R.id.txtCc);
        txtKmActuales = (EditText) findViewById(R.id.txtKmActuales);
        txtComentarios = (EditText) findViewById(R.id.txtComentarios);
        txtFechaCompra = (EditText) findViewById(R.id.txtFechaCompra);
        txtFechaMatriculacion = (EditText) findViewById(R.id.txtFechaMatriculacion);

        lbUnidadDistanciaKmActuales = (TextView) findViewById(R.id.lbUnidadDistanciaKmActuales);
        lbUnidadTamanoDeposito = (TextView) findViewById(R.id.lbUnidadTamanoDeposito);
        lbKmActuales = (TextView) findViewById(R.id.lbKmActuales);

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(this);
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            lbUnidadDistanciaKmActuales.setText(getString(R.string.unidad_distancia_km));
            lbKmActuales.setText(getString(R.string.lb_km_actuales_km));
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            lbUnidadDistanciaKmActuales.setText(getString(R.string.unidad_distancia_millas));
            lbKmActuales.setText(getString(R.string.lb_km_actuales_millas));
        }

        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            lbUnidadTamanoDeposito.setText(getString(R.string.unidad_volumen_litros));
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            lbUnidadTamanoDeposito.setText(getString(R.string.unidad_volumen_galones_UK));
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            lbUnidadTamanoDeposito.setText(getString(R.string.unidad_volumen_galones_US));
        }


        // txtFechaFabricacion = (EditText) findViewById(R.id.txtFechaFabricacion);
//        frameLayoutFoto = (FrameLayout) findViewById(R.id.frameLayoutFoto);
        //
//        imgFotoCoche = (ImageView) findViewById(R.id.imgFotoCoche);
//        registerForContextMenu(imgFotoCoche);
//        imgFotoCoche.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//
//            }
//        });


        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
        listMarcas = bbddMarcas.getTodasLasMarcasOrderByNombre();

        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
        cmbMarcas.setAdapter(adaptadorMarca);


        // ************************************************************
        // *******cmbCombustible***************************************
        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
        listCombustibles = bbddCombustibles.getOnlyTipoCombustibles();
        Combustible tipoCombustibleSeleccionado = null;
        ArrayAdapter<Combustible> adaptadorCombustibles = new ArrayAdapter<Combustible>(
                this, android.R.layout.simple_spinner_item, listCombustibles);
        cmbCombustibles.setAdapter(adaptadorCombustibles);

        String idCoche = getIntent().getStringExtra("idCoche");
        coche = bbddCoche.getCoche(idCoche);


//        final Activity activity = this;
//        imgFotoCoche.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // photoPath = ToolsImagenes.sacarFoto(activity, coche.getIdCoche(), REQUEST_CAMERA);
//                registerForContextMenu(v);
//                openContextMenu(v);
//                unregisterForContextMenu(v);
//            }
//        });

        if (null != coche) {
            esNuevo = false;
            rellenarFormulario(coche);
        } else {
            coche = new Coche();
            esNuevo = true;

            coche = new Coche();
//            frameLayoutFoto.setEnabled(false);
//            frameLayoutFoto.setVisibility(View.INVISIBLE);
//            frameLayoutFoto.removeAllViews();
//            imgFotoCoche.setImageResource(R.drawable.ic_plus_gris_transparente);


            cargarDatosBasicos();
        }


        txtFechaCompra.setFocusable(false);
        txtFechaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaCompra);
            }
        });
        txtFechaMatriculacion.setFocusable(false);
        txtFechaMatriculacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaMatriculacion);
            }
        });
//        txtFechaFabricacion.setFocusable(false);
//        txtFechaFabricacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaFabricacion);
//            }
//        });

//        ArrayList<byte[]> listaImagenes = bbddCoche.getImagenesPorId(coche.getIdCoche());
//        if(listaImagenes.size()>0 && null!= listaImagenes.get(0)){
//            Bitmap img =BitmapFactory.decodeByteArray(listaImagenes.get(0), 0, listaImagenes.get(0).length);
//            imgFotoCoche.setImageBitmap(img);
//        }


    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.context_menu_foto, menu);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case R.id.action_camara:
//                photoPath = ToolsImagenes.sacarFoto(this, coche.getIdCoche(), REQUEST_CAMERA);
//                return true;
//            case R.id.action_galeria:
//
//                photoPath = ToolsImagenes.seleccionarFotoGaleria(this, coche.getIdCoche(), REQUEST_SELECT_PHOTO);
//
////                Intent intent = new Intent(Intent.ACTION_PICK);
////                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                        MediaStore.Images.Media.CONTENT_TYPE);
////                startActivityForResult(intent, REQUEST_SELECT_PHOTO);
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }


    public void showDatePickerDialog(EditText v) {
        DatePickerFragment newFragment = new DatePickerFragment(this, v);
        newFragment.show(getFragmentManager(), "datePicker");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_frm_coche, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_save:
                if (esNuevo) {
                    //imagen = new Imagen();
                    Coche newCoche = new Coche();
                    mappingCoche(newCoche);
                    ArrayList<String> errores = validar(newCoche);
                    if (errores.isEmpty()) {
                        bbddCoche.nuevoCoche(newCoche);
//                        imagen.setIdCoche(bbddCoche.ultimoIdCoche());
//                        bbddImagenes.nuevaImagenYBorradoDeLasDemas(imagen);
//                        ArrayList<Imagen> listaImagenes = bbddImagenes.getImagenesPorTipoOrderIdCocheDesc(Constantes.TIPO_COCHE);
                        listaImagenes = bbddImagenes.getImagenesPorTipoOrderIdCocheDesc(Constantes.TIPO_COCHE);
                        //ToolsImagenes.limpiarFotosCocheSinUso(listaImagenes,this);



                        Intent intent = new Intent(FrmCoche.this,
                                MainActivity.class);
                        intent.putExtra("Coche", "coche");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        Bundle b = new Bundle();

                        b.putString("titulo", getString(R.string.titulo_alerta_errores));
                        b.putString("error", getString(R.string.error_nombre_o_matricula));
                        DialogoAviso dialogo = new DialogoAviso();
                        dialogo.setArguments(b);
                        dialogo.show(fm, "tagAlerta");
                    }
                } else {
                    mappingCoche(coche);
                    ArrayList<String> errores = validar(coche);
                    if (errores.isEmpty()) {
                        //mappingCoche(coche);
                        bbddCoche.actualizarCoche(coche);
//                        imagen.setIdCoche(coche.getIdCoche());
//                        if (null != imagen) {
//                            bbddImagenes.nuevaImagenYBorradoDeLasDemas(imagen);
//                            listaImagenes = bbddImagenes.getImagenesPorTipoOrderIdCocheDesc(Constantes.TIPO_COCHE);
//                            ToolsImagenes.limpiarFotosCocheSinUso(listaImagenes, this);
//
//                        }
                        Intent intent = new Intent(FrmCoche.this, MainActivity.class);
                        intent.putExtra("Coche", "coche");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        Bundle b = new Bundle();

                        b.putString("titulo", getString(R.string.titulo_alerta_errores));
                        b.putString("error", getString(R.string.error_nombre_o_matricula));
                        DialogoAviso dialogo = new DialogoAviso();
                        dialogo.setArguments(b);
                        dialogo.show(fm, "tagAlerta");
                    }
                }
                return true;
            case R.id.action_delete:
                Log.i("FrmDetalleCoche", "Pulsado action_delete");
                if (esNuevo) {
                    onBackPressed();
                } else {
                    DialogoDeleteCoche dialogoDeleteCoche = new DialogoDeleteCoche();
                    Bundle b = new Bundle();
                    b.putString("idCoche", coche.getIdCoche().toString());
                    dialogoDeleteCoche.setArguments(b);
                    dialogoDeleteCoche.show(getFragmentManager(), "delete");
                }
                return true;

//            case R.id.action_camara:
//
//
//                photoPath = ToolsImagenes.sacarFoto(this, coche.getIdCoche(), REQUEST_CAMERA);
//
//
//                return true;
//
//            case R.id.action_galeria:
//                Intent ii = new Intent(Intent.ACTION_PICK);
//                ii.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(ii, REQUEST_SELECT_PHOTO);
//                return true;

            case android.R.id.home:
                DialogoSalirSinGuardar dialogoSalirSinGuardar = new DialogoSalirSinGuardar();
                Bundle b = new Bundle();
                b.putString("keyFragment", "Coche");
                b.putString("valueFragment", "coche");
                dialogoSalirSinGuardar.setArguments(b);
                dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class AdaptadorMarca extends ArrayAdapter<Marca> {
        private Context context;

        List<Marca> datos = listMarcas;

        public AdaptadorMarca(Context context, List<Marca> datos) {
            //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
            super(context, R.layout.spinner_marcas, datos);
            this.context = context;
            this.datos = datos;
        }

        //este método establece el elemento seleccionado sobre el botón del spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_marcas, null);
            }
            ((TextView) convertView.findViewById(R.id.lbNombreMarca)).setText(datos.get(position).getNombre());
            ((ImageView) convertView.findViewById(R.id.imgMarca)).setBackgroundResource(datos.get(position).getIcono());

            return convertView;
        }

        //gestiona la lista usando el View Holder Pattern. Equivale a la típica implementación del getView
        //de un Adapter de un ListView ordinario
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = layoutInflater.inflate(R.layout.spinner_marcas, parent, false);
            }

            if (row.getTag() == null) {
                MarcaHolder marcaHolder = new MarcaHolder();
                marcaHolder.setIcono((ImageView) row.findViewById(R.id.imgMarca));
                marcaHolder.setTextView((TextView) row.findViewById(R.id.lbNombreMarca));
                row.setTag(marcaHolder);
            }

            //rellenamos el layout con los datos de la fila que se está procesando
            Marca marca = datos.get(position);
            ((MarcaHolder) row.getTag()).getIcono().setImageResource(marca.getIcono());
            ((MarcaHolder) row.getTag()).getTextView().setText(marca.getNombre());

            return row;
        }

        private class MarcaHolder {

            private ImageView icono;

            private TextView nombre;

            public ImageView getIcono() {
                return icono;
            }

            public void setIcono(ImageView icono) {
                this.icono = icono;
            }

            public TextView getTextView() {
                return nombre;
            }

            public void setTextView(TextView nombre) {
                this.nombre = nombre;
            }

        }
    }

    private ArrayList<String> validar(Coche coche) {

        ArrayList<String> errores = new ArrayList<String>();
        if ((null == coche.getNombre() || coche.getNombre().equals("")) && (null == coche.getMatricula() || coche.getMatricula().equals(""))) {
            errores.add(getString(R.string.error_nombre_o_matricula));
        }
        return errores;
    }


    private void mappingCoche(Coche coche) {

        if (null != txtMatricula && !"".equals(txtMatricula.getText().toString())) {
            coche.setMatricula(txtMatricula.getText().toString());
        }

        if (null != txtNombreCoche && !"".equals(txtNombreCoche.getText().toString())) {
            coche.setNombre(txtNombreCoche.getText().toString());
        }


//        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
//        listMarcas = bbddMarcas.getTodasLasMarcas();
//        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
//        cmbMarcas.setAdapter(adaptadorMarca);
//        Marca marcaSeleccionada = null;
//
//        for (Marca marca : listMarcas) {
//            if (marca.getIdMarca() == coche.getIdMarca()) {
//                marcaSeleccionada = marca;
//
//            }
//        }
//        if (null != marcaSeleccionada) {
//            int posicion = adaptadorMarca.getPosition(marcaSeleccionada);
//
//            cmbMarcas.setSelection(posicion);
//            // cmbMarcas.refreshDrawableState();
//        } else {
//            // TODO probar a ver si esto funciona en caso de que la
//            // marcaSeleccionada sea null
//            cmbMarcas.refreshDrawableState();
//        }

        Marca marcaFrm = new Marca();
        marcaFrm = (Marca) cmbMarcas.getSelectedItem();
        coche.setIdMarca(marcaFrm.getIdMarca());

//        Marca marca = (Marca) cmbMarcas.getSelectedItem();
//        coche.setIdMarca(marca.getIdMarca());

        if (null != txtModeloCoche && !"".equals(txtModeloCoche.getText().toString())) {
            coche.setModelo(txtModeloCoche.getText().toString());
        }

        if (null != txtVersionCoche && !"".equals(txtVersionCoche.getText().toString())) {
            coche.setVersion(txtVersionCoche.getText().toString());
        }


        // ************************************************************
        // *******cmbCombustible***************************************
//        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
//        listCombustibles = bbddCombustibles.getOnlyTipoCombustibles();
//        Combustible tipoCombustibleSeleccionado = null;
//        for (Combustible tipoCombustible : listCombustibles) {
//            if (tipoCombustible.getIdCombustible() == coche.getIdCombustible()) {
//                tipoCombustibleSeleccionado = tipoCombustible;
//
//            }
//        }
//
//        ArrayAdapter<Combustible> adaptadorCombustibles = new ArrayAdapter<Combustible>(this,
//                android.R.layout.simple_spinner_item, listCombustibles);
//        cmbCombustibles.setAdapter(adaptadorCombustibles);
//
//        if (null != tipoCombustibleSeleccionado) {
//            int posicion = adaptadorCombustibles.getPosition(tipoCombustibleSeleccionado);
//            cmbCombustibles.setSelection(posicion);
//        } else {
//            // TODO probar a ver si esto funciona en caso de que la
//            // marcaSeleccionada sea null
//            cmbCombustibles.refreshDrawableState();
//        }

        Combustible combustible = (Combustible) cmbCombustibles.getSelectedItem();
        coche.setIdCombustible(combustible.getIdCombustible());

        if (null != txtCc && !"".equals(txtCc.getText().toString())) {
            coche.setCc(txtCc.getText().toString());
        }

        if (null != txtTamanoDeposito.getText().toString() && !"".equals(txtTamanoDeposito.getText().toString())) {
            if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                coche.setTamanoDeposito(new BigDecimal(txtTamanoDeposito.getText().toString()));
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                BigDecimal cantidad = ToolsConversiones.galonesUKToLitros(new BigDecimal(txtTamanoDeposito.getText().toString()));
                coche.setTamanoDeposito(cantidad);
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                BigDecimal cantidad = ToolsConversiones.galonesUSToLitros(new BigDecimal(txtTamanoDeposito.getText().toString()));
                coche.setTamanoDeposito(cantidad);
            }
        } else {
            coche.setTamanoDeposito(BigDecimal.ZERO);
        }

//        if (null != txtKmIniciales.getText().toString() && !"".equals(txtKmIniciales.getText().toString())) {
//            coche.setKmIniciales(BigDecimal.valueOf(Double.valueOf(txtKmIniciales.getText().toString())));
//        } else {
//            coche.setKmIniciales(BigDecimal.ZERO);
//        }
        coche.setKmIniciales(BigDecimal.ZERO);

        if (null != txtKmActuales.getText().toString() && !"".equals(txtKmActuales.getText().toString())) {
            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                coche.setKmActuales(new BigDecimal(txtKmActuales.getText().toString()));
            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                BigDecimal distancia = new BigDecimal(txtKmActuales.getText().toString());
                coche.setKmActuales(ToolsConversiones.millasToKm(distancia));
            }

        } else {
            coche.setKmActuales(BigDecimal.ZERO);
        }


        if (null != txtFechaCompra) {
            String sfecha = txtFechaCompra.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar cal = new GregorianCalendar();
            try {
                cal.setTime(format.parse(sfecha));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // calendarFecha.getTimeInMillis();
            coche.setFechaCompra((cal.getTimeInMillis()));
        }


        if (null != txtFechaMatriculacion) {
            String sfecha = txtFechaMatriculacion.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            GregorianCalendar cal = new GregorianCalendar();
            try {
                cal.setTime(format.parse(sfecha));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // calendarFecha.getTimeInMillis();
            coche.setFechaMatriculacion((cal.getTimeInMillis()));
        }


        if (null != txtComentarios && !"".equals(txtComentarios.getText().toString())) {
            coche.setComentarios(txtComentarios.getText().toString());
        }
    }


    private void cargarDatosBasicos() {


        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
        listMarcas = bbddMarcas.getTodasLasMarcasOrderByNombre();

        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
        cmbMarcas.setAdapter(adaptadorMarca);


        // ************************************************************
        // *******cmbCombustible***************************************
        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
        listCombustibles = bbddCombustibles.getOnlyTipoCombustibles();
        if (!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Spanish")) {
            for (Combustible com : listCombustibles) {
                if (com.getTipo().equalsIgnoreCase("Gasolina")) {
                    com.setTipo("Petrol");
                }
            }
        }
        Combustible tipoCombustibleSeleccionado = null;
        ArrayAdapter<Combustible> adaptadorCombustibles = new ArrayAdapter<Combustible>(
                this, android.R.layout.simple_spinner_item, listCombustibles);
        cmbCombustibles.setAdapter(adaptadorCombustibles);

        /************* FECHA  ********************/
        Calendar cal = new GregorianCalendar();
        java.util.Date currentDate = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(currentDate);

        txtFechaCompra.setText(formatteDate);
        txtFechaMatriculacion.setText(formatteDate);


    }

    private void rellenarFormulario(Coche coche) {


        txtMatricula.setText(coche.getMatricula());
        txtNombreCoche.setText(coche.getNombre());
        txtModeloCoche.setText(coche.getModelo());
        txtVersionCoche.setText(coche.getVersion());
        // txtTipoCombustible.setText(cocheParce.getIdCombustible().toString());
        txtCc.setText(coche.getCc());
        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            txtTamanoDeposito.setText(coche.getTamanoDeposito().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            BigDecimal tamano = ToolsConversiones.litrosToGalonesUK(coche.getTamanoDeposito());
            txtTamanoDeposito.setText(tamano.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            BigDecimal tamano = ToolsConversiones.litrosToGalonesUS(coche.getTamanoDeposito());
            txtTamanoDeposito.setText(tamano.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        // txtTamanoDeposito.setText(coche.getTamanoDeposito().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//        txtKmIniciales.setText(coche.getKmIniciales().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            txtKmActuales.setText(coche.getKmActuales().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            BigDecimal distancia = ToolsConversiones.kmToMillas(coche.getKmActuales());
            txtKmActuales.setText(distancia.setScale(0, BigDecimal.ROUND_HALF_UP).toString());

        }
        txtComentarios.setText(coche.getComentarios().toString().trim());


        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
        listMarcas = bbddMarcas.getTodasLasMarcasOrderByNombre();
        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
        cmbMarcas.setAdapter(adaptadorMarca);
        Marca marcaSeleccionada = null;
        for (Marca marca : listMarcas) {
            if (marca.getIdMarca() == coche.getIdMarca()) {
                marcaSeleccionada = marca;

            }
        }
        if (null != marcaSeleccionada) {
            int posicion = adaptadorMarca.getPosition(marcaSeleccionada);

            cmbMarcas.setSelection(posicion);
            // cmbMarcas.refreshDrawableState();
        } else {
            // TODO probar a ver si esto funciona en caso de que la
            // marcaSeleccionada sea null
            cmbMarcas.refreshDrawableState();
        }


        // ************************************************************
        // *******cmbCombustible***************************************
        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
        listCombustibles = bbddCombustibles.getOnlyTipoCombustibles();
        if (!Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Spanish")) {
            for (Combustible com : listCombustibles) {
                if (com.getTipo().equalsIgnoreCase("Gasolina")) {
                    com.setTipo("Petrol");
                }
            }
        }
        Combustible tipoCombustibleSeleccionado = null;
        for (Combustible tipoCombustible : listCombustibles) {
            if (tipoCombustible.getIdCombustible() == coche.getIdCombustible()) {
                tipoCombustibleSeleccionado = tipoCombustible;

            }
        }

        ArrayAdapter<Combustible> adaptadorCombustibles = new ArrayAdapter<Combustible>(this,
                android.R.layout.simple_spinner_item, listCombustibles);
        cmbCombustibles.setAdapter(adaptadorCombustibles);

        if (null != tipoCombustibleSeleccionado) {
            int posicion = adaptadorCombustibles.getPosition(tipoCombustibleSeleccionado);
            cmbCombustibles.setSelection(posicion);
        } else {
            // TODO probar a ver si esto funciona en caso de que la
            // marcaSeleccionada sea null
            cmbCombustibles.refreshDrawableState();
        }

        /************* FECHA COMPRA ********************/
        Calendar calCompra = new GregorianCalendar();
        if (null != coche.getFechaCompra()) {
            java.util.Date fechaCompraDate = new Date(
                    coche.getFechaCompra());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formatteDate = df.format(fechaCompraDate);

            txtFechaCompra.setText(formatteDate);
        } else {
            txtFechaCompra.setText("");
        }
        /************* FECHA MATRICULACION ********************/
        Calendar cal = new GregorianCalendar();
        if (null != coche.getFechaMatriculacion()) {
            java.util.Date fechaMatriculacionDate = new Date(
                    coche.getFechaMatriculacion());
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formatteDate = df.format(fechaMatriculacionDate);

            txtFechaMatriculacion.setText(formatteDate);
        } else {
            txtFechaMatriculacion.setText("");
        }


        String ruta = bbddImagenes.getRutaImagenPorCocheYTipo(coche.getIdCoche(), Constantes.TIPO_COCHE);
//        if (!Constantes.esVacio(ruta)) {
//            foto = ToolsImagenes.decodeSampledBitmapFromFile(ruta, 250, 250);
//            imgFotoCoche.setImageBitmap(foto);
//        } else {
//            imgFotoCoche.setImageResource(R.drawable.ic_plus_gris_transparente);
//        }
//        listaImagenes = bbddImagenes.getImagenesPorCocheYTipoCocheDesc(coche.getIdCoche(), Constantes.TIPO_COCHE);
//        if (listaImagenes.size() > 0) {
//
//            Imagen imagen = listaImagenes.get(0);
//
//
//            foto = ToolsImagenes.decodeSampledBitmapFromFile(imagen.getNombre(), 250, 250);
//
//
//            imgFotoCoche.setImageBitmap(foto);


//            for (int i = 0; listaImagenes.size() > 1 && i < listaImagenes.size(); i++) {
//                if (i >= 1) {
//                    if (ToolsFile.borrarFichero(listaImagenes.get(i).getNombre())) {
//                        bbddImagenes.eliminarImagen(listaImagenes.get(i).getIdImagen());
//                    }
//                }
//            }
//        }


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQUEST_CAMERA:
//                if ((null != photoPath) && !photoPath.equals("")) {
//
//
//
//                    imagen = new Imagen();
//                    if (esNuevo) {
//                        imagen.setIdCoche(bbddCoche.siguienteIdCoche());
//                    } else {
//                        imagen.setIdCoche(coche.getIdCoche());
//
//                    }
//
//                    File src = new File(photoPath);
//                    char cUrl[] = photoPath.toCharArray();
//                    int ultimaBarra = 0;
//                    for (int i = 0; i < photoPath.length(); i++) {
//                        if (cUrl[i] == "/".toCharArray()[0]) {
//                            ultimaBarra = i;
//                        }
//
//
//                    }
//                    imagen.setRuta(photoPath);
//                    String rutaRaiz = photoPath.substring(0, ultimaBarra);
//                    String nombreFoto = photoPath.substring(ultimaBarra + 1, photoPath.length());
//
//                    String rutaThum = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + Constantes.TIPO_COCHE + "/thumbnail";
//
//
//                    // create a File object for the parent directory
//                    File directorio = new File(rutaThum);
//                    if (!directorio.exists()) {
//                        directorio.mkdirs();
//                    }
//                    File dst = new File(directorio, nombreFoto);
//
//
//                    try {
//                        ToolsImagenes.copyImageAndResize(src, dst, 400, 400);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    imagen.setRutaThumb(directorio + "/" + nombreFoto);
//                    imagen.setTipo(Constantes.TIPO_COCHE);
//                    //   bbddImagenes.nuevaImagen(imagen);
//
//
//                }
//
//
//                break;
//            case REQUEST_SELECT_PHOTO:
//
//                if (resultCode != 0) {
//
//                    Cursor c = managedQuery(data.getData(), null, null, null, null);
//                    if (c.moveToFirst()) {
//                        photoPath = c.getString(1);
//                    }
//                }
//
//                imagen = new Imagen();
//                if (esNuevo) {
//                    imagen.setIdCoche(bbddCoche.siguienteIdCoche());
//                } else {
//                    imagen.setIdCoche(coche.getIdCoche());
//
//                }
//
//
//                long captureTime = System.currentTimeMillis();
//
//                File fOrigen = new File(photoPath);
//                photoPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/" + "Coches") + "/" + Constantes.TIPO_COCHE + imagen.getIdCoche().toString() + "_" + captureTime + ".jpg";
//                File fDestino = new File(photoPath);
//
//                try {
//                    ToolsFile.copy(fOrigen, fDestino);
//                } catch (IOException e) {
//                    Toast.makeText(this, "No se ha podido grabar la foto de la galería", Toast.LENGTH_LONG);
//                    e.printStackTrace();
//                }
//
//                imagen.setRuta(photoPath);
//                imagen.setTipo(Constantes.TIPO_COCHE);
//                break;
//
//                // bbddImagenes.nuevaImagen(imagen);
//
//            default:
//                break;
//
//        }
//
//    }

//    private byte[] getLogoImage(String url) {
//        try {
//            URL imageUrl = new URL(" file:///" + url);
//            URLConnection ucon = imageUrl.openConnection();
//
//            InputStream is = ucon.getInputStream();
//            BufferedInputStream bis = new BufferedInputStream(is);
//
//            ByteArrayBuffer baf = new ByteArrayBuffer(500);
//            int current = 0;
//            while ((current = bis.read()) != -1) {
//                baf.append((byte) current);
//            }
//
//            return baf.toByteArray();
//        } catch (Exception e) {
//            Log.d("ImageManager", "Error: " + e.toString());
//        }
//        return null;
//    }


    @Override
    public void onBackPressed() {
        DialogoSalirSinGuardar dialogoSalirSinGuardar = new DialogoSalirSinGuardar();
        Bundle b = new Bundle();
        b.putString("keyFragment", "Coche");
        b.putString("valueFragment", "coche");
        dialogoSalirSinGuardar.setArguments(b);
        dialogoSalirSinGuardar.show(getFragmentManager(), "Retroceder");


    }


    @Override
    protected void onResume() {
        super.onResume();

        linearLayoutPubli.removeAllViews();

//        if (null != imagen) {
//
//            foto = ToolsImagenes.decodeSampledBitmapFromFile(imagen.getRuta(), 150, 150);
//            if (null != foto) {
//                imgFotoCoche.setImageBitmap(foto);
//            }
//        }


    }
}
