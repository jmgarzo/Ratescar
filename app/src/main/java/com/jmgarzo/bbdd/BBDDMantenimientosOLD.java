//package com.jmgarzo.bbdd;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.jmgarzo.objects.Coche;
//import com.jmgarzo.objects.Constantes;
//import com.jmgarzo.objects.Mantenimiento;
//import com.jmgarzo.ratescar.BBDDSQLiteHelper;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
///**
//* Created by jmgarzo on 26/12/14.
//*/
//public class BBDDMantenimientosOLD {
//
//    private SQLiteDatabase bbdd;
//    private BBDDSQLiteHelper bbddhelper;
//    private Context context;
//
//    public BBDDMantenimientos(Context context) {
//        this.context = context;
//        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
//    }
//
//
//    public ArrayList<Mantenimiento> getTodosLosMantenimientos() {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
//
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.selectTodosLosMantenimientos, null);
//
//        if (cursorMantenimiento.moveToFirst()) {
//
//            do {
//                Mantenimiento mantenimiento = new Mantenimiento();
//
//                mapearMantenimiento(cursorMantenimiento, mantenimiento);
//
//                listaMantenimientos.add(mantenimiento);
//            } while (cursorMantenimiento.moveToNext());
//
//        }
//        cursorMantenimiento.close();
//        bbdd.close();
//
//        return listaMantenimientos;
//    }
//
//    public ArrayList<Mantenimiento> getMatenimientosPorCocheOrdenadosPorKmDesc(Integer idCoche) {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
//        String args[] = {idCoche.toString()};
//
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.selectMantenimientosPorCocheOrdenadosPorKmDesc, args);
//
//        if (cursorMantenimiento.moveToFirst()) {
//
//            do {
//                Mantenimiento mantenimiento = new Mantenimiento();
//
//                mapearMantenimiento(cursorMantenimiento, mantenimiento);
//
//                listaMantenimientos.add(mantenimiento);
//            } while (cursorMantenimiento.moveToNext());
//
//        }
//        cursorMantenimiento.close();
//        bbdd.close();
//
//        return listaMantenimientos;
//    }
//
//    public ArrayList<Mantenimiento> getMatenimientosOrdenadorPorKmDesc() {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.selectMantenimientosOrdenadorPorKmDesc, null);
//        if (cursorMantenimiento.moveToFirst()) {
//            do {
//                Mantenimiento mantenimiento = new Mantenimiento();
//                mapearMantenimiento(cursorMantenimiento, mantenimiento);
//                listaMantenimientos.add(mantenimiento);
//            } while (cursorMantenimiento.moveToNext());
//        }
//        cursorMantenimiento.close();
//        bbdd.close();
//        return listaMantenimientos;
//    }
//
//
//    public ArrayList<Mantenimiento> getMatenimientosOrdenadorPorFechaDesc() {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<Mantenimiento> listaMantenimientos = new ArrayList<Mantenimiento>();
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.selectMantenimientosOrdenadorPorFechaDesc, null);
//        if (cursorMantenimiento.moveToFirst()) {
//            do {
//                Mantenimiento mantenimiento = new Mantenimiento();
//                mapearMantenimiento(cursorMantenimiento, mantenimiento);
//                listaMantenimientos.add(mantenimiento);
//            } while (cursorMantenimiento.moveToNext());
//        }
//        cursorMantenimiento.close();
//        bbdd.close();
//        return listaMantenimientos;
//    }
//
//    public Mantenimiento getMantenimiento(String idMantenimientoBuscado) {
//        Mantenimiento mantenimiento = null;
//
//        if (null != idMantenimientoBuscado) {
//
//            bbdd = bbddhelper.getReadableDatabase();
//            String idMante[] = {idMantenimientoBuscado};
//            Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.selectMantenimientoById, idMante);
//
//            if (cursorMantenimiento.moveToFirst()) {
//                mantenimiento = new Mantenimiento();
//
//                mapearMantenimiento(cursorMantenimiento, mantenimiento);
//
//            }
//            cursorMantenimiento.close();
//            bbdd.close();
//        }
//        return mantenimiento;
//
//    }
//
//    public BigDecimal getkmInicialesPorCoche(Integer idCoche) {
//        bbdd = bbddhelper.getReadableDatabase();
//
//
//        String[] args = {idCoche.toString().trim()};
//
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SelectKmInicialesMantenimientoPorCoche, args);
//
//        BigDecimal kmIniciales = BigDecimal.ZERO;
//        if (cursorMantenimiento.moveToFirst()) {
//            if (null != kmIniciales) {
//                kmIniciales = new BigDecimal(cursorMantenimiento.getDouble(0));
//            }
//        }
//
//        return kmIniciales;
//    }
//
//    public BigDecimal getkmFinalesPorCoche(Integer idCoche) {
//        bbdd = bbddhelper.getReadableDatabase();
//
//
//        String[] args = {idCoche.toString().trim()};
//
//        Cursor cursorMantenimiento = bbdd.rawQuery(Constantes.SelectKmFinalesMantenimientoPorCoche, args);
//
//        BigDecimal kmFinales = BigDecimal.ZERO;
//        if (cursorMantenimiento.moveToFirst()) {
//            if (null != kmFinales) {
//                kmFinales = new BigDecimal(cursorMantenimiento.getDouble(0));
//            }
//        }
//        return kmFinales;
//    }
//
//    public void nuevoMantenimiento(Mantenimiento mantenimiento) {
//
//        bbdd = bbddhelper.getWritableDatabase();
//
//        BBDDCoches bbddCoches = new BBDDCoches(context);
//        Coche coche = new Coche();
//        coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());
//
//        ContentValues valoresMantenimiento = new ContentValues();
//        valoresMantenimiento.put("id_coche", mantenimiento.getIdCoche());
//        valoresMantenimiento.put("tipo_mantenimiento", mantenimiento.getTipoMantenimiento());
//        valoresMantenimiento.put("nombre_mantenimiento", mantenimiento.getNombreMantenimiento());
//        valoresMantenimiento.put("fecha_mantenimiento", mantenimiento.getFechaMantenimiento());
//        if (null != mantenimiento.getKmMatenimiento()) {
//            valoresMantenimiento.put("km_mantenimiento", mantenimiento.getKmMatenimiento().doubleValue());
//        } else {
//            valoresMantenimiento.put("km_mantenimiento", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getCosteIva()) {
//            valoresMantenimiento.put("coste_iva", mantenimiento.getCosteIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_iva", BigDecimal.ZERO.doubleValue());
//        }
//
//        if (null != mantenimiento.getCosteSinIva()) {
//            valoresMantenimiento.put("coste_sin_iva", mantenimiento.getCosteSinIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_sin_iva", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getTantoCienIva()) {
//            valoresMantenimiento.put("tanto_por_cien_iva", mantenimiento.getTantoCienIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("tanto_por_cien_iva", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getDescuentoTotal()) {
//            valoresMantenimiento.put("descuento_total", mantenimiento.getDescuentoTotal().doubleValue());
//        } else {
//            valoresMantenimiento.put("descuento_total", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getCosteFinal()) {
//            valoresMantenimiento.put("coste_final", mantenimiento.getCosteFinal().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_final", BigDecimal.ZERO.doubleValue());
//        }
//        valoresMantenimiento.put("comentarios", mantenimiento.getComentarios());
//
//        bbdd.insert("Mantenimientos", null, valoresMantenimiento);
//
//        if (null != coche && coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
//            ContentValues valoresCoche = new ContentValues();
//            valoresCoche.put("km_actuales", mantenimiento.getKmMatenimiento().doubleValue());
//            String[] arg = {mantenimiento.getIdCoche().toString()};
//            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg);
//        }
//
//        bbdd.close();
//
//    }
//
//    public void actualizarMantenimiento(Mantenimiento mantenimiento) {
//
//        BBDDCoches bbddCoches = new BBDDCoches(context);
//        bbdd = bbddhelper.getWritableDatabase();
//        Coche coche = new Coche();
//        coche = bbddCoches.getCoche(mantenimiento.getIdCoche().toString());
//
//
//        ContentValues valoresMantenimiento = new ContentValues();
//        valoresMantenimiento.put("id_coche", mantenimiento.getIdCoche());
//        valoresMantenimiento.put("tipo_mantenimiento", mantenimiento.getTipoMantenimiento());
//        valoresMantenimiento.put("nombre_mantenimiento", mantenimiento.getNombreMantenimiento());
//        valoresMantenimiento.put("fecha_mantenimiento", mantenimiento.getFechaMantenimiento());
//        if (null != mantenimiento.getKmMatenimiento()) {
//            valoresMantenimiento.put("km_mantenimiento", mantenimiento.getKmMatenimiento().doubleValue());
//        } else {
//            valoresMantenimiento.put("km_mantenimiento", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getCosteIva()) {
//            valoresMantenimiento.put("coste_iva", mantenimiento.getCosteIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_iva", BigDecimal.ZERO.doubleValue());
//        }
//
//        if (null != mantenimiento.getCosteSinIva()) {
//            valoresMantenimiento.put("coste_sin_iva", mantenimiento.getCosteSinIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_sin_iva", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getTantoCienIva()) {
//            valoresMantenimiento.put("tanto_por_cien_iva", mantenimiento.getTantoCienIva().doubleValue());
//        } else {
//            valoresMantenimiento.put("tanto_por_cien_iva", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getDescuentoTotal()) {
//            valoresMantenimiento.put("descuento_total", mantenimiento.getDescuentoTotal().doubleValue());
//        } else {
//            valoresMantenimiento.put("descuento_total", BigDecimal.ZERO.doubleValue());
//        }
//        if (null != mantenimiento.getCosteFinal()) {
//            valoresMantenimiento.put("coste_final", mantenimiento.getCosteFinal().doubleValue());
//        } else {
//            valoresMantenimiento.put("coste_final", BigDecimal.ZERO.doubleValue());
//        }
//        valoresMantenimiento.put("comentarios", mantenimiento.getComentarios());
//
//        String arg[] = {mantenimiento.getIdMantenimiento().toString().trim()};
//        int result = bbdd.update("Mantenimientos", valoresMantenimiento, "id_mantenimiento = ?", arg);
//
//        if (null != coche && coche.getKmActuales().compareTo(mantenimiento.getKmMatenimiento()) < 0) {
//            ContentValues valoresCoche = new ContentValues();
//            valoresCoche.put("km_actuales", mantenimiento.getKmMatenimiento().doubleValue());
//            String[] arg2 = {mantenimiento.getIdCoche().toString()};
//            bbdd.update("Coches", valoresCoche, "id_coche = ?", arg2);
//        }
//        bbdd.close();
//    }
//
//
//    public void eliminarMantenimiento(Integer idMantenimiento) {
//        bbdd = bbddhelper.getWritableDatabase();
//
//        String arg[] = {idMantenimiento.toString()};
//        bbdd.delete("Mantenimientos", "id_mantenimiento=?", arg);
//        bbdd.close();
//    }
//
//    public BigDecimal getKmUltimoMatenimientoPorCoche(Integer idCoche) {
//        bbdd = bbddhelper.getReadableDatabase();
//        BigDecimal kmUltimoMantenimiento = new BigDecimal(0);
//
//
//        String[] args = {idCoche.toString()};
//        Cursor cursorMantenimiento = bbdd.rawQuery(
//                Constantes.selectUltimoMantenimientoPorCoche, args);
//
//        if (cursorMantenimiento.moveToFirst()) {
//            if (null != BigDecimal.valueOf(cursorMantenimiento.getFloat(0))) {
//                kmUltimoMantenimiento = BigDecimal.valueOf(cursorMantenimiento.getInt(0));
//            }
//
//        }
//
//        cursorMantenimiento.close();
//        bbdd.close();
//
//        return kmUltimoMantenimiento;
//
//    }
//
//
//    public ArrayList<String> getNombreMantenimientos() {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<String> listaNombres = new ArrayList<String>();
//
//        Cursor cursor = bbdd.rawQuery(Constantes.selectNombreMantenimientos, null);
//
//        if (cursor.moveToFirst()) {
//            String nombre = "";
//
//            do {
//                if (!Constantes.esVacio(cursor.getString(0))) {
//                    nombre = cursor.getString(0);
//                } else {
//                    nombre = "";
//                }
//                listaNombres.add(nombre);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        bbdd.close();
//        return listaNombres;
//    }
//
//    public ArrayList<String> getDescripcionesMantenimientos() {
//        bbdd = bbddhelper.getReadableDatabase();
//        ArrayList<String> listaDescripciones = new ArrayList<String>();
//
//        Cursor cursor = bbdd.rawQuery(Constantes.selectTiposMantenimientos, null);
//
//        if (cursor.moveToFirst()) {
//            String nombre = "";
//
//            do {
//                if (!Constantes.esVacio(cursor.getString(0))) {
//                    nombre = cursor.getString(0);
//                } else {
//                    nombre = "";
//                }
//                listaDescripciones.add(nombre);
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        bbdd.close();
//        return listaDescripciones;
//    }
//
//    private void mapearMantenimiento(Cursor cursorMantenimiento, Mantenimiento mantenimiento) {
//
//        Integer idMantenimiento = cursorMantenimiento.getInt(0);
//        mantenimiento.setIdMantenimiento(idMantenimiento);
//
//        Integer idCoche = cursorMantenimiento.getInt(1);
//        mantenimiento.setIdCoche(idCoche);
//
//        if (!Constantes.esVacio(cursorMantenimiento.getString(2))) {
//            mantenimiento.setTipoMantenimiento(cursorMantenimiento.getString(2));
//        } else {
//            mantenimiento.setTipoMantenimiento("");
//        }
//
//        if (!Constantes.esVacio(cursorMantenimiento.getString(3))) {
//            mantenimiento.setNombreMantenimiento(cursorMantenimiento.getString(3));
//        } else {
//            mantenimiento.setNombreMantenimiento("");
//        }
//
//        mantenimiento.setFechaMantenimiento(cursorMantenimiento.getLong(4));
//
//
//        BigDecimal kmMantenimiento = new BigDecimal((cursorMantenimiento.getFloat(5)));
//        mantenimiento.setKmMatenimiento(kmMantenimiento);
//
//        BigDecimal costeIva = new BigDecimal(
//                cursorMantenimiento.getFloat(6));
//        if (null != costeIva) {
//            mantenimiento.setCosteIva(costeIva);
//        } else {
//            mantenimiento.setCosteIva(BigDecimal.ZERO);
//        }
//
//        BigDecimal costeSinIva = new BigDecimal(
//                cursorMantenimiento.getFloat(7));
//        if (null != costeSinIva) {
//            mantenimiento.setCosteSinIva(costeIva);
//        } else {
//            mantenimiento.setCosteSinIva(BigDecimal.ZERO);
//        }
//
//        BigDecimal tantoCienIva = new BigDecimal(
//                cursorMantenimiento.getFloat(8));
//        if (null != tantoCienIva) {
//            mantenimiento.setTantoCienIva(tantoCienIva);
//        } else {
//            mantenimiento.setTantoCienIva(BigDecimal.ZERO);
//        }
//
//        BigDecimal descuentoTotal = new BigDecimal(
//                cursorMantenimiento.getFloat(9));
//        if (null != descuentoTotal) {
//            mantenimiento.setDescuentoTotal(descuentoTotal);
//        } else {
//            mantenimiento.setDescuentoTotal(BigDecimal.ZERO);
//        }
//
//        BigDecimal costeFinal = new BigDecimal(
//                cursorMantenimiento.getFloat(10));
//        if (null != costeFinal) {
//            mantenimiento.setCosteFinal(costeFinal);
//        } else {
//            mantenimiento.setCosteFinal(BigDecimal.ZERO);
//        }
//
//        if (!Constantes.esVacio(cursorMantenimiento.getString(11))) {
//            mantenimiento.setComentarios(cursorMantenimiento.getString(11));
//        } else {
//            mantenimiento.setComentarios("");
//        }
//
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
