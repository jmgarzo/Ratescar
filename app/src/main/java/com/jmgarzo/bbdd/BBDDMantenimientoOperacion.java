package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Mantenimiento;
import com.jmgarzo.objects.MantenimientoOperacion;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 8/04/15.
 */
public class BBDDMantenimientoOperacion {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDMantenimientoOperacion(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }


    public MantenimientoOperacion getMantenimientoOperacion(String idMantenimiento, String idOperacion) {
        MantenimientoOperacion mantenimientoOperacion = null;

        if (null != idMantenimiento && null != idOperacion) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idMantenimiento.trim(), idOperacion};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_OPERACION_BY_ID, args);

            if (cursor.moveToFirst()) {
                mantenimientoOperacion = new MantenimientoOperacion();
                mapearMantenimientoOperacion(cursor, mantenimientoOperacion);
            }
            cursor.close();
            bbdd.close();
        }
        return mantenimientoOperacion;
    }

    public ArrayList<MantenimientoOperacion> getMantenimientoOperaciones() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<MantenimientoOperacion> listaMantenimientoOperaciones = new ArrayList<MantenimientoOperacion>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_OPERACIONES, null);

        if (cursor.moveToFirst()) {
            MantenimientoOperacion mantenimientoOperacion = null;

            do {
                mantenimientoOperacion = new MantenimientoOperacion();
                mapearMantenimientoOperacion(cursor, mantenimientoOperacion);
                listaMantenimientoOperaciones.add(mantenimientoOperacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaMantenimientoOperaciones;

    }

    public ArrayList<MantenimientoOperacion> getMantenimientoOperacionesByIdMantenimiento(String idMantenimiento) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<MantenimientoOperacion> listaMantenimientoOperaciones = new ArrayList<MantenimientoOperacion>();

        String [] arg ={idMantenimiento};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_OPERACIONES_BY_MANTENIMIENTO, arg);

        if (cursor.moveToFirst()) {
            MantenimientoOperacion mantenimientoOperacion = null;

            do {
                mantenimientoOperacion = new MantenimientoOperacion();
                mapearMantenimientoOperacion(cursor, mantenimientoOperacion);
                listaMantenimientoOperaciones.add(mantenimientoOperacion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaMantenimientoOperaciones;

    }

    /**
     * @param mantenimientoOperacion
     */
    public void nuevoMantenimientoOperacion(MantenimientoOperacion mantenimientoOperacion) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valoresMantenimientoOperacion = new ContentValues();
        valoresMantenimientoOperacion.put("id_mantenimiento", mantenimientoOperacion.getIdMantenimiento());
        valoresMantenimientoOperacion.put("id_operacion", mantenimientoOperacion.getIdOperacion());
        valoresMantenimientoOperacion.put("periodicidad_digito", mantenimientoOperacion.getPeriodicidadUnidad());
        valoresMantenimientoOperacion.put("periodicidad_unidad", mantenimientoOperacion.getPeriodicidadUnidad());
        valoresMantenimientoOperacion.put("coste", mantenimientoOperacion.getCoste().doubleValue());
        valoresMantenimientoOperacion.put("comentarios",mantenimientoOperacion.getComentarios());

        bbdd.insert("MantenimientoOperacion", null, valoresMantenimientoOperacion);
        bbdd.close();
    }


    /**
     * Por como está definida la navegación, primero hay que seleccionar una nueva operación
     * y según se añade ya se graba un nuevo mantenimientoOperación con los valores inicializados
     * @param idMantenimiento
     * @param idOperacion
     */
    public Long nuevoMantenimientoOperacion(Integer idMantenimiento, Integer idOperacion) {

        bbdd = bbddhelper.getWritableDatabase();
        Long resultado;
        ContentValues valoresMantenimientoOperacion = new ContentValues();
        valoresMantenimientoOperacion.put("id_mantenimiento", idMantenimiento);
        valoresMantenimientoOperacion.put("id_operacion", idOperacion);
        valoresMantenimientoOperacion.put("periodicidad_digito", 0);
        valoresMantenimientoOperacion.put("periodicidad_unidad", "");
        valoresMantenimientoOperacion.put("coste", BigDecimal.ZERO.doubleValue());
        valoresMantenimientoOperacion.put("comentarios","");

        resultado = bbdd.insert("MantenimientoOperacion", null, valoresMantenimientoOperacion);
        bbdd.close();

        return resultado;
    }

    public int actualizarMantenimientoOperacion(MantenimientoOperacion mantenimientoOperacion) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        if (null != mantenimientoOperacion.getPeriodicidadDigito()) {
            valores.put("periodicidad_digito", mantenimientoOperacion.getPeriodicidadDigito());
        }else{
            valores.put("periodicidad_digito", 0);
        }

        if(null!= mantenimientoOperacion.getPeriodicidadUnidad()) {
            valores.put("periodicidad_unidad", mantenimientoOperacion.getPeriodicidadUnidad());
        }else{
            valores.put("periodicidad_unidad", "");

        }
        if (null != mantenimientoOperacion.getCoste()) {
            valores.put("coste", mantenimientoOperacion.getCoste().doubleValue());
        } else {
            valores.put("coste", BigDecimal.ZERO.doubleValue());
        }

        if(null!= mantenimientoOperacion.getComentarios()) {
            valores.put("comentarios", mantenimientoOperacion.getComentarios());
        }else {
            valores.put("comentarios", "");
        }

        String args[] = {mantenimientoOperacion.getIdMantenimiento().toString(),mantenimientoOperacion.getIdOperacion().toString()};
        int result = bbdd.update("MantenimientoOperacion", valores, "id_mantenimiento=? AND id_operacion=? ", args);

        bbdd.close();

        return result;
    }


    private void mapearMantenimientoOperacion(Cursor cursor, MantenimientoOperacion mantenimientoOperacion) {

        Integer idMantenimiento = Integer.valueOf(cursor.getInt(0));
        mantenimientoOperacion.setIdMantenimiento(idMantenimiento);

        Integer idOperacion = Integer.valueOf(cursor.getInt(1));
        mantenimientoOperacion.setIdOperacion(idOperacion);

        Integer periodicidadDigito = Integer.valueOf(cursor.getInt(2));
        if (null != periodicidadDigito) {
            mantenimientoOperacion.setPeriodicidadDigito(periodicidadDigito);
        } else {
            mantenimientoOperacion.setPeriodicidadDigito(0);
        }

        if (!Constantes.esVacio(cursor.getString(3))) {
            mantenimientoOperacion.setPeriodicidadUnidad(cursor.getString(3));
        } else {
            mantenimientoOperacion.setPeriodicidadUnidad("");
        }

        BigDecimal coste = new BigDecimal(cursor.getFloat(4));
        if (null != coste) {
            mantenimientoOperacion.setCoste(coste);
        } else {
            mantenimientoOperacion.setCoste(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            mantenimientoOperacion.setComentarios(cursor.getString(5));
        } else {
            mantenimientoOperacion.setComentarios("");
        }
    }

    public void eliminar(String idMantenimiento, String idOperacion) {
        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idMantenimiento,idOperacion};
        bbdd.delete("MantenimientoOperacion", " id_mantenimiento=? AND id_operacion =? ", arg);
        bbdd.close();
    }

}
