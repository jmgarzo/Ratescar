package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Operacion;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 6/04/15.
 */
public class BBDDOperaciones {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDOperaciones(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public Operacion getOperacion(String idOperacion) {
        Operacion operacion = null;

        if (null != idOperacion) {
            bbdd = bbddhelper.getReadableDatabase();
            String arg[] = {idOperacion.trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACION_BY_ID, arg);

            if (cursor.moveToFirst()) {
                operacion = new Operacion();
                mapearOperacion(cursor, operacion);
            }
            cursor.close();
            bbdd.close();
        }
        return operacion;
    }

    /**
     *
     * @param operacion
     * @return Numero de columnas afectadas por el update
     */

    public int actualizarOperacion(Operacion operacion) {
        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nombre", operacion.getNombre());
        valores.put("descripcion", operacion.getDescripcion());

        String arg[] = {operacion.getIdOperacion().toString().trim()};
        int result = bbdd.update("Operaciones", valores, "id_operacion=?", arg);

        bbdd.close();
        return result;

    }

    public void nuevaOperacion(Operacion operacion) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valoresOperacion = new ContentValues();
        valoresOperacion.put("nombre", operacion.getNombre());
        valoresOperacion.put("descripcion", operacion.getDescripcion());

        bbdd.insert("Operaciones", null, valoresOperacion);
        bbdd.close();
    }

    public ArrayList<Operacion> getOperacionesPorOrdenAlfabetico() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Operacion> listaOperaciones = new ArrayList<Operacion>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_OPERACIONES_POR_ORDEN_ALFABETICO, null);

        if (cursor.moveToFirst()) {
            Operacion operacion = null;

            do {
                operacion = new Operacion();
                mapearOperacion(cursor, operacion);
                listaOperaciones.add(operacion);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaOperaciones;

    }


    private void mapearOperacion(Cursor cursor, Operacion operacion) {

        Integer idOperacion = Integer.valueOf(cursor.getInt(0));
        operacion.setIdOperacion(idOperacion);

        if (!Constantes.esVacio(cursor.getString(1))) {
            operacion.setNombre(cursor.getString(1));
        } else {
            operacion.setNombre("");
        }

        if (!Constantes.esVacio(cursor.getString(2))) {
            operacion.setDescripcion(cursor.getString(2));
        } else {
            operacion.setDescripcion("");
        }
    }

    public void eliminarOperacion(Integer idOperacion) {
        bbdd = bbddhelper.getWritableDatabase();

        String arg[] = {idOperacion.toString()};
        bbdd.delete("Operaciones", "id_operacion=?", arg);
        bbdd.close();
    }
}
