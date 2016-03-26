package com.jmgarzo.objects;

import java.math.BigDecimal;

/**
 * Created by jmgarzo on 26/12/14.
 */
public class Mantenimiento {

//    id_mantenimiento INTEGER PRIMARY KEY
//    id_coche INTEGER NOT NULL REFERENCES Coches (id_coche)
//    tipo_mantenimiento TEXT
//    nombre_mantenimiento TEXT
//    fecha_matenimiento INTEGER
//    km_mantenimiento INTEGER
//    coste_iva FLOAT
//    coste_sin_iva FLOAT
//    tanto_por_cien_iva FLOAT
//    descuento_total FLOAT
//    coste_final FLOAT
//    comentarios TEXT

    private Integer idMantenimiento,idCoche;
        private String tipoMantenimiento, comentarios;
    private Long fechaMantenimiento;
    private BigDecimal costeFinal,kmMatenimiento;

    public Integer getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Integer idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public BigDecimal getKmMatenimiento() {
        return kmMatenimiento;
    }

    public void setKmMatenimiento(BigDecimal kmMatenimiento) {
        this.kmMatenimiento = kmMatenimiento;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }


    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Long getFechaMantenimiento() {
        return fechaMantenimiento;
    }

    public void setFechaMantenimiento(Long fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

    public BigDecimal getCosteFinal() {
        return costeFinal;
    }

    public void setCosteFinal(BigDecimal costeFinal) {
        this.costeFinal = costeFinal;
    }
}
