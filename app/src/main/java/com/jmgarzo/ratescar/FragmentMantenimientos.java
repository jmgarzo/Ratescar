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
import com.jmgarzo.bbdd.BBDDMantenimientos;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Mantenimiento;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class FragmentMantenimientos extends Fragment {

    private AdView adView;
    private ListView lstMantenimientos;
    private ArrayList<Mantenimiento> listaMantenimientos;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_mantenimientos, container, false);
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


        //para mantener el título cuando se gira el movil
        getActivity().setTitle(getString(R.string.title_activity_fragment_mantenimientos));

        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());


        lstMantenimientos = (ListView) getActivity().findViewById(R.id.lstMantenimientos);
        BBDDMantenimientos bbddMantenimientos = new BBDDMantenimientos(getActivity());
        listaMantenimientos = new ArrayList<Mantenimiento>();
        listaMantenimientos = bbddMantenimientos.getMantenimientosOrdenadorPorFechaDesc();


        AdaptadorMantenimientos adaptador = new AdaptadorMantenimientos(getActivity());
        lstMantenimientos.setAdapter(adaptador);
        lstMantenimientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mantenimiento mantenimiento = listaMantenimientos.get(position);
                Intent intent = new Intent(getActivity(), FrmMantenimiento.class);
                int idMantenimiento = mantenimiento.getIdMantenimiento();
                intent.putExtra("idMantenimiento", idMantenimiento);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_mantenimientos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmMantenimiento.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class AdaptadorMantenimientos extends ArrayAdapter<Mantenimiento> {

        Activity context;

        AdaptadorMantenimientos(Activity context) {
            super(context, R.layout.list_mantenimientos, listaMantenimientos);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDMantenimientos bbddMantenimientos = new BBDDMantenimientos(context);
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_mantenimientos, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIcono);
                holder.nombreCoche = (TextView) item.findViewById(R.id.lbNombreCocheList);
                holder.fechaMantenimiento = (TextView) item.findViewById(R.id.lbFechaMantemientoList);
                holder.kmMantenimiento = (TextView) item.findViewById(R.id.lbKmMantemientoList);
                holder.unidadDistancia = (TextView) item.findViewById(R.id.lbUnidadDistanciaList);
                holder.costeFinal = (TextView) item.findViewById(R.id.lbCosteFinalList);
                holder.moneda = (TextView) item.findViewById(R.id.lbMonedaList);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idMantenimiento = listaMantenimientos.get(position).getIdMantenimiento()
                    .toString().trim();

            Mantenimiento mantenimiento = bbddMantenimientos.getMantenimiento(idMantenimiento);

            if (null != mantenimiento) {
                holder.icono.setImageResource(R.drawable.ic_senal_taller);
                Coche coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());
                String nombre = "";
                if (null != coche.getNombre() && !coche.getNombre().equals("")) {
                    nombre = coche.getNombre();
                } else if (null != coche.getMatricula() && !"".equals(coche.getMatricula())) {
                    nombre = coche.getMatricula();
                }
                if (nombre.length() > 15) {
                    nombre = nombre.substring(0, 15);
                }
                holder.nombreCoche.setText(nombre);



                //TODO poner la fecha bien

                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(listaMantenimientos.get(position).getFechaMantenimiento());
                //java.util.Date fecha = new Date(listaRepostajes.get(position).getFechaRespostaje());
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formatteDate = df.format(cal.getTime());
                holder.fechaMantenimiento.setText(formatteDate);

                holder.kmMantenimiento.setText(ToolsConversiones.RedondearSinDecimales(getDistanciaMantenimiento(mantenimiento)).toString());
                holder.unidadDistancia.setText(" ".concat(getUnidadDistancia()));
                holder.costeFinal.setText(mantenimiento.getCosteFinal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());

                holder.moneda.setText(" " + bbddAjustesAplicacion.getValorMoneda());

            }

            return (item);
        }
    }


    private String getUnidadDistancia() {
        String unidad = "";
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            unidad = getString(R.string.unidad_distancia_km);
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            unidad = getString(R.string.unidad_distancia_millas);
        }
        return unidad;
    }


    private BigDecimal getDistanciaMantenimiento(Mantenimiento mantenimiento) {
        BigDecimal distancia = BigDecimal.ZERO;
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            distancia = mantenimiento.getKmMatenimiento();
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            distancia = ToolsConversiones.kmToMillas(mantenimiento.getKmMatenimiento());
        }
        return distancia;
    }

    static class ViewHolder {
        ImageView icono;
        TextView nombreCoche;
        TextView fechaMantenimiento;
        TextView kmMantenimiento;
        TextView unidadDistancia;
        TextView costeFinal;
        TextView moneda;

    }


}
