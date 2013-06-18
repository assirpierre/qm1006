package com.qmenu.control;

import java.util.ArrayList;

import android.app.Activity;

import com.qmenu.model.Estab;
import com.qmenu.util.DAO;

public class EstabProvider {
	
	private static ArrayList<Estab> estab = new ArrayList<Estab>();
	private static String codigo = "";
	private static String nomefantasia = "";
	private static String sistemaTrabalho = "";
	
	public static void atualiza(String xml, Activity a){
		DAO rs = new DAO(xml, a);
		if(rs.next()){
			codigo = rs.getString("id");
			nomefantasia = rs.getString("nomefantasia");
			sistemaTrabalho = rs.getString("sistema_trabalho");
			estab = new ArrayList<Estab>();
			do{
				Estab o = new Estab();
				o.setCodigo(rs.getString("id"));
				o.setNomeFantasia(rs.getString("nomeFantasia"));
				o.setSistemaTrabalho(rs.getString("sistema_trabalho"));
				estab.add(o);
			}while(rs.next());
		}
	}

	public static ArrayList<Estab> getEstab() {
		return estab;
	}

	public static void setEstab(ArrayList<Estab> estab) {
		EstabProvider.estab = estab;
	}

	public static String getCodigo() {
		return codigo;
	}

	public static void setCodigo(String codigo) {
		EstabProvider.codigo = codigo;
	}

	public static String getNomefantasia() {
		return nomefantasia;
	}

	public static void setNomefantasia(String nomefantasia) {
		EstabProvider.nomefantasia = nomefantasia;
	}

	public static String getSistemaTrabalho() {
		return sistemaTrabalho;
	}

}
