package com.jmgarzo.ratescar;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.jmgarzo.bbdd.BBDDAjustesAplicacion;
import com.jmgarzo.bbdd.BBDDCoches;
import com.jmgarzo.bbdd.BBDDImpuestosCirculacion;
import com.jmgarzo.bbdd.BBDDItvs;
import com.jmgarzo.bbdd.BBDDMantenimientos;
import com.jmgarzo.bbdd.BBDDRepostajes;
import com.jmgarzo.bbdd.BBDDSeguros;
import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.ImpuestoCirculacion;
import com.jmgarzo.objects.Itv;
import com.jmgarzo.objects.Mantenimiento;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.objects.Resumen;
import com.jmgarzo.objects.Seguro;
import com.jmgarzo.tools.ToolsConversiones;

import java.math.BigDecimal;
import java.util.ArrayList;


public class FragmentInicio extends Fragment {

    private AdView adView;
    private TextView txtInicioSinBBDD;
    private boolean hayBBDD;

    private ListView lstInicio;
    private ArrayList<Resumen> listaResumenes;
    private BBDDCoches bbddCoches;
    private BBDDRepostajes bbddRepostajes;
    private BBDDMantenimientos bbddMantenimientos;
    private BBDDAjustesAplicacion bbddAjustesAplicacion;
    private BBDDItvs bbddItvs;
    //Peajes, no lo añadimos al gasto total para que no eleve el gasto por km
//    private BBDDPeajes bbddPeajes;
    private BBDDSeguros bbddSeguros;
    private BBDDImpuestosCirculacion bbddImpuestosCirculacion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);

        //para mantener el título cuando se gira el movil
        getActivity().setTitle(getString(R.string.title_activity_fragment_inicio));


        /**PUBLICIDAD**/


        // Crear adView.
//        adView = new AdView(this);
//        adView.setAdUnitId(MY_AD_UNIT_ID);
//        adView.setAdSize(AdSize.BANNER);

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


        bbddCoches = new BBDDCoches(getActivity());
        bbddRepostajes = new BBDDRepostajes(getActivity());
        bbddMantenimientos = new BBDDMantenimientos(getActivity());
        bbddAjustesAplicacion = new BBDDAjustesAplicacion(getActivity());
        bbddItvs = new BBDDItvs(getActivity());
//        bbddPeajes = new BBDDPeajes(getActivity());
        bbddSeguros = new BBDDSeguros(getActivity());
        bbddImpuestosCirculacion = new BBDDImpuestosCirculacion(getActivity());

        if (bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId().size() > 0) {
            hayBBDD = true;
        } else {
            hayBBDD = false;
        }
        txtInicioSinBBDD = (TextView) getActivity().findViewById(R.id.txtInicioSinBBDD);
        if (hayBBDD) {
            txtInicioSinBBDD.setEnabled(false);
        } else {
            txtInicioSinBBDD.setEnabled(true);
            txtInicioSinBBDD.setText(getString(R.string.inicio_sin_bbdd));
        }
        if (hayBBDD) {

            lstInicio = (ListView) getActivity().findViewById(R.id.lstResumen);

            listaResumenes = new ArrayList<Resumen>();
            ArrayList<Coche> coches = bbddCoches.getTodosLosCochesConRepostajeOrdenadosPorId();
            for (Coche coche : coches) {
                Resumen resumen = new Resumen();
                mappingResumen(coche, resumen);
                listaResumenes.add(resumen);
            }
            AdaptadorResumenes adaptador = new AdaptadorResumenes(getActivity());

            lstInicio.setAdapter(adaptador);
            lstInicio.setClickable(false);
            // lstInicio.setEnabled(false);


        }

        if (!bbddAjustesAplicacion.ajustesInicializados()) {
            Intent intent = new Intent(getActivity(), FrmUnidades.class);
            intent.putExtra("Unidades", "unidadades");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_inicio, menu);

    }

    /**
     * @param coche
     * @param resumen Se le pasa un coche y un resumen y se rellenan todos los datos del objeto Resumen.
     */
    private void mappingResumen(Coche coche, Resumen resumen) {

        int icono;
        String titulo, subtitulo, sMediaConsumo, sKmRecorridos, sNumeroRepostajes, sLitrosConsumidos, sGastoCombutible, sGastoMantenimiento, sPrecioKm;

        titulo = getTitulo(coche);


        BigDecimal kmRecorridosRepostaje = BigDecimal.ZERO;
        BigDecimal litrosConsumidos = BigDecimal.ZERO;
        BigDecimal mediaConsumo = BigDecimal.ZERO;
        Integer numeroRepostajes = 0;
        BigDecimal gastoCombustible = BigDecimal.ZERO;

        ArrayList<Repostaje> repostajes = bbddRepostajes.getRepostajesPorCocheOrdenadosPorKmDesc(coche.getIdCoche().toString());


        kmRecorridosRepostaje = (repostajes.get(0).getKmRepostaje().subtract(repostajes.get(repostajes.size() - 1).getKmRepostaje()).setScale(2, BigDecimal.ROUND_HALF_UP));

        // kmRecorridosRepostaje = calcularKmRecorridos(coche.getIdCoche());
        numeroRepostajes = repostajes.size();
        for (int i = 0; i < repostajes.size(); i++) {

            if (i != repostajes.size() - 1) {
                litrosConsumidos = litrosConsumidos.add(repostajes.get(i).getLitros());
            }

            gastoCombustible = gastoCombustible.add(repostajes.get(i).getCosteRepostaje());

        }
        if (kmRecorridosRepostaje.compareTo(BigDecimal.ZERO) != 0) {
            mediaConsumo = ((litrosConsumidos.multiply(BigDecimal.valueOf(100))).divide(kmRecorridosRepostaje, 4, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            mediaConsumo = BigDecimal.ZERO;
        }

        BigDecimal gastoMantenimiento = BigDecimal.ZERO;
        ArrayList<Mantenimiento> mantenimientos = bbddMantenimientos.getMatenimientosPorCocheOrdenadosPorKmDesc(coche.getIdCoche());
        for (Mantenimiento mantenimiento : mantenimientos) {
            gastoMantenimiento = gastoMantenimiento.add(mantenimiento.getCosteFinal());
        }

        /**********************GASTO TOTAL ****************************/

        BigDecimal gastoTotal = gastoMantenimiento.add(gastoCombustible);
        ArrayList<Itv> itvs = bbddItvs.getItvsPorCocheOrdenadoFechaDesc(coche.getIdCoche());
        for (Itv itv : itvs) {
            gastoTotal = gastoTotal.add(itv.getPrecio());
        }

        ArrayList<Seguro> seguros = bbddSeguros.getSegurosPorCocheOrdenadoFechaDesc(coche.getIdCoche());
        for (Seguro seguro : seguros) {
            gastoTotal = gastoTotal.add(seguro.getPrima());
        }

        ArrayList<ImpuestoCirculacion> impuestosCirculacion = bbddImpuestosCirculacion.getImpuestosPorCocheOrdenadosFechaDesc(coche.getIdCoche());
        for (ImpuestoCirculacion impuesto : impuestosCirculacion) {
            gastoTotal = gastoTotal.add(impuesto.getImporte());
        }

        /***************GASTO POR KM ************************/

        BigDecimal kmRecorridosTotales = calcularKmRecorridos(coche.getIdCoche());
        kmRecorridosTotales = kmRecorridosTotales.setScale(0, BigDecimal.ROUND_HALF_UP);

        BigDecimal precioPorKm = BigDecimal.ZERO;
        if (kmRecorridosTotales.compareTo(BigDecimal.ZERO) != 0) {
            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                precioPorKm = gastoTotal.setScale(4, BigDecimal.ROUND_HALF_UP).divide(kmRecorridosTotales.setScale(4, BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP);
            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                precioPorKm = gastoTotal.setScale(4, BigDecimal.ROUND_HALF_UP).divide(ToolsConversiones.kmToMillas(kmRecorridosTotales).setScale(4, BigDecimal.ROUND_HALF_UP), BigDecimal.ROUND_HALF_UP);
            }
        }


//        resumen.setIcono();
        resumen.setTitulo(titulo);
        resumen.setSubtitulo("");
        if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            resumen.setMediaConsumo(mediaConsumo.toString());
            resumen.setLitrosConsumidos(litrosConsumidos.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            resumen.setMediaConsumo(ToolsConversiones.litros100ToMpgImp(mediaConsumo).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            resumen.setLitrosConsumidos(ToolsConversiones.litrosToGalonesUK(litrosConsumidos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            resumen.setMediaConsumo(ToolsConversiones.litros100ToMpgUS(mediaConsumo).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            resumen.setLitrosConsumidos(ToolsConversiones.litrosToGalonesUS(litrosConsumidos).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        }
        if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            resumen.setKmRecorridos(kmRecorridosTotales.toString());
        } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            resumen.setKmRecorridos(ToolsConversiones.kmToMillas(kmRecorridosTotales).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        }
//        resumen.setKmRecorridos(kmRecorridosTotales.toString());
        resumen.setNumeroRepostajes(numeroRepostajes.toString());
//        resumen.setLitrosConsumidos(litrosConsumidos.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        resumen.setGastoCombutible(gastoCombustible.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        resumen.setGastoMantenimiento(gastoMantenimiento.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        resumen.setGastoTotal(gastoTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        resumen.setPrecioKm(precioPorKm.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

    }

    private String getTitulo(Coche coche) {
        String titulo = "";
        if (null != coche.getNombre() && !coche.getNombre().equals("")) {
            if (null != coche.getMatricula() && !coche.getMatricula().equals("")) {
                titulo = coche.getNombre().concat(" ").concat("(").concat(coche.getMatricula().concat(")"));
            } else {
                titulo = coche.getNombre();
            }
        } else if (null != coche.getMatricula() && !coche.getMatricula().equals("")) {
            titulo = coche.getMatricula();
        }
        return titulo;
    }

//    private String getMediaConsumo(Coche coche) {
//
//        BigDecimal kmRecorridos = BigDecimal.ZERO;
//        BigDecimal litrosConsumidos = BigDecimal.ZERO;
//        BigDecimal mediaConsumo = BigDecimal.ZERO;
//
//        ArrayList<Repostaje> repostajes = bbddRepostajes.getRepostajesPorCocheOrdenadosPorKmDesc(coche.getIdCoche().toString());
//
//        kmRecorridos = repostajes.get(0).getKmRepostaje().subtract(repostajes.get(repostajes.size() - 1).getKmRepostaje());
//        kmRecorridos = calcularKmRecorridos(coche.getIdCoche());
//
//        for (Repostaje repostaje : repostajes) {
//            litrosConsumidos = litrosConsumidos.add(repostaje.getLitros());
//        }
//        mediaConsumo = (litrosConsumidos.multiply(BigDecimal.valueOf(100))).divide((kmRecorridos).setScale(2, BigDecimal.ROUND_HALF_UP),BigDecimal.ROUND_HALF_UP);
//
//
//        return BigDecimal.ZERO.toString();
//    }

    class AdaptadorResumenes extends ArrayAdapter<Resumen> {

        Activity context;

        AdaptadorResumenes(Activity context) {
            super(context, R.layout.list_resumenes, listaResumenes);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;


            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.list_resumenes, null);

                holder = new ViewHolder();
                //holder.icono = (ImageView) item.findViewById(R.id.ImgIcono);
                holder.lbTitulo = (TextView) item.findViewById(R.id.lbTituloResumen);
                holder.lbTituloDato = (TextView) item.findViewById(R.id.lbTituloResumenDato);
//                holder.lbSubtitulo = (TextView) item.findViewById(R.id.lbSubtituloResumen);
//                holder.lbSubtituloDato = (TextView) item.findViewById(R.id.lbSubtituloResumenDato);
                holder.lbMediaConsumo = (TextView) item.findViewById(R.id.lbMediaConsumoResumen);
                holder.lbMediaConsumoDato = (TextView) item.findViewById(R.id.lbMediaConsumoResumenDato);
                holder.lbKmRecorridos = (TextView) item.findViewById(R.id.lbKmRecorridosResumen);
                holder.lbKmRecorridosDato = (TextView) item.findViewById(R.id.lbKmRecorridosResumenDato);
                holder.lbNumeroRepostajes = (TextView) item.findViewById(R.id.lbNumeroRespostajesResumen);
                holder.lbNumeroRepostajesDato = (TextView) item.findViewById(R.id.lbNumeroRepostajesResumenDato);
                holder.lbLitrosConsumidos = (TextView) item.findViewById(R.id.lbLitrosConsumidosResumen);
                holder.lbLitrosConsumidosDato = (TextView) item.findViewById(R.id.lbLitrosConsumidosResumenDato);
                holder.lbGastoCombustible = (TextView) item.findViewById(R.id.lbGastoCombustibleResumen);
                holder.lbGastoCombustibleDato = (TextView) item.findViewById(R.id.lbGastoCombustibleResumenDato);
                holder.lbGastoMantenimiento = (TextView) item.findViewById(R.id.lbGastoMantenimientoResumen);
                holder.lbGastoMantenimientoDato = (TextView) item.findViewById(R.id.lbGastoMantenimientoResumenDato);
                holder.lbGastoTotal = (TextView) item.findViewById(R.id.lbGastoTotalResumen);
                holder.lbGastoTotalDato = (TextView) item.findViewById(R.id.lbGastoTotalResumenDato);
                holder.lbPrecioKm = (TextView) item.findViewById(R.id.lbPrecioKmResumen);
                holder.lbPrecioKmDato = (TextView) item.findViewById(R.id.lbPrecioKmResumenDato);

                holder.lbUnidadConsumoInicio = (TextView) item.findViewById(R.id.lbUnidadConsumoInicio);
                holder.lbUnidadDistanciaInicio = (TextView) item.findViewById(R.id.lbUnidadDistanciaInicio);
                holder.lbUnidadVolumenInicio = (TextView) item.findViewById(R.id.lbUnidadVolumenInicio);
                holder.lbUnidadMonedaGastoTotalInicio = (TextView) item.findViewById(R.id.lbUnidadMonedaGastoTotalInicio);
                holder.lbUnidadMonedaGastoMantenimientoInicio = (TextView) item.findViewById(R.id.lbUnidadMonedaGastoMantenimientoInicio);
                holder.lbUnidadMonedaGastoTotalResumenInicio = (TextView) item.findViewById(R.id.lbUnidadMonedaGastoTotalResumenInicio);
                holder.lbUnidadMonedaTotalPorKmInicio = (TextView) item.findViewById(R.id.lbUnidadMonedaTotalPorKmInicio);

                item.setTag(holder);
            } else {
                holder = (ViewHolder) item.getTag();
            }

            // holder.icono.setImageResource(R.drawable.ic_senal_gasolinera);
            holder.lbTitulo.setText(getString(R.string.titulo_resumen));
            holder.lbTituloDato.setText(listaResumenes.get(position).getTitulo());
//            holder.lbSubtitulo.setText(getString(R.string.subtitulo_resumen));
//            holder.lbSubtituloDato.setText(listaResumenes.get(position).getSubtitulo());
            holder.lbMediaConsumo.setText(getString(R.string.media_consumo_resumen));
            holder.lbMediaConsumoDato.setText(listaResumenes.get(position).getMediaConsumo());
            holder.lbKmRecorridos.setText(getString(R.string.km_recorridos_resumen));
            holder.lbKmRecorridosDato.setText(listaResumenes.get(position).getKmRecorridos());
            holder.lbNumeroRepostajes.setText(getString(R.string.numero_repostajes_resumen));
            holder.lbNumeroRepostajesDato.setText(listaResumenes.get(position).getNumeroRepostajes());
            holder.lbLitrosConsumidos.setText(getString(R.string.litros_consumidos_resumen));
            holder.lbLitrosConsumidosDato.setText(listaResumenes.get(position).getLitrosConsumidos());
            holder.lbGastoCombustible.setText(getString(R.string.gasto_combustible_resumen));
            holder.lbGastoCombustibleDato.setText(listaResumenes.get(position).getGastoCombutible());
            holder.lbGastoMantenimiento.setText(getString(R.string.gasto_mantenimiento_resumen));
            holder.lbGastoMantenimientoDato.setText(listaResumenes.get(position).getGastoMantenimiento());
            holder.lbGastoTotal.setText(getString(R.string.gasto_total_resumen));
            holder.lbGastoTotalDato.setText(listaResumenes.get(position).getGastoTotal());
            holder.lbPrecioKm.setText(" ");
            holder.lbPrecioKmDato.setText(listaResumenes.get(position).getPrecioKm());


            if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
                holder.lbUnidadConsumoInicio.setText(getString(R.string.unidad_consumo_litros));
                holder.lbUnidadVolumenInicio.setText(getString(R.string.lb_unidad_cantidad_litros));
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
                holder.lbUnidadConsumoInicio.setText((getString(R.string.unidad_consumo_galones_imperiales)));
                holder.lbUnidadVolumenInicio.setText(getString(R.string.lb_unidad_cantidad_galones_uk));
            } else if (bbddAjustesAplicacion.getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
                holder.lbUnidadConsumoInicio.setText((getString(R.string.unidad_consumo_galones_US)));
                holder.lbUnidadVolumenInicio.setText(getString(R.string.lb_unidad_cantidad_galones_us));
            }

            if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
                holder.lbUnidadDistanciaInicio.setText(getString(R.string.unidad_distancia_km));
                holder.lbPrecioKm.setText(getString(R.string.precio_km_resumen));
            } else if (bbddAjustesAplicacion.getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
                holder.lbUnidadDistanciaInicio.setText(getString(R.string.unidad_distancia_millas));
                holder.lbPrecioKm.setText(getString(R.string.precio_milla_resumen));

            }

            holder.lbUnidadMonedaGastoTotalInicio.setText(bbddAjustesAplicacion.getValorMoneda());
            holder.lbUnidadMonedaGastoMantenimientoInicio.setText(bbddAjustesAplicacion.getValorMoneda());
            holder.lbUnidadMonedaGastoTotalResumenInicio.setText(bbddAjustesAplicacion.getValorMoneda());
            holder.lbUnidadMonedaTotalPorKmInicio.setText(bbddAjustesAplicacion.getValorMoneda());
            return (item);
        }


    }

    static class ViewHolder {
        //        ImageView icono;
        TextView lbTitulo, lbTituloDato;
        //        TextView lbSubtitulo, lbSubtituloDato;
        TextView lbMediaConsumo, lbMediaConsumoDato;
        TextView lbKmRecorridos, lbKmRecorridosDato;
        TextView lbNumeroRepostajes, lbNumeroRepostajesDato;
        TextView lbLitrosConsumidos, lbLitrosConsumidosDato;
        TextView lbGastoCombustible, lbGastoCombustibleDato;
        TextView lbGastoMantenimiento, lbGastoMantenimientoDato;
        TextView lbGastoTotal, lbGastoTotalDato;
        TextView lbPrecioKm, lbPrecioKmDato;
        TextView lbUnidadConsumoInicio, lbUnidadDistanciaInicio, lbUnidadVolumenInicio,
                lbUnidadMonedaGastoTotalInicio, lbUnidadMonedaGastoMantenimientoInicio, lbUnidadMonedaGastoTotalResumenInicio, lbUnidadMonedaTotalPorKmInicio;
    }


    private BigDecimal calcularKmRecorridos(Integer idCoche) {

        BigDecimal kmInicialesRepostaje = bbddRepostajes.getkmInicialesPorCoche(idCoche);
        BigDecimal kmInicialesMantenimiento = bbddMantenimientos.getkmInicialesPorCoche(idCoche);

        BigDecimal kmIniciales = BigDecimal.ZERO;


        if (kmInicialesRepostaje.compareTo(BigDecimal.ZERO) != 0) {
            if (kmInicialesMantenimiento.compareTo(BigDecimal.ZERO) != 0) {
                if (kmInicialesRepostaje.compareTo(kmInicialesMantenimiento) < 0) {

                    kmIniciales = kmInicialesRepostaje;
                } else {
                    kmIniciales = kmInicialesMantenimiento;
                }
            } else {
                kmIniciales = kmInicialesRepostaje;
            }
        } else {
            if (kmInicialesMantenimiento.compareTo(BigDecimal.ZERO) != 0) {
                kmIniciales = kmInicialesMantenimiento;
            } else {
                kmIniciales = BigDecimal.ZERO;
            }
        }


        BigDecimal kmFinalesRepostaje = bbddRepostajes.getkmFinalesPorCoche(idCoche);
        BigDecimal kmFinalesMantenimiento = bbddMantenimientos.getkmFinalesPorCoche(idCoche);

        BigDecimal kmFinales = BigDecimal.ZERO;

        if (kmFinalesRepostaje.compareTo(BigDecimal.ZERO) != 0) {
            if (kmFinalesMantenimiento.compareTo(BigDecimal.ZERO) != 0) {
                if (kmFinalesRepostaje.compareTo(kmFinalesMantenimiento) < 0) {
                    kmFinales = kmFinalesMantenimiento;
                } else {
                    kmFinales = kmFinalesRepostaje;
                }

            } else {
                kmFinales = kmFinalesRepostaje;

            }
        } else {
            if (kmFinalesMantenimiento.compareTo(BigDecimal.ZERO) != 0) {
                kmFinales = kmFinalesMantenimiento;
            } else {
                kmFinales = BigDecimal.ZERO;
            }
        }

        BigDecimal kmRecorridos = kmFinales.subtract(kmIniciales);
        return kmRecorridos;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Boolean hayBBDD2 = false;
        if (bbddCoches.getNumeroCoches() != 0) {
            hayBBDD2 = true;
        } else {
            hayBBDD2 = false;
        }
        switch (item.getItemId()) {

            case R.id.action_new_repostaje:
                if (hayBBDD2) {
                    intent = new Intent(getActivity(), FrmRepostaje.class);
                    startActivity(intent);
                } else {
                    mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_repostaje));
                }
                return true;
            case R.id.action_new_mantenimiento:
                if (hayBBDD2) {
                    intent = new Intent(getActivity(), FrmMantenimiento.class);
                    startActivity(intent);
                } else {
                    mostrarAvisoBBDD(getString(R.string.error_no_hay_bbdd_mantenimiento));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        if (hayBBDD) {
            txtInicioSinBBDD.setEnabled(false);
        } else {
            txtInicioSinBBDD.setEnabled(true);
            txtInicioSinBBDD.setText(getString(R.string.inicio_sin_bbdd));
        }
    }


    public void mostrarAvisoBBDD(String error) {
        ArrayList<String> errores = new ArrayList<String>();
        errores.add(error);
        FragmentManager fm = getFragmentManager();
        Bundle b = new Bundle();
        b.putStringArrayList("errores", errores);
        DialogoIniciarBBDD dialogo = new DialogoIniciarBBDD();
        dialogo.setArguments(b);
        dialogo.show(fm, "tagAlerta");

        Fragment fragment = new FragmentInicio();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}


