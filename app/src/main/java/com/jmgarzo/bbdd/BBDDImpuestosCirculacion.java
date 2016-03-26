package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.ImpuestoCirculacion;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 11/03/15.
 */
public class BBDDImpuestosCirculacion {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;


    public BBDDImpuestosCirculacion(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public void nuevoImpuesto(ImpuestoCirculacion impuestoCirculacion) {
        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();

        Coche coche = new Coche();
        coche = bbddCoches.getCoche(impuestoCirculacion.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_coche", impuestoCirculacion.getIdCoche());

        if (null != impuestoCirculacion.getImporte()) {
            valores.put("importe", impuestoCirculacion.getImporte().doubleValue());
        } else {
            valores.put("importe", BigDecimal.ZERO.doubleValue());
        }

        valores.put("anualidad", impuestoCirculacion.getAnualidad());
        valores.put("fecha_fin_pago", impuestoCirculacion.getFechaFinPago());

        valores.put("comentarios", impuestoCirculacion.getComentarios());


        bbdd.insert("Impuestos_Circulacion", null, valores);

    }


    public ImpuestoCirculacion getImpuesto(String idImpuesto) {
        ImpuestoCirculacion impuestoCirculacion = null;

        if (null != idImpuesto) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idImpuesto};
            Cursor cursor = bbdd.rawQuery(Constantes.selectImpuestoCirculacionById, args);

            if (cursor.moveToFirst()) {
                impuestoCirculacion = new ImpuestoCirculacion();
                mapearImpuesto(cursor, impuestoCirculacion);
            }
            cursor.close();
            bbdd.close();
        }
        return impuestoCirculacion;
    }


    public void actualizarImpuesto(ImpuestoCirculacion impuesto) {
        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(impuesto.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_coche", impuesto.getIdCoche());

        if (null != impuesto.getImporte()) {
            valores.put("importe", impuesto.getImporte().doubleValue());
        } else {
            valores.put("importe", BigDecimal.ZERO.doubleValue());
        }

        valores.put("anualidad", impuesto.getAnualidad());

        valores.put("fecha_fin_pago", impuesto.getFechaFinPago());

        valores.put("comentarios", impuesto.getComentarios());


        String arg[] = {impuesto.getIdImpuesto().toString().trim()};
        int result = bbdd.update("Impuestos_Circulacion", valores, "id_impuesto=?", arg);

    }


    public ArrayList<ImpuestoCirculacion> getImpuestosPorFechaDesc() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<ImpuestoCirculacion> listaImpuestos = new ArrayList<ImpuestoCirculacion>();


        Cursor cursor = bbdd.rawQuery(Constantes.selectTodosLosImpuestosCirculacionPorFechaDesc, null);

        if (cursor.moveToFirst()) {
            do {
                ImpuestoCirculacion impuestoCirculacion = new ImpuestoCirculacion();
                mapearImpuesto(cursor, impuestoCirculacion);
                listaImpuestos.add(impuestoCirculacion);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaImpuestos;
    }

    public void eliminar(String idImpuesto) {
        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idImpuesto};
        bbdd.delete("Impuestos_Circulacion", "id_impuesto=?", arg);
        bbdd.close();
    }


    public ArrayList<ImpuestoCirculacion> getImpuestosPorCocheOrdenadosFechaDesc(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<ImpuestoCirculacion> listaImpuestos = new ArrayList<ImpuestoCirculacion>();
        String[] arg = {idCoche.toString().trim()};

        Cursor cursor = bbdd.rawQuery(Constantes.selectImpuestoCirculacionPorCocheOrdenadasPorFechaDesc, arg);

        if (cursor.moveToFirst()) {
            do {
                ImpuestoCirculacion impuestoCirculacion = new ImpuestoCirculacion();
                mapearImpuesto(cursor, impuestoCirculacion);
                listaImpuestos.add(impuestoCirculacion);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaImpuestos;
    }


    private void mapearImpuesto(Cursor cursor, ImpuestoCirculacion impuestoCirculacion) {

        Integer idImpuestoCirculacion = Integer.valueOf(cursor.getInt(0));
        impuestoCirculacion.setIdImpuesto(idImpuestoCirculacion);

        Integer idCoche = Integer.valueOf(cursor.getInt(1));
        impuestoCirculacion.setIdCoche(idCoche);

        BigDecimal importe = new BigDecimal(cursor.getDouble(2));
        if (null != importe) {
            impuestoCirculacion.setImporte(importe);
        } else {
            impuestoCirculacion.setImporte(BigDecimal.ZERO);
        }

        Long anualidad = Long.valueOf(cursor.getLong(3));
        if (null != anualidad) {
            impuestoCirculacion.setAnualidad(anualidad);
        } else {
            impuestoCirculacion.setAnualidad(Long.parseLong("0"));
        }

        Long fechaFin = Long.valueOf(cursor.getLong(4));
        if (null != fechaFin) {
            impuestoCirculacion.setFechaFinPago(fechaFin);
        } else {
            impuestoCirculacion.setFechaFinPago(Long.parseLong("0"));
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            impuestoCirculacion.setComentarios(cursor.getString(5));
        } else {
            impuestoCirculacion.setComentarios("");
        }


    }

}
