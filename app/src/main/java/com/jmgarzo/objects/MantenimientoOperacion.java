package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 5/04/15.
 */
public class MantenimientoOperacion {

    Integer idMantenimiento, idOperacion,periodicidadDigito;
    String periodicidadUnidad,comentarios;
    BigDecimal coste;

    public Integer getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Integer idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Integer getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(Integer idOperacion) {
        this.idOperacion = idOperacion;
    }

    public Integer getPeriodicidadDigito() {
        return periodicidadDigito;
    }

    public void setPeriodicidadDigito(Integer periodicidadDigito) {
        this.periodicidadDigito = periodicidadDigito;
    }

    public String getPeriodicidadUnidad() {
        return periodicidadUnidad;
    }

    public void setPeriodicidadUnidad(String periodicidadUnidad) {
        this.periodicidadUnidad = periodicidadUnidad;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
