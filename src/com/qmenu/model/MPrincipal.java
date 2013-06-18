package com.qmenu.model;

import java.util.ArrayList;

public class MPrincipal {
	private ArrayList<Item> item = new ArrayList<Item>();
	private String codigo;
	private String descricao;
	private String tipocobranca;
	private int qtdeitem;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getQtdeitem() {
		return qtdeitem;
	}
	public void setQtdeitem(int qtdeitem) {
		this.qtdeitem = qtdeitem;
	}
	public ArrayList<Item> getItem() {
		return item;
	}
	public void setItem(ArrayList<Item> item) {
		this.item = item;
	}
	public String getTipocobranca() {
		return tipocobranca;
	}
	public void setTipocobranca(String tipocobranca) {
		this.tipocobranca = tipocobranca;
	}
}
