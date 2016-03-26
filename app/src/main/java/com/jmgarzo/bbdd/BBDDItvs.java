package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Itv;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 14/01/15.
 */
public class BBDDItvs {
    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDItvs(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public void nuevaItv(Itv itv) {

        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();

        Coche coche = new Coche();
        coche = bbddCoches.getCoche(itv.getIdCoche().toString());


//        "CREATE TABLE Itvs (id_itv INTEGER PRIMARY KEY, "
//                + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche), "
//                +" fecha_itv INTEGER, precio FLOAT, estacion TEXT, resultado TEXT, observaciones TEXT) ";
        ContentValues valoresItv = new ContentValues();
        valoresItv.put("id_coche", itv.getIdCoche());
        valoresItv.put("fecha_itv", itv.getFechaItv());

        if (null != itv.getPrecio()) {
            valoresItv.put("precio", itv.getPrecio().doubleValue());
        } else {
            valoresItv.put("precio", BigDecimal.ZERO.doubleValue());
        }
        valoresItv.put("estacion", itv.getEstacion());
        valoresItv.put("resultado", itv.getResultado());
        valoresItv.put("observaciones", itv.getObservaciones());

        if (null != itv.getKmItv()) {
            valoresItv.put("km_itv", itv.getKmItv().doubleValue());
        } else {
            valoresItv.put("km_itv", BigDecimal.ZERO.doubleValue());
        }
        bbdd.insert("Itvs", null, valoresItv);

        if (null != coche && coche.getKmActuales().compareTo(itv.getKmItv()) < 0) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", itv.getKmItv().doubleValue());
            String[] arg = {itv.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg);
        }

        bbdd.close();

    }

    public Itv getItv(String idItv) {
        Itv itv = null;

        if (null != idItv) {
            bbdd = bbddhelper.getReadableDatabase();
            String argsIdItv[] = {idItv};
            Cursor cursorItv = bbdd.rawQuery(Constantes.selectItvById, argsIdItv);

            if (cursorItv.moveToFirst()) {
                itv = new Itv();
                mapearItv(cursorItv, itv);
            }
            cursorItv.close();
            bbdd.close();
        }
        return itv;
    }


    public ArrayList<Itv> getTodasLasItv() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Itv> listaItvs = new ArrayList<Itv>();
        Cursor cursorItv = bbdd.rawQuery(Constantes.selectTodasLasItvs, null);
        if (cursorItv.moveToFirst()) {
            do {
                Itv itv = new Itv();
                mapearItv(cursorItv, itv);
                listaItvs.add(itv);
            } while (cursorItv.moveToNext());
        }
        cursorItv.close();
        bbdd.close();
        return listaItvs;
    }

    public void actualizarItv(Itv itv) {
        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(itv.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_coche", itv.getIdCoche());
        valores.put("fecha_itv", itv.getFechaItv());
        if (null != itv.getPrecio()) {
            valores.put("precio", itv.getPrecio().doubleValue());
        } else {
            valores.put("precio", BigDecimal.ZERO.doubleValue());
        }

        valores.put("estacion", itv.getEstacion());
        valores.put("resultado", itv.getResultado());
        valores.put("observaciones", itv.getObservaciones());

        if (null != itv.getKmItv()) {
            valores.put("km_itv", itv.getKmItv().doubleValue());
        } else {
            valores.put("km_itv", BigDecimal.ZERO.doubleValue());
        }
        String arg[] = {itv.getIdItv().toString().trim()};
        int result = bbdd.update("Itvs", valores, "id_itv=?", arg);


        if (null != coche && coche.getKmActuales().compareTo(itv.getKmItv()) < 0) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", itv.getKmItv().doubleValue());
            String[] arg2 = {itv.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg2);
        }
        bbdd.close();
    }


    public ArrayList<Itv> getItvsPorFechaDesc() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Itv> listaItvs = new ArrayList<Itv>();


        Cursor cursorItv = bbdd.rawQuery(Constantes.selectTodasLasItvsOrdenadasPorFechaDesc, null);

        if (cursorItv.moveToFirst()) {
            do {
                Itv itv = new Itv();
                mapearItv(cursorItv, itv);
                listaItvs.add(itv);

            } while (cursorItv.moveToNext());
        }
        cursorItv.close();
        bbdd.close();
        return listaItvs;
    }


    public ArrayList<Itv> getItvsPorCocheOrdenadoFechaDesc(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Itv> listaItvs = new ArrayList<Itv>();
        String[] arg = {idCoche.toString().trim()};

        Cursor cursorItv = bbdd.rawQuery(Constantes.selectItvsPorCocheOrdenadasPorFechaDesc, arg);

        if (cursorItv.moveToFirst()) {
            do {
                Itv itv = new Itv();
                mapearItv(cursorItv, itv);
                listaItvs.add(itv);

            } while (cursorItv.moveToNext());
        }
        cursorItv.close();
        bbdd.close();
        return listaItvs;
    }

    public ArrayList<String> getNombreEstaciones() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaNombres = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectNombresEstacion, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }
                listaNombres.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaNombres;
    }

    public ArrayList<String> getResultados() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaResultados = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectResultadosItv, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }
                listaResultados.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaResultados;
    }


    public void eliminar(String idItv) {
        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idItv};
        bbdd.delete("Itvs", "id_itv=?", arg);
        bbdd.close();
    }

    private void mapearItv(Cursor cursorItv, Itv itv) {

        Integer idItv = Integer.valueOf(cursorItv.getInt(0));
        itv.setIdItv(idItv);

        Integer idCoche = Integer.valueOf(cursorItv.getInt(1));
        itv.setIdCoche(idCoche);

        Long fechaItv = Long.valueOf(cursorItv.getLong(2));
        if (null != fechaItv) {
            itv.setFechaItv(fechaItv);
        } else {
            itv.setFechaItv(Long.parseLong("0"));
        }

        BigDecimal precio = new BigDecimal(cursorItv.getDouble(3));
        if (null != precio) {
            itv.setPrecio(precio);
        } else {
            itv.setPrecio(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursorItv.getString(4))) {
            itv.setEstacion(cursorItv.getString(4));
        } else {
            itv.setEstacion("");
        }

        if (!Constantes.esVacio(cursorItv.getString(5))) {
            itv.setResultado(cursorItv.getString(5));
        } else {
            itv.setResultado("");
        }

        if (!Constantes.esVacio(cursorItv.getString(6))) {
            itv.setObservaciones(cursorItv.getString(6));
        } else {
            itv.setObservaciones("");
        }

        BigDecimal kmItv = new BigDecimal(cursorItv.getDouble(7));
        if (null != kmItv) {
            itv.setKmItv(kmItv);
        } else {
            itv.setKmItv(BigDecimal.ZERO);
        }
    }


}
