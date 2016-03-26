package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 15/04/15.
 */
public class OperacionRecambio {
    Integer idOperacion;
    Integer idRecambio;
    BigDecimal cantidad;
    BigDecimal precioUnidad;
    BigDecimal coste;

    public Integer getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(Integer idOperacion) {
        this.idOperacion = idOperacion;
    }

    public Integer getIdRecambio() {
        return idRecambio;
    }

    public void setIdRecambio(Integer idRecambio) {
        this.idRecambio = idRecambio;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(BigDecimal precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }
}
