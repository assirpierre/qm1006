package com.qmenu.model;

import com.qmenu.util.Numero;

public class Estabelecimento {
	private String codigo;
	private String nomeFantasia;
	private String descricao;
	private String distancia;
	private String sistemaTrabalho;
	private String tempo;

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getDistancia() {
		return Numero.formata(Numero.getDouble(distancia)/1000,1) + " km";
	}
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	public String getTempo() {
		return Numero.formata(Numero.getDouble(tempo)/60,0) + " min";
	}
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	public String getSistemaTrabalho() {
		return sistemaTrabalho;
	}
	public void setSistemaTrabalho(String sistemaTrabalho) {
		this.sistemaTrabalho = sistemaTrabalho;
	}

}
