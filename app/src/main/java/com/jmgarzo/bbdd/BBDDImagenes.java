package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Imagen;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jmgarzo on 16/03/15.
 */
public class BBDDImagenes {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDImagenes(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public void nuevaImagenYBorradoDeLasDemas(Imagen imagen) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("id_coche", imagen.getIdCoche());
        valores.put("ruta", imagen.getRuta());
        valores.put("ruta_thumb", imagen.getRutaThumb());
        valores.put("tipo",imagen.getTipo());

        long result = bbdd.insert("Imagenes", null, valores);


        if (result != -1) {

            String args[] = {imagen.getRuta().toString().trim(), imagen.getTipo().toString().trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_ID_IMAGEN_POR_RUTA_y_TIPO, args);
            Integer idImagen = null;
            if (cursor.moveToFirst()) {
                idImagen = cursor.getInt(0);
            }

            if (null != idImagen) {
                String arg[] = {imagen.getIdCoche().toString().trim(), idImagen.toString().trim()};
                bbdd.execSQL(Constantes.DELETE_TODAS_LAS_IMAGENES_MENOS, arg);
            }
        }
        bbdd.close();

    }


    public ArrayList<Imagen> getImagenesPorTipoOrderIdCocheDesc(String tipo) {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Imagen> listaImagenes = new ArrayList<Imagen>();
        String[] arg = {tipo};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_IMAGENES_POR_TIPO_ORDER_IDCOCHE_DESC, arg);

        if (cursor.moveToFirst()) {
            do {
                Imagen imagen = new Imagen();
                mapear(cursor, imagen);
                listaImagenes.add(imagen);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaImagenes;

    }

    public ArrayList<Imagen> getImagenesPorCocheYTipoCocheDesc(Integer idCoche,String tipo){

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Imagen> listaImagenes = new ArrayList<Imagen>();
        String[] arg = {idCoche.toString().trim(),tipo};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_IMAGENES_POR_COCHE_Y_TIPO_DESC, arg);

        if (cursor.moveToFirst()) {
            do {
                Imagen imagen = new Imagen();
                mapear(cursor, imagen);
                listaImagenes.add(imagen);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaImagenes;

    }

    public String getRutaImagenPorCocheYTipo(Integer idCoche, String tipo) {
        bbdd = bbddhelper.getReadableDatabase();
        String ruta = "";
        String[] arg = {idCoche.toString().trim(), tipo};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_RUTA_POR_IDCOCHE_Y_TIPO, arg);

        if (cursor.moveToFirst()) {

            if (!Constantes.esVacio(cursor.getString(0))) {
                ruta = cursor.getString(0);
            } else {
                ruta = "";
            }

        }
        cursor.close();
        bbdd.close();
        return ruta;
    }

    public String getRutaThumbImagenPorCocheYTipo(Integer idCoche, String tipo) {
        bbdd = bbddhelper.getReadableDatabase();
        String ruta = "";
        String[] arg = {idCoche.toString().trim(), tipo};

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_RUTA_THUMB_POR_IDCOCHE_Y_TIPO, arg);

        if (cursor.moveToFirst()) {

            if (!Constantes.esVacio(cursor.getString(0))) {
                ruta = cursor.getString(0);
            } else {
                ruta = "";
            }

        }
        cursor.close();
        bbdd.close();
        return ruta;
    }

    private void mapear(Cursor cursor, Imagen imagen) {

        Integer idImagen = Integer.valueOf(cursor.getInt(0));
        imagen.setIdImagen(idImagen);

        Integer idCoche = Integer.valueOf(cursor.getInt(1));
        imagen.setIdCoche(idCoche);

        if (!Constantes.esVacio(cursor.getString(2))) {
            imagen.setRuta(cursor.getString(2));
        } else {
            imagen.setRuta("");
        }

        if (!Constantes.esVacio(cursor.getString(3))) {
            imagen.setRutaThumb(cursor.getString(3));
        } else {
            imagen.setRutaThumb("");
        }

        if (!Constantes.esVacio(cursor.getString(4))) {
            imagen.setTipo(cursor.getString(4));
        } else {
            imagen.setTipo("");
        }

    }

    public void eliminarImagen(Integer idImagen) {
        bbdd = bbddhelper.getWritableDatabase();

        String arg[] = {idImagen.toString()};
        bbdd.delete("Imagenes", "id_imagen=?", arg);

        bbdd.close();
    }

    public void setEsFinal(Integer idImagen, boolean esFinal) {
        BBDDImagenes bbddImagenes = new BBDDImagenes(context);
        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("es_final", esFinal);

        String arg[] = {idImagen.toString()};
        bbdd.update("Imagenes", valores, "id_imagen=?", arg);

        bbdd.close();
    }

}
