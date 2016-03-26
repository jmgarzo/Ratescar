package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 1/07/15.
 */
public class Moneda {

    private Integer idMoneda;
    private String code;
    private Integer number;
    private Integer decimal;

    public Moneda(){
        super();
    }

    public Moneda (Integer idMoneda, String code, Integer number, Integer decimal){
        this.idMoneda = idMoneda;
        this.code = code;
        this.number = number;
        this.decimal = decimal;
    }

    public Integer getIdMoneda() {
        return idMoneda;
    }

    public void setIdMoneda(Integer idMoneda) {
        this.idMoneda = idMoneda;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getDecimal() {
        return decimal;
    }

    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }
}
