package com.jmgarzo.objects;

public class Combustible {

	// id_combustible INTEGER PRIMARY KEY, tipo TEXT,subtipo TEXT
	private Integer idCombustible;
	private String tipo, subtipo;

	public Integer getIdCombustible() {
		return idCombustible;
	}

	public void setIdCombustible(Integer idCombustible) {
		this.idCombustible = idCombustible;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getSubtipo() {
		return subtipo;
	}

	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}

	@Override
	public String toString() {
		return getTipo();
	}

}
