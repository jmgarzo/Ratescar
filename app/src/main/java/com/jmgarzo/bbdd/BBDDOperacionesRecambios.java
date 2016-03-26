package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.OperacionRecambio;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 15/04/15.
 */
public class BBDDOperacionesRecambios {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDOperacionesRecambios(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }


    public void nuevoOperacionRecambio(OperacionRecambio operacionRecambio) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valoresOperacionRecambio = new ContentValues();
        valoresOperacionRecambio.put("id_operacion", operacionRecambio.getIdOperacion());
        valoresOperacionRecambio.put("id_recambio", operacionRecambio.getIdRecambio());
        valoresOperacionRecambio.put("cantidad", operacionRecambio.getCantidad().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        valoresOperacionRecambio.put("precio_unidad", operacionRecambio.getPrecioUnidad().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        valoresOperacionRecambio.put("coste", operacionRecambio.getCoste().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

        bbdd.insert("OperacionRecambio", null, valoresOperacionRecambio);
        bbdd.close();
    }

    public Long nuevoOperacionRecambio(Integer idOperacion, Integer idRecambio) {

        bbdd = bbddhelper.getWritableDatabase();
        Long resultado;
        ContentValues valoresOperacionRecambio = new ContentValues();
        valoresOperacionRecambio.put("id_operacion", idOperacion);
        valoresOperacionRecambio.put("id_recambio",idRecambio);
        valoresOperacionRecambio.put("cantidad", BigDecimal.ZERO.doubleValue());
        valoresOperacionRecambio.put("precio_unidad", BigDecimal.ZERO.doubleValue());
        valoresOperacionRecambio.put("coste", BigDecimal.ZERO.doubleValue());

        resultado = bbdd.insert("OperacionRecambio", null, valoresOperacionRecambio);
        bbdd.close();

        return resultado;
    }

    public int actualizarOperacionRecambio(OperacionRecambio operacionRecambio) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        if (null != operacionRecambio.getCantidad()) {
            valores.put("cantidad", operacionRecambio.getCantidad().doubleValue());
        }else{
            valores.put("cantidad", 0);
        }

        if (null != operacionRecambio.getPrecioUnidad()) {
            valores.put("precio_unidad", operacionRecambio.getPrecioUnidad().doubleValue());
        }else{
            valores.put("precio_unidad", 0);
        }

        if (null != operacionRecambio.getCoste()) {
            valores.put("coste", operacionRecambio.getCoste().doubleValue());
        }else{
            valores.put("coste", 0);
        }

        String args[] = {operacionRecambio.getIdOperacion().toString(),operacionRecambio.getIdRecambio().toString()};
        int result = bbdd.update("OperacionRecambio", valores, "id_operacion=? AND id_recambio=? ", args);

        bbdd.close();

        return result;
    }


    public OperacionRecambio getOperacionRecambiosById(String idOperacion, String idRecambio) {
        OperacionRecambio operacionRecambio = null;

        if (null != idOperacion && null != idRecambio) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idOperacion.trim(), idRecambio};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACION_RECAMBIO_BY_ID, args);

            if (cursor.moveToFirst()) {
                operacionRecambio = new OperacionRecambio();
                mapearOperacionRecambio(cursor, operacionRecambio);
            }
            cursor.close();
            bbdd.close();
        }
        return operacionRecambio;
    }

    public ArrayList<OperacionRecambio> getOperacionRecambios() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<OperacionRecambio> listaOperacionesRecambios = new ArrayList<OperacionRecambio>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACIONES_RECAMBIOS, null);

        if (cursor.moveToFirst()) {
            OperacionRecambio operacionRecambio = null;

            do {
                operacionRecambio = new OperacionRecambio();
                mapearOperacionRecambio(cursor, operacionRecambio);
                listaOperacionesRecambios.add(operacionRecambio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaOperacionesRecambios;

    }


    public ArrayList<OperacionRecambio> getOperacionesRecambiosByIdOperacion(String idOperacion) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<OperacionRecambio> listaOperacionesRecambio = new ArrayList<OperacionRecambio>();

        String [] arg ={idOperacion};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACIONES_RECAMBIOS_BY_OPERACION, arg);

        if (cursor.moveToFirst()) {
            OperacionRecambio operacionRecambio = null;

            do {
                operacionRecambio = new OperacionRecambio();
                mapearOperacionRecambio(cursor, operacionRecambio);
                listaOperacionesRecambio.add(operacionRecambio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaOperacionesRecambio;

    }


    public ArrayList<OperacionRecambio> getOperacionesRecambiosByIdRecambio(String idRecambio) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<OperacionRecambio> listaOperacionesRecambio = new ArrayList<OperacionRecambio>();

        String [] arg ={idRecambio};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACIONES_RECAMBIOS_BY_RECAMBIO, arg);

        if (cursor.moveToFirst()) {
            OperacionRecambio operacionRecambio = null;

            do {
                operacionRecambio = new OperacionRecambio();
                mapearOperacionRecambio(cursor, operacionRecambio);
                listaOperacionesRecambio.add(operacionRecambio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaOperacionesRecambio;

    }


    public void eliminar_por_idRecambio(String idRecambio){

        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idRecambio};

        bbdd.delete("OperacionRecambio", "id_recambio=?", arg);
        bbdd.close();
    }

    public void eliminar(String idOperacion,String idRecambio){

        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idOperacion,idRecambio};

        bbdd.delete("OperacionRecambio", "id_operacion=? and id_recambio=?", arg);
        bbdd.close();
    }

    private void mapearOperacionRecambio(Cursor cursor, OperacionRecambio operacionRecambio) {

        Integer idOperacion = Integer.valueOf(cursor.getInt(0));
        operacionRecambio.setIdOperacion(idOperacion);

        Integer idRecambio = Integer.valueOf(cursor.getInt(1));
        operacionRecambio.setIdRecambio(idRecambio);

        BigDecimal cantidad = new BigDecimal(cursor.getFloat(2));
        if (null != cantidad) {
            operacionRecambio.setCantidad(cantidad);
        } else {
            operacionRecambio.setCantidad(BigDecimal.ZERO);
        }

        BigDecimal precio = new BigDecimal(cursor.getFloat(3));
        if (null != precio) {
            operacionRecambio.setPrecioUnidad(precio);
        } else {
            operacionRecambio.setPrecioUnidad(BigDecimal.ZERO);
        }


        BigDecimal coste = new BigDecimal(cursor.getFloat(4));
        if (null != coste) {
            operacionRecambio.setCoste(coste);
        } else {
            operacionRecambio.setCoste(BigDecimal.ZERO);
        }

    }




}
