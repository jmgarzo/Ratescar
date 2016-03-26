package com.jmgarzo.bbdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jmgarzo.objects.Categoria;
import com.jmgarzo.objects.Constantes;
import com.jmgarzo.objects.Subcategoria;
import com.jmgarzo.ratescar.BBDDSQLiteHelper;
import com.jmgarzo.ratescar.R;

import java.util.ArrayList;

/**
 * Created by jmgarzo on 16/04/15.
 */
public class BBDDCategorias {

    private SQLiteDatabase bbdd;
    private BBDDSQLiteHelper bbddhelper;
    private Context context;

    public BBDDCategorias(Context context) {
        this.context = context;
        bbddhelper = new BBDDSQLiteHelper(context, Constantes.BBDD_NOMBRE, null, Constantes.BBDD_VERSION);
    }

    public Categoria getCategoriaById(String idCategoria) {

        Categoria categoria = null;
        if (null != idCategoria) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idCategoria.trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_CATEGORIA_RECAMBIO_BY_ID, args);

            if (cursor.moveToFirst()) {
                categoria = new Categoria();
                mapearCategoria(cursor, categoria);
            }
            cursor.close();
            bbdd.close();
        }
        return categoria;
    }

    public Categoria getCategoriaPorNombreSubcategoria(String nombreSubcategoria) {

        Categoria categoria = null;
        Integer idCategoria = 0;
        if (null != nombreSubcategoria) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {nombreSubcategoria.trim()};
            Cursor cursorSubcategoria = bbdd.rawQuery(Constantes.SELECT_ID_CATEGORIA_POR_NOMBRE_SUBCATEGORIA, args);

            if (cursorSubcategoria.moveToFirst()) {
                idCategoria = cursorSubcategoria.getInt(0);
            }

            cursorSubcategoria.close();

            if (null != idCategoria) {
                String argCategoria[] = {idCategoria.toString().trim()};
                Cursor cursor = bbdd.rawQuery(Constantes.SELECT_CATEGORIA_RECAMBIO_BY_ID, argCategoria);

                if (cursor.moveToFirst()) {
                    categoria = new Categoria();
                    mapearCategoria(cursor, categoria);
                }
                cursor.close();
                bbdd.close();
            }
        }
        return categoria;

    }


    public String getNombreCategoriaPorIdSubcategoria(Integer idSubcategoria) {

        String nombreCategoria = "";
        if (null != idSubcategoria) {
            bbdd = bbddhelper.getReadableDatabase();
            String args[] = {idSubcategoria.toString().trim()};
            Cursor cursor = bbdd.rawQuery(Constantes.SELECT_NOMBRE_CATEGORIA_POR_ID_SUBCATEGORIA, args);

            if (cursor.moveToFirst()) {
                if(!Constantes.esVacio(cursor.getString(0))){
                    nombreCategoria = cursor.getString(0);
                }

            }

            cursor.close();
            bbdd.close();


        }
        return nombreCategoria;

    }





    public ArrayList<Categoria> getCategoriasOrdenAlfabetico() {

        ArrayList<Categoria> listaCategorias = null;

        bbdd = bbddhelper.getReadableDatabase();

        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_CATEGORIAS_POR_ORDEN_ALFABETICO, null);

        if (cursor.moveToFirst()) {
            listaCategorias = new ArrayList<Categoria>();
            do {
                Categoria categoria = new Categoria();
                mapearCategoria(cursor, categoria);
                listaCategorias.add(categoria);

            } while (cursor.moveToNext());
        }
        cursor.close();
        bbdd.close();

        return listaCategorias;
    }



    private void mapearCategoria(Cursor cursor, Categoria categoria) {

        Integer idCategoria = Integer.valueOf(cursor.getInt(0));
        categoria.setIdCategoria(idCategoria);

        if (!Constantes.esVacio(cursor.getString(1))) {
            categoria.setNombre(cursor.getString(1));
        } else {
            categoria.setNombre("");
        }

        if (!Constantes.esVacio(cursor.getString(2))) {
            categoria.setDescripcion(cursor.getString(2));
        } else {
            categoria.setDescripcion("");
        }
    }


    public void rellenarCategorias() {

        bbdd = bbddhelper.getWritableDatabase();

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

            bbdd.insert("CategoriasRecambio", null, valores);

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


    public void rellenarSubcategoria(String nombreCategoria, ArrayList<Subcategoria> listaSubcategorias) {

        bbdd = bbddhelper.getWritableDatabase();

        String[] arg = {nombreCategoria};
        Cursor cursor = bbdd.rawQuery(Constantes.SELECT_ID_CATEGORIA_BY_NOMBRE_CATEGORIA, arg);
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

                bbdd.insert("SubcategoriasRecambio", null, valores);

            }
        }

    }

    public void eliminarCategoria(Integer idCategoria) {
        bbdd = bbddhelper.getWritableDatabase();

        String arg[] = {idCategoria.toString()};
        try {
            bbdd.delete("CategoriasRecambio", "id_categoria=?", arg);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

        bbdd.close();
    }

    public void eliminarCategorias() {
        ArrayList<Categoria> categorias = getCategoriasOrdenAlfabetico();
        for (Categoria cat : categorias) {
            eliminarCategoria(cat.getIdCategoria());
        }
    }

}