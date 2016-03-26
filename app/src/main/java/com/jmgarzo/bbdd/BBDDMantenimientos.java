package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Mantenimiento;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 6/04/15.
 */
public class BBDDMantenimientos {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDMantenimientos(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }


    public Long nuevoMantenimiento(Mantenimiento mantenimiento) {

        Long result=new Long(-1);
        bbdd = bbddhelper.getWritableDatabase();

        BBDDCoches bbddCoches = new BBDDCoches(context);
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());

        ContentValues valoresMantenimiento = new ContentValues();
        valoresMantenimiento.put("id_coche", mantenimiento.getIdCoche());
        valoresMantenimiento.put("tipo_mantenimiento", mantenimiento.getTipoMantenimiento());
        valoresMantenimiento.put("fecha", mantenimiento.getFechaMantenimiento());
        if (null != mantenimiento.getKmMatenimiento()) {
            valoresMantenimiento.put("km", mantenimiento.getKmMatenimiento().doubleValue());
        } else {
            valoresMantenimiento.put("km", BigDecimal.ZERO.doubleValue());
        }

        if (null != mantenimiento.getCosteFinal()) {
            valoresMantenimiento.put("coste", mantenimiento.getCosteFinal().doubleValue());
        } else {
            valoresMantenimiento.put("coste", BigDecimal.ZERO.doubleValue());
        }
        valoresMantenimiento.put("comentarios", mantenimiento.getComentarios());

        result = bbdd.insert("Mantenimientos", null, valoresMantenimiento);

        if (null != coche && coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", mantenimiento.getKmMatenimiento().doubleValue());
            String[] arg = {mantenimiento.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg);
        }

        bbdd.close();
        return result;

    }


    public ArrayList<Mantenimiento> getMantenimientosOrdenadorPorFechaDesc() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTOS_ORDENADOS_POR_FECHA, null);
        if (cursorMantenimiento.moveToFirst()) {
            do {
                Mantenimiento mantenimiento = new Mantenimiento();
                mapearMantenimiento(cursorMantenimiento, mantenimiento);
                listaMantenimientos.add(mantenimiento);
            } while (cursorMantenimiento.moveToNext());
        }
        cursorMantenimiento.close();
        bbdd.close();
        return listaMantenimientos;
    }

    public ArrayList<Mantenimiento> getMantenimientosOrdenadorPorKmDesc() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTOS_ORDENADOS_POR_KM_DESC, null);
        if (cursorMantenimiento.moveToFirst()) {
            do {
                Mantenimiento mantenimiento = new Mantenimiento();
                mapearMantenimiento(cursorMantenimiento, mantenimiento);
                listaMantenimientos.add(mantenimiento);
            } while (cursorMantenimiento.moveToNext());
        }
        cursorMantenimiento.close();
        bbdd.close();
        return listaMantenimientos;
    }

    public Mantenimiento getUltimoMantenimiento() {
        bbdd = bbddhelper.getReadableDatabase();
        Mantenimiento ultimoMantenimiento = null;
        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_ULTIMO_MANTENIMIENTO, null);
        if (cursorMantenimiento.moveToFirst()) {

                ultimoMantenimiento = new Mantenimiento();
                mapearMantenimiento(cursorMantenimiento, ultimoMantenimiento);
        }
        cursorMantenimiento.close();
        bbdd.close();
        return ultimoMantenimiento;
    }


    public Mantenimiento getMantenimiento(String idMantenimiento) {
        Mantenimiento mantenimiento = null;

        if (null != idMantenimiento) {

            bbdd = bbddhelper.getReadableDatabase();
            String idMante[] = {idMantenimiento};
            Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_POR_ID, idMante);

            if (cursorMantenimiento.moveToFirst()) {
                mantenimiento = new Mantenimiento();

                mapearMantenimiento(cursorMantenimiento, mantenimiento);

            }
            cursorMantenimiento.close();
            bbdd.close();
        }
        return mantenimiento;

    }

    public Mantenimiento getMantenimiento(Integer idMantenimiento) {
        Mantenimiento mantenimiento = null;

        if (null != idMantenimiento) {

            bbdd = bbddhelper.getReadableDatabase();
            String idMante[] = {idMantenimiento.toString()};
            Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_POR_ID, idMante);

            if (cursorMantenimiento.moveToFirst()) {
                mantenimiento = new Mantenimiento();

                mapearMantenimiento(cursorMantenimiento, mantenimiento);

            }
            cursorMantenimiento.close();
            bbdd.close();
        }
        return mantenimiento;

    }


    public ArrayList<Mantenimiento> getMatenimientosPorCocheOrdenadosPorKmDesc(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
        String args[] = {idCoche.toString()};

        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTOS_POR_COCHE_ORDENADOS_POR_KM_DESC, args);

        if (cursorMantenimiento.moveToFirst()) {

            do {
                Mantenimiento mantenimiento = new Mantenimiento();

                mapearMantenimiento(cursorMantenimiento, mantenimiento);

                listaMantenimientos.add(mantenimiento);
            } while (cursorMantenimiento.moveToNext());

        }
        cursorMantenimiento.close();
        bbdd.close();

        return listaMantenimientos;
    }


    public BigDecimal getkmInicialesPorCoche(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();


        String[] args = {idCoche.toString().trim()};

        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_KM_INICIALES_POR_COCHE, args);

        BigDecimal kmIniciales = BigDecimal.ZERO;
        if (cursorMantenimiento.moveToFirst()) {
            if (null != kmIniciales) {
                kmIniciales = new BigDecimal(cursorMantenimiento.getDouble(0));
            }
        }

        return kmIniciales;
    }


    public BigDecimal getkmFinalesPorCoche(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();


        String[] args = {idCoche.toString().trim()};

        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SELECT_MANTENIMIENTO_KM_FINALES_POR_COCHE, args);

        BigDecimal kmFinales = BigDecimal.ZERO;
        if (cursorMantenimiento.moveToFirst()) {
            if (null != kmFinales) {
                kmFinales = new BigDecimal(cursorMantenimiento.getDouble(0));
            }
        }
        return kmFinales;
    }


    private void mapearMantenimiento(Cursor cursorMantenimiento, Mantenimiento mantenimiento) {

        Integer idMantenimiento = cursorMantenimiento.getInt(0);
        mantenimiento.setIdMantenimiento(idMantenimiento);

        Integer idCoche = cursorMantenimiento.getInt(1);
        mantenimiento.setIdCoche(idCoche);

        if (!Constantes.esVacio(cursorMantenimiento.getString(2))) {
            mantenimiento.setTipoMantenimiento(cursorMantenimiento.getString(2));
        } else {
            mantenimiento.setTipoMantenimiento("");
        }


        mantenimiento.setFechaMantenimiento(cursorMantenimiento.getLong(3));


        BigDecimal kmMantenimiento = new BigDecimal((cursorMantenimiento.getFloat(4)));
        if (null != kmMantenimiento) {
            mantenimiento.setKmMatenimiento(kmMantenimiento);
        } else {
            mantenimiento.setKmMatenimiento(BigDecimal.ZERO);
        }


        BigDecimal costeFinal = new BigDecimal(
                cursorMantenimiento.getFloat(5));
        if (null != costeFinal) {
            mantenimiento.setCosteFinal(costeFinal);
        } else {
            mantenimiento.setCosteFinal(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursorMantenimiento.getString(6))) {
            mantenimiento.setComentarios(cursorMantenimiento.getString(6));
        } else {
            mantenimiento.setComentarios("");
        }

    }


    public void actualizarMantenimiento(Mantenimiento mantenimiento) {




        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());


        ContentValues valoresMantenimiento = new ContentValues();
        valoresMantenimiento.put("id_coche", mantenimiento.getIdCoche());
        valoresMantenimiento.put("tipo_mantenimiento", mantenimiento.getTipoMantenimiento());
        valoresMantenimiento.put("fecha", mantenimiento.getFechaMantenimiento());
        if (null != mantenimiento.getKmMatenimiento()) {
            valoresMantenimiento.put("km", mantenimiento.getKmMatenimiento().doubleValue());
        } else {
            valoresMantenimiento.put("km", BigDecimal.ZERO.doubleValue());
        }

        if (null != mantenimiento.getCosteFinal()) {
            valoresMantenimiento.put("coste", mantenimiento.getCosteFinal().doubleValue());
        } else {
            valoresMantenimiento.put("coste", BigDecimal.ZERO.doubleValue());
        }
        valoresMantenimiento.put("comentarios", mantenimiento.getComentarios());

        String arg[] = {mantenimiento.getIdMantenimiento().toString().trim()};
        int result = bbdd.update("Mantenimientos", valoresMantenimiento, "id_mantenimiento = ?", arg);

        if (null != coche && coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
            ContentValues valoresCoche = new ContentValues();
            valoresCoche.put("km_actuales", mantenimiento.getKmMatenimiento().doubleValue());
            String[] arg2 = {mantenimiento.getIdCoche().toString()};
            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg2);
        }
        bbdd.close();
    }

    public void eliminarMantenimiento(Integer idMantenimiento) {
        bbdd = bbddhelper.getWritableDatabase();

        String arg[] = {idMantenimiento.toString()};



        BBDDMantenimientos bbddMantenimientos = new BBDDMantenimientos(context);
        Mantenimiento mantenimiento = bbddMantenimientos.getMantenimiento(idMantenimiento);
        //bbddMantenimientos.eliminarMantenimiento(mantenimiento.getIdMantenimiento());
        BBDDRepostajes bbddRepostajes = new BBDDRepostajes(context);
        BBDDCoches bbddCoches = new BBDDCoches(context);
        BigDecimal kmUltimoRespostaje = bbddRepostajes.getKmUltimoRespostajePorCoche(mantenimiento.getIdCoche());

        Coche coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());
        if (coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
            //podemos borrar el mantenimiento, no hay que hacer nada
            bbdd.delete("Mantenimientos", "id_mantenimiento=?", arg);
        } else if (coche.getKmActuales() == mantenimiento.getKmMatenimiento() || kmUltimoRespostaje.compareTo(coche.getKmActuales()) < 0) {
            //Los km del coche se deben al ultimo mantenimiento, se debe poner
            //los km mayores del último respostaje o del último mantenimiento
            bbdd.delete("Mantenimientos", "id_mantenimiento=?", arg);
            BigDecimal kmUltimoMantenimiento = bbddMantenimientos.getKmUltimoMatenimientoPorCoche(mantenimiento.getIdCoche());
            if (kmUltimoMantenimiento.compareTo(kmUltimoRespostaje) > 0) {
                coche.setKmActuales(kmUltimoMantenimiento);
                bbddCoches.actualizarCoche(coche);
            } else {
                coche.setKmActuales(kmUltimoRespostaje);
                bbddCoches.actualizarCoche(coche);
            }
        } else {
            //Si no cuadra en ninguna opcion borramos
            bbdd.delete("Mantenimientos", "id_mantenimiento=?", arg);

        }
        bbdd.close();
    }

        public BigDecimal getKmUltimoMatenimientoPorCoche(Integer idCoche) {
        bbdd = bbddhelper.getReadableDatabase();
        BigDecimal kmUltimoMantenimiento = new BigDecimal(0);


        String[] args = {idCoche.toString()};
        Cursor cursorMantenimiento = bbdd.rawQuery(
                Constantes.SELECT_ULTIMO_MANTENIMIENTO_POR_COCHE, args);

        if (cursorMantenimiento.moveToFirst()) {
            if (null != BigDecimal.valueOf(cursorMantenimiento.getFloat(0))) {
                kmUltimoMantenimiento = BigDecimal.valueOf(cursorMantenimiento.getInt(0));
            }

        }

        cursorMantenimiento.close();
        bbdd.close();

        return kmUltimoMantenimiento;

    }



}
