package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.ListaConIcono;
import com.jmgarzo.objects.Repostaje;

import java.math.BigDecimal;
import java.util.ArrayList;


public class FragmentEstadisticas extends Fragment {


    private ListView lstEstadisticas;
    private ArrayList<ListaConIcono> listaEstadisticas;
    private AdView adView;
    //private boolean hayRepostajes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_estadisticas, container, false);
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

        getActivity().setTitle(R.string.title_activity_fragment_estadisticas);

//        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(getActivity());
//        ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
//        listaRepostajes = bbddRepostajes.getTodosLosRepostajesOrdenadoPorCoche();
//
//        Integer max=0;
//        int contador=0;
//        Integer idCoche;
//        ArrayList<Integer> cochesConRepostajes= new ArrayList<Integer>();
//        if(null != listaRepostajes && listaRepostajes.size()>1){
//            idCoche=listaRepostajes.get(0).getIdCoche();
//            for(Repostaje re : listaRepostajes){
//                if(idCoche == re.getIdCoche()){
//                    contador++;
//                }else{
//                    if(contador>2){
//                        cochesConRepostajes.add(idCoche);
//                    }
//                    if (contador>max){
//                        max=contador;
//                    }
//                    idCoche=re.getIdCoche();
//                    contador=0;
//                }
//            }
//            if(max>1) {
//                hayRepostajes = true;
//            }
//            else{
//                hayRepostajes=false;
//            }
//        }
//        else{
//            hayRepostajes=false;
//        }


        lstEstadisticas = (ListView) getActivity().findViewById(R.id.lstEstadisticas);

        listaEstadisticas = new ArrayList<ListaConIcono>();
        listaEstadisticas.add(new ListaConIcono(R.drawable.ic_graficotorta, getString(R.string.titulo_estadisticas_consumo_medio), getString(R.string.subtitulo_estadisticas_consumo_medio)));

        AdaptadorListaConIcono adaptadorListaConIcono = new AdaptadorListaConIcono(getActivity(), listaEstadisticas);
        lstEstadisticas.setAdapter(adaptadorListaConIcono);


        lstEstadisticas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (hayRepostajes()) {
                            Intent intent = new Intent(getActivity(), EstConsumoMedio2.class);
                            startActivity(intent);
                        } else {
                            mostrarAvisoDialogo(getString(R.string.titulo_error_no_hay_repostajes), getString(R.string.error_no_hay_repostajes));
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_estadisticas, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()) {
//            case R.id.action_new:
//                Intent intent = new Intent(getActivity(), FrmItv.class);
//                startActivity(intent);
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }

        return super.onOptionsItemSelected(item);
    }


    public void mostrarAvisoDialogo(String titulo, String error) {
        FragmentManager fm = getFragmentManager();
        Bundle b = new Bundle();
        b.putString("titulo", titulo);
        b.putString("error", error);
        DialogoAviso dialogo = new DialogoAviso();
        dialogo.setArguments(b);
        dialogo.show(fm, "tagAlerta");


    }

    private boolean hayRepostajes() {
        boolean hayRepostajes;
        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(getActivity());
        ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
        listaRepostajes = bbddRepostajes.getTodosLosRepostajesOrdenadoPorCoche();

        Integer max = 0;
        int contador = 0;
        Integer idCoche;
        ArrayList<Integer> cochesConRepostajes = new ArrayList<Integer>();
        if (null != listaRepostajes && listaRepostajes.size() > 1) {
            idCoche = listaRepostajes.get(0).getIdCoche();
            for (Repostaje re : listaRepostajes) {
                if (idCoche == re.getIdCoche()) {
                    if (re.getMediaConsumo().toString() != BigDecimal.ZERO.toString()) {
                        contador++;
                    }
                } else {
                    if (contador > 2) {
                        cochesConRepostajes.add(idCoche);
                    }

                    idCoche = re.getIdCoche();
                    contador = 0;
                }
                if (contador > max) {
                    max = contador;
                }
            }
            if (max > 1) {
                hayRepostajes = true;
            } else {
                hayRepostajes = false;
            }
        } else {
            hayRepostajes = false;
        }
        return hayRepostajes;
    }


}
