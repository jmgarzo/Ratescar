package com.jmgarzo.ratescar;


import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.jmgarzo.bbdd.BBDDRecambios;
import com.jmgarzo.objects.Recambio;
import com.jmgarzo.objects.Repostaje;

import java.util.ArrayList;


public class FragmentRecambios extends Fragment {

    private ListView lstRecambios;
    private ArrayList<Recambio> listaRecambios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_recambios, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getActivity().setTitle(getString(R.string.title_activity_fragment_recambios));
//
//        lstRecambios = (ListView) getActivity().findViewById(R.id.lstMostrarRecambios);
//
//        BBDDRecambios bbddRecambios = new BBDDRecambios(getActivity());
//        listaRecambios = new ArrayList<Recambio>();
//        listaRecambios = bbddRecambios.getTodosLosRecambios();
//
//        AdaptadorRecambios adaptador = new AdaptadorRecambios(getActivity());
//
//        lstRecambios.setAdapter(adaptador);
//
//        lstRecambios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//
//                Recambio recambio = listaRecambios.get(position);
//                Intent intent = new Intent(getActivity(),FrmRecambio.class);
//                String idRecambio = recambio.getIdRecambio().toString().trim();
//                intent.putExtra("idRecambio", idRecambio);
//                startActivity(intent);
//
//            }
//        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_recambios, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmRecambio.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    class AdaptadorRecambios extends ArrayAdapter<Recambio> {
        Activity context;

        AdaptadorRecambios(Activity context) {
            super(context, R.layout.list_recambios, listaRecambios);
            this.context = context;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View item = convertView;
//            ViewHolder holder;
//
//            if (item == null) {
//                LayoutInflater inflater = context.getLayoutInflater();
//                item = inflater.inflate(R.layout.list_recambios, null);
//
//                holder = new ViewHolder();
//                holder.nombre = (TextView) item.findViewById(R.id.lbNombreRecambio);
//                holder.marca = (TextView) item.findViewById(R.id.lbMarcaRecambio);
//                holder.referencia = (TextView) item.findViewById(R.id.lbReferenciaRecambio);
//
//                item.setTag(holder);
//            } else {
//                holder = (ViewHolder) item.getTag();
//            }
//
//            String idRecambio = listaRecambios.get(position).getIdRecambio().toString().trim();
//
//            holder.nombre.setText(listaRecambios.get(position).getNombre());
//            holder.marca.setText(listaRecambios.get(position).getMarca().trim());
//            holder.referencia.setText(listaRecambios.get(position).getReferencia().trim());
//
//            return (item);
//        }
//
//    }
//
//    static class ViewHolder {
//        TextView nombre;
//        TextView marca;
//        TextView referencia;
//    }


    }


}
