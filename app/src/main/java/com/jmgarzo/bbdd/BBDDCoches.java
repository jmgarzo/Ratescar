package com.jmgarzo.bbdd;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.jmgarzo.objects.Coche;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;
//import com.jmgarzo.tools.ToolsImagenes;

public class BBDDCoches {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDCoches(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE,
                null, Constantes.BBDD_VERSION);

    }

    public ArrayList<Coche> getTodosLosCoches() {
        bbdd = bbddhelper.getReadableDatabase();
        // bbdd.execSQL("PRAGMA foreign_keys = ON;");
        ArrayList<Coche> listCoches = new ArrayList<Coche>();

        Cursor cursorCoches = bbdd.rawQuery(Constantes.getSelectTodoCoches(),
                null);

        if (cursorCoches.moveToFirst()) {
            /**
             * id_coche ->INTEGER 0 matricula ->TEXT 1 id_marca ->INTEGER 2
             * nombre ->TEXT 3 modelo ->TEXT 4 id_combustible->INTEGER 5 cc
             * ->TEXT 6 tamano_deposito->FLOAT 7 km_iniciales ->INTEGER 8
             * km_actuales ->INTEGER 9 comentarios ->TEXT 10
             * fecha_compra INTEGER 11, fecha_matriculacion INTEGER 12, fecha_fabricacion INTEGER 13
             */
            do {
                Coche coche = new Coche();
                mapearCoche(cursorCoches, coche);
                listCoches.add(coche);

            } while (cursorCoches.moveToNext());
        }

        cursorCoches.close();
        bbdd.close();
        return listCoches;
    }


    public ArrayList<Coche> getTodosLosCochesOrdenadosPorId() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Coche> listCoches = new ArrayList<Coche>();

        ;

        Cursor cursorCoches = bbdd.rawQuery(Constantes.getTodosLosCochesOrdenadosId, null);


        if (cursorCoches.moveToFirst()) {
            /**
             * id_coche ->INTEGER 0 matricula ->TEXT 1 id_marca ->INTEGER 2
             * nombre ->TEXT 3 modelo ->TEXT 4 id_combustible->INTEGER 5 cc
             * ->TEXT 6 tamano_deposito->FLOAT 7 km_iniciales ->INTEGER 8
             * km_actuales ->INTEGER 9 comentarios ->TEXT 10
             * fecha_compra INTEGER 11, fecha_matriculacion INTEGER 12, fecha_fabricacion INTEGER 13
             */
            do {
                Coche coche = new Coche();
                mapearCoche(cursorCoches, coche);
                listCoches.add(coche);

            } while (cursorCoches.moveToNext());
        }

        cursorCoches.close();
        bbdd.close();
        return listCoches;
    }

    public ArrayList<Coche> getTodosLosCochesConRepostajeOrdenadosPorId() {
        bbdd = bbddhelper.getReadableDatabase();
        ArrayList<Coche> listCoches = new ArrayList<Coche>();


        Cursor cursorCoches = bbdd.rawQuery(Constantes.getTodosLosCochesConRepostajesOrdenadosID, null);

        if (cursorCoches.moveToFirst()) {
            /**
             * id_coche ->INTEGER 0 matricula ->TEXT 1 id_marca ->INTEGER 2
             * nombre ->TEXT 3 modelo ->TEXT 4 id_combustible->INTEGER 5 cc
             * ->TEXT 6 tamano_deposito->FLOAT 7 km_iniciales ->INTEGER 8
             * km_actuales ->INTEGER 9 comentarios ->TEXT 10
             * fecha_compra INTEGER 11, fecha_matriculacion INTEGER 12, fecha_fabricacion INTEGER 13
             */
            do {
                Coche coche = new Coche();
                mapearCoche(cursorCoches, coche);
                listCoches.add(coche);

            } while (cursorCoches.moveToNext());
        }

        cursorCoches.close();
        bbdd.close();
        return listCoches;
    }

    /**
     * id_coche ->INTEGER 0 matricula ->TEXT 1 id_marca ->INTEGER 2 nombre
     * ->TEXT 3 modelo ->TEXT 4 id_combustible->INTEGER 5 cc ->TEXT 6
     * tamano_deposito->FLOAT 7 km_iniciales ->INTEGER 8 km_actuales ->INTEGER 9
     * comentarios ->TEXT 10 fecha_compra INTEGER 11, fecha_matriculacion INTEGER 12, fecha_fabricacion INTEGER 13
     */

    public Coche getCoche(String id_coche) {
        Coche coche = null;
        if (null != id_coche) {
            bbdd = bbddhelper.getReadableDatabase();
            String id_coches[] = {id_coche};
            Cursor cursorCoche = bbdd
                    .rawQuery(Constantes.selectCochePorId, id_coches);
            if (cursorCoche.moveToFirst()) {
                coche = new Coche();

                mapearCoche(cursorCoche, coche);

            }
            cursorCoche.close();
            bbdd.close();
        }
        return coche;
    }

    public ArrayList<Integer> getIdsCoche() {
        ArrayList<Integer> listaIdsCoche = new ArrayList<Integer>();
        bbdd = bbddhelper.getReadableDatabase();
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.SELECT_TODOS_LOS_IDS_COCHE, null);
        if (cursorCoche.moveToFirst()) {

            do {
                listaIdsCoche.add(cursorCoche.getInt(0));
            } while (cursorCoche.moveToNext());

        }
        cursorCoche.close();
        bbdd.close();

        return listaIdsCoche;
    }

    /**
     * id_coche ->INTEGER 0 matricula ->TEXT 1 id_marca ->INTEGER 2 nombre
     * ->TEXT 3 modelo ->TEXT 4 id_combustible->INTEGER 5 cc ->TEXT 6
     * tamano_deposito->FLOAT 7 km_iniciales ->INTEGER 8 km_actuales ->INTEGER 9
     * comentarios ->TEXT 10 fecha_compra INTEGER 11, fecha_matriculacion INTEGER 12, fecha_fabricacion INTEGER 13
     */
    public void actualizarCoche(Coche coche) {
        bbdd = bbddhelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("matricula", coche.getMatricula());
        valores.put("id_marca", coche.getIdMarca());
        valores.put("nombre", coche.getNombre());
        valores.put("modelo", coche.getModelo());
        valores.put("version", coche.getVersion());
        valores.put("id_combustible", coche.getIdCombustible());
        valores.put("cc", coche.getCc());
        valores.put("tamano_deposito", coche.getTamanoDeposito().doubleValue());
        valores.put("km_iniciales", coche.getKmIniciales().doubleValue());
        valores.put("km_actuales", coche.getKmActuales().doubleValue());
        valores.put("comentarios", coche.getComentarios());
        valores.put("fecha_compra", coche.getFechaCompra());
        valores.put("fecha_matriculacion", coche.getFechaMatriculacion());
        valores.put("fecha_fabricacion", coche.getFechaFabricacion());


        String arg[] = {coche.getIdCoche().toString()};
        // Actualizamos el registro en la base de datos
        bbdd.update("Coches", valores, "id_coche=?", arg);

        bbdd.close();

    }

    public void nuevoCoche(Coche coche) {
        bbdd = bbddhelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        //valores.put("id_coche", "");
        valores.put("matricula", coche.getMatricula());
        valores.put("id_marca", coche.getIdMarca());
        valores.put("nombre", coche.getNombre());
        valores.put("modelo", coche.getModelo());
        valores.put("version", coche.getVersion());
        valores.put("id_combustible", coche.getIdCombustible());
        valores.put("cc", coche.getCc());
        valores.put("tamano_deposito", coche.getTamanoDeposito().doubleValue());
        valores.put("km_iniciales", coche.getKmIniciales().doubleValue());
        valores.put("km_actuales", coche.getKmActuales().doubleValue());
        valores.put("comentarios", coche.getComentarios());
        valores.put("fecha_compra", coche.getFechaCompra());
        valores.put("fecha_matriculacion", coche.getFechaMatriculacion());
        valores.put("fecha_fabricacion", coche.getFechaFabricacion());

        // Actualizamos el registro en la base de datos
        bbdd.insert("Coches", null, valores);

        bbdd.close();

    }

    public void eliminarCoche(Integer idCoche) {
        bbdd = bbddhelper.getWritableDatabase();

        String arg[] = {idCoche.toString()};
        bbdd.delete("Coches", "id_coche=?", arg);

        bbdd.close();
    }


    public Integer getNumeroCoches() {
        bbdd = bbddhelper.getReadableDatabase();
        Coche coche = null;
        Integer totalCoches = 0;
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.getCuantosCoches, null);
        if (cursorCoche.moveToFirst()) {
            if (null != Integer.valueOf(cursorCoche.getInt(0))) {
                totalCoches = cursorCoche.getInt(0);
            }
        }
        cursorCoche.close();
        bbdd.close();
        return totalCoches;
    }

    public Integer getNumeroCochesConRepostajes() {
        bbdd = bbddhelper.getReadableDatabase();
        Coche coche = null;
        Integer totalCoches = 0;
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.getCuantosCocheConRespostaje, null);
        if (cursorCoche.moveToFirst()) {
            if (null != Integer.valueOf(cursorCoche.getInt(0))) {
                totalCoches = cursorCoche.getInt(0);
            }
        }
        cursorCoche.close();
        bbdd.close();
        return totalCoches;
    }

//    public void anadirFoto(Integer idCoche,String nombre , byte[] imagen) {
//
//        Bitmap image = ToolsImagenes.getImage(imagen);
//
//        Bitmap resized = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * 0.2), (int) (image.getHeight() * 0.2), true);
//
//        bbdd = bbddhelper.getWritableDatabase();
//
//        ContentValues valores = new ContentValues();
//        //valores.put("id_coche", "");
//        valores.put("id_coche", idCoche);
//        valores.put("nombre",nombre );
//        valores.put("imagen", ToolsImagenes.getBytes(resized));
//
//        bbdd.insert("Imagenes_Coche", null, valores);
//        bbdd.close();
//
//    }
//
//    public ArrayList<byte[]> getImagenesPorId(Integer idCoche){
////        bbdd = bbddhelper.getWritableDatabase();
////        bbdd.execSQL(Constantes.sqlDropTableImagenesCoche);
////        bbdd.execSQL(Constantes.sqlCreateImagenesCoche);
//
//        ArrayList<byte[]> listaImagenes = new ArrayList<byte[]>();
//
//        if (null != idCoche) {
//            bbdd = bbddhelper.getReadableDatabase();
//            String arg[] = {idCoche.toString()};
//            Cursor cursorCoche = bbdd
//                    .rawQuery(Constantes.SelectImagenesCochePorId, arg);
//            if (cursorCoche.moveToFirst()) {
//
//                do {
//                    listaImagenes.add(cursorCoche.getBlob(0));
//                }while(cursorCoche.moveToNext());
//            }
//            cursorCoche.close();
//            bbdd.close();
//
//        }
//
//        return listaImagenes;
//    }


    public Integer siguienteIdCoche() {
        bbdd = bbddhelper.getReadableDatabase();
        Integer idCoche = null;
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.getUltimoIdCoche, null);
        if (cursorCoche.moveToFirst()) {

            idCoche = cursorCoche.getInt(0);

        }
        cursorCoche.close();
        bbdd.close();

        if (null != idCoche) {
            idCoche++;
        } else {
            idCoche = 1;
        }
        return idCoche;
    }

    public Integer ultimoIdCoche() {
        bbdd = bbddhelper.getReadableDatabase();
        Integer idCoche = null;
        Cursor cursorCoche = bbdd
                .rawQuery(Constantes.getUltimoIdCoche, null);
        if (cursorCoche.moveToFirst()) {
            if (null != Integer.valueOf(cursorCoche.getInt(0))) {
                idCoche = cursorCoche.getInt(0);
            }
        }
        cursorCoche.close();
        bbdd.close();

        if (null == idCoche) {
            idCoche = 1;
        }
        return idCoche;
    }


    private void mapearCoche(Cursor cursorCoche, Coche coche) {

        Integer idCoche = Integer.valueOf(cursorCoche.getInt(0));
        coche.setIdCoche(idCoche);
        if (!Constantes.esVacio(cursorCoche.getString(1))) {
            coche.setMatricula(cursorCoche.getString(1));
        } else {
            coche.setMatricula("");
        }

        coche.setIdMarca(cursorCoche.getInt(2));

        if (!Constantes.esVacio(cursorCoche.getString(3))) {
            coche.setNombre(cursorCoche.getString(3));
        } else {
            coche.setNombre("");
        }

        if (!Constantes.esVacio(cursorCoche.getString(4))) {
            coche.setModelo(cursorCoche.getString(4));
        } else {
            coche.setModelo("");
        }

        if (!Constantes.esVacio(cursorCoche.getString(5))) {
            coche.setVersion(cursorCoche.getString(5));
        } else {
            coche.setVersion("");
        }

        coche.setIdCombustible(cursorCoche.getInt(6));


        if (!Constantes.esVacio(cursorCoche.getString(7))) {
            coche.setCc(cursorCoche.getString(7));
        } else {
            coche.setCc("");
        }

        BigDecimal tamanoDeposito = new BigDecimal(cursorCoche.getFloat(8));
        if (null != tamanoDeposito) {
            coche.setTamanoDeposito(tamanoDeposito);
        } else {
            coche.setTamanoDeposito(BigDecimal.ZERO);
        }

        BigDecimal kmIniciales = new BigDecimal(cursorCoche.getFloat(9));
        if (null != kmIniciales) {
            coche.setKmIniciales(kmIniciales);
        } else {
            coche.setKmIniciales(BigDecimal.ZERO);
        }

        BigDecimal kmActuales = new BigDecimal(cursorCoche.getFloat(10));
        if (null != kmActuales) {
            coche.setKmActuales(kmActuales);
        } else {
            coche.setKmActuales(BigDecimal.ZERO);
        }

        if (!Constantes.esVacio(cursorCoche.getString(11))) {
            coche.setComentarios(cursorCoche.getString(11));
        } else {
            coche.setComentarios("");
        }

        coche.setFechaCompra(cursorCoche.getLong(12));
        coche.setFechaMatriculacion(cursorCoche.getLong(13));
        coche.setFechaFabricacion(cursorCoche.getLong(14));

    }


}
