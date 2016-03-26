//package com.jmgarzo.ratescar;
//
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.jmgarzo.bbdd.BBDDCoches;
//import com.jmgarzo.bbdd.BBDDCombustibles;
//import com.jmgarzo.bbdd.BBDDMarcas;
//import com.jmgarzo.objects.Coche;
//import com.jmgarzo.objects.Combustible;
//import com.jmgarzo.objects.Marca;
//
//public class FrmNuevoCoche extends Activity {
//
//    private Spinner cmbMarcas, cmbCombustibles;
//
//    private EditText txtMatricula, txtNombreCoche,
//            txtModeloCoche, txtTamanoDeposito, txtCc, txtKmIniciales,
//            txtKmActuales, txtComentarios, txtFechaCompra, txtFechaMatriculacion, txtFechaFabricacion;
//
//    private ArrayList<Marca> listMarcas;
//    private ArrayList<Combustible> listCombustibles;
//
//    private BBDDCoches bbddCoche = null;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_frm_nuevo_coche);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        bbddCoche = new BBDDCoches(this);
//
///*        lbFechaCompra = (TextView) findViewById(R.id.lbFechaCompra);
//        lbFechaCompra.hasOnClickListeners();
//        lbFechaCompra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               showDatePickerDialog(v);
//            }
//        });*/
//
//
//
//        txtMatricula = (EditText) findViewById(R.id.txtMatricula);
//        txtNombreCoche = (EditText) findViewById(R.id.txtNombreCoche);
//        cmbMarcas = (Spinner) findViewById(R.id.CmbMarcas);
//        txtModeloCoche = (EditText) findViewById(R.id.txtModeloCoche);
//        txtTamanoDeposito = (EditText) findViewById(R.id.txtTamanoDeposito);
//        txtKmIniciales = (EditText) findViewById(R.id.txtKmIniciales);
//        cmbCombustibles = (Spinner) findViewById(R.id.cmbCombustibles);
//        txtCc = (EditText) findViewById(R.id.txtCc);
//        txtKmActuales = (EditText) findViewById(R.id.txtKmActuales);
//        txtComentarios = (EditText) findViewById(R.id.txtComentarios);
//        txtFechaCompra = (EditText) findViewById(R.id.txtFechaCompra);
//        txtFechaMatriculacion = (EditText) findViewById(R.id.txtFechaMatriculacion);
//        txtFechaFabricacion = (EditText) findViewById(R.id.txtFechaFabricacion);
//
//        //txtFechaCompra.setOnClickListener(new);
//
//
//        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
//        listMarcas = bbddMarcas.getTodasLasMarcas();
////        listMarcas = new ArrayList<Marca>();
////        listMarcas.add(new Marca(1, "ACURA", R.drawable.ic_acura));
////        listMarcas.add(new Marca(2, "ALFA ROMEO", R.drawable.ic_alfa_romeo));
////        listMarcas.add(new Marca(2, "ASTON MARTIN", R.drawable.ic_aston_martin));
////        listMarcas.add(new Marca(2, "AUDI", R.drawable.ic_audi));
////        listMarcas.add(new Marca(2, "BENTLEY", R.drawable.ic_bentley));
////        listMarcas.add(new Marca(2, "BMW", R.drawable.ic_bmw));
////        listMarcas.add(new Marca(2, "BUGATTY", R.drawable.ic_bugatti));
////        listMarcas.add(new Marca(2, "BUICK", R.drawable.ic_buick));
////        listMarcas.add(new Marca(2, "CADILLAC", R.drawable.ic_cadillac));
////        listMarcas.add(new Marca(2, "CHERY", R.drawable.ic_chery));
////        listMarcas.add(new Marca(2, "CHEVROLET", R.drawable.ic_chevrolet));
////        listMarcas.add(new Marca(2, "CHRYSLER", R.drawable.ic_chrysler));
////        listMarcas.add(new Marca(2, "CITROËN", R.drawable.ic_citroen));
////        listMarcas.add(new Marca(2, "DACIA", R.drawable.ic_dacia));
////        listMarcas.add(new Marca(2, "DAEWOO", R.drawable.ic_daewoo));
////        listMarcas.add(new Marca(2, "DODGE", R.drawable.ic_dodge));
////        listMarcas.add(new Marca(2, "FERRARI", R.drawable.ic_ferrari));
////        listMarcas.add(new Marca(2, "FIAT", R.drawable.ic_fiat));
////        listMarcas.add(new Marca(2, "FORD", R.drawable.ic_ford));
////        listMarcas.add(new Marca(2, "GAZ", R.drawable.ic_gaz));
////        listMarcas.add(new Marca(2, "HOLDEN", R.drawable.ic_holden));
////        listMarcas.add(new Marca(2, "HONDA", R.drawable.ic_honda));
////        listMarcas.add(new Marca(2, "HYUNDAI", R.drawable.ic_hyundai));
////        listMarcas.add(new Marca(2, "INFINITI", R.drawable.ic_infiniti));
////        listMarcas.add(new Marca(2, "JAGUAR", R.drawable.ic_jaguar));
////        listMarcas.add(new Marca(2, "JEEP", R.drawable.ic_jeep));
////        listMarcas.add(new Marca(2, "KIA", R.drawable.ic_kia));
////        listMarcas.add(new Marca(2, "LADA", R.drawable.ic_lada));
////        listMarcas.add(new Marca(2, "LAMBORGHINI", R.drawable.ic_lamborghini));
////        listMarcas.add(new Marca(2, "LANCIA", R.drawable.ic_lancia));
////        listMarcas.add(new Marca(2, "LAND ROVER", R.drawable.ic_land_rover));
////        listMarcas.add(new Marca(2, "LEXUS", R.drawable.ic_lexus));
////        listMarcas.add(new Marca(2, "LOTUS", R.drawable.ic_lotus));
////        listMarcas.add(new Marca(2, "MASERATI", R.drawable.ic_maserati));
////        listMarcas.add(new Marca(2, "MAYBACH", R.drawable.ic_maybach));
////        listMarcas.add(new Marca(2, "MAZDA", R.drawable.ic_mazda));
////        listMarcas.add(new Marca(2, "MERCEDES", R.drawable.ic_mercedes));
////        listMarcas.add(new Marca(2, "MERCURY", R.drawable.ic_mercury));
////        listMarcas.add(new Marca(2, "MINI", R.drawable.ic_mini));
////        listMarcas.add(new Marca(2, "MITSUBISHI", R.drawable.ic_mitsubishi));
////        listMarcas.add(new Marca(2, "NISSAN", R.drawable.ic_nissan));
////        listMarcas.add(new Marca(2, "OPEL", R.drawable.ic_opel));
////        listMarcas.add(new Marca(2, "PAGANI", R.drawable.ic_pagani));
////        listMarcas.add(new Marca(2, "PEUGEOT", R.drawable.ic_peugeot));
////        listMarcas.add(new Marca(2, "PONTIAC", R.drawable.ic_pontiac));
////        listMarcas.add(new Marca(2, "PORCHE", R.drawable.ic_porshe));
////        listMarcas.add(new Marca(2, "RENAULT", R.drawable.ic_renault));
////        listMarcas.add(new Marca(2, "ROLL ROYCE", R.drawable.ic_rolls_royce));
////        listMarcas.add(new Marca(2, "ROVER", R.drawable.ic_rover));
////        listMarcas.add(new Marca(2, "SAAB", R.drawable.ic_saab));
////        listMarcas.add(new Marca(2, "SCION", R.drawable.ic_scion));
////        listMarcas.add(new Marca(2, "SEAT", R.drawable.ic_seat));
////        listMarcas.add(new Marca(2, "SKODA", R.drawable.ic_skoda));
////        listMarcas.add(new Marca(2, "SSANG YONG", R.drawable.ic_ssang_yong));
////        listMarcas.add(new Marca(2, "SUBARU", R.drawable.ic_subaru));
////        listMarcas.add(new Marca(2, "SUZUKI", R.drawable.ic_suzuki));
////        listMarcas.add(new Marca(2, "TOYOTA", R.drawable.ic_toyota));
////        listMarcas.add(new Marca(2, "VAUXHALL", R.drawable.ic_vauxhall));
////        listMarcas.add(new Marca(2, "VOLKSWAGEN", R.drawable.ic_volkswagen));
////        listMarcas.add(new Marca(2, "VOLVO", R.drawable.ic_volvo));
//
//
////		ArrayAdapter<Marca> adaptadorMarcas = new ArrayAdapter<Marca>(this,
////				android.R.layout.simple_spinner_item, listMarcas);
////		cmbMarcas.setAdapter(adaptadorMarcas);
//
//        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
//        cmbMarcas.setAdapter(adaptadorMarca);
//
//        // ************************************************************
//        // *******cmbCombustible***************************************
//        BBDDCombustibles bbddCombustibles = new BBDDCombustibles(this);
//        listCombustibles = bbddCombustibles.getOnlyTipoCombustibles();
//        Combustible tipoCombustibleSeleccionado = null;
//        ArrayAdapter<Combustible> adaptadorCombustibles = new ArrayAdapter<Combustible>(
//                this, android.R.layout.simple_spinner_item, listCombustibles);
//        cmbCombustibles.setAdapter(adaptadorCombustibles);
///*        txtFechaCompra.hasOnClickListeners();
//        txtFechaCompra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(v);
//            }
//        });*/
//
//        txtFechaCompra.setFocusable(false);
//        txtFechaCompra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaCompra);
//            }
//        });
//        txtFechaMatriculacion.setFocusable(false);
//        txtFechaMatriculacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaMatriculacion);
//            }
//        });
//        txtFechaFabricacion.setFocusable(false);
//        txtFechaFabricacion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(txtFechaFabricacion);
//            }
//        });
//
//
////        txtFechaCompra.setOnFocusChangeListener(new View.OnFocusChangeListener(){
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                showDatePickerDialog(txtFechaCompra);
////            }
////        });
////
////        txtFechaMatriculacion.setOnFocusChangeListener(new View.OnFocusChangeListener(){
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                showDatePickerDialog(txtFechaMatriculacion);
////            }
////        });
////
////        txtFechaFabricacion.setOnFocusChangeListener(new View.OnFocusChangeListener(){
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                showDatePickerDialog(txtFechaFabricacion);
////            }
////        });
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.frm_nuevo_coche, menu);
//        return true;
//    }
//
//    public void showDatePickerDialog(EditText v) {
//        DatePickerFragment newFragment = new DatePickerFragment(this,v);
//        newFragment.show(getFragmentManager(), "datePicker");
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        FragmentManager fm = getFragmentManager();
//        switch (item.getItemId()) {
//            case R.id.action_save:
//
//                Coche cochefrm = crearCocheDesdeFrm();
//                ArrayList<String> errores = validar(cochefrm);
//                if (errores.isEmpty()) {
//                    bbddCoche.nuevoCoche(cochefrm);
//                    Intent intent = new Intent(FrmNuevoCoche.this,
//                            MainActivity.class);
//                    intent.putExtra("Coche", "coche");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                } else {
//                    FragmentManager fragmentManager = getFragmentManager();
//                    Bundle b = new Bundle();
//
//                    b.putString("titulo", getString(R.string.titulo_alerta_errores));
//                    b.putString("error", getString(R.string.error_nombre_o_matricula));
//                    DialogoAviso dialogo = new DialogoAviso();
//                    dialogo.setArguments(b);
//                    dialogo.show(fm, "tagAlerta");
//                }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//
//    private Coche crearCocheDesdeFrm() {
//        // Recuperamos la marca del combo para poder insertarla en el objeto
//        // coche
//        Marca marcaFrm = new Marca();
//        marcaFrm = (Marca) cmbMarcas.getSelectedItem();
//        // Recuperamos el combustible del combo
//        Combustible combustibleFrm = new Combustible();
//        combustibleFrm = (Combustible) cmbCombustibles.getSelectedItem();
//        // Creamos un nuevo coche con los datos que hay en el formulario.
//        Coche cocheFrm = new Coche();
//        cocheFrm.setMatricula(txtMatricula.getText().toString().trim());
//        cocheFrm.setIdMarca(marcaFrm.getIdMarca());
//        cocheFrm.setNombre(txtNombreCoche.getText().toString().trim());
//        cocheFrm.setModelo(txtModeloCoche.getText().toString().trim());
//        cocheFrm.setIdCombustible(combustibleFrm.getIdCombustible());
//        cocheFrm.setCc(txtCc.getText().toString().trim());
//        if (null != txtTamanoDeposito.getText()
//                && !"".equals(txtTamanoDeposito.getText().toString())) {
//            cocheFrm.setTamanoDeposito(BigDecimal.valueOf(Double
//                    .valueOf(txtTamanoDeposito.getText().toString().trim())));
//        } else {
//            cocheFrm.setTamanoDeposito(BigDecimal.ZERO);
//        }
//        if (null != txtKmIniciales.getText()
//                && !"".equals(txtKmIniciales.getText().toString())) {
//            cocheFrm.setKmIniciales(BigDecimal.valueOf(Double.valueOf(txtKmIniciales.getText()
//                    .toString().trim())));
//        } else {
//            cocheFrm.setKmIniciales(BigDecimal.ZERO);
//        }
//
//        if (null != txtKmActuales.getText()
//                && !"".equals(txtKmActuales.getText().toString())) {
//            cocheFrm.setKmActuales(BigDecimal.valueOf(Double.valueOf(txtKmActuales.getText()
//                    .toString().trim())));
//        } else {
//            cocheFrm.setKmActuales(BigDecimal.ZERO);
//        }
//        cocheFrm.setComentarios(txtComentarios.getText().toString().trim());
//
//        if (null != txtFechaCompra){
//            String sfecha = txtFechaCompra.getText().toString();
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            GregorianCalendar cal = new GregorianCalendar();
//            try {
//                cal.setTime(format.parse(sfecha));
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            // calendarFecha.getTimeInMillis();
//            cocheFrm.setFechaCompra((cal.getTimeInMillis()));
//      }
//
//        if (null != txtFechaMatriculacion){
//            String sfecha = txtFechaMatriculacion.getText().toString();
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            GregorianCalendar cal = new GregorianCalendar();
//            try {
//                cal.setTime(format.parse(sfecha));
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            // calendarFecha.getTimeInMillis();
//            cocheFrm.setFechaMatriculacion((cal.getTimeInMillis()));
//        }
//
//        if (null != txtFechaFabricacion){
//            String sfecha = txtFechaFabricacion.getText().toString();
//            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//            GregorianCalendar cal = new GregorianCalendar();
//            try {
//                cal.setTime(format.parse(sfecha));
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            // calendarFecha.getTimeInMillis();
//            cocheFrm.setFechaFabricacion((cal.getTimeInMillis()));
//        }
//        // TODO
//        // Combustible combustible =
//        // bbddCombustibles.getCombustible(Integer.valueOf(cmb.))
//        // cocheFrm.setId
//
//        return cocheFrm;
//
//    }
//
//    public class AdaptadorMarca extends ArrayAdapter<Marca> {
//        private Context context;
//
//        List<Marca> datos = listMarcas;
//
//        public AdaptadorMarca(Context context, List<Marca> datos) {
//            //se debe indicar el layout para el item que seleccionado (el que se muestra sobre el botón del botón)
//            super(context, R.layout.spinner_marcas, datos);
//            this.context = context;
//            this.datos = datos;
//        }
//
//        //este método establece el elemento seleccionado sobre el botón del spinner
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spinner_marcas, null);
//            }
//            ((TextView) convertView.findViewById(R.id.lbNombreMarca)).setText(datos.get(position).getNombre());
//            ((ImageView) convertView.findViewById(R.id.imgMarca)).setBackgroundResource(datos.get(position).getIcono());
//
//            return convertView;
//        }
//
//        //gestiona la lista usando el View Holder Pattern. Equivale a la típica implementación del getView
//        //de un Adapter de un ListView ordinario
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            View row = convertView;
//            if (row == null) {
//                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                row = layoutInflater.inflate(R.layout.spinner_marcas, parent, false);
//            }
//
//            if (row.getTag() == null) {
//                MarcaHolder marcaHolder = new MarcaHolder();
//                marcaHolder.setIcono((ImageView) row.findViewById(R.id.imgMarca));
//                marcaHolder.setTextView((TextView) row.findViewById(R.id.lbNombreMarca));
//                row.setTag(marcaHolder);
//            }
//
//            //rellenamos el layout con los datos de la fila que se está procesando
//            Marca marca = datos.get(position);
//            ((MarcaHolder) row.getTag()).getIcono().setImageResource(marca.getIcono());
//            ((MarcaHolder) row.getTag()).getTextView().setText(marca.getNombre());
//
//            return row;
//        }
//
//        private class MarcaHolder {
//
//            private ImageView icono;
//
//            private TextView nombre;
//
//            public ImageView getIcono() {
//                return icono;
//            }
//
//            public void setIcono(ImageView icono) {
//                this.icono = icono;
//            }
//
//            public TextView getTextView() {
//                return nombre;
//            }
//
//            public void setTextView(TextView nombre) {
//                this.nombre = nombre;
//            }
//
//        }
//    }
//
//
//    private ArrayList<String> validar(Coche coche) {
//
//        ArrayList<String> errores = new ArrayList<String>();
//        if ((null == coche.getNombre() || coche.getNombre().equals("")) && (null == coche.getMatricula() || coche.getMatricula().equals(""))) {
//            errores.add(getString(R.string.error_nombre_o_matricula));
//        }
//        return errores;
//    }
//
//
//
//}
