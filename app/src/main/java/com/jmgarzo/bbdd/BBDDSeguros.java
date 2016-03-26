package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Seguro;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 21/01/15.
 */
public class BBDDSeguros {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDSeguros(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public void nuevoSeguro(Seguro seguro) {
        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();

        Coche coche = new Coche();
        coche = bbddCoches.getCoche(seguro.getIdCoche().toString());


        ContentValues valores = new ContentValues();
        valores.put("id_coche", seguro.getIdCoche());
        valores.put("compania", seguro.getCompania());
        valores.put("prima", seguro.getPrima().doubleValue());
        valores.put("tipo_de_seguro", seguro.getTipoSeguro());
        valores.put("numero_poliza", seguro.getNumeroPoliza());
        valores.put("fecha_inicio", seguro.getFechaInicio());
        valores.put("fecha_vencimiento", seguro.getFechaVencimiento());
        valores.put("periodicidad_digito", seguro.getPeriodicidadDigito());
        valores.put("periodicidad_unidad", seguro.getPeriodicidadUnidad());
        valores.put("observaciones", seguro.getObservaciones());

        bbdd.insert("Seguros", null, valores);
        bbdd.close();
    }

    public Seguro getSeguro(String idSeguro) {
        Seguro seguro = null;

        if (null != idSeguro) {
            bbdd = bbddhelper.getReadableDatabase();
            String argsIdSeguro[] = {idSeguro};
            Cursor cursor = bbdd.rawQuery(Constantes.selectSeguroById, argsIdSeguro);

            if (cursor.moveToFirst()) {
                seguro = new Seguro();
                mapearSeguro(cursor, seguro);
            }
            cursor.close();
            bbdd.close();

        }
        return seguro;
    }


    public ArrayList<Seguro> getTodosLosSeguros() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Seguro> listaSeguros = new ArrayList<Seguro>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectTodosLosSeguros, null);

        if (cursor.moveToNext()) {
            do {
                Seguro seguro = new Seguro();
                mapearSeguro(cursor, seguro);
                listaSeguros.add(seguro);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaSeguros;
    }

    public ArrayList<Seguro> getSegurosPorCocheOrdenadoFechaDesc(Integer idCoche) {
        ArrayList<Seguro> listaSeguros = new ArrayList<Seguro>();

        if (null != idCoche) {
            bbdd = bbddhelper.getReadableDatabase();
            String[] arg = {idCoche.toString().trim()};

            Cursor cursor = bbdd.rawQuery(Constantes.selectSegurosPorCocheOrdenadoPorFechaDesc, arg);

            if (cursor.moveToNext()) {
                do {
                    Seguro seguro = new Seguro();
                    mapearSeguro(cursor, seguro);
                    listaSeguros.add(seguro);

                } while (cursor.moveToNext());
            }
            cursor.close();
            bbdd.close();
        }
        return listaSeguros;
    }


    public void actualizarSeguro(Seguro seguro) {


        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(seguro.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_coche", seguro.getIdCoche());
        valores.put("compania", seguro.getCompania());
        valores.put("prima", seguro.getPrima().doubleValue());
        valores.put("tipo_de_seguro", seguro.getTipoSeguro());
        valores.put("numero_poliza", seguro.getNumeroPoliza());
        valores.put("fecha_inicio", seguro.getFechaInicio());
        valores.put("fecha_vencimiento", seguro.getFechaVencimiento());
        valores.put("periodicidad_digito", seguro.getPeriodicidadDigito());
        valores.put("periodicidad_unidad", seguro.getPeriodicidadUnidad());
        valores.put("observaciones", seguro.getObservaciones());

        String arg[] = {seguro.getIdSeguro().toString().trim()};
        int result = bbdd.update("Seguros", valores, "id_seguro=?", arg);
    }


    public ArrayList<Seguro> getSegurosPorFecha() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Seguro> listaSeguros = new ArrayList<Seguro>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectTodosLosSegurosPorFecha, null);

        if (cursor.moveToNext()) {
            do {
                Seguro seguro = new Seguro();
                mapearSeguro(cursor, seguro);
                listaSeguros.add(seguro);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaSeguros;
    }

    public ArrayList<String> getPolizas() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaPolizas = new ArrayList<String>();
        Cursor cursor = bbdd.rawQuery(Constantes.selectPolizas, null);
        if (cursor.moveToNext()) {
            do {
                String poliza = "";
                if (!Constantes.esVacio(cursor.getString(0))) {
                    poliza = cursor.getString(0);
                }

                listaPolizas.add(poliza);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaPolizas;
    }

    public ArrayList<String> getCompanias() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaCompanias = new ArrayList<String>();
        Cursor cursor = bbdd.rawQuery(Constantes.selectCompanias, null);
        if (cursor.moveToNext()) {
            do {
                String compania = "";
                if (!Constantes.esVacio(cursor.getString(0))) {
                    compania = cursor.getString(0);
                }

                listaCompanias.add(compania);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaCompanias;
    }


    public void eliminar(String idSeguro) {
        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idSeguro};
        bbdd.delete("Seguros", "id_seguro=?", arg);
        bbdd.close();
    }


    private void mapearSeguro(Cursor cursor, Seguro seguro) {

        Integer idSeguro = Integer.valueOf(cursor.getInt(0));
        seguro.setIdSeguro(idSeguro);

        Integer idCoche = Integer.valueOf(cursor.getInt(1));
        seguro.setIdCoche(idCoche);

        if (!Constantes.esVacio(cursor.getString(2))) {
            seguro.setCompania(cursor.getString(2));
        } else {
            seguro.setCompania("");
        }

        BigDecimal prima = new BigDecimal(cursor.getDouble(3));
        if (null != prima) {
            seguro.setPrima(prima);
        } else {
            seguro.setPrima(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursor.getString(4))) {
            seguro.setTipoSeguro(cursor.getString(4));
        } else {
            seguro.setTipoSeguro("");
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            seguro.setNumeroPoliza(cursor.getString(5));
        } else {
            seguro.setNumeroPoliza("");
        }

        Long fechaInicio = Long.valueOf(cursor.getLong(6));
        if (null != fechaInicio) {
            seguro.setFechaInicio(fechaInicio);
        } else {
            seguro.setFechaInicio(Long.parseLong("0"));
        }

        Long fechaVencimiento = Long.valueOf(cursor.getLong(7));
        if (null != fechaVencimiento) {
            seguro.setFechaVencimiento(fechaVencimiento);
        } else {
            seguro.setFechaVencimiento(Long.parseLong("0"));
        }

        Integer periodicidadDigito = Integer.valueOf(cursor.getInt(8));
        if (null != periodicidadDigito) {
            seguro.setPeriodicidadDigito(periodicidadDigito);
        } else {
            seguro.setPeriodicidadDigito(0);
        }

        if (!Constantes.esVacio(cursor.getString(9))) {
            seguro.setPeriodicidadUnidad(cursor.getString(9));
        } else {
            seguro.setPeriodicidadUnidad("");
        }


        if (!Constantes.esVacio(cursor.getString(10))) {
            seguro.setObservaciones(cursor.getString(10));
        } else {
            seguro.setObservaciones("");
        }


    }

}


