package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 28/01/15.
 */
public class Ajuste {
    private String titulo,subtitulo;
    private int icono;


    public Ajuste(int icono, String titulo, String subtitulo){
        this.icono = icono;
        this.titulo=titulo;
        this.subtitulo=subtitulo;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
