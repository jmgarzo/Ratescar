package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDMantenimientoOperacion;
import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.MantenimientoOperacion;
import com.jmgarzo.objects.Operacion;
import com.jmgarzo.objects.OperacionListView;

import java.util.ArrayList;


public class LstOperaciones extends Activity {

    private AdView adView;

    private AdaptadorOperaciones dataAdapter = null;
    private String origen = "";

    private Integer idMantenimiento;

    private TextView txtNoHayOperaciones;
    private TextView lbSeleccionarOperaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operaciones_mantenimiento);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        /**PUBLICIDAD**/

        adView = new AdView(this);
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.banner);

        linearLayout.addView(adView);

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
//                .build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/


        txtNoHayOperaciones = (TextView) findViewById(R.id.txtNoHayOperaciones);
        txtNoHayOperaciones.setVisibility(View.INVISIBLE);

        lbSeleccionarOperaciones = (TextView) findViewById(R.id.lbSeleccionarOperaciones);


        idMantenimiento = getIntent().getIntExtra("idMantenimiento",-1);

        if (null != getIntent().getStringExtra("Origen")) {
            origen = getIntent().getStringExtra("Origen");
        }

        displayListView();

        checkButtonClick();
    }


    private void displayListView() {
        BBDDOperaciones bbddOperaciones = new BBDDOperaciones(this);

        //Array list of countries
        ArrayList<Operacion> listaOperaciones = bbddOperaciones.getOperacionesPorOrdenAlfabetico();
        //Eliminamos las operaciones que ya estén seleccionadas para ese mantenimiento
        BBDDMantenimientoOperacion bbddMantenimientoOperacion = new BBDDMantenimientoOperacion(this);
        ArrayList<MantenimientoOperacion> operacionesEnMantenimiento =bbddMantenimientoOperacion.getMantenimientoOperacionesByIdMantenimiento(idMantenimiento.toString());
        ArrayList <Integer> indicesABorrar = new ArrayList<Integer>();

        for(MantenimientoOperacion manOp : operacionesEnMantenimiento){
            for(Operacion op :listaOperaciones){
                if(manOp.getIdOperacion().intValue() == op.getIdOperacion().intValue()){
                    listaOperaciones.remove(op);
                    break;
                }
            }
        }
        //Eliminamos las operaciones que ya estén seleccionadas para ese mantenimiento


        ArrayList<OperacionListView> listaOperacionesListView = new ArrayList<OperacionListView>();

        for (Operacion op : listaOperaciones) {

            OperacionListView operacionListView = new OperacionListView(op, false);
            listaOperacionesListView.add(operacionListView);
        }
        if (listaOperaciones.isEmpty()){
            lbSeleccionarOperaciones.setVisibility(View.INVISIBLE);
            txtNoHayOperaciones.setVisibility(View.VISIBLE);
            txtNoHayOperaciones.setText(getString(R.string.txt_no_hay_operaciones));
        }
        final ArrayList<Operacion> listaOperacionesFinal = new ArrayList<Operacion>();
        listaOperacionesFinal.addAll(listaOperaciones);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new AdaptadorOperaciones(this,
                R.layout.list_operaciones, listaOperacionesListView);
        final ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
//                OperacionListView operacion = (OperacionListView) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + operacion.getOperacion().getNombre(),
//                        Toast.LENGTH_LONG).show();
                Operacion operacion = listaOperacionesFinal.get(position);
                Intent intent = new Intent(getApplication(), FrmOperacion.class);
                String idOperacion = operacion.getIdOperacion().toString().trim();
                intent.putExtra("idMantenimiento", idMantenimiento);
                intent.putExtra("idOperacion", idOperacion);
                startActivity(intent);
            }
        });

    }


    private class AdaptadorOperaciones extends ArrayAdapter<OperacionListView> {

        private ArrayList<OperacionListView> listaOperaciones;

        public AdaptadorOperaciones(Context context, int textViewResourceId,
                                    ArrayList<OperacionListView> listaOperaciones) {
            super(context, textViewResourceId, listaOperaciones);
            this.listaOperaciones = new ArrayList<OperacionListView>();
            this.listaOperaciones.addAll(listaOperaciones);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_operaciones, parent, false);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        OperacionListView operacion = (OperacionListView) cb.getTag();
//                        Toast.makeText(getApplicationContext(),
//                                operacion.getOperacion().getNombre() + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
                        operacion.setSeleccionado(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OperacionListView operacion = listaOperaciones.get(position);
            holder.code.setText(operacion.getOperacion().getNombre());
            //  holder.name.setText(operacion.getOperacion().getNombre());
            holder.name.setChecked(operacion.isSeleccionado());
            holder.name.setTag(operacion);

            return convertView;

        }


    }


    private void checkButtonClick() {


//        Button myButton = (Button) findViewById(R.id.findSelected);
//        myButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<OperacionListView> countryList = dataAdapter.listaOperaciones;
//                for(int i=0;i<countryList.size();i++){
//                    OperacionListView operacionListView = countryList.get(i);
//                    if(operacionListView.isSeleccionado()){
//                        responseText.append("\n" + operacionListView.getOperacion().getNombre());
//                    }
//                }
//
//                Toast.makeText(getApplicationContext(),
//                        responseText, Toast.LENGTH_LONG).show();
//
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_operaciones_mantenimiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(this, FrmOperacion.class);
                intent.putExtra("idMantenimiento",idMantenimiento);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case R.id.action_save:

//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
                ArrayList<Integer> listaIdOperacionesSeleccionadas = new ArrayList<Integer>();
                ArrayList<OperacionListView> listaOper = dataAdapter.listaOperaciones;
                for(int i=0;i<listaOper.size();i++) {
                    OperacionListView operacionListView = listaOper.get(i);
                    if (operacionListView.isSeleccionado()) {
//                        responseText.append("\n" + operacionListView.getOperacion().getNombre());
                        listaIdOperacionesSeleccionadas.add(operacionListView.getOperacion().getIdOperacion());
                    }
                }

                Intent intentSave = new Intent(this, LstMantenimientoOperaciones.class);
                Bundle bSave = new Bundle();
                bSave.putIntegerArrayList("idOperaciones",listaIdOperacionesSeleccionadas);
                bSave.putString("Origen", "LstOperaciones");
                bSave.putInt("idMantenimiento", idMantenimiento);
                intentSave.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentSave.putExtras(bSave);
                startActivity(intentSave);
                return true;

            case android.R.id.home:
                super.onBackPressed();

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public static class DialogoSalir extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //Recuperamos los dos valores que indicarán que fragment debebemos cargar en el mainActivity
            String origenEnviado;
            if (null != getArguments().getString("Origen")) {
                origenEnviado =getArguments().getString("Origen");
            }else{
                origenEnviado="";
            }
            final String origen = origenEnviado;


            builder.setMessage(R.string.dialogo_salir_sin_guardar_operaciones_seleccionadas)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Intent intent = null;

                                    if (origen == "FrmMantenimiento") {
                                        intent = new Intent(getActivity(), FrmMantenimiento.class);

                                    } else if(origen == "Frm") {
                                        //TODO otra posibilidad
                                        intent = new Intent(getActivity(), FrmMantenimiento.class);

                                    }
                                    else{//por defecto
                                        intent = new Intent(getActivity(), FrmMantenimiento.class);
                                    }

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            }

                    )
                    .

                            setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    }

                            );
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
