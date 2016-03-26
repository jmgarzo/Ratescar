package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BBDDRepostajes {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDRepostajes(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE,
                null, Constantes.BBDD_VERSION);

    }

    /**
     * id_repostaje ->INTEGER id_coche ->INTEGER id_combustible ->INTEGER
     * km_repostaje ->INTEGER litros ->FLOAT precio_litro ->FLOAT coste
     * repostaje->FLOAT es_completo ->BOOLEAN es_aa ->BOOLEAN es_remolque
     * ->BOOLEAN es_baca -> BOOLEAN tipo_carretera ->TEXT tipo_pago ->TEXT
     * velocidad_media ->FLOAT area_servicio ->TEXT fecha_repostaje ->DATE
     * tipo_conduccion ->TEXT comentarios ->TEXT km_recorridos->FLOAT, media_consumo->FLOAT
     */

    public void nuevoRepostaje(Repostaje repostaje) {

        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();

        Coche coche = new Coche();

        coche = bbddCoches.getCoche(repostaje.getIdCoche().toString());

        ContentValues valoresRepostaje = new ContentValues();
        valoresRepostaje.put("id_coche", repostaje.getIdCoche());
        valoresRepostaje.put("id_combustible", repostaje.getIdCombustible());
        valoresRepostaje.put("km_repostaje", repostaje.getKmRepostaje().doubleValue());
        valoresRepostaje.put("litros", repostaje.getLitros().doubleValue());
        valoresRepostaje.put("precio_litro", repostaje.getPrecioLitro()
                .doubleValue());
        valoresRepostaje.put("coste_repostaje", repostaje.getCosteRepostaje()
                .doubleValue());
        valoresRepostaje.put("es_completo", repostaje.getEsCompleto());
        valoresRepostaje.put("es_aa", repostaje.getEsAA());
        valoresRepostaje.put("es_remolque", repostaje.getEsRemolque());
        valoresRepostaje.put("es_baca", repostaje.getEsBaca());
        valoresRepostaje.put("tipo_carretera", repostaje.getTipoCarretera());
        valoresRepostaje.put("tipo_pago", repostaje.getTipoPago());
        valoresRepostaje.put("velocidad_media", repostaje.getVelocidadMedia()
                .doubleValue());
        valoresRepostaje.put("area_servicio", repostaje.getAreaServicio());
        valoresRepostaje.put("fecha_repostaje", repostaje.getFechaRespostaje());
        valoresRepostaje.put("tipo_conduccion", repostaje.getTipoConduccion());
        valoresRepostaje.put("comentarios", repostaje.getComentarios());
        valoresRepostaje.put("km_recorridos", repostaje.getKmRecorridos().doubleValue());
        valoresRepostaje.put("media_consumo", repostaje.getMediaConsumo().doubleValue());


        bbdd.insert("Repostajes", null, valoresRepostaje);


        if (null != coche && coche.getKmActuales().compareTo(repostaje.getKmRepostaje()) == -1) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", repostaje.getKmRepostaje().doubleValue());
            String[] arg = {repostaje.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg);
        }

        bbdd.close();

    }

    public Repostaje getRepostaje(String idRepostaje) {

        Repostaje repostaje = null;

        if (null != idRepostaje) {

            bbdd = bbddhelper.getReadableDatabase();
            String idRepos[] = {idRepostaje};
            Cursor cursorRepostaje = bbdd.rawQuery(Constantes.SelectRepostajeById, idRepos);

            if (cursorRepostaje.moveToFirst()) {

                do {
                    repostaje = new Repostaje();
                    mapearRepostaje(cursorRepostaje, repostaje);
                } while (cursorRepostaje.moveToNext());

            }

            cursorRepostaje.close();
            bbdd.close();
        }
        return repostaje;

    }


    public ArrayList<Repostaje> getTodosLosRepostajes() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();

        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectTodosLosRepostajes, null);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());
        }
        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }

    public ArrayList<Repostaje> getTodosLosRepostajesOrdenadosPorFecheDesc() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();

        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectTodosLosRepostajesPorFechaDesc, null);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());
        }
        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }


    public ArrayList<Repostaje> getTodosLosRepostajesOrdenadoPorCoche() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();

        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectTodosLosRepostajesOrdenadoPorCoche, null);

        if (cursorRepostaje.moveToFirst()) {
            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());

        }

        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }


    public void actualizarRespostaje(Repostaje repostaje) {

        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(repostaje.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_repostaje", repostaje.getIdRepostaje());
        valores.put("id_coche", repostaje.getIdCoche());
        valores.put("id_combustible", repostaje.getIdCombustible());
        valores.put("km_repostaje", repostaje.getKmRepostaje().doubleValue());
        valores.put("litros", repostaje.getLitros().doubleValue());
        valores.put("precio_litro", repostaje.getPrecioLitro().doubleValue());
        valores.put("coste_repostaje", repostaje.getCosteRepostaje()
                .doubleValue());
        valores.put("es_completo", repostaje.getEsCompleto());
        valores.put("es_aa", repostaje.getEsAA());
        valores.put("es_remolque", repostaje.getEsRemolque());
        valores.put("es_baca", repostaje.getEsBaca());
        valores.put("tipo_carretera", repostaje.getTipoCarretera());
        valores.put("tipo_pago", repostaje.getTipoPago());
        valores.put("velocidad_media", repostaje.getVelocidadMedia()
                .doubleValue());
        valores.put("fecha_repostaje", repostaje.getFechaRespostaje());
        valores.put("tipo_conduccion", repostaje.getTipoConduccion());
        valores.put("comentarios", repostaje.getComentarios());
        valores.put("km_recorridos", repostaje.getKmRecorridos().doubleValue());
        valores.put("media_consumo", repostaje.getMediaConsumo().doubleValue());

        String arg[] = {repostaje.getIdRepostaje().toString().trim()};
        int result = bbdd.update("Repostajes", valores, "id_repostaje=?", arg);

        if (null != coche && coche.getKmActuales().compareTo(repostaje.getKmRepostaje()) < 0) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", repostaje.getKmRepostaje().doubleValue());
            String[] arg2 = {repostaje.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg2);
        }

        bbdd.close();
    }

    public ArrayList<Repostaje> getRepostajesPorCocheOrdenadosFecha(Repostaje nuevoRepostaje) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();
        Repostaje repostajeAnterior = null;

        String[] args = {nuevoRepostaje.getIdCoche().toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectAnteriorRepostaje, null);

        if (cursorRepostaje.moveToFirst()) {
            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());
        }
        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }


    public ArrayList<Repostaje> getRepostajesPorCocheOrdenadosFecha(String idCocheBuscar) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();
        Repostaje repostajeAnterior = null;

        String[] args = {idCocheBuscar};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectAnteriorRepostaje, args);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();

                mapearRepostaje(cursorRepostaje, repostaje);

                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());

        }

        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }

    public ArrayList<Repostaje> getRepostajesPorCocheOrdenadosPorKmDesc(String idCocheBuscar) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();
        Repostaje repostajeAnterior = null;

        String[] args = {idCocheBuscar};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectRepostajesPorCocheOrdenadosKmDesc, args);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();

                mapearRepostaje(cursorRepostaje, repostaje);

                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());

        }

        cursorRepostaje.close();
        bbdd.close();
        return listRepostajes;
    }


    public ArrayList<Repostaje> getRepostajesPorCocheYEntreFechasOrdenadosFecha(Integer idCocheBuscar, Long fechaInicio, Long fechaFin) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();
        String[] args = {idCocheBuscar.toString(), fechaInicio.toString(), fechaFin.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectRepostajesPorCocheYFechas, args);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());
        }

        cursorRepostaje.close();
        bbdd.close();

        return listRepostajes;


    }


    public Repostaje getRepostajeAnteriorPorCoche(Repostaje nuevoRepostaje) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Repostaje> listRepostajes = new ArrayList<Repostaje>();
        Repostaje repostajeAnterior = null;

        String[] args = {nuevoRepostaje.getIdCoche().toString(), nuevoRepostaje.getKmRepostaje().toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectAnteriorRepostaje, args);

        if (cursorRepostaje.moveToFirst()) {

            do {
                Repostaje repostaje = new Repostaje();
                mapearRepostaje(cursorRepostaje, repostaje);
                listRepostajes.add(repostaje);
            } while (cursorRepostaje.moveToNext());

        }

        cursorRepostaje.close();
        bbdd.close();

        if (listRepostajes.isEmpty()) {
            return null;
        } else {
            return listRepostajes.get(0);
        }
    }


    public BigDecimal getKmUltimoRespostajePorCoche(Integer idCoche) {

        bbdd = bbddhelper.getReadableDatabase();
        BigDecimal kmUltimoRepostaje = new BigDecimal(0);
        String[] args = {idCoche.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectKmAnteriorRepostaje, args);

        if (cursorRepostaje.moveToFirst()) {
            if (null != BigDecimal.valueOf(cursorRepostaje.getFloat(0))) {
                kmUltimoRepostaje = BigDecimal.valueOf(cursorRepostaje.getFloat(0));
            }
        }
        cursorRepostaje.close();
        bbdd.close();
        return kmUltimoRepostaje;

    }


    public void eliminarRepostaje(Integer idRepostaje) {
        bbdd = bbddhelper.getWritableDatabase();



        String arg[] = {idRepostaje.toString()};
        bbdd.delete("Repostajes", "id_repostaje=?", arg);

        bbdd.close();
    }


    public Long getFechaPrimerRepostajePorCoche(Integer idCoche) {

        bbdd = bbddhelper.getReadableDatabase();
        Long FechaPrimerRepostaje = new Long(0);
        String[] args = {idCoche.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectFechaPrimerRepostajePorCoche, args);

        if (cursorRepostaje.moveToFirst()) {
            if (null != Long.valueOf(cursorRepostaje.getLong(0))) {
                FechaPrimerRepostaje = cursorRepostaje.getLong(0);
            }
        }
        cursorRepostaje.close();
        bbdd.close();

        return FechaPrimerRepostaje;
    }

    public Long getFechaPrimerRepostaje() {

        bbdd = bbddhelper.getReadableDatabase();
        Long FechaPrimerRepostaje = new Long(0);
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectFechaPrimerRepostaje, null);

        if (cursorRepostaje.moveToFirst()) {
            if (null != Long.valueOf(cursorRepostaje.getLong(0))) {
                FechaPrimerRepostaje = Long.valueOf(cursorRepostaje.getLong(0));
            }
        }
        cursorRepostaje.close();
        bbdd.close();

        return FechaPrimerRepostaje;

    }


    public Long getFechaUltimoRespotaje() {

        bbdd = bbddhelper.getReadableDatabase();
        Long FechaUltimoRepostaje = new Long(0);
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectFechaUltimoRepostaje, null);

        if (cursorRepostaje.moveToFirst()) {
            if (null != Long.valueOf(cursorRepostaje.getLong(0))) {
                FechaUltimoRepostaje = Long.valueOf(cursorRepostaje.getLong(0));
            }

        }
        cursorRepostaje.close();
        bbdd.close();

        return FechaUltimoRepostaje;
    }


    public Long getFechaUltimoRepostajePorCoche(Integer idCoche) {

        bbdd = bbddhelper.getReadableDatabase();
        Long FechaUltimoRepostaje = new Long(0);
        String[] args = {idCoche.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectFechaUltimoRepostajePorCoche, args);

        if (cursorRepostaje.moveToFirst()) {
            if (null != Long.valueOf(cursorRepostaje.getLong(0))) {
                FechaUltimoRepostaje = cursorRepostaje.getLong(0);
            }
        }

        cursorRepostaje.close();
        bbdd.close();
        return FechaUltimoRepostaje;

    }

    public ArrayList<Number> getConsumoMedioPorCocheOrdenadoFecha(Integer idCoche) {
        ArrayList<Number> listaConsumos = new ArrayList<Number>();
        bbdd = bbddhelper.getReadableDatabase();

        String[] args = {idCoche.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectConsumosMediosPorCocheOrdenadoFecha, args);

        if (cursorRepostaje.moveToFirst()) {
            do {
                listaConsumos.add(cursorRepostaje.getDouble(0));
            } while (cursorRepostaje.moveToNext());
        }
        return listaConsumos;

    }


    public ArrayList<Number> getConsumoMedioPorCocheYFecha(Integer idCoche, Long fechaInicio, Long fechaFin) {
        ArrayList<Number> listaConsumos = new ArrayList<Number>();
        bbdd = bbddhelper.getReadableDatabase();

        String[] args = {idCoche.toString(), fechaInicio.toString(), fechaFin.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectConsumosMediosPorCocheYFechas, args);

        if (cursorRepostaje.moveToFirst()) {
            do {
                listaConsumos.add(cursorRepostaje.getDouble(0));
            } while (cursorRepostaje.moveToNext());
        }
        return listaConsumos;

    }

    public Integer getNumeroRepostajesPorCocheyFecha(Integer idCoche, Long fechaInicio, Long fechaFin) {
        bbdd = bbddhelper.getReadableDatabase();
        Integer numeroRepostajes = 0;
        String[] args = {idCoche.toString(), fechaInicio.toString(), fechaFin.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectNumeroRepostajesPorCocheYFecha, args);

        if (cursorRepostaje.moveToFirst()) {
            try {
                numeroRepostajes = cursorRepostaje.getInt(0);
            } catch (Exception e) {
                e.printStackTrace();
                numeroRepostajes = 0;
            }
        }
        return numeroRepostajes;
    }


    public Integer getUltimoCocheQueReposto() {
        bbdd = bbddhelper.getReadableDatabase();

        Integer idCoche = 0;
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.SelectUltimoCocheQueReposto, null);
        if (cursorCoche.moveToFirst()) {
            if (null != Integer.valueOf(cursorCoche.getInt(0))) {
                idCoche = cursorCoche.getInt(0);
            }
        }

        cursorCoche.close();
        bbdd.close();
        return idCoche;
    }

    public boolean hayUnCompleto(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();
        Integer numRepostajesCompletos = 0;
        boolean resultado = false;

        String[] args = {idCoche.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectNumeroRepostajesCompletosPorIdCoche, args);
        if (cursorRepostaje.moveToFirst()) {
            if (null != Integer.valueOf(cursorRepostaje.getInt(0))) {
                numRepostajesCompletos = cursorRepostaje.getInt(0);
            }
        }
        if (numRepostajesCompletos > 0) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }

    public boolean hayUnCompletoAnterior(Integer idCoche, BigDecimal kmRepostaje) {
        bbdd = bbddhelper.getReadableDatabase();
        Integer numRepostajesCompletos = 0;
        boolean resultado = false;

        String[] args = {idCoche.toString(), kmRepostaje.toString()};
        Cursor cursorRepostaje = bbdd.rawQuery(
                Constantes.SelectNumeroRepostajesCompletosAnterioresPorIdCoche, args);
        if (cursorRepostaje.moveToFirst()) {
            if (null != Integer.valueOf(cursorRepostaje.getInt(0))) {
                numRepostajesCompletos = cursorRepostaje.getInt(0);
            }
        }
        if (numRepostajesCompletos > 0) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }


    public BigDecimal getkmInicialesPorCoche(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();


        String[] args = {idCoche.toString().trim()};

        Cursor cursorRepostaje = bbdd.rawQuery(Constantes.SelectKmInicialesRepostajePorCoche, args);

        BigDecimal kmIniciales = BigDecimal.ZERO;
        if (cursorRepostaje.moveToFirst()) {
            if (null != kmIniciales) {
                kmIniciales = new BigDecimal(cursorRepostaje.getDouble(0));
            }
        }

        return kmIniciales;
    }

    public BigDecimal getkmFinalesPorCoche(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();


        String[] args = {idCoche.toString().trim()};

        Cursor cursorRepostaje = bbdd.rawQuery(Constantes.SelectKmFinalesRepostajePorCoche, args);

        BigDecimal kmFinales = BigDecimal.ZERO;
        if (cursorRepostaje.moveToFirst()) {
            if (null != kmFinales) {
                kmFinales = new BigDecimal(cursorRepostaje.getDouble(0));
            }
        }

        return kmFinales;
    }

    public ArrayList<String> getEstaciones() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaEstaciones = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectEstacionesServicio, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }
                listaEstaciones.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaEstaciones;
    }


    private void mapearRepostaje(Cursor cursorRepostaje, Repostaje repostaje) {

        Integer idRepostaje = Integer.valueOf(cursorRepostaje.getInt(0));
        repostaje.setIdRepostaje(idRepostaje);

        Integer idCoche = Integer.valueOf(cursorRepostaje.getInt(1));
        repostaje.setIdCoche(idCoche);

        Integer idCombustible = Integer.valueOf(cursorRepostaje
                .getInt(2));
        repostaje.setIdCombustible(idCombustible);

        BigDecimal kmRepostaje = new BigDecimal
                (cursorRepostaje.getFloat(3));
        if (null != kmRepostaje) {
            repostaje.setKmRepostaje(kmRepostaje);
        } else {
            repostaje.setKmRepostaje(BigDecimal.ZERO);
        }

        BigDecimal litros = new BigDecimal(cursorRepostaje.getFloat(4));
        if (null != litros) {
            repostaje.setLitros(litros);
        } else {
            repostaje.setLitros(BigDecimal.ZERO);
        }

        BigDecimal precioLitro = new BigDecimal(
                cursorRepostaje.getFloat(5));
        if (null != precioLitro) {
            repostaje.setPrecioLitro(precioLitro);
        } else {
            repostaje.setPrecioLitro(BigDecimal.ZERO);
        }

        BigDecimal costeRepostaje = new BigDecimal(
                cursorRepostaje.getFloat(6));
        if (null != costeRepostaje) {
            repostaje.setCosteRepostaje(costeRepostaje);
        } else {
            repostaje.setCosteRepostaje(BigDecimal.ZERO);
        }

        if (cursorRepostaje.getInt(7) == 0) {
            repostaje.setEsCompleto(false);
        } else {
            repostaje.setEsCompleto(true);
        }

        if (cursorRepostaje.getInt(8) == 0) {
            repostaje.setEsAA(false);
        } else {
            repostaje.setEsAA(true);
        }

        if (cursorRepostaje.getInt(9) == 0) {
            repostaje.setEsRemolque(false);
        } else {
            repostaje.setEsRemolque(true);
        }

        if (cursorRepostaje.getInt(10) == 0) {
            repostaje.setEsBaca(false);
        } else {
            repostaje.setEsBaca(true);
        }

        if (!Constantes.esVacio(cursorRepostaje.getString(11))) {
            repostaje.setTipoCarretera(cursorRepostaje.getString(11));
        } else {
            repostaje.setTipoCarretera("");
        }

        if (!Constantes.esVacio(cursorRepostaje.getString(12))) {
            repostaje.setTipoPago(cursorRepostaje.getString(12));
        } else {
            repostaje.setTipoPago("");
        }

        BigDecimal velocidadMedia = new BigDecimal(
                cursorRepostaje.getFloat(13));
        if (null != velocidadMedia) {
            repostaje.setVelocidadMedia(velocidadMedia);
        } else {
            repostaje.setVelocidadMedia(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursorRepostaje.getString(14))) {
            repostaje.setAreaServicio((cursorRepostaje.getString(14)));
        } else {
            repostaje.setAreaServicio("");
        }

        repostaje.setFechaRespostaje(cursorRepostaje.getLong(15));

        if (!Constantes.esVacio(cursorRepostaje.getString(16))) {
            repostaje
                    .setTipoConduccion((cursorRepostaje.getString(16)));
        } else {
            repostaje.setTipoConduccion("");
        }

        if (!Constantes.esVacio(cursorRepostaje.getString(17))) {
            repostaje.setComentarios((cursorRepostaje.getString(17)));
        } else {
            repostaje.setComentarios("");
        }

        BigDecimal kmRecorridos = new BigDecimal(
                cursorRepostaje.getFloat(18));
        if (null != kmRecorridos) {
            repostaje.setKmRecorridos(kmRecorridos);
        }

        BigDecimal mediaConsumo = new BigDecimal(
                cursorRepostaje.getFloat(19));
        if (null != mediaConsumo) {
            repostaje.setMediaConsumo(mediaConsumo);
        }

    }


}
