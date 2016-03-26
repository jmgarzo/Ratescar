package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 21/01/15.
 */
public class Seguro {
//    id_seguro INTEGER PRIMARY KEY, "
//            + " id_coche INTEGER NOT NULL REFERENCES Coches (id_coche), "
//            + " compañia TEXT, prima FLOAT, tipo_de_seguro TEXT, numero_poliza TEXT,
// fecha_inicio INTEGER, fecha_vencimiento INTEGER,  periodicidad_digito INTEGER,
// periodicidad_unidad TEXT, observaciones TEXT

    private Integer idSeguro, idCoche,periodicidadDigito;
    private String compania,tipoSeguro,numeroPoliza,periodicidadUnidad,observaciones;
    private Long fechaInicio,fechaVencimiento;
    private BigDecimal prima;

    public Integer getIdSeguro() {
        return idSeguro;
    }

    public void setIdSeguro(Integer idSeguro) {
        this.idSeguro = idSeguro;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public Integer getPeriodicidadDigito() {
        return periodicidadDigito;
    }

    public void setPeriodicidadDigito(Integer periodicidadDigito) {
        this.periodicidadDigito = periodicidadDigito;
    }

    public String getCompania() {
        return compania;
    }

    public void setCompania(String compania) {
        this.compania = compania;
    }

    public String getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public String getPeriodicidadUnidad() {
        return periodicidadUnidad;
    }

    public void setPeriodicidadUnidad(String periodicidadUnidad) {
        this.periodicidadUnidad = periodicidadUnidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Long getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Long fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public BigDecimal getPrima() {
        return prima;
    }

    public void setPrima(BigDecimal prima) {
        this.prima = prima;
    }
}
