package com.jmgarzo.bbdd;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Marca;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;
import com.jmgarzo.ratescar.R;

public class BBDDMarcas {

	private SQLiteDatabase bbdd;
	private BBDDSQLiteHelper bbddhelper;
	private ArrayList<Marca> listMarcas;

	public BBDDMarcas(Context context) {

		bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);

	}

	public ArrayList<Marca> getTodasLasMarcas() {
		bbdd = bbddhelper.getWritableDatabase();
		listMarcas = new ArrayList<Marca>();

		Cursor cursorMarcas = bbdd.rawQuery(Constantes.getSelectTodasLasMarcas(), null);
		if (cursorMarcas.moveToFirst()) {
			do {
				Marca marca = new Marca();
				marca.setIdMarca(cursorMarcas.getInt(0));
				marca.setNombre(cursorMarcas.getString(1));
                marca.setIcono(cursorMarcas.getInt(2));

				listMarcas.add(marca);

			} while (cursorMarcas.moveToNext());
		}
		cursorMarcas.close();
		bbdd.close();
		return listMarcas;
	}


    public ArrayList<Marca> getTodasLasMarcasOrderByNombre() {
        bbdd = bbddhelper.getWritableDatabase();
        listMarcas = new ArrayList<Marca>();

        Cursor cursorMarcas = bbdd.rawQuery(Constantes.SELECT_TODAS_LAS_MARCAS_ORDER_BY_NOMBRE, null);
        if (cursorMarcas.moveToFirst()) {
            do {
                Marca marca = new Marca();
                marca.setIdMarca(cursorMarcas.getInt(0));
                marca.setNombre(cursorMarcas.getString(1));
                marca.setIcono(cursorMarcas.getInt(2));

                listMarcas.add(marca);

            } while (cursorMarcas.moveToNext());
        }
        cursorMarcas.close();
        bbdd.close();
        return listMarcas;
    }

	public ArrayList<String> getMarca(String[] id_marca) {
		bbdd = bbddhelper.getWritableDatabase();
		ArrayList<String> listaMarcas = new ArrayList<String>();
		Cursor cursorMarca = bbdd.rawQuery("SELECT nombre FROM Marcas WHERE id_marca in (?)", id_marca);
		if (cursorMarca.moveToFirst()) {
			do {
				listaMarcas.add(cursorMarca.getString(0));

			} while (cursorMarca.moveToNext());

		}
		cursorMarca.close();
		bbdd.close();
		return listaMarcas;
	}

	public Marca getMarca(Integer idMarca) {
		bbdd = bbddhelper.getWritableDatabase();
		// ArrayList<String> listaMarcas = new ArrayList<String>();
		Marca marca = new Marca();
		String[] arg = {idMarca.toString()};
		Cursor cursorMarca = bbdd.rawQuery("SELECT * FROM Marcas WHERE id_marca in (?)", arg);
		if (cursorMarca.moveToFirst()) {
			do {
				//listaMarcas.add(cursorMarca.getString(0));
				marca.setIdMarca(idMarca);
				marca.setNombre(cursorMarca.getString(1));
                marca.setIcono(cursorMarca.getInt(2));

			} while (cursorMarca.moveToNext());

		}
		cursorMarca.close();
		bbdd.close();
		return marca;
	}


    public void recargarMarcas() {
        bbdd = bbddhelper.getWritableDatabase();
//        bbdd.execSQL(Constantes.sqlDropTableMarcas);
//        bbdd.execSQL(Constantes.sqlCreateMarcas);
        listMarcas = new ArrayList<Marca>();
        rellenarMarcas(listMarcas);
        for (Marca marca : listMarcas) {

            ContentValues valores = new ContentValues();
            valores.put("icono", marca.getIcono());
            String[] arg = {marca.getNombre()};

            bbdd.update("Marcas", valores, "nombre = ? ", arg);
        }
    }

    private void rellenarMarcas(ArrayList<Marca> listMarcas) {

        listMarcas.add(new Marca(null, "ACURA", R.drawable.ic_acura));
        listMarcas.add(new Marca(null, "ALFA ROMEO", R.drawable.ic_alfa_romeo));
        listMarcas.add(new Marca(null, "ASTON MARTIN", R.drawable.ic_aston_martin));
        listMarcas.add(new Marca(null, "AUDI", R.drawable.ic_audi));
        listMarcas.add(new Marca(null, "BENTLEY", R.drawable.ic_bentley));
        listMarcas.add(new Marca(null, "BMW", R.drawable.ic_bmw));
        listMarcas.add(new Marca(null, "BUGATTY", R.drawable.ic_bugatti));
        listMarcas.add(new Marca(null, "BUICK", R.drawable.ic_buick));
        listMarcas.add(new Marca(null, "CADILLAC", R.drawable.ic_cadillac));
        listMarcas.add(new Marca(null, "CHERY", R.drawable.ic_chery));
        listMarcas.add(new Marca(null, "CHEVROLET", R.drawable.ic_chevrolet));
        listMarcas.add(new Marca(null, "CHRYSLER", R.drawable.ic_chrysler));
        listMarcas.add(new Marca(null, "CITROËN", R.drawable.ic_citroen));
        listMarcas.add(new Marca(null, "DACIA", R.drawable.ic_dacia));
        listMarcas.add(new Marca(null, "DAEWOO", R.drawable.ic_daewoo));
        listMarcas.add(new Marca(null, "DODGE", R.drawable.ic_dodge));
        listMarcas.add(new Marca(null, "FERRARI", R.drawable.ic_ferrari));
        listMarcas.add(new Marca(null, "FIAT", R.drawable.ic_fiat));
        listMarcas.add(new Marca(null, "FORD", R.drawable.ic_ford));
        listMarcas.add(new Marca(null, "GAZ", R.drawable.ic_gaz));
        listMarcas.add(new Marca(null, "HOLDEN", R.drawable.ic_holden));
        listMarcas.add(new Marca(null, "HONDA", R.drawable.ic_honda));
        listMarcas.add(new Marca(null, "HYUNDAI", R.drawable.ic_hyundai));
        listMarcas.add(new Marca(null, "INFINITI", R.drawable.ic_infiniti));
        listMarcas.add(new Marca(null, "JAGUAR", R.drawable.ic_jaguar));
        listMarcas.add(new Marca(null, "JEEP", R.drawable.ic_jeep));
        listMarcas.add(new Marca(null, "KIA", R.drawable.ic_kia));
        listMarcas.add(new Marca(null, "LADA", R.drawable.ic_lada));
        listMarcas.add(new Marca(null, "LAMBORGHINI", R.drawable.ic_lamborghini));
        listMarcas.add(new Marca(null, "LANCIA", R.drawable.ic_lancia));
        listMarcas.add(new Marca(null, "LAND ROVER", R.drawable.ic_land_rover));
        listMarcas.add(new Marca(null, "LEXUS", R.drawable.ic_lexus));
        listMarcas.add(new Marca(null, "LOTUS", R.drawable.ic_lotus));
        listMarcas.add(new Marca(null, "MASERATI", R.drawable.ic_maserati));
        listMarcas.add(new Marca(null, "MAYBACH", R.drawable.ic_maybach));
        listMarcas.add(new Marca(null, "MAZDA", R.drawable.ic_mazda));
        listMarcas.add(new Marca(null, "MERCEDES", R.drawable.ic_mercedes));
        listMarcas.add(new Marca(null, "MERCURY", R.drawable.ic_mercury));
        listMarcas.add(new Marca(null, "MINI", R.drawable.ic_mini));
        listMarcas.add(new Marca(null, "MITSUBISHI", R.drawable.ic_mitsubishi));
        listMarcas.add(new Marca(null, "NISSAN", R.drawable.ic_nissan));
        listMarcas.add(new Marca(null, "OPEL", R.drawable.ic_opel));
        listMarcas.add(new Marca(null, "PAGANI", R.drawable.ic_pagani));
        listMarcas.add(new Marca(null, "PEUGEOT", R.drawable.ic_peugeot));
        listMarcas.add(new Marca(null, "PONTIAC", R.drawable.ic_pontiac));
        listMarcas.add(new Marca(null, "PORCHE", R.drawable.ic_porshe));
        listMarcas.add(new Marca(null, "RENAULT", R.drawable.ic_renault));
        listMarcas.add(new Marca(null, "ROLL ROYCE", R.drawable.ic_rolls_royce));
        listMarcas.add(new Marca(null, "ROVER", R.drawable.ic_rover));
        listMarcas.add(new Marca(null, "SAAB", R.drawable.ic_saab));
        listMarcas.add(new Marca(null, "SCION", R.drawable.ic_scion));
        listMarcas.add(new Marca(null, "SEAT", R.drawable.ic_seat));
        listMarcas.add(new Marca(null, "SKODA", R.drawable.ic_skoda));
        listMarcas.add(new Marca(null, "SSANG YONG", R.drawable.ic_ssang_yong));
        listMarcas.add(new Marca(null, "SUBARU", R.drawable.ic_subaru));
        listMarcas.add(new Marca(null, "SUZUKI", R.drawable.ic_suzuki));
        listMarcas.add(new Marca(null, "TOYOTA", R.drawable.ic_toyota));
        listMarcas.add(new Marca(null, "VAUXHALL", R.drawable.ic_vauxhall));
        listMarcas.add(new Marca(null, "VOLKSWAGEN", R.drawable.ic_volkswagen));
        listMarcas.add(new Marca(null, "VOLVO", R.drawable.ic_volvo));


    }

    private void recargarMarcas(ArrayList<Marca> listMarcas) {

        listMarcas.add(new Marca(null, "ACURA", R.drawable.ic_acura));
        listMarcas.add(new Marca(null, "ALFA ROMEO", R.drawable.ic_alfa_romeo));
        listMarcas.add(new Marca(null, "ASTON MARTIN", R.drawable.ic_aston_martin));
        listMarcas.add(new Marca(null, "AUDI", R.drawable.ic_audi));
        listMarcas.add(new Marca(null, "BENTLEY", R.drawable.ic_bentley));
        listMarcas.add(new Marca(null, "BMW", R.drawable.ic_bmw));
        listMarcas.add(new Marca(null, "BUGATTY", R.drawable.ic_bugatti));
        listMarcas.add(new Marca(null, "BUICK", R.drawable.ic_buick));
        listMarcas.add(new Marca(null, "CADILLAC", R.drawable.ic_cadillac));
        listMarcas.add(new Marca(null, "CHERY", R.drawable.ic_chery));
        listMarcas.add(new Marca(null, "CHEVROLET", R.drawable.ic_chevrolet));
        listMarcas.add(new Marca(null, "CHRYSLER", R.drawable.ic_chrysler));
        listMarcas.add(new Marca(null, "CITROËN", R.drawable.ic_citroen));
        listMarcas.add(new Marca(null, "DACIA", R.drawable.ic_dacia));
        listMarcas.add(new Marca(null, "DAEWOO", R.drawable.ic_daewoo));
        listMarcas.add(new Marca(null, "DODGE", R.drawable.ic_dodge));
        listMarcas.add(new Marca(null, "FERRARI", R.drawable.ic_ferrari));
        listMarcas.add(new Marca(null, "FIAT", R.drawable.ic_fiat));
        listMarcas.add(new Marca(null, "FORD", R.drawable.ic_ford));
        listMarcas.add(new Marca(null, "GAZ", R.drawable.ic_gaz));
        listMarcas.add(new Marca(null, "HOLDEN", R.drawable.ic_holden));
        listMarcas.add(new Marca(null, "HONDA", R.drawable.ic_honda));
        listMarcas.add(new Marca(null, "HYUNDAI", R.drawable.ic_hyundai));
        listMarcas.add(new Marca(null, "INFINITI", R.drawable.ic_infiniti));
        listMarcas.add(new Marca(null, "JAGUAR", R.drawable.ic_jaguar));
        listMarcas.add(new Marca(null, "JEEP", R.drawable.ic_jeep));
        listMarcas.add(new Marca(null, "KIA", R.drawable.ic_kia));
        listMarcas.add(new Marca(null, "LADA", R.drawable.ic_lada));
        listMarcas.add(new Marca(null, "LAMBORGHINI", R.drawable.ic_lamborghini));
        listMarcas.add(new Marca(null, "LANCIA", R.drawable.ic_lancia));
        listMarcas.add(new Marca(null, "LAND ROVER", R.drawable.ic_land_rover));
        listMarcas.add(new Marca(null, "LEXUS", R.drawable.ic_lexus));
        listMarcas.add(new Marca(null, "LOTUS", R.drawable.ic_lotus));
        listMarcas.add(new Marca(null, "MASERATI", R.drawable.ic_maserati));
        listMarcas.add(new Marca(null, "MAYBACH", R.drawable.ic_maybach));
        listMarcas.add(new Marca(null, "MAZDA", R.drawable.ic_mazda));
        listMarcas.add(new Marca(null, "MERCEDES", R.drawable.ic_mercedes));
        listMarcas.add(new Marca(null, "MERCURY", R.drawable.ic_mercury));
        listMarcas.add(new Marca(null, "MINI", R.drawable.ic_mini));
        listMarcas.add(new Marca(null, "MITSUBISHI", R.drawable.ic_mitsubishi));
        listMarcas.add(new Marca(null, "NISSAN", R.drawable.ic_nissan));
        listMarcas.add(new Marca(null, "OPEL", R.drawable.ic_opel));
        listMarcas.add(new Marca(null, "PAGANI", R.drawable.ic_pagani));
        listMarcas.add(new Marca(null, "PEUGEOT", R.drawable.ic_peugeot));
        listMarcas.add(new Marca(null, "PONTIAC", R.drawable.ic_pontiac));
        listMarcas.add(new Marca(null, "PORCHE", R.drawable.ic_porshe));
        listMarcas.add(new Marca(null, "RENAULT", R.drawable.ic_renault));
        listMarcas.add(new Marca(null, "ROLL ROYCE", R.drawable.ic_rolls_royce));
        listMarcas.add(new Marca(null, "ROVER", R.drawable.ic_rover));
        listMarcas.add(new Marca(null, "SAAB", R.drawable.ic_saab));
        listMarcas.add(new Marca(null, "SCION", R.drawable.ic_scion));
        listMarcas.add(new Marca(null, "SEAT", R.drawable.ic_seat));
        listMarcas.add(new Marca(null, "SKODA", R.drawable.ic_skoda));
        listMarcas.add(new Marca(null, "SSANG YONG", R.drawable.ic_ssang_yong));
        listMarcas.add(new Marca(null, "SUBARU", R.drawable.ic_subaru));
        listMarcas.add(new Marca(null, "SUZUKI", R.drawable.ic_suzuki));
        listMarcas.add(new Marca(null, "TOYOTA", R.drawable.ic_toyota));
        listMarcas.add(new Marca(null, "VAUXHALL", R.drawable.ic_vauxhall));
        listMarcas.add(new Marca(null, "VOLKSWAGEN", R.drawable.ic_volkswagen));
        listMarcas.add(new Marca(null, "VOLVO", R.drawable.ic_volvo));


    }

}
