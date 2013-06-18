package com.qmenu.model;

import com.qmenu.util.Data;


public class Mesa implements Comparable<Mesa>{
	private String id;
    private String codigo;
	private String nome;
	private String situacao;
	private String dataultsituacao;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getDataultsituacao() {
		return dataultsituacao;
	}
	public void setDataultsituacao(String dataultsituacao) {
		this.dataultsituacao = dataultsituacao;
	}
	
	public String getHora(){
		String retorno = "";
		try{
			int pos = ("" + dataultsituacao).indexOf(" ");
			if(pos>0){
				retorno = dataultsituacao.substring(pos);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;		
	}	
	
	public int compareTo(Mesa mesa) {
		if(mesa.getSituacao().equals("D") && getSituacao().equals("O"))
			return -1;
		else if(mesa.getSituacao().equals("O") && getSituacao().equals("D"))
			return 1;
		else
			return Data.getDateHour(mesa.getDataultsituacao()).compareTo(Data.getDateHour(this.getDataultsituacao()));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}