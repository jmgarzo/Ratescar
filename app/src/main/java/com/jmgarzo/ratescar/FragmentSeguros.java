package com.jmgarzo.ratescar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDSeguros;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Seguro;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FragmentSeguros extends Fragment {

    private AdView adView;
    private ListView lstSeguros;
    private ArrayList<Seguro> listaSeguros;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_seguros, container, false);
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

        getActivity().setTitle(R.string.title_activity_fragment_seguros);

        lstSeguros = (ListView) getActivity().findViewById(R.id.lstSeguros);

        BBDDSeguros bbddSeguros = new BBDDSeguros(getActivity());
        listaSeguros = new ArrayList<Seguro>();
        listaSeguros = bbddSeguros.getSegurosPorFecha();
        AdaptadorSeguros adaptadorSeguros = new AdaptadorSeguros(getActivity());

        lstSeguros.setAdapter(adaptadorSeguros);
        lstSeguros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Seguro seguro = listaSeguros.get(position);
                Intent intent = new Intent(getActivity(), FrmSeguro.class);
                String idSeguro = seguro.getIdSeguro().toString().trim();
                intent.putExtra("idSeguro", idSeguro);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_seguros, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmSeguro.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorSeguros extends ArrayAdapter<Seguro> {
        Activity context;

        AdaptadorSeguros(Activity context) {
            super(context, R.layout.list_seguros, listaSeguros);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_seguros, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIconoSeguro);
                holder.coche = (TextView) item.findViewById(R.id.lbCocheSeguro);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatriculaSeguro);
                holder.fechaInicio = (TextView) item.findViewById(R.id.lbFechaInicio);
                holder.separador = (TextView) item.findViewById(R.id.lbSeparador);
                holder.fechaFin = (TextView) item.findViewById(R.id.lbFechaFin);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idSeguro = listaSeguros.get(position).getIdSeguro().toString().trim();

            String idCoche = listaSeguros.get(position).getIdCoche().toString().trim();

            Coche coche = bbddCoches.getCoche(idCoche);

            if (null != coche) {
                holder.icono.setImageResource(R.drawable.ic_seguro);
                holder.coche.setText(coche.getNombre());
                holder.matricula.setText(coche.getMatricula());


                Calendar cal = new GregorianCalendar();
                java.util.Date SeguroDateInicio = new Date(listaSeguros.get(position).getFechaInicio());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formatteDate = df.format(SeguroDateInicio);
                holder.fechaInicio.setText(formatteDate);

                holder.separador.setText(getText(R.string.lb_separador_fecha));


                cal = new GregorianCalendar();
                java.util.Date SeguroDateFin = new Date(listaSeguros.get(position).getFechaVencimiento());
                df = new SimpleDateFormat("dd/MM/yyyy");
                formatteDate = df.format(SeguroDateFin);
                holder.fechaFin.setText(formatteDate);
            }

            return (item);
        }

    }

    static class ViewHolder {
        ImageView icono;
        TextView coche;
        TextView matricula;
        TextView fechaInicio;
        TextView separador;
        TextView fechaFin;
    }


}
