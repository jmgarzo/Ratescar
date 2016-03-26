package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Peaje;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 19/01/15.
 */
public class BBDDPeajes {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDPeajes(Context context){
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE,null,Constantes.BBDD_VERSION);
    }


   // id_peaje, id_coche, nombre, fecha, precio, carretera, direccion,
   // ubicacion, comentarios
    public void nuevoPeaje(Peaje peaje){

        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();

        Coche coche = new Coche();
        coche = bbddCoches.getCoche(peaje.getIdCoche().toString());

        ContentValues valoresPeaje = new ContentValues();
        valoresPeaje.put("id_coche",peaje.getIdCoche());
        valoresPeaje.put("nombre",peaje.getNombre());
        valoresPeaje.put("fecha",peaje.getFecha());
        valoresPeaje.put("precio",peaje.getPrecio().doubleValue());
        valoresPeaje.put("carretera",peaje.getCarretera());
        valoresPeaje.put("direccion",peaje.getDireccion());
        valoresPeaje.put("ubicacion",peaje.getUbicacion());
        valoresPeaje.put("comentarios",peaje.getComentarios());

        bbdd.insert("Peajes",null,valoresPeaje);
        bbdd.close();
    }

    public Peaje getPeaje(String idPeaje){
        Peaje peaje= null;

        if(null != idPeaje){
            bbdd = bbddhelper.getReadableDatabase();
            String argsIdPeaje[]={idPeaje};
            Cursor cursor = bbdd.rawQuery(Constantes.selectPeajeById,argsIdPeaje);

            if(cursor.moveToFirst()){
                do{
                    peaje = new Peaje();



                    Integer idCoche = Integer.valueOf(cursor.getInt(0));
                    peaje.setIdCoche(idCoche);


                    if (!Constantes.esVacio(cursor.getString(1))) {
                        peaje.setNombre(cursor.getString(1));
                    } else {
                        peaje.setNombre("");
                    }

                    Long fecha = Long.valueOf(cursor.getLong(2));
                    if(null!=fecha) {
                        peaje.setFecha(fecha);
                    }
                    else{
                        peaje.setFecha(Long.parseLong("0"));
                    }

                    BigDecimal precio = new BigDecimal(cursor.getDouble(3));
                    if(null!= precio){
                        peaje.setPrecio(precio);
                    }else{
                        peaje.setPrecio(BigDecimal.ZERO);
                    }

                    if (!Constantes.esVacio(cursor.getString(4))) {
                        peaje.setCarretera(cursor.getString(4));
                    } else {
                        peaje.setCarretera("");
                    }

                    if (!Constantes.esVacio(cursor.getString(5))) {
                        peaje.setDireccion(cursor.getString(5));
                    } else {
                        peaje.setDireccion("");
                    }

                    if (!Constantes.esVacio(cursor.getString(6))) {
                        peaje.setUbicacion(cursor.getString(6));
                    } else {
                        peaje.setUbicacion("");
                    }

                    if (!Constantes.esVacio(cursor.getString(7))) {
                        peaje.setComentarios(cursor.getString(7));
                    } else {
                        peaje.setComentarios("");
                    }


                }while(cursor.moveToNext());
            }
            cursor.close();
            bbdd.close();
        }
        return peaje;
    }


// id_coche, nombre, fecha, precio, carretera, direccion,  " +
// ubicacion, comentarios
    public void actualizarPeaje(Peaje peaje){
        BBDDCoches bbddCoches = new BBDDCoches(context);
        bbdd = bbddhelper.getWritableDatabase();
        Coche coche = new Coche();
        coche = bbddCoches.getCoche(peaje.getIdCoche().toString());

        ContentValues valores = new ContentValues();
        valores.put("id_coche",peaje.getIdCoche());
        valores.put("nombre",peaje.getNombre());
        valores.put("fecha",peaje.getFecha());
        valores.put("precio",peaje.getPrecio().doubleValue());
        valores.put("carretera",peaje.getCarretera());
        valores.put("direccion",peaje.getDireccion());
        valores.put("ubicacion", peaje.getUbicacion());
        valores.put("comentarios",peaje.getComentarios());

        String arg[] = {peaje.getIdPeaje().toString().trim()};
        int result = bbdd.update("Peajes",valores,"id_peaje = ?",arg);

    }

    public ArrayList<Peaje> getPeajesPorFecha(){
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Peaje> listaPeaje = new ArrayList<Peaje>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectTodasLosPeajesOrdenadosPorFechaDesc, null);

        if (cursor.moveToFirst()){
            Peaje peaje = null;

            do{
                peaje = new Peaje();

                mapearPeaje(cursor, peaje);

                listaPeaje.add(peaje);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaPeaje;

    }

    public ArrayList<String> getNombrePeajes() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaNombres = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectNombrePeajes, null);

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

    public ArrayList<String> getPrecioPeajes() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaPrecios = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectPreciosPeajes, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }


                listaPrecios.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaPrecios;
    }

    public ArrayList<String> getCarreteras() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaCarreteras = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectCarreterasPeajes, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }


                listaCarreteras.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaCarreteras;
    }

    public ArrayList<String> getDirecciones() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaDireciones = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectDireccionesPeajes, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }


                listaDireciones.add(nombre);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaDireciones;
    }

    public ArrayList<String> getUbicaciones() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listaUbicaciones = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.selectUbicacionesPeajes, null);

        if (cursor.moveToFirst()) {
            String nombre = "";

            do {
                if (!Constantes.esVacio(cursor.getString(0))) {
                    nombre = cursor.getString(0);
                } else {
                    nombre = "";
                }


                listaUbicaciones.add(nombre);

            }while(cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaUbicaciones;
    }

    public void eliminar(String idPeaje){
        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idPeaje};
        bbdd.delete("Peajes", "id_peaje=?", arg);
        bbdd.close();
    }

    private void mapearPeaje(Cursor cursor, Peaje peaje) {

        Integer idPeaje = Integer.valueOf(cursor.getInt(0));
        peaje.setIdPeaje(idPeaje);

        Integer idCoche = Integer.valueOf(cursor.getInt(1));
        peaje.setIdCoche(idCoche);


        if (!Constantes.esVacio(cursor.getString(2))) {
            peaje.setNombre(cursor.getString(2));
        } else {
            peaje.setNombre("");
        }

        Long fecha = Long.valueOf(cursor.getLong(3));
        if (null != fecha) {
            peaje.setFecha(fecha);
        } else {
            peaje.setFecha(Long.parseLong("0"));
        }

        BigDecimal precio = new BigDecimal(cursor.getDouble(4));
        if (null != precio) {
            peaje.setPrecio(precio);
        } else {
            peaje.setPrecio(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            peaje.setCarretera(cursor.getString(5));
        } else {
            peaje.setCarretera("");
        }

        if (!Constantes.esVacio(cursor.getString(6))) {
            peaje.setDireccion(cursor.getString(6));
        } else {
            peaje.setDireccion("");
        }

        if (!Constantes.esVacio(cursor.getString(7))) {
            peaje.setUbicacion(cursor.getString(7));
        } else {
            peaje.setUbicacion("");
        }

        if (!Constantes.esVacio(cursor.getString(8))) {
            peaje.setComentarios(cursor.getString(8));
        } else {
            peaje.setComentarios("");
        }
    }

}
