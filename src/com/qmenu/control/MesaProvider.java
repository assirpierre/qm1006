package com.qmenu.control;

import java.util.ArrayList;

import android.app.Activity;

import com.qmenu.model.Mesa;
import com.qmenu.util.DAO;

public class MesaProvider {
	
	private static ArrayList<Mesa> mesa = new ArrayList<Mesa>();
	
	public static void atualiza(String xml, Activity a){
		DAO rs = new DAO(xml, a);
		if(rs.next()){
			mesa = new ArrayList<Mesa>();
			do{
				Mesa o = new Mesa();
				o.setId(rs.getString("id"));
				o.setCodigo(rs.getString("numero"));
				o.setNome(rs.getString("nome"));
				o.setSituacao(rs.getString("situacao"));
				o.setDataultsituacao(rs.getString("dataultsituacao"));
				mesa.add(o);
			}while(rs.next());
		}
	}

	public static ArrayList<Mesa> getMesa() {
		return mesa;
	}

	public static void setMesa(ArrayList<Mesa> mesa) {
		MesaProvider.mesa = mesa;
	}

	public static Mesa getMesa(String codigo){		
		for(Mesa o: mesa)
			if(o.getCodigo().equals(codigo))
				return o;
		return null;
	}

}
