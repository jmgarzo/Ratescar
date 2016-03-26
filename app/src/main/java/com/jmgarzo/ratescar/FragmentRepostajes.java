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
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FragmentRepostajes extends Fragment {

    private AdView adView;
    private ListView lstRepostajes;
    private ArrayList<Repostaje> listaRepostajes;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_repostajes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //para mantener el título cuando se gira el movil
        getActivity().setTitle(getString(R.string.title_activity_fragment_repostajes));
        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());

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

        lstRepostajes = (ListView) getActivity().findViewById(R.id.lstRespostajes);

        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(getActivity());
        listaRepostajes = new ArrayList<Repostaje>();
        listaRepostajes = bbddRepostajes.getTodosLosRepostajesOrdenadosPorFecheDesc();

        ajustarListaAUnidades(listaRepostajes);

        AdaptadorRepostajes adaptador = new AdaptadorRepostajes(getActivity());

        lstRepostajes.setAdapter(adaptador);

        lstRepostajes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Repostaje repostaje = listaRepostajes.get(position);
                Intent intent = new Intent(getActivity(), FrmRepostaje.class);
                String idRepostaje = repostaje.getIdRepostaje().toString().trim();
                intent.putExtra("idRepostaje", idRepostaje);
                startActivity(intent);

//                Repostaje repostaje = listaRepostajes.get(position);
//                Intent intent = new Intent(FrmListadoRepostajes.this,
//                        FrmMostrarRepostaje.class);
//                intent.putExtra("repostajeParce", repostaje);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_repostajes, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmRepostaje.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorRepostajes extends ArrayAdapter<Repostaje> {

        Activity context;

        AdaptadorRepostajes(Activity context) {
            super(context, R.layout.list_repostajes, listaRepostajes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDCoches bbddCoches = new BBDDCoches(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_repostajes, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIcono);
                holder.coche = (TextView) item.findViewById(R.id.lbCoche);
                //holder.matricula = (TextView) findViewById(R.id.lbMatricula2);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatricula2);
                holder.consumo = (TextView) item.findViewById(R.id.lbConsumo);
                holder.unidadConsumo = (TextView) item.findViewById(R.id.lbUnidadConsumo);
                holder.kmRecorridos = (TextView) item.findViewById(R.id.lbKmRecorridosRepostaje);
//                holder.unidadDistanciaKmRepostaje = (TextView) item.findViewById(R.id.lbUnidadDistanciaKmRepostaje);
                holder.kmRepostaje = (TextView) item.findViewById(R.id.lbKmRepostaje);
                holder.unidadDistancia = (TextView) item.findViewById(R.id.lbUnidadDistancia);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            String idCoche = listaRepostajes.get(position).getIdCoche()
                    .toString().trim();

            Coche coche = bbddCoches.getCoche(idCoche);

            if (null != coche) {
                holder.icono.setImageResource(R.drawable.ic_senal_gasolinera);
                holder.coche.setText(coche.getNombre());
                holder.matricula.setText("(".concat(coche.getMatricula()).concat(")"));
//                holder.consumo.setText(listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                holder.consumo.setText(ajustarSiParcial(position));
//                holder.unidadConsumo.setText("(".concat(getString(R.string.unidad_consumo)).concat(")"));
                holder.unidadConsumo.setText(mostrarUnidadConsumo(position));
                holder.kmRecorridos.setText(listaRepostajes.get(position).getKmRecorridos().setScale(0, BigDecimal.ROUND_HALF_UP).toString());
//                holder.unidadDistanciaKmRepostaje.setText((getUnidadDistancia()));
                holder.kmRepostaje.setText(listaRepostajes.get(position).getKmRepostaje().toString());
                holder.unidadDistancia.setText(getUnidadDistancia());

            }

            return (item);
        }

//        private String mostrarDistanciaRepostaje(int position) {
//            String distancia = "";
//            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase("Km")) {
//                distancia = listaRepostajes.get(position).getKmRepostaje().toString();
//            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase("Miles")) {
//                BigDecimal millas = ToolsConversiones.kmToMillas(listaRepostajes.get(position).getKmRepostaje());
//                distancia = millas.toString();
//            }
//            return distancia;
//        }

        private String getUnidadDistancia() {
            String unidad = "";
            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                unidad = getString(R.string.unidad_distancia_km);
            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                unidad = getString(R.string.unidad_distancia_millas);
            }
            return unidad;
        }

//        private String getConsumoMedio(int position) {
//            String consumo = "";
//            if (listaRepostajes.get(position).getMediaConsumo().compareTo(BigDecimal.ZERO) == 0) {
//                consumo = getString(R.string.tipo_repostaje_parcial);
//            } else {
//                if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase("litros/liters")) {
//                    consumo = listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//                } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase("Gallons(UK)")) {
//                    BigDecimal litrosALos100 = listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP);
//                    consumo = ToolsConversiones.litros100ToMpgImp(litrosALos100).toString();
//                } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase("Gallons(US)")) {
//                    BigDecimal litrosALos100 = listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP);
//                    consumo = ToolsConversiones.litros100ToMpgUS(litrosALos100).toString();
//                }
//            }
//            return consumo;
//        }

        private String ajustarSiParcial(int position) {
            String consumo = "";
            if (listaRepostajes.get(position).getMediaConsumo().compareTo(BigDecimal.ZERO) == 0) {
                consumo = getString(R.string.tipo_repostaje_parcial);
            } else {
                consumo = listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
            return consumo;
        }

        private String Repostaje(int position) {
            String consumo = "";
            if (listaRepostajes.get(position).getMediaConsumo().compareTo(BigDecimal.ZERO) == 0) {
                consumo = getString(R.string.tipo_repostaje_parcial);
            } else {
                consumo = listaRepostajes.get(position).getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
            return consumo;
        }

        private String mostrarUnidadConsumo(int position) {
            String unidadConsumo = "";
            if (listaRepostajes.get(position).getMediaConsumo().compareTo(BigDecimal.ZERO) == 0) {
                unidadConsumo = "";
            } else {
                if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                    unidadConsumo = getString(R.string.unidad_consumo_litros);
                } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                    unidadConsumo = getString(R.string.unidad_consumo_galones_imperiales);
                } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                    unidadConsumo = getString(R.string.unidad_consumo_galones_US);
                }
            }
            return unidadConsumo;
        }

    }

    static class ViewHolder {
        ImageView icono;
        TextView coche;
        TextView matricula;
        TextView consumo;
        TextView unidadConsumo;
        TextView kmRecorridos;
        TextView kmRepostaje;
        TextView unidadDistancia;
//        TextView unidadDistanciaKmRepostaje;
    }

    private void ajustarListaAUnidades(ArrayList<Repostaje> listaRepostajes) {
        if (bbddAjustesAplicacion.esMillas()) {
            if (bbddAjustesAplicacion.esLitros()) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRepostaje())));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRecorridos())));
                    repos.setMediaConsumo(repos.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
                }

            } else if (bbddAjustesAplicacion.esGalonesUK()) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRepostaje())));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRecorridos())));
                    repos.setMediaConsumo(ToolsConversiones.litros100ToMpgImp(repos.getMediaConsumo()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            } else if (bbddAjustesAplicacion.esGalonesUS()) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRepostaje())));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(ToolsConversiones.kmToMillas(repos.getKmRecorridos())));
                    repos.setMediaConsumo(ToolsConversiones.litros100ToMpgUS(repos.getMediaConsumo()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        } else if (bbddAjustesAplicacion.esKm()) {
            if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(repos.getKmRepostaje()));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(repos.getKmRecorridos()));
                    repos.setMediaConsumo(repos.getMediaConsumo().setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(repos.getKmRepostaje()));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(repos.getKmRecorridos()));
                    repos.setMediaConsumo(ToolsConversiones.litros100ToMpgImp(repos.getMediaConsumo()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                for (Repostaje repos : listaRepostajes) {
                    repos.setKmRepostaje(ToolsConversiones.RedondearSinDecimales(repos.getKmRepostaje()));
                    repos.setKmRecorridos(ToolsConversiones.RedondearSinDecimales(repos.getKmRecorridos()));
                    repos.setMediaConsumo(ToolsConversiones.litros100ToMpgUS(repos.getMediaConsumo()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }


}
