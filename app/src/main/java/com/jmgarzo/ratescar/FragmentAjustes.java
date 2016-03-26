package com.jmgarzo.ratescar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.objects.Ajuste;
import com.jmgarzo.objects.Constantes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import ar.com.daidalos.afiledialog.*;


public class FragmentAjustes extends Fragment {

    private AdView adView;

    private ListView lstAjustes;
    private ArrayList<Ajuste> listaAjustes;


    public FragmentAjustes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_ajustes, container, false);

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

        getActivity().setTitle(getString(R.string.title_activity_fragment_ajustes));

        lstAjustes = (ListView) getActivity().findViewById(R.id.lstAjustes);

        listaAjustes = new ArrayList<Ajuste>();
        //TODO buscar un icono para importar y exportar
        listaAjustes.add(new Ajuste(R.drawable.ic_machine2, getString(R.string.titulo_ajuste_unidades), getString(R.string.subtitulo_ajuste_unidades)));
        listaAjustes.add(new Ajuste(R.drawable.ic_machine2, getString(R.string.titulo_ajuste_importar_exportar), getString(R.string.subtitulo_ajuste_importar_exportar)));



        AdaptadorAjustes adaptadorAjustes = new AdaptadorAjustes(getActivity());
        lstAjustes.setAdapter(adaptadorAjustes);
        lstAjustes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                         intent = new Intent(getActivity(), FrmUnidades.class);
                        startActivity(intent);
                        break;


                    case 1:
                         intent = new Intent(getActivity(), FrmImportarExportar.class);
                        startActivity(intent);
                        break;

                }
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        return super.onOptionsItemSelected(item);
//

    }


    class AdaptadorAjustes extends ArrayAdapter<Ajuste> {
        Activity context;

        AdaptadorAjustes(Activity context) {
            super(context, R.layout.list_ajustes, listaAjustes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;


            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_ajustes, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.ImgIconoAjuste);
                holder.titulo = (TextView) item.findViewById(R.id.lbTituloAjuste);
                holder.subtitulo = (TextView) item.findViewById(R.id.lbSubtituloAjuste);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            holder.icono.setImageResource(listaAjustes.get(position).getIcono());
            holder.titulo.setText(listaAjustes.get(position).getTitulo());
            holder.subtitulo.setText(listaAjustes.get(position).getSubtitulo());


            return (item);
        }

    }

    static class ViewHolder {
        ImageView icono;
        TextView titulo;
        TextView subtitulo;

    }


}


