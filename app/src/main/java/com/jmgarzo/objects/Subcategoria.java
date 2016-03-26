package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 16/04/15.
 */
public class Subcategoria {
    private Integer idSubcategoria,idCategoria;
    private String nombre, descripcion;

    public Subcategoria(){
        super();
    }

    public Subcategoria(String nombre,String descripcion){
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

    public Integer getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Integer idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
