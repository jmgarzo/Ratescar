package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 5/04/15.
 */
public class Operacion {
    Integer idOperacion;
    String nombre, descripcion;

    public Operacion(){
        super();
    }

    public Operacion(String nombre, String descripcion){
        super();
        this.nombre= nombre;
        this.descripcion=descripcion;
    }

    public Integer getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(Integer idOperacion) {
        this.idOperacion = idOperacion;
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
