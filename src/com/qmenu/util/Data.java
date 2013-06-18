/*
 * Created on Jun 8, 2005
 *
 */
package com.qmenu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @author assir
 *
 */
public class Data {
	
	public final static String formata(Date dt, String formato){
		if(dt==null)
			return null;
		else{
			SimpleDateFormat df = new SimpleDateFormat(formato, Locale.getDefault()); 
			return df.format(dt);
		}
	}
	
	public final static String getAtual(){
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); 
		return df.format(dt);
	}

	public final static String getHoraAtual(){
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault()); 
		return df.format(dt);
	}

	public final static String getDataHoraAtual(){
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		return df.format(dt);
	}

	public final static int getMesAtual(){
		return getMes(new Date());
	}

	public final static int getAnoAtual(){
		return getAno(new Date());
	}

    public static String getMesExtenso(int mes) {
        switch (mes) {
        case 1:
            return "Janeiro";
        case 2:
            return "Fevereiro";
        case 3:
            return "Março";
        case 4:
            return "Abril";
        case 5:
            return "Maio";
        case 6:
            return "Junho";
        case 7:
            return "Julho";
        case 8:
            return "Agosto";
        case 9:
            return "Setembro";
        case 10:
            return "Outubro";
        case 11:
            return "Novembro";
        default:
            return "Dezembro";
        }
    }

    public static String getMesExtensoAbr(int mes) {
        switch (mes) {
        case 1:
            return "Jan";
        case 2:
            return "Fev";
        case 3:
            return "Mar";
        case 4:
            return "Abr";
        case 5:
            return "Mai";
        case 6:
            return "Jun";
        case 7:
            return "Jul";
        case 8:
            return "Ago";
        case 9:
            return "Set";
        case 10:
            return "Out";
        case 11:
            return "Nov";
        default:
            return "Dez";
        }
    }

    public static int getAnoAnterior(int ano, int mes) {
    	if(mes == 1)
    		ano--;
    	return ano;
    }
    
    public static int getMesAnterior(int mes) {
    	if(mes == 1)
    		mes = 12;
    	else
    		mes--;
    	return mes;
    }
    
    public static int getAnoPosterior(int ano, int mes) {
    	if(mes == 12)
    		ano++;
    	return ano;
    }
    
    public static int getMesPosterior(int mes) {
    	if(mes == 12)
    		mes = 1;
    	else
    		mes++;
    	return mes;
    }
	
    public static int getDia(String data) {
    	int retorno = 0;    	
		try {
			Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.trim());
			SimpleDateFormat df = new java.text.SimpleDateFormat("dd", Locale.getDefault());
			retorno = Integer.parseInt(df.format(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return retorno;
    }
    
    public static java.sql.Date getSQLDate(String data) {
    	return new java.sql.Date(getDate(data).getTime());
    }
    
    public static Date getDate(String data) {
    	Date dt = null;    	
		try {
			dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return dt;
    }

    public static Date getDateHour(String datahora) {
    	Date dt = null;    	
    	try {
    		dt = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).parse(datahora.trim());
    	} catch (ParseException e) {
    		e.printStackTrace();
    	}
    	return dt;
    }
    
    public static int getMes(String data) {
    	int retorno = 0;    	
		try {
			Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.trim());
			SimpleDateFormat df = new java.text.SimpleDateFormat("MM", Locale.getDefault());
			retorno = Integer.parseInt(df.format(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return retorno;
    }

    public static int getAno(String data) {
    	int retorno = 0;    	
		try {
			Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.trim());
			SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy", Locale.getDefault());
			retorno = Integer.parseInt(df.format(dt));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return retorno;
    }    

    public static int getMes(Date dt) {
    	int retorno = 0;    	
		SimpleDateFormat df = new java.text.SimpleDateFormat("MM", Locale.getDefault());
		try{
			retorno = Integer.parseInt(df.format(dt));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return retorno;
    }

    public static String getDataExtenso(String cidade, Date dt) {
    	String retorno = "";    	
    	retorno += cidade + ", ";
    	retorno += getDia(dt) + " de "; 
    	retorno += getMesExtenso(getMes(dt)) + " de ";
    	retorno += getAno(dt);
    	return retorno;
    }

    public static int getDia(Date dt) {
    	int retorno = 0;    	
		SimpleDateFormat df = new java.text.SimpleDateFormat("dd", Locale.getDefault());
		try{
			retorno = Integer.parseInt(df.format(dt));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return retorno;
    }

    public static int getAno(Date dt) {
    	int retorno = 0;    	
		SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy", Locale.getDefault());
		try{
			retorno = Integer.parseInt(df.format(dt));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return retorno;
    }

    public static GregorianCalendar getGregorianCalendar(String data){
		GregorianCalendar gcData = null;
        try{
			Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(data.trim());  
			SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy", Locale.getDefault());
	        int ano = Integer.parseInt(df.format(dt));
	        df = new java.text.SimpleDateFormat("MM", Locale.getDefault());
	        int mes = Integer.parseInt(df.format(dt));
	        df = new java.text.SimpleDateFormat("dd", Locale.getDefault());
	        int dia = Integer.parseInt(df.format(dt));
			gcData = new GregorianCalendar(ano, mes - 1, dia);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return gcData;
	}    

    public static GregorianCalendar getGregorianCalendar(Date dt){
    	GregorianCalendar gcData = null;
    	try{
    		SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy", Locale.getDefault());
    		int ano = Integer.parseInt(df.format(dt));
    		df = new java.text.SimpleDateFormat("MM", Locale.getDefault());
    		int mes = Integer.parseInt(df.format(dt));
    		df = new java.text.SimpleDateFormat("dd", Locale.getDefault());
    		int dia = Integer.parseInt(df.format(dt));
    		df = new java.text.SimpleDateFormat("hh", Locale.getDefault());
    		int hora = Integer.parseInt(df.format(dt));
    		df = new java.text.SimpleDateFormat("mm", Locale.getDefault());
    		int minuto = Integer.parseInt(df.format(dt));
    		df = new java.text.SimpleDateFormat("ss", Locale.getDefault());
    		int segundo = Integer.parseInt(df.format(dt));
    		gcData = new GregorianCalendar(ano, mes - 1, dia, hora, minuto, segundo);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return gcData;
    }    
    
    public static String subtraiHora(Date dt){
    	String retorno = "";
    	try{
    		SimpleDateFormat df1 = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    		SimpleDateFormat df2 = new java.text.SimpleDateFormat("hh", Locale.getDefault());
    		SimpleDateFormat df3 = new java.text.SimpleDateFormat("mm:ss", Locale.getDefault());
    		int hora = Integer.parseInt(df2.format(dt));
    		retorno = df1.format(dt) + " " + (hora==0?23:(hora-1)) + ":" + df3.format(dt);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return retorno;
    }
    
    public static String getData(GregorianCalendar gc){
    	return gc.get(GregorianCalendar.DAY_OF_MONTH) + "/" + 
			  (gc.get(GregorianCalendar.MONTH)+ 1) + "/" +
			   gc.get(GregorianCalendar.YEAR);
    }

    public static String getDataHora(GregorianCalendar gc){
    	return gc.get(GregorianCalendar.DAY_OF_MONTH) + "/" + 
    	(gc.get(GregorianCalendar.MONTH)+ 1) + "/" +
    	gc.get(GregorianCalendar.YEAR) + " " +
    	gc.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
    	gc.get(GregorianCalendar.MINUTE) + ":" +
    	gc.get(GregorianCalendar.SECOND);
    }

    public static int getDifTimeZone(){
    	acertaHorarioVerao();
    	GregorianCalendar gcInicio = new GregorianCalendar();
    	
    	GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    	GregorianCalendar gcFim = new GregorianCalendar(gc.get(GregorianCalendar.YEAR), 
													    gc.get(GregorianCalendar.MONTH),
													    gc.get(GregorianCalendar.DAY_OF_MONTH),
													    gc.get(GregorianCalendar.HOUR_OF_DAY),
													    gc.get(GregorianCalendar.MINUTE),
													    gc.get(GregorianCalendar.SECOND));
        int dif = 0;
        while(gcInicio.compareTo(gcFim)<0){
        	dif++;
        	gcInicio.add(GregorianCalendar.HOUR, 1);
        }
    	return dif;
    }
    
	public static int getDiaSemana(String data){
	    int dia = 0;
        try{
			dia = getGregorianCalendar(data).get(GregorianCalendar.DAY_OF_WEEK);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return dia;
	}
	
    public static Date UltimoDiaMes(int mes, int ano) {
        GregorianCalendar g = new GregorianCalendar();
        return getDate(g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH) + "/" + mes + "/" + ano);
    }
	
    public static void acertaHorarioVerao(){
		TimeZone.setDefault(
				new SimpleTimeZone(
				TimeZone.getDefault().getRawOffset(),
				"America/Sao_Paulo",
				Calendar.OCTOBER,
				21,
				0,
				3600000*1+60000*0,  // 01h00
				Calendar.FEBRUARY,
				17,
				0,
				3600000*2+60000*0,  // 02h00
				3600000));		
	}	    
    
}
