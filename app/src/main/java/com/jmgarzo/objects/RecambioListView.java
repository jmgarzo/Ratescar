package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 17/04/15.
 */
public class RecambioListView {

    private Recambio recambio;
    private boolean seleccionado;

    public RecambioListView(Recambio recambio, boolean seleccionado){
        this.recambio = recambio;
        this.seleccionado = seleccionado;
    }

    public Recambio getRecambio() {
        return recambio;
    }

    public void setRecambio(Recambio recambio) {
        this.recambio = recambio;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}
