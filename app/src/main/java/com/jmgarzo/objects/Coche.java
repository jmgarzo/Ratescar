package com.jmgarzo.objects;

import java.math.BigDecimal;

import android.os.Parcel;
import android.os.Parcelable;

public class Coche implements Parcelable {

    /**
     * id_coche ->INTEGER matricula ->TEXT id_marca ->INTEGER nombre ->TEXT
     * modelo ->TEXT id_combustible->INTEGER cc ->TEXT tamano_deposito->FLOAT
     * km_iniciales ->INTEGER km_actuales ->INTEGER comentarios ->TEXT
     */

    private Integer idCoche, idMarca, idCombustible;
    private String matricula;
    private String nombre;
    private String marca;
    private String modelo;
    private String cc;
    private String comentarios;
    private String version;
    private BigDecimal tamanoDeposito,kmIniciales, kmActuales;
    private Long fechaCompra, fechaMatriculacion, fechaFabricacion;

    // public Coche(Integer id_coche) {
    // this.idCoche = id_coche;
    //
    // }

    public Coche() {

    }

    public Coche(Parcel in) {
        readFromParcel(in);

    }

    public Integer getIdCoche() {
        return idCoche;
    }

    public void setIdCoche(Integer idCoche) {
        this.idCoche = idCoche;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Integer getIdCombustible() {
        return idCombustible;
    }

    public void setIdCombustible(Integer idCombustible) {
        this.idCombustible = idCombustible;
    }

    public BigDecimal getKmIniciales() {
        return kmIniciales;
    }

    public void setKmIniciales(BigDecimal kmIniciales) {
        this.kmIniciales = kmIniciales;
    }

    public BigDecimal getKmActuales() {
        return kmActuales;
    }

    public void setKmActuales(BigDecimal kmActuales) {
        this.kmActuales = kmActuales;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public BigDecimal getTamanoDeposito() {
        return tamanoDeposito;
    }

    public void setTamanoDeposito(BigDecimal tamanoDeposito) {
        this.tamanoDeposito = tamanoDeposito;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Long getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Long fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Long getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(Long fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public Long getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(Long fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * private Integer idCoche, idMarca, idCombustible, kmIniciales, kmActuales;
     * public String matricula; private String nombre; private String marca;
     * private String modelo; private String cc; private String comentarios;
     * private BigDecimal tamanoDeposito;
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCoche);
        dest.writeInt(idMarca);
        dest.writeInt(idCombustible);
        dest.writeDouble(kmIniciales.doubleValue());
        dest.writeDouble(kmActuales.doubleValue());
        dest.writeString(matricula);
        dest.writeString(nombre);
        dest.writeString(marca);
        dest.writeString(modelo);
        dest.writeString(cc);
        dest.writeString(comentarios);
        dest.writeDouble(tamanoDeposito.doubleValue());
    }

    public void readFromParcel(Parcel in) {

        idCoche = in.readInt();
        idMarca = in.readInt();
        idCombustible = in.readInt();
        kmIniciales = BigDecimal.valueOf(in.readDouble());
        kmActuales = BigDecimal.valueOf(in.readDouble());
        matricula = in.readString();
        nombre = in.readString();
        marca = in.readString();
        modelo = in.readString();
        cc = in.readString();
        comentarios = in.readString();
        tamanoDeposito = BigDecimal.valueOf(in.readDouble());
    }

    public static final Parcelable.Creator<Coche> CREATOR = new Parcelable.Creator<Coche>() {
        @Override
        public Coche createFromParcel(Parcel in) {
            return new Coche(in);
        }

        @Override
        public Coche[] newArray(int size) {
            // TODO Auto-generated method stub

            return new Coche[size];
        }

    };

    @Override
    public String toString() {

        return getNombre().concat("  ").concat(getMatricula());
    }

}
