package com.jmgarzo.bbdd;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmgarzo.objects.Combustible;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;

public class BBDDCombustibles {
	private SQLiteDatabase bbdd;
	private BBDDSQLiteHelper bbddhelper;
	private Context context;

	public BBDDCombustibles(Context context) {
		this.context = context;
		bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE,
				null, Constantes.BBDD_VERSION);
	}

	public ArrayList<Combustible> getTodosLosCombustibles() {
		bbdd = bbddhelper.getReadableDatabase();
		ArrayList<Combustible> listCombustibles = new ArrayList<Combustible>();

		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectTodosLosCombustibles, null);

		if (cursorCombustibles.moveToFirst()) {
			do {
				Combustible combustible = new Combustible();

				combustible.setIdCombustible(cursorCombustibles.getInt(0));
				if (null != cursorCombustibles.getString(1)) {
					combustible.setTipo(cursorCombustibles.getString(1));
				} else {
					combustible.setTipo("");
				}

				if (null != cursorCombustibles.getString(2)) {
					combustible.setSubtipo(cursorCombustibles.getString(2));
				} else {
					combustible.setSubtipo("");
				}

				listCombustibles.add(combustible);

			} while (cursorCombustibles.moveToNext());

		}
		cursorCombustibles.close();
		bbdd.close();
		return listCombustibles;
	}

	public Combustible getCombustible(Integer idCombustible) {
		bbdd = bbddhelper.getReadableDatabase();
		Combustible combustible = null;
		String idStringCombustible[] = { idCombustible.toString() };
		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectCombustiblesById, idStringCombustible);
		if (cursorCombustibles.moveToFirst()) {
			combustible = new Combustible();
			combustible.setIdCombustible(cursorCombustibles.getInt(0));
			if (null != cursorCombustibles.getString(1)) {
				combustible.setTipo(cursorCombustibles.getString(1));
			} else {
				combustible.setTipo("");
			}

			if (null != cursorCombustibles.getString(2)) {
				combustible.setSubtipo(cursorCombustibles.getColumnName(2));
			} else {
				combustible.setSubtipo("");
			}

		}
		cursorCombustibles.close();
		bbdd.close();

		return combustible;

	}

	public ArrayList<String> getStringOnlyTipoCombustibles() {
		bbdd = bbddhelper.getReadableDatabase();
		ArrayList<String> tiposCombustibles = null;
		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectOnlyTiposCombustibles, null);
		if (cursorCombustibles.moveToFirst()) {
			tiposCombustibles = new ArrayList<String>();
			do {
				tiposCombustibles.add(cursorCombustibles.getString(0));

			} while (cursorCombustibles.moveToNext());
		}
		cursorCombustibles.close();
		bbdd.close();

		return tiposCombustibles;

	}

	/**
	 * 
	 * @return
	 * 
	 *         Solo devuelve los tipos de combustible existentes, no los
	 *         subtipos.Es decir los que en BBDD tengan el campo subtipo=''
	 */

	public ArrayList<Combustible> getOnlyTipoCombustibles() {
		bbdd = bbddhelper.getReadableDatabase();
		Combustible combustible;
		ArrayList<Combustible> listTiposCombustibles = null;
		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectOnlyTiposCombustibles, null);
		if (cursorCombustibles.moveToFirst()) {
			listTiposCombustibles = new ArrayList<Combustible>();
			do {
				combustible = new Combustible();
				combustible.setIdCombustible(cursorCombustibles.getInt(0));
				if (null != cursorCombustibles.getString(1)) {
					combustible.setTipo(cursorCombustibles.getString(1));
				} else {
					combustible.setTipo("");
				}

				if (null != cursorCombustibles.getString(2)) {
					combustible.setSubtipo(cursorCombustibles.getColumnName(2));
				} else {
					combustible.setSubtipo("");
				}
				listTiposCombustibles.add(combustible);

			} while (cursorCombustibles.moveToNext());
		}
		cursorCombustibles.close();
		bbdd.close();

		return listTiposCombustibles;

	}

	/**
	 * 
	 * @param idCombustible
	 * @return Devuelve una lista de Combutibles que sean correspondientes con
	 *         el tipo generico del id de combustible.<br>
	 *         Es decir devuelve todos los tipos con subtipo dado el id de un
	 *         combustible general.
	 * 
	 */
	public ArrayList<Combustible> getCombustibleByTipo(Integer idCombustible) {
		bbdd = bbddhelper.getReadableDatabase();
		Combustible combustible;
		ArrayList<Combustible> listTiposCombustibles = null;
		int idTipoGeneral = idCombustible;
		String[] arg = { (idCombustible.toString()) };
		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectCombustiblesByIdTipoGeneral, arg);
		if (cursorCombustibles.moveToFirst()) {
			listTiposCombustibles = new ArrayList<Combustible>();
			do {
				combustible = new Combustible();
				combustible.setIdCombustible(cursorCombustibles.getInt(0));
				if (null != cursorCombustibles.getString(1)) {
					combustible.setTipo(cursorCombustibles.getString(1));
				} else {
					combustible.setTipo("");
				}

				if (null != cursorCombustibles.getString(2)) {
					combustible.setSubtipo(cursorCombustibles.getString(2));
				} else {
					combustible.setSubtipo("");
				}
				listTiposCombustibles.add(combustible);

			} while (cursorCombustibles.moveToNext());
		}
		cursorCombustibles.close();
		bbdd.close();

		return listTiposCombustibles;
	}

	public Cursor getCursorCombustiblesByTipo(Integer idCombustible) {
		bbdd = bbddhelper.getReadableDatabase();

		int idTipoGeneral = idCombustible;
		String[] arg = { (idCombustible.toString()) };
		Cursor cursorCombustibles = bbdd.rawQuery(
				Constantes.selectCombustiblesByIdTipoGeneral, arg);

		// cursorCombustibles.close();
		bbdd.close();

		return cursorCombustibles;
	}

}
