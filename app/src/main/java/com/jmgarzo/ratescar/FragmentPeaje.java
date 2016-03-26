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
import com.jmgarzo.bbdd.BBDDPeajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Peaje;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FragmentPeaje extends Fragment {
    private AdView adView;
    private ListView lstPeajes;
    private ArrayList<Peaje> listaPeajes;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_peaje, container, false);
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

        getActivity().setTitle(R.string.title_activity_fragment_peajes);

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());

        lstPeajes = (ListView) getActivity().findViewById(R.id.lstPeajes);

        BBDDPeajes bbddPeajes = new BBDDPeajes(getActivity());
        listaPeajes = new ArrayList<Peaje>();
        listaPeajes = bbddPeajes.getPeajesPorFecha();

        AdaptadorPeajes adaptadorPeajes = new AdaptadorPeajes(getActivity());

        lstPeajes.setAdapter(adaptadorPeajes);
        lstPeajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Peaje peaje = listaPeajes.get(position);
                Intent intent = new Intent(getActivity(), FrmPeaje.class);
                String idPeaje = peaje.getIdPeaje().toString().trim();
                intent.putExtra("idPeaje", idPeaje);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_peaje, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmPeaje.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorPeajes extends ArrayAdapter<Peaje> {
        Activity context;

        AdaptadorPeajes(Activity context) {
            super(context, R.layout.list_peajes, listaPeajes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_peajes, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIconoPeaje);
                holder.fecha = (TextView) item.findViewById(R.id.lbFechaPeaje);
                holder.coche = (TextView) item.findViewById(R.id.lbCochePeaje);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatriculaPeaje);
                holder.precio = (TextView) item.findViewById(R.id.lbPrecioPeaje);
                holder.unidadMoneda = (TextView) item.findViewById(R.id.lbUnidadMonedaPeaje);
                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idPeaje = listaPeajes.get(position).getIdPeaje().toString().trim();

            String idCoche = listaPeajes.get(position).getIdCoche().toString().trim();

            Coche coche = bbddCoches.getCoche(idCoche);

            if (null != coche) {
                holder.icono.setImageResource(R.drawable.ic_peaje);

                Calendar cal = new GregorianCalendar();
                java.util.Date peajeDate = new Date(listaPeajes.get(position).getFecha());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formatteDate = df.format(peajeDate);
                holder.fecha.setText(formatteDate);

                holder.coche.setText(coche.getNombre());
                holder.matricula.setText(coche.getMatricula());

                holder.precio.setText(listaPeajes.get(position).getPrecio().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                holder.unidadMoneda.setText(" ".concat(bbddAjustesAplicacion.getValorMoneda()));
            }

            return (item);
        }

    }

    static class ViewHolder {
        ImageView icono;
        TextView fecha;
        TextView coche;
        TextView matricula;
        TextView precio;
        TextView unidadMoneda;
    }


}
