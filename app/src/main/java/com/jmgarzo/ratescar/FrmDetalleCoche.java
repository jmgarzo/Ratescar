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
//import android.app.FragmentManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
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
//import com.jmgarzo.objects.Repostaje;
//
//public class FrmDetalleCoche extends Activity {
//
//    private Spinner cmbMarcas, cmbCombustibles;
//
//    private EditText txtMatricula, txtNombreCoche, txtModeloCoche, txtTamanoDeposito, txtCc, txtKmIniciales,
//            txtKmActuales, txtComentarios, txtFechaCompra, txtFechaMatriculacion, txtFechaFabricacion;
//
//    private ArrayList<Coche> listCoches;
//    private ArrayList<Marca> listMarcas;
//    private ArrayList<Combustible> listCombustibles;
//    private BBDDCoches bbddCoche = null;
//    private Coche coche;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_frm_detalle_coche);
//
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//
//        bbddCoche = new BBDDCoches(this);
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
//        Integer idCoche = getIntent().getIntExtra("idCoche", 0);
//
//
//        coche = bbddCoche.getCoche(idCoche.toString());
//
//
//        txtMatricula.setText(coche.getMatricula());
//        txtNombreCoche.setText(coche.getNombre());
//        txtModeloCoche.setText(coche.getModelo());
//        // txtTipoCombustible.setText(cocheParce.getIdCombustible().toString());
//        txtCc.setText(coche.getCc());
//        txtTamanoDeposito.setText(coche.getTamanoDeposito().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//        txtKmIniciales.setText(coche.getKmIniciales().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//        txtKmActuales.setText(coche.getKmActuales().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//        txtComentarios.setText(coche.getComentarios().toString().trim());
//
//        /************* FECHA COMPRA ********************/
//        Calendar calCompra = new GregorianCalendar();
//        if (null != coche.getFechaCompra()) {
//            java.util.Date fechaCompraDate = new Date(
//                    coche.getFechaCompra());
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            String formatteDate = df.format(fechaCompraDate);
//
//            txtFechaCompra.setText(formatteDate);
//        } else {
//            txtFechaCompra.setText("");
//        }
//
//
//        /************* FECHA MATRICULACION ********************/
//        Calendar cal = new GregorianCalendar();
//        if (null != coche.getFechaMatriculacion()) {
//            java.util.Date fechaMatriculacionDate = new Date(
//                    coche.getFechaMatriculacion());
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            String formatteDate = df.format(fechaMatriculacionDate);
//
//            txtFechaMatriculacion.setText(formatteDate);
//        } else {
//            txtFechaMatriculacion.setText("");
//        }
//
//
//        /************* FECHA FABRICACION ********************/
//        Calendar calFabricacion = new GregorianCalendar();
//        if (null != coche.getFechaFabricacion()) {
//            java.util.Date fechaFabricacionDate = new Date(
//                    coche.getFechaFabricacion());
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//            String formatteDate = df.format(fechaFabricacionDate);
//
//            txtFechaFabricacion.setText(formatteDate);
//        } else {
//            txtFechaFabricacion.setText("");
//        }
//
//        BBDDMarcas bbddMarcas = new BBDDMarcas(this);
//        listMarcas = bbddMarcas.getTodasLasMarcas();
//        AdaptadorMarca adaptadorMarca = new AdaptadorMarca(this, listMarcas);
//        cmbMarcas.setAdapter(adaptadorMarca);
//        Marca marcaSeleccionada = null;
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
//
//
//        // ************************************************************
//        // *******cmbCombustible***************************************
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
///*        txtFechaCompra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showDatePickerDialog(txtFechaCompra);
//            }
//        });
//
//        txtFechaMatriculacion.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showDatePickerDialog(txtFechaMatriculacion);
//            }
//        });
//
//        txtFechaFabricacion.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                showDatePickerDialog(txtFechaFabricacion);
//            }
//        });*/
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.frm_detalle_coche, menu);
//        return true;
//    }
//
//    public void showDatePickerDialog(EditText v) {
//        DatePickerFragment newFragment = new DatePickerFragment(this, v);
//        newFragment.show(getFragmentManager(), "datePicker");
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        FragmentManager fm = getFragmentManager();
//        Coche coche = crearCocheDesdeFrm();
//        switch (item.getItemId()) {
//            case R.id.action_save:
//                ArrayList<String> errores = validar(coche);
//                if (errores.isEmpty()) {
//                    bbddCoche.actualizarCoche(coche);
//
//
//                    Intent intent = new Intent(FrmDetalleCoche.this, MainActivity.class);
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
//
//
//                return true;
//
//            case R.id.action_delete:
//                Log.i("FrmDetalleCoche", "Pulsado action_delete");
//
//
//                DialogoDeleteCoche dialogoDeleteCoche = new DialogoDeleteCoche();
//                Bundle b = new Bundle();
//                b.putString("idCoche", coche.getIdCoche().toString());
//                dialogoDeleteCoche.setArguments(b);
//                dialogoDeleteCoche.show(getFragmentManager(), "delete");
//
////                bbddCoche.eliminarCoche(coche.getIdCoche());
////                super.onBackPressed();
////
////                Intent intent2 = new Intent(FrmDetalleCoche.this, MainActivity.class);
////                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent2);
//
////                if (fm.getBackStackEntryCount() > 0) {
////                    // Log.i("MainActivity", "popping backstack");
////                    fm.popBackStack();
////                } else {
////                    // Log.i("MainActivity", "nothing on backstack, calling super");
////                    super.onBackPressed();
////                }
//
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }
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
//        cocheFrm.setIdCoche(coche.getIdCoche());
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
//        if (null != txtFechaCompra) {
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
//        }
//
//        if (null != txtFechaMatriculacion) {
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
//        if (null != txtFechaFabricacion) {
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
//}
