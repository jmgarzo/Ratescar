package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 11/03/15.
 */
public class ImpuestoCirculacion {
    private Integer idImpuesto, idCoche;
    private Long anualidad;
    private Long fechaFinPago;
    private BigDecimal importe;
    private String comentarios;

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public Long getAnualidad() {
        return anualidad;
    }

    public void setAnualidad(Long anualidad) {
        this.anualidad = anualidad;
    }

    public Long getFechaFinPago() {
        return fechaFinPago;
    }

    public void setFechaFinPago(Long fechaFinPago) {
        this.fechaFinPago = fechaFinPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
