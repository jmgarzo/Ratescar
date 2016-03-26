package com.jmgarzo.ratescar;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.bbdd.BBDDOperacionesRecambios;
import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.objects.OperacionRecambio;
import com.jmgarzo.objects.Recambio;
import com.jmgarzo.objects.RecambioListView;

import java.util.ArrayList;


public class LstRecambios extends Activity {

    private TextView lbNoHayRecambios, lbSeleccionarRecambios;

    private Integer idOperacion;

    private AdaptadorRecambios dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_recambios);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        lbNoHayRecambios = (TextView) findViewById(R.id.lbNoHayRecambios);
        lbNoHayRecambios.setVisibility(View.INVISIBLE);

        lbSeleccionarRecambios = (TextView) findViewById(R.id.lbSeleccionarRecambios);

        idOperacion = getIntent().getIntExtra("idOperacion",-1);


        displayListView();






    }



    private void displayListView() {
        BBDDOperaciones bbddOperaciones = new BBDDOperaciones(this);

        BBDDRecambios bbddRecambios = new BBDDRecambios(this);

        //Array list of countries
        final ArrayList<Recambio> listaRecambios = bbddRecambios.getRecambiosPorOrdenAlfabetico();
        //Eliminamos las operaciones que ya estén seleccionadas para ese mantenimiento
        BBDDOperacionesRecambios bbddOperacionesRecambios = new BBDDOperacionesRecambios(this);
        ArrayList<OperacionRecambio> recambiosEnOperacion =bbddOperacionesRecambios.getOperacionesRecambiosByIdOperacion(idOperacion.toString());
        ArrayList <Integer> indicesABorrar = new ArrayList<Integer>();

        for(OperacionRecambio opRec : recambiosEnOperacion){
            for(Recambio re :listaRecambios){
                if(opRec.getIdRecambio().intValue() == re.getIdRecambio().intValue()){
                    listaRecambios.remove(re);
                    break;
                }
            }
        }
        //Eliminamos las operaciones que ya estén seleccionadas para ese mantenimiento


        ArrayList<RecambioListView> listaRecambiosListView = new ArrayList<RecambioListView>();

       for (Recambio rec : listaRecambios) {

            RecambioListView recambioListView = new RecambioListView(rec, false);
            listaRecambiosListView.add(recambioListView);
        }
        if (listaRecambios.isEmpty()){
            lbSeleccionarRecambios.setVisibility(View.INVISIBLE);
            lbNoHayRecambios.setVisibility(View.VISIBLE);
            lbNoHayRecambios.setText(getString(R.string.lb_no_hay_operaciones));
        }
        final ArrayList<Recambio> listaRecambiosFinal = new ArrayList<Recambio>();
        listaRecambiosFinal.addAll(listaRecambios);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new AdaptadorRecambios(this,
                R.layout.list_operaciones, listaRecambiosListView);
        final ListView listView = (ListView) findViewById(R.id.listViewRecambios);
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
                Recambio recambio = listaRecambiosFinal.get(position);
                Intent intent = new Intent(getApplication(), FrmRecambio.class);
                String idRecambio = recambio.getIdRecambio().toString().trim();
                intent.putExtra("idOperacion", idOperacion);
                intent.putExtra("idRecambio", idRecambio);
                startActivity(intent);
            }
        });

    }


    private class AdaptadorRecambios extends ArrayAdapter<RecambioListView> {

        private ArrayList<RecambioListView> listaRecambios;

        public AdaptadorRecambios(Context context, int textViewResourceId,
                                    ArrayList<RecambioListView> listaRecambios) {
            super(context, textViewResourceId, listaRecambios);
            this.listaRecambios = new ArrayList<RecambioListView>();
            this.listaRecambios.addAll(listaRecambios);
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
                        RecambioListView recambio = (RecambioListView) cb.getTag();
//                        Toast.makeText(getApplicationContext(),
//                                operacion.getOperacion().getNombre() + cb.getText() +
//                                        " is " + cb.isChecked(),
//                                Toast.LENGTH_LONG).show();
                        recambio.setSeleccionado(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RecambioListView recambio = listaRecambios.get(position);
            holder.code.setText(recambio.getRecambio().getNombre());
            //  holder.name.setText(operacion.getOperacion().getNombre());
            holder.name.setChecked(recambio.isSeleccionado());
            holder.name.setTag(recambio);

            return convertView;

        }


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lst_recambios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(this, FrmRecambio.class);
                intent.putExtra("idOperacion",idOperacion);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case R.id.action_save:

//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
                ArrayList<Integer> listaIdRecambiosSeleccionados = new ArrayList<Integer>();
                ArrayList<RecambioListView> listaRec = dataAdapter.listaRecambios;
                for(int i=0;i<listaRec.size();i++) {
                    RecambioListView recambioListView = listaRec.get(i);
                    if (recambioListView.isSeleccionado()) {
//                        responseText.append("\n" + operacionListView.getOperacion().getNombre());
                        listaIdRecambiosSeleccionados.add(recambioListView.getRecambio().getIdRecambio());
                    }
                }

                Intent intentSave = new Intent(this, LstOperacionesRecambios.class);
                Bundle bSave = new Bundle();
                bSave.putIntegerArrayList("idRecambios",listaIdRecambiosSeleccionados);
                bSave.putString("Origen", "LstRecambios");
                bSave.putInt("idOperacion", idOperacion);
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
}
