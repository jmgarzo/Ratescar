package com.jmgarzo.objects;

/**
 * Created by JoseMaria on 27/10/2015.
 * EStos serán los objetos para guardar los paises con el código  ISO 3166-1 alpha-3
 */
public class Pais {
    private Integer idPais;
    private String nombreIS0, nombre;

    public Pais() {
        super();
    }

    public Pais(Integer idPais, String nombreISO, String nombre) {
        this.idPais = idPais;
        this.nombreIS0 = nombreISO;
        this.nombre = nombre;

    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getNombreIS0() {
        return nombreIS0;
    }

    public void setNombreIS0(String nombreIS0) {
        this.nombreIS0 = nombreIS0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
