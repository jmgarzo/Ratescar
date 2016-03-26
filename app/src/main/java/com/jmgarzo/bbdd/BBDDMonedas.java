package com.jmgarzo.bbdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Moneda;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 1/07/15.
 */
public class BBDDMonedas {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private ArrayList<Moneda> listMonedas;

    public BBDDMonedas(Context context) {

        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);

    }

    public ArrayList<Moneda> getTodasLasMonedasOrderCode(){
        bbdd = bbddhelper.getReadableDatabase();
        listMonedas = new ArrayList<Moneda>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_MONEDAS_ORDER_CODE,null);
        if (cursor.moveToFirst()) {
            do {
                Moneda moneda = new Moneda();
                moneda.setIdMoneda(cursor.getInt(0));
                moneda.setCode(cursor.getString(1));
                moneda.setNumber(cursor.getInt(2));
                moneda.setDecimal(cursor.getInt(3));

                listMonedas.add(moneda);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return  listMonedas;

        }

    public ArrayList<String> getTodosLosCode(){
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<String> listCodes = new ArrayList<String>();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_MONEDAS_CODE_ORDER_CODE,null);
        if (cursor.moveToFirst()) {
            do {
                listCodes.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();
        return  listCodes;

    }


}
