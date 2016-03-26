package com.jmgarzo.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Subcategoria;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 16/04/15.
 */
public class BBDDSubcategorias {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDSubcategorias(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public Subcategoria getSubcategoriaById(String idSubcategoria) {

        Subcategoria subCategoria = null;
        if (null != idSubcategoria) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idSubcategoria.trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_SUBCATEGORIA_RECAMBIO_BY_ID, args);

            if (cursor.moveToFirst()) {
                subCategoria = new Subcategoria();
                mapearSubcategoria(cursor, subCategoria);
            }
            cursor.close();
            bbdd.close();
        }
        return subCategoria;
    }


    public Integer getIdSubcategoriaByNombre(String idNombre) {

        Integer idSubcategoria = null;
        if (null != idNombre) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idNombre.trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_ID_SUBCATEGORIA_BY_NOMBRE, args);

            if (cursor.moveToFirst()) {
                idSubcategoria = cursor.getInt(0);
            }
            cursor.close();
            bbdd.close();
        }
        return idSubcategoria;
    }


    public ArrayList<Subcategoria> getSubcategoriasPorIdCategoria(String idCategoria) {

        ArrayList<Subcategoria> listaSubcategorias = null;

        bbdd = bbddhelper.getReadableDatabase();

        String[] arg = {idCategoria};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_SUBCATEGORIAS_POR_ID_CATEGORIA, arg);

        if (cursor.moveToFirst()) {
            listaSubcategorias = new ArrayList<Subcategoria>();
            do {
                Subcategoria subcategoria = new Subcategoria();
                mapearSubcategoria(cursor, subcategoria);
                listaSubcategorias.add(subcategoria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();

        return listaSubcategorias;
    }

    public ArrayList<Subcategoria> getSubcategoriasPorNombreCategoria(String nombreCategoria) {

        ArrayList<Subcategoria> listaSubcategorias = null;

        bbdd = bbddhelper.getReadableDatabase();

        String[] arg = {nombreCategoria};

        Cursor cursorCategoria = bbdd.rawQuery(Constantes.SELECT_ID_CATEGORIA_BY_NOMBRE_CATEGORIA, arg);

        Integer idCategoria=null;
        if (cursorCategoria.moveToFirst()) {
            idCategoria = cursorCategoria.getInt(0);

        }

        if(null!=idCategoria) {

            String [] argIdCategoria = {idCategoria.toString()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_SUBCATEGORIAS_POR_ID_CATEGORIA, argIdCategoria);

            if (cursor.moveToFirst()) {
                listaSubcategorias = new ArrayList<Subcategoria>();
                do {
                    Subcategoria subcategoria = new Subcategoria();
                    mapearSubcategoria(cursor, subcategoria);
                    listaSubcategorias.add(subcategoria);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        cursorCategoria.close();
        bbdd.close();

        return listaSubcategorias;
    }




    public String getNombrePorId(Integer idCategoria) {


        bbdd = bbddhelper.getReadableDatabase();

        String[] arg = {idCategoria.toString()};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_NOMBRE_POR_ID, arg);

        String nombre="";
        if (cursor.moveToFirst()) {
            if(!Constantes.esVacio(cursor.getString(0))){
                nombre = cursor.getString(0);
            }

        }
        cursor.close();
        bbdd.close();

        return nombre;
    }





    private void mapearSubcategoria(Cursor cursor, Subcategoria subCategoria) {

        Integer idSubcategoria = Integer.valueOf(cursor.getInt(0));
        subCategoria.setIdSubcategoria(idSubcategoria);

        Integer idCategoria = Integer.valueOf(cursor.getInt(1));
        subCategoria.setIdCategoria(idCategoria);

        if (!Constantes.esVacio(cursor.getString(2))) {
            subCategoria.setNombre(cursor.getString(2));
        } else {
            subCategoria.setNombre("");
        }

        if (!Constantes.esVacio(cursor.getString(3))) {
            subCategoria.setDescripcion(cursor.getString(3));
        } else {
            subCategoria.setDescripcion("");
        }
    }


}
