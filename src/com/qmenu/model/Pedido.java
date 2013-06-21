package com.qmenu.model;

import java.util.ArrayList;

import com.qmenu.util.Numero;


public class Pedido {
	private String codigo;
	private String linha;
    private ArrayList<Item> l_item = new ArrayList<Item>();
    private ArrayList<Item> l_itemadd = new ArrayList<Item>();
    private String item = "";
    private String itemadd = "";
    private String itemdescricao = "";
    private String itemadddescricao = "";
    private MPrincipal mprincipal;
	private String datapedido;
	private String observacao;
	private String usuario;
	private String situacao;
	private int qtde;
	private double precounitario;
	private double precoadicionais = 0;
	private double total;
	private int posItemSelecionado;

	public String getDatapedido() {
		return datapedido;
	}
	public void setDatapedido(String datapedido) {
		this.datapedido = datapedido;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getLinha() {
		return linha;
	}
	public void setLinha(String linha) {
		this.linha = linha;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public int getQtde() {
		return qtde;
	}
	public void setQtde(int qtde) {
		this.qtde = qtde;
		calculaTotal();
	}
	
	private void calculaTotal() {
		this.total = precounitario * qtde + precoadicionais * qtde;
	}
	
	public double getTotal() {
		return total;
	}
	public String getTotalF() {
		return Numero.formata(total,2);
	}
	public void setTotal(double total) {
		this.total = total;
	}

	public String getHora(){
		String retorno = "";
		try{
			int pos = ("" + datapedido).indexOf(" ");
			if(pos>0){
				retorno = datapedido.substring(pos);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;		
	}

	public void addItem(Item item){
        this.item += (l_item.size() == 0?"":" - ") + item.getDescricao();
        this.itemdescricao += (l_item.size() == 0?"":" - ") + item.getDescricaoestab();
		l_item.add(item);
	}
	
	public void addItemadd(Item item, boolean recalcula){
        this.itemadd += (l_itemadd.size() == 0?"":" - ") + item.getDescricao();
        this.itemadddescricao += (l_itemadd.size() == 0?"":" - ") + item.getDescricaoestab();
		l_itemadd.add(item);
		if(recalcula){
			precoadicionais += item.getPreco();
			calculaTotal();
		}
	}
	
	public int getQtdeItem(){
		return l_item.size();
	}
	
	public int getQtdeItemadd(){
		return l_itemadd.size();
	}
	
	public Item getItemSelecionado(){
		return l_item.get(posItemSelecionado);
	}
	
	public String getDescricaoItens(){
		String retorno = "";
		for(int i = 0; i <l_item.size();i++)
			retorno += (i==0?"":" / ") + l_item.get(i).getDescricao();
		return retorno;
	}

	public ArrayList<Item> getL_item() {
		return l_item;
	}
	public void setL_item(ArrayList<Item> l_item) {
		this.l_item = l_item;
	}
	public int getPosItemSelecionado() {
		return posItemSelecionado;
	}
	public void setPosItemSelecionado(int posItemSelecionado) {
		this.posItemSelecionado = posItemSelecionado;
	}
	public MPrincipal getMprincipal() {
		return mprincipal;
	}
	public void setMprincipal(MPrincipal mprincipal) {
		this.mprincipal = mprincipal;
	}
	public void setMprincipalIni(MPrincipal mprincipal) {
		this.mprincipal = mprincipal;
		qtde = 1;
		if(mprincipal.getTipocobranca().equals("N")){
			for(Item o:l_item){
				if(qtde * o.getPreco()>total)
					total = qtde * o.getPreco();
			}
		}else{
			for(Item o:l_item)
				total += qtde * o.getPreco();			
			total = total / l_item.size();
		}
		precounitario = total;		
		
	}
	public double getPrecounitario() {
		return precounitario;
	}
	public void setPrecounitario(double precounitario) {
		this.precounitario = precounitario;
	}
	public void limpaItemadd(){
        itemadd = "";
        itemadddescricao = "";
		l_itemadd.clear();
		precoadicionais = 0;
		calculaTotal();
	}
	public double getPrecoadicionais() {
		return precoadicionais;
	}
	public void setPrecoadicionais(double precoadicionais) {
		this.precoadicionais = precoadicionais;
	}
	public ArrayList<Item> getL_itemadd() {
		return l_itemadd;
	}
	public void setL_itemadd(ArrayList<Item> l_itemadd) {
		this.l_itemadd = l_itemadd;
	}
	
	public boolean isItemaddChecked(Item item){
		for(Item o:l_itemadd)
			if(o.getCodigo().equals(item.getCodigo()))
				return true;
		return false;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

    public String getItemadddescricao() {
        return itemadddescricao;
    }

    public void setItemadddescricao(String itemadddescricao) {
        this.itemadddescricao = itemadddescricao;
    }

    public String getItemdescricao() {
        return itemdescricao;
    }

    public void setItemdescricao(String itemdescricao) {
        this.itemdescricao = itemdescricao;
    }

    public String getItemadd() {
        return itemadd;
    }

    public void setItemadd(String itemadd) {
        this.itemadd = itemadd;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}