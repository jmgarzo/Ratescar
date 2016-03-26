package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 14/01/15.
 */
public class Itv {

    private Integer idItv,idCoche;
    private Long fechaItv;
    private BigDecimal precio, kmItv;
    private String estacion,resultado,observaciones;

    public Long getFechaItv() {
        return fechaItv;
    }

    public void setFechaItv(Long fechaItv) {
        this.fechaItv = fechaItv;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdItv() {
        return idItv;
    }

    public void setIdItv(Integer idItv) {
        this.idItv = idItv;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public BigDecimal getKmItv() {
        return kmItv;
    }

    public void setKmItv(BigDecimal kmItv) {
        this.kmItv = kmItv;
    }
}
