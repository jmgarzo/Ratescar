package com.jmgarzo.ratescar;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDImagenes;
import com.jmgarzo.bbdd.BBDDMarcas;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
//import com.jmgarzo.tools.ToolsImagenes;

public class FragmentCoches extends Fragment {
    private ListView lstCoches;
    private ArrayList<Coche> listaCoches;
    private AdView adView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_coches, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //para mantener el titulo cuando se gira el movil.
        getActivity().setTitle(getString(R.string.title_activity_fragment_coches));


        /**PUBLICIDAD**/

        adView = new AdView(getActivity());
        adView.setAdUnitId(Constantes.ADUNITID);
        adView.setAdSize(AdSize.BANNER);

        // Buscar LinearLayout suponiendo que se le ha asignado
        // el atributo android:id="@+id/mainLayout".
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.banner);

        // Añadirle adView.
        linearLayout.addView(adView);

        // Iniciar una solicitud genérica.
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulador
//                .addTestDevice("16A4155AB1690691277DB1EB823AB891") // Mi teléfono
                .build();

        // Cargar adView con la solicitud de anuncio.
        adView.loadAd(adRequest);

        /*FIN PUBLIDAD*/


        BBDDCoches bbddCoches = new BBDDCoches(getActivity());
        listaCoches = new ArrayList<Coche>();
        listaCoches = bbddCoches.getTodosLosCoches();

        lstCoches = (ListView) getActivity().findViewById(R.id.lstCoches);
        AdaptadorCoches adaptador = new AdaptadorCoches(getActivity());
        lstCoches.setAdapter(adaptador);

        lstCoches.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer idCoche = listaCoches.get(position).getIdCoche();
                Intent intent = new Intent(getActivity(), FrmCoche.class);
                intent.putExtra("idCoche", idCoche.toString());
                startActivity(intent);

            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_coches, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), FrmCoche.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // ***************---------------------------------*************************
    // *************** ADAPTADOR PARA LA EL LISTVIEW DE COCHES *************************
    // ***************---------------------------------*************************

    class AdaptadorCoches extends ArrayAdapter<Coche> {

        Activity context;


        AdaptadorCoches(Activity context) {
            super(context, R.layout.list_coches, listaCoches);
            this.context = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;
            BBDDMarcas bbddMarcas = new BBDDMarcas(context);
            BBDDImagenes bbddImagenes = new BBDDImagenes(context);

            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_coches, null);

                holder = new ViewHolder();
                holder.icono = (ImageView) item.findViewById(R.id.imgMarca);
                holder.matricula = (TextView) item.findViewById(R.id.lbMatricula);
                holder.nombre = (TextView) item.findViewById(R.id.lbNombre);
                holder.marca = (TextView) item.findViewById(R.id.lbMarca);
                holder.modelo = (TextView) item.findViewById(R.id.lbModelo);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }
            String ruta = bbddImagenes.getRutaThumbImagenPorCocheYTipo(listaCoches.get(position).getIdCoche(), Constantes.TIPO_COCHE);
//            if (ruta != "") {
//                holder.icono.setImageResource(bbddMarcas.getMarca(listaCoches.get(position).getIdMarca()).getIcono());
//                Bitmap foto = ToolsImagenes.decodeSampledBitmapFromFile(ruta, 200, 200);
//                holder.icono.setImageBitmap(foto);
//
//
//            } else {
                holder.icono.setImageResource(bbddMarcas.getMarca(listaCoches.get(position).getIdMarca()).getIcono());
//            }
            holder.nombre.setText(listaCoches.get(position).getNombre());
            holder.matricula.setText(listaCoches.get(position).getMatricula());
            //String idMarca[] = { listaCoches.get(position).getIdMarca().toString() };
            holder.marca.setText(bbddMarcas.getMarca(listaCoches.get(position).getIdMarca()).getNombre());
            holder.modelo.setText(listaCoches.get(position).getModelo());

            return (item);
        }
    }

    static class ViewHolder {
        ImageView icono;
        TextView matricula;
        TextView nombre;
        TextView marca;
        TextView modelo;
    }


    // ***************---------------------------------*************************
    // *************** ADAPTADOR PARA LA EL LISTVIEW DE MARCAS *************************
    // ***************---------------------------------*************************

//	class AdaptadorMarca extends ArrayAdapter<Marca> {
//
//		Activity context;
//
//		AdaptadorMarca(Activity context) {
//			super(context, R.layout.list_marcas, listaMarcas);
//			this.context = context;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View item = convertView;
//			ViewHolderMarcas holder;
//			BBDDMarcas bbddMarcas = new BBDDMarcas(context);
//
//			if (item == null) {
//				LayoutInflater inflater = context.getLayoutInflater();
//				item = inflater.inflate(R.layout.list_marcas, null);
//
//				holder = new ViewHolderMarcas();
//				holder.icono = (ImageView) item.findViewById(R.id.imgMarca);
//				holder.nombre = (TextView) item.findViewById(R.id.lbNombre);
//
//				item.setTag(holder);
//			} else {
//				holder = (ViewHolderMarcas) item.getTag();
//			}
//
//			holder.nombre.setText(listaMarcas.get(position).getNombre());
//			holder.icono.setImageResource(listaMarcas.get(position).getIcono());
//
//			return (item);
//		}
//	}
//
//	static class ViewHolderMarcas {
//		ImageView icono;
//		TextView nombre;
//	}

}

