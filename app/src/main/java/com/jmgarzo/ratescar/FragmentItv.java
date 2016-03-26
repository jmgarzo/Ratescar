package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDItvs;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Itv;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FragmentItv extends Fragment {

    private AdView adView;
    private ListView lstItvs;
    private ArrayList<Itv> listaItvs;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_itv, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**PUBLICIDAD**/

        adView = new AdView(getActivity());
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.banner);

        linearLayout.addView(adView);

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
//                .build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/


        getActivity().setTitle(R.string.title_activity_fragment_itvs);

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());

        lstItvs = (ListView) getActivity().findViewById(R.id.lstItvs);

        BBDDItvs bbddItvs = new BBDDItvs(getActivity());
        listaItvs = new ArrayList<Itv>();
        listaItvs = bbddItvs.getItvsPorFechaDesc();

        AdaptadorItvs adaptadorItvs = new AdaptadorItvs(getActivity());

        lstItvs.setAdapter(adaptadorItvs);
        lstItvs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Itv itv = listaItvs.get(position);
                Intent intent = new Intent(getActivity(), FrmItv.class);
                String idItv = itv.getIdItv().toString().trim();
                intent.putExtra("idItv", idItv);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_itv, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmItv.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorItvs extends ArrayAdapter<Itv> {
        Activity context;

        AdaptadorItvs(Activity context) {
            super(context, R.layout.list_itvs, listaItvs);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_itvs, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIconoItv);
                holder.coche = (TextView) item.findViewById(R.id.lbCocheItv);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatriculaItv);
                holder.fecha = (TextView) item.findViewById(R.id.lbFechaItv);
                holder.resultado = (TextView) item.findViewById(R.id.lbResultadoItv);
                holder.precio = (TextView) item.findViewById(R.id.lbPrecioItv);
                holder.unidadMoneda = (TextView) item.findViewById(R.id.lbUnidadMonedaItv);
                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idItv = listaItvs.get(position).getIdItv().toString().trim();

            String idCoche = listaItvs.get(position).getIdCoche().toString().trim();

            Coche coche = bbddCoches.getCoche(idCoche);

            if (null != coche) {
                holder.icono.setImageResource(R.drawable.ic_itv);

                String nombre = "";
                if (null != coche.getNombre() && !"".equals(coche.getNombre())) {
                    if (coche.getNombre().length() > 20) {
                        nombre = coche.getNombre().substring(0, 20).concat(" / ");
                    } else {
                        nombre = coche.getNombre().concat(" / ");
                    }
                } else {
                    holder.coche.setEnabled(false);
                    holder.coche.setVisibility(View.INVISIBLE);
                }

                holder.coche.setText(nombre);

                String matricula = "";
                if (null != coche.getMatricula()) {
                    if (coche.getMatricula().length() > 15) {
                        matricula = coche.getMatricula().substring(0, 15);
                    } else {
                        matricula = coche.getMatricula();
                    }
                }
                holder.matricula.setText(matricula);


                Calendar cal = new GregorianCalendar();
                java.util.Date itvDate = new Date(listaItvs.get(position).getFechaItv());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formatteDate = df.format(itvDate);
                holder.fecha.setText(formatteDate);
                String resultado = "";
                if (listaItvs.get(position).getResultado().trim().length() > 15) {
                    resultado = listaItvs.get(position).getResultado().trim().substring(0, 15);
                } else {
                    resultado = listaItvs.get(position).getResultado().trim();
                }
                holder.resultado.setText(resultado);
                holder.precio.setText(listaItvs.get(position).getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                holder.unidadMoneda.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));
            }

            return (item);
        }

    }

    static class ViewHolder {
        ImageView icono;
        TextView coche;
        TextView matricula;
        TextView fecha;
        TextView resultado;
        TextView precio;
        TextView unidadMoneda;
    }


}
