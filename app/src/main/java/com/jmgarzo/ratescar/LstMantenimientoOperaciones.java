package com.jmgarzo.ratescar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDMantenimientoOperacion;
import com.jmgarzo.bbdd.BBDDMantenimientos;
import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.MantenimientoOperacion;
import com.jmgarzo.objects.Operacion;

import java.math.BigDecimal;
import java.util.ArrayList;


public class LstMantenimientoOperaciones extends Activity {

    private String origen;
    private Integer idMantenimiento;
    private ArrayList<MantenimientoOperacion> listaMantenimientoOperacion;

    private AdView adView;
    private ListView lstMantenimientoOperaciones;
    private BBDDMantenimientoOperacion bbddMantenimientoOperacion;
    private BBDDMantenimientos bbddMantenimientos;
    private BBDDOperaciones bbddOperaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_operaciones_mantenimiento);
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


        idMantenimiento = getIntent().getIntExtra("idMantenimiento", -1);

        lstMantenimientoOperaciones = (ListView) findViewById(R.id.lstMantenimientoOperacion);

        bbddMantenimientoOperacion = new BBDDMantenimientoOperacion(this);
        bbddMantenimientos = new BBDDMantenimientos(this);
        bbddOperaciones = new BBDDOperaciones(this);

        ArrayList<Integer> idOperaciones = new ArrayList<Integer>();
        origen = getIntent().getStringExtra("Origen");
        listaMantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperaciones();

        if (null != origen) {
            if (origen.equals("LstOperaciones")) {
                if (null != getIntent().getIntegerArrayListExtra("idOperaciones")) {
                    idOperaciones.addAll(getIntent().getIntegerArrayListExtra("idOperaciones"));
                    //Integer idUltimoMantenimiento = bbddMantenimientos.getUltimoMantenimiento().getIdMantenimiento();
                    guardarMantenimientoOperaciones(idMantenimiento, idOperaciones);
                    listaMantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperacionesByIdMantenimiento(idMantenimiento.toString());


                }
            } else if (origen.equals("FrmMantenimientoOperacion")) {
                listaMantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperacionesByIdMantenimiento(idMantenimiento.toString());

            } else if (origen.equals("FrmMantenimiento")) {
                listaMantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperacionesByIdMantenimiento(idMantenimiento.toString());

            }

        }


        AdaptadorMantenimientoOperacion adaptador = new AdaptadorMantenimientoOperacion(this);
        lstMantenimientoOperaciones.setAdapter(adaptador);
        lstMantenimientoOperaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MantenimientoOperacion mantenimientoOperacion = listaMantenimientoOperacion.get(position);
                Intent intent = new Intent(LstMantenimientoOperaciones.this, FrmMantenimientoOperacion.class);
                Integer idMantenimiento = mantenimientoOperacion.getIdMantenimiento();
                Integer idOperacion = mantenimientoOperacion.getIdOperacion();
                intent.putExtra("idMantenimiento", idMantenimiento);
                intent.putExtra("idOperacion", idOperacion);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lst_operaciones_mantenimiento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:

                Intent intent = new Intent(this, LstOperaciones.class);
                intent.putExtra("idMantenimiento",idMantenimiento);
                startActivity(intent);
                return true;

            case android.R.id.home:
                super.onBackPressed();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class AdaptadorMantenimientoOperacion extends ArrayAdapter<MantenimientoOperacion> {

        Activity context;

        AdaptadorMantenimientoOperacion(Activity context) {
            super(context, R.layout.list_operaciones_mantenimiento, listaMantenimientoOperacion);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_operaciones_mantenimiento, null);

                holder = new ViewHolder();
                holder.txtNombreMantenimientoOperacion = (TextView) item.findViewById(R.id.lbNombreMantenimientoOperacion);
                holder.txtCosteMantenimientoOperacion = (TextView) item.findViewById(R.id.lbCosteMantenimientoOperacion);
                holder.txtPeriodicidadDigito = (TextView) item.findViewById(R.id.lbPeriodicidadDigito);
                holder.txtPeriodicidadUnidad = (TextView) item.findViewById(R.id.lbPeriodicidadUnidad);
                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idMantenimiento = listaMantenimientoOperacion.get(position).getIdMantenimiento()
                    .toString().trim();
            String idOperacion = listaMantenimientoOperacion.get(position).getIdOperacion().toString();

            MantenimientoOperacion mantenimientoOperacion = bbddMantenimientoOperacion.getMantenimientoOperacion(idMantenimiento, idOperacion);
            // Mantenimiento mantenimiento = bbddMantenimientos.getMantenimiento(mantenimientoOperacion.getIdMantenimiento().toString());
            Operacion operacion = bbddOperaciones.getOperacion(mantenimientoOperacion.getIdOperacion().toString());

            if (null != mantenimientoOperacion) {

                if (null != operacion) {
                    holder.txtNombreMantenimientoOperacion.setText(operacion.getNombre());
                }
                holder.txtCosteMantenimientoOperacion.setText(mantenimientoOperacion.getCoste().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                holder.txtPeriodicidadDigito.setText(mantenimientoOperacion.getPeriodicidadDigito().toString());
                holder.txtPeriodicidadUnidad.setText(mantenimientoOperacion.getPeriodicidadUnidad());
            }

            return (item);
        }
    }


    static class ViewHolder {
        //ImageView icono;
        TextView txtNombreMantenimientoOperacion;
        TextView txtCosteMantenimientoOperacion;
        TextView txtPeriodicidadDigito;
        TextView txtPeriodicidadUnidad;

    }


    private boolean guardarMantenimientoOperaciones(Integer idMantenimiento, ArrayList<Integer> idOperaciones) {
        boolean esCorrecto = true;
        for (Integer idOperacion : idOperaciones) {
            if (bbddMantenimientoOperacion.nuevoMantenimientoOperacion(idMantenimiento, idOperacion) == -1) {
                esCorrecto = false;
            }
        }
        return esCorrecto;
    }
}
