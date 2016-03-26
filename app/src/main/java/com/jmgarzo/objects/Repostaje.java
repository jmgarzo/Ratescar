package com.jmgarzo.objects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


import java.math.BigDecimal;

/**
 * Created by jmgarzo on 17/12/14.
 */
public class Repostaje implements Parcelable {

    /**
     * id_repostaje ->INTEGER id_coche ->INTEGER id_combustible ->INTEGER
     * km_repostaje ->INTEGER litros ->FLOAT precio_litro ->FLOAT coste_repostaje->FLOAT
     *  es_completo ->BOOLEAN es_aa ->BOOLEAN es_remolque ->BOOLEAN es_baca -> BOOLEAN
     * tipo_carretera ->TEXT tipo_pago ->TEXT velocidad_media ->FLOAT
     * area_servicio ->TEXT fecha_repostaje ->DATE tipo_conduccion ->TEXT
     * comentarios ->TEXT
     */

    private Integer idRepostaje, idCoche, idCombustible;
    private Boolean esCompleto, esAA, esRemolque, esBaca;
    private BigDecimal kmRepostaje, litros, precioLitro, costeRepostaje,velocidadMedia,kmRecorridos, mediaConsumo;
    private String tipoCarretera, tipoPago, areaServicio, tipoConduccion,
            comentarios;

    private Long fechaRespostaje;
    public Repostaje(){

    }
    public Repostaje(Parcel in) {
        readFromParcel(in);
    }

    public Integer getIdRepostaje() {
        return idRepostaje;
    }

    public void setIdRepostaje(Integer idRepostaje) {
        this.idRepostaje = idRepostaje;
    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public Integer getIdCombustible() {
        return idCombustible;
    }

    public void setIdCombustible(Integer idCombustible) {
        this.idCombustible = idCombustible;
    }

    public BigDecimal getKmRepostaje() {
        return kmRepostaje;
    }

    public void setKmRepostaje(BigDecimal kmRepostaje) {
        this.kmRepostaje = kmRepostaje;
    }

    public Boolean getEsCompleto() {
        return esCompleto;
    }

    public void setEsCompleto(Boolean esCompleto) {
        this.esCompleto = esCompleto;
    }

    public Boolean getEsAA() {
        return esAA;
    }

    public void setEsAA(Boolean esAA) {
        this.esAA = esAA;
    }

    public Boolean getEsRemolque() {
        return esRemolque;
    }

    public void setEsRemolque(Boolean esRemolque) {
        this.esRemolque = esRemolque;
    }

    public Boolean getEsBaca() {
        return esBaca;
    }

    public void setEsBaca(Boolean esBaca) {
        this.esBaca = esBaca;
    }

    public BigDecimal getLitros() {
        return litros;
    }

    public void setLitros(BigDecimal litros) {
        this.litros = litros;
    }


    public BigDecimal getCosteRepostaje() {
        return costeRepostaje;
    }
    public void setCosteRepostaje(BigDecimal costeRepostaje) {
        this.costeRepostaje = costeRepostaje;
    }
    public BigDecimal getPrecioLitro() {
        return precioLitro;
    }

    public void setPrecioLitro(BigDecimal precioLitro) {
        this.precioLitro = precioLitro;
    }

    public BigDecimal getVelocidadMedia() {
        return velocidadMedia;
    }

    public void setVelocidadMedia(BigDecimal velocidadMedia) {
        this.velocidadMedia = velocidadMedia;
    }

    public String getTipoCarretera() {
        return tipoCarretera;
    }

    public void setTipoCarretera(String tipoCarretera) {
        this.tipoCarretera = tipoCarretera;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getAreaServicio() {
        return areaServicio;
    }

    public void setAreaServicio(String areaServicio) {
        this.areaServicio = areaServicio;
    }

    public String getTipoConduccion() {
        return tipoConduccion;
    }

    public void setTipoConduccion(String tipoConduccion) {
        this.tipoConduccion = tipoConduccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Long getFechaRespostaje() {
        return fechaRespostaje;
    }

    public void setFechaRespostaje(Long fechaRespostaje) {
        this.fechaRespostaje = fechaRespostaje;
    }


    public BigDecimal getKmRecorridos() {
        return kmRecorridos;
    }
    public void setKmRecorridos(BigDecimal kmRecorridos) {
        this.kmRecorridos = kmRecorridos;
    }
    public BigDecimal getMediaConsumo() {
        return mediaConsumo;
    }
    public void setMediaConsumo(BigDecimal mediaConsumo) {
        this.mediaConsumo = mediaConsumo;
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * id_repostaje ->INTEGER id_coche ->INTEGER id_combustible ->INTEGER
     * km_repostaje ->INTEGER litros ->FLOAT precio_litro ->FLOAT es_completo
     * ->BOOLEAN es_aa ->BOOLEAN es_remolque ->BOOLEAN es_baca -> BOOLEAN tipo_carretera ->TEXT
     * tipo_pago ->TEXT velocidad_media ->FLOAT area_servicio ->TEXT
     * fecha_repostaje ->DATE tipo_conduccion ->TEXT comentarios ->TEXT
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idRepostaje);
        dest.writeInt(idCoche);
        dest.writeInt(idCombustible);
        dest.writeDouble(kmRepostaje.doubleValue());
        dest.writeDouble(litros.doubleValue());
        dest.writeDouble(precioLitro.doubleValue());
        dest.writeDouble(costeRepostaje.doubleValue());
        if (esCompleto) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (esAA) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (esRemolque) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (esBaca){
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }


        dest.writeString(tipoCarretera);
        dest.writeString(tipoPago);
        dest.writeDouble(velocidadMedia.doubleValue());
        dest.writeString(areaServicio);
        dest.writeLong(fechaRespostaje);
        dest.writeString(tipoConduccion);
        dest.writeString(comentarios);
        dest.writeDouble(kmRecorridos.doubleValue());
        dest.writeDouble(mediaConsumo.doubleValue());
    }

    public void readFromParcel(Parcel in) {
        idRepostaje = in.readInt();
        idCoche = in.readInt();
        idCombustible = in.readInt();
        kmRepostaje = BigDecimal.valueOf(in.readDouble());
        litros = BigDecimal.valueOf(in.readDouble());
        precioLitro = BigDecimal.valueOf(in.readDouble());
        costeRepostaje = BigDecimal.valueOf(in.readDouble());
        if (in.readInt() == 1) {
            esCompleto = true;
        } else {
            esCompleto = false;
        }

        if (in.readInt() == 1) {
            esAA = true;
        } else {
            esAA = false;
        }
        if (in.readInt() == 1) {
            esRemolque = true;
        } else {
            esRemolque = false;
        }

        if (in.readInt() == 1) {
            esBaca = true;
        } else {
            esBaca = false;
        }

        tipoCarretera = in.readString();
        tipoPago = in.readString();
        velocidadMedia = BigDecimal.valueOf(in.readDouble());
        areaServicio = in.readString();
        fechaRespostaje = in.readLong();
        tipoConduccion = in.readString();
        comentarios = in.readString();
        kmRecorridos = BigDecimal.valueOf(in.readDouble());
        mediaConsumo = BigDecimal.valueOf(in.readDouble());
    }

    public static final Parcelable.Creator<Repostaje> CREATOR = new Parcelable.Creator<Repostaje>() {

        @Override
        public Repostaje createFromParcel(Parcel in) {
            return new Repostaje(in);
        }

        @Override
        public Repostaje[] newArray(int size) {
            return new Repostaje[size];
        }

    };




}
