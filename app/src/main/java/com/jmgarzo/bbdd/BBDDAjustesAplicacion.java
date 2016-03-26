package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.AjustesAplicacion;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 10/11/2015.
 */
public class BBDDAjustesAplicacion {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDAjustesAplicacion(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);

    }

    public ArrayList<AjustesAplicacion> getNombreValorOrderByNombre() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<AjustesAplicacion> listAjustesAplicacion = new ArrayList<AjustesAplicacion>();
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_AJUSTES_APLICACION_NOMBRE_VALOR_BY_NOMBRE, null);

        if (cursor.moveToFirst()) {
            do {
                AjustesAplicacion ajustesAplicacion = new AjustesAplicacion();

                if (!Constantes.esVacio(cursor.getString(0))) {
                    ajustesAplicacion.setNombre(cursor.getString(0));
                } else {
                    ajustesAplicacion.setNombre("");
                }

                if (!Constantes.esVacio(cursor.getString(1))) {
                    ajustesAplicacion.setValor(cursor.getString(1));
                } else {
                    ajustesAplicacion.setValor("");
                }

                listAjustesAplicacion.add(ajustesAplicacion);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listAjustesAplicacion;
    }

    public ArrayList<AjustesAplicacion> getAjustesAplicacionOrderByNombre() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<AjustesAplicacion> listAjustesAplicacion = new ArrayList<AjustesAplicacion>();
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_AJUSTES_APLICACION_ORDER_BY_NOMBRE, null);

        if (cursor.moveToFirst()) {
            do {
                AjustesAplicacion ajustesAplicacion = new AjustesAplicacion();
                mapearAjusteAplicacion(cursor, ajustesAplicacion);
                listAjustesAplicacion.add(ajustesAplicacion);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listAjustesAplicacion;
    }


    public String getValorPorNombre(String nombre) {
        bbdd = bbddhelper.getReadableDatabase();
        String valor = null;
        String[] args = {nombre};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_AJUSTES_APLICACION_VALOR_POR_NOMBRE_AJUSTE, args);

        if (cursor.moveToFirst()) {
            valor = cursor.getString(0);
        }
        cursor.close();
        bbdd.close();
        return valor;
    }

    public String getValorCantidadCombustible() {
        return getValorPorNombre("cantidadCombustible");
    }

    public String getValorMoneda() {
        return getValorPorNombre("moneda");
    }

    public String getValorDistancia() {
        return getValorPorNombre("distancia");
    }

    public String getValorIdioma() {
        return getValorPorNombre("idioma");
    }

    public boolean esKm() {
        boolean result;
        if (getValorDistancia().equalsIgnoreCase(Constantes.KM)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean esMillas() {
        boolean result;
        if (getValorDistancia().equalsIgnoreCase(Constantes.MILLAS)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean esLitros() {
        if (getValorCantidadCombustible().equalsIgnoreCase(Constantes.LITROS)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esGalonesUS() {
        if (getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_US)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esGalonesUK() {
        if (getValorCantidadCombustible().equalsIgnoreCase(Constantes.GALONES_UK)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ajustesInicializados() {
        bbdd = bbddhelper.getReadableDatabase();
        String valor = null;
        String[] args = {"inicializados"};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_AJUSTES_APLICACION_VALOR_POR_NOMBRE_AJUSTE, args);

        if (cursor.moveToFirst()) {
            valor = cursor.getString(0);
        }
        cursor.close();
        bbdd.close();
        boolean resultado = false;
        if (null != valor && !"".equalsIgnoreCase(valor)) {
            if (valor.equalsIgnoreCase("no")) {
                resultado = false;
            } else if (valor.equalsIgnoreCase("si")) {
                resultado = true;
            }
        }
        return resultado;
    }


    public void AñadirAjuste(AjustesAplicacion ajuste) {

        BBDDAjustesAplicacion bbddAjustesAplicacion = new BBDDAjustesAplicacion(context);
        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("id_ajuste", ajuste.getIdAjuste());
        valores.put("nombre", ajuste.getNombre());
        valores.put("valor", ajuste.getValor());
        valores.put("descripcion", ajuste.getDescripcion());
        valores.put("aux1_string", ajuste.getAuxString1());
        valores.put("aux2_string", ajuste.getAuxString2());
        if (null != ajuste.getAuxInteger1()) {
            valores.put("aux1_integer", ajuste.getAuxInteger1());
        } else {
            valores.put("aux1_integer", new Integer(0));
        }
        if (null != ajuste.getAuxInteger2()) {
            valores.put("aux2_integer", ajuste.getAuxInteger1());
        } else {
            valores.put("aux2_integer", new Integer(0));
        }

        bbdd.insert("AjustesAplicacion", null, valores);
        bbdd.close();

    }

    public void actualizarAjustesAplicacion(ArrayList<AjustesAplicacion> listaAjustesAplicacion) {
        bbdd = bbddhelper.getWritableDatabase();

        for (AjustesAplicacion ajuste : listaAjustesAplicacion) {

            ContentValues valores = new ContentValues();
            valores.put("id_ajuste", ajuste.getIdAjuste());
            valores.put("nombre", ajuste.getNombre());
            valores.put("valor", ajuste.getValor());
            valores.put("descripcion", ajuste.getDescripcion());
            valores.put("aux1_string", ajuste.getAuxString1());
            valores.put("aux2_string", ajuste.getAuxString2());
            if (null != ajuste.getAuxInteger1()) {
                valores.put("aux1_integer", ajuste.getAuxInteger1());
            } else {
                valores.put("aux1_integer", new Integer(0));
            }
            if (null != ajuste.getAuxInteger2()) {
                valores.put("aux2_integer", ajuste.getAuxInteger2());
                ;
            } else {
                valores.put("aux2_integer", new Integer(0));
            }
            String arg[] = {ajuste.getIdAjuste().toString().trim()};
            int result = bbdd.update("AjustesAplicacion", valores, " id_ajuste = ? ", arg);


        }
        bbdd.close();
    }


    private void mapearAjusteAplicacion(Cursor cursor, AjustesAplicacion ajusteAplicacion) {
        Integer idAjuste = Integer.valueOf(cursor.getInt(0));
        ajusteAplicacion.setIdAjuste(idAjuste);

        if (!Constantes.esVacio(cursor.getString(1))) {
            ajusteAplicacion.setNombre(cursor.getString(1));
        } else {
            ajusteAplicacion.setNombre("");
        }

        if (!Constantes.esVacio(cursor.getString(2))) {
            ajusteAplicacion.setValor(cursor.getString(2));
        } else {
            ajusteAplicacion.setValor("");
        }

        if (!Constantes.esVacio(cursor.getString(3))) {
            ajusteAplicacion.setDescripcion(cursor.getString(3));
        } else {
            ajusteAplicacion.setDescripcion("");
        }

        if (!Constantes.esVacio(cursor.getString(4))) {
            ajusteAplicacion.setAuxString1(cursor.getString(4));
        } else {
            ajusteAplicacion.setAuxString1("");
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            ajusteAplicacion.setAuxString2(cursor.getString(5));
        } else {
            ajusteAplicacion.setAuxString2("");
        }

        Integer auxInteger1 = new Integer(cursor.getInt(6));
        if (null != auxInteger1) {
            ajusteAplicacion.setAuxInteger1(0);
        } else {
            ajusteAplicacion.setAuxInteger1(cursor.getInt(6));
        }

        Integer auxInteger2 = new Integer(cursor.getInt(7));
        if (null != auxInteger2) {
            ajusteAplicacion.setAuxInteger2(0);
        } else {
            ajusteAplicacion.setAuxInteger2(cursor.getInt(7));
        }

    }


    /*Esto es necesario porque en las BBDD anteriores a la versión 16, en los campos de Repostajes tipo_pago y tipo_carretera
    se guardaban los valores en solo en Español al pasar a la versión en Ingles se ha tenido que modificar la BBDD para poder
    tenerlo en varios idiomas
     */
    public void reacondicionarBBDDRepostajesCarreteraYPagos() {
        bbdd = bbddhelper.getWritableDatabase();
        if (bbdd.getVersion() < 14) {
            Cursor cursor = bbdd.rawQuery(Constantes.SelectTodosLosRepostajesPorFechaDesc, null);
            ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
            if (cursor.moveToFirst()) {
                do {

                    Repostaje repostaje = new Repostaje();
                    Integer idRepostaje = Integer.valueOf(cursor.getInt(0));
                    repostaje.setIdRepostaje(idRepostaje);

                    Integer idCoche = Integer.valueOf(cursor.getInt(1));
                    repostaje.setIdCoche(idCoche);

                    Integer idCombustible = Integer.valueOf(cursor
                            .getInt(2));
                    repostaje.setIdCombustible(idCombustible);

                    BigDecimal kmRepostaje = new BigDecimal
                            (cursor.getFloat(3));
                    if (null != kmRepostaje) {
                        repostaje.setKmRepostaje(kmRepostaje);
                    } else {
                        repostaje.setKmRepostaje(BigDecimal.ZERO);
                    }

                    BigDecimal litros = new BigDecimal(cursor.getFloat(4));
                    if (null != litros) {
                        repostaje.setLitros(litros);
                    } else {
                        repostaje.setLitros(BigDecimal.ZERO);
                    }

                    BigDecimal precioLitro = new BigDecimal(
                            cursor.getFloat(5));
                    if (null != precioLitro) {
                        repostaje.setPrecioLitro(precioLitro);
                    } else {
                        repostaje.setPrecioLitro(BigDecimal.ZERO);
                    }

                    BigDecimal costeRepostaje = new BigDecimal(
                            cursor.getFloat(6));
                    if (null != costeRepostaje) {
                        repostaje.setCosteRepostaje(costeRepostaje);
                    } else {
                        repostaje.setCosteRepostaje(BigDecimal.ZERO);
                    }

                    if (cursor.getInt(7) == 0) {
                        repostaje.setEsCompleto(false);
                    } else {
                        repostaje.setEsCompleto(true);
                    }

                    if (cursor.getInt(8) == 0) {
                        repostaje.setEsAA(false);
                    } else {
                        repostaje.setEsAA(true);
                    }

                    if (cursor.getInt(9) == 0) {
                        repostaje.setEsRemolque(false);
                    } else {
                        repostaje.setEsRemolque(true);
                    }

                    if (cursor.getInt(10) == 0) {
                        repostaje.setEsBaca(false);
                    } else {
                        repostaje.setEsBaca(true);
                    }

                    if (!Constantes.esVacio(cursor.getString(11))) {
                        repostaje.setTipoCarretera(cursor.getString(11));
                    } else {
                        repostaje.setTipoCarretera("");
                    }

                    if (!Constantes.esVacio(cursor.getString(12))) {
                        repostaje.setTipoPago(cursor.getString(12));
                    } else {
                        repostaje.setTipoPago("");
                    }

                    BigDecimal velocidadMedia = new BigDecimal(
                            cursor.getFloat(13));
                    if (null != velocidadMedia) {
                        repostaje.setVelocidadMedia(velocidadMedia);
                    } else {
                        repostaje.setVelocidadMedia(BigDecimal.ZERO);
                    }

                    if (!Constantes.esVacio(cursor.getString(14))) {
                        repostaje.setAreaServicio((cursor.getString(14)));
                    } else {
                        repostaje.setAreaServicio("");
                    }

                    repostaje.setFechaRespostaje(cursor.getLong(15));

                    if (!Constantes.esVacio(cursor.getString(16))) {
                        repostaje
                                .setTipoConduccion((cursor.getString(16)));
                    } else {
                        repostaje.setTipoConduccion("");
                    }

                    if (!Constantes.esVacio(cursor.getString(17))) {
                        repostaje.setComentarios((cursor.getString(17)));
                    } else {
                        repostaje.setComentarios("");
                    }

                    BigDecimal kmRecorridos = new BigDecimal(
                            cursor.getFloat(18));
                    if (null != kmRecorridos) {
                        repostaje.setKmRecorridos(kmRecorridos);
                    }

                    BigDecimal mediaConsumo = new BigDecimal(
                            cursor.getFloat(19));
                    if (null != mediaConsumo) {
                        repostaje.setMediaConsumo(mediaConsumo);
                    }
                    listaRepostajes.add(repostaje);


                } while (cursor.moveToNext());
            }
            cursor.close();

//        listTipoCarretera.add("Carretera");
//        listTipoCarretera.add("Ciudad");
//        listTipoCarretera.add("Autopista");
//        listTipoCarretera.add("Mixto");

//        <string name="tipo_carretera_carretera">Highway</string>
//        <string name="tipo_carretera_ciudad">City</string>
//        <string name="tipo_carretera_autopista">Freeway</string>
//        <string name="tipo_carretera_mixto">Mixed</string>
//
//        <string name="tipo_de_pago_efectivo">Cash</string>
//        <string name="tipo_de_pago_tarjeta">Card</string>
//        listTipoPago.add("Efectivo");
//        listTipoPago.add("Tarjeta");

            ArrayList<Repostaje> repostajesParaActualizar = new ArrayList<Repostaje>();

            for (Repostaje rep : listaRepostajes) {

                if (!rep.getTipoCarretera().equalsIgnoreCase("") || !rep.getTipoPago().equalsIgnoreCase("")) {

                    if (!rep.getTipoCarretera().equalsIgnoreCase("")) {

                        if (rep.getTipoCarretera().equalsIgnoreCase("Carretera")) {
                            rep.setTipoCarretera("tipo_carretera_carretera");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Ciudad")) {
                            rep.setTipoCarretera("tipo_carretera_ciudad");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Autopista")) {
                            rep.setTipoCarretera("tipo_carretera_autopista");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Mixto")) {
                            rep.setTipoCarretera("tipo_carretera_mixto");
                        }
                    }

                    if (!rep.getTipoPago().equalsIgnoreCase("")) {
                        if (rep.getTipoPago().equalsIgnoreCase("Efectivo")) {
                            rep.setTipoPago("tipo_de_pago_efectivo");
                        } else if (rep.getTipoPago().equalsIgnoreCase("Tarjeta")) {
                            rep.setTipoPago("tipo_de_pago_tarjeta");
                        }
                    }


                }

                if (rep.getTipoConduccion().equalsIgnoreCase("Normal")) {
                    rep.setTipoConduccion("tipo_de_conduccion_normal");
                } else if (rep.getTipoConduccion().equalsIgnoreCase("Económica")) {
                    rep.setTipoConduccion("tipo_de_conduccion_economica");
                } else if (rep.getTipoConduccion().equalsIgnoreCase("Deportiva")) {
                    rep.setTipoConduccion("tipo_de_conduccion_deportiva");
                }
                repostajesParaActualizar.add(rep);
            }

            for (Repostaje rep : repostajesParaActualizar) {


                ContentValues valores = new ContentValues();
//            valores.put("id_repostaje", rep.getIdRepostaje());
//            valores.put("id_coche", rep.getIdCoche());
//            valores.put("id_combustible", rep.getIdCombustible());
//            valores.put("km_repostaje", rep.getKmRepostaje().doubleValue());
//            valores.put("litros", rep.getLitros().doubleValue());
//            valores.put("precio_litro", rep.getPrecioLitro().doubleValue());
//            valores.put("coste_repostaje", rep.getCosteRepostaje()
//                    .doubleValue());
//            valores.put("es_completo", rep.getEsCompleto());
//            valores.put("es_aa", rep.getEsAA());
//            valores.put("es_remolque", rep.getEsRemolque());
//            valores.put("es_baca", rep.getEsBaca());
                valores.put("tipo_carretera", rep.getTipoCarretera());
                valores.put("tipo_pago", rep.getTipoPago());
//            valores.put("velocidad_media", rep.getVelocidadMedia()
//                    .doubleValue());
//            valores.put("fecha_repostaje", rep.getFechaRespostaje());
              valores.put("tipo_conduccion", rep.getTipoConduccion());
//            valores.put("comentarios", rep.getComentarios());
//            valores.put("km_recorridos", rep.getKmRecorridos().doubleValue());
//            valores.put("media_consumo", rep.getMediaConsumo().doubleValue());

                String arg[] = {rep.getIdRepostaje().toString().trim()};
                int result = bbdd.update("Repostajes", valores, "id_repostaje=?", arg);


            }

        }


    }
}
