package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 10/11/2015.
 */
public class AjustesAplicacion {
    private Integer idAjuste;
    private String nombre,valor,descripcion,auxString1,auxString2;
    private Integer auxInteger1,auxInteger2;

    public AjustesAplicacion(Integer idAjuste, String nombre,String valor,String descripcion){
        this.idAjuste=idAjuste;
        this.nombre=nombre;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    public AjustesAplicacion(){
        super();
    }

    public Integer getIdAjuste() {
        return idAjuste;
    }

    public void setIdAjuste(Integer idAjuste) {
        this.idAjuste = idAjuste;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAuxString1() {
        return auxString1;
    }

    public void setAuxString1(String auxString1) {
        this.auxString1 = auxString1;
    }

    public String getAuxString2() {
        return auxString2;
    }

    public void setAuxString2(String auxString2) {
        this.auxString2 = auxString2;
    }

    public Integer getAuxInteger1() {
        return auxInteger1;
    }

    public void setAuxInteger1(Integer auxInteger1) {
        this.auxInteger1 = auxInteger1;
    }

    public Integer getAuxInteger2() {
        return auxInteger2;
    }

    public void setAuxInteger2(Integer auxInteger2) {
        this.auxInteger2 = auxInteger2;
    }
}
