package com.jmgarzo.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Pais;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by JoseMaria on 27/10/2015.
 */
public class BBDDPaises {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private ArrayList<Pais> listPaises;

    public BBDDPaises(Context context) {

        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);

    }


    public ArrayList<String> getNombreIsoOrderByNombreIso() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listNombre = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_PAISES_NOMBREISO_BY_NOMBREISO, null);
        if (cursor.moveToFirst()) {
            do {
                listNombre.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listNombre;

    }


    public ArrayList<Pais> getPaisesOrderByNombre() {
        bbdd = bbddhelper.getReadableDatabase();
        listPaises = new ArrayList<Pais>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_PAISES_ORDER_NOMBRE, null);
        if (cursor.moveToFirst()) {
            do {
                Pais pais = new Pais();
                pais.setIdPais(cursor.getInt(0));
                pais.setNombreIS0(cursor.getString(1));
                pais.setNombre(cursor.getString(2));

                listPaises.add(pais);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return listPaises;

    }


}
