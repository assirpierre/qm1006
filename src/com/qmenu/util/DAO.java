/*
 * Created on Dec 31, 2004
 *
 */
package com.qmenu.util;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;


/**
 * @author Assir
 *
 */
public class DAO {
	
	private Object[][] dados = new Object[0][0];
	private HashMap<String, String> nomeColuna = new HashMap<String, String>();
	private HashMap<String, String> colunaNome = new HashMap<String, String>();
	private int linha;
	Context ctx;
	
	public DAO(String xml, Context ctx) {
		this.ctx = ctx;
		carregaDados(xml);
	}
	
	
	private void carregaDados(String xml){
		try{
			if(!xml.equals("")){
		        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();   
		        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes("ISO-8859-1")));            
				linha = -1;
				dados = new Object[Numero.getInt(document.getFirstChild().getAttributes().item(1).getTextContent())][Numero.getInt(document.getFirstChild().getAttributes().item(0).getTextContent())];
		        NodeList nregistro = document.getElementsByTagName("registro");
		        for(int i = 0;i<nregistro.getLength();i++){
		        	NodeList registro = nregistro.item(i).getChildNodes();
		        	for(int j = 0;j<registro.getLength();j++){
	        			if(i==0){
		        			nomeColuna.put(registro.item(j).getNodeName().toUpperCase(Locale.getDefault()),"" + (j+1));
		        			colunaNome.put("" + (j+1), registro.item(j).getNodeName().toUpperCase(Locale.getDefault()));
	        			}
	        			dados[i][j] = registro.item(j).getTextContent();
		        	}
		        }
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getString(int coluna) {
		String retorno = "";
		try{
			if (dados[linha][coluna-1] != null)
				retorno = "" + dados[linha][coluna-1];
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public String getDateF(int linha, int coluna) {
		String retorno = "";
		try{
			if(dados[linha][coluna-1]!= null){
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
				retorno = df.format((Date)dados[linha][coluna-1]);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;		
	}
			
	public double getDouble(int coluna){
		return Numero.getDouble("" + dados[linha][coluna-1]);
	}
	
	public int getInt(int coluna){
		return Numero.getInt(getString(coluna));
	}

	public boolean getBoolean(int coluna){
		return getInt(coluna)==1;
	}

	public boolean getBoolean(String nomeColuna){
		return getInt(nomeColuna)==1;
	}
	
	public int getInt(String nomeColuna){
		return getInt(getCol(nomeColuna));
	}
	
	public Date getDate(int coluna){
		return (Date)dados[linha][coluna-1];
	}	

	public String getDateF(int coluna){
		String retorno = "";
		if(dados[linha][coluna-1]!= null){
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			retorno = df.format((Date)dados[linha][coluna-1]);
		}
		return retorno;
	}	
	
	public String getDateF(String nomeColuna){
		return getDateF(getCol(nomeColuna));
	}	

	public String getDataHora(int coluna){
		String retorno = "";
		if(dados[linha][coluna-1]!=null){
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
			retorno = df.format((Date)dados[linha][coluna-1]);
		}
		return retorno;
	}	

	public String getDataHora(String nomeColuna){		
		return getDataHora(getCol(nomeColuna));
	}
	
	public String getHora(String nomeColuna){
		return getHora(getCol(nomeColuna));
	}
	
	public String getHora(int coluna){
		String retorno = "";
		try{
			int pos = ("" + dados[linha][coluna-1]).indexOf(" ");
			if(pos>0){
				retorno = ("" + dados[linha][coluna-1]).substring(pos);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;		
	}	

	public String getString(String nomeColuna){
		return getString(getCol(nomeColuna));
	}
	
	public double getDouble(String nomeColuna){
		return getDouble(getCol(nomeColuna));
	}

	public int getCol(String nomeColuna) {
		return Integer.parseInt("" + this.nomeColuna.get(nomeColuna.toUpperCase(Locale.getDefault())));
	}

	public String getNumero(int coluna, int decimal){
		return Numero.formata(getDouble(coluna), decimal);
	}
	public String getNumero(String nomeColuna, int decimal){
		return Numero.formata(getDouble(nomeColuna), decimal);
	}
	public String getPreco(int coluna, int decimal){
		return Numero.formata(getDouble(coluna), decimal, true);
	}
	public String getPreco(String nomeColuna, int decimal){
		return Numero.formata(getDouble(nomeColuna), decimal, true);
	}
	public String getPrecoF(int coluna, int decimal){
		return Numero.formataEsp(getDouble(coluna), decimal, true);
	}

	public String getPrecoF(String coluna, int decimal){
		return Numero.formataEsp(getDouble(coluna), decimal, true);
	}
	
	public Date getDate(String nomeColuna){
		return getDate(getCol(nomeColuna));
	}	
	
	public String getColumnName(int i){
		return "" + colunaNome.get("" + i);
	}
	
	public int getColumnCount(){
		return colunaNome.size();
	}
	
	public int getRow(){
		return linha + 1;
	}

	public void setRow(int linha){
		this.linha = linha - 1;
	}

	public int getRows(){
		return dados.length;
	}

	public void beforeFirst() {
		linha=-1;
	}
	
	public boolean absolute(int linha){
		if (linha > 0 && linha <= dados.length){
			this.linha = linha-1;
			return true;
		}else
			return false;
	}
	
	public boolean next() {
		linha++;
		return !isAfterLast();
	}

	public boolean previous() {
		linha--;
		return !isBeforeFirst();
	}

	public boolean first() {
		if (dados.length == 0)
			return false;
		else{
			linha = 0;
			return true;
		}
	}

	public boolean last() {
		if (dados.length == 0)
			return false;
		else{
			linha = dados.length-1;
			return true;
		}
	}

	public boolean isAfterLast() {
		if (linha>=dados.length)
			return true;
		else{
			return false;
		}
	}

	public boolean isLast() {
		if (linha!=dados.length-1)
			return false;
		else{
			return true;
		}
	}

	public boolean isBeforeFirst() {
		if (linha!=-1)
			return false;
		else{
			return true;
		}
	}

	public boolean isFirst() {
		if (linha!=0)
			return false;
		else{
			return true;
		}
	}
		
	public String[] getArray(String nomeColuna){
		int col = getCol(nomeColuna);
		String[] retorno = new String[getRows()];
		for(int i=0;i<getRows();i++)
			retorno[i] = "" + dados[i][col-1];
		return retorno;
	}


	public String getErro() {
		return "";
	}
}
