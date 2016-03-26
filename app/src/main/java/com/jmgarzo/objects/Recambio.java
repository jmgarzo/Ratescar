package com.jmgarzo.objects;

public class Recambio {

	// id_recambio INTEGER PRIMARY KEY
	// referencia TEXT,
	// marca TEXT,
	// nombre TEXT,
	// caracteristicas TEXT,
	// comentarios TEXT)

	private Integer idRecambio;
	private Integer idSubcategoria;
	private String nombre;
	private String fabricante;
	private String referencia;
	private String descripcion;
	private String comentarios;

	public Integer getIdRecambio() {
		return idRecambio;
	}

	public void setIdRecambio(Integer idRecambio) {
		this.idRecambio = idRecambio;
	}

	public Integer getIdSubcategoria() {
		return idSubcategoria;
	}

	public void setIdSubcategoria(Integer idSubcategoria) {
		this.idSubcategoria = idSubcategoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getComentarios() {
		return comentarios;
	}

	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
}
