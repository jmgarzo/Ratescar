package com.jmgarzo.bbdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.OperacionRecambio;
import com.jmgarzo.objects.Recambio;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

public class BBDDRecambios {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDRecambios(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }


    public void nuevoRecambio(Recambio recambio) {

        bbdd = bbddhelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("id_subcategoria", recambio.getIdSubcategoria());
        valores.put("nombre", recambio.getNombre());
        valores.put("fabricante", recambio.getFabricante());
        valores.put("referencia", recambio.getReferencia());
        valores.put("descripcion", recambio.getDescripcion());
        valores.put("comentarios", recambio.getComentarios());

        bbdd.insert("Recambios", null, valores);


        bbdd.close();

    }


    public void actualizar(Recambio recambio) {
        bbdd = bbddhelper.getWritableDatabase();

//		Recambio recambioAnterior = this.getRecambio(recambio.getIdRecambio().toString());
//
//        if(recambioAnterior.getIdSubcategoria() == recambio.getIdSubcategoria()) {
        ContentValues valores = new ContentValues();
        valores.put("id_subcategoria", recambio.getIdSubcategoria());
        valores.put("nombre", recambio.getNombre());
        valores.put("fabricante", recambio.getFabricante());
        valores.put("referencia", recambio.getReferencia());
        valores.put("descripcion", recambio.getDescripcion());
        valores.put("comentarios", recambio.getComentarios());
        String arg[] = {recambio.getIdRecambio().toString().trim()};
        int result = bbdd.update("Recambios", valores, "id_recambio=?", arg);
//        }
//        else{
//            this.eliminar(recambioAnterior.getIdRecambio().toString());
//            nuevoRecambio(recambio);
//        }

        bbdd.close();
    }


    public Recambio getRecambio(String idRecambio) {
        Recambio recambio = null;

        if (null != idRecambio) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idRecambio};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_RECAMBIO_BY_ID, args);

            if (cursor.moveToFirst()) {
                recambio = new Recambio();
                mapearRecambio(cursor, recambio);
            }
            cursor.close();
            bbdd.close();
        }
        return recambio;
    }

    public ArrayList<Recambio> getRecambiosPorOrdenAlfabetico() {

        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Recambio> listaRecambios = new ArrayList<Recambio>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_RECAMBIOS_POR_ORDEN_ALFABETICO, null);

        if (cursor.moveToFirst()) {
            Recambio recambio = null;

            do {
                recambio = new Recambio();
                mapearRecambio(cursor, recambio);
                listaRecambios.add(recambio);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listaRecambios;

    }


    private void mapearRecambio(Cursor cursor, Recambio recambio) {

        Integer idRecambio = Integer.valueOf(cursor.getInt(0));
        recambio.setIdRecambio(idRecambio);

        Integer idSubcategoria = Integer.valueOf(cursor.getInt(1));
        recambio.setIdSubcategoria(idSubcategoria);


        if (!Constantes.esVacio(cursor.getString(2))) {
            recambio.setNombre(cursor.getString(2));
        } else {
            recambio.setNombre("");
        }

        if (!Constantes.esVacio(cursor.getString(3))) {
            recambio.setFabricante(cursor.getString(3));
        } else {
            recambio.setFabricante("");
        }

        if (!Constantes.esVacio(cursor.getString(4))) {
            recambio.setReferencia(cursor.getString(4));
        } else {
            recambio.setReferencia("");
        }

        if (!Constantes.esVacio(cursor.getString(5))) {
            recambio.setDescripcion(cursor.getString(5));
        } else {
            recambio.setDescripcion("");
        }

        if (!Constantes.esVacio(cursor.getString(6))) {
            recambio.setComentarios(cursor.getString(6));
        } else {
            recambio.setComentarios("");
        }
    }

    public void eliminar(String idRecambio) {

        BBDDOperacionesRecambios bbddOperacionesRecambios = new BBDDOperacionesRecambios(context);
        ArrayList<OperacionRecambio> listaOperacionRecambio = new ArrayList<OperacionRecambio>();
        listaOperacionRecambio = bbddOperacionesRecambios.getOperacionesRecambiosByIdRecambio(idRecambio);

        if (listaOperacionRecambio.size() != 0) {
            bbddOperacionesRecambios.eliminar_por_idRecambio(idRecambio);
        }


        bbdd = bbddhelper.getWritableDatabase();
        String arg[] = {idRecambio};

        bbdd.delete("Recambios", "id_recambio=?", arg);
        bbdd.close();
    }


}
