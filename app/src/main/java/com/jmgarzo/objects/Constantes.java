package com.jmgarzo.objects;

import java.math.BigDecimal;

public class Constantes {

    //APP DROPBOX

    public static final String APP_KEY = "aep1aa93gs1iauo";
    public static final String APP_SECRET = "0y29g0sykinvy5f";

    //PUBLICIDAD
    public static final String ADUNITID = "ca-app-pub-9433862228892990/5816419666";

    // PROPIEDADES BBDD

    public static final String sqlPragmaON = "PRAGMA foreign_keys = ON ";

    public static final String BBDD_NOMBRE = "ratescar.sqlite";
    public static final int BBDD_VERSION = 14;
    public static final String DIRECTORIOAPP = "ratescar";

    //TIPOS DE IMAGENES

    public static final String TIPO_COCHE = "Coches";
    public static final String TIPO_ITV = "Itvs";

    // ***********************************************************************//
    // ********************** COCHES *****************************************//
    // ***********************************************************************//

    /**
     * id_coche ->INTEGER matricula ->TEXT id_marca ->INTEGER nombre ->TEXT
     * modelo ->TEXT id_combustible->INTEGER cc ->TEXT tamano_deposito->FLOAT
     * km_iniciales ->INTEGER km_actuales ->INTEGER comentarios ->TEXT
     * fecha_compra -> INTEGER, fecha_matriculacion -> INTEGER, fecha_fabricacion -> INTEGER
     */
    public static final String AlterTableCochesAnadirVersion = "ALTER TABLE Coches ADD version TEXT ";


    public static final String sqlCreateCoches = "CREATE TABLE Coches "
            + "(id_coche INTEGER PRIMARY KEY, matricula TEXT, "
            + "	id_marca INTEGER NOT NULL REFERENCES Marcas (id_marca), nombre TEXT, modelo TEXT,version TEXT, "
            + " id_combustible INTEGER NOT NULL REFERENCES Combustibles(id_combustible), "
            + " cc TEXT, tamano_deposito FLOAT, km_iniciales FLOAT, km_actuales FLOAT, comentarios TEXT, "
            + " fecha_compra INTEGER, fecha_matriculacion INTEGER, fecha_fabricacion INTEGER)";




    private static final String selectTodoCoches = "SELECT id_coche,matricula,id_marca,nombre,modelo,version,id_combustible,"
            + " cc,tamano_deposito, km_iniciales, km_actuales, comentarios, fecha_compra, fecha_matriculacion, fecha_fabricacion "
            + " FROM Coches ";

    public static final String selectCochePorId = "SELECT id_coche,matricula,id_marca,nombre,modelo,version,id_combustible,cc,tamano_deposito,"
            + " km_iniciales,km_actuales,comentarios,fecha_compra,fecha_matriculacion, fecha_fabricacion " +
            " FROM Coches WHERE id_coche = ? ";

    public static final String SELECT_TODOS_LOS_IDS_COCHE = "SELECT id_coche FROM Coches ";


    public static String getSelectTodoCoches() {
        return selectTodoCoches;
    }

    public static final String sqlDropTableCoches = "DROP TABLE Coches";

    public static final String getCuantosCoches = "SELECT COUNT(*) FROM Coches";
    //Cuantos coches que al menos tengan un repostaje y que el consumo_medio sea distito de 0(evita que se muestr
    public static final String getCuantosCocheConRespostaje = "SELECT COUNT(DISTINCT Re.id_coche) FROM Coches Co " +
            "INNER join Repostajes  Re ON CO.id_coche = RE.id_coche " +
            " WHERE RE.media_consumo<>0 ";


    public static final String getTodosLosCochesOrdenadosId = "SELECT * FROM Coches ORDER BY id_coche ";
    //    public static final String getTodosLosCochesConRepostajesOrdenadosID = "SELECT distinct CO.id_coche ,CO.matricula,CO.id_marca,CO.nombre,CO.modelo,CO.id_combustible, " +
//            " CO.cc,CO.tamano_deposito, CO.km_iniciales, CO.km_actuales, CO.comentarios, CO.fecha_compra, CO.fecha_matriculacion, CO.fecha_fabricacion FROM Coches CO " +
//            " INNER JOIN Repostajes RE ON CO.id_coche = RE.id_coche  ORDER BY CO.id_coche ";
    //Devuelve todos los coches que tienen al menos un repostaje, y este tiene un consume_medio distinto de 0, evita que aparecezcan coches en el spinner que no tienen repostajes, o los que tienen, tienen un consumo_medio=0
    public static final String getTodosLosCochesConRepostajesOrdenadosID = "SELECT distinct CO.id_coche ,CO.matricula,CO.id_marca,CO.nombre,CO.modelo,CO.version,CO.id_combustible, " +
            " CO.cc,CO.tamano_deposito, CO.km_iniciales, CO.km_actuales, CO.comentarios, CO.fecha_compra, CO.fecha_matriculacion, CO.fecha_fabricacion FROM Coches CO " +
            " INNER JOIN Repostajes RE ON CO.id_coche = RE.id_coche WHERE RE.media_consumo <> 0 ORDER BY CO.id_coche ASC ";


    public static final String getUltimoIdCoche = "SELECT id_coche FROM Coches ORDER BY id_coche DESC LIMIT 1 ";


    // ***********************************************************************//
    // ******************** MARCAS ***********************************//
    // ***********************************************************************//


    public static final String sqlCreateMarcas = "CREATE TABLE Marcas(id_marca INTEGER PRIMARY KEY, nombre TEXT, icono INTEGER)";

    private static final String selectTodasLasMarcas = "SELECT id_marca, nombre, icono FROM Marcas ";

    public static final String getSelectTodasLasMarcas() {
        return selectTodasLasMarcas;
    }

    public static final String SELECT_TODAS_LAS_MARCAS_ORDER_BY_NOMBRE = "SELECT id_marca, nombre, icono FROM Marcas ORDER BY nombre ";


    public static final String sqlDropTableMarcas = "DROP TABLE Marcas";

    // ***********************************************************************//
    // *************** COMBUSTIBLES ************************************//
    // ***********************************************************************//

    public static final String sqlCreateCombustibles = "CREATE TABLE Combustibles (id_combustible INTEGER PRIMARY KEY, tipo TEXT, subtipo TEXT)";

    public static final String selectTodosLosCombustibles = "SELECT id_combustible,tipo,subtipo FROM Combustibles ";
    public static final String selectCombustiblesById = "SELECT id_combustible,tipo,subtipo FROM Combustibles where id_combustible = ? ";
    public static final String selectDistinctCombustibles = "SELECT DISTINCT tipo FROM Combustibles ";
    public static final String selectOnlyTiposCombustibles = "SELECT id_combustible,tipo,subtipo FROM Combustibles where subtipo = ''";
    public static final String selectCombustiblesbyTipo = "SELECT id_combustible,tipo,subtipo FROM Combustibles where subtipo = '' ";
    public static final String selectCombustiblesByIdTipoGeneral = "SELECT id_combustible,tipo,subtipo FROM Combustibles WHERE tipo = "
            + " (SELECT tipo FROM COMBUSTIBLES where id_combustible = ? ) and subtipo<>''";

    public static final String sqlDropTableCombustibles = "DROP TABLE Combustibles";


    public static boolean esVacio(String valor) {
        if (valor == null || valor.isEmpty() || valor == "") {
            return true;
        } else {
            return false;
        }
    }

    public static boolean esVacio(BigDecimal valor) {
        if (valor == null) {
            return true;
        } else {
            return false;
        }
    }

    // ***********************************************************************//
    // ******************** REPOSTAJES ***********************************//
    // ***********************************************************************//

    /**
     * id_repostaje ->INTEGER id_coche ->INTEGER id_combustible ->INTEGER
     * km_repostaje ->INTEGER litros ->FLOAT precio_litro ->FLOAT coste
     * repostaje->FLOAT es_completo ->BOOLEAN es_aa ->BOOLEAN es_remolque
     * ->BOOLEAN es_baca -> BOOLEAN tipo_carretera ->TEXT tipo_pago ->TEXT
     * velocidad_media ->FLOAT area_servicio ->TEXT fecha_repostaje ->DATE
     * tipo_conduccion ->TEXT comentarios ->TEXT km_recorridos->FLOAT,
     * media_consumo->FLOAT
     */

    public static final String sqlCreateRepostajes = "CREATE TABLE Repostajes (id_repostaje INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE,"
            + " id_combustible INTEGER NOT NULL REFERENCES Combustibles(id_combustible),"
            + " km_repostaje FLOAT, litros FLOAT, precio_litro FLOAT, coste_repostaje FLOAT, "
            + " es_completo BOOLEAN, es_aa BOOLEAN, es_remolque BOOLEAN,es_baca BOOLEAN, tipo_carretera TEXT,"
            + " tipo_pago TEXT, velocidad_media FLOAT, area_servicio TEXT, fecha_repostaje INTEGER, "
            + " tipo_conduccion TEXT, comentarios TEXT, km_recorridos FLOAT, media_consumo FLOAT) ";
    // public static final String sqlAlterTableRepostajes01
    // ="ALTER TABLE Repostajes ADD fecha_real REAL ";

    public static final String sqlDropTableRespostajes = "DROP TABLE Repostajes";

    public static final String SelectTodosLosRepostajes = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes  ";
    public static final String SelectTodosLosRepostajesPorFechaDesc = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes ORDER BY fecha_repostaje  DESC ";

    public static final String SelectAnteriorRepostaje = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes  WHERE id_coche = ?  and  km_repostaje < ? ORDER BY km_repostaje desc";

    public static final String SelectRepostajeById = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes " + "WHERE id_repostaje = ?";
    public static final String SelectRepostajesPorCocheOrdenadosPorKmDesc = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje, "
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Respostajes WHERE id_coche = ? ORDER BY km_repostaje ASC ";

    public static final String SelectKmAnteriorRepostaje = "SELECT km_repostaje FROM Repostajes WHERE id_coche = ? ORDER BY km_repostaje desc ";

    public static final String SelectRepostajesPorCocheOrdenadosKmASC = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes WHERE id_coche = ? ORDER BY km_repostaje ASC ";
    public static final String SelectRepostajesPorCocheOrdenadosKmDesc = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes WHERE id_coche = ? ORDER BY km_repostaje DESC ";

    public static final String SelectUltimoCocheQueReposto = "SELECT id_coche"
            + " FROM Repostajes ORDER BY fecha_repostaje DESC ";

    public static final String SelectFechaPrimerRepostaje = "SELECT fecha_repostaje FROM Repostajes ORDER BY  fecha_repostaje ASC LIMIT 1";
    public static final String SelectFechaPrimerRepostajePorCoche = "SELECT fecha_repostaje FROM Repostajes WHERE id_coche = ? ORDER BY  fecha_repostaje ASC LIMIT 1";
    public static final String SelectFechaUltimoRepostaje = "SELECT fecha_repostaje FROM Repostajes ORDER BY  fecha_repostaje DESC LIMIT 1";
    public static final String SelectFechaUltimoRepostajePorCoche = " SELECT fecha_repostaje FROM Repostajes WHERE id_coche = ? ORDER BY  fecha_repostaje DESC LIMIT 1 ";
    //Devuelve los
    public static final String SelectConsumosMediosPorCocheOrdenadoFecha = " SELECT media_consumo FROM Repostajes WHERE id_coche = ? ORDER BY fecha_repostaje ASC ";
    public static final String SelectConsumosMediosPorCocheYFechas = " SELECT media_consumo FROM Repostajes WHERE id_coche = ? and fecha_repostaje BETWEEN ? AND ? and media_consumo <> 0 ORDER BY fecha_repostaje ASC ";
    public static final String SelectRepostajesPorCocheYFechas = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes WHERE id_coche = ? and fecha_repostaje BETWEEN ? AND ? and media_consumo <> 0 ORDER BY fecha_repostaje ASC ";
    public static final String SelectTodosLosRepostajesOrdenadoPorCoche = "SELECT id_repostaje,id_coche,id_combustible,km_repostaje,litros, precio_litro, coste_repostaje,"
            + " es_completo, es_aa, es_remolque, es_baca, tipo_carretera, tipo_pago, velocidad_media, area_servicio, fecha_repostaje, tipo_conduccion, comentarios, km_recorridos, media_consumo "
            + " FROM Repostajes ORDER BY id_coche ";

    public static final String SelectKmInicialesRepostajePorCoche = "SELECT km_repostaje FROM Repostajes WHERE id_coche = ? ORDER BY km_repostaje ASC";

    public static final String SelectKmFinalesRepostajePorCoche = "SELECT km_repostaje FROM Repostajes WHERE id_coche = ? AND km_repostaje <> 0 ORDER BY km_repostaje DESC";

    /**
     * Nos devuelve el número de Repostajes para un coche y unas fechas dadas. El repostaje tiene que tener un consumo medio <>0
     * esto es para contar solo los repostajes completos y no los parciales.
     */
    public static final String SelectNumeroRepostajesPorCocheYFecha = "SELECT COUNT(*) FROM Repostajes WHERE id_coche = ? and fecha_repostaje BETWEEN ? AND ? AND media_consumo <>0";

    public static final String SelectNumeroRepostajesCompletosPorIdCoche = "SELECT COUNT(*) FROM Repostajes WHERE id_coche = ? AND es_completo = 1";
    public static final String SelectNumeroRepostajesCompletosAnterioresPorIdCoche = "SELECT COUNT(*) FROM Repostajes WHERE id_coche = ? AND km_repostaje < ? AND es_completo = 1";

    public static final String selectEstacionesServicio = "SELECT DISTINCT area_servicio FROM Repostajes WHERE area_servicio<>''";

    // ***********************************************************************//
    // ******************** RECAMBIOS *******************************//
    // ***********************************************************************//

    // Recambios(id_recambio, referencia, marca,
    // nombre,características,comentarios)

//    public static final String sqlCreateRecambios = "CREATE TABLE Recambios "
//            + " ( id_recambio INTEGER PRIMARY KEY, referencia TEXT, marca TEXT, nombre TEXT, caracteristicas TEXT, comentarios TEXT) ";
//    public static final String selectTodosLosRecambios = "SELECT id_recambio, referencia, marca, nombre, caracteristicas, comentarios FROM Recambios";
//    public static final String selectRecambioById = "SELECT id_recambio, referencia, marca, nombre, caracteristicas, comentarios FROM Recambios WHERE id_recambio=? ";

    // ***********************************************************************//
    // ******************** MANO DE OBRA *******************************//
    // ***********************************************************************//

    // id_mano_obra,nombre_mano_obra, horas,
    // descripcion,comentarios,coste_total)

    public static final String sqlCreateManoDeObra = " CREATE TABLE Manos_de_obras "
            + " (id_mano_de_obra INTEGER PRIMARY KEY, nombre TEXT, descripcion TEXT, comentarios TEXT)  ";
    public static final String selectManoObrabyId = " SELECT id_mano_de_obra, nombre, descripcion, comentarios FROM Manos_de_obras WHERE id_mano_de_obra=? ";
    public static final String selectTodasLasManoObra = " SELECT id_mano_de_obra, nombre, descripcion, comentarios FROM Manos_de_obras ";

    // ***********************************************************************//
    // ******************** MANTENIMIENTOS *******************************//
    // ***********************************************************************//

    // id_mantenimiento, id_coche,tipo_mantenimiento, nombre_mantenimiento,
    // fecha_mantenimiento,km_mantenimiento, coste_iva, coste_sin_iva,
    // tanto_por_cien_iva, descuento, coste_final, comentarios

//    public static final String sqlCreateMantenimientos = "CREATE TABLE Mantenimientos "
//            + " (id_mantenimiento INTEGER PRIMARY KEY, id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
//            + " tipo_mantenimiento TEXT, nombre_mantenimiento TEXT, fecha_mantenimiento INTEGER, "
//            + " km_mantenimiento FLOAT, coste_iva FLOAT, coste_sin_iva FLOAT, tanto_por_cien_iva FLOAT, "
//            + " descuento_total FLOAT, coste_final FLOAT, comentarios TEXT) ";
//
//    public static final String selectTodosLosMantenimientos = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
//            " nombre_mantenimiento, fecha_mantenimiento, km_mantenimiento, coste_iva, coste_sin_iva, tanto_por_cien_iva," +
//            "  descuento_total, coste_final, comentarios FROM Mantenimientos ";
//
//    public static final String selectMantenimientosPorCocheOrdenadosPorKmDesc = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
//            " nombre_mantenimiento, fecha_mantenimiento, km_mantenimiento, coste_iva, coste_sin_iva, tanto_por_cien_iva," +
//            "  descuento_total, coste_final, comentarios " +
//            " FROM Mantenimientos WHERE id_coche = ? ORDER BY km_mantenimiento DESC   ";
//
//    public static final String selectMantenimientosOrdenadorPorKmDesc = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
//            " nombre_mantenimiento, fecha_mantenimiento, km_mantenimiento, coste_iva, coste_sin_iva, tanto_por_cien_iva," +
//            "  descuento_total, coste_final, comentarios " +
//            " FROM Mantenimientos  ORDER BY km_mantenimiento DESC   ";
//
//    public static final String selectMantenimientosOrdenadorPorFechaDesc = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
//            " nombre_mantenimiento, fecha_mantenimiento, km_mantenimiento, coste_iva, coste_sin_iva, tanto_por_cien_iva," +
//            "  descuento_total, coste_final, comentarios " +
//            " FROM Mantenimientos  ORDER BY fecha_mantenimiento DESC   ";
//
//    public static final String selectMantenimientoById = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
//            " nombre_mantenimiento, fecha_mantenimiento, km_mantenimiento, coste_iva, coste_sin_iva, tanto_por_cien_iva," +
//            "  descuento_total, coste_final, comentarios FROM Mantenimientos WHERE id_mantenimiento =? ";
//
//    public static final String selectUltimoMantenimientoPorCoche = "SELECT km_mantenimiento FROM Mantenimientos WHERE id_coche =? ORDER BY km_mantenimiento desc ";
//
//    public static final String SelectKmInicialesMantenimientoPorCoche = "SELECT km_mantenimiento FROM Mantenimientos WHERE id_coche = ? ORDER BY km_mantenimiento ASC";
//    public static final String SelectKmFinalesMantenimientoPorCoche = "SELECT km_mantenimiento FROM Mantenimientos WHERE id_coche = ? AND km_mantenimiento <> 0  ORDER BY km_mantenimiento DESC";
//
//    public static final String selectNombreMantenimientos = "SELECT DISTINCT nombre_mantenimiento FROM Mantenimientos WHERE nombre_mantenimiento<>'' ";
//
//    public static final String selectTiposMantenimientos = "SELECT DISTINCT tipo_mantenimiento FROM Mantenimientos WHERE tipo_mantenimiento<>'' ";
//

    // ***********************************************************************//
    // ***************** MANTENIMIENTOS-RECAMBIOS **************************//
    // ***********************************************************************//

    // (id_mantenimiento,id_recambio,coste_total, descuento,
    // tanto_por_cien_descuento, precio_iva,
    // precio_sin_iva,tanto_por_cien_iva,comentarios,fecha)

//    public static final String sqlCreateMantenimientosRecambios = " CREATE TABLE Mantenimientos_Recambios "
//            + " ( id_mantenimiento INTEGER NOT NULL, id_recambio INTEGER NOT NULL, "
//            + " cantidad REAL, coste_total FLOAT, descuento FLOAT, tanto_por_cien_descuento FLOAT, precio_unitario_iva FLOAT, precio_unitario_sin_iva FLOAT, "
//            + " tanto_por_cien_iva FLOAT, comentarios STRING, fecha INTEGER,km_proximo_matenimiento INTEGER, periodicidad_km INTEGER, "
//            + " periodicidad_meses dias INTEGER, "
//            + " FOREIGN KEY(id_mantenimiento) REFERENCES Matenimientos(id_mantenimiento), "
//            + " FOREIGN KEY(id_recambio) REFERENCES Recambios(id_recambio),PRIMARY KEY (id_mantenimiento, id_recambio)) ";

    // public static final String sqlCreateMantenimientosRecambios =
    // " CREATE TABLE Mantenimiento_Recambios " +
    // " ( id_matenimiento, id_recambio INTEGER PRIMARY KEY NOT NULL, " +
    // " coste_total FLOAT, descuento FLOAT, tanto_por_cien_descuento FLOAT, precio_iva FLOAT, precio_sin_iva FLOAT, "
    // +
    // " tanto_por_cien_iva FLOAT, comentarios STRING, fecha INTEGER," +
    // " FOREIGN KEY(id_matenimiento) REFERENCES Matenimientos(id_matenimiento), "
    // +
    // " FOREIGN KEY(id_recambio) REFERENCES Recambios(id_recambio)) ";


    // ***********************************************************************//
    // ***************** MANTENIMIENTOS-RECAMBIOS **************************//
    // ***********************************************************************//

    public static final String sqlCreateMantenimientosManoObra = "CREATE TABLE Mantenimientos_ManoObra" +
            " (id_mantenimiento INTEGER NOT NULL, id_mano_de_obra INTEGER NOT NULL, horas REAL, precio_hora_iva REAL, " +
            " precio_hora_sin_iva REAL, tanto_por_cien_iva REAL, descuento REAL, tanto_cien_descuento REAL, coste_total REAL, fecha INTEGER ";


    // ***********************************************************************//
    // ***************** PEAJES***********************************************//
    // ***********************************************************************//
    public static final String sqlCreatePeajes = "CREATE TABLE Peajes (id_peaje INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " nombre TEXT, fecha INTEGER, precio FLOAT, carretera TEXT, direccion TEXT, ubicacion TEXT, comentarios TEXT) ";


    public static final String selectTodosLosPeajes = "SELECT id_peaje, id_coche, nombre, fecha, precio, carretera, direccion, " +
            " ubicacion, comentarios FROM Peajes ";

    public static final String selectTodasLosPeajesOrdenadosPorFechaDesc = "SELECT id_peaje, id_coche, nombre, fecha, precio, carretera, direccion, " +
            " ubicacion, comentarios FROM Peajes ORDER BY fecha desc";

    public static final String selectPeajeById = "SELECT id_coche, nombre, fecha, precio, carretera, direccion,  " +
            " ubicacion, comentarios FROM Peajes WHERE id_peaje = ?";

    public static final String selectNombrePeajes = "SELECT DISTINCT nombre FROM Peajes WHERE nombre<>'' ";

    public static final String selectPreciosPeajes = "SELECT DISTINCT precio FROM Peajes WHERE precio<>'' ";

    public static final String selectCarreterasPeajes = "SELECT DISTINCT carretera FROM Peajes WHERE carretera <>'' ";

    public static final String selectDireccionesPeajes = "SELECT DISTINCT direccion FROM Peajes WHERE direccion<>''";

    public static final String selectUbicacionesPeajes = "SELECT DISTINCT ubicacion FROM Peajes WHERE ubicacion<>''";


    // ***********************************************************************//
    // *****************  ITV  ***********************************************//
    // ***********************************************************************//

    public static final String sqlCreateItvs = "CREATE TABLE Itvs (id_itv INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " fecha_itv INTEGER, precio FLOAT, estacion TEXT, resultado TEXT, observaciones TEXT, km_itv FLOAT) ";

    public static final String selectTodasLasItvs = "SELECT id_itv, id_coche, fecha_itv, precio, estacion, resultado, " +
            " observaciones, km_itv  FROM Itvs ";

    public static final String selectTodasLasItvsOrdenadasPorFechaDesc = "SELECT id_itv, id_coche, fecha_itv, precio, estacion, resultado, " +
            " observaciones, km_itv  FROM Itvs ORDER BY fecha_itv desc";

    public static final String selectItvsPorCocheOrdenadasPorFechaDesc = "SELECT id_itv, id_coche, fecha_itv, precio, estacion, resultado, " +
            " observaciones, km_itv  FROM Itvs WHERE id_coche = ? ORDER BY fecha_itv desc";

    public static final String selectItvById = "SELECT id_itv, id_coche, fecha_itv, precio, estacion, resultado, " +
            " observaciones, km_itv  FROM Itvs WHERE id_itv = ?";

    public static final String selectNombresEstacion = "SELECT DISTINCT estacion FROM Itvs WHERE estacion <>'' ";

    public static final String selectResultadosItv = "SELECT DISTINCT resultado FROM Itvs WHERE resultado<>''";


    // ***********************************************************************//
    // *****************  SEGUROS  *******************************************//
    // ***********************************************************************//

    public static final String sqlCreateSeguros = "CREATE TABLE Seguros (id_seguro INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " compania TEXT, prima FLOAT, tipo_de_seguro TEXT, numero_poliza TEXT, fecha_inicio INTEGER, fecha_vencimiento INTEGER,  periodicidad_digito INTEGER, periodicidad_unidad TEXT, observaciones TEXT) ";

    public static final String selectTodosLosSeguros = " SELECT id_seguro, id_coche, compania, prima, tipo_de_seguro, " +
            " numero_poliza, fecha_inicio, fecha_vencimiento,  periodicidad_digito, periodicidad_unidad, observaciones FROM Seguros ";

    public static final String selectTodosLosSegurosPorFecha = " SELECT id_seguro, id_coche, compania, prima, tipo_de_seguro, " +
            " numero_poliza, fecha_inicio, fecha_vencimiento,  periodicidad_digito, periodicidad_unidad, observaciones FROM Seguros ORDER BY fecha_inicio desc ";

    public static final String selectSeguroById = "SELECT id_seguro,id_coche, compania, prima, tipo_de_seguro, " +
            " numero_poliza, fecha_inicio, fecha_vencimiento,  periodicidad_digito, periodicidad_unidad, observaciones FROM Seguros WHERE id_seguro = ? ";

    public static final String selectSegurosPorCocheOrdenadoPorFechaDesc = "SELECT id_seguro,id_coche, compania, prima, tipo_de_seguro, " +
            " numero_poliza, fecha_inicio, fecha_vencimiento,  periodicidad_digito, periodicidad_unidad, observaciones FROM Seguros WHERE id_coche = ? " +
            "ORDER BY  fecha_inicio DESC";

    public static final String selectPolizas = " SELECT DISTINCT numero_poliza FROM Seguros WHERE numero_poliza<>''  ";

    public static final String selectCompanias = " SELECT DISTINCT compania FROM Seguros WHERE compania<>''  ";

    //********************************IMPUESTO CIRCULACION**************************//
    // ***********************************************************************//
    // ***********************************************************************//


    public static final String sqlCreateImpuestoCirculacion = "CREATE TABLE Impuestos_Circulacion (id_impuesto INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " importe FLOAT,  anualidad INTEGER, fecha_fin_pago INTEGER, comentarios TEXT) ";


    public static final String selectImpuestoCirculacionById = "SELECT id_impuesto, id_coche, importe, anualidad, fecha_fin_pago, " +
            " comentarios FROM Impuestos_Circulacion WHERE id_impuesto = ? ";

    public static final String selectTodosLosImpuestosCirculacionPorFechaDesc = " SELECT id_impuesto, id_coche, importe, anualidad, fecha_fin_pago, " +
            " comentarios FROM Impuestos_Circulacion ORDER BY  fecha_fin_pago DESC ";

    public static final String selectImpuestoCirculacionPorCocheOrdenadasPorFechaDesc = "SELECT id_impuesto, id_coche, importe, anualidad, fecha_fin_pago, " +
            " comentarios FROM Impuestos_Circulacion WHERE id_coche = ? ORDER BY fecha_fin_pago DESC";


    //**************************IMAGENES************************************//
    // ***********************************************************************//
    // ***********************************************************************//


    public static final String sqlCreateImagenes = "CREATE TABLE Imagenes (id_imagen INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " ruta TEXT, ruta_thumb TEXT , tipo TEXT ) ";


    public static final String sqlDropTableImagenes = "DROP TABLE Imagenes";

    public static final String SELECT_IMAGENES_POR_COCHE_Y_TIPO_DESC = "SELECT id_imagen, id_coche, ruta, ruta_thumb, tipo  " +
            " FROM Imagenes WHERE id_coche = ? AND tipo = ? ORDER BY ruta DESC ";

    public static final String SELECT_IMAGENES_POR_TIPO_ORDER_IDCOCHE_DESC = "SELECT id_imagen, id_coche, ruta, ruta_thumb, tipo " +
            " FROM Imagenes WHERE tipo = ? ORDER BY id_coche DESC ";

    public static final String SELECT_ID_IMAGEN_POR_RUTA_y_TIPO = "SELECT id_imagen FROM Imagenes WHERE ruta = ? AND TIPO = ?";

    public static final String DELETE_TODAS_LAS_IMAGENES_MENOS = "DELETE FROM Imagenes WHERE id_coche = ? and id_imagen<> ?";

    public static final String SELECT_RUTA_POR_IDCOCHE_Y_TIPO = "SELECT ruta  " +
            " FROM Imagenes WHERE id_coche = ? AND tipo = ? ORDER BY id_imagen DESC ";

    public static final String SELECT_RUTA_THUMB_POR_IDCOCHE_Y_TIPO = "SELECT ruta_thumb  " +
            " FROM Imagenes WHERE id_coche = ? AND tipo = ? ORDER BY id_imagen DESC ";


    //**************************MANTENIMIENTOS************************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_MANTENIMIENTOS = "CREATE TABLE Mantenimientos (id_mantenimiento INTEGER PRIMARY KEY, "
            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche) ON DELETE CASCADE, "
            + " tipo_mantenimiento TEXT, fecha INTEGER, km FLOAT, coste FLOAT, comentarios TEXT ) ";

    public static final String SELECT_MANTENIMIENTOS_ORDENADOS_POR_FECHA = "SELECT id_mantenimiento,id_coche,tipo_mantenimiento,fecha,km,coste,comentarios " +
            " FROM Mantenimientos ORDER BY fecha DESC ";

    public static final String SELECT_MANTENIMIENTOS_ORDENADOS_POR_KM_DESC = "SELECT id_mantenimiento,id_coche,tipo_mantenimiento,fecha,km,coste,comentarios " +
            " FROM Mantenimientos ORDER BY km DESC ";

    public static final String SELECT_MANTENIMIENTO_POR_ID = "SELECT id_mantenimiento,id_coche,tipo_mantenimiento,fecha,km,coste,comentarios " +
            " FROM Mantenimientos WHERE id_mantenimiento = ? ORDER BY fecha DESC ";

    public static final String SELECT_MANTENIMIENTOS_POR_COCHE_ORDENADOS_POR_KM_DESC = "SELECT id_mantenimiento, id_coche, tipo_mantenimiento, " +
            " fecha, km, coste, comentarios " +
            " FROM Mantenimientos WHERE id_coche = ? ORDER BY km DESC   ";

    public static final String SELECT_MANTENIMIENTO_KM_INICIALES_POR_COCHE = "SELECT km FROM Mantenimientos WHERE id_coche = ? ORDER BY km ASC";

    public static final String SELECT_MANTENIMIENTO_KM_FINALES_POR_COCHE = "SELECT km FROM Mantenimientos WHERE id_coche = ? AND km <> 0  ORDER BY km DESC";

    public static final String SELECT_ULTIMO_MANTENIMIENTO_POR_COCHE = "SELECT km FROM Mantenimientos WHERE id_coche =? ORDER BY km desc ";

    public static final String SELECT_ULTIMO_MANTENIMIENTO = "SELECT id_mantenimiento,id_coche,tipo_mantenimiento,fecha,km,coste,comentarios " +
            " FROM Mantenimientos ORDER BY id_mantenimiento DESC LIMIT 1 ";


    //**************************OPERACIONES************************************//
    // ***********************************************************************//
    // ***********************************************************************//
    public static final String CREATE_OPERACIONES = "CREATE TABLE Operaciones (id_operacion INTEGER PRIMARY KEY, "
            + " nombre TEXT, descripcion TEXT ) ";

    public static final String SELECT_OPERACIONES_POR_ORDEN_ALFABETICO = "SELECT id_operacion,nombre,descripcion  " +
            " FROM Operaciones ORDER BY nombre ASC ";

    public static final String SELECT_OPERACION_BY_ID = "SELECT id_operacion, nombre, descripcion FROM Operaciones WHERE id_operacion = ?";


    //**************************MANTENIMIENTO OPERACION************************************//
    // ***********************************************************************//
    // ***********************************************************************//
    public static final String CREATE_MANTENIMIENTO_OPERACION = "CREATE TABLE MantenimientoOperacion" +
            "( id_mantenimiento INTEGER, id_operacion INTEGER, periodicidad_digito INTEGER, periodicidad_unidad TEXT, coste FLOAT," +
            " comentarios TEXT, " +
            "PRIMARY KEY(id_mantenimiento,id_operacion), " +
            "FOREIGN KEY(id_mantenimiento) REFERENCES Mantenimientos(id_mantenimiento) ON DELETE CASCADE, " +
                "FOREIGN KEY(id_operacion) REFERENCES Operaciones(id_operacion) ON DELETE CASCADE)";


    public static final String SELECT_MANTENIMIENTO_OPERACION_BY_ID = "SELECT id_mantenimiento, id_operacion, periodicidad_digito, periodicidad_unidad,coste, comentarios " +
            " FROM MantenimientoOperacion " +
            " WHERE id_mantenimiento = ? AND id_operacion = ? ";

    public static final String SELECT_MANTENIMIENTO_OPERACIONES = "SELECT id_mantenimiento, id_operacion, periodicidad_digito, periodicidad_unidad,coste,comentarios " +
            " FROM MantenimientoOperacion ";

    public static final String SELECT_MANTENIMIENTO_OPERACIONES_BY_MANTENIMIENTO = "SELECT id_mantenimiento, id_operacion, periodicidad_digito, periodicidad_unidad,coste,comentarios " +
            " FROM MantenimientoOperacion WHERE id_mantenimiento = ?";



    //**************************CATEGORIAS_RECAMBIOS****************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_CATEGORIAS_RECAMBIO = "CREATE TABLE CategoriasRecambio (id_categoria INTEGER PRIMARY KEY, "
            + " nombre TEXT, descripcion TEXT ) ";

    public static final String SELECT_CATEGORIA_RECAMBIO_BY_ID = " SELECT id_categoria,nombre,descripcion " +
            " FROM CategoriasRecambio WHERE id_categoria = ?";

    public static final String SELECT_ID_CATEGORIA_BY_NOMBRE_CATEGORIA = " SELECT id_categoria " +
            " FROM CategoriasRecambio WHERE nombre = ?";

    public static final String SELECT_CATEGORIAS_POR_ORDEN_ALFABETICO = " SELECT id_categoria,nombre,descripcion " +
            " FROM CategoriasRecambio ORDER BY nombre ASC";

    public static final String SELECT_NOMBRE_CATEGORIAS_POR_ORDEN_ALFABETICO = " SELECT nombre" +
            " FROM CategoriasRecambio ORDER BY nombre ASC";

    public static final String SELECT_ID_CATEGORIA_POR_NOMBRE_CATEGORIA = " SELECT id_categoria" +
            " FROM CategoriasRecambio WHERE nombre = ? ";

    public static final String SELECT_NOMBRE_CATEGORIA_POR_ID_SUBCATEGORIA = " SELECT CAT.nombre " +
            " FROM CategoriasRecambio CAT INNER JOIN SubcategoriasRecambio SUB " +
            " ON SUB.id_categoria = CAT.id_categoria " +
            " WHERE SUB.id_subcategoria = ? LIMIT 1 ";



    //**************************SUBCATEGORIAS_RECAMBIOS****************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_SUBCATEGORIAS_RECAMBIO = "CREATE TABLE SubcategoriasRecambio (id_subcategoria INTEGER PRIMARY KEY," +
            " id_categoria INTEGER NOT NULL REFERENCES CategoriasRecambio (id_categoria) ON DELETE CASCADE, "
            + " nombre TEXT, descripcion TEXT ) ";

    public static final String SELECT_SUBCATEGORIA_RECAMBIO_BY_ID = " SELECT id_subcategoria,id_categoria,nombre,descripcion " +
            " FROM SubcategoriasRecambio WHERE id_subcategoria = ?";

    public static final String SELECT_ID_SUBCATEGORIA_BY_NOMBRE = " SELECT id_subcategoria " +
            " FROM SubcategoriasRecambio WHERE nombre = ? LIMIT 1";

    public static final String SELECT_SUBCATEGORIAS_POR_ID_CATEGORIA = " SELECT id_subcategoria, id_categoria,nombre,descripcion " +
            " FROM SubcategoriasRecambio WHERE id_categoria = ? ORDER BY nombre ASC";
    public static final String SELECT_ID_CATEGORIA_POR_NOMBRE_SUBCATEGORIA = " SELECT id_categoria " +
            " FROM SubcategoriasRecambio WHERE nombre = ? LIMIT 1 ";

    public static final String SELECT_NOMBRE_POR_ID = " SELECT nombre " +
            " FROM SubcategoriasRecambio WHERE id_subcategoria = ? LIMIT 1 ";







    //**************************RECAMBIOS************************************//
    // ***********************************************************************//
    // ***********************************************************************//
//    public static final String CREATE_RECAMBIOS = "CREATE TABLE Recambios (id_recambio INTEGER PRIMARY KEY, "
//            +" id_subcategoria INTEGER NOT NULL REFERENCES SubcategoriasRecambio (id_subcategoria), " +
//             " nombre TEXT, fabricante TEXT, referencia TEXT, descripcion TEXT, comentarios TEXT  ) ";

    public static final String CREATE_RECAMBIOS = "CREATE TABLE Recambios (id_recambio INTEGER PRIMARY KEY, "
            +" id_subcategoria INTEGER NOT NULL, " +
            " nombre TEXT, fabricante TEXT, referencia TEXT, descripcion TEXT, comentarios TEXT  ) ";




    public static final String SELECT_RECAMBIOS_POR_ORDEN_ALFABETICO = "SELECT id_recambio, id_subcategoria, nombre, fabricante, referencia, descripcion, comentarios " +
            " FROM Recambios ORDER BY nombre ASC ";

    public static final String SELECT_RECAMBIO_BY_ID = "SELECT id_recambio,id_subcategoria,nombre,fabricante,referencia,descripcion,comentarios" +
            " FROM Recambios WHERE id_recambio = ?";







    //**************************OPERACION RECAMBIO***********************************//
    // ***********************************************************************//
    // ***********************************************************************//


    public static final String CREATE_OPERACION_RECAMBIO = " CREATE TABLE OperacionRecambio " +
            "( id_operacion INTEGER, id_recambio INTEGER, cantidad FLOAT, precio_unidad FLOAT, coste FLOAT,  " +
            "PRIMARY KEY(id_operacion,id_recambio)  " +
            "FOREIGN KEY(id_operacion) REFERENCES Operaciones(id_operacion) ON DELETE CASCADE ON UPDATE CASCADE )";

//    public static final String CREATE_OPERACION_RECAMBIO = " CREATE TABLE OperacionRecambio " +
//            "( id_operacion INTEGER, id_recambio INTEGER, cantidad FLOAT, precio_unidad FLOAT, coste FLOAT,  " +
//            "PRIMARY KEY(id_operacion,id_recambio) " +
//            " )";



    public static final String SELECT_OPERACION_RECAMBIO_BY_ID = "SELECT id_operacion, id_recambio, cantidad, precio_unidad,coste " +
            " FROM OperacionRecambio " +
            " WHERE id_operacion = ? AND id_recambio = ? ";

    public static final String SELECT_OPERACIONES_RECAMBIOS = "SELECT id_operacion, id_recambio, cantidad, precio_unidad,coste " +
            " FROM OperacionRecambio ";

    public static final String SELECT_OPERACIONES_RECAMBIOS_BY_OPERACION = "SELECT id_operacion, id_recambio, cantidad, precio_unidad,coste " +
            " FROM OperacionRecambio WHERE id_operacion = ?";

    public static final String SELECT_OPERACIONES_RECAMBIOS_BY_RECAMBIO = "SELECT id_operacion, id_recambio, cantidad, precio_unidad,coste " +
            " FROM OperacionRecambio WHERE id_recambio = ?";


    //**************************MONEDAS    ***********************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_MONEDAS = "CREATE TABLE Monedas "
            + "(id_moneda INTEGER PRIMARY KEY, code TEXT, "
            + "	number INTEGER, decimal INTEGER )";



    public static final String SELECT_MONEDAS_ORDER_CODE = "SELECT id_moneda, code, number, decimal" +
            " FROM Monedas ORDER BY code";
    public static final String SELECT_MONEDAS_CODE_ORDER_CODE = "SELECT  code " +
            " FROM Monedas ORDER BY code";

    //**************************PAISES ***************************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_PAISES = "CREATE TABLE Paises " +
            "(id_pais INTEGER PRIMARY KEY, nombre_iso TEXT, nombre TEXT)";

    public static final String SELECT_PAISES_NOMBREISO_BY_NOMBREISO = "SELECT nombre_iso FROM Paises ORDER BY nombre_iso ";

    public static final String SELECT_PAISES_ORDER_NOMBRE = "SELECT id_pais, nombre_iso, nombre FROM Paises ORDER BY nombre";




    //************************ AJUSTES APLICACION *****************************//
    // ***********************************************************************//
    // ***********************************************************************//

    public static final String CREATE_AJUSTES_APLICACION = "CREATE TABLE AjustesAplicacion" +
            "(id_ajuste INTEGER PRIMARY KEY, nombre TEXT, valor TEXT, descripcion TEXT, aux1_string TEXT, aux2_string TEXT," +
            "aux1_integer INTEGER, aux2_integer INTEGER)";

    public static final String SELECT_AJUSTES_APLICACION_NOMBRE_VALOR_BY_NOMBRE = "SELECT nombre,valor FROM AjustesAplicacion ORDER by nombre ";

    public static final String SELECT_AJUSTES_APLICACION_ORDER_BY_NOMBRE = "SELECT id_ajuste,nombre,valor,descripcion,aux1_string,aux2_string,aux1_integer,aux2_integer" +
            " FROM AjustesAplicacion ORDER BY nombre";

    public static final String SELECT_AJUSTES_APLICACION_VALOR_POR_NOMBRE_AJUSTE = "SELECT valor FROM AjustesAplicacion WHERE nombre = ?";

    public static final String LITROS = "litros";
    public static final String GALONES_UK = "galonesUK";
    public static final String GALONES_US = "galonesUS";
    public static final String KM = "km";
    public static final String MILLAS = "millas";


}

