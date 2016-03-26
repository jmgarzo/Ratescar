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
import com.jmgarzo.bbdd.BBDDCategorias;
import com.jmgarzo.bbdd.BBDDOperaciones;
import com.jmgarzo.bbdd.BBDDOperacionesRecambios;
import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.bbdd.BBDDSubcategorias;
import com.jmgarzo.objects.Categoria;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Operacion;
import com.jmgarzo.objects.OperacionRecambio;
import com.jmgarzo.objects.Recambio;
import com.jmgarzo.objects.Subcategoria;

import java.math.BigDecimal;
import java.util.ArrayList;


public class LstOperacionesRecambios extends Activity {

    private Integer idOperacion;

    private ArrayList<OperacionRecambio> listaOperacionRecambios;

    private AdView adView;

    private ListView lstOperacionesRecambios;
    private BBDDOperaciones bbddOperaciones;
    private BBDDRecambios bbddRecambios;
    private BBDDOperacionesRecambios bbddOperacionesRecambios;
    private BBDDSubcategorias bbddSubcategorias;
    private BBDDCategorias bbddCategorias;

    private String origen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lst_operaciones_recambios);

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


        idOperacion = getIntent().getIntExtra("idOperacion", -1);
        origen = getIntent().getStringExtra("Origen");


        lstOperacionesRecambios = (ListView) findViewById(R.id.lstOperacionRecamios);

        bbddOperacionesRecambios = new BBDDOperacionesRecambios(this);
        bbddOperaciones = new BBDDOperaciones(this);
        bbddRecambios = new BBDDRecambios(this);
        bbddSubcategorias = new BBDDSubcategorias(this);
        bbddCategorias = new BBDDCategorias(this);

        listaOperacionRecambios = null;

        if (null != origen) {
            if (origen.equals("FrmMantenimientoOperacion")) {
                listaOperacionRecambios = bbddOperacionesRecambios.getOperacionesRecambiosByIdOperacion(idOperacion.toString());
            } else if (origen.equals("LstRecambios")) {
                if(null != getIntent().getIntegerArrayListExtra("idRecambios")) {
                    ArrayList<Integer> idRecambios= new ArrayList<Integer>();
                    idRecambios.addAll(getIntent().getIntegerArrayListExtra("idRecambios"));
                    guardarOperacionesRecambio(idOperacion,idRecambios);
                    listaOperacionRecambios = bbddOperacionesRecambios.getOperacionesRecambiosByIdOperacion(idOperacion.toString());

                }
            }else if(origen.equals("FrmOperacionRecambio")){
                listaOperacionRecambios = bbddOperacionesRecambios.getOperacionesRecambiosByIdOperacion(idOperacion.toString());

            }




            AdaptadorOperacionesRecambios adaptador = new AdaptadorOperacionesRecambios(this);
            lstOperacionesRecambios.setAdapter(adaptador);
            lstOperacionesRecambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OperacionRecambio operacionRecambio = listaOperacionRecambios.get(position);
                    Intent intent = new Intent(LstOperacionesRecambios.this, FrmOperacionRecambio.class);
                    Integer idOperacion = operacionRecambio.getIdOperacion();
                    Integer idRecambio = operacionRecambio.getIdRecambio();
                    intent.putExtra("idOperacion", idOperacion);
                    intent.putExtra("idRecambio", idRecambio);
                    startActivity(intent);
                }
            });


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lst_operaciones_recambios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:

                Intent intent = new Intent(this, LstRecambios.class);
                intent.putExtra("idOperacion", idOperacion);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            case android.R.id.home:
                super.onBackPressed();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    class AdaptadorOperacionesRecambios extends ArrayAdapter<OperacionRecambio> {

        Activity context;

        AdaptadorOperacionesRecambios(Activity context) {
            super(context, R.layout.list_operaciones_recambios, listaOperacionRecambios);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_operaciones_recambios, null);

                holder = new ViewHolder();
                holder.txtMarca = (TextView) item.findViewById(R.id.lbMarcaOperacionRecambio);
                holder.txtNombre = (TextView) item.findViewById(R.id.lbNombreOperacionRecambio);
                holder.txtCoste = (TextView) item.findViewById(R.id.lbCosteOperacionRecambio);
                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idOperacion = listaOperacionRecambios.get(position).getIdOperacion()
                    .toString().trim();
            String idRecambio = listaOperacionRecambios.get(position).getIdRecambio().toString();
            OperacionRecambio operacionRecambio = bbddOperacionesRecambios.getOperacionRecambiosById(idOperacion, idRecambio);
            Recambio recambio = bbddRecambios.getRecambio(idRecambio);

//            Subcategoria subcategoria = bbddSubcategorias.getSubcategoriaById(recambio.getIdSubcategoria().toString());
//            Categoria categoria = bbddCategorias.getCategoriaById(subcategoria.getIdCategoria().toString());


            if (null != operacionRecambio) {

//                if (null != categoria) {
//                    holder.txtCategoria.setText(categoria.getNombre());
//                }

                if (null != recambio) {
                    holder.txtNombre.setText(recambio.getNombre());
                    holder.txtMarca.setText(recambio.getFabricante());
                }
                holder.txtCoste.setText(operacionRecambio.getCoste().setScale(3, BigDecimal.ROUND_HALF_UP).toString());

            }

            return (item);
        }
    }


    static class ViewHolder {
        //ImageView icono;
        TextView txtMarca;
        TextView txtNombre;
        TextView txtCoste;

    }


    private boolean guardarOperacionesRecambio(Integer idOperacion, ArrayList<Integer> idRecambios) {
        boolean esCorrecto = true;
        for (Integer idRecambio : idRecambios) {
            if (bbddOperacionesRecambios.nuevoOperacionRecambio(idOperacion, idRecambio) == -1) {
                esCorrecto = false;

            }
        }
        return esCorrecto;
    }

}
