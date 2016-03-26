package com.jmgarzo.ratescar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jmgarzo.objects.AjustesAplicacion;
import com.jmgarzo.objects.Categoria;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Marca;
import com.jmgarzo.objects.Moneda;
import com.jmgarzo.objects.Operacion;
import com.jmgarzo.objects.Pais;
import com.jmgarzo.objects.Repostaje;
import com.jmgarzo.objects.Subcategoria;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BBDDSQLiteHelper extends SQLiteOpenHelper {

    ArrayList<Marca> listMarcas;

    SQLiteDatabase db;

    private Context context;

    public BBDDSQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;

        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        // *************COMBUSTIBLES**********************
        // Los ditintos tipos de combustible se recuperan sabiendo que el
        // subtipo va a estar vacio.
        // Si se quieren añadir mas tipos de combustible hay que tener en cuenta
        // que hay que crear uno con el subtipo vacio
        List<String> listCombustibles = new ArrayList<String>();
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Gasolina','')");
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Diesel','')");
//        listCombustibles
//                .add("INSERT INTO Combustibles VALUES (null,'GPL','')");
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Diesel','normal')");
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Diesel','premium')");
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Gasolina','95')");
        listCombustibles
                .add("INSERT INTO Combustibles VALUES (null,'Gasolina','98')");

        db.execSQL(Constantes.sqlPragmaON);
        db.execSQL(Constantes.sqlCreateCombustibles);


        // Rellenamos la tabla combustibles
        for (String combustible : listCombustibles) {
            db.execSQL(combustible);
        }

        // ***************MARCAS**********************
        db.execSQL(Constantes.sqlCreateMarcas);


        listMarcas = new ArrayList<Marca>();
        rellenarMarcas(listMarcas);


        // Rellenamos la tabla Marcas
        for (Marca marca : listMarcas) {

            ContentValues valores = new ContentValues();
            valores.put("nombre", marca.getNombre());
            valores.put("icono", marca.getIcono());

            db.insert("Marcas", null, valores);
        }

        // ***************MONEDAS**********************

        db.execSQL(Constantes.CREATE_MONEDAS);
        rellenarMonedas(db);


        // ***************COCHES**********************
        db.execSQL(Constantes.sqlCreateCoches);
//        ArrayList<String> listCochesPrueba = new ArrayList<String>();

        /**
         * id_coche ->INTEGER matricula ->TEXT id_marca ->INTEGER nombre ->TEXT
         * modelo ->TEXT id_combustible->INTEGER cc ->TEXT
         * tamano_deposito->FLOAT km_iniciales ->INTEGER km_actuales ->INTEGER
         * comentarios ->TEXT
         */

//        listCochesPrueba
//                .add("INSERT INTO Coches(id_coche,matricula,id_marca,nombre,modelo,id_combustible,"
//                        + " cc,tamano_deposito,km_iniciales,km_actuales, comentarios)"
//                        + " VALUES (null,'LE-9811-W',47,'21 5 Puertas','21GTD',2,'2098',64,500000,508000,'comentario1')");
//        listCochesPrueba
//                .add("INSERT INTO Coches(id_coche,matricula,id_marca,nombre,modelo,id_combustible,cc,tamano_deposito,km_iniciales,km_actuales,comentarios)"
//                        + " VALUES (null,'SS-7273-AP',47,'21 4 Puertas','21GTD',2,'2098',67,82000,95000,'Comentario2')");
//
//
//        for (String coche : listCochesPrueba) {
//            db.execSQL(coche);
//        }


        db.execSQL(Constantes.sqlCreateRepostajes);

        db.execSQL(Constantes.sqlCreateManoDeObra);

        db.execSQL(Constantes.sqlCreateItvs);

        db.execSQL(Constantes.sqlCreatePeajes);

        db.execSQL(Constantes.sqlCreateSeguros);

        db.execSQL(Constantes.sqlCreateImpuestoCirculacion);

        db.execSQL(Constantes.sqlCreateImagenes);
//*****************************************************************

        db.execSQL(Constantes.CREATE_MANTENIMIENTOS);

        db.execSQL(Constantes.CREATE_OPERACIONES);

        db.execSQL(Constantes.CREATE_MANTENIMIENTO_OPERACION);

        db.execSQL(Constantes.CREATE_CATEGORIAS_RECAMBIO);

        db.execSQL(Constantes.CREATE_SUBCATEGORIAS_RECAMBIO);

        db.execSQL(Constantes.CREATE_RECAMBIOS);

        db.execSQL(Constantes.CREATE_OPERACION_RECAMBIO);

        rellenarCategorias();

        rellenarOperaciones();


        db.execSQL(Constantes.CREATE_PAISES);
        rellenarPaises(db);

        db.execSQL(Constantes.CREATE_AJUSTES_APLICACION);
        inicializarAjustes(db);


        reacondicionarBBDDRepostajesCarreteraYPagos(db);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//*********ACTUALIZAR ICONOS DE MARCAS**************///
//        db.execSQL(Constantes.sqlDropTableMarcas);
//        db.execSQL(Constantes.sqlCreateMarcas);
//        listMarcas = new ArrayList<Marca>();
//        rellenarMarcas(listMarcas);
//        for (Marca marca : listMarcas) {
//
//            ContentValues valores = new ContentValues();
//            valores.put("nombre", marca.getNombre());
//            valores.put("icono", marca.getIcono());
//
//            db.insert("Marcas", null, valores);
//        }
//**********FIN ACTUALIZAR ICONOS DE MARCAS


//          db.execSQL(Constantes.AlterTableCochesAnadirVersión);


        //Para la version V13 de la BBDD


        Marca MarcaPiaggio = new Marca(null, "PIAGGIO", R.drawable.ic_piaggio);

        ContentValues valoresPiaggo = new ContentValues();
        valoresPiaggo.put("nombre", MarcaPiaggio.getNombre());
        valoresPiaggo.put("icono", MarcaPiaggio.getIcono());


        try {
            db.insert("Marcas", null, valoresPiaggo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Marca MarcaEbro = new Marca(null, "EBRO", R.drawable.ic_ebro_logo);

        ContentValues valoresEbro = new ContentValues();
        valoresEbro.put("nombre", MarcaEbro.getNombre());
        valoresEbro.put("icono", MarcaEbro.getIcono());


        try {
            db.insert("Marcas", null, valoresEbro);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //FIN ACTUALIZACION V13

        listMarcas = new ArrayList<Marca>();
        rellenarMarcas(listMarcas);
        for (Marca marca : listMarcas) {

            ContentValues valores = new ContentValues();
            valores.put("icono", marca.getIcono());
            String[] arg = {marca.getNombre()};

            db.update("Marcas", valores, "nombre = ? ", arg);
        }

        try {
            db.execSQL(Constantes.sqlCreateImpuestoCirculacion);

        } catch (SQLException e) {
            Log.e(getClass().getCanonicalName(), "Error al actualizar tablas Create ImpuestoCirculacion");
            e.printStackTrace();
        }
        try {
            db.execSQL(Constantes.AlterTableCochesAnadirVersion);
        } catch (SQLException e) {
            Log.e(getClass().getCanonicalName(), "Error al actualizar tablas ActualizarAnadirVersion");
            e.printStackTrace();
        }


        try {
            db.execSQL(Constantes.sqlCreateImagenes);
        } catch (SQLException e) {
            Log.e(getClass().getCanonicalName(), "Error al Crear Tabla Imagenes");
            e.printStackTrace();
        }
//***************************


        try {
            db.execSQL(Constantes.CREATE_MANTENIMIENTOS);
        } catch (SQLException e) {
            Log.e(getClass().getCanonicalName(), "Error al Crear Tabla Mantenimientos");
        }

        try {
            db.execSQL(Constantes.CREATE_OPERACIONES);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(getClass().getCanonicalName(), "Error al Crear Tabla Operaciones");

        }

        try {
            db.execSQL(Constantes.CREATE_MANTENIMIENTO_OPERACION);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(getClass().getCanonicalName(), "Error al Crear Tabla Mantenimiento Operacion");

        }

        try {
            db.execSQL(Constantes.CREATE_CATEGORIAS_RECAMBIO);
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e(getClass().getCanonicalName(), "Error al Crear Tabla Categorias Recambio");

        }

        try {
            db.execSQL(Constantes.CREATE_SUBCATEGORIAS_RECAMBIO);
            rellenarCategorias();
            rellenarOperaciones();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(Constantes.CREATE_RECAMBIOS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(Constantes.CREATE_OPERACION_RECAMBIO);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //rellenarCategorias();
        //rellenarOperaciones();


        //***V14

        try {
            db.execSQL(Constantes.CREATE_MONEDAS);
            rellenarMonedas(db);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(Constantes.CREATE_PAISES);
            rellenarPaises(db);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            int version = db.getVersion();
            if (oldVersion < 14) {
                db.execSQL(Constantes.CREATE_AJUSTES_APLICACION);
                inicializarAjustes(db);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        reacondicionarBBDDRepostajesCarreteraYPagos(db);
    }

    public static String DB_FILEPATH = "/data/data/com.jmgarzo.ratescar/databases/".concat(Constantes.BBDD_NOMBRE);

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     */
    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            try {
                getWritableDatabase().execSQL(Constantes.AlterTableCochesAnadirVersion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        super.getWritableDatabase().execSQL("PRAGMA foreign_keys = ON;");
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        super.getReadableDatabase().execSQL("PRAGMA foreign_keys = ON;");
        return super.getReadableDatabase();
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
        listMarcas.add(new Marca(null, "EBRO", R.drawable.ic_ebro_logo));
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
        listMarcas.add(new Marca(null, "PIAGGIO", R.drawable.ic_piaggio));
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


    //TODO ¿ELIMINAR PARA VERSION PRO?

    private void rellenarOperaciones() {


        ArrayList<Operacion> listOperaciones = new ArrayList<Operacion>();
        listOperaciones.add(new Operacion("Cambio de Aceite", "Vaciado y llenado del carter de aceite"));
        listOperaciones.add(new Operacion("Cambio Filtro de Aceite", "Sustitución del elemento filtrador del aceite"));
        listOperaciones.add(new Operacion("Cambio Filtro de Aire", "Sustitución del elemento filtrador del aire"));


        for (Operacion op : listOperaciones) {

            ContentValues valoresOperacion = new ContentValues();
            valoresOperacion.put("nombre", op.getNombre());
            valoresOperacion.put("descripcion", op.getDescripcion());

            db.insert("Operaciones", null, valoresOperacion);

        }
    }

    //TODO ¿ELIMINAR PARA VERSION PRO?


    private void rellenarCategorias() {


        ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_frenos), context.getString(R.string.cat_descripcion_frenos)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_direccion), context.getString(R.string.cat_descripcion_direccion)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_escape), context.getString(R.string.cat_descripcion_escape)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_embrague), context.getString(R.string.cat_descripcion_embrague)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_partes_motor), context.getString(R.string.cat_descripcion_partes_motor)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_filtracion), context.getString(R.string.cat_descripcion_filtracion)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_sistema_arranque), context.getString(R.string.cat_descripcion_sistema_arranque)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_piezas_termicas), context.getString(R.string.cat_descripcion_piezas_termicas)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_visibilidad), context.getString(R.string.cat_descripcion_visibilidad)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_piezas_habitaculo), context.getString(R.string.cat_descripcion_piezas_habitaculo)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_accesorios_mantenimiento), context.getString(R.string.cat_descripcion_accesorios_mantenimiento)));
        listCategorias.add(new Categoria(context.getString(R.string.cat_nombre_herramientas), context.getString(R.string.cat_descripcion_herramientas)));


        for (Categoria cat : listCategorias) {

            ContentValues valores = new ContentValues();
            valores.put("nombre", cat.getNombre());
            valores.put("descripcion", cat.getDescripcion());

            db.insert("CategoriasRecambio", null, valores);

        }

        //FRENOS
        ArrayList<Subcategoria> listaSubcategoriasFrenado = new ArrayList<Subcategoria>();
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_pastillas), context.getString(R.string.sub_frenos_descripcion_pastillas)));
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_discos_de_freno), context.getString(R.string.sub_frenos_descripcion_discos_de_freno)));
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_frenos_de_tambor), context.getString(R.string.sub_frenos_descripcion_frenos_de_tambor)));
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_hidraulica), context.getString(R.string.sub_frenos_descripcion_hidraulica)));
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_captores_y_cables), context.getString(R.string.sub_frenos_descripcion_captores_y_cables)));
        listaSubcategoriasFrenado.add(new Subcategoria(context.getString(R.string.sub_frenos_nombre_ayuda_al_frenada), context.getString(R.string.sub_frenos_descripcion_ayuda_al_frenada)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_frenos), listaSubcategoriasFrenado);


        //Dirección, Suspensión, Tren
        ArrayList<Subcategoria> listaSubcategoriasDireccion = new ArrayList<Subcategoria>();
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_amortiguadores), context.getString(R.string.sub_direccion_descripcion_amortiguadores)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_cojinetes), context.getString(R.string.sub_direccion_descripcion_cojinetes)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_muelles_y_fuelles), context.getString(R.string.sub_direccion_descripcion_muelles_y_fuelles)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_cubos_y_rodamientos), context.getString(R.string.sub_direccion_descripcion_cubos_y_rodamientos)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_rotulas_y_suspension), context.getString(R.string.sub_direccion_descripcion_rotulas_y_suspension)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_suspension_de_ejes), context.getString(R.string.sub_direccion_descripcion_suspension_de_ejes)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_direccion), context.getString(R.string.sub_direccion_descripcion_direccion)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_rotulas_de_direccion), context.getString(R.string.sub_direccion_descripcion_rotulas_de_direccion)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_transmision), context.getString(R.string.sub_direccion_descripcion_transmision)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_rueda), context.getString(R.string.sub_direccion_descripcion_rueda)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_kit_reparacion_montaje), context.getString(R.string.sub_direccion_descripcion_kit_reparacion_montaje)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_otras_piezas_suspension), context.getString(R.string.sub_direccion_descripcion_otras_piezas_suspension)));
        listaSubcategoriasDireccion.add(new Subcategoria(context.getString(R.string.sub_direccion_nombre_otras_piezas_transmisión), context.getString(R.string.sub_direccion_descripcion_otras_piezas_transmisión)));


        rellenarSubcategoria(context.getString(R.string.cat_nombre_direccion), listaSubcategoriasDireccion);


        //Escape
        ArrayList<Subcategoria> listaSubcategoriasEscape = new ArrayList<Subcategoria>();
        listaSubcategoriasEscape.add(new Subcategoria(context.getString(R.string.sub_escape_nombre_silenciadores_y_tubos), context.getString(R.string.sub_escape_descripcion_silenciadores_y_tubos)));
        listaSubcategoriasEscape.add(new Subcategoria(context.getString(R.string.sub_escape_nombre_catalizadores_y_filtros_de_particulas), context.getString(R.string.sub_escape_descripcion_catalizadores_y_filtros_de_particulas)));
        listaSubcategoriasEscape.add(new Subcategoria(context.getString(R.string.sub_escape_nombre_captadores_de_escape), context.getString(R.string.sub_escape_descripcion_captadores_de_escape)));
        listaSubcategoriasEscape.add(new Subcategoria(context.getString(R.string.sub_escape_nombre_otras_piezas_de_escape), context.getString(R.string.sub_escape_descripcion_otras_piezas_de_escape)));
        listaSubcategoriasEscape.add(new Subcategoria(context.getString(R.string.sub_escape_nombre_accesorios_de_montaje), context.getString(R.string.sub_escape_descripcion_accesorios_de_montaje)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_escape), listaSubcategoriasEscape);


        //Embrague

        ArrayList<Subcategoria> listaSubcategoriasEmbrague = new ArrayList<Subcategoria>();
        listaSubcategoriasEmbrague.add(new Subcategoria(context.getString(R.string.sub_embrague_nombre_embrague_y_volante_motor), context.getString(R.string.sub_embrague_descripcion_embrague_y_volante_motor)));
        listaSubcategoriasEmbrague.add(new Subcategoria(context.getString(R.string.sub_embrague_nombre_otras_piezas_embrague), context.getString(R.string.sub_embrague_descripcion_otras_piezas_embrague)));
        listaSubcategoriasEmbrague.add(new Subcategoria(context.getString(R.string.sub_embrague_nombre_accesorios_caja_cambios), context.getString(R.string.sub_embrague_descripcion_accesorios_caja_cambios)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_embrague), listaSubcategoriasEmbrague);

        //Partes del motor
        ArrayList<Subcategoria> listaSubcategoriasPartesMotor = new ArrayList<Subcategoria>();
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_correas), context.getString(R.string.sub_motor_descripcion_correas)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_bomba), context.getString(R.string.sub_motor_descripcion_bomba)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_bujias_y_precalentamiento), context.getString(R.string.sub_motor_descripcion_bujias_y_precalentamiento)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_motor_y_culata), context.getString(R.string.sub_motor_descripcion_motor_y_culata)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_soportes_motor), context.getString(R.string.sub_motor_descripcion_soportes_motor)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_turbocompresor), context.getString(R.string.sub_motor_descripcion_turbocompresor)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_inyeccion_y_carburacion), context.getString(R.string.sub_motor_descripcion_inyeccion_y_carburacion)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_aceite_accesorios_purga), context.getString(R.string.sub_motor_descripcion_aceite_accesorios_purga)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_lubricacion), context.getString(R.string.sub_motor_descripcion_lubricacion)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_valvulas), context.getString(R.string.sub_motor_descripcion_valvulas)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_captores_y_cables), context.getString(R.string.sub_motor_descripcion_captores_y_cables)));
        listaSubcategoriasPartesMotor.add(new Subcategoria(context.getString(R.string.sub_motor_nombre_anticongelante), context.getString(R.string.sub_motor_nombre_anticongelante)));


        rellenarSubcategoria(context.getString(R.string.cat_nombre_partes_motor), listaSubcategoriasPartesMotor);

        //Filtracion
        ArrayList<Subcategoria> listaSubcategoriasFiltracion = new ArrayList<Subcategoria>();
        listaSubcategoriasFiltracion.add(new Subcategoria(context.getString(R.string.sub_filtracion_nombre_filtro), context.getString(R.string.sub_filtracion_descripcion_filtro)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_filtracion), listaSubcategoriasFiltracion);


        //Sistema de carga y arranque
        ArrayList<Subcategoria> listaSubcategoriasSistemaCarga = new ArrayList<Subcategoria>();
        listaSubcategoriasSistemaCarga.add(new Subcategoria(context.getString(R.string.sub_sistema_arranque_nombre_baterias), context.getString(R.string.sub_sistema_arranque_descripcion_baterias)));
        listaSubcategoriasSistemaCarga.add(new Subcategoria(context.getString(R.string.sub_sistema_arranque_nombre_alternadores), context.getString(R.string.sub_sistema_arranque_descripcion_alternadores)));
        listaSubcategoriasSistemaCarga.add(new Subcategoria(context.getString(R.string.sub_sistema_arranque_nombre_motores_de_arranque), context.getString(R.string.sub_sistema_arranque_descripcion_motores_de_arranque)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_sistema_arranque), listaSubcategoriasSistemaCarga);


        //piezas termicas y climatizacion
        ArrayList<Subcategoria> listaSubcategoriasPiezasTermicas = new ArrayList<Subcategoria>();
        listaSubcategoriasPiezasTermicas.add(new Subcategoria(context.getString(R.string.sub_piezas_termicas_nombre_refrigeracion), context.getString(R.string.sub_piezas_termicas_descripcion_refrigeracion)));
        listaSubcategoriasPiezasTermicas.add(new Subcategoria(context.getString(R.string.sub_piezas_termicas_nombre_climatizacion), context.getString(R.string.sub_piezas_termicas_descripcion_climatizacion)));
        listaSubcategoriasPiezasTermicas.add(new Subcategoria(context.getString(R.string.sub_piezas_termicas_nombre_calefacción_y_ventilacion), context.getString(R.string.sub_piezas_termicas_descripcion_calefacción_y_ventilacion)));
        listaSubcategoriasPiezasTermicas.add(new Subcategoria(context.getString(R.string.sub_piezas_termicas_nombre_captores_y_sondas_termicas), context.getString(R.string.sub_piezas_termicas_descripcion_captores_y_sondas_termicas)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_piezas_termicas), listaSubcategoriasPiezasTermicas);


        //visibilidad

        ArrayList<Subcategoria> listaSubcategoriasVisibilidad = new ArrayList<Subcategoria>();
        listaSubcategoriasVisibilidad.add(new Subcategoria(context.getString(R.string.sub_visibilidad_nombre_limpiaparabrisas), context.getString(R.string.sub_visibilidad_descripcion_limpiaparabrisas)));
        listaSubcategoriasVisibilidad.add(new Subcategoria(context.getString(R.string.sub_visibilidad_nombre_opticos_y_faros), context.getString(R.string.sub_visibilidad_descripcion_opticos_y_faros)));
        listaSubcategoriasVisibilidad.add(new Subcategoria(context.getString(R.string.sub_visibilidad_nombre_bombillas), context.getString(R.string.sub_visibilidad_descripcion_bombillas)));
        listaSubcategoriasVisibilidad.add(new Subcategoria(context.getString(R.string.sub_visibilidad_nombre_retrovisor_exterior), context.getString(R.string.sub_visibilidad_descripcion_retrovisor_exterior)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_visibilidad), listaSubcategoriasVisibilidad);

        //piezas habitáculo

        ArrayList<Subcategoria> listaSubcategoriasPiezasHabitaculo = new ArrayList<Subcategoria>();
        listaSubcategoriasPiezasHabitaculo.add(new Subcategoria(context.getString(R.string.sub_piezas_habitaculo_nombre_muelles_neumaticos), context.getString(R.string.sub_piezas_habitaculo_descripcion_muelles_neumaticos)));
        listaSubcategoriasPiezasHabitaculo.add(new Subcategoria(context.getString(R.string.sub_piezas_habitaculo_nombre_elevalunas), context.getString(R.string.sub_piezas_habitaculo_descripcion_elevalunas)));
        listaSubcategoriasPiezasHabitaculo.add(new Subcategoria(context.getString(R.string.sub_piezas_habitaculo_nombre_cierre), context.getString(R.string.sub_piezas_habitaculo_descripcion_cierre)));
        listaSubcategoriasPiezasHabitaculo.add(new Subcategoria(context.getString(R.string.sub_piezas_habitaculo_nombre_electricidad), context.getString(R.string.sub_piezas_habitaculo_descripcion_electricidad)));
        listaSubcategoriasPiezasHabitaculo.add(new Subcategoria(context.getString(R.string.sub_piezas_habitaculo_nombre_mandos_y_pedales), context.getString(R.string.sub_piezas_habitaculo_descripcion_mandos_y_pedales)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_piezas_habitaculo), listaSubcategoriasPiezasHabitaculo);


        //Accesorios y mantenimiento
        ArrayList<Subcategoria> listaSubcategoriasAccesorios = new ArrayList<Subcategoria>();
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_ayuda_conduccion), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_ayuda_conduccion)));
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_productos_mantenimiento), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_productos_mantenimiento)));
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_tornillos_y_tuercas), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_tornillos_y_tuercas)));
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_limpieza_carroceria), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_limpieza_carroceria)));
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_liquido_limpiacristales), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_liquido_limpiacristales)));
        listaSubcategoriasAccesorios.add(new Subcategoria(context.getString(R.string.sub_accesorios_mantenimiento_nombre_cadenas_y_fundas_de_nieve), context.getString(R.string.sub_accesorios_mantenimiento_descripcion_cadenas_y_fundas_de_nieve)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_accesorios_mantenimiento), listaSubcategoriasAccesorios);


        //Herramientas
        ArrayList<Subcategoria> listaSubcategoriasHerramientas = new ArrayList<Subcategoria>();
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_bujias), context.getString(R.string.sub_herramientas_descripcion_bujias)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_uso_general), context.getString(R.string.sub_herramientas_descripcion_uso_general)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_de_frenos), context.getString(R.string.sub_herramientas_descripcion_de_frenos)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_equipo_individual), context.getString(R.string.sub_herramientas_descripcion_equipo_individual)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_calado_y_distribucion), context.getString(R.string.sub_herramientas_descripcion_calado_y_distribucion)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_para_baterias), context.getString(R.string.sub_herramientas_descripcion_para_baterias)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_carracas_y_vasos), context.getString(R.string.sub_herramientas_descripcion_carracas_y_vasos)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_de_motor), context.getString(R.string.sub_herramientas_descripcion_de_motor)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_embrague_y_caja_cambios), context.getString(R.string.sub_herramientas_descripcion_embrague_y_caja_cambios)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_direccion_suspension_tren), context.getString(R.string.sub_herramientas_descripcion_direccion_suspension_tren)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_cambio_de_aceite), context.getString(R.string.sub_herramientas_descripcion_cambio_de_aceite)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_llaves), context.getString(R.string.sub_herramientas_descripcion_llaves)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_filtrado), context.getString(R.string.sub_herramientas_descripcion_filtrado)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_estuches), context.getString(R.string.sub_herramientas_descripcion_estuches)));
        listaSubcategoriasHerramientas.add(new Subcategoria(context.getString(R.string.sub_herramientas_nombre_escape), context.getString(R.string.sub_herramientas_descripcion_escape)));

        rellenarSubcategoria(context.getString(R.string.cat_nombre_herramientas), listaSubcategoriasHerramientas);


    }


    private void rellenarSubcategoria(String nombreCategoria, ArrayList<Subcategoria> listaSubcategorias) {

        String[] arg = {nombreCategoria};
        Cursor cursor = db.rawQuery(Constantes.SELECT_ID_CATEGORIA_BY_NOMBRE_CATEGORIA, arg);
        Integer idCategoria = 0;
        if (cursor.moveToFirst()) {
            idCategoria = cursor.getInt(0);
        }

        if (null != idCategoria) {

            for (Subcategoria cat : listaSubcategorias) {

                ContentValues valores = new ContentValues();
                valores.put("id_categoria", idCategoria);
                valores.put("nombre", cat.getNombre());
                valores.put("descripcion", cat.getDescripcion());

                db.insert("SubcategoriasRecambio", null, valores);

            }
        }

    }


    private void rellenarMonedas(SQLiteDatabase db) {

        ArrayList<Moneda> listMonedas = new ArrayList<Moneda>();

        listMonedas.add(new Moneda(null, "AED", 784, 2));
        listMonedas.add(new Moneda(null, "AFN", 971, 2));
        listMonedas.add(new Moneda(null, "ALL", 8, 2));
        listMonedas.add(new Moneda(null, "AMD", 51, 2));
        listMonedas.add(new Moneda(null, "ANG", 532, 2));
        listMonedas.add(new Moneda(null, "AOA", 973, 2));
        listMonedas.add(new Moneda(null, "ARS", 32, 2));
        listMonedas.add(new Moneda(null, "AUD", 36, 2));
        listMonedas.add(new Moneda(null, "AWG", 533, 2));
        listMonedas.add(new Moneda(null, "AZN", 944, 2));
        listMonedas.add(new Moneda(null, "BAM", 977, 2));
        listMonedas.add(new Moneda(null, "BBD", 52, 2));
        listMonedas.add(new Moneda(null, "BDT", 50, 2));
        listMonedas.add(new Moneda(null, "BGN", 975, 2));
        listMonedas.add(new Moneda(null, "BHD", 48, 3));
        listMonedas.add(new Moneda(null, "BIF", 108, 0));
        listMonedas.add(new Moneda(null, "BMD", 60, 2));
        listMonedas.add(new Moneda(null, "BND", 96, 2));
        listMonedas.add(new Moneda(null, "BOB", 68, 2));
        listMonedas.add(new Moneda(null, "BOV", 984, 2));
        listMonedas.add(new Moneda(null, "BRL", 986, 2));
        listMonedas.add(new Moneda(null, "BSD", 44, 2));
        listMonedas.add(new Moneda(null, "BTN", 64, 2));
        listMonedas.add(new Moneda(null, "BWP", 72, 2));
        listMonedas.add(new Moneda(null, "BYR", 974, 0));
        listMonedas.add(new Moneda(null, "BZD", 84, 2));
        listMonedas.add(new Moneda(null, "CAD", 124, 2));
        listMonedas.add(new Moneda(null, "CDF", 976, 2));
        listMonedas.add(new Moneda(null, "CHE", 947, 2));
        listMonedas.add(new Moneda(null, "CHF", 756, 2));
        listMonedas.add(new Moneda(null, "CHW", 948, 2));
        listMonedas.add(new Moneda(null, "CLF", 990, 4));
        listMonedas.add(new Moneda(null, "CLP", 152, 0));
        listMonedas.add(new Moneda(null, "CNY", 156, 2));
        listMonedas.add(new Moneda(null, "COP", 170, 2));
        listMonedas.add(new Moneda(null, "COU", 970, 2));
        listMonedas.add(new Moneda(null, "CRC", 188, 2));
        listMonedas.add(new Moneda(null, "CUC", 931, 2));
        listMonedas.add(new Moneda(null, "CUP", 192, 2));
        listMonedas.add(new Moneda(null, "CVE", 132, 0));
        listMonedas.add(new Moneda(null, "CZK", 203, 2));
        listMonedas.add(new Moneda(null, "DJF", 262, 0));
        listMonedas.add(new Moneda(null, "DKK", 208, 2));
        listMonedas.add(new Moneda(null, "DOP", 214, 2));
        listMonedas.add(new Moneda(null, "DZD", 12, 2));
        listMonedas.add(new Moneda(null, "EGP", 818, 2));
        listMonedas.add(new Moneda(null, "ERN", 232, 2));
        listMonedas.add(new Moneda(null, "ETB", 230, 2));
        listMonedas.add(new Moneda(null, "EUR", 978, 2));
        listMonedas.add(new Moneda(null, "FJD", 242, 2));
        listMonedas.add(new Moneda(null, "FKP", 238, 2));
        listMonedas.add(new Moneda(null, "GBP", 826, 2));
        listMonedas.add(new Moneda(null, "GEL", 981, 2));
        listMonedas.add(new Moneda(null, "GHS", 936, 2));
        listMonedas.add(new Moneda(null, "GIP", 292, 2));
        listMonedas.add(new Moneda(null, "GMD", 270, 2));
        listMonedas.add(new Moneda(null, "GNF", 324, 0));
        listMonedas.add(new Moneda(null, "GTQ", 320, 2));
        listMonedas.add(new Moneda(null, "GYD", 328, 2));
        listMonedas.add(new Moneda(null, "HKD", 344, 2));
        listMonedas.add(new Moneda(null, "HNL", 340, 2));
        listMonedas.add(new Moneda(null, "HRK", 191, 2));
        listMonedas.add(new Moneda(null, "HTG", 332, 2));
        listMonedas.add(new Moneda(null, "HUF", 348, 2));
        listMonedas.add(new Moneda(null, "IDR", 360, 2));
        listMonedas.add(new Moneda(null, "ILS", 376, 2));
        listMonedas.add(new Moneda(null, "INR", 356, 2));
        listMonedas.add(new Moneda(null, "IQD", 368, 3));
        listMonedas.add(new Moneda(null, "IRR", 364, 2));
        listMonedas.add(new Moneda(null, "ISK", 352, 0));
        listMonedas.add(new Moneda(null, "JMD", 388, 2));
        listMonedas.add(new Moneda(null, "JOD", 400, 3));
        listMonedas.add(new Moneda(null, "JPY", 392, 0));
        listMonedas.add(new Moneda(null, "KES", 404, 2));
        listMonedas.add(new Moneda(null, "KGS", 417, 2));
        listMonedas.add(new Moneda(null, "KHR", 116, 2));
        listMonedas.add(new Moneda(null, "KMF", 174, 0));
        listMonedas.add(new Moneda(null, "KPW", 408, 2));
        listMonedas.add(new Moneda(null, "KRW", 410, 0));
        listMonedas.add(new Moneda(null, "KWD", 414, 3));
        listMonedas.add(new Moneda(null, "KYD", 136, 2));
        listMonedas.add(new Moneda(null, "KZT", 398, 2));
        listMonedas.add(new Moneda(null, "LAK", 418, 2));
        listMonedas.add(new Moneda(null, "LBP", 422, 2));
        listMonedas.add(new Moneda(null, "LKR", 144, 2));
        listMonedas.add(new Moneda(null, "LRD", 430, 2));
        listMonedas.add(new Moneda(null, "LSL", 426, 2));
        listMonedas.add(new Moneda(null, "LYD", 434, 3));
        listMonedas.add(new Moneda(null, "MAD", 504, 2));
        listMonedas.add(new Moneda(null, "MDL", 498, 2));
        listMonedas.add(new Moneda(null, "MGA", 969, 1));
        listMonedas.add(new Moneda(null, "MKD", 807, 2));
        listMonedas.add(new Moneda(null, "MMK", 104, 2));
        listMonedas.add(new Moneda(null, "MNT", 496, 2));
        listMonedas.add(new Moneda(null, "MOP", 446, 2));
        listMonedas.add(new Moneda(null, "MRO", 478, 1));
        listMonedas.add(new Moneda(null, "MUR", 480, 2));
        listMonedas.add(new Moneda(null, "MVR", 462, 2));
        listMonedas.add(new Moneda(null, "MWK", 454, 2));
        listMonedas.add(new Moneda(null, "MXN", 484, 2));
        listMonedas.add(new Moneda(null, "MXV", 979, 2));
        listMonedas.add(new Moneda(null, "MYR", 458, 2));
        listMonedas.add(new Moneda(null, "MZN", 943, 2));
        listMonedas.add(new Moneda(null, "NAD", 516, 2));
        listMonedas.add(new Moneda(null, "NGN", 566, 2));
        listMonedas.add(new Moneda(null, "NIO", 558, 2));
        listMonedas.add(new Moneda(null, "NOK", 578, 2));
        listMonedas.add(new Moneda(null, "NPR", 524, 2));
        listMonedas.add(new Moneda(null, "NZD", 554, 2));
        listMonedas.add(new Moneda(null, "OMR", 512, 3));
        listMonedas.add(new Moneda(null, "PAB", 590, 2));
        listMonedas.add(new Moneda(null, "PEN", 604, 2));
        listMonedas.add(new Moneda(null, "PGK", 598, 2));
        listMonedas.add(new Moneda(null, "PHP", 608, 2));
        listMonedas.add(new Moneda(null, "PKR", 586, 2));
        listMonedas.add(new Moneda(null, "PLN", 985, 2));
        listMonedas.add(new Moneda(null, "PYG", 600, 0));
        listMonedas.add(new Moneda(null, "QAR", 634, 2));
        listMonedas.add(new Moneda(null, "RON", 946, 2));
        listMonedas.add(new Moneda(null, "RSD", 941, 2));
        listMonedas.add(new Moneda(null, "RUB", 643, 2));
        listMonedas.add(new Moneda(null, "RWF", 646, 0));
        listMonedas.add(new Moneda(null, "SAR", 682, 2));
        listMonedas.add(new Moneda(null, "SBD", 90, 2));
        listMonedas.add(new Moneda(null, "SCR", 690, 2));
        listMonedas.add(new Moneda(null, "SDG", 938, 2));
        listMonedas.add(new Moneda(null, "SEK", 752, 2));
        listMonedas.add(new Moneda(null, "SGD", 702, 2));
        listMonedas.add(new Moneda(null, "SHP", 654, 2));
        listMonedas.add(new Moneda(null, "SLL", 694, 2));
        listMonedas.add(new Moneda(null, "SOS", 706, 2));
        listMonedas.add(new Moneda(null, "SRD", 968, 2));
        listMonedas.add(new Moneda(null, "SSP", 728, 2));
        listMonedas.add(new Moneda(null, "STD", 678, 2));
        listMonedas.add(new Moneda(null, "SYP", 760, 2));
        listMonedas.add(new Moneda(null, "SZL", 748, 2));
        listMonedas.add(new Moneda(null, "THB", 764, 2));
        listMonedas.add(new Moneda(null, "TJS", 972, 2));
        listMonedas.add(new Moneda(null, "TMT", 934, 2));
        listMonedas.add(new Moneda(null, "TND", 788, 3));
        listMonedas.add(new Moneda(null, "TOP", 776, 2));
        listMonedas.add(new Moneda(null, "TRY", 949, 2));
        listMonedas.add(new Moneda(null, "TTD", 780, 2));
        listMonedas.add(new Moneda(null, "TWD", 901, 2));
        listMonedas.add(new Moneda(null, "TZS", 834, 2));
        listMonedas.add(new Moneda(null, "UAH", 980, 2));
        listMonedas.add(new Moneda(null, "UGX", 800, 0));
        listMonedas.add(new Moneda(null, "USD", 840, 2));
        listMonedas.add(new Moneda(null, "USN", 997, 2));
        listMonedas.add(new Moneda(null, "USS", 998, 2));
        listMonedas.add(new Moneda(null, "UYI", 940, 0));
        listMonedas.add(new Moneda(null, "UYU", 858, 2));
        listMonedas.add(new Moneda(null, "UZS", 860, 2));
        listMonedas.add(new Moneda(null, "VEF", 937, 2));
        listMonedas.add(new Moneda(null, "VND", 704, 0));
        listMonedas.add(new Moneda(null, "VUV", 548, 0));
        listMonedas.add(new Moneda(null, "WST", 882, 2));
        listMonedas.add(new Moneda(null, "XAF", 950, 0));
        listMonedas.add(new Moneda(null, "XAG", 961, 0));
        listMonedas.add(new Moneda(null, "XAU", 959, 0));
        listMonedas.add(new Moneda(null, "XBA", 955, 0));
        listMonedas.add(new Moneda(null, "XBB", 956, 0));
        listMonedas.add(new Moneda(null, "XBC", 957, 0));
        listMonedas.add(new Moneda(null, "XBD", 958, 0));
        listMonedas.add(new Moneda(null, "XCD", 951, 2));
        listMonedas.add(new Moneda(null, "XDR", 960, 0));
        listMonedas.add(new Moneda(null, "XFU", 0, 0));
        listMonedas.add(new Moneda(null, "XOF", 952, 0));
        listMonedas.add(new Moneda(null, "XPD", 964, 0));
        listMonedas.add(new Moneda(null, "XPF", 953, 0));
        listMonedas.add(new Moneda(null, "XPT", 962, 0));
        listMonedas.add(new Moneda(null, "XSU", 994, 0));
        listMonedas.add(new Moneda(null, "XTS", 963, 0));
        listMonedas.add(new Moneda(null, "XUA", 965, 0));
        listMonedas.add(new Moneda(null, "XXX", 999, 0));
        listMonedas.add(new Moneda(null, "YER", 886, 2));
        listMonedas.add(new Moneda(null, "ZAR", 710, 2));
        listMonedas.add(new Moneda(null, "ZMW", 967, 2));


        // Rellenamos la tabla Monedas
        for (Moneda moneda : listMonedas) {

            ContentValues valores = new ContentValues();
            valores.put("code", moneda.getCode());
            valores.put("number", moneda.getNumber());
            valores.put("decimal", moneda.getDecimal());

            db.insert("Monedas", null, valores);


        }


    }


    private void rellenarPaises(SQLiteDatabase db) {

        ArrayList<Pais> listPaises = new ArrayList<Pais>();

        // listPaises.add(new Pais(null,"AED",784,2));

        listPaises.add(new Pais(null, "AND", "Andorra"));
        listPaises.add(new Pais(null, "ARE", "United Arab Emirates"));
        listPaises.add(new Pais(null, "ARG", "Argentina"));
        listPaises.add(new Pais(null, "ARM", "Armenia"));
        listPaises.add(new Pais(null, "ASM", "American Samoa"));
        listPaises.add(new Pais(null, "ATA", "Antarctica"));
        listPaises.add(new Pais(null, "ATF", "French Southern Territories"));
        listPaises.add(new Pais(null, "ATG", "Antigua and Barbuda"));
        listPaises.add(new Pais(null, "AUS", "Australia"));
        listPaises.add(new Pais(null, "AUT", "Austria"));
        listPaises.add(new Pais(null, "AZE", "Azerbaijan"));
        listPaises.add(new Pais(null, "BDI", "Burundi"));
        listPaises.add(new Pais(null, "BEL", "Belgium"));
        listPaises.add(new Pais(null, "BEN", "Benin"));
        listPaises.add(new Pais(null, "BES", "Bonaire, Sint Eustatius and Saba"));
        listPaises.add(new Pais(null, "BFA", "Burkina Faso"));
        listPaises.add(new Pais(null, "BGD", "Bangladesh"));
        listPaises.add(new Pais(null, "BGR", "Bulgaria"));
        listPaises.add(new Pais(null, "BHR", "Bahrain"));
        listPaises.add(new Pais(null, "BHS", "Bahamas"));
        listPaises.add(new Pais(null, "BIH", "Bosnia and Herzegovina"));
        listPaises.add(new Pais(null, "BLM", "Saint Barthélemy"));
        listPaises.add(new Pais(null, "BLR", "Belarus"));
        listPaises.add(new Pais(null, "BLZ", "Belize"));
        listPaises.add(new Pais(null, "BMU", "Bermuda"));
        listPaises.add(new Pais(null, "BOL", "Bolivia, Plurinational State of"));
        listPaises.add(new Pais(null, "BRA", "Brazil"));
        listPaises.add(new Pais(null, "BRB", "Barbados"));
        listPaises.add(new Pais(null, "BRN", "Brunei Darussalam"));
        listPaises.add(new Pais(null, "BTN", "Bhutan"));
        listPaises.add(new Pais(null, "BVT", "Bouvet Island"));
        listPaises.add(new Pais(null, "BWA", "Botswana"));
        listPaises.add(new Pais(null, "CAF", "Central African Republic"));
        listPaises.add(new Pais(null, "CAN", "Canada"));
        listPaises.add(new Pais(null, "CCK", "Cocos (Keeling) Islands"));
        listPaises.add(new Pais(null, "CHE", "Switzerland"));
        listPaises.add(new Pais(null, "CHL", "Chile"));
        listPaises.add(new Pais(null, "CHN", "China"));
        listPaises.add(new Pais(null, "CIV", "Côte d'Ivoire"));
        listPaises.add(new Pais(null, "CMR", "Cameroon"));
        listPaises.add(new Pais(null, "COD", "Congo, the Democratic Republic of the"));
        listPaises.add(new Pais(null, "COG", "Congo"));
        listPaises.add(new Pais(null, "COK", "Cook Islands"));
        listPaises.add(new Pais(null, "COL", "Colombia"));
        listPaises.add(new Pais(null, "COM", "Comoros"));
        listPaises.add(new Pais(null, "CPV", "Cabo Verde"));
        listPaises.add(new Pais(null, "CRI", "Costa Rica"));
        listPaises.add(new Pais(null, "CUB", "Cuba"));
        listPaises.add(new Pais(null, "CUW", "Curaçao"));
        listPaises.add(new Pais(null, "CXR", "Christmas Island"));
        listPaises.add(new Pais(null, "CYM", "Cayman Islands"));
        listPaises.add(new Pais(null, "CYP", "Cyprus"));
        listPaises.add(new Pais(null, "CZE", "Czech Republic"));
        listPaises.add(new Pais(null, "DEU", "Germany"));
        listPaises.add(new Pais(null, "DJI", "Djibouti"));
        listPaises.add(new Pais(null, "DMA", "Dominica"));
        listPaises.add(new Pais(null, "DNK", "Denmark"));
        listPaises.add(new Pais(null, "DOM", "Dominican Republic"));
        listPaises.add(new Pais(null, "DZA", "Algeria"));
        listPaises.add(new Pais(null, "ECU", "Ecuador"));
        listPaises.add(new Pais(null, "EGY", "Egypt"));
        listPaises.add(new Pais(null, "ERI", "Eritrea"));
        listPaises.add(new Pais(null, "ESH", "Western Sahara"));
        listPaises.add(new Pais(null, "ESP", "Spain"));
        listPaises.add(new Pais(null, "EST", "Estonia"));
        listPaises.add(new Pais(null, "ETH", "Ethiopia"));
        listPaises.add(new Pais(null, "FIN", "Finland"));
        listPaises.add(new Pais(null, "FJI", "Fiji"));
        listPaises.add(new Pais(null, "FLK", "Falkland Islands (Malvinas)"));
        listPaises.add(new Pais(null, "FRA", "France"));
        listPaises.add(new Pais(null, "FRO", "Faroe Islands"));
        listPaises.add(new Pais(null, "FSM", "Micronesia, Federated States of"));
        listPaises.add(new Pais(null, "GAB", "Gabon"));
        listPaises.add(new Pais(null, "GBR", "United Kingdom"));
        listPaises.add(new Pais(null, "GEO", "Georgia"));
        listPaises.add(new Pais(null, "GGY", "Guernsey"));
        listPaises.add(new Pais(null, "GHA", "Ghana"));
        listPaises.add(new Pais(null, "GIB", "Gibraltar"));
        listPaises.add(new Pais(null, "GIN", "Guinea"));
        listPaises.add(new Pais(null, "GLP", "Guadeloupe"));
        listPaises.add(new Pais(null, "GMB", "Gambia"));
        listPaises.add(new Pais(null, "GNB", "Guinea-Bissau"));
        listPaises.add(new Pais(null, "GNQ", "Equatorial Guinea"));
        listPaises.add(new Pais(null, "GRC", "Greece"));
        listPaises.add(new Pais(null, "GRD", "Grenada"));
        listPaises.add(new Pais(null, "GRL", "Greenland"));
        listPaises.add(new Pais(null, "GTM", "Guatemala"));
        listPaises.add(new Pais(null, "GUF", "French Guiana"));
        listPaises.add(new Pais(null, "GUM", "Guam"));
        listPaises.add(new Pais(null, "GUY", "Guyana"));
        listPaises.add(new Pais(null, "HKG", "Hong Kong"));
        listPaises.add(new Pais(null, "HMD", "Heard Island and McDonald Islands"));
        listPaises.add(new Pais(null, "HND", "Honduras"));
        listPaises.add(new Pais(null, "HRV", "Croatia"));
        listPaises.add(new Pais(null, "HTI", "Haiti"));
        listPaises.add(new Pais(null, "HUN", "Hungary"));
        listPaises.add(new Pais(null, "IDN", "Indonesia"));
        listPaises.add(new Pais(null, "IMN", "Isle of Man"));
        listPaises.add(new Pais(null, "IND", "India"));
        listPaises.add(new Pais(null, "IOT", "British Indian Ocean Territory"));
        listPaises.add(new Pais(null, "IRL", "Ireland"));
        listPaises.add(new Pais(null, "IRN", "Iran, Islamic Republic of"));
        listPaises.add(new Pais(null, "IRQ", "Iraq"));
        listPaises.add(new Pais(null, "ISL", "Iceland"));
        listPaises.add(new Pais(null, "ISR", "Israel"));
        listPaises.add(new Pais(null, "ITA", "Italy"));
        listPaises.add(new Pais(null, "JAM", "Jamaica"));
        listPaises.add(new Pais(null, "JEY", "Jersey"));
        listPaises.add(new Pais(null, "JOR", "Jordan"));
        listPaises.add(new Pais(null, "JPN", "Japan"));
        listPaises.add(new Pais(null, "KAZ", "Kazakhstan"));
        listPaises.add(new Pais(null, "KEN", "Kenya"));
        listPaises.add(new Pais(null, "KGZ", "Kyrgyzstan"));
        listPaises.add(new Pais(null, "KHM", "Cambodia"));
        listPaises.add(new Pais(null, "KIR", "Kiribati"));
        listPaises.add(new Pais(null, "KNA", "Saint Kitts and Nevis"));
        listPaises.add(new Pais(null, "KOR", "Korea, Republic of"));
        listPaises.add(new Pais(null, "KWT", "Kuwait"));
        listPaises.add(new Pais(null, "LAO", "Lao People's Democratic Republic"));
        listPaises.add(new Pais(null, "LBN", "Lebanon"));
        listPaises.add(new Pais(null, "LBR", "Liberia"));
        listPaises.add(new Pais(null, "LBY", "Libya"));
        listPaises.add(new Pais(null, "LCA", "Saint Lucia"));
        listPaises.add(new Pais(null, "LIE", "Liechtenstein"));
        listPaises.add(new Pais(null, "LKA", "Sri Lanka"));
        listPaises.add(new Pais(null, "LSO", "Lesotho"));
        listPaises.add(new Pais(null, "LTU", "Lithuania"));
        listPaises.add(new Pais(null, "LUX", "Luxembourg"));
        listPaises.add(new Pais(null, "LVA", "Latvia"));
        listPaises.add(new Pais(null, "MAC", "Macao"));
        listPaises.add(new Pais(null, "MAF", "Saint Martin (French part)"));
        listPaises.add(new Pais(null, "MAR", "Morocco"));
        listPaises.add(new Pais(null, "MCO", "Monaco"));
        listPaises.add(new Pais(null, "MDA", "Moldova, Republic of"));
        listPaises.add(new Pais(null, "MDG", "Madagascar"));
        listPaises.add(new Pais(null, "MDV", "Maldives"));
        listPaises.add(new Pais(null, "MEX", "Mexico"));
        listPaises.add(new Pais(null, "MHL", "Marshall Islands"));
        listPaises.add(new Pais(null, "MKD", "Macedonia, the former Yugoslav Republic of"));
        listPaises.add(new Pais(null, "MLI", "Mali"));
        listPaises.add(new Pais(null, "MLT", "Malta"));
        listPaises.add(new Pais(null, "MMR", "Myanmar"));
        listPaises.add(new Pais(null, "MNE", "Montenegro"));
        listPaises.add(new Pais(null, "MNG", "Mongolia"));
        listPaises.add(new Pais(null, "MNP", "Northern Mariana Islands"));
        listPaises.add(new Pais(null, "MOZ", "Mozambique"));
        listPaises.add(new Pais(null, "MRT", "Mauritania"));
        listPaises.add(new Pais(null, "MSR", "Montserrat"));
        listPaises.add(new Pais(null, "MTQ", "Martinique"));
        listPaises.add(new Pais(null, "MUS", "Mauritius"));
        listPaises.add(new Pais(null, "MWI", "Malawi"));
        listPaises.add(new Pais(null, "MYS", "Malaysia"));
        listPaises.add(new Pais(null, "MYT", "Mayotte"));
        listPaises.add(new Pais(null, "NAM", "Namibia"));
        listPaises.add(new Pais(null, "NCL", "New Caledonia"));
        listPaises.add(new Pais(null, "NER", "Niger"));
        listPaises.add(new Pais(null, "NFK", "Norfolk Island"));
        listPaises.add(new Pais(null, "NGA", "Nigeria"));
        listPaises.add(new Pais(null, "NIC", "Nicaragua"));
        listPaises.add(new Pais(null, "NIU", "Niue"));
        listPaises.add(new Pais(null, "NLD", "Netherlands"));
        listPaises.add(new Pais(null, "NOR", "Norway"));
        listPaises.add(new Pais(null, "NPL", "Nepal"));
        listPaises.add(new Pais(null, "NRU", "Nauru"));
        listPaises.add(new Pais(null, "NZL", "New Zealand"));
        listPaises.add(new Pais(null, "OMN", "Oman"));
        listPaises.add(new Pais(null, "PAK", "Pakistan"));
        listPaises.add(new Pais(null, "PAN", "Panama"));
        listPaises.add(new Pais(null, "PCN", "Pitcairn"));
        listPaises.add(new Pais(null, "PER", "Peru"));
        listPaises.add(new Pais(null, "PHL", "Philippines"));
        listPaises.add(new Pais(null, "PLW", "Palau"));
        listPaises.add(new Pais(null, "PNG", "Papua New Guinea"));
        listPaises.add(new Pais(null, "POL", "Poland"));
        listPaises.add(new Pais(null, "PRI", "Puerto Rico"));
        listPaises.add(new Pais(null, "PRK", "Korea, Democratic People's Republic of"));
        listPaises.add(new Pais(null, "PRT", "Portugal"));
        listPaises.add(new Pais(null, "PRY", "Paraguay"));
        listPaises.add(new Pais(null, "PSE", "Palestine, State of"));
        listPaises.add(new Pais(null, "PYF", "French Polynesia"));
        listPaises.add(new Pais(null, "QAT", "Qatar"));
        listPaises.add(new Pais(null, "REU", "Réunion"));
        listPaises.add(new Pais(null, "ROU", "Romania"));
        listPaises.add(new Pais(null, "RUS", "Russian Federation"));
        listPaises.add(new Pais(null, "RWA", "Rwanda"));
        listPaises.add(new Pais(null, "SAU", "Saudi Arabia"));
        listPaises.add(new Pais(null, "SDN", "Sudan"));
        listPaises.add(new Pais(null, "SEN", "Senegal"));
        listPaises.add(new Pais(null, "SGP", "Singapore"));
        listPaises.add(new Pais(null, "SGS", "South Georgia and the South Sandwich Islands"));
        listPaises.add(new Pais(null, "SHN", "Saint Helena, Ascension and Tristan da Cunha"));
        listPaises.add(new Pais(null, "SJM", "Svalbard and Jan Mayen"));
        listPaises.add(new Pais(null, "SLB", "Solomon Islands"));
        listPaises.add(new Pais(null, "SLE", "Sierra Leone"));
        listPaises.add(new Pais(null, "SLV", "El Salvador"));
        listPaises.add(new Pais(null, "SMR", "San Marino"));
        listPaises.add(new Pais(null, "SOM", "Somalia"));
        listPaises.add(new Pais(null, "SPM", "Saint Pierre and Miquelon"));
        listPaises.add(new Pais(null, "SRB", "Serbia"));
        listPaises.add(new Pais(null, "SSD", "South Sudan"));
        listPaises.add(new Pais(null, "STP", "Sao Tome and Principe"));
        listPaises.add(new Pais(null, "SUR", "Suriname"));
        listPaises.add(new Pais(null, "SVK", "Slovakia"));
        listPaises.add(new Pais(null, "SVN", "Slovenia"));
        listPaises.add(new Pais(null, "SWE", "Sweden"));
        listPaises.add(new Pais(null, "SWZ", "Swaziland"));
        listPaises.add(new Pais(null, "SXM", "Sint Maarten (Dutch part)"));
        listPaises.add(new Pais(null, "SYC", "Seychelles"));
        listPaises.add(new Pais(null, "SYR", "Syrian Arab Republic"));
        listPaises.add(new Pais(null, "TCA", "Turks and Caicos Islands"));
        listPaises.add(new Pais(null, "TCD", "Chad"));
        listPaises.add(new Pais(null, "TGO", "Togo"));
        listPaises.add(new Pais(null, "THA", "Thailand"));
        listPaises.add(new Pais(null, "TJK", "Tajikistan"));
        listPaises.add(new Pais(null, "TKL", "Tokelau"));
        listPaises.add(new Pais(null, "TKM", "Turkmenistan"));
        listPaises.add(new Pais(null, "TLS", "Timor-Leste"));
        listPaises.add(new Pais(null, "TON", "Tonga"));
        listPaises.add(new Pais(null, "TTO", "Trinidad and Tobago"));
        listPaises.add(new Pais(null, "TUN", "Tunisia"));
        listPaises.add(new Pais(null, "TUR", "Turkey"));
        listPaises.add(new Pais(null, "TUV", "Tuvalu"));
        listPaises.add(new Pais(null, "TWN", "Taiwan, Province of China"));
        listPaises.add(new Pais(null, "TZA", "Tanzania, United Republic of"));
        listPaises.add(new Pais(null, "UGA", "Uganda"));
        listPaises.add(new Pais(null, "UKR", "Ukraine"));
        listPaises.add(new Pais(null, "UMI", "United States Minor Outlying Islands"));
        listPaises.add(new Pais(null, "URY", "Uruguay"));
        listPaises.add(new Pais(null, "USA", "United States"));
        listPaises.add(new Pais(null, "UZB", "Uzbekistan"));
        listPaises.add(new Pais(null, "VAT", "Holy See (Vatican City State)"));
        listPaises.add(new Pais(null, "VCT", "Saint Vincent and the Grenadines"));
        listPaises.add(new Pais(null, "VEN", "Venezuela, Bolivarian Republic of"));
        listPaises.add(new Pais(null, "VGB", "Virgin Islands, British"));
        listPaises.add(new Pais(null, "VIR", "Virgin Islands, U.S."));
        listPaises.add(new Pais(null, "VNM", "Viet Nam"));
        listPaises.add(new Pais(null, "VUT", "Vanuatu"));
        listPaises.add(new Pais(null, "WLF", "Wallis and Futuna"));
        listPaises.add(new Pais(null, "WSM", "Samoa"));
        listPaises.add(new Pais(null, "YEM", "Yemen"));
        listPaises.add(new Pais(null, "ZAF", "South Africa"));
        listPaises.add(new Pais(null, "ZMB", "Zambia"));
        listPaises.add(new Pais(null, "ZWE", "Zimbabwe"));


        // Rellenamos la tabla Monedas
        for (Pais pais : listPaises) {

            ContentValues valores = new ContentValues();
            valores.put("nombre_iso", pais.getNombreIS0());
            valores.put("nombre", pais.getNombre());

            db.insert("Paises", null, valores);


        }


    }

    //listCantidadCombustible.add("litros/liters");
    //listCantidadCombustible.add("Gallons(UK)");
    //listCantidadCombustible.add("Gallons(US)");

    //listAjusteDistancia.add("Km");
    //listAjusteDistancia.add("Miles");
    private void inicializarAjustes(SQLiteDatabase db) {

        ArrayList<AjustesAplicacion> listAjustes = new ArrayList<AjustesAplicacion>();

        String valor = "";
        Locale.getDefault().getDisplayLanguage();
        String[] args = {"inicializados"};
        Cursor cursor = db.rawQuery(Constantes.SELECT_AJUSTES_APLICACION_VALOR_POR_NOMBRE_AJUSTE, args);

        if (cursor.moveToFirst()) {
            valor = cursor.getString(0);
        }
        cursor.close();
        boolean resultado = false;
        if (null != valor && !"".equalsIgnoreCase(valor)) {
            if (valor.equalsIgnoreCase("no")) {
                resultado = false;
            } else if (valor.equalsIgnoreCase("si")) {
                resultado = true;
            }
        }
        if (!resultado) {


            //Valores posibles todas las monedas de la tabla monedas
            listAjustes.add(new AjustesAplicacion(null, "moneda", "EUR", "moneda"));
            //Valores posibles litros/liters,galonesUK,galonesUS
            listAjustes.add(new AjustesAplicacion(null, "cantidadCombustible", Constantes.LITROS, "cantidad de combustible"));
            //valores posibles km,millas
            listAjustes.add(new AjustesAplicacion(null, "distancia", Constantes.KM, "distancia"));

            listAjustes.add(new AjustesAplicacion(null, "idioma", Locale.getDefault().getDisplayLanguage(), "idioma"));

            listAjustes.add(new AjustesAplicacion(null, "inicializados", "no", "Si se han inicializado los ajustes"));
        }


        for (AjustesAplicacion ajuste : listAjustes) {
            ContentValues valores = new ContentValues();
            valores.put("nombre", ajuste.getNombre());
            valores.put("valor", ajuste.getValor());
            valores.put("descripcion", ajuste.getDescripcion());

            db.insert("AjustesAplicacion", null, valores);
        }
    }


    public void reacondicionarBBDDRepostajesCarreteraYPagos(SQLiteDatabase db) {

        //Cogemos todos los repostajes


        if (db.getVersion() < 14) {
            Cursor cursor = db.rawQuery(Constantes.SelectTodosLosRepostajesPorFechaDesc, null);
            ArrayList<Repostaje> listaRepostajes = new ArrayList<Repostaje>();
            if (cursor.moveToFirst()) {
                do {

                    Repostaje repostaje = new Repostaje();
                    Integer idRepostaje = Integer.valueOf(cursor.getInt(0));
                    repostaje.setIdRepostaje(idRepostaje);

                    Integer idCoche = Integer.valueOf(cursor.getInt(1));
                    repostaje.setIdCoche(idCoche);

                    Integer idCombustible = Integer.valueOf(cursor
                            .getInt(2));
                    repostaje.setIdCombustible(idCombustible);

                    BigDecimal kmRepostaje = new BigDecimal
                            (cursor.getFloat(3));
                    if (null != kmRepostaje) {
                        repostaje.setKmRepostaje(kmRepostaje);
                    } else {
                        repostaje.setKmRepostaje(BigDecimal.ZERO);
                    }

                    BigDecimal litros = new BigDecimal(cursor.getFloat(4));
                    if (null != litros) {
                        repostaje.setLitros(litros);
                    } else {
                        repostaje.setLitros(BigDecimal.ZERO);
                    }

                    BigDecimal precioLitro = new BigDecimal(
                            cursor.getFloat(5));
                    if (null != precioLitro) {
                        repostaje.setPrecioLitro(precioLitro);
                    } else {
                        repostaje.setPrecioLitro(BigDecimal.ZERO);
                    }

                    BigDecimal costeRepostaje = new BigDecimal(
                            cursor.getFloat(6));
                    if (null != costeRepostaje) {
                        repostaje.setCosteRepostaje(costeRepostaje);
                    } else {
                        repostaje.setCosteRepostaje(BigDecimal.ZERO);
                    }

                    if (cursor.getInt(7) == 0) {
                        repostaje.setEsCompleto(false);
                    } else {
                        repostaje.setEsCompleto(true);
                    }

                    if (cursor.getInt(8) == 0) {
                        repostaje.setEsAA(false);
                    } else {
                        repostaje.setEsAA(true);
                    }

                    if (cursor.getInt(9) == 0) {
                        repostaje.setEsRemolque(false);
                    } else {
                        repostaje.setEsRemolque(true);
                    }

                    if (cursor.getInt(10) == 0) {
                        repostaje.setEsBaca(false);
                    } else {
                        repostaje.setEsBaca(true);
                    }

                    if (!Constantes.esVacio(cursor.getString(11))) {
                        repostaje.setTipoCarretera(cursor.getString(11));
                    } else {
                        repostaje.setTipoCarretera("");
                    }

                    if (!Constantes.esVacio(cursor.getString(12))) {
                        repostaje.setTipoPago(cursor.getString(12));
                    } else {
                        repostaje.setTipoPago("");
                    }

                    BigDecimal velocidadMedia = new BigDecimal(
                            cursor.getFloat(13));
                    if (null != velocidadMedia) {
                        repostaje.setVelocidadMedia(velocidadMedia);
                    } else {
                        repostaje.setVelocidadMedia(BigDecimal.ZERO);
                    }

                    if (!Constantes.esVacio(cursor.getString(14))) {
                        repostaje.setAreaServicio((cursor.getString(14)));
                    } else {
                        repostaje.setAreaServicio("");
                    }

                    repostaje.setFechaRespostaje(cursor.getLong(15));

                    if (!Constantes.esVacio(cursor.getString(16))) {
                        repostaje
                                .setTipoConduccion((cursor.getString(16)));
                    } else {
                        repostaje.setTipoConduccion("");
                    }

                    if (!Constantes.esVacio(cursor.getString(17))) {
                        repostaje.setComentarios((cursor.getString(17)));
                    } else {
                        repostaje.setComentarios("");
                    }

                    BigDecimal kmRecorridos = new BigDecimal(
                            cursor.getFloat(18));
                    if (null != kmRecorridos) {
                        repostaje.setKmRecorridos(kmRecorridos);
                    }

                    BigDecimal mediaConsumo = new BigDecimal(
                            cursor.getFloat(19));
                    if (null != mediaConsumo) {
                        repostaje.setMediaConsumo(mediaConsumo);
                    }
                    listaRepostajes.add(repostaje);


                } while (cursor.moveToNext());
            }
            cursor.close();

//        listTipoCarretera.add("Carretera");
//        listTipoCarretera.add("Ciudad");
//        listTipoCarretera.add("Autopista");
//        listTipoCarretera.add("Mixto");

//        <string name="tipo_carretera_carretera">Highway</string>
//        <string name="tipo_carretera_ciudad">City</string>
//        <string name="tipo_carretera_autopista">Freeway</string>
//        <string name="tipo_carretera_mixto">Mixed</string>
//
//        <string name="tipo_de_pago_efectivo">Cash</string>
//        <string name="tipo_de_pago_tarjeta">Card</string>
//        listTipoPago.add("Efectivo");
//        listTipoPago.add("Tarjeta");

            ArrayList<Repostaje> repostajesParaActualizar = new ArrayList<Repostaje>();

            for (Repostaje rep : listaRepostajes) {

                if (!rep.getTipoCarretera().equalsIgnoreCase("") || !rep.getTipoPago().equalsIgnoreCase("")) {

                    if (!rep.getTipoCarretera().equalsIgnoreCase("")) {

                        if (rep.getTipoCarretera().equalsIgnoreCase("Carretera")) {
                            rep.setTipoCarretera("tipo_carretera_carretera");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Ciudad")) {
                            rep.setTipoCarretera("tipo_carretera_ciudad");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Autopista")) {
                            rep.setTipoCarretera("tipo_carretera_autopista");
                        } else if (rep.getTipoCarretera().equalsIgnoreCase("Mixto")) {
                            rep.setTipoCarretera("tipo_carretera_mixto");
                        }
                    }

                    if (!rep.getTipoPago().equalsIgnoreCase("")) {
                        if (rep.getTipoPago().equalsIgnoreCase("Efectivo")) {
                            rep.setTipoPago("tipo_de_pago_efectivo");
                        } else if (rep.getTipoPago().equalsIgnoreCase("Tarjeta")) {
                            rep.setTipoPago("tipo_de_pago_tarjeta");
                        }
                    }


                }
                if (rep.getTipoConduccion().equalsIgnoreCase("Normal")) {
                    rep.setTipoConduccion("tipo_de_conduccion_normal");
                } else if (rep.getTipoConduccion().equalsIgnoreCase("Económica")) {
                    rep.setTipoConduccion("tipo_de_conduccion_economica");
                } else if (rep.getTipoConduccion().equalsIgnoreCase("Deportiva")) {
                    rep.setTipoConduccion("tipo_de_conduccion_deportiva");
                }
                repostajesParaActualizar.add(rep);
            }

            for (Repostaje rep : repostajesParaActualizar) {


                ContentValues valores = new ContentValues();
//            valores.put("id_repostaje", rep.getIdRepostaje());
//            valores.put("id_coche", rep.getIdCoche());
//            valores.put("id_combustible", rep.getIdCombustible());
//            valores.put("km_repostaje", rep.getKmRepostaje().doubleValue());
//            valores.put("litros", rep.getLitros().doubleValue());
//            valores.put("precio_litro", rep.getPrecioLitro().doubleValue());
//            valores.put("coste_repostaje", rep.getCosteRepostaje()
//                    .doubleValue());
//            valores.put("es_completo", rep.getEsCompleto());
//            valores.put("es_aa", rep.getEsAA());
//            valores.put("es_remolque", rep.getEsRemolque());
//            valores.put("es_baca", rep.getEsBaca());
                valores.put("tipo_carretera", rep.getTipoCarretera());
                valores.put("tipo_pago", rep.getTipoPago());
//            valores.put("velocidad_media", rep.getVelocidadMedia()
//                    .doubleValue());
//            valores.put("fecha_repostaje", rep.getFechaRespostaje());
                 valores.put("tipo_conduccion", rep.getTipoConduccion());
//            valores.put("comentarios", rep.getComentarios());
//            valores.put("km_recorridos", rep.getKmRecorridos().doubleValue());
//            valores.put("media_consumo", rep.getMediaConsumo().doubleValue());

                String arg[] = {rep.getIdRepostaje().toString().trim()};
                int result = db.update("Repostajes", valores, "id_repostaje=?", arg);


            }


        }
    }


}
