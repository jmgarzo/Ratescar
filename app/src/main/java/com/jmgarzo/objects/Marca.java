package com.jmgarzo.objects;

import android.graphics.drawable.Drawable;

public class Marca {

	private Integer idMarca;
	private String nombre;
	private int icono;

	public Marca() {
		super();
	}

	public Marca(Integer idMarca, String nombre, int icono) {
		this.idMarca = idMarca;
		this.nombre = nombre;
		this.icono = icono;
	}

	public int getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {

		return getNombre();
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

}
