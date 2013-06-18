/*
 * Created on Jun 8, 2005
 *
 */
package com.qmenu.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author assir
 *
 */
public class Numero {
	public final static double verificaZero(double numero){
		if(numero == 0)
			return 1;
		else
			return numero;
	}
	/**
	 * Obtem um número com precisão decimal (double) a partir de uma String.
	 * Qualquer erro na transformação força um retorno de 0.0.
	 * <br>
	 * @param numero String com o número a converter.
	 * @return Número tipo double convertido ou 0.
	 */
	public final static double getDouble(String numero){ 
		double n = 0.0;
		try{
			n = Double.parseDouble(geraNumero(numero));
		}catch(Exception e){}
		return n;
	}

	/**
	 * Obtem um número com precisão decimal (Double) a partir de uma String.
	 * Qualquer erro na transformação força um retorno de 0.0.
	 * <br>
	 * @param numero String com o número a converter.
	 * @return Número tipo Double convertido ou 0.
	 */
	public final static Double getNewDouble(String numero){ 
		Double n = null;
		try{
			n = new Double(numero);
		}catch(Exception e){
			n = new Double(0);
		}
		return n;
	}
	/**
	 * Obtem um número com precisão inteira (int) a partir de uma String.
	 * Qualquer erro na transformação força um retorno de 0.
	 * <br>
	 * @param numero String com o número a converter.
	 * @return Número tipo int convertido ou 0.
	 */
	public final static int getInt(String numero){ 
		int n = 0;
		try{
			n = Integer.parseInt(geraNumero(numero));
		}catch(Exception e){}
		return n;
	}	    

	public final static int getInt(String numero, int alternativo){ 
		int n = alternativo;
		try{
			if(numero!=null && !numero.equals(""))
				n = Integer.parseInt(geraNumero(numero));
		}catch(Exception e){}
		return n;
	}	    
	/**
	 * Obtem um número com precisão inteira (int) a partir de uma String.
	 * Qualquer erro na transformação força um retorno de 0.
	 * <br>
	 * @param numero String com o número a converter.
	 * @return Número tipo Integer convertido ou 0.
	 */
	public final static Integer getInteger(String numero){ 
		return new Integer(getInt(numero));
	}	    
	
	/**
	 * Substitui virgula por ponto 
	 * @param numero
	 * @return numero tratado ou 0 caso entrada for null ou branco
	 */
	public final static String geraNumero(String numero) {
		String retorno = "0";
		if (numero!=null && !numero.equals(""))
			retorno =  numero.replaceAll(",",".");
		return retorno;
	}
	/**
	 * @param retorno
	 * @param decimal
	 * @return
	 */
	public static String formata(double numero, int decimal) {
		return formata(numero, decimal, false);
	}
	
	/**
	 * @param retorno
	 * @param decimal
	 * @return
	 */
	public static String formata(double numero, int decimal, boolean formataMilhar) {
    	String mascara = "0.";
    	String mascaraMilhar = "";
    	DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dc = new DecimalFormatSymbols();
    	if(formataMilhar){
	    	dc.setDecimalSeparator(',');
	    	dc.setGroupingSeparator('.');		    	
	    	if(Math.abs(numero) >= 1000)
	    		mascaraMilhar = "0,00";	
    	}else{
    		dc.setDecimalSeparator('.');    		
    	}
    	df.setDecimalFormatSymbols(dc);
    	if (decimal != 0) {
    		for (int i = 0; i < decimal; i++)
    			mascara += "0";
    	} else
    		mascara = "0";
    	df.applyPattern(mascaraMilhar+mascara);            
    	return df.format(numero);    	
	}

	public static String formataEsp(double numero, int decimal, boolean formataMilhar){
		String retorno = "";
		if(numero<0)
			retorno = "(" + formata(Math.abs(numero), decimal, formataMilhar) + ")";
		else if(numero==0)
			retorno = "-";
		else
			retorno = Numero.formata(numero, decimal, formataMilhar);
		return retorno;
	}
	
    public static String formataSimples(double numero, int decimal) {
    	String mascara = "0.";
    	java.text.DecimalFormat f = new java.text.DecimalFormat();
    	if (decimal != 0) {
    		for (int i = 0; i < decimal; i++)
    			mascara += "0";
    	} else
    		mascara = "0";
		DecimalFormatSymbols dc = new DecimalFormatSymbols();
   		dc.setDecimalSeparator(',');    		
    	f.applyPattern(mascara);            
    	return f.format(numero);    	
    }
	
	public final static double arredonda(double numero, int decimal){ 
		double n = 0.0;
		try{
			n = Double.parseDouble(formata(numero, decimal));
		}catch(Exception e){}
		return n;
	}
	
    /**
     * Despreza as casas decimais do número a partir de "casas" casas decimais.
     * <br>
     * @param numero Número a ser "truncado".
     * @param casas Número de casas decimas do resultado.
     * @return Número "truncado"
     */
    public static final double trunca(double numero, int casas){
        int inteiro = (int) numero;
        double decimal = numero - inteiro ;
        double desloca = Math.pow(10, casas); 
        double valor = 	inteiro + 
        				(int) (decimal * desloca) /
        				desloca;
        return valor;
    }	
}
