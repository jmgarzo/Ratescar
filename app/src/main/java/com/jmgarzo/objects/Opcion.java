package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 19/12/14.
 */
public class Opcion {
    private String nombre;
    private int icono;

    public Opcion(){
        super();
    }

    public Opcion(String nombre, int icono){
        this.nombre = nombre;
        this.icono = icono;

    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }

    public void setIcono(int icono) {
        icono = icono;
    }

    public int getIcono() {
        return icono;
    }
}
