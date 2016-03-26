package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 2/02/15.
 */
public class ListaConIcono {
    private int icono;
    private String titulo, subtitulo;

    public ListaConIcono(int icono, String titulo, String subtitulo) {
        this.icono = icono;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
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
}
