package com.qmenu.model;

import com.qmenu.util.Numero;

public class Item {
    private Integer codigo;
	private String descricao;
	private String descricaoestab;
	private Integer grupoadicionais;
	private double preco;
	private boolean selecionado;

    public String getDescricaoestab() {
		return descricaoestab;
	}
	public void setDescricaoestab(String descricaoestab) {
		this.descricaoestab = descricaoestab;
	}
    public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getPreco() {
		return preco;
	}
	public String getPrecoF() {
		return Numero.formata(preco,2);
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public boolean isSelecionado() {
		return selecionado;
	}
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}
	public Integer getGrupoadicionais() {
		return grupoadicionais;
	}
	public void setGrupoadicionais(Integer grupoadicionais) {
		this.grupoadicionais = grupoadicionais;
	}
}