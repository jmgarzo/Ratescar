package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 16/03/15.
 */
public class Imagen {

    private Integer idImagen, idCoche;
    String ruta, rutaThumb, tipo;

    public Integer getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Integer idImagen) {
        this.idImagen = idImagen;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRutaThumb() {
        return rutaThumb;
    }

    public void setRutaThumb(String rutaThumb) {
        this.rutaThumb = rutaThumb;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


}
