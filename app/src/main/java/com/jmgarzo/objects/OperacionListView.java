package com.jmgarzo.objects;

/**
 * Created by jmgarzo on 6/04/15.
 */
public class OperacionListView {
    private Operacion operacion;
    private boolean seleccionado;

    public OperacionListView(Operacion operacion, boolean seleccionado){
        this.operacion = operacion;
        this.seleccionado = seleccionado;
    }

    public Operacion getOperacion() {
        return operacion;
    }

    public void setOperacion(Operacion operacion) {
        this.operacion = operacion;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}
