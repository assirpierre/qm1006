package com.qmenu.model;

import java.util.ArrayList;

public class Adicionais{
	private String grupoadicionais;
	private ArrayList<Item> item = new ArrayList<Item>();
	private String descricaoestab;
	private double preco;
	public String getGrupoadicionais() {
		return grupoadicionais;
	}
	public void setGrupoadicionais(String grupoadicionais) {
		this.grupoadicionais = grupoadicionais;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public String getDescricaoestab() {
		return descricaoestab;
	}
	public void setDescricaoestab(String descricaoestab) {
		this.descricaoestab = descricaoestab;
	}
	public ArrayList<Item> getItem() {
		return item;
	}
	public void setItem(ArrayList<Item> item) {
		this.item = item;
	}
}