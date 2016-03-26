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
import com.jmgarzo.bbdd.BBDDImpuestosCirculacion;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.ImpuestoCirculacion;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FragmentImpuestoCirculacion extends Fragment {
    private AdView adView;
    private ListView lstImpuestos;
    private ArrayList<ImpuestoCirculacion> listaImpuestos;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_impuesto_circulacion, container, false);
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

        getActivity().setTitle(getString(R.string.title_activity_fragment_impuesto_circulacion));

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());

        lstImpuestos = (ListView) getActivity().findViewById(R.id.lstImpuestosCirculacion);

        BBDDImpuestosCirculacion bbddImpuestosCirculacion = new BBDDImpuestosCirculacion(getActivity());
        listaImpuestos = new ArrayList<ImpuestoCirculacion>();
        listaImpuestos = bbddImpuestosCirculacion.getImpuestosPorFechaDesc();

        AdaptadorImpuesto adaptadorImpuesto = new AdaptadorImpuesto(getActivity());

        lstImpuestos.setAdapter(adaptadorImpuesto);
        lstImpuestos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImpuestoCirculacion impuestoCirculacion = listaImpuestos.get(position);
                Intent intent = new Intent(getActivity(), FrmImpuestoCirculacion.class);
                String idImpuestoCirculacion = impuestoCirculacion.getIdImpuesto().toString().trim();
                intent.putExtra("idImpuestoCirculacion", idImpuestoCirculacion);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_impuesto_circulacion, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmImpuestoCirculacion.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorImpuesto extends ArrayAdapter<ImpuestoCirculacion> {
        Activity context;

        AdaptadorImpuesto(Activity context) {
            super(context, R.layout.list_impuesto_circulacion, listaImpuestos);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_impuesto_circulacion, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIconoImpuestoCirculacion);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatriculaImpuesto);
                holder.anualidad = (TextView) item.findViewById(R.id.lbAnualidadImpuesto);
                holder.importe = (TextView) item.findViewById(R.id.lbImporteImpuestoCirculacion);
                holder.unidadMoneda = (TextView) item.findViewById(R.id.lbUnidadMonedaImpuestoCirculacion);
                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idImpuesto = listaImpuestos.get(position).getIdImpuesto().toString().trim();

            String idCoche = listaImpuestos.get(position).getIdCoche().toString().trim();

            Coche coche = bbddCoches.getCoche(idCoche);

            if (null != coche) {
                holder.icono.setImageResource(R.drawable.ic_impuestos);


                if (!Constantes.esVacio(coche.getMatricula())) {
                    String matricula = "";
                    if (null != coche.getMatricula() && !"".equals(coche.getMatricula())) {
                        if (coche.getMatricula().length() > 20) {
                            matricula = coche.getMatricula().substring(0, 20);
                        } else {
                            matricula = coche.getMatricula();
                        }
                        holder.matricula.setText(matricula);
                    }
                } else {
                    String nombre = "";
                    if (null != coche.getNombre() && !"".equals(coche.getNombre())) {
                        if (coche.getNombre().length() > 20) {
                            nombre = coche.getNombre().substring(0, 20);
                        } else {
                            nombre = coche.getNombre();
                        }
                        holder.matricula.setText(nombre);

                    }
                }


                Calendar cal = new GregorianCalendar();
                java.util.Date anualidad = new Date(listaImpuestos.get(position).getAnualidad());
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                String formatteDate = df.format(anualidad);
                holder.anualidad.setText(formatteDate);

                holder.importe.setText(listaImpuestos.get(position).getImporte().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                holder.unidadMoneda.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));
            }

            return (item);
        }
    }

    static class ViewHolder {
        ImageView icono;
        TextView matricula;
        TextView anualidad;
        TextView importe;
        TextView unidadMoneda;
    }

}



